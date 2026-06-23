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
internal fun PlaylistDetailScreen(
    playlist: Playlist?,
    librarySongs: List<Song>,
    favoriteSongIds: Set<Long>,
    currentSongId: Long?,
    isCurrentSongPlaying: Boolean,
    bottomPadding: Dp,
    onBack: () -> Unit,
    onPlayPlaylist: (List<Song>, String) -> Unit,
    onShufflePlaylist: (List<Song>, String) -> Unit,
    onSongSelected: (Song, List<Song>) -> Unit,
    onAddSongs: (List<Long>) -> Unit,
    onUpdateSongOrder: (List<Long>) -> Unit,
    onRenamePlaylist: (Long, String) -> Unit,
    onDeletePlaylist: (Long) -> Unit,
    onToggleFavorite: (Long) -> Unit,
) {
    if (playlist == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Playlist not found.")
        }
        return
    }

    val songsById = remember(librarySongs) { librarySongs.associateBy { it.id } }
    val defaultSongMenuActions = LocalSongMenuActions.current
    var editMode by rememberSaveable(playlist.id) { mutableStateOf(false) }
    var showAddSongsPicker by rememberSaveable(playlist.id) { mutableStateOf(false) }
    var editableSongIds by rememberSaveable(playlist.id) { mutableStateOf(playlist.songIds) }
    var songIdsMarkedForRemoval by rememberSaveable(playlist.id) { mutableStateOf(setOf<Long>()) }
    var activelyDraggedSongId by rememberSaveable(playlist.id) { mutableStateOf<Long?>(null) }
    var showEditModeMenu by rememberSaveable(playlist.id) { mutableStateOf(false) }
    var showRenameDialog by rememberSaveable(playlist.id) { mutableStateOf(false) }
    LaunchedEffect(playlist.id, playlist.songIds, editMode) {
        if (!editMode) {
            editableSongIds = playlist.songIds
            songIdsMarkedForRemoval = emptySet()
            activelyDraggedSongId = null
        }
    }
    val displayedSongIds = if (editMode) editableSongIds else playlist.songIds
    val playlistSongMenuActions = remember(defaultSongMenuActions, playlist.id, playlist.songIds) {
        defaultSongMenuActions.copy(
            onDeleteFromLibrary = { song ->
                onUpdateSongOrder(playlist.songIds.filterNot { it == song.id })
            },
            deletePhrase = UiPhrase.RemoveFromList,
        )
    }
    val playlistSongs = remember(displayedSongIds, songsById) {
        displayedSongIds.mapNotNull(songsById::get)
    }
    val playlistDurationMs = remember(playlistSongs) { playlistSongs.sumOf { it.durationMs } }
    val detailTopPadding = detailTopBarOccupiedHeight()
    val editMenuTopInset by animateDpAsState(
        targetValue = if (editMode && showEditModeMenu) 50.dp else 0.dp,
        animationSpec = ElovaireMotion.sizeSoft(),
        label = "playlist_edit_menu_top_inset",
    )
    val topBarActions = remember(editMode, playlist.isSystem) {
        buildList {
            if (editMode && !playlist.isSystem) {
                add(
                    TopBarActionSpec(
                        iconResId = R.drawable.ic_lucide_plus,
                        contentDescription = "Add songs",
                        onClick = { showAddSongsPicker = true },
                    ),
                )
            }
            if (!playlist.isSystem) {
                add(
                    TopBarActionSpec(
                        iconResId = if (editMode) R.drawable.ic_lucide_check else R.drawable.ic_lucide_square_pen,
                        contentDescription = if (editMode) "Save playlist changes" else "Edit playlist",
                        onClick = {
                            if (editMode) {
                                val updatedSongIds = editableSongIds.filterNot { it in songIdsMarkedForRemoval }
                                onUpdateSongOrder(updatedSongIds)
                                editableSongIds = updatedSongIds
                                songIdsMarkedForRemoval = emptySet()
                                activelyDraggedSongId = null
                                editMode = false
                                showEditModeMenu = false
                            } else {
                                editableSongIds = playlist.songIds
                                songIdsMarkedForRemoval = emptySet()
                                editMode = true
                                showEditModeMenu = true
                            }
                        },
                    ),
                )
            }
        }
    }
    BackHandler(enabled = showAddSongsPicker || editMode) {
        if (showAddSongsPicker) {
            showAddSongsPicker = false
        } else if (editMode) {
            val updatedSongIds = editableSongIds.filterNot { it in songIdsMarkedForRemoval }
            onUpdateSongOrder(updatedSongIds)
            editableSongIds = updatedSongIds
            songIdsMarkedForRemoval = emptySet()
            activelyDraggedSongId = null
            editMode = false
            showEditModeMenu = false
        }
    }
    CompositionLocalProvider(LocalSongMenuActions provides playlistSongMenuActions) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
        val listState = rememberElovaireLazyListState(playlist.id, "playlist_detail")
        val scope = rememberCoroutineScope()
        LazyColumn(
            state = listState,
            overscrollEffect = null,
            userScrollEnabled = true,
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .ensureSingleItemRubberBand(listState),
            contentPadding = PaddingValues(
                start = 20.dp,
                top = detailTopPadding + ElovaireSpacing.albumHeaderTopGap + editMenuTopInset,
                end = 20.dp,
                bottom = bottomPadding,
            ),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    PlaylistArtworkPreview(
                        songs = playlistSongs,
                        title = playlist.name,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Text(
                                text = playlist.name,
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontSize = elovaireScaledSp(ALBUM_HEADER_TITLE_TEXT_SIZE_SP),
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = MaterialTheme.typography.displayLarge.lineHeight * 0.8f,
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        SpanStyle(
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontWeight = FontWeight.Normal,
                                        ),
                                    ) {
                                        append(formatCountLabel(playlistSongs.size, "track"))
                                    }
                                    append("  •  ")
                                    withStyle(
                                        SpanStyle(
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                                            fontWeight = FontWeight.Normal,
                                        ),
                                    ) {
                                        append(formatPlaylistDuration(playlistDurationMs))
                                    }
                                },
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = elovaireScaledSp(12f)),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AlbumHeaderPlayButton(
                                tint = Color.White,
                                backgroundColor = RoseAccent,
                                onClick = { onPlayPlaylist(playlistSongs, playlist.name) },
                            )
                            if (playlistSongs.isNotEmpty()) {
                                AlbumHeaderActionButton(
                                    iconResId = R.drawable.ic_lucide_shuffle,
                                    contentDescription = "Shuffle playlist",
                                    tint = Color.White,
                                    backgroundColor = RoseAccent.copy(alpha = 0.7f),
                                    iconSize = 18.dp,
                                    onClick = { onShufflePlaylist(playlistSongs, playlist.name) },
                                )
                            }
                        }
                    }
                }
            }
            if (playlistSongs.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            if (playlistSongs.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(top = 34.dp, bottom = 34.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(
                            text = miscPhrase(LocalAppLanguage.current, MiscPhrase.NoSongsYet),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = miscPhrase(LocalAppLanguage.current, MiscPhrase.AddSongsViaEdit),
                            style = MaterialTheme.typography.bodyLarge,
                            color = readableSecondaryTextColor(),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            } else {
                itemsIndexed(
                    items = playlistSongs,
                    key = { index, song -> "${song.id}_$index" },
                    contentType = { _, _ -> "playlist_song_row" },
                ) { index, song ->
                    GroupedListRowContainer(
                        index = index,
                        lastIndex = playlistSongs.lastIndex,
                        modifier = if (editMode) {
                            Modifier.animateItem(
                                placementSpec = spring(
                                    dampingRatio = 0.52f,
                                    stiffness = 190f,
                                ),
                            )
                        } else {
                            Modifier
                        },
                    ) {
                        PlaylistSongRow(
                            song = song,
                            isFavorite = song.id in favoriteSongIds,
                            isCurrentSong = song.id == currentSongId,
                            isPlaybackActive = isCurrentSongPlaying,
                            editMode = editMode,
                            onClick = {
                                if (!editMode) {
                                    onSongSelected(song, playlistSongs)
                                }
                            },
                            markedForRemoval = song.id in songIdsMarkedForRemoval,
                            onLongPress = {
                                if (!playlist.isSystem && playlistSongs.isNotEmpty()) {
                                    if (!editMode) {
                                        editableSongIds = playlist.songIds
                                        songIdsMarkedForRemoval = emptySet()
                                        editMode = true
                                        showEditModeMenu = true
                                    }
                                }
                            },
                            onToggleMarkedForRemoval = {
                                songIdsMarkedForRemoval = if (song.id in songIdsMarkedForRemoval) {
                                    songIdsMarkedForRemoval - song.id
                                } else {
                                    songIdsMarkedForRemoval + song.id
                                }
                            },
                            isDragged = activelyDraggedSongId == song.id,
                            onDragActiveChanged = { isActive ->
                                activelyDraggedSongId = when {
                                    isActive -> song.id
                                    activelyDraggedSongId == song.id -> null
                                    else -> activelyDraggedSongId
                                }
                            },
                            onToggleFavorite = { onToggleFavorite(song.id) },
                            onMoveBy = { delta ->
                                if (editMode && delta != 0) {
                                    val fromIndex = editableSongIds.indexOf(song.id)
                                    if (fromIndex >= 0) {
                                        val targetIndex = (fromIndex + delta).coerceIn(0, editableSongIds.lastIndex)
                                        if (targetIndex != fromIndex) {
                                            editableSongIds = editableSongIds.toMutableList().apply {
                                                add(targetIndex, removeAt(fromIndex))
                                            }.toList()
                                        }
                                    }
                                }
                            },
                            onReorderDrag = { dragAmount ->
                                if (editMode && editableSongIds.size > 1) {
                                    val visibleSongItems = listState.layoutInfo.visibleItemsInfo
                                        .filter { it.contentType == "playlist_song_row" }
                                    val currentAbsoluteIndex = index + 2
                                    val firstVisibleSongIndex = visibleSongItems.firstOrNull()?.index ?: currentAbsoluteIndex
                                    val lastVisibleSongIndex = visibleSongItems.lastOrNull()?.index ?: currentAbsoluteIndex
                                    val canScrollUp =
                                        listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
                                    val canScrollDown =
                                        visibleSongItems.lastOrNull()?.index != listState.layoutInfo.totalItemsCount - 1
                                    when {
                                        dragAmount < 0f &&
                                            currentAbsoluteIndex <= firstVisibleSongIndex &&
                                            canScrollUp -> {
                                            scope.launch {
                                                listState.scrollBy((dragAmount * 0.72f).coerceAtLeast(-22f))
                                            }
                                        }
                                        dragAmount > 0f &&
                                            currentAbsoluteIndex >= lastVisibleSongIndex &&
                                            canScrollDown -> {
                                            scope.launch {
                                                listState.scrollBy((dragAmount * 0.72f).coerceAtMost(22f))
                                            }
                                        }
                                    }
                                }
                            },
                            showOverflowMenu = !editMode,
                            showDivider = index != playlistSongs.lastIndex,
                        )
                    }
                }
            }
        }

        DetailListTopBar(
            title = playlist.name,
            subtitle = localizedCountLabel(playlistSongs.size, "song", LocalAppLanguage.current),
            onBack = {
                if (showAddSongsPicker) {
                    showAddSongsPicker = false
                } else if (editMode) {
                    val updatedSongIds = editableSongIds.filterNot { it in songIdsMarkedForRemoval }
                    onUpdateSongOrder(updatedSongIds)
                    editableSongIds = updatedSongIds
                    songIdsMarkedForRemoval = emptySet()
                    activelyDraggedSongId = null
                    editMode = false
                    showEditModeMenu = false
                } else {
                    onBack()
                }
            },
            actions = topBarActions,
            modifier = Modifier.align(Alignment.TopCenter),
        )
        AnimatedVisibility(
            visible = editMode && showEditModeMenu && !playlist.isSystem,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .zIndex(3f),
            enter = ElovaireMotion.verticalRevealEnter(),
            exit = ElovaireMotion.verticalRevealExit(),
        ) {
            TopBarDualActionMenu(
                topBarHeight = detailTopPadding,
                leadingAction = TopBarMenuAction(
                    iconResId = R.drawable.ic_lucide_square_pen,
                    label = uiPhrase(LocalAppLanguage.current, UiPhrase.Rename),
                    tint = MaterialTheme.colorScheme.onSurface,
                    onClick = { showRenameDialog = true },
                ),
                trailingAction = TopBarMenuAction(
                    iconResId = R.drawable.ic_lucide_trash_2,
                    label = uiPhrase(LocalAppLanguage.current, UiPhrase.RemoveFromList),
                    tint = DestructiveRed,
                    enabled = songIdsMarkedForRemoval.isNotEmpty(),
                    onClick = {
                        if (songIdsMarkedForRemoval.isNotEmpty()) {
                            editableSongIds = editableSongIds.filterNot { it in songIdsMarkedForRemoval }
                            songIdsMarkedForRemoval = emptySet()
                            activelyDraggedSongId = null
                        }
                    },
                ),
            )
        }
    }
    }

    if (showAddSongsPicker && !playlist.isSystem) {
        AddSongsToPlaylistOverlay(
            availableSongs = librarySongs,
            existingSongIds = editableSongIds.toSet(),
            onDismiss = { showAddSongsPicker = false },
            onAddSongs = { selectedSongIds ->
                editableSongIds = (editableSongIds + selectedSongIds).distinct()
                showAddSongsPicker = false
            },
        )
    }
    if (showRenameDialog && !playlist.isSystem) {
        PlaylistNameDialog(
            title = "Rename playlist",
            confirmLabel = "Save",
            initialName = playlist.name,
            onDismiss = { showRenameDialog = false },
            onConfirm = { name ->
                onRenamePlaylist(playlist.id, name)
                showRenameDialog = false
            },
        )
    }
}

