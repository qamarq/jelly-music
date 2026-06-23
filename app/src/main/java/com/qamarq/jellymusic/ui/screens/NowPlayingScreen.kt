package com.qamarq.jellymusic.ui.screens

import android.Manifest
import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Xml
import java.io.File
import androidx.annotation.DrawableRes
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.ui.viewinterop.AndroidView
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.framework.CastButtonFactory
import com.qamarq.jellymusic.BuildConfig
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import com.qamarq.jellymusic.R
import com.qamarq.jellymusic.core.AppContainer
import com.qamarq.jellymusic.data.changelog.ChangelogRelease
import com.qamarq.jellymusic.data.changelog.ChangelogRepository
import com.qamarq.jellymusic.data.library.LibraryContentState
import com.qamarq.jellymusic.data.library.LibraryScanState
import com.qamarq.jellymusic.data.library.LibraryUiState
import com.qamarq.jellymusic.data.lyrics.LyricsLine
import com.qamarq.jellymusic.data.lyrics.LyricsLookupMode
import com.qamarq.jellymusic.data.lyrics.LyricsPayload
import com.qamarq.jellymusic.data.lyrics.LyricsResult
import com.qamarq.jellymusic.data.lyrics.LyricsService
import com.qamarq.jellymusic.data.tags.AlbumTagEditRequest
import com.qamarq.jellymusic.data.tags.AlbumTagEditorService
import com.qamarq.jellymusic.data.tags.AlbumTagMatchSuggestion
import com.qamarq.jellymusic.data.playback.EqualizerDspConfig
import com.qamarq.jellymusic.data.playback.EqualizerDspModel
import com.qamarq.jellymusic.data.playback.PlaybackCollectionKind
import com.qamarq.jellymusic.data.playback.PlaybackManager
import com.qamarq.jellymusic.data.playback.PlaybackNowPlayingState
import com.qamarq.jellymusic.data.playback.PlaybackProgressState
import com.qamarq.jellymusic.data.playback.PlaybackQueueState
import com.qamarq.jellymusic.data.playback.PlaybackTransportState
import com.qamarq.jellymusic.data.playback.PlaybackRepeatMode
import com.qamarq.jellymusic.data.playback.PlaybackUiState
import com.qamarq.jellymusic.data.playback.PlaybackVolumeState
import com.qamarq.jellymusic.data.playback.RecentPlaybackState
import com.qamarq.jellymusic.domain.model.Album
import com.qamarq.jellymusic.domain.model.AppLanguage
import com.qamarq.jellymusic.domain.model.EqSettings
import com.qamarq.jellymusic.domain.model.Playlist
import com.qamarq.jellymusic.domain.model.ReverbProfile
import com.qamarq.jellymusic.domain.model.SearchHistoryEntry
import com.qamarq.jellymusic.domain.model.SearchHistoryKind
import com.qamarq.jellymusic.domain.model.Song
import com.qamarq.jellymusic.domain.model.SpaciousnessMode
import com.qamarq.jellymusic.domain.model.TextSizePreset
import com.qamarq.jellymusic.domain.model.ThemeMode
import com.qamarq.jellymusic.ui.components.ArtworkImage
import com.qamarq.jellymusic.ui.components.rememberArtworkBitmap
import com.qamarq.jellymusic.ui.components.rememberArtworkGradient
import com.qamarq.jellymusic.ui.motion.ElovaireAnimatedContent
import com.qamarq.jellymusic.ui.motion.ElovaireAnimatedVisibility
import com.qamarq.jellymusic.ui.motion.ElovaireMotion
import com.qamarq.jellymusic.ui.motion.SyncElovaireMotionScale
import com.qamarq.jellymusic.ui.motion.rememberSystemAnimationScale
import com.qamarq.jellymusic.ui.i18n.LocalAppLanguage
import com.qamarq.jellymusic.ui.i18n.MiscPhrase
import com.qamarq.jellymusic.ui.i18n.SettingsLanguageCopy
import com.qamarq.jellymusic.ui.i18n.UiPhrase
import com.qamarq.jellymusic.ui.i18n.commonUiCopy
import com.qamarq.jellymusic.ui.i18n.formatCountLabel
import com.qamarq.jellymusic.ui.i18n.homeCopy
import com.qamarq.jellymusic.ui.i18n.localizedAllSongsSource
import com.qamarq.jellymusic.ui.i18n.localizedCountLabel
import com.qamarq.jellymusic.ui.i18n.miscPhrase
import com.qamarq.jellymusic.ui.i18n.playLabel
import com.qamarq.jellymusic.ui.i18n.playingFromPrefix
import com.qamarq.jellymusic.ui.i18n.queueTitle
import com.qamarq.jellymusic.ui.i18n.searchCopy
import com.qamarq.jellymusic.ui.i18n.searchSortModeLabel
import com.qamarq.jellymusic.ui.i18n.settingsCopy
import com.qamarq.jellymusic.ui.i18n.uiPhrase
import com.qamarq.jellymusic.ui.i18n.displayLabel
import com.qamarq.jellymusic.ui.screens.tags.AlbumTagEditorScreen
import com.qamarq.jellymusic.ui.theme.ElovaireRadii
import com.qamarq.jellymusic.ui.theme.ElovaireSpacing
import com.qamarq.jellymusic.ui.theme.AboutCardButtonAccent
import com.qamarq.jellymusic.ui.theme.DestructiveRed
import com.qamarq.jellymusic.ui.theme.elovaireScaledSp
import com.qamarq.jellymusic.ui.theme.rememberElovaireOverscrollFactory
import com.qamarq.jellymusic.ui.theme.InkText
import com.qamarq.jellymusic.ui.theme.RoseAccent
import com.qamarq.jellymusic.ui.theme.ToggleEnabledGreen
import java.net.URL
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.ceil
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.roundToInt
import kotlin.math.pow
import org.xmlpull.v1.XmlPullParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

