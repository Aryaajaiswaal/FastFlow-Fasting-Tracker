package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.FastingRecord
import com.example.data.entity.UserSettings
import com.example.model.FastingStage
import com.example.ui.components.TimerCircle
import com.example.ui.components.WaterHydrationCard
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.FastingOrange
import com.example.ui.theme.RenewalEmerald
import com.example.ui.theme.TealAccent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TimerScreen(
    activeFast: FastingRecord?,
    settings: UserSettings,
    currentTime: Long,
    elapsedMillis: Long,
    currentStage: FastingStage,
    waterTotalMl: Int,
    onStartFast: () -> Unit,
    onEndFastClick: () -> Unit,
    onEditStartTimeClick: () -> Unit,
    onPlanClick: () -> Unit,
    onAddWater: (Int) -> Unit,
    onUndoWater: () -> Unit,
    onEditWaterGoal: () -> Unit,
    onNavigateToStages: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFastingActive = activeFast != null
    val timeFormatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "FastFlow",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isFastingActive) "You are actively fasting" else "Plan: ${settings.selectedPlanName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .size(42.dp)
                    .clickable { onPlanClick() }
                    .testTag("plan_settings_icon_btn")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Fasting Plans",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Timer Circle Hero Component
        TimerCircle(
            elapsedMillis = elapsedMillis,
            targetHours = activeFast?.targetDurationHours ?: settings.targetFastHours,
            isFastingActive = isFastingActive,
            planName = activeFast?.planName ?: settings.selectedPlanName,
            currentStage = if (isFastingActive) currentStage else null,
            onPlanClick = onPlanClick,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Fast Start & End Timing Info Card (if fasting active)
        AnimatedVisibility(visible = isFastingActive && activeFast != null) {
            if (activeFast != null) {
                val targetMillis = (activeFast.targetDurationHours * 3600 * 1000).toLong()
                val projectedEndTime = activeFast.startTimeMillis + targetMillis

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "STARTED",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = timeFormatter.format(Date(activeFast.startTimeMillis)),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(
                            onClick = onEditStartTimeClick,
                            modifier = Modifier.size(36.dp).testTag("edit_start_time_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Start Time",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "TARGET GOAL",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = timeFormatter.format(Date(projectedEndTime)),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FastingOrange
                            )
                        }
                    }
                }
            }
        }

        // Primary Start / End Fast Action Button
        if (isFastingActive) {
            Button(
                onClick = onEndFastClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = FastingOrange,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("end_fast_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "End Fast & Log",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Button(
                onClick = onStartFast,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("start_fast_button")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Start Fasting (${settings.targetFastHours.toInt()}h)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Active Metabolic Stage Card Banner
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            border = BorderStroke(1.dp, currentStage.color.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToStages() }
                .testTag("metabolic_stage_preview_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = currentStage.color.copy(alpha = 0.2f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${currentStage.stageNumber}",
                                    fontWeight = FontWeight.Bold,
                                    color = currentStage.color
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "CURRENT METABOLIC PHASE",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = currentStage.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Text(
                        text = "View Stages >",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = currentStage.headline,
                    style = MaterialTheme.typography.bodyMedium,
                    color = currentStage.color,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = currentStage.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Water Hydration Tracker Card
        WaterHydrationCard(
            currentMl = waterTotalMl,
            goalMl = settings.dailyWaterGoalMl,
            onAddWater = onAddWater,
            onUndo = onUndoWater,
            onEditGoal = onEditWaterGoal
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Fasting Wisdom / Tip Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = "Tip",
                    tint = FastingOrange,
                    modifier = Modifier.size(20.dp).padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Pro Fasting Tip",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Black coffee, unsweetened green tea, and pure water do not break a fast and help suppress hunger hormones while boosting fat oxidation.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
