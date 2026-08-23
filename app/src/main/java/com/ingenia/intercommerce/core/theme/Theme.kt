package com.ingenia.intercommerce.core.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = InterOrange,       // Resaltes, botones
    secondary = InterDarkGray,   // Textos secundarios o acentos
    background = InterLightGray, // Fondo general
    surface = InterWhite,        // Tarjetas y TopBar
    onPrimary = InterWhite,
    onSecondary = InterWhite,
    onBackground = InterDarkGray,
    onSurface = InterDarkGray    // Texto principal sobre tarjetas y TopBar
)

@Composable
fun InterCommerceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb() // Status bar blanca
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true // Íconos oscuros en status bar
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