@Composable
internal fun NowPlayingScreen(
    playbackManager: PlaybackManager,
    playerUiState: PlayerUiState,
    enrichedSongsById: Map<Long, Song>,
    isFavorite: Boolean,
    playlists: List<Playlist>,
    lyricsUiState: LyricsUiState,
    activeLyricsLineIndex: Int,
    playbackProgress: PlaybackProgressState,
    onBack: () -> Unit,
    onOpenCurrentAlbum: (Long) -> Unit,
    onTogglePlayback: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    onCycleRepeatMode: () -> Unit,
    onToggleShuffle: () -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onAddCurrentSongToPlaylist: (Long, Song) -> Unit,
    onCreatePlaylist: (String) -> Long,
    onQueueItemSelected: (Int) -> Unit,
    onQueueItemRemoved: (Int) -> Unit,
    onOpenEqualizer: () -> Unit,
    eqSettings: EqSettings,
    onSpaciousnessChanged: (Float) -> Unit,
    onVolumeChanged: (Float) -> Unit,
    transitionSnapshot: NowPlayingTransitionSnapshot?,
    onTransitionProgress: (Float, PlayerOverlayTransitionState) -> Unit = { _, _ -> },
    onClearTransitionSnapshot: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val liveCurrentSong = playerUiState.currentSong
    val liveDisplaySong = liveCurrentSong?.let { enrichedSongsById[it.id] ?: it }
    val playerHazeState = rememberHazeState()
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    var playerDismissTriggered by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(liveCurrentSong?.id) {
        if (liveCurrentSong == null) {
            if (!playerDismissTriggered) {
                playerDismissTriggered = true
                onBack()
            }
        } else {
            playerDismissTriggered = false
        }
    }
    val appBackground = MaterialTheme.colorScheme.background
    val gradient = rememberArtworkGradient(liveCurrentSong?.artUri).value
    val artwork = rememberArtworkBitmap(liveCurrentSong?.artUri, size = 768)
    // The shared-element expand-from-mini-player snapshot must only ever drive the very first
    // expand of this screen instance, not re-trigger every time prev/next happens to land back
    // on the song that snapshot was originally captured for.
    var snapshotConsumed by remember { mutableStateOf(false) }
    val activeTransitionSnapshot = remember(transitionSnapshot, liveCurrentSong?.id, snapshotConsumed) {
        if (snapshotConsumed) {
            null
        } else {
            transitionSnapshot?.takeIf {
                it.songId == liveCurrentSong?.id &&
                    it.barBounds.isValidTransitionBounds &&
                    it.artworkBounds.isValidTransitionBounds
            }
        }
    }
    val transitionProgress = remember {
        Animatable(if (activeTransitionSnapshot != null) 0f else 1f)
    }
    var transitionState by remember {
        mutableStateOf(
            if (activeTransitionSnapshot != null) {
                PlayerOverlayTransitionState.Expanding
            } else {
                PlayerOverlayTransitionState.Expanded
            },
        )
    }
    LaunchedEffect(transitionState) {
        snapshotFlow { transitionProgress.value }.collect { value ->
            onTransitionProgress(value, transitionState)
        }
    }
    val expandSettleAnimationSpec = remember {
        tween<Float>(
            durationMillis = 360,
            easing = LinearOutSlowInEasing,
        )
    }
    val collapseSettleAnimationSpec = remember {
        tween<Float>(
            durationMillis = 260,
            easing = FastOutSlowInEasing,
        )
    }
    var interactiveTransitionProgress by remember(liveCurrentSong?.id) { mutableStateOf<Float?>(null) }
    var dismissAnimationRunning by remember(liveCurrentSong?.id) { mutableStateOf(false) }
    val effectiveTransitionProgress = interactiveTransitionProgress ?: transitionProgress.value
    val transitionInFlight = transitionState != PlayerOverlayTransitionState.Expanded || interactiveTransitionProgress != null || dismissAnimationRunning
    val adaptivePalette = remember(gradient, appBackground) {
        buildPlayerAdaptivePalette(
            gradient = gradient,
            appBackground = appBackground,
            darkTheme = false,
        )
    }
    val tintColor by animateColorAsState(
        targetValue = adaptivePalette.tintColor,
        animationSpec = tween(320, easing = LinearOutSlowInEasing),
        label = "player_tint_color",
    )
    val baseSurface by animateColorAsState(
        targetValue = adaptivePalette.backdropBase,
        animationSpec = tween(320, easing = LinearOutSlowInEasing),
        label = "player_backdrop_base",
    )
    val contentColor by animateColorAsState(
        targetValue = adaptivePalette.contentColor,
        animationSpec = tween(260, easing = LinearOutSlowInEasing),
        label = "player_content_color",
    )
    val secondaryContentColor by animateColorAsState(
        targetValue = adaptivePalette.secondaryContentColor,
        animationSpec = tween(260, easing = LinearOutSlowInEasing),
        label = "player_secondary_content_color",
    )
    val currentSong = liveCurrentSong
    val displaySong = liveDisplaySong
    val language = LocalAppLanguage.current
    val playingFromText = remember(language, playerUiState.sourceLabel, currentSong?.album) {
        val source = playerUiState.sourceLabel
            ?.takeIf { it.isNotBlank() }
            ?: currentSong?.album?.takeIf { it.isNotBlank() }
            ?: localizedAllSongsSource(language)
        "${playingFromPrefix(language)} $source"
    }
    var showLyricsSheet by remember(currentSong?.id) { mutableStateOf(false) }
    var showQueueSheet by remember(currentSong?.id) { mutableStateOf(false) }
    var showAddToPlaylistDialog by remember(currentSong?.id) { mutableStateOf(false) }
    var queueStatusText by remember(currentSong?.id) { mutableStateOf<String?>(null) }
    LaunchedEffect(queueStatusText) {
        if (queueStatusText != null) {
            delay(1500L)
            queueStatusText = null
        }
    }
    DisposableEffect(currentSong?.id) {
        onDispose {
            playbackManager.cancelScrub()
        }
    }

    suspend fun settlePlayerTransition(
        targetValue: Float,
        animationSpec: AnimationSpec<Float>,
        targetState: PlayerOverlayTransitionState,
    ) {
        val startValue = interactiveTransitionProgress ?: transitionProgress.value
        interactiveTransitionProgress = null
        transitionState = if (targetValue >= startValue) {
            PlayerOverlayTransitionState.Expanding
        } else {
            PlayerOverlayTransitionState.Collapsing
        }
        transitionProgress.stop()
        transitionProgress.snapTo(startValue)
        transitionProgress.animateTo(
            targetValue = targetValue,
            animationSpec = animationSpec,
        )
        transitionState = targetState
    }

    LaunchedEffect(currentSong?.id, activeTransitionSnapshot?.songId) {
        if (currentSong == null || dismissAnimationRunning || transitionState == PlayerOverlayTransitionState.Collapsing) {
            return@LaunchedEffect
        }
        if (activeTransitionSnapshot != null && transitionProgress.value < 1f) {
            settlePlayerTransition(
                targetValue = 1f,
                animationSpec = expandSettleAnimationSpec,
                targetState = PlayerOverlayTransitionState.Expanded,
            )
            snapshotConsumed = true
            onClearTransitionSnapshot()
        } else if (activeTransitionSnapshot == null && transitionProgress.value != 1f) {
            transitionProgress.stop()
            transitionProgress.snapTo(1f)
            transitionState = PlayerOverlayTransitionState.Expanded
        }
    }

    val dismissNowPlaying: ((() -> Unit)?) -> Unit = { afterDismiss ->
        if (!dismissAnimationRunning && transitionState != PlayerOverlayTransitionState.Compact) {
            dismissAnimationRunning = true
            scope.launch {
                settlePlayerTransition(
                    targetValue = 0f,
                    animationSpec = collapseSettleAnimationSpec,
                    targetState = PlayerOverlayTransitionState.Compact,
                )
                if (afterDismiss != null) {
                    afterDismiss()
                } else {
                    onBack()
                }
            }
        }
    }

    BackHandler(enabled = !showLyricsSheet) {
        dismissNowPlaying(null)
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .then(
                if (transitionInFlight) {
                    Modifier
                } else {
                    Modifier.hazeSource(playerHazeState)
                },
            ),
    ) {
        val screenWidthPx = with(density) { maxWidth.toPx() }
        val screenHeightPx = with(density) { maxHeight.toPx() }
        val fullSurfaceBounds = remember(screenWidthPx, screenHeightPx) {
            androidx.compose.ui.geometry.Rect(
                left = 0f,
                top = 0f,
                right = screenWidthPx,
                bottom = screenHeightPx,
            )
        }
        val fallbackSourceBounds = remember(screenWidthPx, screenHeightPx, density) {
            val horizontalInset = with(density) { 16.dp.toPx() }
            val bottomInset = with(density) { 88.dp.toPx() }
            val barHeight = with(density) { 72.dp.toPx() }
            androidx.compose.ui.geometry.Rect(
                left = horizontalInset,
                top = screenHeightPx - bottomInset - barHeight,
                right = screenWidthPx - horizontalInset,
                bottom = screenHeightPx - bottomInset,
            )
        }
        val sourceSurfaceBounds = (activeTransitionSnapshot?.barBounds ?: fallbackSourceBounds).coerceWithin(fullSurfaceBounds)
        val sourceArtworkBounds = (activeTransitionSnapshot?.artworkBounds ?: fallbackSourceBounds).coerceWithin(fullSurfaceBounds)
        val statusBarTopInsetPx = WindowInsets.statusBars.getTop(density).toFloat()
        val fallbackTargetArtworkBounds = remember(screenWidthPx, statusBarTopInsetPx, density) {
            val horizontalInset = with(density) { 20.dp.toPx() }
            val artworkSize = screenWidthPx - (horizontalInset * 2f)
            val topInset = statusBarTopInsetPx + with(density) { 70.dp.toPx() }
            androidx.compose.ui.geometry.Rect(
                left = horizontalInset,
                top = topInset,
                right = horizontalInset + artworkSize,
                bottom = topInset + artworkSize,
            )
        }
        val targetArtworkBounds = fallbackTargetArtworkBounds.coerceWithin(fullSurfaceBounds)
        val animatedSurfaceBounds = lerpRect(sourceSurfaceBounds, fullSurfaceBounds, effectiveTransitionProgress)
        val artworkRevealProgress = ((effectiveTransitionProgress - 0.08f) / 0.92f).coerceIn(0f, 1f)
        val contentRevealProgress = ((effectiveTransitionProgress - 0.22f) / 0.78f).coerceIn(0f, 1f)
        val playerContentAlpha = if (showLyricsSheet) 0f else contentRevealProgress
        val playerSurfaceCorner = lerpFloat(with(density) { ElovaireRadii.card.toPx() }, 0f, effectiveTransitionProgress)
        val sharedArtworkBounds = lerpRect(sourceArtworkBounds, targetArtworkBounds, artworkRevealProgress).coerceWithin(fullSurfaceBounds)
        val volumeSectionProgress = ((effectiveTransitionProgress - 0.22f) / 0.16f).coerceIn(0f, 1f)
        val actionsSectionProgress = ((effectiveTransitionProgress - 0.34f) / 0.16f).coerceIn(0f, 1f)
        val transportSectionProgress = ((effectiveTransitionProgress - 0.48f) / 0.16f).coerceIn(0f, 1f)
        val progressSectionProgress = ((effectiveTransitionProgress - 0.6f) / 0.15f).coerceIn(0f, 1f)
        val metadataSectionProgress = ((effectiveTransitionProgress - 0.72f) / 0.14f).coerceIn(0f, 1f)
        val useSharedArtworkOverlay =
            activeTransitionSnapshot != null &&
                transitionState != PlayerOverlayTransitionState.Expanded &&
                sourceArtworkBounds.isValidTransitionBounds &&
                targetArtworkBounds.isValidTransitionBounds &&
                sharedArtworkBounds.isValidTransitionBounds &&
                artwork.value != null

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    baseSurface.copy(alpha = 0.68f * effectiveTransitionProgress.coerceIn(0f, 1f)),
                ),
        )
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = animatedSurfaceBounds.left.roundToInt(),
                        y = animatedSurfaceBounds.top.roundToInt(),
                    )
                }
                .width(with(density) { animatedSurfaceBounds.width.toDp() })
                .height(with(density) { animatedSurfaceBounds.height.toDp() })
                .clip(RoundedCornerShape(with(density) { playerSurfaceCorner.toDp() }))
                .background(baseSurface)
                .graphicsLayer {
                    clip = true
                },
        ) {
        val backgroundArtworkBitmap = artwork.value
        if (backgroundArtworkBitmap != null) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (transitionInFlight) {
                    Image(
                        bitmap = backgroundArtworkBitmap,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = 1.04f
                                scaleY = 1.04f
                            }
                            .blur(56.dp),
                        alpha = 0.92f,
                    )
                } else {
                    Image(
                        bitmap = backgroundArtworkBitmap,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = 1.08f
                                scaleY = 1.08f
                            }
                            .blur(116.dp),
                        alpha = 0.98f,
                    )
                    Image(
                        bitmap = backgroundArtworkBitmap,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = 1.03f
                                scaleY = 1.03f
                                alpha = 0.34f
                            }
                            .blur(48.dp),
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            tintColor.copy(alpha = 0.38f),
                            baseSurface.copy(alpha = 0.44f),
                            baseSurface.copy(alpha = 0.7f),
                            baseSurface.copy(alpha = 0.9f),
                        ),
                    ),
                ),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            gradient.first().copy(alpha = 0.18f),
                            Color.Transparent,
                        ),
                        radius = 1200f,
                    ),
                ),
        )

        CompositionLocalProvider(LocalPlayerHazeState provides playerHazeState) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(start = 20.dp, top = 18.dp, end = 20.dp, bottom = 20.dp)
                    .alpha(playerContentAlpha),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
            if (currentSong == null) {
                Spacer(modifier = Modifier.fillMaxSize())
                return@Column
            }

            val centeredInfoWidth = 0.95f
            val nowPlayingTitleTopGap = ElovaireSpacing.nowPlayingTitleTopGap
            val nowPlayingTitleBottomGap = ElovaireSpacing.nowPlayingTitleBottomGap
            val transportShowsPause = remember(currentSong.id, playerUiState.transportShowsPause) {
                playerUiState.transportShowsPause
            }
            val spaciousnessEnabled = eqSettings.spaciousness > 0.02f
            val favoriteAlpha by animateFloatAsState(
                targetValue = if (showQueueSheet) 0f else 1f,
                animationSpec = tween(80),
                label = "queue_favorite_alpha",
            )
            val transportAlpha by animateFloatAsState(
                targetValue = if (showQueueSheet) 0f else 1f,
                animationSpec = tween(80),
                label = "queue_transport_alpha",
            )
            val animatedArtworkCornerRadius by animateDpAsState(
                targetValue = if (showQueueSheet) 10.dp else ElovaireRadii.module,
                animationSpec = tween(ElovaireMotion.Standard, easing = FastOutSlowInEasing),
                label = "queue_artwork_corner_radius",
            )
            fun Modifier.nowPlayingDismissGesture(): Modifier = pointerInput(currentSong.id) {
                var dragDistance = 0f
                val dismissDistance = with(density) { 320.dp.toPx() }
                detectVerticalDragGestures(
                    onVerticalDrag = { change, dragAmount ->
                        if (dismissAnimationRunning) return@detectVerticalDragGestures
                        val continuingDismissDrag = dragDistance > 0f
                        if (dragAmount <= 0f && !continuingDismissDrag) return@detectVerticalDragGestures
                        change.consume()
                        dragDistance = (dragDistance + dragAmount).coerceAtLeast(0f)
                        if (dragDistance <= 0f) {
                            interactiveTransitionProgress = 1f
                            transitionState = PlayerOverlayTransitionState.Expanded
                            return@detectVerticalDragGestures
                        }
                        transitionState = PlayerOverlayTransitionState.Dragging
                        interactiveTransitionProgress =
                            (1f - (dragDistance / dismissDistance)).coerceIn(0f, 1f)
                    },
                    onDragEnd = {
                        val progress = interactiveTransitionProgress ?: 1f
                        dragDistance = 0f
                        if (progress < 0.6f) {
                            dismissNowPlaying(null)
                        } else {
                            scope.launch {
                                settlePlayerTransition(
                                    targetValue = 1f,
                                    animationSpec = expandSettleAnimationSpec,
                                    targetState = PlayerOverlayTransitionState.Expanded,
                                )
                            }
                        }
                    },
                    onDragCancel = {
                        if (dismissAnimationRunning) return@detectVerticalDragGestures
                        dragDistance = 0f
                        scope.launch {
                            settlePlayerTransition(
                                targetValue = 1f,
                                animationSpec = expandSettleAnimationSpec,
                                targetState = PlayerOverlayTransitionState.Expanded,
                            )
                        }
                    },
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(34.dp),
                    ) {
                        HeaderIconButton(
                            iconResId = R.drawable.ic_lucide_chevron_down,
                            contentDescription = "Minimize",
                            showBackground = false,
                            tint = contentColor,
                            onClick = { dismissNowPlaying(null) },
                            modifier = Modifier.align(Alignment.CenterStart),
                        )
                        Row(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 64.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_lucide_circle_play),
                                contentDescription = null,
                                tint = secondaryContentColor,
                                modifier = Modifier.size(14.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = playingFromText,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
                                color = secondaryContentColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        CastButton(modifier = Modifier.align(Alignment.CenterEnd))
                    }
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .nowPlayingDismissGesture(),
                    ) {
                        val expandedArtworkWidth = maxWidth
                        val compactArtworkWidth = maxWidth * 0.38f
                        val animatedArtworkWidth by animateDpAsState(
                            targetValue = if (showQueueSheet) compactArtworkWidth else expandedArtworkWidth,
                            animationSpec = tween(ElovaireMotion.Standard, easing = FastOutSlowInEasing),
                            label = "queue_artwork_width",
                        )
                        val compactContentStart = compactArtworkWidth + 18.dp
                        if (!useSharedArtworkOverlay) {
                            AnimatedContent(
                                targetState = currentSong.id,
                                transitionSpec = {
                                    val fromIndex = playerUiState.queue.indexOfFirst { it.id == initialState }
                                    val toIndex = playerUiState.queue.indexOfFirst { it.id == targetState }
                                    ElovaireMotion.directionalContentSwapTransform(forward = toIndex >= fromIndex)
                                },
                                label = "player_artwork_content",
                            ) { songId ->
                                val animatedSong = playerUiState.queue.firstOrNull { it.id == songId } ?: currentSong
                                ArtworkImage(
                                    uri = animatedSong.artUri,
                                    title = animatedSong.title,
                                    modifier = Modifier
                                        .width(animatedArtworkWidth)
                                        .aspectRatio(1f),
                                    cornerRadius = animatedArtworkCornerRadius,
                                    requestedSizePx = 1024,
                                )
                            }
                        }
                        androidx.compose.animation.AnimatedVisibility(
                            visible = showQueueSheet,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = compactContentStart, end = 2.dp),
                            enter = fadeIn(animationSpec = ElovaireMotion.contentFadeInSpec()) +
                                slideInVertically(
                                    animationSpec = ElovaireMotion.offsetSoft(durationMillis = ElovaireMotion.Standard),
                                    initialOffsetY = { it / 5 },
                                ),
                            exit = fadeOut(animationSpec = ElovaireMotion.contentFadeOutSpec()),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        ExplicitTitleText(
                                            title = currentSong.title,
                                            isExplicit = currentSong.isExplicit,
                                            style = MaterialTheme.typography.displayLarge.copy(fontSize = elovaireScaledSp(NOW_PLAYING_TITLE_TEXT_SIZE_SP)),
                                            color = contentColor,
                                            maxLines = 1,
                                            overflow = TextOverflow.Clip,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .basicMarquee(
                                                    iterations = Int.MAX_VALUE,
                                                    animationMode = MarqueeAnimationMode.Immediately,
                                                    repeatDelayMillis = 2500,
                                                    initialDelayMillis = 2500,
                                                    velocity = 24.dp,
                                                ),
                                        )
                                    }
                                    Text(
                                        text = currentSong.artist,
                                        style = MaterialTheme.typography.titleLarge.copy(fontSize = elovaireScaledSp(NOW_PLAYING_ARTIST_TEXT_SIZE_SP)),
                                        color = secondaryContentColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Clip,
                                        modifier = Modifier.basicMarquee(
                                            iterations = Int.MAX_VALUE,
                                            animationMode = MarqueeAnimationMode.Immediately,
                                            repeatDelayMillis = 2500,
                                            initialDelayMillis = 2500,
                                            velocity = 24.dp,
                                        ),
                                    )
                                }
                                CompactQueuePlaybackSummary(
                                    playbackManager = playbackManager,
                                    currentSongId = currentSong.id,
                                    freezeUpdates = transitionInFlight,
                                    contentColor = contentColor,
                                    secondaryContentColor = secondaryContentColor,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = nowPlayingTitleTopGap, bottom = nowPlayingTitleBottomGap),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(centeredInfoWidth)
                        .align(Alignment.Center)
                        .graphicsLayer {
                            alpha = if (showQueueSheet) 0f else metadataSectionProgress
                        },
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    currentSong.takeIf { it.albumId > 0L }?.albumId?.let { albumId ->
                                        dismissNowPlaying {
                                            onOpenCurrentAlbum(albumId)
                                        }
                                    }
                                },
                            ),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AnimatedContent(
                            targetState = currentSong.id,
                            transitionSpec = {
                                val fromIndex = playerUiState.queue.indexOfFirst { it.id == initialState }
                                val toIndex = playerUiState.queue.indexOfFirst { it.id == targetState }
                                ElovaireMotion.directionalContentSwapTransform(forward = toIndex >= fromIndex)
                            },
                            label = "player_metadata_content",
                            modifier = Modifier.weight(1f),
                        ) { songId ->
                            val animatedSong = playerUiState.queue.firstOrNull { it.id == songId } ?: currentSong
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    ExplicitTitleText(
                                        title = animatedSong.title,
                                        isExplicit = animatedSong.isExplicit,
                                        style = MaterialTheme.typography.displayLarge.copy(fontSize = elovaireScaledSp(NOW_PLAYING_TITLE_TEXT_SIZE_SP)),
                                        color = contentColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Clip,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .basicMarquee(
                                                iterations = Int.MAX_VALUE,
                                                animationMode = MarqueeAnimationMode.Immediately,
                                                repeatDelayMillis = 2500,
                                                initialDelayMillis = 2500,
                                                velocity = 28.dp,
                                            ),
                                    )
                                }
                                Text(
                                    text = animatedSong.artist,
                                    style = MaterialTheme.typography.titleLarge.copy(fontSize = elovaireScaledSp(NOW_PLAYING_ARTIST_TEXT_SIZE_SP)),
                                    color = secondaryContentColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .alpha(favoriteAlpha * if (showQueueSheet) 0f else metadataSectionProgress),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        FavoriteSongButton(
                            isFavorite = isFavorite,
                            tint = contentColor,
                            onClick = { onToggleFavorite(currentSong.id) },
                        )
                    }
                }
            }

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth(centeredInfoWidth)
                    .align(Alignment.CenterHorizontally)
                    .weight(1f),
            ) {
                val queueSheetTopExtension = 462.dp
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    alpha = if (showQueueSheet) 0f else progressSectionProgress
                                },
                            verticalArrangement = Arrangement.spacedBy(0.dp),
                        ) {
                            NowPlayingProgressSummary(
                                playbackManager = playbackManager,
                                currentSongId = currentSong.id,
                                freezeUpdates = transitionInFlight,
                                format = displaySong?.audioFormat ?: currentSong.audioFormat,
                                quality = displaySong?.audioQuality ?: currentSong.audioQuality,
                                contentColor = contentColor,
                                secondaryContentColor = secondaryContentColor,
                                modifier = Modifier.fillMaxWidth(),
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center,
                            ) {
                                Column(
                                    modifier = Modifier.graphicsLayer {
                                        alpha = if (showQueueSheet) 0f else transportSectionProgress * transportAlpha
                                    },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(22.dp),
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        PlayerTransportButton(
                                            iconResId = R.drawable.ic_elovaire_backward_filled,
                                            contentDescription = "Previous",
                                            tint = contentColor,
                                            iconSize = 42.dp,
                                            onClick = onSkipPrevious,
                                        )
                                        PlayerTransportButton(
                                            iconResId = if (transportShowsPause) R.drawable.ic_elovaire_pause_filled else R.drawable.ic_lucide_play,
                                            contentDescription = if (transportShowsPause) "Pause" else "Play",
                                            tint = contentColor,
                                            iconSize = 46.dp,
                                            onClick = onTogglePlayback,
                                        )
                                        PlayerTransportButton(
                                            iconResId = R.drawable.ic_elovaire_forward_filled,
                                            contentDescription = "Next",
                                            tint = contentColor,
                                            iconSize = 42.dp,
                                            onClick = onSkipNext,
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                alpha = if (showQueueSheet) 0f else actionsSectionProgress
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        PlayerSecondaryActionButton(
                            iconResId = R.drawable.ic_lucide_align_left,
                            label = "",
                            iconSize = 20.dp,
                            tint = contentColor,
                            showBackground = false,
                            onClick = {
                                showQueueSheet = false
                                showAddToPlaylistDialog = false
                                showLyricsSheet = true
                            },
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        PlayerSecondaryActionButton(
                            iconResId = repeatModeIconRes(playerUiState.repeatMode),
                            label = "",
                            iconSize = 20.dp,
                            tint = contentColor,
                            showBackground = playerUiState.repeatMode != PlaybackRepeatMode.Off,
                            onClick = onCycleRepeatMode,
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        PlayerSecondaryActionButton(
                            iconResId = R.drawable.ic_lucide_plus,
                            label = "",
                            iconSize = 20.dp,
                            tint = contentColor,
                            showBackground = showAddToPlaylistDialog,
                            onClick = {
                                showLyricsSheet = false
                                showQueueSheet = false
                                showAddToPlaylistDialog = true
                            },
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        PlayerSecondaryActionButton(
                            iconResId = R.drawable.ic_lucide_list_music,
                            label = "",
                            iconSize = 20.dp,
                            tint = contentColor,
                            showBackground = showQueueSheet,
                            onClick = {
                                showLyricsSheet = false
                                showAddToPlaylistDialog = false
                                showQueueSheet = !showQueueSheet
                            },
                        )
                    }
                }

                if (showQueueSheet) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {},
                            ),
                    )
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = showQueueSheet,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    enter = fadeIn(animationSpec = ElovaireMotion.contentFadeInSpec()) +
                        scaleIn(
                            initialScale = 0.94f,
                            transformOrigin = TransformOrigin(1f, 1f),
                            animationSpec = ElovaireMotion.standardTween(
                                durationMillis = ElovaireMotion.Standard,
                                easing = FastOutSlowInEasing,
                            ),
                        ) +
                        slideInHorizontally(
                            initialOffsetX = { it / 14 },
                            animationSpec = ElovaireMotion.standardTween(
                                durationMillis = ElovaireMotion.Standard,
                                easing = FastOutSlowInEasing,
                            ),
                        ) +
                        slideInVertically(
                            initialOffsetY = { it / 14 },
                            animationSpec = ElovaireMotion.standardTween(
                                durationMillis = ElovaireMotion.Standard,
                                easing = FastOutSlowInEasing,
                            ),
                        ),
                    exit = fadeOut(animationSpec = ElovaireMotion.contentFadeOutSpec()) +
                        scaleOut(
                            targetScale = 0.98f,
                            transformOrigin = TransformOrigin(1f, 1f),
                            animationSpec = ElovaireMotion.standardTween(
                                durationMillis = ElovaireMotion.Quick,
                            ),
                        ),
                ) {
                    QueueSheet(
                        queue = playerUiState.queue,
                        currentIndex = playerUiState.currentIndex,
                        playlists = playlists,
                        playlistSongsById = enrichedSongsById,
                        currentSong = currentSong,
                        tint = contentColor,
                        secondaryTint = secondaryContentColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(maxHeight + queueSheetTopExtension)
                            .align(Alignment.BottomCenter),
                        onSongSelected = onQueueItemSelected,
                        onQueueItemRemoved = onQueueItemRemoved,
                        shuffleEnabled = playerUiState.shuffleEnabled,
                        onToggleShuffle = {
                            queueStatusText = if (playerUiState.shuffleEnabled) {
                                "Shuffle | Disabled"
                            } else {
                                "Shuffle | Enabled"
                            }
                            onToggleShuffle()
                        },
                        spaciousnessEnabled = spaciousnessEnabled,
                        onToggleSpaciousness = {
                            val enabling = !spaciousnessEnabled
                            queueStatusText = if (enabling) "Spaciousness | Enabled" else null
                            onSpaciousnessChanged(if (enabling) 0.5f else 0f)
                        },
                        spaciousnessAmount = eqSettings.spaciousness.coerceIn(0f, 1f),
                        onSpaciousnessAmountChanged = onSpaciousnessChanged,
                        onOpenEqualizer = onOpenEqualizer,
                        onAddSongToPlaylist = onAddCurrentSongToPlaylist,
                        onCreatePlaylist = onCreatePlaylist,
                        statusText = queueStatusText,
                        onDismiss = { showQueueSheet = false },
                        isPlaying = playerUiState.isPlaying,
                    )
                }
            }

            VolumeControlBar(
                volume = playerUiState.volume,
                contentColor = contentColor,
                onVolumeChanged = onVolumeChanged,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = volumeSectionProgress
                    }
                    .fillMaxWidth(centeredInfoWidth)
                    .align(Alignment.CenterHorizontally),
            )
            }
        }
        }
        if (useSharedArtworkOverlay && currentSong != null) {
            val sharedArtworkCornerRadius = with(density) {
                lerpFloat(
                    ElovaireRadii.artworkSmall.toPx(),
                    ElovaireRadii.module.toPx(),
                    artworkRevealProgress,
                ).toDp()
            }
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = sharedArtworkBounds.left.roundToInt(),
                            y = sharedArtworkBounds.top.roundToInt(),
                        )
                    }
                    .width(with(density) { sharedArtworkBounds.width.toDp() })
                    .height(with(density) { sharedArtworkBounds.height.toDp() })
                    .clipToBounds()
                    .graphicsLayer {
                        clip = true
                        shape = RoundedCornerShape(sharedArtworkCornerRadius)
                    }
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            ) {
                val sharedArtworkBitmap = artwork.value
                if (sharedArtworkBitmap != null) {
                    Image(
                        bitmap = sharedArtworkBitmap,
                        contentDescription = currentSong.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = showLyricsSheet,
            enter = fadeIn(animationSpec = ElovaireMotion.standardTween(durationMillis = ElovaireMotion.Standard, easing = LinearOutSlowInEasing)) +
                slideInVertically(
                    animationSpec = ElovaireMotion.standardTween(durationMillis = ElovaireMotion.Standard, easing = FastOutSlowInEasing),
                    initialOffsetY = { it / 12 },
                ) +
                scaleIn(
                    animationSpec = ElovaireMotion.standardTween(durationMillis = ElovaireMotion.Standard, easing = FastOutSlowInEasing),
                    initialScale = 0.985f,
                    transformOrigin = TransformOrigin(0.5f, 1f),
                ),
            exit = fadeOut(animationSpec = ElovaireMotion.standardTween(durationMillis = ElovaireMotion.Quick, easing = FastOutLinearInEasing)) +
                slideOutVertically(
                    animationSpec = ElovaireMotion.standardTween(durationMillis = ElovaireMotion.Quick, easing = FastOutSlowInEasing),
                    targetOffsetY = { it / 18 },
                ) +
                scaleOut(
                    animationSpec = ElovaireMotion.standardTween(durationMillis = ElovaireMotion.Quick, easing = FastOutLinearInEasing),
                    targetScale = 0.992f,
                    transformOrigin = TransformOrigin(0.5f, 1f),
                ),
        ) {
            LyricsOverlay(
                song = currentSong,
                lyricsUiState = lyricsUiState,
                activeLyricsLineIndex = activeLyricsLineIndex,
                playbackProgress = playbackProgress,
                tintColor = baseSurface.copy(alpha = 0.66f),
                contentColor = contentColor,
                secondaryContentColor = secondaryContentColor,
                onSeekTo = playbackManager::seekTo,
                onHideLyrics = { showLyricsSheet = false },
            )
        }
        if (showAddToPlaylistDialog) {
            AddToPlaylistPickerDialog(
                playlists = playlists,
                playlistSongsById = enrichedSongsById,
                onDismiss = { showAddToPlaylistDialog = false },
                onPlaylistSelected = { playlistId ->
                    currentSong?.let { onAddCurrentSongToPlaylist(playlistId, it) }
                    showAddToPlaylistDialog = false
                },
                onCreatePlaylist = onCreatePlaylist,
            )
        }
    }
}

