package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "university_scales")
data class UniversityScale(
    @PrimaryKey val universityName: String,
    val isCustom: Boolean = false
)

@Entity(tableName = "grade_mappings")
data class GradeMapping(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val universityName: String,
    val grade: String,
    val gpa: Double
)

@Entity(tableName = "semesters")
data class Semester(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val displayOrder: Int = 0,
    val isCompleted: Boolean = false
)

@Entity(tableName = "subjects")
data class Subject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val semesterId: Int,
    val name: String,
    val creditHours: Int,
    val grade: String,
    val gpa: Double
)

@Entity(tableName = "academic_goals")
data class AcademicGoal(
    @PrimaryKey val id: Int = 1, // Single active configuration
    val targetCgpa: Double = 3.5,
    val currentCgpa: Double = 3.0,
    val completedCredits: Int = 0,
    val totalCreditsRequired: Int = 130
)

@Entity(tableName = "advisor_messages")
data class AdvisorMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String, // "user" or "advisor"
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
