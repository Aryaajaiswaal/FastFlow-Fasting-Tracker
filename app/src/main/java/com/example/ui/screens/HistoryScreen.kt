package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.theme.FastingOrange
import com.example.ui.theme.RenewalEmerald
import com.example.ui.theme.TealAccent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    fasts: List<FastingRecord>,
    onDeleteFast: (Long) -> Unit,
    onStartFastClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val completedFasts = fasts.filter { it.status == "COMPLETED" }
    val dateFormat = remember { SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()) }
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("history_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Fasting History",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${completedFasts.size} completed sessions logged",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (completedFasts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HourglassEmpty,
                        contentDescription = "No Fasting History",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Fasts Logged Yet",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your completed fasts and metabolic logs will be saved right here. Begin your journey today!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onStartFastClick,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("start_first_fast_btn")
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Start Fast Now")
                    }
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(completedFasts, key = { it.id }) { fast ->
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
                            .testTag("history_item_${fast.id}")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Top Row: Date & Plan
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = dateFormat.format(Date(fast.startTimeMillis)),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surface
                                ) {
                                    Text(
                                        text = fast.planName,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Middle Row: Big duration and status
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
                                                    text = "Goal Met",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = RenewalEmerald,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }

                                IconButton(
                                    onClick = { onDeleteFast(fast.id) },
                                    modifier = Modifier.size(28.dp).testTag("delete_fast_${fast.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Delete Fast Log",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            // Times range
                            val endStr = if (fast.endTimeMillis != null) timeFormat.format(Date(fast.endTimeMillis)) else "Now"
                            val startStr = timeFormat.format(Date(fast.startTimeMillis))
                            Text(
                                text = "$startStr – $endStr",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // Feeling & Notes if logged
                            if (fast.feeling != null || fast.note != null || fast.weightKg != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (fast.feeling != null) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
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

                                    if (fast.weightKg != null) {
                                        Text(
                                            text = "${fast.weightKg} kg",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                if (!fast.note.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "\"${fast.note}\"",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    )
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
}