@Composable
internal fun rememberRenderedPlaybackProgress(
    playbackManager: PlaybackManager,
    currentSongId: Long?,
    freezeUpdates: Boolean,
): PlaybackProgressState {
    val liveProgress by playbackManager.progressState.collectAsStateWithLifecycle()
    var frozenProgress by remember(currentSongId) {
        mutableStateOf(playbackManager.progressState.value)
    }
    LaunchedEffect(liveProgress, freezeUpdates, currentSongId) {
        if (!freezeUpdates) {
            frozenProgress = liveProgress
        }
    }
    return if (freezeUpdates) frozenProgress else liveProgress
}

@Composable
internal fun CompactQueuePlaybackSummary(
    playbackManager: PlaybackManager,
    currentSongId: Long,
    freezeUpdates: Boolean,
    contentColor: Color,
    secondaryContentColor: Color,
    modifier: Modifier = Modifier,
) {
    val playbackProgress = rememberRenderedPlaybackProgress(
        playbackManager = playbackManager,
        currentSongId = currentSongId,
        freezeUpdates = freezeUpdates,
    )
    val progress = remember(playbackProgress.displayPositionMs, playbackProgress.durationMs) {
        if (playbackProgress.durationMs > 0L) {
            (playbackProgress.displayPositionMs.toFloat() / playbackProgress.durationMs.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        CompactPlaybackProgressBar(
            progress = progress,
            contentColor = contentColor,
            modifier = Modifier.fillMaxWidth(),
        )
        CompactPlaybackTimingRow(
            displayedPositionMs = playbackProgress.displayPositionMs,
            durationMs = playbackProgress.durationMs,
            contentColor = contentColor,
            secondaryContentColor = secondaryContentColor,
        )
    }
}

@Composable
internal fun NowPlayingProgressSummary(
    playbackManager: PlaybackManager,
    currentSongId: Long,
    freezeUpdates: Boolean,
    format: String,
    quality: String?,
    contentColor: Color,
    secondaryContentColor: Color,
    modifier: Modifier = Modifier,
) {
    val playbackProgress = rememberRenderedPlaybackProgress(
        playbackManager = playbackManager,
        currentSongId = currentSongId,
        freezeUpdates = freezeUpdates,
    )
    val progress = remember(playbackProgress.displayPositionMs, playbackProgress.durationMs) {
        if (playbackProgress.durationMs > 0L) {
            (playbackProgress.displayPositionMs.toFloat() / playbackProgress.durationMs.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        PlaybackProgressBar(
            progress = progress,
            isInteracting = playbackProgress.isUserScrubbing,
            contentColor = contentColor,
            onScrubStarted = playbackManager::beginScrub,
            onScrubFractionChanged = { fraction ->
                val target = fractionToDurationPosition(
                    fraction = fraction,
                    durationMs = playbackProgress.durationMs,
                )
                playbackManager.updateScrubPosition(target)
            },
            onScrubFinished = { fraction ->
                val target = fractionToDurationPosition(
                    fraction = fraction,
                    durationMs = playbackProgress.durationMs,
                )
                playbackManager.finishScrub(target)
            },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = formatPlaybackPosition(playbackProgress.displayPositionMs),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = contentColor,
                )
            }
            SongFileInfoPill(
                format = format,
                quality = quality,
                tint = contentColor,
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Text(
                    text = formatDuration(playbackProgress.durationMs),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = secondaryContentColor.copy(alpha = 0.7f),
                )
            }
        }
    }
}

@Composable
internal fun SongFileInfoPill(
    format: String,
    quality: String?,
    tint: Color,
) {
    Surface(
        modifier = Modifier.playerFrostedSurface(tint = tint),
        shape = RoundedCornerShape(ElovaireRadii.pill),
        color = tint.copy(alpha = 0.2f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lucide_audio_waveform),
                contentDescription = null,
                tint = tint.copy(alpha = 0.82f),
                modifier = Modifier.size(12.dp),
            )
            Text(
                text = format.ifBlank { "AUDIO" },
                style = MaterialTheme.typography.labelLarge.copy(fontSize = elovaireScaledSp(11f)),
                color = tint.copy(alpha = 0.92f),
                maxLines = 1,
            )
            Text(
                text = quality ?: "--",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = elovaireScaledSp(11f)),
                color = tint.copy(alpha = 0.72f),
                maxLines = 1,
            )
        }
    }
}

