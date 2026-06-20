package com.qamarq.jellymusic.ui.screens

import android.os.Build
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qamarq.jellymusic.ui.motion.ElovaireMotion
import com.qamarq.jellymusic.ui.theme.ElovaireSpacing
import com.qamarq.jellymusic.ui.theme.InkText
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.snapshotFlow

@Composable
internal fun statusBarInsetDp(): Dp {
    val density = LocalDensity.current
    return with(density) { WindowInsets.statusBars.getTop(this).toDp() }
}

@Composable
internal fun navigationBarInsetDp(): Dp {
    val density = LocalDensity.current
    return with(density) { WindowInsets.navigationBars.getBottom(this).toDp() }
}

@Composable
internal fun buttonNavigationScrollBoost(): Dp {
    val navigationInset = navigationBarInsetDp()
    return if (navigationInset >= 28.dp) {
        (navigationInset - 16.dp).coerceAtLeast(0.dp)
    } else {
        0.dp
    }
}

@Composable
internal fun screenContainerSizePx(): androidx.compose.ui.unit.IntSize {
    return LocalWindowInfo.current.containerSize
}

@Composable
internal fun topBarOccupiedHeight(): Dp = statusBarInsetDp() + ElovaireSpacing.topBarContentHeight

@Composable
internal fun detailTopBarOccupiedHeight(): Dp = statusBarInsetDp() + ElovaireSpacing.detailTopBarContentHeight

@Composable
internal fun sharedTopBarOccupiedHeight(): Dp =
    statusBarInsetDp() + maxOf(ElovaireSpacing.topBarContentHeight, ElovaireSpacing.detailTopBarContentHeight)

@Composable
internal fun bottomNavigationOccupiedHeight(): Dp {
    return navigationBarInsetDp() + ElovaireSpacing.bottomNavigationBodyHeight
}

@Composable
internal fun blurSurfaceOverlayColor(): Color = MaterialTheme.colorScheme.surface

@Composable
internal fun blurSurfaceBorderColor(): Color {
    return if (MaterialTheme.colorScheme.background.luminance() < 0.5f) {
        Color.White.copy(alpha = 0.12f)
    } else {
        InkText.copy(alpha = 0.08f)
    }
}

@Composable
internal fun Modifier.horizontalGestureSafe(): Modifier {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        this.systemGestureExclusion()
    } else {
        this
    }
}

@Composable
internal fun rememberElovaireLazyListState(vararg inputs: Any?): LazyListState {
    val cacheKey = remember(*inputs) {
        inputs.joinToString(separator = "|") { it?.toString().orEmpty() }
    }
    val cachedPosition = remember(cacheKey) {
        lazyListPositionCache[cacheKey] ?: (0 to 0)
    }
    val state = rememberSaveable(cacheKey, saver = LazyListState.Saver) {
        LazyListState(
            firstVisibleItemIndex = cachedPosition.first,
            firstVisibleItemScrollOffset = cachedPosition.second,
        )
    }
    LaunchedEffect(state, cacheKey) {
        snapshotFlow { state.firstVisibleItemIndex to state.firstVisibleItemScrollOffset }
            .distinctUntilChanged()
            .collect { position ->
                lazyListPositionCache[cacheKey] = position
            }
    }
    return state
}

@Composable
internal fun rememberElovaireLazyGridState(vararg inputs: Any?): LazyGridState {
    val cacheKey = remember(*inputs) {
        inputs.joinToString(separator = "|") { it?.toString().orEmpty() }
    }
    val cachedPosition = remember(cacheKey) {
        lazyGridPositionCache[cacheKey] ?: (0 to 0)
    }
    val state = rememberSaveable(cacheKey, saver = LazyGridState.Saver) {
        LazyGridState(
            firstVisibleItemIndex = cachedPosition.first,
            firstVisibleItemScrollOffset = cachedPosition.second,
        )
    }
    LaunchedEffect(state, cacheKey) {
        snapshotFlow { state.firstVisibleItemIndex to state.firstVisibleItemScrollOffset }
            .distinctUntilChanged()
            .collect { position ->
                lazyGridPositionCache[cacheKey] = position
            }
    }
    return state
}

@Composable
internal fun rememberElovaireScrollState(vararg inputs: Any?): androidx.compose.foundation.ScrollState {
    val cacheKey = remember(*inputs) {
        inputs.joinToString(separator = "|") { it?.toString().orEmpty() }
    }
    val cachedPosition = remember(cacheKey) { scrollPositionCache[cacheKey] ?: 0 }
    val state = rememberScrollState(cachedPosition)
    LaunchedEffect(state, cacheKey) {
        snapshotFlow { state.value }
            .distinctUntilChanged()
            .collect { value ->
                scrollPositionCache[cacheKey] = value
            }
    }
    return state
}

@Composable
internal fun Modifier.elovairePressBounce(
    interactionSource: MutableInteractionSource,
    label: String,
    pressedScale: Float = 0.9f,
): Modifier {
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        animationSpec = if (pressed) {
            ElovaireMotion.pressDownSpec()
        } else {
            ElovaireMotion.bounceSpringSpec()
        },
        label = label,
    )
    return this.scale(scale)
}

@Composable
internal fun Modifier.elovaireAnimateContentSize(): Modifier {
    return this.animateContentSize()
}
