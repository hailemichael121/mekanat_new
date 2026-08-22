package com.example.mekanat_new.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

private val DarkColorScheme = darkColorScheme(
    primary = BrandEmber,
    onPrimary = Color.White,
    primaryContainer = Color(0x28FF5A1F),
    onPrimaryContainer = BrandEmberInkDark,
    secondary = WayfindingTeal,
    onSecondary = Color.White,
    secondaryContainer = Color(0x240FB2A0),
    onSecondaryContainer = WayfindingTeal,
    tertiary = CrimsonPulse,
    onTertiary = Color.White,
    background = DarkBg,
    surface = DarkBgElev,
    surfaceVariant = DarkBgSunk,
    onBackground = DarkText,
    onSurface = DarkText,
    onSurfaceVariant = DarkTextDim,
    outline = DarkBorder,
    outlineVariant = DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = BrandEmber,
    onPrimary = Color.White,
    primaryContainer = Color(0x1FFF5A1F),
    onPrimaryContainer = BrandEmberInk,
    secondary = WayfindingTeal,
    onSecondary = Color.White,
    secondaryContainer = Color(0x180FB2A0),
    onSecondaryContainer = WayfindingTealInk,
    tertiary = CrimsonPulse,
    onTertiary = Color.White,
    background = LightBg,
    surface = LightBgElev,
    surfaceVariant = LightBgSunk,
    onBackground = LightText,
    onSurface = LightText,
    onSurfaceVariant = LightTextDim,
    outline = LightBorder,
    outlineVariant = LightBorder
)

@Composable
fun MekanatTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                // Light status bars & navigation bars: true when background is white/light, so icons are black.
                // false when background is dark/black, so icons are white.
                insetsController.isAppearanceLightStatusBars = !isDark
                insetsController.isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MekanatTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !darkTheme
                insetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
