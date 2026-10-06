package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FastingStage
import com.example.ui.theme.RenewalEmerald
import java.util.Locale

@Composable
fun StagesScreen(
    elapsedHours: Float,
    isFastingActive: Boolean,
    modifier: Modifier = Modifier
) {
    var expandedStageNumber by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("stages_screen")
    ) {
        Text(
            text = "Metabolic Stages",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = if (isFastingActive)
                "Currently at ${String.format(Locale.getDefault(), "%.1f", elapsedHours)} hours into fast"
            else
                "Explore what happens inside your body at every hour of fasting",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(FastingStage.ALL_STAGES) { stage ->
                val isCompleted = isFastingActive && stage.isPassed(elapsedHours)
                val isActive = isFastingActive && stage.isActive(elapsedHours)
                val isExpanded = expandedStageNumber == stage.stageNumber

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            isActive -> stage.color.copy(alpha = 0.12f)
                            isCompleted -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        }
                    ),
                    border = when {
                        isActive -> BorderStroke(1.5.dp, stage.color)
                        isCompleted -> BorderStroke(1.dp, RenewalEmerald.copy(alpha = 0.5f))
                        else -> null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .clickable {
                            expandedStageNumber = if (isExpanded) null else stage.stageNumber
                        }
                        .testTag("stage_card_${stage.stageNumber}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = when {
                                        isActive -> stage.color
                                        isCompleted -> RenewalEmerald
                                        else -> MaterialTheme.colorScheme.surface
                                    },
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        if (isCompleted) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Completed",
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        } else if (isActive) {
                                            Icon(
                                                imageVector = Icons.Default.RadioButtonChecked,
                                                contentDescription = "Active",
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.Lock,
                                                contentDescription = "Upcoming",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = stage.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Text(
                                        text = "${stage.startHours.toInt()}h – ${stage.endHours.toInt()}h of Fast",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = stage.color,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            // Active / Status Pill
                            if (isActive) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = stage.color,
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    Text(
                                        text = "ACTIVE NOW",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (isExpanded) "Collapse" else "Expand",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Progress line for current active stage
                        if (isActive) {
                            val stageProgress = stage.progress(elapsedHours)
                            Spacer(modifier = Modifier.height(10.dp))
                            LinearProgressIndicator(
                                progress = { stageProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = stage.color,
                                trackColor = stage.color.copy(alpha = 0.2f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stage.headline,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stage.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Expandable Scientific Benefits
                        AnimatedVisibility(visible = isExpanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                Text(
                                    text = "Cellular & Metabolic Benefits:",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = stage.color
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                stage.scientificBenefits.forEach { benefit ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 3.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = null,
                                            tint = stage.color,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = benefit,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