@Composable
internal fun CompactPlaybackProgressBar(
    progress: Float,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    val clampedProgress = progress.coerceIn(0f, 1f)
    Box(
        modifier = modifier
            .height(12.dp),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(ElovaireRadii.pill))
                .background(contentColor.copy(alpha = 0.18f)),
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth(clampedProgress)
                .height(4.dp)
                .clip(RoundedCornerShape(ElovaireRadii.pill))
                .background(contentColor),
        )
    }
}

@Composable
internal fun CompactPlaybackTimingRow(
    displayedPositionMs: Long,
    durationMs: Long,
    contentColor: Color,
    secondaryContentColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = formatPlaybackPosition(displayedPositionMs),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = contentColor,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "・",
            style = MaterialTheme.typography.labelLarge,
            color = contentColor.copy(alpha = 0.5f),
            maxLines = 1,
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = formatDuration(durationMs),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
            color = secondaryContentColor.copy(alpha = 0.7f),
            maxLines = 1,
        )
    }
}

@Composable
internal fun QueueSheet(
    queue: List<Song>,
    currentIndex: Int,
    playlists: List<Playlist>,
    playlistSongsById: Map<Long, Song>,
    currentSong: Song?,
    tint: Color,
    secondaryTint: Color,
    onSongSelected: (Int) -> Unit,
    onQueueItemRemoved: (Int) -> Unit,
    shuffleEnabled: Boolean,
    onToggleShuffle: () -> Unit,
    spaciousnessEnabled: Boolean,
    onToggleSpaciousness: () -> Unit,
    spaciousnessAmount: Float,
    onSpaciousnessAmountChanged: (Float) -> Unit,
    onOpenEqualizer: () -> Unit,
    onAddSongToPlaylist: (Long, Song) -> Unit,
    onCreatePlaylist: (String) -> Long,
    statusText: String?,
    onDismiss: () -> Unit,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
) {
    val language = LocalAppLanguage.current
    val listState = rememberElovaireLazyListState("equalizer_screen")
    var showSpaciousnessSlider by remember(spaciousnessEnabled) { mutableStateOf(spaciousnessEnabled) }
    var playlistTargetSong by remember(currentSong?.id, queue) { mutableStateOf<Song?>(null) }
    val footerExpanded = showSpaciousnessSlider || statusText != null
    val footerHeight by animateDpAsState(
        targetValue = if (footerExpanded) 90.dp else 60.dp,
        animationSpec = tween(120, easing = FastOutSlowInEasing),
        label = "queue_footer_height",
    )
    LaunchedEffect(spaciousnessEnabled) {
        if (!spaciousnessEnabled) {
            showSpaciousnessSlider = false
        }
    }
    LaunchedEffect(currentIndex, queue.size) {
        if (currentIndex in queue.indices) {
            listState.scrollToItem((currentIndex - 2).coerceAtLeast(0))
        }
    }
    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lucide_list_music),
                            contentDescription = null,
                            tint = tint.copy(alpha = 0.92f),
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            text = queueTitle(language),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = elovaireScaledSp(18f),
                                fontWeight = FontWeight.Medium,
                            ),
                            color = tint,
                        )
                    }
                    Row(
                        modifier = Modifier.offset(x = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = localizedCountLabel(queue.size, "track", language),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
                            color = secondaryTint.copy(alpha = 0.7f),
                        )
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(tint.copy(alpha = 0.1f))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onDismiss,
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_lucide_x),
                                contentDescription = "Close queue",
                                tint = tint.copy(alpha = 0.92f),
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    }
                }
            }
            QueueSeparator(tint = tint, modifier = Modifier.fillMaxWidth())
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                LazyColumn(
                    state = listState,
                    overscrollEffect = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .ensureSingleItemRubberBand(listState),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                ) {
                    itemsIndexed(queue, key = { index, song -> "${song.id}_$index" }) { index, song ->
                        QueueSongRow(
                            song = song,
                            active = index == currentIndex,
                            tint = tint,
                            secondaryTint = secondaryTint,
                            showDivider = false,
                            onClick = { onSongSelected(index) },
                            isPlaying = isPlaying,
                            onAddToPlaylist = { playlistTargetSong = song },
                            onRemoveFromQueue = { onQueueItemRemoved(index) },
                        )
                    }
                }
            }
            QueueSeparator(tint = tint, modifier = Modifier.fillMaxWidth())
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(footerHeight),
            ) {
                AnimatedContent(
                    targetState = statusText,
                    transitionSpec = {
                        fadeIn(animationSpec = ElovaireMotion.contentFadeInSpec()) +
                            slideInVertically(
                                animationSpec = ElovaireMotion.offsetSoft(durationMillis = ElovaireMotion.Standard),
                                initialOffsetY = { it / 5 },
                            ) togetherWith
                            fadeOut(animationSpec = ElovaireMotion.contentFadeOutSpec())
                    },
                    label = "queue_status_text",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 10.dp),
                ) { queueStatus ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (queueStatus != null && !showSpaciousnessSlider) {
                            Text(
                                text = queueStatus,
                                style = MaterialTheme.typography.labelLarge,
                                color = tint.copy(alpha = 0.92f),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
                androidx.compose.animation.AnimatedVisibility(
                    visible = showSpaciousnessSlider,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 10.dp),
                    enter = fadeIn(animationSpec = ElovaireMotion.contentFadeInSpec()) + slideInVertically(
                        animationSpec = ElovaireMotion.offsetSoft(durationMillis = ElovaireMotion.Standard),
                        initialOffsetY = { it / 3 },
                    ),
                    exit = fadeOut(animationSpec = ElovaireMotion.contentFadeOutSpec()) + slideOutVertically(
                        animationSpec = ElovaireMotion.standardTween(
                            durationMillis = ElovaireMotion.Fast,
                            easing = FastOutLinearInEasing,
                        ),
                        targetOffsetY = { it / 4 },
                    ),
                ) {
                    ThinContinuousSlider(
                        value = spaciousnessAmount.coerceIn(0f, 1f),
                        onValueChange = {
                            val clamped = it.coerceIn(0f, 1f)
                            onSpaciousnessAmountChanged(clamped)
                            if (clamped <= 0.001f) {
                                showSpaciousnessSlider = false
                                if (spaciousnessEnabled) {
                                    onToggleSpaciousness()
                                }
                            }
                        },
                        valueRange = 0f..1f,
                        lineThickness = 4.dp,
                        knobSize = 18.dp,
                        modifier = Modifier.fillMaxWidth(0.9f),
                    )
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PlayerSecondaryActionButton(
                        iconResId = R.drawable.ic_lucide_wind,
                        label = "",
                        iconSize = 20.dp,
                        tint = tint,
                        showBackground = spaciousnessEnabled,
                        onClick = {
                            if (!spaciousnessEnabled) {
                                showSpaciousnessSlider = true
                                if (spaciousnessAmount < 0.02f) {
                                    onSpaciousnessAmountChanged(0.5f)
                                }
                                onToggleSpaciousness()
                            } else if (!showSpaciousnessSlider) {
                                showSpaciousnessSlider = true
                            } else {
                                showSpaciousnessSlider = false
                                onToggleSpaciousness()
                            }
                        },
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    PlayerSecondaryActionButton(
                        iconResId = R.drawable.ic_lucide_sliders_vertical,
                        label = "",
                        iconSize = 20.dp,
                        tint = tint,
                        showBackground = false,
                        onClick = {
                            onDismiss()
                            onOpenEqualizer()
                        },
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    PlayerSecondaryActionButton(
                        iconResId = R.drawable.ic_lucide_plus,
                        label = "",
                        iconSize = 20.dp,
                        tint = tint,
                        showBackground = playlistTargetSong != null,
                        onClick = {
                            playlistTargetSong = currentSong
                        },
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    PlayerSecondaryActionButton(
                        iconResId = R.drawable.ic_lucide_shuffle,
                        label = "",
                        iconSize = 20.dp,
                        tint = tint,
                        showBackground = shuffleEnabled,
                        onClick = onToggleShuffle,
                    )
                }
            }
        }
    }
    playlistTargetSong?.let { song ->
        AddToPlaylistPickerDialog(
            playlists = playlists,
            playlistSongsById = playlistSongsById,
            onDismiss = { playlistTargetSong = null },
            onPlaylistSelected = { playlistId ->
                onAddSongToPlaylist(playlistId, song)
                playlistTargetSong = null
            },
            onCreatePlaylist = onCreatePlaylist,
        )
    }
}

@Composable
internal fun QueueSeparator(
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(1.dp)
            .background(tint.copy(alpha = 0.3f)),
    )
}

