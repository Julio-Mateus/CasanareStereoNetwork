package com.jcmateus.casanarestereo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlin.text.compareTo

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB71C1C), // Rojo principal
    onPrimary = Color.White, // Blanco sobre rojo (acento)
    secondary = Color(0xFFEEEEEE), // Gris claro (secundario)
    onSecondary = Color.Black, // Negro sobre gris claro
    background = Color(0xFF121212), // Gris oscuro (fondo)
    onBackground = Color.White, // Blanco sobre gris oscuro
    surface = Color(0xFF212121), // Gris oscuro (superficie)
    onSurface = Color.White // Blanco sobre gris oscuro
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFB71C1C), // Rojo principal
    onPrimary = Color.White, // Blanco sobre rojo (acento)
    secondary = Color(0xFF333333), // Gris oscuro (secundario)
    onSecondary = Color.White, // Blanco sobre gris oscuro
    background = Color.White, // Blanco (fondo)
    onBackground = Color.Black, // Negro sobre blanco
    surface = Color(0xFFEEEEEE), // Gris claro (superficie)
    onSurface = Color.Black // Negro sobre gris claro
)

@Composable
fun CasanareStereoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes2,
        content = content
    )
}