@Composable
internal fun PlaylistSongRow(
    song: Song,
    isFavorite: Boolean,
    isCurrentSong: Boolean = false,
    isPlaybackActive: Boolean = false,
    editMode: Boolean = false,
    onClick: () -> Unit,
    markedForRemoval: Boolean = false,
    onLongPress: () -> Unit = {},
    onToggleMarkedForRemoval: () -> Unit = {},
    isDragged: Boolean = false,
    onDragActiveChanged: (Boolean) -> Unit = {},
    onToggleFavorite: () -> Unit,
    onMoveBy: (Int) -> Unit = {},
    onReorderDrag: (Float) -> Unit = {},
    showOverflowMenu: Boolean = false,
    showDivider: Boolean,
) {
    val density = LocalDensity.current
    val reorderStepPx = remember(density) { with(density) { 18.dp.toPx() } }
    var reorderDragAccumulator by remember(song.id, editMode) { mutableFloatStateOf(0f) }
    var handleDragActive by remember(song.id, editMode) { mutableStateOf(false) }
    val visualDragOffsetY = reorderDragAccumulator.coerceIn(-20f, 20f)
    val handleTint by animateColorAsState(
        targetValue = if (handleDragActive) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.94f)
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        },
        animationSpec = ElovaireMotion.colorFadeSpec(),
        label = "playlist_reorder_handle_tint",
    )
    val handleScale by animateFloatAsState(
        targetValue = if (handleDragActive) 1.1f else 1f,
        animationSpec = ElovaireMotion.releaseSpringSpec(),
        label = "playlist_reorder_handle_scale",
    )
    val rowScale by animateFloatAsState(
        targetValue = if (isDragged) 1.018f else 1f,
        animationSpec = if (isDragged) {
            spring(
                dampingRatio = 0.5f,
                stiffness = 210f,
            )
        } else {
            ElovaireMotion.releaseSpringSpec()
        },
        label = "playlist_drag_row_scale",
    )
    val rowTranslationY by animateFloatAsState(
        targetValue = if (isDragged) visualDragOffsetY else 0f,
        animationSpec = spring(
            dampingRatio = 0.42f,
            stiffness = 250f,
        ),
        label = "playlist_drag_row_translation",
    )
    val dragHighlight by animateColorAsState(
        targetValue = if (handleDragActive) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        } else {
            Color.Transparent
        },
        animationSpec = ElovaireMotion.colorFadeSpec(),
        label = "playlist_reorder_drag_highlight",
    )
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(ElovaireRadii.tile))
                .background(dragHighlight)
                .graphicsLayer {
                    scaleX = rowScale
                    scaleY = rowScale
                    translationY = rowTranslationY
                }
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick,
                    onLongClick = onLongPress,
                )
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ElovaireAnimatedVisibility(
                visible = editMode,
                enter = fadeIn(animationSpec = ElovaireMotion.fadeMedium()) +
                    slideInHorizontally(
                        initialOffsetX = { -it / 2 },
                        animationSpec = ElovaireMotion.offsetSoft(),
                    ),
                exit = fadeOut(animationSpec = ElovaireMotion.fadeFast()) +
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = ElovaireMotion.offsetSoft(durationMillis = 80),
                    ),
                label = "playlist_song_reorder_handle",
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .graphicsLayer {
                            scaleX = handleScale
                            scaleY = handleScale
                        }
                        .pointerInput(song.id, editMode) {
                            detectVerticalDragGestures(
                                onDragStart = {
                                    reorderDragAccumulator = 0f
                                    handleDragActive = true
                                    onDragActiveChanged(true)
                                },
                                onVerticalDrag = { change, dragAmount ->
                                    change.consume()
                                    reorderDragAccumulator += dragAmount
                                    onReorderDrag(dragAmount)
                                    while (reorderDragAccumulator <= -reorderStepPx) {
                                        onMoveBy(-1)
                                        reorderDragAccumulator += reorderStepPx
                                    }
                                    while (reorderDragAccumulator >= reorderStepPx) {
                                        onMoveBy(1)
                                        reorderDragAccumulator -= reorderStepPx
                                    }
                                },
                                onDragEnd = {
                                    reorderDragAccumulator = 0f
                                    handleDragActive = false
                                    onDragActiveChanged(false)
                                },
                                onDragCancel = {
                                    reorderDragAccumulator = 0f
                                    handleDragActive = false
                                    onDragActiveChanged(false)
                                },
                            )
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lucide_chevrons_up_down),
                        contentDescription = "Reorder song",
                        tint = handleTint,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
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
                    visible = isCurrentSong && isPlaybackActive,
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ExplicitTitleText(
                        title = song.title,
                        isExplicit = song.isExplicit,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Row(
                modifier = Modifier.width(if (editMode) 36.dp else if (showOverflowMenu) 96.dp else 64.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ElovaireAnimatedVisibility(
                    visible = !editMode,
                    enter = fadeIn(animationSpec = ElovaireMotion.fadeMedium()),
                    exit = fadeOut(animationSpec = ElovaireMotion.fadeFast()),
                    label = "playlist_song_metadata_visibility",
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = formatDuration(song.durationMs),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                            maxLines = 1,
                            textAlign = TextAlign.End,
                            modifier = Modifier.width(45.dp),
                        )
                        InlineFavoriteSongButton(
                            isFavorite = isFavorite,
                            tint = MaterialTheme.colorScheme.onSurface,
                            onClick = onToggleFavorite,
                        )
                        if (showOverflowMenu) {
                            SongOverflowMenuButton(
                                song = song,
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
                ElovaireAnimatedVisibility(
                    visible = editMode,
                    enter = fadeIn(animationSpec = ElovaireMotion.fadeMedium()) +
                        scaleIn(
                            initialScale = 0.92f,
                            animationSpec = ElovaireMotion.scaleSoft(),
                        ),
                    exit = fadeOut(animationSpec = ElovaireMotion.fadeFast()) +
                        scaleOut(
                            targetScale = 0.92f,
                            animationSpec = ElovaireMotion.fadeFast(),
                        ),
                    label = "playlist_song_remove_toggle",
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onToggleMarkedForRemoval,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        SelectionIndicatorIcon(selected = markedForRemoval)
                    }
                }
            }
        }
        if (showDivider) {
            DividerLine()
        }
    }
}

