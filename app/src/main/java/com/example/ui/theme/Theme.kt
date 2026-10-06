package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TealAccent,
    onPrimary = Color(0xFF003831),
    primaryContainer = TealPrimaryDark,
    onPrimaryContainer = Color(0xFF99F6E4),
    secondary = FastingOrange,
    onSecondary = Color(0xFF4A1800),
    secondaryContainer = Color(0xFF7C2D12),
    onSecondaryContainer = Color(0xFFFFDBC8),
    tertiary = WaterBlue,
    onTertiary = Color(0xFF003548),
    tertiaryContainer = WaterBlueBg,
    onTertiaryContainer = Color(0xFFBAE6FD),
    background = DarkBg,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = Color(0xFF1E293B)
)

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCCFBF1),
    onPrimaryContainer = Color(0xFF115E59),
    secondary = FastingOrange,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFEDD5),
    onSecondaryContainer = Color(0xFF9A3412),
    tertiary = WaterBlueDark,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE0F2FE),
    onTertiaryContainer = Color(0xFF0369A1),
    background = LightBg,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceElevated,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder,
    outlineVariant = Color(0xFFCBD5E1)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our tailored metabolic theme palette for brand distinction
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
