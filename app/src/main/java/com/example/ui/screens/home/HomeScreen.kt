package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.LanguageCurriculum
import com.example.ui.AppDestination
import com.example.ui.StudyMateViewModel
import com.example.ui.components.AdMobBannerCard
import java.util.Calendar

@Composable
fun HomeScreen(
    viewModel: StudyMateViewModel,
    onNavigate: (AppDestination) -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val notes by viewModel.allNotes.collectAsState()
    val plans by viewModel.allPlans.collectAsState()

    val greeting = rememberGreeting()
    val userName = userProfile?.name ?: "Danos"
    val isPremium = userProfile?.isPremium ?: false

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Greeting Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "$greeting, $userName 👋",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.3).sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "What would you like to learn today?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isPremium) Color(0xFFF59E0B).copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onNavigate(AppDestination.SUBSCRIPTION) }
                    .padding(4.dp)
            ) {
                Text(
                    text = if (isPremium) "PRO 👑" else "FREE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = if (isPremium) Color(0xFFB45309) else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Daily Motivation & Goals Banner
        DailyStudyTrackerBanner(
            xp = userProfile?.xp ?: 0,
            streak = userProfile?.streak ?: 7,
            pendingTasks = plans.count { !it.isCompleted }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Core Learning Tools",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 6 Main Feature Cards from Spec:
        // 1. Scan Question
        // 2. My Notes
        // 3. Practice Quiz
        // 4. AI Tutor
        // 5. Learn a Language
        // 6. Study Planner

        Row(modifier = Modifier.fillMaxWidth()) {
            CoreActionCard(
                title = "📸 Scan Question",
                description = "Take a photo & understand step-by-step solutions.",
                icon = Icons.Default.CameraAlt,
                gradientColors = listOf(Color(0xFF2563EB), Color(0xFF3B82F6)),
                modifier = Modifier
                    .weight(1f)
                    .testTag("home_card_scanner"),
                onClick = { onNavigate(AppDestination.SCANNER) }
            )
            Spacer(modifier = Modifier.width(12.dp))
            CoreActionCard(
                title = "🤖 AI Tutor",
                description = "Ask anything with 6 specialized tutoring modes.",
                icon = Icons.Default.SmartToy,
                gradientColors = listOf(Color(0xFF7C3AED), Color(0xFF8B5CF6)),
                modifier = Modifier
                    .weight(1f)
                    .testTag("home_card_ai_tutor"),
                onClick = { onNavigate(AppDestination.AI_TUTOR) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            CoreActionCard(
                title = "📚 My Notes",
                description = "Review materials, AI summaries & flashcards.",
                icon = Icons.Default.AutoStories,
                gradientColors = listOf(Color(0xFF0D9488), Color(0xFF14B8A6)),
                modifier = Modifier
                    .weight(1f)
                    .testTag("home_card_notes"),
                onClick = { onNavigate(AppDestination.NOTES) }
            )
            Spacer(modifier = Modifier.width(12.dp))
            CoreActionCard(
                title = "🧠 Practice Quiz",
                description = "Test your knowledge with timed explanations.",
                icon = Icons.Default.Quiz,
                gradientColors = listOf(Color(0xFFEA580C), Color(0xFFF97316)),
                modifier = Modifier
                    .weight(1f)
                    .testTag("home_card_quiz"),
                onClick = { onNavigate(AppDestination.QUIZ) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            CoreActionCard(
                title = "🌍 Learn Languages",
                description = "Master English, French, Kinyarwanda & 4 more.",
                icon = Icons.Default.Language,
                gradientColors = listOf(Color(0xFF059669), Color(0xFF10B981)),
                modifier = Modifier
                    .weight(1f)
                    .testTag("home_card_language"),
                onClick = { onNavigate(AppDestination.LEARN) }
            )
            Spacer(modifier = Modifier.width(12.dp))
            CoreActionCard(
                title = "📅 Study Planner",
                description = "Intelligent schedules for upcoming exams.",
                icon = Icons.Default.CalendarMonth,
                gradientColors = listOf(Color(0xFF4F46E5), Color(0xFF6366F1)),
                modifier = Modifier
                    .weight(1f)
                    .testTag("home_card_planner"),
                onClick = { onNavigate(AppDestination.STUDY_PLANNER) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Language Explorer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Language Journeys (7 Available)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "View All",
                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary),
                modifier = Modifier.clickable { onNavigate(AppDestination.LEARN) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(LanguageCurriculum.languages) { lang ->
                LanguagePillCard(
                    lang = lang,
                    onClick = {
                        viewModel.selectLanguage(lang)
                        onNavigate(AppDestination.LEARN)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // AdMob Banner placement
        AdMobBannerCard(
            isPremium = isPremium,
            onUpgradeClick = { onNavigate(AppDestination.SUBSCRIPTION) }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun CoreActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brush.linearGradient(gradientColors)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun DailyStudyTrackerBanner(
    xp: Int,
    streak: Int,
    pendingTasks: Int
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Daily Academic Goal",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "$streak-day study streak active 🔥",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "$pendingTasks tasks due",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val currentLevelProgress = (xp % 200) / 200f
            LinearProgressIndicator(
                progress = { currentLevelProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Level ${(xp / 200) + 1} Scholar",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "${xp % 200} / 200 XP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun LanguagePillCard(
    lang: com.example.data.model.LanguageInfo,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .testTag("lang_pill_${lang.code}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(text = lang.flag, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = lang.name,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = lang.nativeName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun rememberGreeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when {
        hour < 12 -> "Good morning"
        hour < 17 -> "Good afternoon"
        else -> "Good evening"
    }
}
