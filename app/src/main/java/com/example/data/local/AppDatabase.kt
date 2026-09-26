package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.FlashcardDao
import com.example.data.local.dao.LanguageProgressDao
import com.example.data.local.dao.NoteDao
import com.example.data.local.dao.QuizDao
import com.example.data.local.dao.StudyPlanDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entities.FlashcardEntity
import com.example.data.local.entities.LanguageProgressEntity
import com.example.data.local.entities.NoteEntity
import com.example.data.local.entities.QuizHistoryEntity
import com.example.data.local.entities.StudyPlanEntity
import com.example.data.local.entities.UserProfileEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfileEntity::class,
        NoteEntity::class,
        FlashcardEntity::class,
        QuizHistoryEntity::class,
        StudyPlanEntity::class,
        LanguageProgressEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun noteDao(): NoteDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun quizDao(): QuizDao
    abstract fun studyPlanDao(): StudyPlanDao
    abstract fun languageProgressDao(): LanguageProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "studymate_db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed initial mock data so the user has immediate rich content to explore
                        CoroutineScope(Dispatchers.IO).launch {
                            val dbInstance = INSTANCE ?: getDatabase(context)
                            seedInitialData(dbInstance)
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun seedInitialData(database: AppDatabase) {
            // Seed user profile
            database.userDao().saveUserProfile(
                UserProfileEntity(
                    id = 1,
                    name = "Danos",
                    email = "danos@studymate.ai",
                    educationLevel = "University",
                    interfaceLanguage = "English",
                    learningGoals = "Academic Study, Exam Preparation, Learn Languages",
                    xp = 520,
                    streak = 7,
                    level = 3,
                    isPremium = false,
                    completedOnboarding = true
                )
            )

            // Seed initial study notes
            val note1Id = database.noteDao().insertNote(
                NoteEntity(
                    title = "Cellular Respiration & ATP Cycle",
                    subject = "Biology",
                    content = "Cellular respiration is the biochemical pathway by which cells release energy stored in the chemical bonds of glucose and synthesize ATP. The process occurs in three main stages: Glycolysis (in cytoplasm, produces 2 ATP & 2 NADH), Krebs Cycle (in mitochondrial matrix, produces 2 ATP, 6 NADH, 2 FADH2), and Oxidative Phosphorylation (along the inner mitochondrial membrane, producing up to 28-32 ATP via ATP synthase). Oxygen acts as the final electron acceptor, forming water as a byproduct.",
                    summary = "Cellular respiration converts glucose into usable ATP energy via Glycolysis, Krebs Cycle, and Electron Transport Chain, yielding 30-32 ATP total.",
                    keyPoints = "• Glycolysis occurs in cytoplasm (anaerobic).\n• Krebs cycle happens in mitochondrial matrix.\n• ETC produces the bulk of ATP.\n• Oxygen is the terminal electron acceptor."
                )
            )

            database.noteDao().insertNote(
                NoteEntity(
                    title = "Newton's Laws of Motion & Momentum",
                    subject = "Physics",
                    content = "1. First Law (Inertia): An object remains at rest or uniform motion unless acted upon by a net external force.\n2. Second Law (F = ma): The acceleration of an object is directly proportional to net force and inversely proportional to mass.\n3. Third Law (Action-Reaction): For every action, there is an equal and opposite reaction force.\nMomentum (p = mv) is conserved in isolated systems.",
                    summary = "Newton formulated three fundamental laws describing motion, force, inertia, and momentum conservation.",
                    keyPoints = "• Law 1: Inertia prevents sudden velocity changes.\n• Law 2: Force equals mass times acceleration (F=ma).\n• Law 3: Forces always occur in matched pairs.\n• Total momentum is conserved in collisions."
                )
            )

            // Seed initial flashcards
            database.flashcardDao().insertFlashcards(
                listOf(
                    FlashcardEntity(
                        noteId = note1Id,
                        category = "Biology",
                        question = "What is the primary energy currency of the cell?",
                        answer = "Adenosine Triphosphate (ATP)"
                    ),
                    FlashcardEntity(
                        noteId = note1Id,
                        category = "Biology",
                        question = "Where does the Krebs Cycle occur inside eukaryotic cells?",
                        answer = "Mitochondrial matrix"
                    ),
                    FlashcardEntity(
                        category = "Physics",
                        question = "What is Newton's Second Law of Motion formula?",
                        answer = "F = m · a (Force = mass × acceleration)"
                    ),
                    FlashcardEntity(
                        category = "Mathematics",
                        question = "What is the quadratic formula to solve ax² + bx + c = 0?",
                        answer = "x = (-b ± √(b² - 4ac)) / (2a)"
                    ),
                    FlashcardEntity(
                        category = "Kinyarwanda",
                        question = "How do you say 'Thank you very much' in Kinyarwanda?",
                        answer = "Murakoze cyane"
                    )
                )
            )

            // Seed initial study plans
            database.studyPlanDao().insertPlans(
                listOf(
                    StudyPlanEntity(
                        subject = "Mathematics",
                        examTitle = "Calculus & Linear Algebra Midterm",
                        examDateMillis = System.currentTimeMillis() + (10L * 24 * 3600 * 1000),
                        dayNumber = 1,
                        dayTitle = "Day 1 — Algebra & Limits",
                        taskDescription = "Review limit definitions, L'Hôpital's rule, and practice 15 algebraic limit problems.",
                        isCompleted = true
                    ),
                    StudyPlanEntity(
                        subject = "Mathematics",
                        examTitle = "Calculus & Linear Algebra Midterm",
                        examDateMillis = System.currentTimeMillis() + (10L * 24 * 3600 * 1000),
                        dayNumber = 2,
                        dayTitle = "Day 2 — Derivatives & Chain Rule",
                        taskDescription = "Master product, quotient, and chain rules; compute implicit derivatives.",
                        isCompleted = true
                    ),
                    StudyPlanEntity(
                        subject = "Mathematics",
                        examTitle = "Calculus & Linear Algebra Midterm",
                        examDateMillis = System.currentTimeMillis() + (10L * 24 * 3600 * 1000),
                        dayNumber = 3,
                        dayTitle = "Day 3 — Integration Basics",
                        taskDescription = "Understand Riemann sums, definite integrals, and standard substitution techniques.",
                        isCompleted = false
                    ),
                    StudyPlanEntity(
                        subject = "Mathematics",
                        examTitle = "Calculus & Linear Algebra Midterm",
                        examDateMillis = System.currentTimeMillis() + (10L * 24 * 3600 * 1000),
                        dayNumber = 4,
                        dayTitle = "Day 4 — Integration by Parts",
                        taskDescription = "Practice tabular integration and inverse trigonometric integral forms.",
                        isCompleted = false
                    ),
                    StudyPlanEntity(
                        subject = "Mathematics",
                        examTitle = "Calculus & Linear Algebra Midterm",
                        examDateMillis = System.currentTimeMillis() + (10L * 24 * 3600 * 1000),
                        dayNumber = 5,
                        dayTitle = "Day 5 — Mock Exam & Revision",
                        taskDescription = "Complete 2-hour timed mock exam; diagnose weak topics in AI Tutor.",
                        isCompleted = false
                    )
                )
            )

            // Seed initial quiz history
            database.quizDao().insertQuizResult(
                QuizHistoryEntity(
                    subject = "Biology",
                    topic = "Cellular Energy",
                    difficulty = "Intermediate",
                    score = 9,
                    totalQuestions = 10,
                    xpEarned = 90
                )
            )
            database.quizDao().insertQuizResult(
                QuizHistoryEntity(
                    subject = "Physics",
                    topic = "Kinematics",
                    difficulty = "Beginner",
                    score = 8,
                    totalQuestions = 10,
                    xpEarned = 80
                )
            )

            // Seed language progress
            database.languageProgressDao().saveProgress(LanguageProgressEntity(languageCode = "en", wordsLearned = 120, lessonsCompleted = 14, speakingScore = 92))
            database.languageProgressDao().saveProgress(LanguageProgressEntity(languageCode = "fr", wordsLearned = 45, lessonsCompleted = 5, speakingScore = 78))
            database.languageProgressDao().saveProgress(LanguageProgressEntity(languageCode = "rw", wordsLearned = 60, lessonsCompleted = 8, speakingScore = 85))
            database.languageProgressDao().saveProgress(LanguageProgressEntity(languageCode = "es", wordsLearned = 35, lessonsCompleted = 4, speakingScore = 70))
            database.languageProgressDao().saveProgress(LanguageProgressEntity(languageCode = "ko", wordsLearned = 25, lessonsCompleted = 3, speakingScore = 65))
            database.languageProgressDao().saveProgress(LanguageProgressEntity(languageCode = "zh", wordsLearned = 20, lessonsCompleted = 2, speakingScore = 60))
            database.languageProgressDao().saveProgress(LanguageProgressEntity(languageCode = "pt", wordsLearned = 30, lessonsCompleted = 3, speakingScore = 75))
        }
    }
}
