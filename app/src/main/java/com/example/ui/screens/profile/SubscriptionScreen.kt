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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.StudyMateViewModel

@Composable
fun SubscriptionScreen(
    viewModel: StudyMateViewModel,
    onBack: () -> Unit
) {
    val profile by viewModel.userProfile.collectAsState()
    val isPremium = profile?.isPremium ?: false

    var selectedPeriod by remember { mutableIntStateOf(0) } // 0: Monthly, 1: Yearly

    val comparisonFeatures = listOf(
        Triple("AI Tutor Queries", "Basic (15/day)", "Unlimited Deep Reasoning"),
        Triple("Question Scanner", "Standard OCR", "High-Resolution Multimodal"),
        Triple("Smart Notes & Docs", "Basic Notes", "Unlimited Summaries & OCR"),
        Triple("7 Languages Curriculum", "Beginner Lessons", "Full Advanced + Roleplay"),
        Triple("Speaking & Pronunciation", "Standard TTS", "AI Speech Feedback Engine"),
        Triple("Study Planner", "Manual", "AI Multi-Day Schedule Generator"),
        Triple("Advertisements", "Ad-Supported", "100% Ad-Free Experience")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            shape = CircleShape,
            color = Color(0xFFF59E0B).copy(alpha = 0.15f),
            modifier = Modifier.size(68.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Diamond,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "StudyMate AI Premium",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Unlock the ultimate AI educational advantage worldwide",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Billing Cycle Selector: Monthly vs Yearly
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                .padding(4.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (selectedPeriod == 0) MaterialTheme.colorScheme.surface else Color.Transparent,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { selectedPeriod = 0 }
                    .padding(vertical = 10.dp)
                    .testTag("billing_monthly_btn")
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Monthly",
                        fontWeight = if (selectedPeriod == 0) FontWeight.Bold else FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "$4.99 / month",
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (selectedPeriod == 1) MaterialTheme.colorScheme.surface else Color.Transparent,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { selectedPeriod = 1 }
                    .padding(vertical = 10.dp)
                    .testTag("billing_yearly_btn")
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Yearly",
                            fontWeight = if (selectedPeriod == 1) FontWeight.Bold else FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF10B981)
                        ) {
                            Text(
                                text = "SAVE 33%",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "$39.99 / year",
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Feature Comparison Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Tier Comparison",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                comparisonFeatures.forEach { (feat, freeVal, proVal) ->
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                        Text(
                            text = feat,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Free: $freeVal",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = proVal,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF10B981)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isPremium) {
            Button(
                onClick = { viewModel.downgradeToFree() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("downgrade_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Switch to Free Tier", color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
            }
        } else {
            Button(
                onClick = { viewModel.upgradeToPremium() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("upgrade_premium_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.WorkspacePremium, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedPeriod == 0) "Subscribe for $4.99 / Month" else "Subscribe for $39.99 / Year",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Supports Google Play Billing & Apple In-App Purchases • Cancel anytime in store settings.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}
