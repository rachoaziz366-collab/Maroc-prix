package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DarkEmerald,
    onPrimary = Color(0xFF003920),
    primaryContainer = Color(0xFF005230),
    onPrimaryContainer = Color(0xFF86F8B6),
    secondary = DarkTerracotta,
    onSecondary = Color(0xFF5B1A00),
    secondaryContainer = Color(0xFF7E2A07),
    onSecondaryContainer = Color(0xFFFFDBCF),
    tertiary = DarkGold,
    background = DarkCanvas,
    surface = DarkSurface,
    onBackground = Color(0xFFE1E3DE),
    onSurface = Color(0xFFE1E3DE),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFC0C9C1),
    outline = Color(0xFF8A938C)
)

private val LightColorScheme = lightColorScheme(
    primary = MoroccanEmerald,
    onPrimary = Color.White,
    primaryContainer = MoroccanEmeraldContainer,
    onPrimaryContainer = MoroccanOnEmeraldContainer,
    secondary = MoroccanTerracotta,
    onSecondary = Color.White,
    secondaryContainer = MoroccanTerracottaContainer,
    onSecondaryContainer = MoroccanOnTerracottaContainer,
    tertiary = MoroccanGold,
    background = WarmCream,
    surface = SurfaceWhite,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFFEDF2EE),
    onSurfaceVariant = TextSecondaryDark,
    outline = BorderSoft
)

@Composable
fun MoroccoFoodPricesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Alias for compatibility
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MoroccoFoodPricesTheme(darkTheme = darkTheme, content = content)
}
