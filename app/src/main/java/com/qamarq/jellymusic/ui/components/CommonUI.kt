package com.qamarq.jellymusic.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qamarq.jellymusic.ui.theme.ElovaireRadii
import com.qamarq.jellymusic.ui.theme.InkText
import com.qamarq.jellymusic.ui.theme.elovaireScaledSp
import android.os.Build
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import androidx.compose.runtime.compositionLocalOf

val LocalChromeHazeState = compositionLocalOf<HazeState?> { null }

@OptIn(ExperimentalHazeApi::class)
@Composable
fun DynamicBackdropSurface(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    overlayAlpha: Float = 0.7f,
    borderColor: Color? = null,
    showTopEdgeLine: Boolean = false,
    showBottomEdgeLine: Boolean = false,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val hazeState = LocalChromeHazeState.current
    val overlayColor = MaterialTheme.colorScheme.surface

    Box(
        modifier = modifier.clip(shape),
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && hazeState != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .hazeEffect(hazeState) {
                        blurRadius = 30.dp
                        backgroundColor = overlayColor.copy(alpha = overlayAlpha)
                        tints = listOf(HazeTint(overlayColor.copy(alpha = overlayAlpha)))
                        noiseFactor = 0.015f
                    },
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(overlayColor.copy(alpha = overlayAlpha)),
        )
        if (borderColor != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .border(1.dp, borderColor, shape),
            )
        }
        if (showTopEdgeLine) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(blurSurfaceBorderColor()),
            )
        }
        if (showBottomEdgeLine) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(blurSurfaceBorderColor()),
            )
        }
        content()
    }
}

@Composable
fun blurSurfaceBorderColor(): Color {
    return if (MaterialTheme.colorScheme.background.luminance() < 0.5f) {
        Color.White.copy(alpha = 0.12f)
    } else {
        InkText.copy(alpha = 0.08f)
    }
}

@Composable
fun ModuleCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ElovaireRadii.module),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 0.dp,
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = readableCardBorderColor(),
                    shape = RoundedCornerShape(ElovaireRadii.module),
                )
                .padding(18.dp),
        ) {
            content()
        }
    }
}

@Composable
fun readableCardBorderColor(): Color {
    return if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        InkText.copy(alpha = 0.1f)
    } else {
        Color.White.copy(alpha = 0.07f)
    }
}

@Composable
fun SectionTitleRow(
    title: String,
    subtitle: String? = null,
    compact: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 6.dp),
    ) {
        Text(
            text = title,
            style = if (compact) {
                MaterialTheme.typography.titleLarge.copy(fontSize = elovaireScaledSp(16f))
            } else {
                MaterialTheme.typography.headlineMedium
            },
        )
        if (!subtitle.isNullOrBlank()) {
            Text(
                text = subtitle,
                style = if (compact) {
                    MaterialTheme.typography.labelLarge
                } else {
                    MaterialTheme.typography.bodyLarge.copy(lineHeight = elovaireScaledSp(19.2f))
                },
                color = readableSecondaryTextColor(),
            )
        }
    }
}

@Composable
fun readableSecondaryTextColor(): Color {
    return if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        InkText.copy(alpha = 0.82f)
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.92f)
    }
}

@Composable
fun DividerLine(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .height(1.dp)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
    )
}
