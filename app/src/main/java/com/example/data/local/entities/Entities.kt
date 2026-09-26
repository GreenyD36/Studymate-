package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Danos",
    val email: String = "student@studymate.ai",
    val educationLevel: String = "University",
    val interfaceLanguage: String = "English",
    val learningGoals: String = "Academic Study, Exam Preparation, Learn Languages",
    val xp: Int = 340,
    val streak: Int = 7,
    val level: Int = 3,
    val isPremium: Boolean = false,
    val completedOnboarding: Boolean = true
)

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val subject: String,
    val summary: String = "",
    val keyPoints: String = "", // Comma or bullet separated
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val noteId: Long? = null,
    val category: String,
    val question: String,
    val answer: String,
    val isMastered: Boolean = false,
    val reviewCount: Int = 0
)

@Entity(tableName = "quiz_history")
data class QuizHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String,
    val topic: String,
    val difficulty: String,
    val score: Int,
    val totalQuestions: Int,
    val xpEarned: Int,
    val dateMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "study_plans")
data class StudyPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String,
    val examTitle: String,
    val examDateMillis: Long,
    val dayNumber: Int,
    val dayTitle: String,
    val taskDescription: String,
    val isCompleted: Boolean = false
)

@Entity(tableName = "language_progress")
data class LanguageProgressEntity(
    @PrimaryKey val languageCode: String, // en, fr, rw, es, ko, zh, pt
    val wordsLearned: Int = 0,
    val lessonsCompleted: Int = 0,
    val speakingScore: Int = 0
)
