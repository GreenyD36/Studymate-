package com.example.ui.screens.profile

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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppDestination
import com.example.ui.StudyMateViewModel

@Composable
fun ProfileScreen(
    viewModel: StudyMateViewModel,
    onNavigate: (AppDestination) -> Unit
) {
    val profile by viewModel.userProfile.collectAsState()
    val isPremium = profile?.isPremium ?: false

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showEducationDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Profile Avatar & Name Header
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(76.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Avatar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = profile?.name ?: "Danos",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = profile?.email ?: "danos@studymate.ai",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = profile?.educationLevel ?: "University",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isPremium) Color(0xFFF59E0B).copy(alpha = 0.2f) else MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = if (isPremium) "PRO Subscriber 👑" else "Free Plan",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPremium) Color(0xFFB45309) else MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Subscription Banner
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isPremium) Color(0xFF065F46) else MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable { onNavigate(AppDestination.SUBSCRIPTION) }
                .testTag("profile_subscription_card")
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isPremium) Icons.Default.WorkspacePremium else Icons.Default.Diamond,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isPremium) "StudyMate PRO Active" else "Upgrade to StudyMate PRO",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                        Text(
                            text = if (isPremium) "Unlimited AI Tutor & Offline Mode" else "Ad-free, unlimited AI questions & tutor",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }

                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "App & Learning Preferences",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Settings items
        SettingsRowItem(
            icon = Icons.Default.Language,
            title = "Interface Language",
            subtitle = profile?.interfaceLanguage ?: "English",
            onClick = { showLanguageDialog = true },
            testTag = "settings_language"
        )

        SettingsRowItem(
            icon = Icons.Default.School,
            title = "Education Level",
            subtitle = profile?.educationLevel ?: "University",
            onClick = { showEducationDialog = true },
            testTag = "settings_education"
        )

        SettingsRowItem(
            icon = Icons.Default.Diamond,
            title = "Subscription & Billing",
            subtitle = if (isPremium) "$4.99/mo Active" else "Free tier",
            onClick = { onNavigate(AppDestination.SUBSCRIPTION) },
            testTag = "settings_billing"
        )

        SettingsRowItem(
            icon = Icons.Default.Notifications,
            title = "Study Reminders & Streak Notifications",
            subtitle = "Daily goals at 18:00",
            onClick = {},
            testTag = "settings_notifications"
        )

        SettingsRowItem(
            icon = Icons.Default.Security,
            title = "Privacy & Student Data Security",
            subtitle = "Zero tracking • GDPR & COPPA compliant",
            onClick = {},
            testTag = "settings_privacy"
        )

        SettingsRowItem(
            icon = Icons.AutoMirrored.Filled.Logout,
            title = "Reset Learning Session / Log Out",
            subtitle = "Switch account or restart onboarding",
            onClick = { showLogoutDialog = true },
            tint = MaterialTheme.colorScheme.error,
            testTag = "settings_logout"
        )

        Spacer(modifier = Modifier.height(30.dp))
    }

    // Language Selector Dialog
    if (showLanguageDialog) {
        val options = listOf("English", "Français", "Ikinyarwanda")
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("Choose Interface Language") },
            text = {
                Column {
                    options.forEach { lang ->
                        Text(
                            text = lang,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    viewModel.updateInterfaceLanguage(lang)
                                    showLanguageDialog = false
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            },
            confirmButton = {}
        )
    }

    // Education Level Dialog
    if (showEducationDialog) {
        val levels = listOf("Primary", "Secondary", "University", "Other")
        AlertDialog(
            onDismissRequest = { showEducationDialog = false },
            title = { Text("Select Education Level") },
            text = {
                Column {
                    levels.forEach { lvl ->
                        Text(
                            text = lvl,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    viewModel.updateEducationLevel(lvl)
                                    showEducationDialog = false
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            },
            confirmButton = {}
        )
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Restart Onboarding?") },
            text = { Text("Would you like to restart the onboarding and account flow?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onNavigate(AppDestination.ONBOARDING)
                    }
                ) {
                    Text("Restart Onboarding")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SettingsRowItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    testTag: String = ""
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = tint
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