@Composable
internal fun QueueSongRow(
    song: Song,
    active: Boolean,
    isPlaying: Boolean,
    tint: Color,
    secondaryTint: Color,
    showDivider: Boolean,
    onClick: () -> Unit,
    onAddToPlaylist: () -> Unit,
    onRemoveFromQueue: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(
                    if (active) tint.copy(alpha = 0.1f) else Color.Transparent,
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick,
                )
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.Center,
            ) {
                ArtworkImage(
                    uri = song.artUri,
                    title = song.album,
                    modifier = Modifier.matchParentSize(),
                    cornerRadius = ElovaireRadii.artworkSmall,
                )
                androidx.compose.animation.AnimatedVisibility(
                    visible = active && isPlaying,
                    enter = fadeIn(animationSpec = tween(60)),
                    exit = fadeOut(animationSpec = tween(60)),
                ) {
                    PlaybackActiveArtworkOverlay(
                        uri = song.artUri,
                        title = song.album,
                        modifier = Modifier.matchParentSize(),
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                ExplicitTitleText(
                    title = song.title,
                    isExplicit = song.isExplicit,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = if (active) FontWeight.SemiBold else FontWeight.Medium,
                    ),
                    color = if (active) tint else tint.copy(alpha = 0.84f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.labelLarge,
                    color = secondaryTint.copy(alpha = 0.78f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = formatDuration(song.durationMs),
                    style = MaterialTheme.typography.labelLarge,
                    color = secondaryTint.copy(alpha = 0.78f),
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(45.dp),
                )
                QueueSongOverflowMenuButton(
                    tint = tint,
                    onAddToPlaylist = onAddToPlaylist,
                    onRemoveFromQueue = onRemoveFromQueue,
                )
            }
        }
        if (showDivider) {
            QueueSeparator(
                tint = tint,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
internal fun QueueSongOverflowMenuButton(
    tint: Color,
    onAddToPlaylist: () -> Unit,
    onRemoveFromQueue: () -> Unit,
) {
    val language = LocalAppLanguage.current
    var expanded by remember { mutableStateOf(false) }
    var shouldRenderMenu by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val motionDurationScale = rememberSystemAnimationScale()
    val buttonScale by animateFloatAsState(
        targetValue = if (pressed) 0.88f else 1f,
        animationSpec = ElovaireMotion.releaseSpringSpec(),
        label = "queue_song_overflow_scale",
    )
    LaunchedEffect(expanded) {
        if (expanded) {
            shouldRenderMenu = true
        } else if (shouldRenderMenu) {
            delay(ElovaireMotion.scaleDurationMillis(180L, motionDurationScale))
            shouldRenderMenu = false
        }
    }

    Box {
        Box(
            modifier = Modifier
                .size(24.dp)
                .scale(buttonScale)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { expanded = true },
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lucide_ellipsis_vertical),
                contentDescription = "Queue song options",
                tint = tint.copy(alpha = 0.82f),
                modifier = Modifier.size(OverflowMenuIconSize),
            )
        }

        if (shouldRenderMenu) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = { expanded = false },
                offset = OverflowMenuAnchorOffset,
                containerColor = Color.Transparent,
                shadowElevation = 0.dp,
                tonalElevation = 0.dp,
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = expanded,
                    enter = ElovaireMotion.contextMenuEnter(),
                    exit = ElovaireMotion.contextMenuExit(),
                    label = "QueueSongOverflowMenuVisibility",
                ) {
                    QueueContextMenuSurface(
                        modifier = Modifier.width(210.dp),
                    ) {
                        SongContextMenuItem(
                            iconResId = R.drawable.ic_lucide_list_plus,
                            text = uiPhrase(language, UiPhrase.AddToPlaylist),
                            tint = tint,
                            onClick = {
                                expanded = false
                                onAddToPlaylist()
                            },
                        )
                        DividerLine()
                        SongContextMenuItem(
                            iconResId = R.drawable.ic_lucide_list_x,
                            text = uiPhrase(language, UiPhrase.RemoveFromList),
                            tint = tint,
                            onClick = {
                                expanded = false
                                onRemoveFromQueue()
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun QueueContextMenuSurface(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    DynamicBackdropSurface(
        modifier = modifier,
        shape = RoundedCornerShape(ElovaireRadii.card),
        overlayAlpha = 0.1f,
        borderColor = blurSurfaceBorderColor(),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

@Composable
internal fun PlayerTransportButton(
    iconResId: Int,
    contentDescription: String,
    tint: Color,
    iconSize: Dp,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val buttonScale by animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f,
        animationSpec = ElovaireMotion.releaseSpringSpec(),
        label = "${contentDescription}_transport_scale",
    )
    Box(
        modifier = Modifier
            .size(72.dp)
            .scale(buttonScale)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = iconResId,
            transitionSpec = {
                (
                    fadeIn(animationSpec = ElovaireMotion.iconSwapInSpec()) +
                        scaleIn(
                            initialScale = 0.9f,
                            animationSpec = ElovaireMotion.releaseSpringSpec(
                                dampingRatio = 0.8f,
                                stiffness = 520f,
                            ),
                        )
                    ) togetherWith
                    (
                        fadeOut(animationSpec = ElovaireMotion.iconSwapOutSpec()) +
                            scaleOut(
                                targetScale = 1.04f,
                                animationSpec = ElovaireMotion.contentFadeOutSpec(),
                            )
                        )
            },
            label = "${contentDescription}_transport_icon",
        ) { currentIcon ->
            Icon(
                painter = painterResource(id = currentIcon),
                contentDescription = contentDescription,
                tint = tint,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}

@Composable
internal fun QueueMenuButton(
    iconResId: Int,
    tint: Color,
    active: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val backgroundAlpha by animateFloatAsState(
        targetValue = if (active) 0.2f else 0f,
        animationSpec = ElovaireMotion.contentFadeInSpec(),
        label = "queue_button_alpha",
    )
    val buttonScale by animateFloatAsState(
        targetValue = when {
            pressed -> 0.9f
            active -> 1f
            else -> 0.96f
        },
        animationSpec = ElovaireMotion.releaseSpringSpec(
            dampingRatio = 0.82f,
            stiffness = 520f,
        ),
        label = "queue_button_scale",
    )
    Box(
        modifier = Modifier
            .size(40.dp)
            .scale(buttonScale)
            .clip(CircleShape)
            .playerFrostedSurface(tint = tint)
            .background(tint.copy(alpha = backgroundAlpha))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = "Queue",
            tint = tint.copy(alpha = 0.92f),
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
internal fun FavoriteSongButton(
    isFavorite: Boolean,
    tint: Color,
    modifier: Modifier = Modifier,
    backgroundColor: Color = tint.copy(alpha = 0.2f),
    borderColor: Color = Color.Transparent,
    frosted: Boolean = false,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val motionDurationScale = rememberSystemAnimationScale()
    var previousFavoriteState by remember { mutableStateOf(isFavorite) }
    var shouldBounce by remember { mutableStateOf(false) }
    LaunchedEffect(isFavorite) {
        if (previousFavoriteState != isFavorite) {
            shouldBounce = true
            delay(ElovaireMotion.scaleDurationMillis(180L, motionDurationScale))
            shouldBounce = false
            previousFavoriteState = isFavorite
        }
    }
    val buttonScale by animateFloatAsState(
        targetValue = when {
            pressed -> 0.88f
            shouldBounce -> 1.08f
            else -> 1f
        },
        animationSpec = if (shouldBounce) {
            ElovaireMotion.bounceSpringSpec()
        } else {
            ElovaireMotion.releaseSpringSpec()
        },
        label = "favorite_button_scale",
    )
    val iconScale by animateFloatAsState(
        targetValue = when {
            pressed -> 0.84f
            shouldBounce -> 1.12f
            isFavorite -> 1f
            else -> 0.96f
        },
        animationSpec = if (shouldBounce) {
            ElovaireMotion.bounceSpringSpec()
        } else {
            ElovaireMotion.releaseSpringSpec(
                dampingRatio = 0.8f,
                stiffness = 520f,
            )
        },
        label = "favorite_icon_scale",
    )

    Box(
        modifier = modifier
            .size(44.dp)
            .scale(buttonScale)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (frosted) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .blur(18.dp)
                    .background(backgroundColor.copy(alpha = 0.86f)),
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(backgroundColor),
            )
            if (borderColor.alpha > 0f) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .border(1.dp, borderColor, CircleShape),
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(backgroundColor),
            )
        }
        AnimatedContent(
            targetState = isFavorite,
            transitionSpec = {
                (
                    fadeIn(animationSpec = ElovaireMotion.iconSwapInSpec()) +
                        scaleIn(
                            initialScale = 0.88f,
                            animationSpec = ElovaireMotion.releaseSpringSpec(),
                        )
                    ) togetherWith
                    (
                        fadeOut(animationSpec = ElovaireMotion.iconSwapOutSpec()) +
                            scaleOut(
                                targetScale = 1.04f,
                                animationSpec = ElovaireMotion.contentFadeOutSpec(),
                            )
                        )
            },
            label = "favorite_button_icon",
        ) { favorite ->
            Icon(
                painter = painterResource(
                    id = if (favorite) R.drawable.ic_lucide_star_filled else R.drawable.ic_lucide_star,
                ),
                contentDescription = if (favorite) "Unlike song" else "Like song",
                tint = tint,
                modifier = Modifier
                    .size(20.dp)
                    .scale(iconScale),
            )
        }
    }
}

@Composable
internal fun AlbumHeaderActionButton(
    iconResId: Int,
    contentDescription: String,
    tint: Color,
    backgroundColor: Color,
    iconSize: Dp = 20.dp,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.88f else 1f,
        animationSpec = ElovaireMotion.releaseSpringSpec(),
        label = "${contentDescription}_album_header_scale",
    )

    Box(
        modifier = Modifier
            .size(44.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(iconSize),
        )
    }
}

@Composable
internal fun AlbumHeaderPlayButton(
    tint: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
) {
    val language = LocalAppLanguage.current
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.88f else 1f,
        animationSpec = ElovaireMotion.releaseSpringSpec(),
        label = "album_play_button_scale",
    )

    Surface(
        modifier = Modifier.scale(scale),
        onClick = onClick,
        shape = RoundedCornerShape(ElovaireRadii.pill),
        color = backgroundColor,
        interactionSource = interactionSource,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lucide_circle_play),
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = playLabel(language),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = elovaireScaledSp(16f),
                    fontWeight = FontWeight.SemiBold,
                ),
                color = tint,
            )
        }
    }
}

@Composable
internal fun InlineFavoriteSongButton(
    isFavorite: Boolean,
    tint: Color,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val motionDurationScale = rememberSystemAnimationScale()
    var previousFavoriteState by remember { mutableStateOf(isFavorite) }
    var shouldBounce by remember { mutableStateOf(false) }
    LaunchedEffect(isFavorite) {
        if (previousFavoriteState != isFavorite) {
            shouldBounce = true
            delay(ElovaireMotion.scaleDurationMillis(180L, motionDurationScale))
            shouldBounce = false
            previousFavoriteState = isFavorite
        }
    }
    val buttonScale by animateFloatAsState(
        targetValue = when {
            pressed -> 0.8f
            shouldBounce -> 1.12f
            else -> 1f
        },
        animationSpec = if (shouldBounce) {
            ElovaireMotion.bounceSpringSpec()
        } else {
            ElovaireMotion.releaseSpringSpec(
                dampingRatio = 0.8f,
                stiffness = 520f,
            )
        },
        label = "inline_favorite_scale",
    )
    val iconScale by animateFloatAsState(
        targetValue = when {
            pressed -> 0.8f
            shouldBounce -> 1.18f
            isFavorite -> 1f
            else -> 0.96f
        },
        animationSpec = if (shouldBounce) {
            ElovaireMotion.bounceSpringSpec()
        } else {
            ElovaireMotion.releaseSpringSpec(
                dampingRatio = 0.8f,
                stiffness = 520f,
            )
        },
        label = "inline_favorite_icon_scale",
    )

    Box(
        modifier = Modifier
            .size(24.dp)
            .scale(buttonScale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = isFavorite,
            transitionSpec = {
                (
                    fadeIn(animationSpec = ElovaireMotion.iconSwapInSpec()) +
                        scaleIn(
                            initialScale = 0.88f,
                            animationSpec = ElovaireMotion.releaseSpringSpec(),
                        )
                    ) togetherWith
                    (
                        fadeOut(animationSpec = ElovaireMotion.iconSwapOutSpec()) +
                            scaleOut(
                                targetScale = 1.04f,
                                animationSpec = ElovaireMotion.contentFadeOutSpec(),
                            )
                        )
            },
            label = "inline_favorite_icon",
        ) { favorite ->
            Icon(
                painter = painterResource(
                    id = if (favorite) R.drawable.ic_lucide_star_filled else R.drawable.ic_lucide_star,
                ),
                contentDescription = if (favorite) "Unlike song" else "Like song",
                tint = tint.copy(alpha = if (favorite) 1f else 0.82f),
                modifier = Modifier
                    .size(18.dp)
                    .scale(iconScale),
            )
        }
    }
}

internal val OverflowMenuIconSize = 21.6.dp
internal val OverflowMenuAnchorOffset = DpOffset(x = 4.dp, y = (-8).dp)

@Composable
internal fun AlbumOverflowMenuButton(
    album: Album,
    playlists: List<Playlist>,
    playlistSongsById: Map<Long, Song>,
    tint: Color,
    onAddToQueue: () -> Unit,
    onAddToPlaylist: (Long) -> Unit,
    onCreatePlaylist: ((String) -> Long)?,
    onDeleteAlbum: () -> Unit,
) {
    val language = LocalAppLanguage.current
    var expanded by remember(album.id) { mutableStateOf(false) }
    var shouldRenderMenu by remember(album.id) { mutableStateOf(false) }
    var showPlaylistDialog by remember(album.id) { mutableStateOf(false) }
    val motionDurationScale = rememberSystemAnimationScale()
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val buttonScale by animateFloatAsState(
        targetValue = if (pressed) 0.86f else 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 380f,
        ),
        label = "album_overflow_scale",
    )

    LaunchedEffect(expanded) {
        if (expanded) {
            shouldRenderMenu = true
        } else if (shouldRenderMenu) {
            delay(ElovaireMotion.scaleDurationMillis(180L, motionDurationScale))
            shouldRenderMenu = false
        }
    }

    Box {
        Box(
            modifier = Modifier
                .size(24.dp)
                .scale(buttonScale)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { expanded = true },
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lucide_ellipsis_vertical),
                contentDescription = "Album options",
                tint = tint.copy(alpha = 0.82f),
                modifier = Modifier.size(OverflowMenuIconSize),
            )
        }

        if (shouldRenderMenu) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = { expanded = false },
                offset = OverflowMenuAnchorOffset,
                containerColor = Color.Transparent,
                shadowElevation = 0.dp,
                tonalElevation = 0.dp,
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = expanded,
                    enter = ElovaireMotion.contextMenuEnter(),
                    exit = ElovaireMotion.contextMenuExit(),
                    label = "AlbumOverflowMenuVisibility",
                ) {
                    FrostedContextMenuSurface(
                        modifier = Modifier.width(208.dp),
                    ) {
                        SongContextMenuItem(
                            iconResId = R.drawable.ic_lucide_plus,
                            text = uiPhrase(language, UiPhrase.AddToQueue),
                            tint = MaterialTheme.colorScheme.onSurface,
                            onClick = {
                                expanded = false
                                onAddToQueue()
                            },
                        )
                        DividerLine()
                        SongContextMenuItem(
                            iconResId = R.drawable.ic_lucide_list_music,
                            text = uiPhrase(language, UiPhrase.AddToPlaylist),
                            tint = MaterialTheme.colorScheme.onSurface,
                            onClick = {
                                expanded = false
                                showPlaylistDialog = true
                            },
                        )
                        DividerLine()
                        SongContextMenuItem(
                            iconResId = R.drawable.ic_lucide_trash_2,
                            text = uiPhrase(language, UiPhrase.DeleteAlbum),
                            tint = DestructiveRed,
                            containerColor = DestructiveRed.copy(alpha = 0.2f),
                            bottomPadding = 10.dp,
                            onClick = {
                                expanded = false
                                onDeleteAlbum()
                            },
                        )
                    }
                }
            }
        }
    }

    if (showPlaylistDialog) {
        AddToPlaylistPickerDialog(
            playlists = playlists,
            playlistSongsById = playlistSongsById,
            onDismiss = { showPlaylistDialog = false },
            onPlaylistSelected = { playlistId ->
                onAddToPlaylist(playlistId)
                showPlaylistDialog = false
            },
            onCreatePlaylist = onCreatePlaylist,
        )
    }
}

