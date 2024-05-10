package com.codigodelsur.mlkit.core.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Indigo500,
    onPrimary = Color.White,
    primaryContainer = Indigo100,
    onPrimaryContainer = Color.Black,
    secondary = LightBlue500,
    onSecondary = Color.White,
    secondaryContainer = LightBlue100,
    onSecondaryContainer = Color.Black,
    tertiary = Teal200,
    onTertiary = Color.Black,
    tertiaryContainer = Teal50,
    onTertiaryContainer = Color.Black,
    background = Gray100,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    error = Red700,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = Indigo100,
    onPrimary = Color.Black,
    primaryContainer = Indigo500,
    onPrimaryContainer = Color.White,
    secondary = LightBlue100,
    onSecondary = Color.Black,
    secondaryContainer = LightBlue500,
    onSecondaryContainer = Color.White,
    tertiary = Teal700,
    onTertiary = Color.White,
    tertiaryContainer = Teal300,
    onTertiaryContainer = Color.White,
    background = Gray800,
    onBackground = Color.White,
    surface = Gray900,
    onSurface = Color.White,
    error = Red400,
    onError = Color.Black
)

@Composable
fun MlkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}