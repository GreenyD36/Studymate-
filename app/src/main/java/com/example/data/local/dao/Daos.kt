package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.FlashcardEntity
import com.example.data.local.entities.LanguageProgressEntity
import com.example.data.local.entities.NoteEntity
import com.example.data.local.entities.QuizHistoryEntity
import com.example.data.local.entities.StudyPlanEntity
import com.example.data.local.entities.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET xp = xp + :points WHERE id = 1")
    suspend fun addXp(points: Int)

    @Query("UPDATE user_profile SET streak = :newStreak WHERE id = 1")
    suspend fun updateStreak(newStreak: Int)

    @Query("UPDATE user_profile SET isPremium = :isPremium WHERE id = 1")
    suspend fun setPremiumStatus(isPremium: Boolean)

    @Query("UPDATE user_profile SET interfaceLanguage = :language WHERE id = 1")
    suspend fun updateInterfaceLanguage(language: String)

    @Query("UPDATE user_profile SET educationLevel = :level WHERE id = 1")
    suspend fun updateEducationLevel(level: String)
}

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Long): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: Long)
}

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcards ORDER BY id DESC")
    fun getAllFlashcards(): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards WHERE category = :category")
    fun getFlashcardsByCategory(category: String): Flow<List<FlashcardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: FlashcardEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcards(flashcards: List<FlashcardEntity>)

    @Update
    suspend fun updateFlashcard(flashcard: FlashcardEntity)

    @Query("DELETE FROM flashcards WHERE id = :id")
    suspend fun deleteFlashcard(id: Long)
}

@Dao
interface QuizDao {
    @Query("SELECT * FROM quiz_history ORDER BY dateMillis DESC")
    fun getAllQuizHistory(): Flow<List<QuizHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: QuizHistoryEntity): Long
}

@Dao
interface StudyPlanDao {
    @Query("SELECT * FROM study_plans ORDER BY dayNumber ASC")
    fun getAllPlans(): Flow<List<StudyPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlans(plans: List<StudyPlanEntity>)

    @Query("UPDATE study_plans SET isCompleted = :completed WHERE id = :id")
    suspend fun toggleCompleted(id: Long, completed: Boolean)

    @Query("DELETE FROM study_plans")
    suspend fun clearPlans()
}

@Dao
interface LanguageProgressDao {
    @Query("SELECT * FROM language_progress")
    fun getAllProgress(): Flow<List<LanguageProgressEntity>>

    @Query("SELECT * FROM language_progress WHERE languageCode = :code LIMIT 1")
    suspend fun getProgressForLanguage(code: String): LanguageProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: LanguageProgressEntity)
}
