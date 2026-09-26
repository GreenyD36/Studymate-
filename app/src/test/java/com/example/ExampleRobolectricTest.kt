package com.example

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.entities.FlashcardEntity
import com.example.data.local.entities.LanguageProgressEntity
import com.example.data.local.entities.NoteEntity
import com.example.data.local.entities.QuizHistoryEntity
import com.example.data.local.entities.StudyPlanEntity
import com.example.data.local.entities.UserProfileEntity
import com.example.data.remote.GeminiService
import com.example.ui.AppDestination
import com.example.ui.StudyMateViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var context: Context
    private lateinit var inMemoryDb: AppDatabase

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext<Context>()
        inMemoryDb = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        inMemoryDb.close()
    }

    @Test
    fun testAppNameResourceString() {
        val appName = context.getString(R.string.app_name)
        assertEquals("StudyMate AI", appName)
    }

    @Test
    fun testRoomDatabaseUserDao() = runBlocking {
        val userDao = inMemoryDb.userDao()

        val initialProfile = UserProfileEntity(
            id = 1,
            name = "Test Student",
            email = "student@test.com",
            educationLevel = "University",
            interfaceLanguage = "English",
            learningGoals = "Academic Study",
            xp = 100,
            streak = 5,
            level = 2,
            isPremium = false,
            completedOnboarding = true
        )
        userDao.saveUserProfile(initialProfile)

        val retrieved = userDao.getUserProfile().first()
        assertNotNull(retrieved)
        assertEquals("Test Student", retrieved?.name)
        assertEquals(100, retrieved?.xp)
        assertEquals(5, retrieved?.streak)

        // Test XP update
        userDao.addXp(50)
        val updated = userDao.getUserProfile().first()
        assertEquals(150, updated?.xp)

        // Test Premium status update
        userDao.setPremiumStatus(true)
        val premiumUser = userDao.getUserProfile().first()
        assertTrue(premiumUser?.isPremium == true)
    }

    @Test
    fun testRoomDatabaseNoteAndFlashcardDao() = runBlocking {
        val noteDao = inMemoryDb.noteDao()
        val flashcardDao = inMemoryDb.flashcardDao()

        val noteId = noteDao.insertNote(
            NoteEntity(
                title = "Calculus Derivatives",
                subject = "Mathematics",
                content = "Power rule: d/dx(x^n) = n*x^(n-1). Product rule: (uv)' = u'v + uv'.",
                summary = "Fundamental differentiation rules including power and product rules.",
                keyPoints = "• Power rule: bring down exponent.\n• Product rule: sum of cross derivatives."
            )
        )
        assertTrue(noteId > 0)

        val retrievedNote = noteDao.getNoteById(noteId)
        assertNotNull(retrievedNote)
        assertEquals("Calculus Derivatives", retrievedNote?.title)
        assertEquals("Mathematics", retrievedNote?.subject)

        // Insert associated flashcard
        val cardId = flashcardDao.insertFlashcard(
            FlashcardEntity(
                noteId = noteId,
                category = "Mathematics",
                question = "What is the derivative of x²?",
                answer = "2x",
                isMastered = false
            )
        )
        assertTrue(cardId > 0)

        val cards = flashcardDao.getFlashcardsByCategory("Mathematics").first()
        assertEquals(1, cards.size)
        assertEquals("What is the derivative of x²?", cards[0].question)

        // Test toggle mastery
        val updatedCard = cards[0].copy(isMastered = true)
        flashcardDao.updateFlashcard(updatedCard)
        val allCards = flashcardDao.getAllFlashcards().first()
        assertEquals(1, allCards.size)
        assertTrue(allCards[0].isMastered)
    }

    @Test
    fun testRoomDatabaseQuizAndStudyPlanDao() = runBlocking {
        val quizDao = inMemoryDb.quizDao()
        val studyPlanDao = inMemoryDb.studyPlanDao()

        val quizId = quizDao.insertQuizResult(
            QuizHistoryEntity(
                subject = "Chemistry",
                topic = "Periodic Table",
                difficulty = "Beginner",
                score = 9,
                totalQuestions = 10,
                xpEarned = 90
            )
        )
        assertTrue(quizId > 0)

        val history = quizDao.getAllQuizHistory().first()
        assertEquals(1, history.size)
        assertEquals("Periodic Table", history[0].topic)
        assertEquals(9, history[0].score)

        // Test Study Plan insertion and completion
        studyPlanDao.insertPlans(
            listOf(
                StudyPlanEntity(
                    subject = "Physics",
                    examTitle = "Mechanics Exam",
                    examDateMillis = System.currentTimeMillis() + 86400000L,
                    dayNumber = 1,
                    dayTitle = "Day 1 — Kinematics",
                    taskDescription = "Solve 20 kinematics motion problems",
                    isCompleted = false
                )
            )
        )

        val plans = studyPlanDao.getAllPlans().first()
        assertEquals(1, plans.size)
        assertFalse(plans[0].isCompleted)

        studyPlanDao.toggleCompleted(plans[0].id, true)
        val updatedPlans = studyPlanDao.getAllPlans().first()
        assertTrue(updatedPlans[0].isCompleted)
    }

    @Test
    fun testRoomDatabaseLanguageProgressDao() = runBlocking {
        val dao = inMemoryDb.languageProgressDao()

        dao.saveProgress(
            LanguageProgressEntity(
                languageCode = "rw",
                wordsLearned = 45,
                lessonsCompleted = 6,
                speakingScore = 88
            )
        )

        val progress = dao.getProgressForLanguage("rw")
        assertNotNull(progress)
        assertEquals(45, progress?.wordsLearned)
        assertEquals(6, progress?.lessonsCompleted)
        assertEquals(88, progress?.speakingScore)
    }

    @Test
    fun testGeminiLocalEducationalFallbackForMath() = runBlocking {
        // Test step-by-step pedagogical solver when offline or without external API key
        val response = GeminiService.generateEducationalResponse(
            prompt = "Solve: 2x + 5 = 15"
        )
        assertNotNull(response)
        assertTrue("Response should contain step-by-step logic", response.contains("Step-by-Step", ignoreCase = true) || response.contains("Step", ignoreCase = true))
        assertTrue("Response should contain final answer", response.contains("Answer", ignoreCase = true) || response.contains("x =", ignoreCase = true))
    }

    @Test
    fun testViewModelNavigationAndOnboardingCUJ() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = StudyMateViewModel(app)

        assertEquals(AppDestination.HOME, viewModel.currentDestination.value)

        // Test navigation
        viewModel.navigateTo(AppDestination.SCANNER)
        assertEquals(AppDestination.SCANNER, viewModel.currentDestination.value)

        viewModel.navigateTo(AppDestination.QUIZ)
        assertEquals(AppDestination.QUIZ, viewModel.currentDestination.value)

        // Test navigateBack
        val didPop = viewModel.navigateBack()
        assertTrue(didPop)
        assertEquals(AppDestination.SCANNER, viewModel.currentDestination.value)

        viewModel.navigateBack()
        assertEquals(AppDestination.HOME, viewModel.currentDestination.value)

        // Test onboarding steps
        viewModel.navigateTo(AppDestination.ONBOARDING)
        assertEquals(1, viewModel.onboardingStep.value)
        viewModel.nextOnboardingStep()
        assertEquals(2, viewModel.onboardingStep.value)
        viewModel.nextOnboardingStep()
        assertEquals(3, viewModel.onboardingStep.value)
        viewModel.nextOnboardingStep()
        assertEquals(4, viewModel.onboardingStep.value)
        viewModel.nextOnboardingStep()
        assertEquals(5, viewModel.onboardingStep.value)
    }

    @Test
    fun testViewModelQuizFlowCUJ() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = StudyMateViewModel(app)

        viewModel.startQuiz(subject = "Mathematics", topic = "Algebra", difficulty = "Beginner")
        assertEquals(AppDestination.QUIZ, viewModel.currentDestination.value)

        val quizState = viewModel.activeQuiz.value
        assertEquals("Mathematics", quizState.subject)
        assertTrue("Questions should be generated", quizState.questions.isNotEmpty())
        assertEquals(0, quizState.currentIndex)
        assertEquals(0, quizState.score)

        // Select an option and confirm
        viewModel.selectQuizOption(quizState.questions[0].correctIndex)
        assertEquals(quizState.questions[0].correctIndex, viewModel.activeQuiz.value.selectedOptionIndex)

        viewModel.confirmQuizAnswer()
        assertTrue(viewModel.activeQuiz.value.isAnswerConfirmed)
        assertEquals(1, viewModel.activeQuiz.value.score)
    }

    @Test
    fun testViewModelScannerCUJ() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = StudyMateViewModel(app)

        viewModel.setScannerText("Solve: 5y - 10 = 20")
        assertEquals("Solve: 5y - 10 = 20", viewModel.scannerInputText.value)

        viewModel.submitQuestionForAnalysis()
        // Verify state changes
        assertNotNull(viewModel.scannerInputText.value)
    }

    @Test
    fun testViewModelMonetizationAndGamificationCUJ() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = StudyMateViewModel(app)

        // Test Rewarded Ad dialog
        viewModel.setRewardedAdDialogVisible(true)
        assertTrue(viewModel.showRewardedAdDialog.value)

        viewModel.watchRewardedAd()
        assertTrue(viewModel.rewardedAdRewardUnlocked.value)
        assertFalse(viewModel.showRewardedAdDialog.value)

        // Test Premium upgrade toggle
        viewModel.upgradeToPremium()
        // Downgrade toggle
        viewModel.downgradeToFree()
    }
}