@Composable
internal fun SongOverflowMenuButton(
    song: Song,
    tint: Color,
) {
    val actions = LocalSongMenuActions.current
    val language = LocalAppLanguage.current
    var expanded by remember(song.id) { mutableStateOf(false) }
    var shouldRenderMenu by remember(song.id) { mutableStateOf(false) }
    var showPlaylistDialog by remember(song.id) { mutableStateOf(false) }
    val motionDurationScale = rememberSystemAnimationScale()
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val buttonScale by animateFloatAsState(
        targetValue = if (pressed) 0.86f else 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 380f,
        ),
        label = "song_overflow_scale",
    )
    LaunchedEffect(expanded) {
        if (expanded) {
            shouldRenderMenu = true
        } else if (shouldRenderMenu) {
            delay(ElovaireMotion.scaleDurationMillis(180L, motionDurationScale))
            shouldRenderMenu = false
        }
    }

    Box {
        Box(
            modifier = Modifier
                .size(24.dp)
                .scale(buttonScale)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        expanded = true
                    },
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lucide_ellipsis_vertical),
                contentDescription = "Song options",
                tint = tint.copy(alpha = 0.82f),
                modifier = Modifier.size(OverflowMenuIconSize),
            )
        }

        if (shouldRenderMenu) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = { expanded = false },
                offset = OverflowMenuAnchorOffset,
                containerColor = Color.Transparent,
                shadowElevation = 0.dp,
                tonalElevation = 0.dp,
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = expanded,
                    enter = ElovaireMotion.contextMenuEnter(),
                    exit = ElovaireMotion.contextMenuExit(),
                    label = "SongOverflowMenuVisibility",
                ) {
                    FrostedContextMenuSurface(
                        modifier = Modifier.width(208.dp),
                    ) {
                        SongContextMenuItem(
                            iconResId = R.drawable.ic_lucide_list_music,
                            text = uiPhrase(language, UiPhrase.AddToPlaylist),
                            tint = MaterialTheme.colorScheme.onSurface,
                            onClick = {
                                expanded = false
                                showPlaylistDialog = true
                            },
                        )
                        DividerLine()
                        SongContextMenuItem(
                            iconResId = R.drawable.ic_lucide_plus,
                            text = uiPhrase(language, UiPhrase.AddToQueue),
                            tint = MaterialTheme.colorScheme.onSurface,
                            onClick = {
                                expanded = false
                                actions.onAddToQueue(song)
                            },
                        )
                        SongContextMenuItem(
                            iconResId = R.drawable.ic_lucide_trash_2,
                            text = uiPhrase(language, actions.deletePhrase),
                            tint = DestructiveRed,
                            containerColor = DestructiveRed.copy(alpha = 0.2f),
                            bottomPadding = 10.dp,
                            onClick = {
                                expanded = false
                                actions.onDeleteFromLibrary(song)
                            },
                        )
                    }
                }
            }
        }
    }

    if (showPlaylistDialog) {
        AddToPlaylistPickerDialog(
            playlists = actions.playlists,
            playlistSongsById = actions.songsById,
            onDismiss = { showPlaylistDialog = false },
            onPlaylistSelected = { playlistId ->
                actions.onAddToPlaylist(playlistId, song)
                showPlaylistDialog = false
            },
            onCreatePlaylist = actions.onCreatePlaylist,
        )
    }
}

