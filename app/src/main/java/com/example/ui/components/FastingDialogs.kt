package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FastingPlan
import com.example.ui.theme.FastingOrange
import com.example.ui.theme.TealAccent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun PlanSelectionDialog(
    currentPlanName: String,
    onSelectPlan: (FastingPlan) -> Unit,
    onCustomPlanClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Choose Fasting Protocol",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(FastingPlan.PRESETS) { plan ->
                    val isSelected = plan.name == currentPlanName
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected)
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                            else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = if (isSelected)
                            BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                        else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectPlan(plan) }
                            .testTag("plan_card_${plan.fastHours.toInt()}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = plan.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Text(
                                        text = plan.tag,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = plan.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                item {
                    OutlinedButton(
                        onClick = onCustomPlanClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_plan_button"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Configure Custom Fast Duration")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun CustomPlanDialog(
    initialHours: Float,
    onSave: (fastHours: Float, eatHours: Float) -> Unit,
    onDismiss: () -> Unit
) {
    var hours by remember { mutableFloatStateOf(initialHours.coerceIn(1f, 72f)) }
    val eatHours = (24f - hours).coerceAtLeast(0f)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Custom Fasting Goal", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${hours.toInt()} Hours",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                if (hours <= 24f) {
                    Text(
                        text = "Fasting: ${hours.toInt()}h • Eating window: ${eatHours.toInt()}h",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "Extended Multi-Day Fast (${String.format(Locale.getDefault(), "%.1f", hours / 24f)} days)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = FastingOrange
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Slider(
                    value = hours,
                    onValueChange = { hours = it },
                    valueRange = 1f..72f,
                    steps = 70,
                    modifier = Modifier.testTag("custom_hours_slider")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(hours, eatHours) },
                modifier = Modifier.testTag("save_custom_plan_btn")
            ) {
                Text("Set Goal")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EditStartTimeDialog(
    currentStartTimeMillis: Long,
    onConfirmNewStartTime: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val now = System.currentTimeMillis()
    var selectedOptionHoursAgo by remember { mutableFloatStateOf(0f) }
    var useManualOffset by remember { mutableStateOf(false) }
    var manualHoursInput by remember { mutableStateOf("") }

    val currentDiffHours = (now - currentStartTimeMillis) / (1000f * 60f * 60f)
    val timeFormatter = remember { SimpleDateFormat("hh:mm a, MMM dd", Locale.getDefault()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Adjust Fast Start Time", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text(
                    text = "Current start: ${timeFormatter.format(Date(currentStartTimeMillis))}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Quick Adjust (Started Earlier):",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))

                val quickOffsets = listOf(0.5f, 1f, 2f, 4f, 8f, 12f)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickOffsets.take(3).forEach { offset ->
                        FilterChip(
                            selected = !useManualOffset && selectedOptionHoursAgo == offset,
                            onClick = {
                                useManualOffset = false
                                selectedOptionHoursAgo = offset
                            },
                            label = { Text("${if (offset == 0.5f) "30m" else "${offset.toInt()}h"} ago") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickOffsets.drop(3).forEach { offset ->
                        FilterChip(
                            selected = !useManualOffset && selectedOptionHoursAgo == offset,
                            onClick = {
                                useManualOffset = false
                                selectedOptionHoursAgo = offset
                            },
                            label = { Text("${offset.toInt()}h ago") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = manualHoursInput,
                    onValueChange = {
                        manualHoursInput = it
                        useManualOffset = true
                    },
                    label = { Text("Or exact hours ago (e.g. 3.5)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth().testTag("manual_hours_ago_input"),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val hoursAgo = if (useManualOffset) {
                        manualHoursInput.toFloatOrNull() ?: selectedOptionHoursAgo
                    } else {
                        selectedOptionHoursAgo
                    }
                    val targetTime = (now - (hoursAgo * 3600 * 1000).toLong()).coerceAtMost(now)
                    onConfirmNewStartTime(targetTime)
                },
                modifier = Modifier.testTag("confirm_start_time_btn")
            ) {
                Text("Update Time")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun FinishFastDialog(
    elapsedHours: Float,
    targetHours: Float,
    onComplete: (feeling: String?, note: String?, weightKg: Float?) -> Unit,
    onCancelFast: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedFeeling by remember { mutableStateOf("Great") }
    var noteText by remember { mutableStateOf("") }
    var weightText by remember { mutableStateOf("") }

    val feelings = listOf(
        "Energetic" to "⚡ Energetic",
        "Great" to "🌟 Great",
        "Good" to "😊 Good",
        "Tired" to "😴 Tired",
        "Hungry" to "🤤 Hungry"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Complete Fast", fontWeight = FontWeight.Bold)
                Text(
                    text = "${String.format(Locale.getDefault(), "%.1f", elapsedHours)} hours fasted (Goal: ${targetHours.toInt()}h)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "How are you feeling?",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    feelings.forEach { (key, label) ->
                        FilterChip(
                            selected = selectedFeeling == key,
                            onClick = { selectedFeeling = key },
                            label = { Text(label, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    label = { Text("Weight (kg, optional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Notes / Reflections (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = onCancelFast,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.align(Alignment.CenterHorizontally).testTag("discard_fast_button")
                ) {
                    Text("Discard Fast (Do not save to stats)")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val weight = weightText.toFloatOrNull()
                    val note = noteText.ifBlank { null }
                    onComplete(selectedFeeling, note, weight)
                },
                modifier = Modifier.testTag("save_finished_fast_btn")
            ) {
                Text("Save Fast")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Continue Fasting")
            }
        }
    )
}

@Composable
fun WaterGoalDialog(
    currentGoalMl: Int,
    onSaveGoal: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var goalText by remember { mutableStateOf(currentGoalMl.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Daily Water Goal", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Adequate hydration prevents headaches and maintains electrolyte balance while fasting.")
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = goalText,
                    onValueChange = { goalText = it },
                    label = { Text("Target in ml (e.g. 2500)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("water_goal_input"),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val goal = goalText.toIntOrNull() ?: currentGoalMl
                    onSaveGoal(goal.coerceIn(500, 10000))
                },
                modifier = Modifier.testTag("save_water_goal_btn")
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
