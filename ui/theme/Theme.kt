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

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB71C1C),
    secondary = Color(0xFFFFD600),
    tertiary = Color.White,
    onPrimary = Color.White, // Acento (blanco sobre rojo oscuro)
    background = Color.White,
    surface = Color(0xFF000000), // Negro
    onBackground  = Color(0xFFEEEEEE)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFB71C1C), // Rojo oscuro
    secondary = Color(0xFFFFD600), // Dorado
    onPrimary = Color.White, // Acento (blanco sobre rojo oscuro)
    onSecondary = Color(0xFF000000), // Negro sobre dorado
    background = Color(0xFF333333),// Gris oscuro
    surface = Color(0xFFEEEEEE),// Gris claro
    onSurface = Color(0xFF000000),// Negro sobre gris claro
    onBackground  = Color(0xFFEEEEEE)

)


@Composable
fun CasanareStereoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val color = if(darkTheme){
        DarkColorScheme
    }else{
        LightColorScheme
    }
    val colorScheme = when {
        /*
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        */
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        //colorScheme = color,
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes2,
        content = content
    )
}