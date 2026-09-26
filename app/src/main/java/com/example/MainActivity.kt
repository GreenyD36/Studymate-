package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.AppDestination
import com.example.ui.StudyMateViewModel
import com.example.ui.components.RewardedAdDialog
import com.example.ui.components.StudyMateBottomBar
import com.example.ui.components.StudyMateTopBar
import com.example.ui.screens.aitutor.AiTutorScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.languages.LanguageDetailScreen
import com.example.ui.screens.languages.LanguagesScreen
import com.example.ui.screens.notes.FlashcardDeckScreen
import com.example.ui.screens.notes.NotesScreen
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.planner.StudyPlannerScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.profile.SubscriptionScreen
import com.example.ui.screens.progress.ProgressScreen
import com.example.ui.screens.quiz.ActiveQuizScreen
import com.example.ui.screens.quiz.QuizLauncherScreen
import com.example.ui.screens.quiz.QuizResultsScreen
import com.example.ui.screens.scanner.QuestionScannerScreen
import com.example.ui.theme.StudyMateTheme

class MainActivity : ComponentActivity() {

    private val viewModel: StudyMateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyMateTheme {
                StudyMateApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun StudyMateApp(viewModel: StudyMateViewModel) {
    val currentDestination by viewModel.currentDestination.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val showRewardedAdDialog by viewModel.showRewardedAdDialog.collectAsState()

    val xp = userProfile?.xp ?: 0
    val streak = userProfile?.streak ?: 7

    val isMainTab = currentDestination in listOf(
        AppDestination.HOME,
        AppDestination.LEARN,
        AppDestination.AI_TUTOR,
        AppDestination.PROGRESS,
        AppDestination.PROFILE
    )

    val canGoBack = !isMainTab && currentDestination != AppDestination.ONBOARDING

    BackHandler(enabled = canGoBack) {
        viewModel.navigateBack()
    }

    val topBarTitle = when (currentDestination) {
        AppDestination.ONBOARDING -> "StudyMate AI"
        AppDestination.HOME -> "StudyMate AI"
        AppDestination.LEARN -> "Languages & Subjects"
        AppDestination.AI_TUTOR -> "StudyMate AI Tutor"
        AppDestination.PROGRESS -> "Academic Analytics"
        AppDestination.PROFILE -> "Student Profile"
        AppDestination.SCANNER -> "Question Scanner"
        AppDestination.NOTES -> "Smart Notes"
        AppDestination.FLASHCARDS -> "Flashcard Deck"
        AppDestination.QUIZ -> "Practice Quiz"
        AppDestination.QUIZ_RESULTS -> "Quiz Assessment"
        AppDestination.LANGUAGE_DETAIL -> "Language Mastery"
        AppDestination.SPEAKING_PRACTICE -> "Speech Lab"
        AppDestination.STUDY_PLANNER -> "Study Planner"
        AppDestination.SUBSCRIPTION -> "StudyMate PRO"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentDestination != AppDestination.ONBOARDING) {
                StudyMateTopBar(
                    title = topBarTitle,
                    canNavigateBack = canGoBack,
                    onNavigateBack = { viewModel.navigateBack() },
                    xp = xp,
                    streak = streak,
                    onXpClick = { viewModel.navigateTo(AppDestination.PROGRESS) }
                )
            }
        },
        bottomBar = {
            if (isMainTab && currentDestination != AppDestination.ONBOARDING) {
                StudyMateBottomBar(
                    currentDestination = currentDestination,
                    onTabSelected = { dest -> viewModel.navigateTo(dest) }
                )
            }
        }
    ) { innerPadding ->
        androidx.compose.foundation.layout.Box(modifier = Modifier.padding(innerPadding)) {
            when (currentDestination) {
                AppDestination.ONBOARDING -> OnboardingScreen(
                    viewModel = viewModel,
                    onFinished = { viewModel.navigateTo(AppDestination.HOME) }
                )
                AppDestination.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onNavigate = { dest -> viewModel.navigateTo(dest) }
                )
                AppDestination.LEARN -> LanguagesScreen(
                    viewModel = viewModel,
                    onNavigate = { dest -> viewModel.navigateTo(dest) }
                )
                AppDestination.AI_TUTOR -> AiTutorScreen(
                    viewModel = viewModel
                )
                AppDestination.PROGRESS -> ProgressScreen(
                    viewModel = viewModel
                )
                AppDestination.PROFILE -> ProfileScreen(
                    viewModel = viewModel,
                    onNavigate = { dest -> viewModel.navigateTo(dest) }
                )
                AppDestination.SCANNER -> QuestionScannerScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                AppDestination.NOTES -> NotesScreen(
                    viewModel = viewModel,
                    onNavigate = { dest -> viewModel.navigateTo(dest) }
                )
                AppDestination.FLASHCARDS -> FlashcardDeckScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                AppDestination.QUIZ -> ActiveQuizScreen(
                    viewModel = viewModel
                )
                AppDestination.QUIZ_RESULTS -> QuizResultsScreen(
                    viewModel = viewModel,
                    onRetake = { viewModel.navigateTo(AppDestination.QUIZ) },
                    onDone = { viewModel.navigateTo(AppDestination.HOME) }
                )
                AppDestination.LANGUAGE_DETAIL -> LanguageDetailScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                AppDestination.SPEAKING_PRACTICE -> LanguageDetailScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                AppDestination.STUDY_PLANNER -> StudyPlannerScreen(
                    viewModel = viewModel
                )
                AppDestination.SUBSCRIPTION -> SubscriptionScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
            }
        }
    }

    // Rewarded Ad Dialog (simulation)
    RewardedAdDialog(
        isOpen = showRewardedAdDialog,
        onDismiss = { viewModel.setRewardedAdDialogVisible(false) },
        onWatchAdConfirmed = { viewModel.watchRewardedAd() }
    )
}
