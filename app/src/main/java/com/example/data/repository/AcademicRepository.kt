package com.example.data.repository

import com.example.data.dao.AcademicDao
import com.example.data.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class AcademicRepository(private val dao: AcademicDao) {

    val allUniversities: Flow<List<UniversityScale>> = dao.getAllUniversities()
    val allSemesters: Flow<List<Semester>> = dao.getAllSemesters()
    val allSubjects: Flow<List<Subject>> = dao.getAllSubjects()
    val academicGoal: Flow<AcademicGoal?> = dao.getAcademicGoal()
    val allMessages: Flow<List<AdvisorMessage>> = dao.getAllMessages()

    suspend fun initializeDefaultTemplatesIfNeeded() {
        val currentUniversities = dao.getAllUniversitiesSync()
        if (currentUniversities.isEmpty()) {
            // Seed HEC default
            val hecName = "HEC Pakistan (Standard)"
            dao.insertUniversity(UniversityScale(hecName, isCustom = false))
            dao.insertGradeMappings(listOf(
                GradeMapping(universityName = hecName, grade = "A", gpa = 4.00),
                GradeMapping(universityName = hecName, grade = "A-", gpa = 3.67),
                GradeMapping(universityName = hecName, grade = "B+", gpa = 3.33),
                GradeMapping(universityName = hecName, grade = "B", gpa = 3.00),
                GradeMapping(universityName = hecName, grade = "B-", gpa = 2.67),
                GradeMapping(universityName = hecName, grade = "C+", gpa = 2.33),
                GradeMapping(universityName = hecName, grade = "C", gpa = 2.00),
                GradeMapping(universityName = hecName, grade = "C-", gpa = 1.67),
                GradeMapping(universityName = hecName, grade = "D+", gpa = 1.33),
                GradeMapping(universityName = hecName, grade = "D", gpa = 1.00),
                GradeMapping(universityName = hecName, grade = "F", gpa = 0.00)
            ))

            // Seed FAST-NUCES default (Similar to HEC but customized)
            val fastName = "FAST-NUCES"
            dao.insertUniversity(UniversityScale(fastName, isCustom = false))
            dao.insertGradeMappings(listOf(
                GradeMapping(universityName = fastName, grade = "A", gpa = 4.00),
                GradeMapping(universityName = fastName, grade = "A-", gpa = 3.67),
                GradeMapping(universityName = fastName, grade = "B+", gpa = 3.33),
                GradeMapping(universityName = fastName, grade = "B", gpa = 3.00),
                GradeMapping(universityName = fastName, grade = "B-", gpa = 2.67),
                GradeMapping(universityName = fastName, grade = "C+", gpa = 2.33),
                GradeMapping(universityName = fastName, grade = "C", gpa = 2.00),
                GradeMapping(universityName = fastName, grade = "C-", gpa = 1.67),
                GradeMapping(universityName = fastName, grade = "D+", gpa = 1.33),
                GradeMapping(universityName = fastName, grade = "D", gpa = 1.00),
                GradeMapping(universityName = fastName, grade = "F", gpa = 0.00)
            ))

            // Seed COMSATS Scale
            val comsatsName = "COMSATS"
            dao.insertUniversity(UniversityScale(comsatsName, isCustom = false))
            dao.insertGradeMappings(listOf(
                GradeMapping(universityName = comsatsName, grade = "A", gpa = 4.0),
                GradeMapping(universityName = comsatsName, grade = "A-", gpa = 3.7),
                GradeMapping(universityName = comsatsName, grade = "B+", gpa = 3.4),
                GradeMapping(universityName = comsatsName, grade = "B", gpa = 3.0),
                GradeMapping(universityName = comsatsName, grade = "B-", gpa = 2.7),
                GradeMapping(universityName = comsatsName, grade = "C+", gpa = 2.4),
                GradeMapping(universityName = comsatsName, grade = "C", gpa = 2.0),
                GradeMapping(universityName = comsatsName, grade = "C-", gpa = 1.7),
                GradeMapping(universityName = comsatsName, grade = "D", gpa = 1.3),
                GradeMapping(universityName = comsatsName, grade = "F", gpa = 0.0)
            ))

            // Seed NUST Scale
            val nustName = "NUST"
            dao.insertUniversity(UniversityScale(nustName, isCustom = false))
            dao.insertGradeMappings(listOf(
                GradeMapping(universityName = nustName, grade = "A", gpa = 4.0),
                GradeMapping(universityName = nustName, grade = "B+", gpa = 3.5),
                GradeMapping(universityName = nustName, grade = "B", gpa = 3.0),
                GradeMapping(universityName = nustName, grade = "C+", gpa = 2.5),
                GradeMapping(universityName = nustName, grade = "C", gpa = 2.0),
                GradeMapping(universityName = nustName, grade = "D", gpa = 1.0),
                GradeMapping(universityName = nustName, grade = "F", gpa = 0.0)
            ))

            // Seed Punjab University Scale
            val puName = "Punjab University (PU)"
            dao.insertUniversity(UniversityScale(puName, isCustom = false))
            dao.insertGradeMappings(listOf(
                GradeMapping(universityName = puName, grade = "A", gpa = 4.00),
                GradeMapping(universityName = puName, grade = "A-", gpa = 3.70),
                GradeMapping(universityName = puName, grade = "B+", gpa = 3.40),
                GradeMapping(universityName = puName, grade = "B", gpa = 3.00),
                GradeMapping(universityName = puName, grade = "B-", gpa = 2.70),
                GradeMapping(universityName = puName, grade = "C+", gpa = 2.40),
                GradeMapping(universityName = puName, grade = "C", gpa = 2.00),
                GradeMapping(universityName = puName, grade = "C-", gpa = 1.70),
                GradeMapping(universityName = puName, grade = "D", gpa = 1.00),
                GradeMapping(universityName = puName, grade = "F", gpa = 0.00)
            ))

            // Seed UET Scale
            val uetName = "UET Lahore"
            dao.insertUniversity(UniversityScale(uetName, isCustom = false))
            dao.insertGradeMappings(listOf(
                GradeMapping(universityName = uetName, grade = "A", gpa = 4.0),
                GradeMapping(universityName = uetName, grade = "A-", gpa = 3.7),
                GradeMapping(universityName = uetName, grade = "B+", gpa = 3.3),
                GradeMapping(universityName = uetName, grade = "B", gpa = 3.0),
                GradeMapping(universityName = uetName, grade = "B-", gpa = 2.7),
                GradeMapping(universityName = uetName, grade = "C+", gpa = 2.3),
                GradeMapping(universityName = uetName, grade = "C", gpa = 2.0),
                GradeMapping(universityName = uetName, grade = "C-", gpa = 1.7),
                GradeMapping(universityName = uetName, grade = "D", gpa = 1.0),
                GradeMapping(universityName = uetName, grade = "F", gpa = 0.0)
            ))

            // Seed Bahria University Scale
            val bahriaName = "Bahria University"
            dao.insertUniversity(UniversityScale(bahriaName, isCustom = false))
            dao.insertGradeMappings(listOf(
                GradeMapping(universityName = bahriaName, grade = "A", gpa = 4.0),
                GradeMapping(universityName = bahriaName, grade = "A-", gpa = 3.67),
                GradeMapping(universityName = bahriaName, grade = "B+", gpa = 3.33),
                GradeMapping(universityName = bahriaName, grade = "B", gpa = 3.0),
                GradeMapping(universityName = bahriaName, grade = "B-", gpa = 2.67),
                GradeMapping(universityName = bahriaName, grade = "C+", gpa = 2.33),
                GradeMapping(universityName = bahriaName, grade = "C", gpa = 2.0),
                GradeMapping(universityName = bahriaName, grade = "C-", gpa = 1.67),
                GradeMapping(universityName = bahriaName, grade = "D+", gpa = 1.33),
                GradeMapping(universityName = bahriaName, grade = "D", gpa = 1.0),
                GradeMapping(universityName = bahriaName, grade = "F", gpa = 0.0)
            ))

            // Seed default academic goals
            dao.insertAcademicGoal(AcademicGoal())

            // Seed default semesters with sample credits completed (or first blank semester)
            dao.insertSemester(Semester(name = "1st Semester", displayOrder = 1, isCompleted = true))
            dao.insertSemester(Semester(name = "2nd Semester", displayOrder = 2, isCompleted = false))
        }
    }

    fun getGradeMappingsForUniv(univName: String): Flow<List<GradeMapping>> {
        return dao.getGradeMappingsForUniv(univName)
    }

    suspend fun getGradeMappingsForUnivSync(univName: String): List<GradeMapping> {
        return dao.getGradeMappingsForUnivSync(univName)
    }

    suspend fun insertCustomUniversity(univName: String, mappings: List<GradeMapping>) {
        dao.insertUniversity(UniversityScale(univName, isCustom = true))
        dao.deleteGradeMappings(univName)
        dao.insertGradeMappings(mappings.map { it.copy(universityName = univName) })
    }

    suspend fun deleteUniversityScale(univName: String) {
        dao.deleteUniversity(univName)
        dao.deleteGradeMappings(univName)
    }

    fun getSubjectsForSemester(semesterId: Int): Flow<List<Subject>> {
        return dao.getSubjectsForSemester(semesterId)
    }

    suspend fun addSemester(name: String, isCompleted: Boolean = false): Long {
        val currentMax = dao.getAllSemesters().first().maxByOrNull { it.displayOrder }?.displayOrder ?: 0
        return dao.insertSemester(Semester(name = name, displayOrder = currentMax + 1, isCompleted = isCompleted))
    }

    suspend fun deleteSemesterAndData(semesterId: Int) {
        dao.deleteSemester(semesterId)
        dao.deleteSubjectsForSemester(semesterId)
    }

    suspend fun addSubject(semesterId: Int, name: String, credits: Int, grade: String, gpa: Double) {
        dao.insertSubject(Subject(semesterId = semesterId, name = name, creditHours = credits, grade = grade, gpa = gpa))
    }

    suspend fun updateSubject(subject: Subject) {
        dao.updateSubject(subject)
    }

    suspend fun deleteSubject(subjectId: Int) {
        dao.deleteSubject(subjectId)
    }

    suspend fun updateGoal(targetCgpa: Double, currentCgpa: Double, completedCredits: Int, totalCreditsRequired: Int) {
        dao.insertAcademicGoal(
            AcademicGoal(
                id = 1,
                targetCgpa = targetCgpa,
                currentCgpa = currentCgpa,
                completedCredits = completedCredits,
                totalCreditsRequired = totalCreditsRequired
            )
        )
    }

    suspend fun addMessage(sender: String, content: String): Long {
        return dao.insertMessage(AdvisorMessage(sender = sender, content = content))
    }

    suspend fun clearChat() {
        dao.clearMessages()
    }
}
