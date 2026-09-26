package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.StudyMateAudio
import com.example.data.local.entities.FlashcardEntity
import com.example.data.local.entities.NoteEntity
import com.example.data.local.entities.QuizHistoryEntity
import com.example.data.local.entities.StudyPlanEntity
import com.example.data.local.entities.UserProfileEntity
import com.example.data.model.LanguageInfo
import com.example.data.model.LanguageLesson
import com.example.data.model.LanguageQuizQuestion
import com.example.data.model.VocabularyItem
import com.example.data.repository.LanguageCurriculum
import com.example.data.repository.StudyMateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppDestination {
    ONBOARDING,
    HOME,
    LEARN,
    AI_TUTOR,
    PROGRESS,
    PROFILE,
    SCANNER,
    NOTES,
    FLASHCARDS,
    QUIZ,
    QUIZ_RESULTS,
    LANGUAGE_DETAIL,
    SPEAKING_PRACTICE,
    STUDY_PLANNER,
    SUBSCRIPTION
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "user" or "studymate"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class ActiveQuizState(
    val subject: String = "Mathematics",
    val topic: String = "Algebra",
    val difficulty: String = "Beginner",
    val questions: List<LanguageQuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerConfirmed: Boolean = false,
    val score: Int = 0,
    val userAnswers: List<Int?> = emptyList()
)

class StudyMateViewModel(application: Application) : AndroidViewModel(application) {

    val repository = StudyMateRepository(application)
    val audio = StudyMateAudio(application)

    // Current navigation destination & backstack
    private val _currentDestination = MutableStateFlow(AppDestination.HOME)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    private val navStack = mutableListOf<AppDestination>()

    // User Profile
    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Notes
    val allNotes: StateFlow<List<NoteEntity>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flashcards
    val allFlashcards: StateFlow<List<FlashcardEntity>> = repository.allFlashcards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Quiz History
    val quizHistory: StateFlow<List<QuizHistoryEntity>> = repository.quizHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Study Plans
    val allPlans: StateFlow<List<StudyPlanEntity>> = repository.allPlans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Language Progress
    val languageProgress = repository.languageProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Question Scanner State
    private val _scannerInputText = MutableStateFlow("Solve: 2x + 5 = 15")
    val scannerInputText: StateFlow<String> = _scannerInputText.asStateFlow()

    private val _scannerImage = MutableStateFlow<Bitmap?>(null)
    val scannerImage: StateFlow<Bitmap?> = _scannerImage.asStateFlow()

    private val _scannerAnalysis = MutableStateFlow<String?>(null)
    val scannerAnalysis: StateFlow<String?> = _scannerAnalysis.asStateFlow()

    private val _isAnalyzingQuestion = MutableStateFlow(false)
    val isAnalyzingQuestion: StateFlow<Boolean> = _isAnalyzingQuestion.asStateFlow()

    // AI Tutor State
    val tutorModes = listOf(
        "Teacher Mode",
        "Quiz Master",
        "Writing Coach",
        "Subject Tutor",
        "Language Tutor",
        "Conversation Partner"
    )

    private val _selectedTutorMode = MutableStateFlow("Teacher Mode")
    val selectedTutorMode: StateFlow<String> = _selectedTutorMode.asStateFlow()

    private val _tutorSubject = MutableStateFlow("Academic Studies")
    val tutorSubject: StateFlow<String> = _tutorSubject.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "studymate",
                text = "Hello! I'm StudyMate, your personal AI tutor 🎓\n\nI can explain concepts step-by-step, test your knowledge, check essays, or practice 7 languages. What would you like to master today?"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isTutorThinking = MutableStateFlow(false)
    val isTutorThinking: StateFlow<Boolean> = _isTutorThinking.asStateFlow()

    // Language Learning State
    private val _selectedLanguage = MutableStateFlow(LanguageCurriculum.languages[2]) // Kinyarwanda as featured default
    val selectedLanguage: StateFlow<LanguageInfo> = _selectedLanguage.asStateFlow()

    private val _selectedLesson = MutableStateFlow<LanguageLesson?>(null)
    val selectedLesson: StateFlow<LanguageLesson?> = _selectedLesson.asStateFlow()

    // Active Quiz State
    private val _activeQuiz = MutableStateFlow(ActiveQuizState())
    val activeQuiz: StateFlow<ActiveQuizState> = _activeQuiz.asStateFlow()

    // Flashcard study mode
    private val _flashcardDeckIndex = MutableStateFlow(0)
    val flashcardDeckIndex: StateFlow<Int> = _flashcardDeckIndex.asStateFlow()

    private val _isCardFlipped = MutableStateFlow(false)
    val isCardFlipped: StateFlow<Boolean> = _isCardFlipped.asStateFlow()

    // Monetization dialog
    private val _showRewardedAdDialog = MutableStateFlow(false)
    val showRewardedAdDialog: StateFlow<Boolean> = _showRewardedAdDialog.asStateFlow()

    private val _rewardedAdRewardUnlocked = MutableStateFlow(false)
    val rewardedAdRewardUnlocked: StateFlow<Boolean> = _rewardedAdRewardUnlocked.asStateFlow()

    // Onboarding State
    private val _onboardingStep = MutableStateFlow(1)
    val onboardingStep: StateFlow<Int> = _onboardingStep.asStateFlow()

    fun navigateTo(dest: AppDestination) {
        if (_currentDestination.value != dest) {
            navStack.add(_currentDestination.value)
            _currentDestination.value = dest
        }
    }

    fun navigateBack(): Boolean {
        return if (navStack.isNotEmpty()) {
            _currentDestination.value = navStack.removeAt(navStack.size - 1)
            true
        } else if (_currentDestination.value != AppDestination.HOME) {
            _currentDestination.value = AppDestination.HOME
            true
        } else {
            false
        }
    }

    // Onboarding Actions
    fun nextOnboardingStep() {
        if (_onboardingStep.value < 5) {
            _onboardingStep.value += 1
        } else {
            completeOnboarding()
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfileEntity()
            repository.saveUserProfile(current.copy(completedOnboarding = true))
            _currentDestination.value = AppDestination.HOME
        }
    }

    // Question Scanner Actions
    fun setScannerText(text: String) {
        _scannerInputText.value = text
    }

    fun setScannerImage(bitmap: Bitmap?) {
        _scannerImage.value = bitmap
    }

    fun submitQuestionForAnalysis() {
        viewModelScope.launch {
            _isAnalyzingQuestion.value = true
            _scannerAnalysis.value = null
            try {
                val result = repository.analyzeQuestion(
                    _scannerInputText.value,
                    _scannerImage.value
                )
                _scannerAnalysis.value = result
                repository.addXp(35)
            } catch (e: Exception) {
                _scannerAnalysis.value = "Unable to process question. Please verify your prompt."
            } finally {
                _isAnalyzingQuestion.value = false
            }
        }
    }

    // AI Tutor Actions
    fun selectTutorMode(mode: String) {
        _selectedTutorMode.value = mode
    }

    fun sendTutorMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(sender = "user", text = text)
        _chatMessages.value = _chatMessages.value + userMsg

        viewModelScope.launch {
            _isTutorThinking.value = true
            try {
                val level = userProfile.value?.educationLevel ?: "University"
                val response = repository.askTutor(
                    userMessage = text,
                    mode = _selectedTutorMode.value,
                    educationLevel = level,
                    subject = _tutorSubject.value
                )
                val tutorMsg = ChatMessage(sender = "studymate", text = response)
                _chatMessages.value = _chatMessages.value + tutorMsg
                repository.addXp(15)
            } catch (e: Exception) {
                val errorMsg = ChatMessage(sender = "studymate", text = "I encountered a minor issue. Please try again.")
                _chatMessages.value = _chatMessages.value + errorMsg
            } finally {
                _isTutorThinking.value = false
            }
        }
    }

    // Notes Actions
    fun createNote(title: String, content: String, subject: String) {
        viewModelScope.launch {
            repository.createNote(title, content, subject)
            repository.addXp(20)
        }
    }

    fun summarizeNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.summarizeNoteWithAi(note)
        }
    }

    fun generateFlashcardsForNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.generateFlashcardsFromNote(note)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    // Flashcard Flip & Mastery
    fun toggleCardFlip() {
        _isCardFlipped.value = !_isCardFlipped.value
    }

    fun nextCard(total: Int) {
        if (total > 0) {
            _isCardFlipped.value = false
            _flashcardDeckIndex.value = (_flashcardDeckIndex.value + 1) % total
        }
    }

    fun markCardMastered(card: FlashcardEntity, total: Int) {
        viewModelScope.launch {
            repository.toggleFlashcardMastery(card)
            nextCard(total)
        }
    }

    // Language Selection & Audio
    fun selectLanguage(lang: LanguageInfo) {
        _selectedLanguage.value = lang
    }

    fun selectLesson(lesson: LanguageLesson) {
        _selectedLesson.value = lesson
    }

    fun speakPhrase(text: String, langCode: String) {
        audio.speak(text, langCode)
    }

    fun completeCurrentLesson() {
        val lesson = _selectedLesson.value ?: return
        viewModelScope.launch {
            repository.recordLessonCompletion(lesson.languageCode, lesson.vocabulary.size)
        }
    }

    // Quiz Generator & Runner
    fun startQuiz(subject: String, topic: String, difficulty: String) {
        val generatedQuestions = generateQuestionsForTopic(subject, topic, difficulty)
        _activeQuiz.value = ActiveQuizState(
            subject = subject,
            topic = topic,
            difficulty = difficulty,
            questions = generatedQuestions,
            currentIndex = 0,
            selectedOptionIndex = null,
            isAnswerConfirmed = false,
            score = 0,
            userAnswers = List(generatedQuestions.size) { null }
        )
        navigateTo(AppDestination.QUIZ)
    }

    fun selectQuizOption(index: Int) {
        if (!_activeQuiz.value.isAnswerConfirmed) {
            _activeQuiz.value = _activeQuiz.value.copy(selectedOptionIndex = index)
        }
    }

    fun confirmQuizAnswer() {
        val current = _activeQuiz.value
        val question = current.questions.getOrNull(current.currentIndex) ?: return
        val isCorrect = current.selectedOptionIndex == question.correctIndex
        val newScore = if (isCorrect) current.score + 1 else current.score

        val updatedAnswers = current.userAnswers.toMutableList()
        if (current.currentIndex < updatedAnswers.size) {
            updatedAnswers[current.currentIndex] = current.selectedOptionIndex
        }

        _activeQuiz.value = current.copy(
            isAnswerConfirmed = true,
            score = newScore,
            userAnswers = updatedAnswers
        )
    }

    fun nextQuizQuestion() {
        val current = _activeQuiz.value
        if (current.currentIndex + 1 < current.questions.size) {
            _activeQuiz.value = current.copy(
                currentIndex = current.currentIndex + 1,
                selectedOptionIndex = null,
                isAnswerConfirmed = false
            )
        } else {
            // Quiz completed
            viewModelScope.launch {
                repository.recordQuizResult(
                    current.subject,
                    current.topic,
                    current.difficulty,
                    current.score,
                    current.questions.size
                )
            }
            navigateTo(AppDestination.QUIZ_RESULTS)
        }
    }

    private fun generateQuestionsForTopic(
        subject: String,
        topic: String,
        difficulty: String
    ): List<LanguageQuizQuestion> {
        return when {
            subject.contains("Math", ignoreCase = true) -> listOf(
                LanguageQuizQuestion("Solve for x: 3x - 7 = 14", listOf("x = 5", "x = 7", "x = 9", "x = 6"), 1, "Add 7 to both sides: 3x = 21, then divide by 3: x = 7."),
                LanguageQuizQuestion("What is the derivative of f(x) = 4x³ + 2x?", listOf("12x² + 2", "7x² + 2", "12x³", "4x² + 2"), 0, "Using the power rule: d/dx(4x³) = 12x² and d/dx(2x) = 2."),
                LanguageQuizQuestion("What is the slope of a horizontal line?", listOf("1", "Undefined", "0", "-1"), 2, "A horizontal line has zero vertical change (Δy = 0), so slope = 0."),
                LanguageQuizQuestion("What is √144?", listOf("11", "12", "13", "14"), 1, "12 × 12 = 144.")
            )
            subject.contains("Bio", ignoreCase = true) -> listOf(
                LanguageQuizQuestion("Which organelle is considered the powerhouse of the cell?", listOf("Nucleus", "Ribosome", "Mitochondria", "Golgi apparatus"), 2, "Mitochondria generate the majority of cellular chemical energy in the form of ATP."),
                LanguageQuizQuestion("What molecule carries genetic instructions in living organisms?", listOf("DNA", "Hemoglobin", "Glucose", "Insulin"), 0, "Deoxyribonucleic acid (DNA) stores hereditary information."),
                LanguageQuizQuestion("During which phase of mitosis do chromosomes align in the cell center?", listOf("Prophase", "Metaphase", "Anaphase", "Telophase"), 1, "In metaphase, chromosomes align along the equatorial metaphase plate.")
            )
            subject.contains("Physics", ignoreCase = true) -> listOf(
                LanguageQuizQuestion("What is the SI unit of electrical resistance?", listOf("Volt", "Ampere", "Ohm", "Watt"), 2, "The Ohm (Ω) is the SI unit of electrical resistance."),
                LanguageQuizQuestion("What is standard acceleration due to gravity on Earth's surface?", listOf("9.8 m/s²", "8.9 m/s²", "12.2 m/s²", "6.4 m/s²"), 0, "Earth's standard gravitational acceleration is approximately 9.8 m/s²."),
                LanguageQuizQuestion("Which particle has a negative electrical charge?", listOf("Proton", "Neutron", "Electron", "Positron"), 2, "Electrons carry an elementary negative electric charge.")
            )
            else -> listOf(
                LanguageQuizQuestion("What is the primary objective of the scientific method?", listOf("Proof by authority", "Systematic inquiry & empirical testing", "Fast guesswork", "Popular vote"), 1, "The scientific method relies on systematic observation, hypothesis formulation, and empirical testing."),
                LanguageQuizQuestion("Which principle states that energy cannot be created or destroyed?", listOf("First Law of Thermodynamics", "Archimedes Principle", "Ohm's Law", "Hooke's Law"), 0, "The First Law of Thermodynamics establishes the conservation of energy.")
            )
        }
    }

    // Study Planner Actions
    fun generatePlan(subject: String, examTitle: String, days: Int, dailyHours: Double, topics: String) {
        viewModelScope.launch {
            repository.generateStudyPlan(subject, examTitle, days, dailyHours, topics)
        }
    }

    fun togglePlanItem(id: Long, completed: Boolean) {
        viewModelScope.launch {
            repository.toggleStudyPlanTask(id, completed)
        }
    }

    // Monetization & Subscription
    fun setRewardedAdDialogVisible(visible: Boolean) {
        _showRewardedAdDialog.value = visible
    }

    fun watchRewardedAd() {
        viewModelScope.launch {
            _rewardedAdRewardUnlocked.value = true
            _showRewardedAdDialog.value = false
            repository.addXp(50)
        }
    }

    fun upgradeToPremium() {
        viewModelScope.launch {
            repository.setPremiumStatus(true)
            repository.addXp(200)
        }
    }

    fun downgradeToFree() {
        viewModelScope.launch {
            repository.setPremiumStatus(false)
        }
    }

    fun updateInterfaceLanguage(lang: String) {
        viewModelScope.launch {
            repository.updateInterfaceLanguage(lang)
        }
    }

    fun updateEducationLevel(level: String) {
        viewModelScope.launch {
            repository.updateEducationLevel(level)
        }
    }

    override fun onCleared() {
        super.onCleared()
        audio.shutdown()
    }
}
