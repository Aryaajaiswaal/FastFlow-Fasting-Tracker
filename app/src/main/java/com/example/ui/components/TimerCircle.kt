package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FastingStage
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.FastingAmber
import com.example.ui.theme.FastingOrange
import com.example.ui.theme.RenewalEmerald
import com.example.ui.theme.TealAccent
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TimerCircle(
    elapsedMillis: Long,
    targetHours: Float,
    isFastingActive: Boolean,
    planName: String,
    currentStage: FastingStage?,
    onPlanClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 280.dp
) {
    val targetMillis = (targetHours * 3600 * 1000).toLong()
    val rawProgress = if (targetMillis > 0) elapsedMillis.toFloat() / targetMillis.toFloat() else 0f
    val clampedProgress = rawProgress.coerceIn(0f, 1f)
    val isOverTarget = rawProgress >= 1f

    val animatedProgress by animateFloatAsState(
        targetValue = if (isFastingActive) clampedProgress else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "timerProgress"
    )

    // Formatted time strings
    val totalSeconds = (elapsedMillis / 1000)
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    // Arc color scheme
    val trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    val startColor = if (isOverTarget) RenewalEmerald else TealAccent
    val endColor = if (isOverTarget) Color(0xFF34D399) else FastingOrange

    Box(
        modifier = modifier
            .size(size)
            .testTag("timer_circle_container"),
        contentAlignment = Alignment.Center
    ) {
        // Draw the circular canvas track and animated progress arc
        Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            val strokeWidth = 18.dp.toPx()
            val canvasSize = this.size
            val diameter = canvasSize.minDimension - strokeWidth
            val topLeft = Offset(
                (canvasSize.width - diameter) / 2f,
                (canvasSize.height - diameter) / 2f
            )
            val arcSize = Size(diameter, diameter)

            // Background circle track
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress Arc
            if (isFastingActive && animatedProgress > 0f) {
                val sweepAngle = 360f * animatedProgress
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(startColor, endColor, startColor),
                        center = Offset(canvasSize.width / 2f, canvasSize.height / 2f)
                    ),
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Glowing indicator thumb dot at head of the arc
                val angleRad = Math.toRadians((-90.0 + sweepAngle))
                val radius = diameter / 2f
                val centerX = canvasSize.width / 2f
                val centerY = canvasSize.height / 2f
                val thumbX = (centerX + radius * cos(angleRad)).toFloat()
                val thumbY = (centerY + radius * sin(angleRad)).toFloat()

                // Glow ring
                drawCircle(
                    color = endColor.copy(alpha = 0.35f),
                    radius = strokeWidth * 0.9f,
                    center = Offset(thumbX, thumbY)
                )
                // Solid center dot
                drawCircle(
                    color = Color.White,
                    radius = strokeWidth * 0.45f,
                    center = Offset(thumbX, thumbY)
                )
            }
        }

        // Inner Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Plan Selector Pill
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .testTag("plan_selector_pill")
                    .clickable { onPlanClick() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Select Plan",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = " $planName",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Stopwatch Time Display
            Text(
                text = if (isFastingActive) formattedTime else "00:00:00",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.testTag("timer_stopwatch_text")
            )

            // Status label
            if (isFastingActive) {
                if (isOverTarget) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Target Reached",
                            tint = RenewalEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = " Goal Reached! (${(rawProgress * 100).toInt()}%)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = RenewalEmerald
                        )
                    }
                } else {
                    val remainingSeconds = ((targetMillis - elapsedMillis) / 1000).coerceAtLeast(0)
                    val remHours = remainingSeconds / 3600
                    val remMins = (remainingSeconds % 3600) / 60
                    Text(
                        text = "${remHours}h ${remMins}m remaining",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Text(
                    text = "Ready to start",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Current Stage Chip
            if (isFastingActive && currentStage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = currentStage.color.copy(alpha = 0.18f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, currentStage.color.copy(alpha = 0.4f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = currentStage.title,
                            tint = currentStage.color,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = currentStage.title,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = currentStage.color,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
