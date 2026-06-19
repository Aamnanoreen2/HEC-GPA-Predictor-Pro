package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.entity.*
import com.example.data.network.GeminiClient
import com.example.data.repository.AcademicRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AcademicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AcademicRepository
    private val geminiClient = GeminiClient()

    // Database states
    val availableUniversities: StateFlow<List<UniversityScale>>
    val semesters: StateFlow<List<Semester>>
    val allSubjects: StateFlow<List<Subject>>
    val academicGoal: StateFlow<AcademicGoal?>
    val chatMessages: StateFlow<List<AdvisorMessage>>

    // UI State Holders
    private val _selectedUniversity = MutableStateFlow("HEC Pakistan (Standard)")
    val selectedUniversity: StateFlow<String> = _selectedUniversity.asStateFlow()

    private val _gradeMappings = MutableStateFlow<List<GradeMapping>>(emptyList())
    val gradeMappings: StateFlow<List<GradeMapping>> = _gradeMappings.asStateFlow()

    private val _isAdvisorThinking = MutableStateFlow(false)
    val isAdvisorThinking: StateFlow<Boolean> = _isAdvisorThinking.asStateFlow()

    private val sharedPrefs = application.getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE)
    private val _themePreference = MutableStateFlow(sharedPrefs.getInt("theme_preference", 0)) // 0: System, 1: Light, 2: Dark
    val themePreference: StateFlow<Int> = _themePreference.asStateFlow()

    fun setThemePreference(pref: Int) {
        _themePreference.value = pref
        sharedPrefs.edit().putInt("theme_preference", pref).apply()
    }

    init {
        val database = AppDatabase.getDatabase(application)
        repository = AcademicRepository(database.academicDao())

        availableUniversities = repository.allUniversities
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        semesters = repository.allSemesters
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allSubjects = repository.allSubjects
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        academicGoal = repository.academicGoal
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

        chatMessages = repository.allMessages
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // Launch initialization and reload mappings
        viewModelScope.launch {
            repository.initializeDefaultTemplatesIfNeeded()
            loadGradeMappings(_selectedUniversity.value)
        }
    }

    fun selectUniversity(name: String) {
        _selectedUniversity.value = name
        viewModelScope.launch {
            loadGradeMappings(name)
        }
    }

    private suspend fun loadGradeMappings(univName: String) {
        val mappings = repository.getGradeMappingsForUnivSync(univName)
        _gradeMappings.value = mappings
    }

    fun insertCustomUniversity(name: String, grades: List<Pair<String, Double>>) {
        viewModelScope.launch {
            val mappings = grades.map { (grade, gpa) ->
                GradeMapping(universityName = name, grade = grade, gpa = gpa)
            }
            repository.insertCustomUniversity(name, mappings)
            selectUniversity(name)
        }
    }

    fun deleteUniversity(name: String) {
        viewModelScope.launch {
            repository.deleteUniversityScale(name)
            selectUniversity("HEC Pakistan (Standard)")
        }
    }

    // Semester and course planning
    fun addSemester(name: String, isCompleted: Boolean = false) {
        viewModelScope.launch {
            repository.addSemester(name, isCompleted)
        }
    }

    fun deleteSemester(semesterId: Int) {
        viewModelScope.launch {
            repository.deleteSemesterAndData(semesterId)
        }
    }

    fun addSubject(semesterId: Int, name: String, credits: Int, grade: String) {
        viewModelScope.launch {
            val gpaValue = _gradeMappings.value.find { it.grade.equals(grade, ignoreCase = true) }?.gpa ?: 0.0
            repository.addSubject(semesterId, name, credits, grade, gpaValue)
        }
    }

    fun deleteSubject(subjectId: Int) {
        viewModelScope.launch {
            repository.deleteSubject(subjectId)
        }
    }

    fun updateSubjectDetails(subjectId: Int, semesterId: Int, name: String, credits: Int, grade: String) {
        viewModelScope.launch {
            val gpaValue = _gradeMappings.value.find { it.grade.equals(grade, ignoreCase = true) }?.gpa ?: 0.0
            repository.updateSubject(com.example.data.entity.Subject(id = subjectId, semesterId = semesterId, name = name, creditHours = credits, grade = grade, gpa = gpaValue))
        }
    }

    // Goals settings
    fun saveAcademicGoal(targetGpa: Double, totalCredits: Int) {
        viewModelScope.launch {
            // Get computed current statistics to sync the goal fields
            val computedStats = calculateAcademicStats()
            repository.updateGoal(
                targetCgpa = targetGpa,
                currentCgpa = computedStats.completedCgpa,
                completedCredits = computedStats.completedCredits,
                totalCreditsRequired = totalCredits
            )
        }
    }

    // Calculator helper stats class
    data class AcademicStats(
        val completedCgpa: Double = 0.0,
        val completedCredits: Int = 0,
        val predictedCgpa: Double = 0.0,
        val totalCredits: Int = 0,
        val completedSemestersCount: Int = 0,
        val plannedSemestersCount: Int = 0,
        val semesterGpas: Map<Int, Double> = emptyMap(),
        val highestGpaSemester: Pair<String, Double> = "" to 0.0,
        val lowestGpaSemester: Pair<String, Double> = "" to 0.0,
        val academicGrowth: Double = 0.0
    )

    fun calculateAcademicStats(): AcademicStats {
        val currentSemesters = semesters.value
        val currentSubjects = allSubjects.value

        if (currentSemesters.isEmpty()) return AcademicStats()

        var totalCompletedPoints = 0.0
        var totalCompletedCredits = 0

        var totalAllPoints = 0.0
        var totalAllCredits = 0

        val semesterGpas = mutableMapOf<Int, Double>()
        var completedSemCount = 0
        var plannedSemCount = 0

        val semesterDetails = mutableListOf<Triple<Int, String, Double>>()

        currentSemesters.forEach { sem ->
            val subjectsInSem = currentSubjects.filter { it.semesterId == sem.id }
            val semCredits = subjectsInSem.sumOf { it.creditHours }
            val semPoints = subjectsInSem.sumOf { it.creditHours * it.gpa }
            val semGpa = if (semCredits > 0) semPoints / semCredits else 0.0

            semesterGpas[sem.id] = semGpa

            if (semGpa > 0.0) {
                semesterDetails.add(Triple(sem.id, sem.name, semGpa))
            }

            if (sem.isCompleted) {
                totalCompletedPoints += semPoints
                totalCompletedCredits += semCredits
                completedSemCount++
            } else {
                plannedSemCount++
            }

            totalAllPoints += semPoints
            totalAllCredits += semCredits
        }

        val completedCgpa = if (totalCompletedCredits > 0) totalCompletedPoints / totalCompletedCredits else 0.0
        val predictedCgpa = if (totalAllCredits > 0) totalAllPoints / totalAllCredits else 0.0

        val highestGpaSem = semesterDetails.maxByOrNull { it.third }?.let { it.second to it.third } ?: ("N/A" to 0.0)
        val lowestGpaSem = semesterDetails.minByOrNull { it.third }?.let { it.second to it.third } ?: ("N/A" to 0.0)

        // Academic Growth: difference between last recorded GPA and first recorded GPA, as a percentage
        val academicGrowth = if (semesterDetails.size >= 2) {
            val firstGpa = semesterDetails.first().third
            val lastGpa = semesterDetails.last().third
            if (firstGpa > 0.0) ((lastGpa - firstGpa) / firstGpa) * 100.0 else 0.0
        } else {
            0.0
        }

        return AcademicStats(
            completedCgpa = completedCgpa,
            completedCredits = totalCompletedCredits,
            predictedCgpa = predictedCgpa,
            totalCredits = totalAllCredits,
            completedSemestersCount = completedSemCount,
            plannedSemestersCount = plannedSemCount,
            semesterGpas = semesterGpas,
            highestGpaSemester = highestGpaSem,
            lowestGpaSemester = lowestGpaSem,
            academicGrowth = academicGrowth
        )
    }

    // AI Academic Advisor interactions
    fun askAdvisor(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            // Save user message to database
            repository.addMessage(sender = "user", content = query)
            _isAdvisorThinking.value = true

            // Build student's academic profile summary context for Gemini
            val stats = calculateAcademicStats()
            val goal = academicGoal.value
            val contextSummary = """
                Selected University Scheme: ${_selectedUniversity.value}
                Current CGPA (Completed): ${"%.2f".format(stats.completedCgpa)}
                Completed Credits: ${stats.completedCredits}
                Predicted CGPA (with current plans): ${"%.2f".format(stats.predictedCgpa)}
                Target Graduation CGPA: ${goal?.targetCgpa ?: 3.5}
                Total Required Credits for Graduation: ${goal?.totalCreditsRequired ?: 130}
            """.trimIndent()

            // Fetch chat history for multi-turn guidelines
            val pastMessages = chatMessages.value.takeLast(10).map { it.sender to it.content }

            // Query advisor response
            val reply = geminiClient.generateAdvisorAdvice(
                prompt = query,
                chatHistory = pastMessages,
                userAcademicContext = contextSummary
            )

            // Save reply to database
            repository.addMessage(sender = "advisor", content = reply)
            _isAdvisorThinking.value = false
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }
}
