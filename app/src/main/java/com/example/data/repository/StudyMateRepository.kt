package com.example.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.example.data.local.AppDatabase
import com.example.data.local.entities.FlashcardEntity
import com.example.data.local.entities.LanguageProgressEntity
import com.example.data.local.entities.NoteEntity
import com.example.data.local.entities.QuizHistoryEntity
import com.example.data.local.entities.StudyPlanEntity
import com.example.data.local.entities.UserProfileEntity
import com.example.data.remote.GeminiService
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StudyMateRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val userDao = db.userDao()
    private val noteDao = db.noteDao()
    private val flashcardDao = db.flashcardDao()
    private val quizDao = db.quizDao()
    private val studyPlanDao = db.studyPlanDao()
    private val languageProgressDao = db.languageProgressDao()

    // User Profile
    val userProfile: Flow<UserProfileEntity?> = userDao.getUserProfile()

    suspend fun saveUserProfile(profile: UserProfileEntity) {
        userDao.saveUserProfile(profile)
    }

    suspend fun addXp(points: Int) {
        userDao.addXp(points)
    }

    suspend fun setPremiumStatus(isPremium: Boolean) {
        userDao.setPremiumStatus(isPremium)
    }

    suspend fun updateInterfaceLanguage(lang: String) {
        userDao.updateInterfaceLanguage(lang)
    }

    suspend fun updateEducationLevel(level: String) {
        userDao.updateEducationLevel(level)
    }

    // Notes
    val allNotes: Flow<List<NoteEntity>> = noteDao.getAllNotes()

    suspend fun createNote(title: String, content: String, subject: String): Long {
        return noteDao.insertNote(
            NoteEntity(
                title = title,
                content = content,
                subject = subject
            )
        )
    }

    suspend fun updateNote(note: NoteEntity) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(id: Long) {
        noteDao.deleteNote(id)
    }

    suspend fun summarizeNoteWithAi(note: NoteEntity): NoteEntity {
        val prompt = "Provide a concise educational summary and 4 bullet key points for the following study notes:\n\nTitle: ${note.title}\nSubject: ${note.subject}\nContent:\n${note.content}"
        val aiResponse = GeminiService.generateEducationalResponse(prompt)

        val updated = note.copy(
            summary = "Summary: " + aiResponse.lines().take(4).joinToString(" "),
            keyPoints = "• " + aiResponse.lines().drop(4).take(4).joinToString("\n• ")
        )
        noteDao.updateNote(updated)
        addXp(25)
        return updated
    }

    suspend fun generateFlashcardsFromNote(note: NoteEntity): List<FlashcardEntity> {
        val prompt = "Create 3 high-yield study flashcards from the note: '${note.title}'. For each flashcard, output: Q: [Question] and A: [Answer]."
        val response = GeminiService.generateEducationalResponse(prompt)

        val cards = mutableListOf<FlashcardEntity>()
        val lines = response.lines()
        var currentQ = ""

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.startsWith("Q:") || trimmed.startsWith("Question:")) {
                currentQ = trimmed.substringAfter(":").trim()
            } else if ((trimmed.startsWith("A:") || trimmed.startsWith("Answer:")) && currentQ.isNotEmpty()) {
                val ans = trimmed.substringAfter(":").trim()
                cards.add(
                    FlashcardEntity(
                        noteId = note.id,
                        category = note.subject,
                        question = currentQ,
                        answer = ans
                    )
                )
                currentQ = ""
            }
        }

        if (cards.isEmpty()) {
            cards.add(
                FlashcardEntity(
                    noteId = note.id,
                    category = note.subject,
                    question = "What is the key takeaway of ${note.title}?",
                    answer = if (note.summary.isNotEmpty()) note.summary else note.content.take(120)
                )
            )
        }

        flashcardDao.insertFlashcards(cards)
        addXp(30)
        return cards
    }

    // Flashcards
    val allFlashcards: Flow<List<FlashcardEntity>> = flashcardDao.getAllFlashcards()

    suspend fun toggleFlashcardMastery(card: FlashcardEntity) {
        flashcardDao.updateFlashcard(
            card.copy(
                isMastered = !card.isMastered,
                reviewCount = card.reviewCount + 1
            )
        )
        if (!card.isMastered) {
            addXp(15)
        }
    }

    suspend fun addFlashcard(category: String, question: String, answer: String) {
        flashcardDao.insertFlashcard(
            FlashcardEntity(
                category = category,
                question = question,
                answer = answer
            )
        )
    }

    suspend fun deleteFlashcard(id: Long) {
        flashcardDao.deleteFlashcard(id)
    }

    // Quiz
    val quizHistory: Flow<List<QuizHistoryEntity>> = quizDao.getAllQuizHistory()

    suspend fun recordQuizResult(
        subject: String,
        topic: String,
        difficulty: String,
        score: Int,
        totalQuestions: Int
    ) {
        val xp = score * 10
        quizDao.insertQuizResult(
            QuizHistoryEntity(
                subject = subject,
                topic = topic,
                difficulty = difficulty,
                score = score,
                totalQuestions = totalQuestions,
                xpEarned = xp
            )
        )
        addXp(xp)
    }

    // Study Planner
    val allPlans: Flow<List<StudyPlanEntity>> = studyPlanDao.getAllPlans()

    suspend fun generateStudyPlan(
        subject: String,
        examTitle: String,
        examDays: Int,
        dailyHours: Double,
        topics: String
    ): List<StudyPlanEntity> {
        val topicList = topics.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            .ifEmpty { listOf("Fundamentals & Definitions", "Core Principles & Formulas", "Worked Problems", "Comprehensive Revision") }

        val plans = mutableListOf<StudyPlanEntity>()
        val examDateMillis = System.currentTimeMillis() + (examDays * 24L * 3600 * 1000)

        for (day in 1..examDays) {
            val assignedTopic = topicList[(day - 1) % topicList.size]
            val desc = "Spend $dailyHours hours focusing on $assignedTopic. Practice 10 practice questions and create 5 flashcards."
            plans.add(
                StudyPlanEntity(
                    subject = subject,
                    examTitle = examTitle,
                    examDateMillis = examDateMillis,
                    dayNumber = day,
                    dayTitle = "Day $day — $assignedTopic",
                    taskDescription = desc,
                    isCompleted = false
                )
            )
        }

        studyPlanDao.clearPlans()
        studyPlanDao.insertPlans(plans)
        addXp(40)
        return plans
    }

    suspend fun toggleStudyPlanTask(id: Long, completed: Boolean) {
        studyPlanDao.toggleCompleted(id, completed)
        if (completed) {
            addXp(20)
        }
    }

    // Language Progress
    val languageProgress: Flow<List<LanguageProgressEntity>> = languageProgressDao.getAllProgress()

    suspend fun recordLessonCompletion(languageCode: String, wordsIncrement: Int) {
        val existing = languageProgressDao.getProgressForLanguage(languageCode)
        val updated = if (existing != null) {
            existing.copy(
                wordsLearned = existing.wordsLearned + wordsIncrement,
                lessonsCompleted = existing.lessonsCompleted + 1
            )
        } else {
            LanguageProgressEntity(
                languageCode = languageCode,
                wordsLearned = wordsIncrement,
                lessonsCompleted = 1,
                speakingScore = 75
            )
        }
        languageProgressDao.saveProgress(updated)
        addXp(50)
    }

    // Question Scanner
    suspend fun analyzeQuestion(
        questionText: String,
        imageBitmap: Bitmap? = null
    ): String {
        val prompt = """
Analyze the following educational question using the StudyMate Learning-First Answer System.
Question: "$questionText"

Structure the output into 5 explicit sections:
1. ### Understanding: What is this question asking? What are the given values?
2. ### Method: Which concept, law, or formula should be used and why?
3. ### Steps: Step-by-step mathematical or logical calculation.
4. ### Answer: Clear final statement of the solution.
5. ### Practice Question: Provide one similar problem with a helpful tip so the student can verify their own comprehension.
Do not just provide a raw answer. Make it pedagogical, encouraging, and clear.
        """.trimIndent()

        return GeminiService.generateEducationalResponse(prompt, imageBitmap = imageBitmap)
    }

    // AI Tutor
    suspend fun askTutor(
        userMessage: String,
        mode: String,
        educationLevel: String,
        subject: String
    ): String {
        val systemPrompt = """
You are StudyMate, an intelligent, empathetic, world-class AI educational tutor for $educationLevel students.
Current Subject: $subject
Active Tutoring Mode: $mode.
- Teacher Mode: Explain concepts clearly step-by-step with intuitive real-world analogies.
- Quiz Master: Generate engaging test questions and evaluate the user's answers.
- Writing Coach: Help polish writing, structure arguments, and fix grammatical mistakes.
- Subject Tutor: Deep academic STEM and humanities expertise.
- Language Tutor: Focus on pronunciation, vocabulary, and grammar rules.
- Conversation Partner: Speak naturally in character to simulate authentic dialogue.

Always encourage critical thinking and never encourage academic dishonesty.
        """.trimIndent()

        return GeminiService.generateEducationalResponse(userMessage, systemInstruction = systemPrompt)
    }
}
