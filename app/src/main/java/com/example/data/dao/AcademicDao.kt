package com.example.data.dao

import androidx.room.*
import com.example.data.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AcademicDao {
    // University scale
    @Query("SELECT * FROM university_scales")
    fun getAllUniversities(): Flow<List<UniversityScale>>

    @Query("SELECT * FROM university_scales")
    suspend fun getAllUniversitiesSync(): List<UniversityScale>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUniversity(scale: UniversityScale)

    // Grade mappings
    @Query("SELECT * FROM grade_mappings WHERE universityName = :univName")
    fun getGradeMappingsForUniv(univName: String): Flow<List<GradeMapping>>

    @Query("SELECT * FROM grade_mappings WHERE universityName = :univName")
    suspend fun getGradeMappingsForUnivSync(univName: String): List<GradeMapping>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGradeMappings(mappings: List<GradeMapping>)

    @Query("DELETE FROM grade_mappings WHERE universityName = :univName")
    suspend fun deleteGradeMappings(univName: String)

    @Query("DELETE FROM university_scales WHERE universityName = :univName")
    suspend fun deleteUniversity(univName: String)

    // Semesters
    @Query("SELECT * FROM semesters ORDER BY displayOrder ASC")
    fun getAllSemesters(): Flow<List<Semester>>

    @Query("SELECT * FROM semesters WHERE id = :id")
    suspend fun getSemesterById(id: Int): Semester?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSemester(semester: Semester): Long

    @Query("DELETE FROM semesters WHERE id = :semesterId")
    suspend fun deleteSemester(semesterId: Int)

    // Subjects
    @Query("SELECT * FROM subjects WHERE semesterId = :semesterId")
    fun getSubjectsForSemester(semesterId: Int): Flow<List<Subject>>

    @Query("SELECT * FROM subjects")
    fun getAllSubjects(): Flow<List<Subject>>

    @Query("SELECT * FROM subjects")
    suspend fun getAllSubjectsSync(): List<Subject>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: Subject): Long

    @Update
    suspend fun updateSubject(subject: Subject)

    @Query("DELETE FROM subjects WHERE id = :subjectId")
    suspend fun deleteSubject(subjectId: Int)

    @Query("DELETE FROM subjects WHERE semesterId = :semesterId")
    suspend fun deleteSubjectsForSemester(semesterId: Int)

    // Academic Goals
    @Query("SELECT * FROM academic_goals WHERE id = 1")
    fun getAcademicGoal(): Flow<AcademicGoal?>

    @Query("SELECT * FROM academic_goals WHERE id = 1")
    suspend fun getAcademicGoalSync(): AcademicGoal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAcademicGoal(goal: AcademicGoal)

    // Advisor messages
    @Query("SELECT * FROM advisor_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<AdvisorMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(msg: AdvisorMessage): Long

    @Query("DELETE FROM advisor_messages")
    suspend fun clearMessages()
}
