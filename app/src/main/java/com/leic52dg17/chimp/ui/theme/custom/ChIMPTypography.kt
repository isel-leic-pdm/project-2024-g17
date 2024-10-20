package com.leic52dg17.chimp.ui.theme.custom

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val ChIMPTypography = Typography(
    bodySmall = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.Light,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp,
        fontSize = 22.sp
    ),

    displaySmall = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 38.sp,
        fontSize = 36.sp
    ),

    displayLarge = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 60.sp,
        fontSize = 58.sp,
        letterSpacing = 0.8.sp,
    )
)