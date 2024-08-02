package com.jcmateus.casanarestereo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.jcmateus.casanarestereo.R

// Set of Material typography styles to start with

private val Inter = FontFamily(
    androidx.compose.ui.text.font.Font(R.font.inter_regular),
    androidx.compose.ui.text.font.Font(R.font.inter_medium, FontWeight.W500),
    androidx.compose.ui.text.font.Font(R.font.inter_bold, FontWeight.Bold),
    androidx.compose.ui.text.font.Font(R.font.inter_black, FontWeight.Black),
    androidx.compose.ui.text.font.Font(R.font.inter_thin, FontWeight.Thin),
    androidx.compose.ui.text.font.Font(R.font.inter_light, FontWeight.ExtraLight),
    androidx.compose.ui.text.font.Font(R.font.inter_extrabold, FontWeight.SemiBold),
    androidx.compose.ui.text.font.Font(R.font.inter_semibold, FontWeight.SemiBold),
    androidx.compose.ui.text.font.Font(R.font.inter_extralight, FontWeight.ExtraLight),
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,

        ),


    )

