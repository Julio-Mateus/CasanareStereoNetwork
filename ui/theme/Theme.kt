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
// Light Theme Colors
val md_theme_light_primary = Color(0xFFB71C1C) // Rojo principal
val md_theme_light_onPrimary = Color(0xFFFFFFFF) // Blanco sobre rojo
val md_theme_light_primaryContainer = Color(0xFFFFDAD6) // Contenedor de rojo claro
val md_theme_light_onPrimaryContainer = Color(0xFF410005) // Texto sobre contenedor de rojo claro
val md_theme_light_secondary = Color(0xFF775654) // Gris oscuro (secundario)
val md_theme_light_onSecondary = Color(0xFFFFFFFF) // Blanco sobre gris oscuro
val md_theme_light_secondaryContainer = Color(0xFFFFDAD6) // Contenedor de gris claro
val md_theme_light_onSecondaryContainer = Color(0xFF2C1514) // Texto sobre contenedor de gris claro
val md_theme_light_tertiary = Color(0xFF725B2E) // Amarillo oscuro
val md_theme_light_onTertiary = Color(0xFFFFFFFF) // Blanco sobre amarillo oscuro
val md_theme_light_tertiaryContainer = Color(0xFFFFE0A9) // Contenedor de amarillo claro
val md_theme_light_onTertiaryContainer = Color(0xFF271900) // Texto sobre contenedor de amarillo claro
val md_theme_light_error = Color(0xFFBA1A1A) // Rojo de error
val md_theme_light_errorContainer = Color(0xFFFFDAD6) // Contenedor de rojo de error
val md_theme_light_onError = Color(0xFFFFFFFF) // Blanco sobre rojo de error
val md_theme_light_onErrorContainer = Color(0xFF410002) // Texto sobre contenedor de rojo de error
val md_theme_light_background = Color(0xFFFFFBFF) // Blanco (fondo)
val md_theme_light_onBackground = Color(0xFF201A1A) // Negro sobre blanco
val md_theme_light_surface = Color(0xFFFFFBFF) // Blanco (superficie)
val md_theme_light_onSurface = Color(0xFF201A1A) // Negro sobre blanco
val md_theme_light_surfaceVariant = Color(0xFFF4DDDD) // Gris claro (variante de superficie)
val md_theme_light_onSurfaceVariant = Color(0xFF524343) // Gris oscuro sobre variante de superficie
val md_theme_light_outline = Color(0xFF847373) // Gris claro (borde)
val md_theme_light_inverseOnSurface = Color(0xFFFBEEED) // Blanco sobre superficie inversa
val md_theme_light_inverseSurface = Color(0xFF352F2F) // Gris oscuro (superficie inversa)
val md_theme_light_inversePrimary = Color(0xFFFFB3AE) // Rojo claro (primario inverso)
val md_theme_light_shadow = Color(0xFFFFFFFF) // Blanco sobre negro
val md_theme_light_surfaceTint = Color(0xFFB71C1C) // Rojo principal (tinte de superficie)
val md_theme_light_outlineVariant = Color(0xFFD6C2C2) // Gris claro (variante de borde)
val md_theme_light_scrim = Color(0xFF000000) // Negro (scrim)

// Dark Theme Colors
val md_theme_dark_primary = Color(0xFFFFB3AE) // Rojo claro (primario)
val md_theme_dark_onPrimary = Color(0xFF68000C) // Rojo oscuro sobre rojo claro
val md_theme_dark_primaryContainer = Color(0xFF930016) // Contenedor de rojo oscuro
val md_theme_dark_onPrimaryContainer = Color(0xFFFFDAD6) // Texto sobre contenedor de rojo oscuro
val md_theme_dark_secondary = Color(0xFFE7BDBC) // Gris claro (secundario)
val md_theme_dark_onSecondary = Color(0xFF442928) // Gris oscuro sobre gris claro
val md_theme_dark_secondaryContainer = Color(0xFF5D3F3E) // Contenedor de gris oscuro
val md_theme_dark_onSecondaryContainer = Color(0xFFFFDAD6) // Texto sobre contenedor de gris oscuro
val md_theme_dark_tertiary = Color(0xFFE0C38F) // Amarillo claro
val md_theme_dark_onTertiary = Color(0xFF402D05) // Amarillo oscuro sobre amarillo claro
val md_theme_dark_tertiaryContainer = Color(0xFF594319) // Contenedor de amarillo oscuro
val md_theme_dark_onTertiaryContainer = Color(0xFFFFE0A9) // Texto sobre contenedor de amarillo oscuro
val md_theme_dark_error = Color(0xFFFFB4AB) // Rojo claro de error
val md_theme_dark_errorContainer = Color(0xFF93000A) // Contenedor de rojo oscuro de error
val md_theme_dark_onError = Color(0xFF690005) // Rojo oscuro sobre rojo claro de error
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6) // Texto sobre contenedor de rojo oscuro de error
val md_theme_dark_background = Color(0xFF201A1A) // Negro (fondo)
val md_theme_dark_onBackground = Color(0xFFECE0DF) // Blanco sobre negro
val md_theme_dark_surface = Color(0xFF201A1A) // Negro (superficie)
val md_theme_dark_onSurface = Color(0xFFECE0DF) // Blanco sobre negro
val md_theme_dark_surfaceVariant = Color(0xFF524343) // Gris oscuro (variante de superficie)
val md_theme_dark_onSurfaceVariant = Color(0xFFD6C2C2) // Gris claro sobre variante de superficie
val md_theme_dark_outline = Color(0xFF9F8C8C) // Gris oscuro (borde)
val md_theme_dark_inverseOnSurface = Color(0xFF201A1A) // Negro sobre superficie inversa
val md_theme_dark_inverseSurface = Color(0xFFECE0DF) // Blanco (superficie inversa)
val md_theme_dark_inversePrimary = Color(0xFFB71C1C) // Rojo principal (primario inverso)
val md_theme_dark_shadow = Color(0xFFFFFFFF) // Blanco sobre negro
val md_theme_dark_surfaceTint = Color(0xFFFFB3AE) // Rojo claro (tinte de superficie)
val md_theme_dark_outlineVariant = Color(0xFF524343) // Gris oscuro (variante de borde)
val md_theme_dark_scrim = Color(0xFF000000) // Negro (scrim)

private val darkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary, // Rojo principal
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
    surfaceBright = md_theme_dark_shadow



)

private val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
    surfaceBright = md_theme_light_shadow
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
        darkTheme -> darkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes2,
        content = content
    )
}