package com.example.tolkienquizapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = MiddleEarthGold,
    secondary = ElvenSilver,
    tertiary = DeepForestGreen,
    background = NazgulBlack,
    surface = MordorDark,
    onPrimary = NazgulBlack,
    onSecondary = NazgulBlack,
    onTertiary = GondorWhite,
    onBackground = GondorWhite,
    onSurface = GondorWhite
)

private val LightColorScheme = lightColorScheme(
    primary = DeepForestGreen,
    secondary = MiddleEarthGold,
    tertiary = ElvenSilver,
    background = Parchment,
    surface = Parchment,
    onPrimary = GondorWhite,
    onSecondary = NazgulBlack,
    onTertiary = NazgulBlack,
    onBackground = NazgulBlack,
    onSurface = NazgulBlack
)

@Composable
fun TolkienQuizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}