package com.qamarq.jellymusic.ui.screens

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import com.qamarq.jellymusic.ui.motion.ElovaireMotion
import kotlin.math.roundToInt

internal fun String?.isAlbumDetailRoute(): Boolean = this == "$ALBUM_ROUTE/{albumId}"

internal fun tileExpandEnterTransition(expandOrigin: ExpandOrigin): EnterTransition {
    val albumTransitionDuration = ElovaireMotion.Emphasized + 200
    return fadeIn(
        animationSpec = ElovaireMotion.standardTween(durationMillis = albumTransitionDuration),
    ) +
        scaleIn(
            animationSpec = ElovaireMotion.standardTween(
                durationMillis = albumTransitionDuration,
                easing = ElovaireMotion.EmphasizedDecelerate,
            ),
            initialScale = 0.76f,
            transformOrigin = expandOrigin.toTransformOrigin(),
        ) +
        slideInHorizontally(
            animationSpec = ElovaireMotion.standardTween(
                durationMillis = albumTransitionDuration,
                easing = ElovaireMotion.EmphasizedDecelerate,
            ),
            initialOffsetX = { fullWidth ->
                ((expandOrigin.xFraction - 0.5f) * fullWidth * 0.24f).roundToInt()
            },
        ) +
        slideInVertically(
            animationSpec = ElovaireMotion.standardTween(
                durationMillis = albumTransitionDuration,
                easing = ElovaireMotion.EmphasizedDecelerate,
            ),
            initialOffsetY = { fullHeight ->
                ((expandOrigin.yFraction - 0.5f) * fullHeight * 0.24f).roundToInt()
            },
        )
}

internal fun tileExpandExitTransition(expandOrigin: ExpandOrigin): ExitTransition {
    val albumTransitionDuration = ElovaireMotion.Emphasized + 200
    return fadeOut(
        animationSpec = ElovaireMotion.standardTween(
            durationMillis = albumTransitionDuration,
            easing = FastOutLinearInEasing,
        ),
    ) +
        scaleOut(
            animationSpec = ElovaireMotion.standardTween(
                durationMillis = albumTransitionDuration,
                easing = ElovaireMotion.EmphasizedAccelerate,
            ),
            targetScale = 0.82f,
            transformOrigin = expandOrigin.toTransformOrigin(),
        ) +
        slideOutHorizontally(
            animationSpec = ElovaireMotion.standardTween(
                durationMillis = albumTransitionDuration,
                easing = ElovaireMotion.EmphasizedAccelerate,
            ),
            targetOffsetX = { fullWidth ->
                ((expandOrigin.xFraction - 0.5f) * fullWidth * 0.24f).roundToInt()
            },
        ) +
        slideOutVertically(
            animationSpec = ElovaireMotion.standardTween(
                durationMillis = albumTransitionDuration,
                easing = ElovaireMotion.EmphasizedAccelerate,
            ),
            targetOffsetY = { fullHeight ->
                ((expandOrigin.yFraction - 0.5f) * fullHeight * 0.24f).roundToInt()
            },
        )
}

internal fun resolveForwardEnterTransition(
    transition: NavHostTransitionResolution,
    expandOrigin: ExpandOrigin,
): EnterTransition = when {
    transition.targetRoute == PLAYER_ROUTE -> EnterTransition.None
    transition.targetUsesTileExpand -> tileExpandEnterTransition(expandOrigin)
    transition.topLevelTransition.isTopLevelTransition -> ElovaireMotion.topLevelEnter(
        forward = transition.topLevelTransition.isForward,
    )
    transition.targetRoute.isAlbumDetailRoute() -> ElovaireMotion.albumDetailForwardEnter()
    transition.targetUsesDetailTransition -> ElovaireMotion.detailForwardEnter()
    else -> ElovaireMotion.fullScreenForwardEnter()
}

internal fun resolveForwardExitTransition(
    transition: NavHostTransitionResolution,
): ExitTransition = when {
    transition.targetRoute == PLAYER_ROUTE -> ExitTransition.None
    transition.targetUsesTileExpand -> fadeOut(animationSpec = ElovaireMotion.fadeFast())
    transition.topLevelTransition.isTopLevelTransition -> ElovaireMotion.topLevelExit(
        forward = transition.topLevelTransition.isForward,
    )
    transition.targetRoute.isAlbumDetailRoute() -> ElovaireMotion.albumDetailForwardExit()
    transition.targetUsesDetailTransition -> ElovaireMotion.detailForwardExit()
    else -> ElovaireMotion.fullScreenForwardExit()
}

internal fun resolvePopEnterTransition(
    transition: NavHostTransitionResolution,
): EnterTransition = when {
    transition.initialRoute == PLAYER_ROUTE -> EnterTransition.None
    transition.initialUsesTileExpand -> fadeIn(animationSpec = ElovaireMotion.fadeMedium())
    transition.topLevelTransition.isTopLevelTransition -> ElovaireMotion.topLevelEnter(
        forward = transition.topLevelTransition.isForward,
    )
    transition.targetRoute.isAlbumDetailRoute() -> ElovaireMotion.albumDetailBackEnter()
    transition.targetUsesDetailTransition -> ElovaireMotion.detailBackEnter()
    else -> ElovaireMotion.fullScreenBackEnter()
}

internal fun resolvePopExitTransition(
    transition: NavHostTransitionResolution,
    expandOrigin: ExpandOrigin,
): ExitTransition = when {
    transition.initialRoute == PLAYER_ROUTE -> ExitTransition.None
    transition.initialUsesTileExpand -> tileExpandExitTransition(expandOrigin)
    transition.topLevelTransition.isTopLevelTransition -> ElovaireMotion.topLevelExit(
        forward = transition.topLevelTransition.isForward,
    )
    transition.initialRoute.isAlbumDetailRoute() -> ElovaireMotion.albumDetailBackExit()
    transition.initialUsesDetailTransition -> ElovaireMotion.detailBackExit()
    else -> ElovaireMotion.fullScreenBackExit()
}

internal fun clearTopLevelScrollPositionMemory(route: String) {
    val prefixes = topLevelScrollCachePrefixes[route].orEmpty()
    if (prefixes.isEmpty()) return
    lazyListPositionCache.keys.removeIf { cacheKey ->
        prefixes.any { prefix -> cacheKey.contains(prefix) }
    }
    lazyGridPositionCache.keys.removeIf { cacheKey ->
        prefixes.any { prefix -> cacheKey.contains(prefix) }
    }
    scrollPositionCache.keys.removeIf { cacheKey ->
        prefixes.any { prefix -> cacheKey.contains(prefix) }
    }
}
