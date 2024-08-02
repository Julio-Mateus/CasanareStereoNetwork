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
    primary = Color2,
    secondary = Color3,
    tertiary = Color4
)

private val LightColorScheme = lightColorScheme(
    primary = Color2,
    secondary = Color3,
    tertiary = Color4,



    background = Color6,
    surface = Color6,
    onPrimary = Color7,
    onSecondary = Color1,
    onTertiary = Color6,
    onBackground = Color5,
    onSurface = Color(0xFFFFFFFF),

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