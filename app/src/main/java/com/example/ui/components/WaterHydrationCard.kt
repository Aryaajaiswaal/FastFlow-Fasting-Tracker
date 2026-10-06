package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WaterBlue
import com.example.ui.theme.WaterBlueDark

@Composable
fun WaterHydrationCard(
    currentMl: Int,
    goalMl: Int,
    onAddWater: (Int) -> Unit,
    onUndo: () -> Unit,
    onEditGoal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (goalMl > 0) (currentMl.toFloat() / goalMl.toFloat()).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "waterProgress")
    val percentage = (progress * 100).toInt()

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("water_hydration_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = "Hydration Tracker",
                        tint = WaterBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Water Intake",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onEditGoal,
                        modifier = Modifier.size(32.dp).testTag("edit_water_goal_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Water Goal",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    if (currentMl > 0) {
                        IconButton(
                            onClick = onUndo,
                            modifier = Modifier.size(32.dp).testTag("undo_water_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Undo,
                                contentDescription = "Undo Last Water",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Amount stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$currentMl",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = WaterBlue
                    )
                    Text(
                        text = " / $goalMl ml",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = if (percentage >= 100) WaterBlue else MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = WaterBlue,
                trackColor = MaterialTheme.colorScheme.surface
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Quick add buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilledTonalButton(
                    onClick = { onAddWater(250) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("add_water_250_btn"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalDrink,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "+250 ml", style = MaterialTheme.typography.labelMedium)
                }

                FilledTonalButton(
                    onClick = { onAddWater(500) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("add_water_500_btn"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "+500 ml", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
