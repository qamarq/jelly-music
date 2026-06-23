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
import androidx.compose.ui.input.pointer.positionChange
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
internal fun NowPlayingBar(
    song: Song,
    isPlaying: Boolean,
    progress: Float,
    visible: Boolean,
    contentColor: Color,
    secondaryContentColor: Color,
    onOpenPlayer: (NowPlayingTransitionSnapshot?) -> Unit,
    onTogglePlayback: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
) {
    val barGradient = rememberArtworkGradient(song.artUri).value
    val darkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val controlBaseTint = if (darkTheme) {
        barGradient.last().copy(alpha = 0.28f).compositeOver(Color.Black.copy(alpha = 0.16f))
    } else {
        barGradient.last().copy(alpha = 0.22f).compositeOver(Color.White.copy(alpha = 0.16f))
    }
    val controlTint by animateColorAsState(
        targetValue = controlBaseTint,
        animationSpec = ElovaireMotion.colorFadeSpec(),
        label = "MiniPlayerButtonTint",
    )
    val controlIconTint by animateColorAsState(
        targetValue = if (controlTint.luminance() > 0.42f) InkText else Color.White,
        animationSpec = ElovaireMotion.colorFadeSpec(),
        label = "MiniPlayerButtonIconTint",
    )
    val resolvedPrimaryTextColor by animateColorAsState(
        targetValue = controlIconTint,
        animationSpec = ElovaireMotion.colorFadeSpec(),
        label = "MiniPlayerTextPrimary",
    )
    val resolvedSecondaryTextColor by animateColorAsState(
        targetValue = controlIconTint.copy(alpha = 0.72f),
        animationSpec = ElovaireMotion.colorFadeSpec(),
        label = "MiniPlayerTextSecondary",
    )
    val compactControlBackground by animateColorAsState(
        targetValue = resolvedPrimaryTextColor.copy(alpha = 0.2f),
        animationSpec = ElovaireMotion.colorFadeSpec(),
        label = "MiniPlayerControlBackground",
    )
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val buttonScale by animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f,
        animationSpec = ElovaireMotion.releaseSpringSpec(
            dampingRatio = 0.58f,
            stiffness = 420f,
        ),
        label = "MiniPlayerPlayButtonScale",
    )
    val density = LocalDensity.current
    val swipeThresholdPx = with(density) { 52.dp.toPx() }
    var dragOffsetX by remember(song.id) { mutableFloatStateOf(0f) }
    var barBounds by remember(song.id) { mutableStateOf<androidx.compose.ui.geometry.Rect?>(null) }
    var artworkBounds by remember(song.id) { mutableStateOf<androidx.compose.ui.geometry.Rect?>(null) }
    val animatedDragOffsetX by animateFloatAsState(
        targetValue = dragOffsetX,
        animationSpec = ElovaireMotion.releaseSpringSpec(
            dampingRatio = 0.82f,
            stiffness = 380f,
        ),
        label = "MiniPlayerDragOffsetX",
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(ElovaireRadii.card))
            .onGloballyPositioned { barBounds = it.boundsInRoot() }
            .background(Color.Transparent)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) 0.08f else 0.05f),
                shape = RoundedCornerShape(ElovaireRadii.card),
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val openPlayerFromTap = remember(song.id, barBounds, artworkBounds) {
                {
                    val validSnapshot = if (
                        barBounds != null &&
                        artworkBounds != null &&
                        barBounds!!.isValidTransitionBounds &&
                        artworkBounds!!.isValidTransitionBounds
                    ) {
                        NowPlayingTransitionSnapshot(
                            songId = song.id,
                            barBounds = barBounds!!,
                            artworkBounds = artworkBounds!!,
                        )
                    } else {
                        null
                    }
                    onOpenPlayer(validSnapshot)
                }
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .graphicsLayer { translationX = animatedDragOffsetX * 0.18f }
                    .then(
                        if (visible) {
                            // A single gesture detector handles both tap-to-open and
                            // swipe-to-skip; stacking a separate pointerInput drag detector
                            // alongside .clickable() makes the first tap's down event get
                            // claimed by the drag detector, so the click never registers.
                            Modifier.pointerInput(song.id) {
                                var isDragging = false
                                awaitEachGesture {
                                    val down = awaitFirstDown(requireUnconsumed = false)
                                    isDragging = false
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        val change = event.changes.firstOrNull { it.id == down.id } ?: break
                                        val dx = change.positionChange().x
                                        if (!isDragging && kotlin.math.abs(dragOffsetX + dx) > 12f) {
                                            isDragging = true
                                        }
                                        if (isDragging) {
                                            change.consume()
                                            dragOffsetX = (dragOffsetX + dx).coerceIn(-160f, 160f)
                                        }
                                        if (!change.pressed) break
                                    }
                                    if (isDragging) {
                                        when {
                                            dragOffsetX <= -swipeThresholdPx -> onSkipNext()
                                            dragOffsetX >= swipeThresholdPx -> onSkipPrevious()
                                        }
                                        dragOffsetX = 0f
                                    } else {
                                        openPlayerFromTap()
                                    }
                                }
                            }
                        } else {
                            Modifier
                        }
                    ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ArtworkImage(
                    uri = song.artUri,
                    title = song.title,
                    modifier = Modifier
                        .size(48.dp)
                        .onGloballyPositioned { artworkBounds = it.boundsInRoot() },
                    cornerRadius = ElovaireRadii.artworkSmall,
                    requestedSizePx = 192,
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                ) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        color = resolvedPrimaryTextColor,
                        overflow = TextOverflow.Clip,
                        modifier = Modifier.basicMarquee(
                            iterations = Int.MAX_VALUE,
                            animationMode = MarqueeAnimationMode.Immediately,
                            repeatDelayMillis = 2500,
                            initialDelayMillis = 2500,
                            velocity = 24.dp,
                        ),
                    )
                    Text(
                        text = song.artist,
                        style = MaterialTheme.typography.labelLarge,
                        color = resolvedSecondaryTextColor,
                        maxLines = 1,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable(
                        enabled = visible,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onSkipPrevious,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_elovaire_backward_filled),
                    contentDescription = "Previous",
                    tint = controlIconTint,
                    modifier = Modifier.size(18.dp),
                )
            }
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .scale(buttonScale)
                    .clip(CircleShape)
                    .background(compactControlBackground)
                    .clickable(
                        enabled = visible,
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onTogglePlayback,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(
                    modifier = Modifier.matchParentSize(),
                ) {
                    val strokeWidth = size.minDimension * 0.1f
                    val arcInset = strokeWidth / 2f + 2.2f
                    val arcStart = -90f
                    val arcSweep = 360f
                    drawArc(
                        color = controlIconTint.copy(alpha = 0.18f),
                        startAngle = arcStart,
                        sweepAngle = arcSweep,
                        useCenter = false,
                        topLeft = Offset(arcInset, arcInset),
                        size = Size(size.width - (arcInset * 2f), size.height - (arcInset * 2f)),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    )
                    drawArc(
                        color = controlIconTint,
                        startAngle = arcStart,
                        sweepAngle = arcSweep * progress.coerceIn(0f, 1f),
                        useCenter = false,
                        topLeft = Offset(arcInset, arcInset),
                        size = Size(size.width - (arcInset * 2f), size.height - (arcInset * 2f)),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    )
                }
                AnimatedContent(
                    targetState = isPlaying,
                    transitionSpec = {
                        (
                            fadeIn(animationSpec = ElovaireMotion.iconSwapInSpec()) +
                                scaleIn(
                                    initialScale = 0.9f,
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
                    label = "mini_player_play_pause_icon",
                ) { playing ->
                    Icon(
                        painter = painterResource(
                            id = if (playing) R.drawable.ic_lucide_pause else R.drawable.ic_lucide_play,
                        ),
                        contentDescription = if (playing) "Pause" else "Play",
                        tint = controlIconTint,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable(
                        enabled = visible,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onSkipNext,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_elovaire_forward_filled),
                    contentDescription = "Next",
                    tint = controlIconTint,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

@Composable
internal fun FrostedChrome(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape,
    content: @Composable () -> Unit,
) {
    val darkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val baseTint = if (darkTheme) {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.78f)
    } else {
        Color.White.copy(alpha = 0.82f)
    }
    val softTint = if (darkTheme) {
        Color.White.copy(alpha = 0.06f)
    } else {
        Color.Black.copy(alpha = 0.04f)
    }
    Box(
        modifier = modifier
            .clip(shape)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = 0.99f }
                .blur(70.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            softTint,
                            baseTint.copy(alpha = 0.18f),
                            softTint.copy(alpha = 0.76f),
                        ),
                    ),
                ),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(baseTint),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .border(
                    width = 1.dp,
                    color = if (darkTheme) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.05f),
                    shape = shape,
                ),
        )
        content()
    }
}