@Composable
internal fun FrostedContextMenuSurface(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(ElovaireRadii.card)
    DynamicBackdropSurface(
        modifier = modifier,
        shape = shape,
        overlayAlpha = 0.7f,
        borderColor = blurSurfaceBorderColor(),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

@Composable
internal fun TopBarContextMenuOverlay(
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenEqualizer: () -> Unit,
    onOpenChangelog: () -> Unit,
    onOpenAbout: () -> Unit,
) {
    val language = LocalAppLanguage.current
    val settingsCopy = remember(language) { settingsCopy(language) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss,
            ),
    ) {
        androidx.compose.animation.AnimatedVisibility(
            visible = true,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 6.dp, end = 10.dp),
            enter = ElovaireMotion.contextMenuEnter(),
            exit = ElovaireMotion.contextMenuExit(),
            label = "TopBarContextMenuVisibility",
        ) {
            FrostedContextMenuSurface(
                modifier = Modifier.width(190.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    SongContextMenuItem(
                        iconResId = R.drawable.ic_lucide_settings,
                        text = settingsCopy.settings,
                        tint = MaterialTheme.colorScheme.onSurface,
                        topPadding = 8.dp,
                        onClick = onOpenSettings,
                    )
                    DividerLine()
                    SongContextMenuItem(
                        iconResId = R.drawable.ic_lucide_audio_waveform,
                        text = settingsCopy.equalizer,
                        tint = MaterialTheme.colorScheme.onSurface,
                        onClick = onOpenEqualizer,
                    )
                    DividerLine()
                    SongContextMenuItem(
                        iconResId = R.drawable.ic_lucide_list,
                        text = settingsCopy.changelog,
                        tint = MaterialTheme.colorScheme.onSurface,
                        onClick = onOpenChangelog,
                    )
                    DividerLine()
                    SongContextMenuItem(
                        iconResId = R.drawable.ic_lucide_info,
                        text = uiPhrase(language, UiPhrase.About),
                        tint = MaterialTheme.colorScheme.onSurface,
                        topPadding = 6.dp,
                        bottomPadding = 8.dp,
                        onClick = onOpenAbout,
                    )
                }
            }
        }
    }
}

