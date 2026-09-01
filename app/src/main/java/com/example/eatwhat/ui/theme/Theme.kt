package com.example.eatwhat.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Accent,
    onPrimary = Surface,
    primaryContainer = AccentTint,
    onPrimaryContainer = AccentDeep,
    secondary = Warm,
    onSecondary = Surface,
    secondaryContainer = WarmTint,
    onSecondaryContainer = WarmDeep,
    tertiary = AccentStrong,
    background = Bg,
    onBackground = Fg,
    surface = Surface,
    onSurface = Fg,
    surfaceVariant = Hover,
    onSurfaceVariant = Muted,
    outline = BorderStrong,
    outlineVariant = Border,
    error = Danger,
    errorContainer = DangerTint,
)

@Composable
fun EatwhatTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
