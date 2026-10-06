package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DailyFastBar
import com.example.ui.FastingStats
import com.example.ui.theme.FastingOrange
import com.example.ui.theme.RenewalEmerald
import com.example.ui.theme.TealAccent
import java.util.Locale

@Composable
fun StatsScreen(
    stats: FastingStats,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("stats_screen")
    ) {
        Text(
            text = "Analytics & Streaks",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Your consistency and metabolic performance metrics",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Streak Card Hero
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = FastingOrange.copy(alpha = 0.15f)
            ),
            border = BorderStroke(1.5.dp, FastingOrange.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth().testTag("streak_banner_card")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = FastingOrange,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Fasting Streak",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "${stats.currentStreakDays} DAY STREAK",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = FastingOrange
                        )
                        Text(
                            text = if (stats.currentStreakDays > 0)
                                "Keep the metabolic fire burning!"
                            else
                                "Complete a fast today to start your streak",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 4 Key Stats Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatSummaryCard(
                title = "Total Fasts",
                value = "${stats.totalFasts}",
                subtitle = "completed",
                icon = Icons.Default.EmojiEvents,
                iconTint = TealAccent,
                modifier = Modifier.weight(1f)
            )

            StatSummaryCard(
                title = "Total Hours",
                value = String.format(Locale.getDefault(), "%.1fh", stats.totalFastingHours),
                subtitle = "in fasting state",
                icon = Icons.Default.Timer,
                iconTint = FastingOrange,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatSummaryCard(
                title = "Average Fast",
                value = String.format(Locale.getDefault(), "%.1fh", stats.averageFastingHours),
                subtitle = "per session",
                icon = Icons.Default.TrendingUp,
                iconTint = RenewalEmerald,
                modifier = Modifier.weight(1f)
            )

            StatSummaryCard(
                title = "Longest Fast",
                value = String.format(Locale.getDefault(), "%.1fh", stats.longestFastHours),
                subtitle = "personal record",
                icon = Icons.Default.Star,
                iconTint = Color(0xFFA855F7),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Weekly 7-Day Fasting Chart Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth().testTag("weekly_fasting_chart")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Last 7 Days (Hours Fasted)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Goal: 16h",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bar Chart Visualizer
                WeeklyBarChart(
                    bars = stats.weeklyChartBars,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Fasting Milestones / Achievements
        Text(
            text = "Milestones & Badges",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))

        val milestones = listOf(
            Milestone(
                title = "First Step",
                desc = "Complete your first intermittent fast",
                achieved = stats.totalFasts >= 1
            ),
            Milestone(
                title = "Fat Burn Pioneer",
                desc = "Complete a 16+ hour fast",
                achieved = stats.longestFastHours >= 16f
            ),
            Milestone(
                title = "Autophagy Explorer",
                desc = "Reach 20+ hours of fasting",
                achieved = stats.longestFastHours >= 20f
            ),
            Milestone(
                title = "Consistency Champion",
                desc = "Reach a 3-day fasting streak",
                achieved = stats.currentStreakDays >= 3
            )
        )

        milestones.forEach { m ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (m.achieved)
                        MaterialTheme.colorScheme.surfaceVariant
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (m.achieved) RenewalEmerald else MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (m.achieved) Icons.Default.EmojiEvents else Icons.Default.Star,
                                contentDescription = null,
                                tint = if (m.achieved) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = m.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (m.achieved) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = m.desc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun StatSummaryCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun WeeklyBarChart(
    bars: List<DailyFastBar>,
    modifier: Modifier = Modifier
) {
    if (bars.isEmpty()) return

    val maxDisplayHours = 24f

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        bars.forEach { bar ->
            val barHeightFraction = (bar.hours / maxDisplayHours).coerceIn(0.04f, 1f)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                // Hours label
                Text(
                    text = if (bar.hours > 0f) String.format(Locale.getDefault(), "%.0f", bar.hours) else "",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = if (bar.hours >= bar.targetHours) RenewalEmerald else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Bar Canvas
                Box(
                    modifier = Modifier
                        .width(20.dp)
                        .height(100.dp * barHeightFraction)
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        .background(
                            when {
                                bar.hours >= bar.targetHours -> RenewalEmerald
                                bar.hours > 0f -> TealAccent
                                bar.isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                            }
                        )
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Day Label
                Text(
                    text = bar.dayLabel,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (bar.isToday) FontWeight.Bold else FontWeight.Normal,
                    color = if (bar.isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private data class Milestone(
    val title: String,
    val desc: String,
    val achieved: Boolean
)