@Composable
internal fun SongContextMenuItem(
    @DrawableRes iconResId: Int,
    text: String,
    tint: Color,
    containerColor: Color = Color.Transparent,
    topPadding: Dp = 6.dp,
    bottomPadding: Dp = 6.dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = topPadding, end = 10.dp, bottom = bottomPadding)
            .clip(RoundedCornerShape(ElovaireRadii.card * 0.72f))
            .background(containerColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(17.dp),
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = tint,
            )
        }
    }
}

@Composable
internal fun AddToPlaylistPickerDialog(
    playlists: List<Playlist>,
    playlistSongsById: Map<Long, Song>,
    onDismiss: () -> Unit,
    onPlaylistSelected: (Long) -> Unit,
    onCreatePlaylist: ((String) -> Long)? = null,
) {
    val language = LocalAppLanguage.current
    PlaylistSelectionDialog(
        title = uiPhrase(language, UiPhrase.AddToPlaylist),
        subtitle = null,
        playlists = playlists,
        playlistSongsById = playlistSongsById,
        onDismiss = onDismiss,
        onPlaylistSelected = onPlaylistSelected,
        onCreatePlaylist = onCreatePlaylist,
    )
}

@Composable
internal fun LyricsOverlay(
    song: Song?,
    lyricsUiState: LyricsUiState,
    activeLyricsLineIndex: Int,
    playbackProgress: PlaybackProgressState,
    tintColor: Color,
    contentColor: Color,
    secondaryContentColor: Color,
    onSeekTo: (Long) -> Unit,
    onHideLyrics: () -> Unit,
) {
    BackHandler(onBack = onHideLyrics)
    val scope = rememberCoroutineScope()
    val hideButtonArea = 112.dp
    val lyricsBottomBlurArea = 72.dp
    val bottomBlurSurfaceHeight = lyricsBottomBlurArea + navigationBarInsetDp()
    val lyricsHazeState = rememberHazeState()
    val listState = rememberLazyListState()
    var autoScrollHeld by remember(song?.id) { mutableStateOf(false) }
    var autoScrollResumeJob by remember(song?.id) { mutableStateOf<kotlinx.coroutines.Job?>(null) }
    var userLyricsScrollActive by remember(song?.id) { mutableStateOf(false) }

    LaunchedEffect(listState.isScrollInProgress, userLyricsScrollActive) {
        if (userLyricsScrollActive && !listState.isScrollInProgress) {
            autoScrollResumeJob?.cancel()
            autoScrollResumeJob = scope.launch {
                delay(1_600L)
                autoScrollHeld = false
                userLyricsScrollActive = false
            }
        }
    }

    val lyricsScrollObserver = remember(song?.id) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (source == NestedScrollSource.UserInput && available.y != 0f) {
                    autoScrollHeld = true
                    userLyricsScrollActive = true
                    autoScrollResumeJob?.cancel()
                }
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
            )
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        tintColor.copy(alpha = 0.9f),
                        tintColor.copy(alpha = 0.84f),
                        tintColor.copy(alpha = 0.92f),
                    ),
                ),
            ),
    ) {
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        Box(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(lyricsHazeState),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
            ) {
                song?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.75f),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lucide_circle_play),
                            contentDescription = null,
                            tint = secondaryContentColor,
                            modifier = Modifier.size(18.dp),
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text(
                                text = it.title,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = elovaireScaledSp(17f),
                                    fontWeight = FontWeight.Medium,
                                ),
                                color = contentColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = it.artist,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = elovaireScaledSp(15f),
                                ),
                                color = secondaryContentColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .width(screenWidth * 0.9f)
                            .height(1.dp)
                            .background(contentColor.copy(alpha = 0.2f)),
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    AnimatedContent(
                        targetState = lyricsUiState,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(ElovaireMotion.Standard, easing = LinearOutSlowInEasing)) +
                                slideInVertically(
                                    animationSpec = tween(ElovaireMotion.ScreenExpand, easing = FastOutSlowInEasing),
                                    initialOffsetY = { it / 12 },
                                ) +
                                expandVertically(
                                    expandFrom = Alignment.Top,
                                    animationSpec = tween(ElovaireMotion.ScreenExpand, easing = FastOutSlowInEasing),
                                ) togetherWith
                                fadeOut(animationSpec = tween(ElovaireMotion.Quick, easing = FastOutLinearInEasing))
                        },
                        contentKey = { state -> state::class },
                        label = "lyrics_content_state",
                    ) { state ->
                        when (state) {
                            LyricsUiState.Hidden -> Unit
                            LyricsUiState.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = "Loading lyrics...",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = contentColor,
                                    )
                                }
                            }

                            LyricsUiState.Empty -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_lucide_info),
                                            contentDescription = null,
                                            tint = contentColor.copy(alpha = 0.7f),
                                            modifier = Modifier.size(18.dp),
                                        )
                                        Text(
                                            text = "This song seems to have no lyrics",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = contentColor,
                                        )
                                    }
                                }
                            }

                            is LyricsUiState.Ready -> {
                                LyricsReadyContent(
                                    song = song,
                                    payload = state.payload,
                                    activeLyricLineIndex = activeLyricsLineIndex,
                                    playbackProgress = playbackProgress,
                                    listState = listState,
                                    autoScrollHeld = autoScrollHeld,
                                    setAutoScrollHeld = { autoScrollHeld = it },
                                    autoScrollResumeJob = autoScrollResumeJob,
                                    setAutoScrollResumeJob = { autoScrollResumeJob = it },
                                    setUserLyricsScrollActive = { userLyricsScrollActive = it },
                                    lyricsScrollObserver = lyricsScrollObserver,
                                    hideButtonArea = hideButtonArea,
                                    lyricsBottomBlurArea = lyricsBottomBlurArea,
                                    contentColor = contentColor,
                                    onSeekTo = onSeekTo,
                                    scope = scope,
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(bottomBlurSurfaceHeight)
                .clipToBounds()
                .zIndex(3f),
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .hazeEffect(lyricsHazeState) {
                            progressive = HazeProgressive.LinearGradient(
                                startIntensity = 0f,
                                endIntensity = 1f,
                                preferPerformance = true,
                            )
                            blurRadius = 34.dp
                            backgroundColor = Color.Transparent
                            tints = listOf(
                                HazeTint(tintColor.copy(alpha = 0.06f)),
                                HazeTint(tintColor.copy(alpha = 0.02f)),
                            )
                            noiseFactor = 0.02f
                        },
                )
            }
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                tintColor.copy(alpha = 0.08f),
                                tintColor.copy(alpha = 0.28f),
                                tintColor.copy(alpha = 0.62f),
                                tintColor.copy(alpha = 0.96f),
                            ),
                        ),
                    ),
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(lyricsBottomBlurArea)
                    .offset(y = (-6).dp)
                    .padding(horizontal = 20.dp)
                    .zIndex(4f),
                contentAlignment = Alignment.Center,
            ) {
                Surface(
                    onClick = onHideLyrics,
                    shape = RoundedCornerShape(ElovaireRadii.pill),
                    color = contentColor.copy(alpha = 0.18f),
                    contentColor = contentColor,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lucide_eye_off),
                            contentDescription = "Hide lyrics",
                            modifier = Modifier.size(15.dp),
                        )
                        Text(
                            text = "Hide lyrics",
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
        }
    }
}
