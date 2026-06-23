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
internal fun EmptyPlaylistState(
    modifier: Modifier = Modifier,
    onCreate: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Surface(
            onClick = onCreate,
            shape = RoundedCornerShape(ElovaireRadii.card),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 6.dp,
        ) {
            Box(
                modifier = Modifier.size(74.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lucide_plus),
                    contentDescription = "Create playlist",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        Text(
            text = "Tap to create new playlist",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
internal fun CreatePlaylistTile(
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Surface(
            shape = RoundedCornerShape(ElovaireRadii.card),
            color = MaterialTheme.colorScheme.primary,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lucide_square_plus),
                    contentDescription = "Create playlist",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        Text(
            text = uiPhrase(LocalAppLanguage.current, UiPhrase.NewPlaylist),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
        )
    }
}

@Composable
internal fun PlaylistGridTile(
    playlist: Playlist,
    previewSongs: List<Song>,
    selected: Boolean,
    selectionMode: Boolean,
    onClick: (ExpandOrigin) -> Unit,
    onLongPress: () -> Unit,
) {
    val screenSizePx = screenContainerSizePx()
    val screenWidthPx = screenSizePx.width.toFloat()
    val screenHeightPx = screenSizePx.height.toFloat()
    val language = LocalAppLanguage.current
    var bounds by remember { mutableStateOf<androidx.compose.ui.geometry.Rect?>(null) }

    Column(
        modifier = Modifier
            .onGloballyPositioned { bounds = it.boundsInWindow() }
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onClick(bounds.toExpandOrigin(screenWidthPx, screenHeightPx)) },
                onLongClick = onLongPress,
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box {
            PlaylistArtworkPreview(
                songs = previewSongs,
                title = playlist.name,
                modifier = Modifier.fillMaxWidth(),
            )
            if (selectionMode && !playlist.isSystem) {
                PlaylistSelectionIndicator(
                    selected = selected,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp),
                )
            }
        }
        Text(
            text = playlist.name,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 1,
        )
        Text(
            text = formatCountLabel(playlist.songIds.size, "song"),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

internal fun Set<Long>.togglePlaylistSelection(playlistId: Long): Set<Long> {
    return if (playlistId in this) this - playlistId else this + playlistId
}

@Composable
internal fun PlaylistSelectionIndicator(
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val tint = MaterialTheme.colorScheme.onSurface
    Box(
        modifier = modifier
            .size(26.dp)
            .clip(CircleShape)
            .background(
                if (selected) {
                    tint.copy(alpha = 0.3f)
                } else {
                    Color.Transparent
                },
            )
            .border(
                width = 1.dp,
                color = tint.copy(alpha = if (selected) 0f else 0.64f),
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lucide_check),
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Composable
internal fun PlaylistActionPill(
    label: String,
    enabled: Boolean,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(ElovaireRadii.pill),
        color = backgroundColor,
        contentColor = contentColor,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
            color = contentColor,
        )
    }
}

@Composable
internal fun PlaylistArtworkPreview(
    songs: List<Song>,
    title: String,
    modifier: Modifier = Modifier,
) {
    val collageSongs = remember(songs) {
        val usedAlbumIds = mutableSetOf<Long>()
        songs.filter { song ->
            usedAlbumIds.add(song.albumId)
        }.take(4)
    }
    val usesCollage = collageSongs.size >= 4
    val coverSong = songs.firstOrNull()
    val gradient = rememberArtworkGradient(coverSong?.artUri).value
    Box(modifier = modifier.aspectRatio(1f)) {
        if (songs.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp, top = 18.dp, end = 12.dp, bottom = 8.dp)
                    .clip(RoundedCornerShape(ElovaireRadii.artwork))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                gradient.first().copy(alpha = 0f),
                                gradient.first().copy(alpha = 0.12f),
                                gradient.last().copy(alpha = 0.2f),
                            ),
                        ),
                    )
                    .blur(30.dp),
            )
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(ElovaireRadii.artwork),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.32f),
        ) {
            when {
                usesCollage -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                    ) {
                        repeat(2) { rowIndex ->
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(1.dp),
                            ) {
                                repeat(2) { columnIndex ->
                                    val song = collageSongs.getOrNull((rowIndex * 2) + columnIndex)
                                    if (song != null) {
                                        ArtworkImage(
                                            uri = song.artUri,
                                            title = song.title,
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxHeight(),
                                            cornerRadius = 0.dp,
                                            requestedSizePx = 512,
                                        )
                                    } else {
                                        Spacer(
                                            modifier = Modifier
                                                .weight(1f)
                                            .fillMaxHeight(),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                songs.isNotEmpty() -> {
                    ArtworkImage(
                        uri = coverSong?.artUri,
                        title = coverSong?.title ?: title,
                        modifier = Modifier.fillMaxSize(),
                        cornerRadius = ElovaireRadii.artwork,
                        requestedSizePx = 384,
                    )
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lucide_music),
                            contentDescription = title.ifBlank { "Playlist artwork placeholder" },
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                            modifier = Modifier.size(40.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun PlaylistNameDialog(
    title: String = "New playlist",
    confirmLabel: String = "Save",
    initialName: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    val language = LocalAppLanguage.current
    var name by rememberSaveable(initialName) { mutableStateOf(initialName) }
    val canConfirm = name.isNotBlank()
    val displayTitle = if (title == "New playlist") uiPhrase(language, UiPhrase.NewPlaylist) else title
    val displayConfirmLabel = if (confirmLabel == "Save" || confirmLabel == "Create") uiPhrase(language, UiPhrase.Create) else confirmLabel
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                ),
            contentAlignment = Alignment.Center,
        ) {
            DynamicBackdropSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {},
                    ),
                shape = RoundedCornerShape(ElovaireRadii.card),
                overlayAlpha = 0.6f,
                borderColor = blurSurfaceBorderColor(),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    Text(
                        text = displayTitle,
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = elovaireScaledSp(24f)),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    PlaylistNameInputField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = uiPhrase(language, UiPhrase.Cancel),
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            onClick = { onConfirm(name.trim()) },
                            enabled = canConfirm,
                            shape = RoundedCornerShape(ElovaireRadii.pill),
                            color = if (canConfirm) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.92f)
                            } else {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            },
                            contentColor = if (canConfirm) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.52f)
                            },
                        ) {
                            Text(
                                text = displayConfirmLabel,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun PlaylistNameInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Playlist name",
) {
    val language = LocalAppLanguage.current
    val localizedPlaceholder = if (placeholder == "Playlist name") uiPhrase(language, UiPhrase.NewPlaylist) else placeholder
    val contentColor = MaterialTheme.colorScheme.onSurface
    val leadingIconAlpha by animateFloatAsState(
        targetValue = 0.5f,
        animationSpec = ElovaireMotion.standardTween(durationMillis = 80),
        label = "playlist_name_icon_alpha",
    )
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        shape = RoundedCornerShape(ElovaireRadii.input),
        placeholder = {
            Text(
                text = localizedPlaceholder,
                color = contentColor.copy(alpha = 0.44f),
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_lucide_pencil_line),
                contentDescription = null,
                tint = contentColor.copy(alpha = leadingIconAlpha),
                modifier = Modifier.size(16.dp),
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = value.isNotEmpty(),
                enter = fadeIn(animationSpec = ElovaireMotion.fadeMedium()) +
                    scaleIn(
                        animationSpec = ElovaireMotion.scaleSoft(),
                        initialScale = 0.92f,
                    ),
                exit = fadeOut(animationSpec = ElovaireMotion.fadeFast()) +
                    scaleOut(
                        animationSpec = ElovaireMotion.fadeFast(),
                        targetScale = 0.92f,
                    ),
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(contentColor.copy(alpha = 0.1f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onValueChange("") },
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lucide_x),
                        contentDescription = "Clear playlist name",
                        tint = contentColor.copy(alpha = 0.86f),
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.38f),
            unfocusedContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.3f),
            focusedTextColor = contentColor,
            unfocusedTextColor = contentColor,
            cursorColor = contentColor,
            focusedPlaceholderColor = contentColor.copy(alpha = 0.44f),
            unfocusedPlaceholderColor = contentColor.copy(alpha = 0.44f),
        ),
    )
}

