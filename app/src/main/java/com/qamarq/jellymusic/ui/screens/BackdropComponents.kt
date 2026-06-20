package com.qamarq.jellymusic.ui.screens

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

@OptIn(ExperimentalHazeApi::class)
@Composable
internal fun DynamicBackdropSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(0.dp),
    overlayAlpha: Float = 0.7f,
    borderColor: Color? = null,
    showTopEdgeLine: Boolean = false,
    showBottomEdgeLine: Boolean = false,
    content: @Composable androidx.compose.foundation.layout.BoxScope.() -> Unit = {},
) {
    val hazeState = LocalChromeHazeState.current
    val overlayColor = blurSurfaceOverlayColor()

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
internal fun ProgressiveChromeBackdrop(
    darkTheme: Boolean,
    edge: ProgressiveChromeEdge,
    modifier: Modifier = Modifier,
    overlayAlpha: Float? = null,
    flatOverlay: Boolean = false,
    showEdgeLine: Boolean = true,
) {
    DynamicBackdropSurface(
        modifier = modifier,
        overlayAlpha = overlayAlpha ?: 0.7f,
        showTopEdgeLine = edge == ProgressiveChromeEdge.Bottom && showEdgeLine,
        showBottomEdgeLine = edge == ProgressiveChromeEdge.Top && showEdgeLine,
    )
}

@OptIn(ExperimentalHazeApi::class)
@Composable
internal fun ChromeHazeLayer(
    darkTheme: Boolean,
    edge: ProgressiveChromeEdge,
    modifier: Modifier = Modifier,
    overlayAlpha: Float? = null,
    flatOverlay: Boolean = false,
    showEdgeLine: Boolean = true,
) {
    ProgressiveChromeBackdrop(
        darkTheme = darkTheme,
        edge = edge,
        overlayAlpha = overlayAlpha,
        flatOverlay = flatOverlay,
        showEdgeLine = showEdgeLine,
        modifier = modifier,
    )
}

@OptIn(ExperimentalHazeApi::class)
@Composable
internal fun FrostedTopBarBackground(
    darkTheme: Boolean,
    modifier: Modifier = Modifier,
    edge: ProgressiveChromeEdge = ProgressiveChromeEdge.Top,
    overlayAlpha: Float? = null,
    flatOverlay: Boolean = false,
    showEdgeLine: Boolean = true,
) {
    ChromeHazeLayer(
        darkTheme = darkTheme,
        edge = edge,
        overlayAlpha = overlayAlpha,
        flatOverlay = flatOverlay,
        showEdgeLine = showEdgeLine,
        modifier = modifier,
    )
}

@Composable
internal fun RegisterSharedTopBar(spec: SharedTopBarSpec) {
    val controller = LocalSharedTopBarController.current ?: return
    val registrationId = remember { Any() }
    val specSignature = remember(spec) {
        when (spec) {
            is SharedTopBarSpec.Unified -> "unified|${spec.showSettings}|${spec.supplementalActionIconResId ?: 0}|${spec.supplementalActionContentDescription.orEmpty()}"
            is SharedTopBarSpec.Back -> "back|${spec.title}|${spec.centeredTitle}"
            is SharedTopBarSpec.Detail -> "detail|${spec.title}|${spec.subtitle.orEmpty()}|${spec.actions.joinToString { "${it.iconResId}:${it.contentDescription}" }}"
        }
    }
    LaunchedEffect(controller, registrationId, specSignature, spec) {
        controller.registration = SharedTopBarRegistration(
            id = registrationId,
            spec = spec,
        )
    }
    DisposableEffect(controller, registrationId) {
        onDispose {
            if (controller.registration?.id == registrationId) {
                controller.registration = null
            }
        }
    }
}

@OptIn(ExperimentalHazeApi::class)
internal fun Modifier.playerFrostedSurface(
    tint: Color,
): Modifier = composed {
    val hazeState = LocalPlayerHazeState.current
    if (hazeState == null) {
        this
    } else {
        val tintIsDark = tint.luminance() < 0.44f
        hazeEffect(hazeState) {
            progressive = HazeProgressive.LinearGradient(
                startIntensity = 0.9f,
                endIntensity = 0.42f,
                preferPerformance = true,
            )
            blurRadius = 28.dp
            backgroundColor = tint.copy(alpha = if (tintIsDark) 0.18f else 0.14f)
            tints = listOf(
                HazeTint(tint.copy(alpha = if (tintIsDark) 0.28f else 0.2f)),
                HazeTint(
                    if (tintIsDark) {
                        Color.Black.copy(alpha = 0.14f)
                    } else {
                        Color.White.copy(alpha = 0.16f)
                    },
                ),
            )
            noiseFactor = 0.04f
        }
    }
}
