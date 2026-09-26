package com.example.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.StudyMateViewModel

@Composable
fun OnboardingScreen(
    viewModel: StudyMateViewModel,
    onFinished: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var email by remember { mutableStateOf("danos@studymate.ai") }
    var password by remember { mutableStateOf("••••••••") }
    var selectedLevel by remember { mutableStateOf("University") }
    var selectedLanguage by remember { mutableStateOf("English") }
    val selectedGoals = remember { mutableStateListOf("Academic Study", "Exam Preparation", "Learn Languages") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Step progress dots
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 1..5) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (i == step) 24.dp else 8.dp, 8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (i <= step) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outlineVariant
                        )
                )
            }
        }

        AnimatedContent(
            targetState = step,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "onboarding_step"
        ) { currentStep ->
            when (currentStep) {
                1 -> StepWelcome(onGetStarted = { step = 2 }, onLogIn = { step = 2 })
                2 -> StepCreateAccount(
                    email = email,
                    onEmailChange = { email = it },
                    password = password,
                    onPasswordChange = { password = it },
                    onContinue = { step = 3 }
                )
                3 -> StepLearningGoals(
                    selectedGoals = selectedGoals,
                    onToggleGoal = { goal ->
                        if (goal == "All of the above") {
                            selectedGoals.clear()
                            selectedGoals.addAll(listOf("Academic Study", "Exam Preparation", "Learn Languages", "Homework Help"))
                        } else {
                            if (selectedGoals.contains(goal)) selectedGoals.remove(goal)
                            else selectedGoals.add(goal)
                        }
                    },
                    onContinue = { step = 4 }
                )
                4 -> StepEducationLevel(
                    selectedLevel = selectedLevel,
                    onSelectLevel = {
                        selectedLevel = it
                        viewModel.updateEducationLevel(it)
                    },
                    onContinue = { step = 5 }
                )
                5 -> StepInterfaceLanguage(
                    selectedLanguage = selectedLanguage,
                    onSelectLanguage = {
                        selectedLanguage = it
                        viewModel.updateInterfaceLanguage(it)
                    },
                    onFinish = {
                        viewModel.completeOnboarding()
                        onFinished()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun StepWelcome(
    onGetStarted: () -> Unit,
    onLogIn: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        // Hero visual badge
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(110.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(56.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "StudyMate AI",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Learn Smarter. Understand Better. Achieve More.",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your all-in-one AI study companion: scan questions, generate summaries, master quizzes, plan study sessions, and learn 7 world languages.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("onboarding_get_started_btn"),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Get Started", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onLogIn,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("onboarding_login_btn"),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Log In to Existing Account", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun StepCreateAccount(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Create Your Account",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Sign in to save your notes, streak, and language progress.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email address") },
            leadingIcon = { Icon(Icons.Default.Mail, contentDescription = null) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_email_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_password_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("auth_continue_btn"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Continue with Email", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("auth_google_btn"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Sign in with Google", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun StepLearningGoals(
    selectedGoals: List<String>,
    onToggleGoal: (String) -> Unit,
    onContinue: () -> Unit
) {
    val goals = listOf(
        "📚 Academic Study" to "Academic Study",
        "🧠 Exam Preparation" to "Exam Preparation",
        "🌍 Learn Languages" to "Learn Languages",
        "📝 Homework Help" to "Homework Help",
        "🎯 All of the above" to "All of the above"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "What is your main goal?",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Select all that apply. We'll tailor your personalized feed.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        goals.forEach { (label, key) ->
            val isChecked = selectedGoals.contains(key)
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isChecked) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onToggleGoal(key) }
                    .border(
                        1.5.dp,
                        if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(14.dp)
                    )
                    .testTag("goal_${key.lowercase().replace(" ", "_")}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = if (isChecked) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurface
                    )
                    if (isChecked) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("goals_continue_btn"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Next Step", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun StepEducationLevel(
    selectedLevel: String,
    onSelectLevel: (String) -> Unit,
    onContinue: () -> Unit
) {
    val levels = listOf("Primary", "Secondary", "University", "Other")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "What is your education level?",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "StudyMate adjusts its explanations and vocabulary depth accordingly.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        levels.forEach { level ->
            val isSelected = selectedLevel == level
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onSelectLevel(level) }
                    .border(
                        1.5.dp,
                        if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(14.dp)
                    )
                    .testTag("level_${level.lowercase()}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = level,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                        else MaterialTheme.colorScheme.onSurface
                    )
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("level_continue_btn"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Next Step", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun StepInterfaceLanguage(
    selectedLanguage: String,
    onSelectLanguage: (String) -> Unit,
    onFinish: () -> Unit
) {
    val languages = listOf(
        Triple("English", "English", "🇬🇧"),
        Triple("French", "Français", "🇫🇷"),
        Triple("Kinyarwanda", "Ikinyarwanda", "🇷🇼")
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Choose Interface Language",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "You can change this anytime in Profile settings.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        languages.forEach { (name, nativeName, flag) ->
            val isSelected = selectedLanguage == name
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onSelectLanguage(name) }
                    .border(
                        1.5.dp,
                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(14.dp)
                    )
                    .testTag("lang_${name.lowercase()}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = flag, fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = nativeName,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("finish_onboarding_btn"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Enter StudyMate AI", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
