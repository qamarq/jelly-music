package com.qamarq.jellymusic.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import com.qamarq.jellymusic.R

// Primary font family used throughout the app.
private val GeistFamily = FontFamily(
    Font(R.font.geist_light, FontWeight.Light),
    Font(R.font.geist_regular, FontWeight.Normal),
    Font(R.font.geist_medium, FontWeight.Medium),
    Font(R.font.geist_semibold, FontWeight.SemiBold),
    Font(R.font.geist_bold, FontWeight.Bold),
)

// Base typography tokens before the user text-size multiplier is applied.
private val BaseTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = GeistFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.5).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = GeistFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = GeistFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = GeistFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = GeistFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        letterSpacing = 0.2.sp,
    ),
)

// App-wide multiplier used by the text-size slider in settings.
val LocalTextScale = staticCompositionLocalOf { 1f }

fun elovaireTypography(scaleFactor: Float): Typography {
    // Central place that scales all core text styles with the current text-size setting.
    return BaseTypography.copy(
        displayLarge = BaseTypography.displayLarge.scaled(scaleFactor),
        headlineMedium = BaseTypography.headlineMedium.scaled(scaleFactor),
        titleLarge = BaseTypography.titleLarge.scaled(scaleFactor),
        bodyLarge = BaseTypography.bodyLarge.scaled(scaleFactor),
        labelLarge = BaseTypography.labelLarge.scaled(scaleFactor),
    )
}

@Composable
fun elovaireScaledSp(baseSize: Float): TextUnit {
    // Helper for one-off font sizes that should still follow the text-size setting.
    return (baseSize * LocalTextScale.current).sp
}

private fun TextStyle.scaled(scaleFactor: Float): TextStyle {
    // Keeps font size, line height, and letter spacing moving together when text size changes.
    return copy(
        fontSize = fontSize.scaled(scaleFactor),
        lineHeight = lineHeight.scaled(scaleFactor),
        letterSpacing = letterSpacing.scaled(scaleFactor),
    )
}

private fun TextUnit.scaled(scaleFactor: Float): TextUnit {
    // Leaves unspecified values untouched and scales real sp values only.
    return if (isUnspecified) this else (value * scaleFactor).sp
}