@Composable
internal fun AddAlbumToPlaylistDialog(
    album: Album,
    playlists: List<Playlist>,
    playlistSongsById: Map<Long, Song>,
    onDismiss: () -> Unit,
    onPlaylistSelected: (Long, Album) -> Unit,
    onCreatePlaylist: (String) -> Long,
) {
    val language = LocalAppLanguage.current
    PlaylistSelectionDialog(
        title = uiPhrase(language, UiPhrase.AddToPlaylist),
        subtitle = album.title,
        playlists = playlists,
        playlistSongsById = playlistSongsById,
        onDismiss = onDismiss,
        onPlaylistSelected = { playlistId -> onPlaylistSelected(playlistId, album) },
        onCreatePlaylist = onCreatePlaylist,
    )
}

@Composable
internal fun PlaylistSelectionDialog(
    title: String,
    subtitle: String?,
    playlists: List<Playlist>,
    playlistSongsById: Map<Long, Song>,
    onDismiss: () -> Unit,
    onPlaylistSelected: (Long) -> Unit,
    onCreatePlaylist: ((String) -> Long)?,
) {
    val language = LocalAppLanguage.current
    val listState = rememberElovaireLazyListState(title, subtitle, "playlist_picker")
    val createdPlaylists = remember(title, subtitle) { mutableStateListOf<Playlist>() }
    var draftPlaylistName by rememberSaveable(title, subtitle) { mutableStateOf("") }
    var showInlineCreator by rememberSaveable(title, subtitle) { mutableStateOf(false) }
    var selectedPlaylistId by rememberSaveable(title, subtitle) { mutableStateOf<Long?>(null) }
    val visibleRows = 4
    val rowHeight = 82.dp
    val rowSpacing = 12.dp
    val listHeight = (rowHeight * visibleRows) + (rowSpacing * (visibleRows - 1))
    val displayedPlaylists = run {
        val existingIds = playlists.mapTo(mutableSetOf<Long>()) { it.id }
        playlists + createdPlaylists.filter { existingIds.add(it.id) }
    }

    LaunchedEffect(showInlineCreator, displayedPlaylists.size) {
        if (showInlineCreator) {
            listState.animateScrollToItem(displayedPlaylists.size)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                ),
            contentAlignment = Alignment.Center,
        ) {
            DynamicBackdropSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {},
                    ),
                shape = RoundedCornerShape(ElovaireRadii.card),
                overlayAlpha = 0.6f,
                borderColor = blurSurfaceBorderColor(),
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                        .animateContentSize(animationSpec = ElovaireMotion.sizeSoft()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lucide_list_plus),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    if (!subtitle.isNullOrBlank()) {
                        Text(
                            text = subtitle,
                            style = secondaryBodyTextStyle().copy(fontWeight = FontWeight.Medium),
                            color = readableSecondaryTextColor(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(listHeight),
                    ) {
                        if (displayedPlaylists.isEmpty() && !showInlineCreator) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = uiPhrase(language, UiPhrase.NewPlaylist),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = readableSecondaryTextColor(),
                                    textAlign = TextAlign.Center,
                                )
                            }
                        } else {
                            LazyColumn(
                                state = listState,
                                overscrollEffect = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .ensureSingleItemRubberBand(listState),
                                verticalArrangement = Arrangement.spacedBy(rowSpacing),
                            ) {
                                items(displayedPlaylists, key = { it.id }) { playlist ->
                                    val previewSongs = playlist.songIds.mapNotNull(playlistSongsById::get)
                                    PlaylistPickerRow(
                                        playlist = playlist,
                                        previewSongs = previewSongs,
                                        selected = playlist.id == selectedPlaylistId,
                                        modifier = Modifier.animateItem(
                                            placementSpec = spring(
                                                dampingRatio = 0.76f,
                                                stiffness = 360f,
                                            ),
                                        ),
                                        onClick = {
                                            selectedPlaylistId = if (selectedPlaylistId == playlist.id) {
                                                null
                                            } else {
                                                playlist.id
                                            }
                                        },
                                    )
                                }
                                if (showInlineCreator && onCreatePlaylist != null) {
                                    item(key = "inline_playlist_creator") {
                                        InlinePlaylistCreatorRow(
                                            name = draftPlaylistName,
                                            onNameChange = { draftPlaylistName = it },
                                            modifier = Modifier.animateItem(
                                                placementSpec = spring(
                                                    dampingRatio = 0.76f,
                                                    stiffness = 360f,
                                                ),
                                            ),
                                            onSave = {
                                                val trimmedName = draftPlaylistName.trim()
                                                if (trimmedName.isBlank()) return@InlinePlaylistCreatorRow
                                                val createdId = onCreatePlaylist(trimmedName)
                                                if (createdId > 0L) {
                                                    createdPlaylists += Playlist(id = createdId, name = trimmedName)
                                                    selectedPlaylistId = createdId
                                                    draftPlaylistName = ""
                                                    showInlineCreator = false
                                                }
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (onCreatePlaylist != null) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(40.dp)
                                .align(Alignment.CenterHorizontally),
                            onClick = {
                                showInlineCreator = true
                                selectedPlaylistId = null
                            },
                            shape = RoundedCornerShape(ElovaireRadii.pill),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            contentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.92f),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_lucide_plus),
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = uiPhrase(language, UiPhrase.NewPlaylist),
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = uiPhrase(language, UiPhrase.Cancel),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Surface(
                            onClick = {
                                selectedPlaylistId?.let(onPlaylistSelected)
                            },
                            enabled = selectedPlaylistId != null,
                            shape = RoundedCornerShape(ElovaireRadii.pill),
                            color = if (selectedPlaylistId != null) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.92f)
                            } else {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            },
                            contentColor = if (selectedPlaylistId != null) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.52f)
                            },
                        ) {
                            Text(
                                text = uiPhrase(language, UiPhrase.AddToPlaylist),
                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun PlaylistPickerRow(
    playlist: Playlist,
    previewSongs: List<Song>,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val highlightColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        } else {
            Color.Transparent
        },
        animationSpec = ElovaireMotion.colorFadeSpec(),
        label = "playlist_picker_row_highlight",
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(ElovaireRadii.tile))
            .background(highlightColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 4.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PlaylistArtworkPreview(
                songs = previewSongs,
                title = playlist.name,
                modifier = Modifier.size(62.dp),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text(
                    text = playlist.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (playlist.songIds.isEmpty()) {
                    Text(
                        text = "No songs in this playlist yet",
                        style = MaterialTheme.typography.labelLarge,
                        color = readableSecondaryTextColor().copy(alpha = 0.7f),
                    )
                } else {
                    val durationMs = remember(previewSongs) { previewSongs.sumOf { it.durationMs } }
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.onSurface,
                                ),
                            ) {
                                append(formatCountLabel(playlist.songIds.size, "track"))
                            }
                            append("  •  ")
                            withStyle(
                                SpanStyle(
                                    color = readableSecondaryTextColor().copy(alpha = 0.82f),
                                ),
                            ) {
                                append(formatPlaylistDuration(durationMs))
                            }
                        },
                        style = secondaryBodyTextStyle().copy(fontSize = MaterialTheme.typography.labelLarge.fontSize),
                    )
                }
            }
            SelectionIndicatorIcon(
                selected = selected,
                modifier = Modifier.padding(end = 6.dp),
            )
        }
    }
}

@Composable
internal fun InlinePlaylistCreatorRow(
    name: String,
    onNameChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val canSave = name.isNotBlank()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(ElovaireRadii.tile))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
            .padding(10.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PlaylistArtworkPreview(
                    songs = emptyList(),
                    title = uiPhrase(LocalAppLanguage.current, UiPhrase.NewPlaylist),
                    modifier = Modifier.size(62.dp),
                )
                PlaylistNameInputField(
                    value = name,
                    onValueChange = onNameChange,
                    modifier = Modifier.weight(1f),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    onClick = onSave,
                    enabled = canSave,
                    shape = RoundedCornerShape(ElovaireRadii.pill),
                    color = if (canSave) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.92f)
                    } else {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    },
                    contentColor = if (canSave) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.52f)
                    },
                ) {
                    Text(
                        text = "Save",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
            }
        }
    }
}
