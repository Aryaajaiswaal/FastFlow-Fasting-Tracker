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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassEmpty
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
import androidx.compose.material3.TextButton
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
    completedFasts: List<FastingRecord>,
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
    onViewAllHistory: () -> Unit,
    onDeleteFast: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val isFastingActive = activeFast != null
    val timeFormatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val sessionDateFormat = remember { SimpleDateFormat("EEE, MMM d • hh:mm a", Locale.getDefault()) }
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
                    text = "End Fast & Log Session",
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

        Spacer(modifier = Modifier.height(24.dp))

        // COMPLETED FASTS SESSION HISTORY ON MAIN SCREEN
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("main_screen_completed_fasts_section")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Fasting History",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Completed Fasting Sessions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (completedFasts.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "${completedFasts.size}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                if (completedFasts.isNotEmpty()) {
                    TextButton(
                        onClick = onViewAllHistory,
                        modifier = Modifier.testTag("view_all_history_btn")
                    ) {
                        Text(
                            text = "View All",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (completedFasts.isEmpty()) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("empty_main_fasts_card")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.HourglassEmpty,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "No Completed Sessions Yet",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Start a timer above! When you tap 'End Fast', your session duration, metabolic milestones, and feelings will be stored here.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                // Show recent completed sessions directly on main screen (up to 4)
                val displayList = completedFasts.take(4)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    displayList.forEach { fast ->
                        val durationHours = fast.durationHours
                        val hours = durationHours.toInt()
                        val minutes = ((durationHours - hours) * 60).toInt()
                        val durationStr = "${hours}h ${minutes}m"
                        val isGoalMet = fast.isGoalAchieved

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("main_screen_fast_card_${fast.id}")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                // Top row: Plan & Date
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.surface
                                    ) {
                                        Text(
                                            text = fast.planName,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }

                                    Text(
                                        text = sessionDateFormat.format(Date(fast.startTimeMillis)),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Main duration row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = durationStr,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))

                                        if (isGoalMet) {
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = RenewalEmerald.copy(alpha = 0.2f)
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.CheckCircle,
                                                        contentDescription = "Goal Met",
                                                        tint = RenewalEmerald,
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "Goal Met (${fast.targetDurationHours.toInt()}h)",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = RenewalEmerald,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        } else {
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = FastingOrange.copy(alpha = 0.15f)
                                            ) {
                                                Text(
                                                    text = "Target: ${fast.targetDurationHours.toInt()}h",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = FastingOrange,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }

                                    IconButton(
                                        onClick = { onDeleteFast(fast.id) },
                                        modifier = Modifier.size(28.dp).testTag("delete_main_fast_${fast.id}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DeleteOutline,
                                            contentDescription = "Delete Fast Log",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                // Feeling & Note preview
                                if (fast.feeling != null || !fast.note.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        if (fast.feeling != null) {
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = FastingOrange.copy(alpha = 0.15f)
                                            ) {
                                                Text(
                                                    text = fast.feeling,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = FastingOrange,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        if (!fast.note.isNullOrBlank()) {
                                            Text(
                                                text = "\"${fast.note}\"",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                maxLines = 1,
                                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

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