@Composable
internal fun AddSongsToPlaylistOverlay(
    availableSongs: List<Song>,
    existingSongIds: Set<Long>,
    onDismiss: () -> Unit,
    onAddSongs: (List<Long>) -> Unit,
) {
    var selectedTab by rememberSaveable { mutableStateOf(PlaylistPickerTab.Songs) }
    var query by rememberSaveable { mutableStateOf("") }
    var selectedSongIds by rememberSaveable { mutableStateOf(listOf<Long>()) }
    var selectedAlbumId by rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedArtistName by rememberSaveable { mutableStateOf<String?>(null) }
    var listResetVersion by rememberSaveable { mutableLongStateOf(0L) }
    val selectedSongIdSet = remember(selectedSongIds) { selectedSongIds.toSet() }
    val listState = rememberElovaireLazyListState(
        "playlist_add_songs_overlay",
        selectedTab.name,
        selectedAlbumId ?: -1L,
        selectedArtistName.orEmpty(),
        listResetVersion,
    )
    val candidateSongs = remember(availableSongs, existingSongIds) {
        availableSongs.filterNot { it.id in existingSongIds }
    }
    val trimmedQuery = query.trim()
    val albums = remember(candidateSongs) {
        candidateSongs.groupBy { it.albumId }
            .values
            .mapNotNull { songs ->
                songs.firstOrNull()?.let { first ->
                    val orderedSongs = songs.sortedWith(
                        compareBy<Song> { it.discNumber.takeIf { disc -> disc > 0 } ?: Int.MAX_VALUE }
                            .thenBy { it.trackNumber.takeIf { track -> track > 0 } ?: Int.MAX_VALUE }
                            .thenBy { it.dateAddedSeconds }
                            .thenBy { it.id },
                    )
                    Album(
                        id = first.albumId,
                        title = first.album,
                        artist = first.artist,
                        artUri = first.artUri,
                        songCount = orderedSongs.size,
                        durationMs = orderedSongs.sumOf { it.durationMs },
                        songs = orderedSongs,
                    )
                }
            }
            .sortedWith(compareBy<Album> { it.artist.lowercase() }.thenBy { it.title.lowercase() })
    }
    val artists = remember(candidateSongs) {
        candidateSongs.groupBy { it.artist }
            .map { (artistName, songs) ->
                ArtistEntry(
                    name = artistName,
                    artUri = songs.firstOrNull()?.artUri,
                    albumCount = songs.map { it.albumId }.distinct().size,
                    songCount = songs.size,
                ) to songs.sortedBy { it.title.lowercase() }
            }
            .sortedBy { it.first.name.lowercase() }
    }
    val filteredAlbums = remember(albums, trimmedQuery) {
        albums.filter { album ->
            searchMatchesComposite(trimmedQuery, listOf(album.title, album.artist))
        }
    }
    val filteredArtists = remember(artists, trimmedQuery) {
        artists.filter { (artist, songs) ->
            searchMatchesComposite(trimmedQuery, buildList {
                add(artist.name)
                songs.firstOrNull()?.album?.let(::add)
            })
        }
    }
    val filteredSongs = remember(candidateSongs, trimmedQuery) {
        candidateSongs.filter { song ->
            searchMatchesComposite(trimmedQuery, listOf(song.title, song.artist, song.album))
        }
    }
    val selectedAlbum = remember(selectedAlbumId, albums) {
        albums.firstOrNull { it.id == selectedAlbumId }
    }
    val selectedArtistSongs = remember(selectedArtistName, artists) {
        artists.firstOrNull { it.first.name == selectedArtistName }?.second.orEmpty()
    }
    val filteredAlbumSongs = remember(selectedAlbum) {
        selectedAlbum?.songs
            .orEmpty()
    }
    val filteredArtistSongs = remember(selectedArtistSongs, trimmedQuery) {
        selectedArtistSongs.filter { song ->
            searchMatchesComposite(trimmedQuery, listOf(song.title, song.artist, song.album))
        }
    }
    val handleBack: () -> Unit = {
        when {
            selectedAlbumId != null -> {
                selectedAlbumId = null
                listResetVersion += 1L
            }
            selectedArtistName != null -> {
                selectedArtistName = null
                listResetVersion += 1L
            }
            else -> onDismiss()
        }
    }
    val currentHandleBack = rememberUpdatedState(handleBack)
    val stableHandleBack = remember {
        { currentHandleBack.value.invoke() }
    }
    BackHandler(enabled = selectedAlbumId != null || selectedArtistName != null) {
        stableHandleBack()
    }
    val overlayTopPadding = detailTopBarOccupiedHeight()
    val overlayBottomPadding = 124.dp + buttonNavigationScrollBoost()
    val selectedSongIdsState = rememberUpdatedState(selectedSongIds)
    val topBarActions = remember(onAddSongs, onDismiss) {
        listOf(
            TopBarActionSpec(
                iconResId = R.drawable.ic_lucide_check,
                contentDescription = "Confirm added songs",
                onClick = {
                    val currentSelectedSongIds = selectedSongIdsState.value
                    if (currentSelectedSongIds.isNotEmpty()) {
                        onAddSongs(currentSelectedSongIds)
                    } else {
                        onDismiss()
                    }
                },
            ),
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .zIndex(12f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = overlayTopPadding),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PlaylistPickerTab.entries.forEach { tab ->
                        val selected = selectedTab == tab
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(ElovaireRadii.pill))
                                .clickable {
                                    if (selected) {
                                        selectedAlbumId = null
                                        selectedArtistName = null
                                        listResetVersion += 1L
                                    } else {
                                        selectedTab = tab
                                        selectedAlbumId = null
                                        selectedArtistName = null
                                        listResetVersion += 1L
                                    }
                                }
                                .padding(vertical = 14.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(id = tab.iconResId),
                                contentDescription = null,
                                tint = if (selected) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f)
                                },
                                modifier = Modifier.size(16.dp),
                            )
                            Text(
                                text = tab.label,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = if (selected) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f)
                                },
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
                )
            }

            val searchBarContentColor = MaterialTheme.colorScheme.onSurface
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 16.dp),
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(ElovaireRadii.input),
                    singleLine = true,
                    placeholder = { Text("Search library") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lucide_search),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp),
                        )
                    },
                    trailingIcon = {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = trimmedQuery.isNotBlank(),
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
                                    .background(searchBarContentColor.copy(alpha = 0.1f))
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = { query = "" },
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_lucide_x),
                                    contentDescription = "Clear search",
                                    tint = searchBarContentColor.copy(alpha = 0.86f),
                                    modifier = Modifier.size(14.dp),
                                )
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        cursorColor = MaterialTheme.colorScheme.onSurface,
                        focusedPlaceholderColor = searchBarContentColor.copy(alpha = 0.5f),
                        unfocusedPlaceholderColor = searchBarContentColor.copy(alpha = 0.5f),
                    ),
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                val pickerContentKey = when {
                    selectedTab == PlaylistPickerTab.Albums && selectedAlbum != null -> "album:${selectedAlbum.id}"
                    selectedTab == PlaylistPickerTab.Artists && selectedArtistName != null -> "artist:${selectedArtistName.orEmpty()}"
                    else -> "tab:${selectedTab.name}"
                }
                ElovaireAnimatedContent(
                    targetState = pickerContentKey,
                    modifier = Modifier.fillMaxSize(),
                    transitionSpec = {
                        val forward = targetState > initialState
                        (fadeIn(
                            animationSpec = tween(
                                durationMillis = 120,
                                easing = LinearOutSlowInEasing,
                            ),
                            initialAlpha = 0.86f,
                        ) + slideInHorizontally(
                            animationSpec = tween(
                                durationMillis = 160,
                                easing = FastOutSlowInEasing,
                            ),
                            initialOffsetX = { fullWidth ->
                                val offset = (fullWidth / 18f).roundToInt()
                                if (forward) offset else -offset
                            },
                        )) togetherWith (fadeOut(
                            animationSpec = tween(
                                durationMillis = 50,
                                easing = FastOutLinearInEasing,
                            ),
                            targetAlpha = 0.9f,
                        ) + slideOutHorizontally(
                            animationSpec = tween(
                                durationMillis = 110,
                                easing = FastOutSlowInEasing,
                            ),
                            targetOffsetX = { fullWidth ->
                                val offset = (fullWidth / 22f).roundToInt()
                                if (forward) -offset else offset
                            },
                        ))
                    },
                    label = "PlaylistAddSongsContent",
                ) {
                    LazyColumn(
                        state = listState,
                        overscrollEffect = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .ensureSingleItemRubberBand(listState),
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            end = 20.dp,
                            bottom = overlayBottomPadding,
                        ),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        when {
                            selectedTab == PlaylistPickerTab.Albums && selectedAlbum != null -> {
                                items(filteredAlbumSongs, key = { it.id }) { song ->
                                    val selected = song.id in selectedSongIdSet
                                    SelectableSongRow(
                                        song = song,
                                        selected = selected,
                                        selectionIndicatorOnRight = true,
                                        showDivider = song != filteredAlbumSongs.lastOrNull(),
                                        onClick = {
                                            selectedSongIds = if (selected) {
                                                selectedSongIds.filterNot { it == song.id }
                                            } else {
                                                selectedSongIds + song.id
                                            }
                                        },
                                    )
                                }
                            }
                            selectedTab == PlaylistPickerTab.Artists && selectedArtistName != null -> {
                                items(filteredArtistSongs, key = { it.id }) { song ->
                                    val selected = song.id in selectedSongIdSet
                                    SelectableSongRow(
                                        song = song,
                                        selected = selected,
                                        selectionIndicatorOnRight = true,
                                        showDivider = song != filteredArtistSongs.lastOrNull(),
                                        onClick = {
                                            selectedSongIds = if (selected) {
                                                selectedSongIds.filterNot { it == song.id }
                                            } else {
                                                selectedSongIds + song.id
                                            }
                                        },
                                    )
                                }
                            }
                            selectedTab == PlaylistPickerTab.Albums -> {
                                items(filteredAlbums, key = { it.id }) { album ->
                                    SelectableAlbumPickerRow(
                                        album = album,
                                        selected = album.songs.all { it.id in selectedSongIdSet } && album.songs.isNotEmpty(),
                                        showDivider = album != filteredAlbums.lastOrNull(),
                                        onOpen = { selectedAlbumId = album.id },
                                        onToggleSelection = {
                                            val albumSongIds = album.songs.map(Song::id)
                                            val allSelected = albumSongIds.all { it in selectedSongIdSet }
                                            selectedSongIds = if (allSelected) {
                                                selectedSongIds.filterNot { it in albumSongIds }
                                            } else {
                                                (selectedSongIds + albumSongIds).distinct()
                                            }
                                        },
                                    )
                                }
                            }

                            selectedTab == PlaylistPickerTab.Artists -> {
                                itemsIndexed(filteredArtists, key = { _, item -> item.first.name }) { index, (artist, _) ->
                                    ArtistRow(
                                        artist = artist,
                                        onClick = { selectedArtistName = artist.name },
                                    )
                                    if (index != filteredArtists.lastIndex) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                    }
                                }
                            }

                            selectedTab == PlaylistPickerTab.Songs -> {
                                items(filteredSongs, key = { it.id }) { song ->
                                    val selected = song.id in selectedSongIdSet
                                    SelectableSongRow(
                                        song = song,
                                        selected = selected,
                                        selectionIndicatorOnRight = true,
                                        showDivider = song != filteredSongs.lastOrNull(),
                                        onClick = {
                                            selectedSongIds = if (selected) {
                                                selectedSongIds.filterNot { it == song.id }
                                            } else {
                                                selectedSongIds + song.id
                                            }
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
                FastScrollbar(
                    state = listState,
                    topInset = 0.dp,
                    bottomInset = overlayBottomPadding - 6.dp,
                )
            }
        }

        DetailListTopBar(
            title = when {
                selectedAlbum != null -> selectedAlbum.title
                selectedArtistName != null -> selectedArtistName.orEmpty()
                else -> miscPhrase(LocalAppLanguage.current, MiscPhrase.AddSongs)
            },
            subtitle = when (selectedSongIds.size) {
                0 -> when {
                    selectedAlbum != null -> selectedAlbum.artist
                    selectedArtistName != null -> miscPhrase(LocalAppLanguage.current, MiscPhrase.ChooseSongs)
                    else -> null
                }
                else -> localizedCountLabel(selectedSongIds.size, "song", LocalAppLanguage.current)
            },
            onBack = stableHandleBack,
            actions = topBarActions,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Composable
internal fun SelectableCollectionRow(
    title: String,
    subtitle: String,
    artUri: Uri?,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(ElovaireRadii.tile),
        color = readableCardSurfaceColor(),
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.size(62.dp)) {
                ArtworkImage(
                    uri = artUri,
                    title = title,
                    modifier = Modifier.matchParentSize(),
                    cornerRadius = ElovaireRadii.artworkSmall,
                )
                SelectionIndicatorIcon(
                    selected = selected,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp),
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelLarge,
                    color = readableSecondaryTextColor(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
internal fun SelectableSongRow(
    song: Song,
    selected: Boolean,
    selectionIndicatorOnRight: Boolean = false,
    showDivider: Boolean = true,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.size(44.dp)) {
                ArtworkImage(
                    uri = song.artUri,
                    title = song.album,
                    modifier = Modifier.matchParentSize(),
                    cornerRadius = ElovaireRadii.artworkSmall,
                )
                if (!selectionIndicatorOnRight) {
                    SelectionIndicatorIcon(
                        selected = selected,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(2.dp),
                    )
                }
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ExplicitTitleText(
                        title = song.title,
                        isExplicit = song.isExplicit,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.labelLarge,
                    color = readableSecondaryTextColor(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Row(
                modifier = Modifier.width(if (selectionIndicatorOnRight) 72.dp else 40.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = formatDuration(song.durationMs),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                    modifier = Modifier.width(45.dp),
                    textAlign = TextAlign.End,
                )
                if (selectionIndicatorOnRight) {
                    Box(contentAlignment = Alignment.Center) {
                        SelectionIndicatorIcon(selected = selected)
                    }
                }
            }
        }
        if (showDivider) {
            DividerLine()
        }
    }
}

@Composable
internal fun SelectableAlbumPickerRow(
    album: Album,
    selected: Boolean,
    showDivider: Boolean = true,
    onOpen: () -> Unit,
    onToggleSelection: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onOpen,
                )
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ArtworkImage(
                uri = album.artUri,
                title = album.title,
                modifier = Modifier.size(62.dp),
                cornerRadius = ElovaireRadii.artworkSmall,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text(
                    text = album.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = album.artist,
                    style = MaterialTheme.typography.labelLarge,
                    color = readableSecondaryTextColor(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f),
                            ),
                        ) {
                            append(formatCountLabel(album.songCount, "track"))
                        }
                        append("  •  ")
                        withStyle(
                            SpanStyle(
                                color = readableSecondaryTextColor().copy(alpha = 0.7f),
                            ),
                        ) {
                            append(formatDuration(album.durationMs))
                        }
                    },
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onToggleSelection,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                SelectionIndicatorIcon(selected = selected)
            }
        }
        if (showDivider) {
            DividerLine()
        }
    }
}

@Composable
internal fun DividerLine(
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

@Composable
internal fun GroupedListRowContainer(
    index: Int,
    lastIndex: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val shape = when {
        lastIndex <= 0 -> RoundedCornerShape(ElovaireRadii.card)
        index == 0 -> RoundedCornerShape(
            topStart = ElovaireRadii.card,
            topEnd = ElovaireRadii.card,
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
        )
        index == lastIndex -> RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = ElovaireRadii.card,
            bottomEnd = ElovaireRadii.card,
        )
        else -> RoundedCornerShape(0.dp)
    }
    Surface(
        modifier = modifier,
        shape = shape,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Box(
            modifier = Modifier.padding(
                top = if (index == 0) 6.dp else 0.dp,
                bottom = if (index == lastIndex) 6.dp else 0.dp,
            ),
        ) {
            content()
        }
    }
}

@Composable
internal fun DetailListTopBar(
    title: String,
    subtitle: String?,
    onBack: () -> Unit,
    actions: List<TopBarActionSpec> = emptyList(),
    modifier: Modifier = Modifier,
) {
    val darkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val useSharedBackdrop = LocalUseSharedTopBarBackdrop.current
    if (useSharedBackdrop && !LocalRenderSharedTopBarContent.current) {
        RegisterSharedTopBar(
            SharedTopBarSpec.Detail(
                title = title,
                subtitle = subtitle,
                onBack = onBack,
                actions = actions,
            ),
        )
        return
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .zIndex(if (useSharedBackdrop) 8f else 0f)
            .background(Color.Transparent),
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {},
                ),
        )
        if (!useSharedBackdrop) {
            FrostedTopBarBackground(
                darkTheme = darkTheme,
                modifier = Modifier.matchParentSize(),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 14.dp, end = 14.dp, top = 3.dp, bottom = 13.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HeaderIconButton(
                iconResId = R.drawable.ic_lucide_chevron_left,
                contentDescription = "Back",
                showBackground = false,
                onClick = onBack,
                modifier = Modifier.zIndex(1f),
            )
            if (subtitle.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .zIndex(1f)
                        .weight(1f),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    AnimatedContent(
                        targetState = title,
                        transitionSpec = { ElovaireMotion.titleSwapTransform() },
                        label = "detailTopBarTitleOnly",
                    ) { currentTitle ->
                        Text(
                            text = currentTitle,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .zIndex(1f)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    AnimatedContent(
                        targetState = title,
                        transitionSpec = { ElovaireMotion.titleSwapTransform() },
                        label = "detailTopBarTitleWithSubtitle",
                    ) { currentTitle ->
                        Text(
                            text = currentTitle,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            if (actions.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    actions.forEach { action ->
                        HeaderIconButton(
                            iconResId = action.iconResId,
                            contentDescription = action.contentDescription,
                            showBackground = false,
                            onClick = action.onClick,
                            modifier = Modifier.zIndex(1f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun AddSongsToPlaylistDialog(
    availableSongs: List<Song>,
    existingSongIds: Set<Long>,
    onDismiss: () -> Unit,
    onAddSongs: (List<Long>) -> Unit,
) {
    val candidates = remember(availableSongs, existingSongIds) {
        availableSongs.filterNot { it.id in existingSongIds }.take(24)
    }
    val selectedSongIds = remember { mutableStateOf(setOf<Long>()) }
    val listState = rememberLazyListState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add songs") },
        text = {
            LazyColumn(
                state = listState,
                overscrollEffect = null,
                modifier = Modifier
                    .height(320.dp)
                    .ensureSingleItemRubberBand(listState),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(candidates, key = { it.id }) { song ->
                    Surface(
                        onClick = {
                            selectedSongIds.value = if (song.id in selectedSongIds.value) {
                                selectedSongIds.value - song.id
                            } else {
                                selectedSongIds.value + song.id
                            }
                        },
                        shape = RoundedCornerShape(ElovaireRadii.tile),
                        color = if (song.id in selectedSongIds.value) {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            ArtworkImage(
                                uri = song.artUri,
                                title = song.title,
                                modifier = Modifier.size(42.dp),
                                cornerRadius = ElovaireRadii.artworkSmall,
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = song.title,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                    maxLines = 1,
                                )
                                Text(
                                    text = song.artist,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onAddSongs(selectedSongIds.value.toList()) },
                enabled = selectedSongIds.value.isNotEmpty(),
            ) {
                Text(uiPhrase(LocalAppLanguage.current, UiPhrase.AddToPlaylist))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(uiPhrase(LocalAppLanguage.current, UiPhrase.Cancel))
            }
        },
    )
}
