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
import com.qamarq.jellymusic.data.update.AppReleaseInfo
import com.qamarq.jellymusic.data.update.AppUpdateUiState
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
import com.qamarq.jellymusic.ui.screens.home.HomeScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull


// Internal helper functions
private fun querySongParentDirectories(
    context: Context,
    songs: List<Song>,
): Set<String> {
    return querySongFilePaths(context, songs)
        .mapNotNull { path -> File(path).parentFile?.absolutePath }
        .toSet()
}

private fun querySongFilePaths(
    context: Context,
    songs: List<Song>,
): Set<String> {
    val contentResolver = context.contentResolver
    return songs.asSequence()
        .mapNotNull { song ->
            when (song.uri.scheme) {
                "file" -> song.uri.path
                else -> runCatching {
                    contentResolver.query(
                        song.uri,
                        arrayOf(MediaStore.MediaColumns.DATA),
                        null,
                        null,
                        null,
                    )?.use { cursor ->
                        if (!cursor.moveToFirst()) {
                            null
                        } else {
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
                        }
                    }
                }.getOrNull()
            }
        }
        .toSet()
}

private fun cleanupEmptyDirectories(paths: Set<String>) {
    paths.asSequence()
        .map(::File)
        .filter { file -> file.exists() && file.isDirectory }
        .sortedByDescending { file -> file.absolutePath.length }
        .forEach { directory ->
            runCatching {
                if (directory.listFiles().isNullOrEmpty()) {
                    directory.delete()
                }
            }
        }
}

internal fun Set<Long>.toggleSelection(id: Long): Set<Long> {
    return if (id in this) this - id else this + id
}

@OptIn(ExperimentalHazeApi::class)
@Composable
fun ElovaireRoot(
    container: AppContainer,
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    SyncElovaireMotionScale()
    val libraryContentState by container.mergedContentState.collectAsStateWithLifecycle()
    val jellyfinAlbums by container.jellyfinRepository.albums.collectAsStateWithLifecycle(emptyList())
    val jellyfinArtists by container.jellyfinRepository.artists.collectAsStateWithLifecycle(emptyList())
    val jellyfinHost by container.preferenceStore.jellyfinHost.collectAsStateWithLifecycle()
    val jellyfinToken by container.preferenceStore.jellyfinAccessToken.collectAsStateWithLifecycle()
    val isJellyfinConnected = remember(jellyfinHost, jellyfinToken) { jellyfinHost.isNotEmpty() && jellyfinToken.isNotEmpty() }
    val libraryScanState by container.libraryRepository.scanState.collectAsStateWithLifecycle()
    val playbackNowPlayingState by container.playbackManager.nowPlayingState.collectAsStateWithLifecycle()
    val playbackTransportState by container.playbackManager.transportState.collectAsStateWithLifecycle()
    val playbackQueueState by container.playbackManager.queueState.collectAsStateWithLifecycle()
    val playbackVolumeState by container.playbackManager.volumeState.collectAsStateWithLifecycle()
    val recentPlaybackState by container.playbackManager.recentPlaybackState.collectAsStateWithLifecycle()
    val eqSettings by container.preferenceStore.eqSettings.collectAsStateWithLifecycle()
    val themeMode by container.preferenceStore.themeMode.collectAsStateWithLifecycle()
    val textSizePreset by container.preferenceStore.textSizePreset.collectAsStateWithLifecycle()
    val appLanguage by container.preferenceStore.appLanguage.collectAsStateWithLifecycle()
    val playlists by container.preferenceStore.playlists.collectAsStateWithLifecycle()
    val hasCompletedOnboarding by container.preferenceStore.hasCompletedOnboarding.collectAsStateWithLifecycle()
    val favoriteSongIds by container.preferenceStore.favoriteSongIds.collectAsStateWithLifecycle()
    val favoriteSongIdSet = remember(favoriteSongIds) { favoriteSongIds.toHashSet() }
    val albumPlayCounts by container.preferenceStore.albumPlayCounts.collectAsStateWithLifecycle()
    val songPlayCounts by container.preferenceStore.songPlayCounts.collectAsStateWithLifecycle()
    val albumCollectionLayoutModeName by container.preferenceStore.albumCollectionLayoutMode.collectAsStateWithLifecycle()
    val songCollectionGridEnabled by container.preferenceStore.songCollectionGridEnabled.collectAsStateWithLifecycle()
    val albumCollectionSortModeName by container.preferenceStore.albumCollectionSortMode.collectAsStateWithLifecycle()
    val songCollectionSortModeName by container.preferenceStore.songCollectionSortMode.collectAsStateWithLifecycle()
    val appUpdateState by container.appUpdateManager.uiState.collectAsStateWithLifecycle()
    val albumCollectionLayoutMode = albumCollectionLayoutModeName.toAlbumLayoutMode()
    val changelogReleases = remember(context) { ChangelogRepository(context).loadReleases() }
    val rootScope = rememberCoroutineScope()
    val viewModelFactory = remember(container) { ElovaireViewModelFactory(container) }
    val searchViewModel: SearchViewModel = viewModel(factory = viewModelFactory)
    val nowPlayingViewModel: NowPlayingViewModel = viewModel(factory = viewModelFactory)
    val libraryState = remember(libraryContentState, libraryScanState) {
        libraryUiStateOf(libraryContentState, libraryScanState)
    }
    val playbackState = remember(
        playbackNowPlayingState,
        playbackTransportState,
        playbackQueueState,
        playbackVolumeState,
        recentPlaybackState,
    ) {
        playbackUiStateOf(
            nowPlaying = playbackNowPlayingState,
            transport = playbackTransportState,
            queue = playbackQueueState,
            volume = playbackVolumeState,
            recent = recentPlaybackState,
        )
    }
    var hasPermission by remember { mutableStateOf(hasAudioPermission(context)) }
    var hasNotificationPermission by remember { mutableStateOf(hasNotificationPermission(context)) }
    var hasNearbyWifiDevicesPermission by remember { mutableStateOf(hasNearbyWifiDevicesPermission(context)) }
    var hasLocalNetworkPermission by remember { mutableStateOf(hasLocalNetworkPermission(context)) }
    var pendingJellyfinSetupAfterOnboarding by rememberSaveable { mutableStateOf(false) }
    var firstLaunchPermissionExperienceActive by rememberSaveable {
        mutableStateOf(!hasPermission)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    var playFirstLaunchHomeReveal by rememberSaveable {
        mutableStateOf(false)
    }
    var pendingSongDeletion by remember { mutableStateOf<PendingSongDeletion?>(null) }
    val songsById = remember(libraryState.songs) { libraryState.songs.associateBy { it.id } }
    val songsByAlbumId = remember(libraryState.songs) { libraryState.songs.groupBy { it.albumId } }
    val albumsById = remember(libraryState.albums) { libraryState.albums.associateBy { it.id } }

    val recentlyAddedAlbums = remember(libraryState.albums) {
        recentlyAddedAlbumsFor(libraryState)
    }
    val recentAlbums = remember(libraryState.albums, playbackState.recentAlbumIds) {
        recentAlbumsFor(libraryState, playbackState)
    }
    val recentlyPlayedJellyfinAlbums = remember(jellyfinAlbums, playbackState.recentAlbumIds) {
        val jellyfinAlbumsById = jellyfinAlbums.associateBy { it.id }
        playbackState.recentAlbumIds.mapNotNull(jellyfinAlbumsById::get).take(6)
    }
    val topPlayedSongs = remember(songsById, songPlayCounts) {
        songsById.values
            .filter { (songPlayCounts[it.id] ?: 0) > 0 }
            .sortedByDescending { songPlayCounts[it.id] ?: 0 }
            .take(10)
    }
    val playlistsById = remember(playlists) { playlists.associateBy { it.id } }
    val recentlyPlayedPlaylists = remember(playlistsById, playbackState.recentPlaylistIds) {
        playbackState.recentPlaylistIds.mapNotNull(playlistsById::get).take(8)
    }
    val lastPlayedPlaylist = remember(
        playlistsById,
        playbackState.lastPlayedCollectionKind,
        playbackState.lastPlayedCollectionId,
    ) {
        if (playbackState.lastPlayedCollectionKind == PlaybackCollectionKind.Playlist) {
            playbackState.lastPlayedCollectionId?.let(playlistsById::get)
        } else {
            null
        }
    }
    val lastPlayedAlbum = remember(
        albumsById,
        recentAlbums,
        playbackState.lastPlayedCollectionKind,
        playbackState.lastPlayedCollectionId,
    ) {
        when (playbackState.lastPlayedCollectionKind) {
            PlaybackCollectionKind.Album -> playbackState.lastPlayedCollectionId?.let(albumsById::get)
            PlaybackCollectionKind.Playlist -> null
            null -> recentAlbums.firstOrNull()
        } ?: recentAlbums.firstOrNull()
    }
    val favoriteAlbums = remember(libraryState.albums, songPlayCounts, recentAlbums, recentlyAddedAlbums) {
        favoriteAlbumsFor(
            libraryState = libraryState,
            songPlayCounts = songPlayCounts,
            recentAlbums = recentAlbums,
            recentlyAddedAlbums = recentlyAddedAlbums,
        )
    }

    val nearbyWifiDevicesPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        hasNearbyWifiDevicesPermission = granted
    }
    val localNetworkPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        hasLocalNetworkPermission = granted
    }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        hasNotificationPermission = granted
        container.setNotificationsEnabled(granted)
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        hasPermission = granted
        container.libraryRepository.onPermissionChanged(granted)
    }
    suspend fun completeSongDeletion(
        songs: List<Song>,
        parentDirectories: Set<String>,
    ) {
        songs.forEach { song ->
            container.preferenceStore.removeSongReferences(song.id)
        }
        withContext(Dispatchers.IO) {
            cleanupEmptyDirectories(parentDirectories)
        }
        container.libraryRepository.refresh(
            forceMediaIndex = true,
            showLoadingIndicator = false,
        )
    }
    val deleteSongLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
    ) { result ->
        val pendingDeletion = pendingSongDeletion ?: return@rememberLauncherForActivityResult
        pendingSongDeletion = null
        if (result.resultCode == Activity.RESULT_OK) {
            rootScope.launch {
                completeSongDeletion(
                    songs = pendingDeletion.songs,
                    parentDirectories = pendingDeletion.parentDirectories,
                )
            }
        }
    }

    LaunchedEffect(hasPermission) {
        container.libraryRepository.onPermissionChanged(hasPermission)
    }

    LaunchedEffect(hasNotificationPermission) {
        container.setNotificationsEnabled(hasNotificationPermission)
    }

    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val refreshedAudioPermission = hasAudioPermission(context)
                val refreshedNotificationPermission = hasNotificationPermission(context)
                val refreshedNearbyWifiDevicesPermission = hasNearbyWifiDevicesPermission(context)
                val refreshedLocalNetworkPermission = hasLocalNetworkPermission(context)
                if (hasPermission != refreshedAudioPermission) {
                    hasPermission = refreshedAudioPermission
                    container.libraryRepository.onPermissionChanged(refreshedAudioPermission)
                }
                if (hasNotificationPermission != refreshedNotificationPermission) {
                    hasNotificationPermission = refreshedNotificationPermission
                    container.setNotificationsEnabled(refreshedNotificationPermission)
                }
                if (hasNearbyWifiDevicesPermission != refreshedNearbyWifiDevicesPermission) {
                    hasNearbyWifiDevicesPermission = refreshedNearbyWifiDevicesPermission
                }
                if (hasLocalNetworkPermission != refreshedLocalNetworkPermission) {
                    hasLocalNetworkPermission = refreshedLocalNetworkPermission
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val showFirstLaunchPermissionOverlay =
        firstLaunchPermissionExperienceActive &&
            (
                !hasPermission ||
                    libraryState.isLoading ||
                    (
                        libraryState.songs.isEmpty() &&
                            libraryState.albums.isEmpty() &&
                            libraryState.errorMessage == null &&
                            !playFirstLaunchHomeReveal
                        )
                )

    LaunchedEffect(
        firstLaunchPermissionExperienceActive,
        hasPermission,
        libraryState.isLoading,
        libraryState.songs.size,
        libraryState.albums.size,
        libraryState.errorMessage,
    ) {
        if (
            firstLaunchPermissionExperienceActive &&
            hasPermission &&
            !libraryState.isLoading &&
            (libraryState.songs.isNotEmpty() || libraryState.albums.isNotEmpty() || libraryState.errorMessage != null)
        ) {
            playFirstLaunchHomeReveal = true
        }
    }

    if (!hasCompletedOnboarding) {
        val onboardingSteps = remember(
            hasPermission,
            hasNotificationPermission,
            hasNearbyWifiDevicesPermission,
            hasLocalNetworkPermission,
        ) {
            buildList {
                if (!hasPermission) {
                    add(
                        OnboardingStep(
                            iconResId = R.drawable.ic_lucide_music,
                            title = "Audio library access",
                            description = "JellyMusic needs access to the music files on your device so it can play them.",
                            primaryLabel = "Allow access",
                            skippable = false,
                            onPrimaryAction = { permissionLauncher.launch(audioPermission()) },
                        ),
                    )
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                    add(
                        OnboardingStep(
                            iconResId = R.drawable.ic_lucide_bell,
                            title = "Notifications",
                            description = "We show a notification for the currently playing track, so you can control playback from the notification shade and lock screen.",
                            primaryLabel = "Allow",
                            onPrimaryAction = {
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            },
                        ),
                    )
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNearbyWifiDevicesPermission) {
                    add(
                        OnboardingStep(
                            iconResId = R.drawable.ic_lucide_wifi,
                            title = "Nearby devices",
                            description = "Needed to discover Chromecasts, TVs, and speakers on your Wi-Fi network so you can stream music to them.",
                            primaryLabel = "Allow",
                            onPrimaryAction = {
                                nearbyWifiDevicesPermissionLauncher.launch(Manifest.permission.NEARBY_WIFI_DEVICES)
                            },
                        ),
                    )
                }
                if (Build.VERSION.SDK_INT >= 37 && !hasLocalNetworkPermission) {
                    add(
                        OnboardingStep(
                            iconResId = R.drawable.ic_lucide_speaker,
                            title = "Local network",
                            description = "Starting with Android 17, this permission is required for the app to talk to devices on your home network, e.g. when casting to a TV.",
                            primaryLabel = "Allow",
                            onPrimaryAction = {
                                localNetworkPermissionLauncher.launch(Manifest.permission.ACCESS_LOCAL_NETWORK)
                            },
                        ),
                    )
                }
                add(
                    OnboardingStep(
                        iconResId = R.drawable.ic_lucide_server,
                        title = "Connect to Jellyfin",
                        description = "Optional, but recommended: connect to your Jellyfin server to access your whole library in one place.",
                        primaryLabel = "Connect",
                        onPrimaryAction = { pendingJellyfinSetupAfterOnboarding = true },
                    ),
                )
            }
        }
        OnboardingCarousel(
            steps = onboardingSteps,
            onFinished = { container.preferenceStore.setHasCompletedOnboarding(true) },
        )
        return
    }

    LaunchedEffect(pendingJellyfinSetupAfterOnboarding) {
        if (pendingJellyfinSetupAfterOnboarding) {
            navController.navigate(JELLYFIN_SETUP_ROUTE)
            pendingJellyfinSetupAfterOnboarding = false
        }
    }

    if (!hasPermission) {
        FirstLaunchPermissionLoadingScreen(
            showLoading = true,
            onRequestPermission = { permissionLauncher.launch(audioPermission()) },
        )
        return
    }

    val isPlaybackActuallyPlaying = playbackState.isPlaying && playbackState.currentSong != null

    val topLevelDestinations = DefaultTopLevelDestinations

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val currentConcreteRoute = currentBackStackEntry?.concreteNavigationRoute() ?: currentRoute
    val currentAlbumRouteId = currentBackStackEntry?.arguments?.let { arguments ->
        val idObj = arguments.get("albumId")
        when (idObj) {
            is Long -> idObj
            is String -> idObj.toLongOrNull()
            else -> null
        }
    }
    var detailExpandOrigin by remember { mutableStateOf(ExpandOrigin()) }
    var detailRouteTransitionMode by remember { mutableStateOf(DetailRouteTransitionMode.TileExpand) }
    var nowPlayingTransitionSnapshot by remember { mutableStateOf<NowPlayingTransitionSnapshot?>(null) }
    var isPlayerOverlayVisible by rememberSaveable { mutableStateOf(false) }
    var playerTransitionProgress by remember { mutableFloatStateOf(0f) }
    var playerTransitionState by remember { mutableStateOf(PlayerOverlayTransitionState.Compact) }
    var lastPlayerOpenRequestAt by remember { mutableLongStateOf(0L) }
    var isSearchQueryActive by rememberSaveable { mutableStateOf(false) }
    var browsingOriginRoute by rememberSaveable { mutableStateOf(HOME_ROUTE) }
    var selectedBottomRoute by rememberSaveable { mutableStateOf(HOME_ROUTE) }
    var lastHomeTabRoute by rememberSaveable { mutableStateOf(HOME_ROUTE) }
    var lastLibraryTabRoute by rememberSaveable { mutableStateOf(ALBUMS_ROUTE) }
    var lastPlaylistsTabRoute by rememberSaveable { mutableStateOf(PLAYLISTS_ROUTE) }
    var lastSearchTabRoute by rememberSaveable { mutableStateOf(SEARCH_ROUTE) }
    val routeOwnerOverrides = remember { mutableStateMapOf<String, String>() }
    var searchFieldFocused by rememberSaveable { mutableStateOf(false) }
    var homeScrollRequestVersion by rememberSaveable { mutableLongStateOf(0L) }
    var libraryScrollRequestVersion by rememberSaveable { mutableLongStateOf(0L) }
    var playlistsScrollRequestVersion by rememberSaveable { mutableLongStateOf(0L) }
    var searchScrollRequestVersion by rememberSaveable { mutableLongStateOf(0L) }
    val showTopLevelChrome = currentRoute in TopLevelRoutes
    val showBottomNavigation = currentRoute in BottomNavigationRoutes
    LaunchedEffect(currentRoute) {
        if (currentRoute in TopLevelRoutes) {
            browsingOriginRoute = currentRoute.orEmpty()
            selectedBottomRoute = currentRoute.orEmpty()
        }
    }
    LaunchedEffect(currentBackStackEntry, browsingOriginRoute, currentConcreteRoute) {
        val concreteRoute = currentBackStackEntry?.elovaireConcreteRoute() ?: return@LaunchedEffect
        val normalizedConcreteRoute = concreteRoute.normalizedNavigationRoute()
        val ownerRoute = when (concreteRoute.normalizedNavigationRoute()) {
            HOME_ROUTE -> HOME_ROUTE
            SEARCH_ROUTE -> SEARCH_ROUTE
            ALBUMS_ROUTE -> ALBUMS_ROUTE
            PLAYLISTS_ROUTE -> PLAYLISTS_ROUTE
            "$LIBRARY_COLLECTION_ROUTE/{kind}",
            "$GENRE_ROUTE/{genre}",
            "$ARTIST_ROUTE/{artistName}",
            -> ALBUMS_ROUTE

            "$PLAYLIST_ROUTE/{playlistId}" -> PLAYLISTS_ROUTE
            "$ALBUM_ROUTE/{albumId}" -> {
                routeOwnerOverrides[concreteRoute]
                    ?: navController.previousBackStackEntry?.concreteNavigationRoute()?.let(routeOwnerOverrides::get)
                    ?: topLevelOwnerRoute(
                        navController.previousBackStackEntry?.destination?.route,
                        browsingOriginRoute,
                    )
                    ?: browsingOriginRoute.takeIf { it in TopLevelRoutes }
                    ?: selectedBottomRoute
            }

            else -> topLevelOwnerRoute(currentRoute, browsingOriginRoute) ?: selectedBottomRoute
        }
        if (ownerRoute in TopLevelRoutes) {
            routeOwnerOverrides[concreteRoute] = ownerRoute
        }
        if (concreteRoute in setOf(PLAYER_ROUTE, SETTINGS_ROUTE, EQUALIZER_ROUTE, CHANGELOG_ROUTE, ABOUT_ROUTE)) {
            return@LaunchedEffect
        }
        if (normalizedConcreteRoute == "$ALBUM_TAG_EDITOR_ROUTE/{albumId}") {
            return@LaunchedEffect
        }
        if (normalizedConcreteRoute in setOf("$ALBUM_ROUTE/{albumId}", "$PLAYLIST_ROUTE/{playlistId}")) {
            return@LaunchedEffect
        }
        when (ownerRoute) {
            HOME_ROUTE -> lastHomeTabRoute = concreteRoute
            ALBUMS_ROUTE -> lastLibraryTabRoute = concreteRoute
            PLAYLISTS_ROUTE -> lastPlaylistsTabRoute = concreteRoute
            SEARCH_ROUTE -> lastSearchTabRoute = concreteRoute
        }
    }
    val activeBottomRoute = routeOwnerOverrides[currentConcreteRoute]
        ?: topLevelOwnerRoute(currentRoute, browsingOriginRoute)
        ?: selectedBottomRoute
    val resetTopLevelTabState: (String) -> Unit = { route ->
        clearTopLevelScrollPositionMemory(route)
        when (route) {
            HOME_ROUTE -> {
                lastHomeTabRoute = HOME_ROUTE
                homeScrollRequestVersion += 1L
            }
            ALBUMS_ROUTE -> {
                lastLibraryTabRoute = ALBUMS_ROUTE
                libraryScrollRequestVersion += 1L
            }
            PLAYLISTS_ROUTE -> {
                lastPlaylistsTabRoute = PLAYLISTS_ROUTE
                playlistsScrollRequestVersion += 1L
            }
            SEARCH_ROUTE -> {
                lastSearchTabRoute = SEARCH_ROUTE
                searchScrollRequestVersion += 1L
            }
        }
    }
    val keyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val hideCompactNowPlayingRoutes = setOf(
        CHANGELOG_ROUTE,
        "$ALBUM_TAG_EDITOR_ROUTE/{albumId}",
    )
    val hideCompactNowPlaying = (keyboardVisible && currentRoute == PLAYLISTS_ROUTE) ||
        (currentRoute == SEARCH_ROUTE && isSearchQueryActive) ||
        currentRoute in hideCompactNowPlayingRoutes
    val reserveCompactNowPlayingSpace = playbackState.currentSong != null && !hideCompactNowPlaying
    val canHostCompactNowPlaying = playbackState.currentSong != null
    val showGlobalNowPlaying = canHostCompactNowPlaying && !hideCompactNowPlaying && (
        !isPlayerOverlayVisible || playerTransitionProgress < 0.99f
        )
    val reenteringFromPlayer = false
    val overscrollFactory = rememberElovaireOverscrollFactory()
    val navHostBlur = 0.dp
    val navHostScrimAlpha = 0f
    val rootView = LocalView.current
    val appBackground = MaterialTheme.colorScheme.background
    val darkTheme = appBackground.luminance() < 0.5f
    val chromeHazeState = rememberHazeState()
    val sharedTopBarController = remember { SharedTopBarController() }
    val sharedBackIconPainter = painterResource(id = R.drawable.ic_lucide_chevron_left)
    val sharedTopMenuIconPainter = painterResource(id = R.drawable.ic_lucide_menu)
    var showTopBarMenu by rememberSaveable { mutableStateOf(false) }
    var showChangelogSheet by rememberSaveable { mutableStateOf(false) }
    var showPlaylistCreateDialog by rememberSaveable { mutableStateOf(false) }
    val playerArtworkGradient = rememberArtworkGradient(playbackState.currentSong?.artUri).value
    val playerAdaptivePalette = remember(
        playbackState.currentSong?.id,
        playerArtworkGradient,
        darkTheme,
        appBackground,
    ) {
        buildPlayerAdaptivePalette(
            gradient = playerArtworkGradient,
            appBackground = appBackground,
            darkTheme = darkTheme,
        )
    }
    val openPlayerIfAllowed: (NowPlayingTransitionSnapshot?) -> Unit = { snapshot ->
        val now = System.currentTimeMillis()
        if (now - lastPlayerOpenRequestAt > 450L) {
            lastPlayerOpenRequestAt = now
            nowPlayingTransitionSnapshot = snapshot
            isPlayerOverlayVisible = true
        }
    }
    val openCurrentPlayingAlbum: (Long) -> Unit = { albumId ->
        val sameAlbumAlreadyVisible =
            currentRoute == "$ALBUM_ROUTE/{albumId}" && currentAlbumRouteId == albumId
        isPlayerOverlayVisible = false
        if (!sameAlbumAlreadyVisible) {
            navController.navigate("$ALBUM_ROUTE/$albumId")
        }
    }
    val openSettingsFromMenu = remember(navController) {
        {
            showTopBarMenu = false
            navController.navigate(SETTINGS_ROUTE)
        }
    }
    val openEqualizerFromMenu = remember(navController) {
        {
            showTopBarMenu = false
            navController.navigate(EQUALIZER_ROUTE)
        }
    }
    val openChangelogSheetFromMenu = remember {
        {
            showTopBarMenu = false
            showChangelogSheet = true
        }
    }
    val openAboutFromMenu = remember(navController) {
        {
            showTopBarMenu = false
            navController.navigate(ABOUT_ROUTE)
        }
    }
    val showPlaylistCreateAction = currentRoute == PLAYLISTS_ROUTE && playlists.isNotEmpty()
    val sharedTopBarSpec = sharedTopBarController.registration?.spec
        ?: if (showTopLevelChrome) {
            SharedTopBarSpec.Unified(
                title = topBarTitle(currentRoute, appLanguage),
                showSettings = currentRoute in setOf(HOME_ROUTE, ALBUMS_ROUTE, PLAYLISTS_ROUTE),
                supplementalActionIconResId = if (showPlaylistCreateAction) R.drawable.ic_lucide_plus else null,
                supplementalActionContentDescription = if (showPlaylistCreateAction) "Create playlist" else null,
                onSupplementalAction = if (showPlaylistCreateAction) {
                    { showPlaylistCreateDialog = true }
                } else {
                    null
                },
                onOpenMenu = { showTopBarMenu = true },
            )
        } else {
            null
        }
    LaunchedEffect(container) {
        container.openPlayerRequests.collect {
            openPlayerIfAllowed(null)
        }
    }
    LaunchedEffect(isPlayerOverlayVisible) {
        if (!isPlayerOverlayVisible) {
            nowPlayingTransitionSnapshot = null
        }
    }
    LaunchedEffect(currentRoute) {
        showTopBarMenu = false
        if (currentRoute != PLAYLISTS_ROUTE) {
            showPlaylistCreateDialog = false
        }
    }
    SideEffect {
        val window = (rootView.context as? Activity)?.window ?: return@SideEffect
        val controller = WindowCompat.getInsetsController(window, rootView)
        val usesLightSystemBarIcons = if (isPlayerOverlayVisible) {
            playerAdaptivePalette.contentColor.luminance() < 0.56f
        } else {
            !darkTheme
        }
        controller.isAppearanceLightStatusBars = usesLightSystemBarIcons
        controller.isAppearanceLightNavigationBars = usesLightSystemBarIcons
    }

    val deleteSongsFromDevice = remember(context, rootScope, deleteSongLauncher) {
        { songs: List<Song> ->
            val uniqueSongs = songs.distinctBy { it.id }
            if (uniqueSongs.isNotEmpty()) {
                rootScope.launch {
                    val parentDirectories = querySongParentDirectories(context, uniqueSongs)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        pendingSongDeletion = PendingSongDeletion(
                            songs = uniqueSongs,
                            parentDirectories = parentDirectories,
                        )
                        deleteSongLauncher.launch(
                            IntentSenderRequest.Builder(
                                MediaStore.createDeleteRequest(
                                    context.contentResolver,
                                    uniqueSongs.map(Song::uri),
                                ).intentSender,
                            ).build(),
                        )
                    } else {
                        runCatching {
                            withContext(Dispatchers.IO) {
                                uniqueSongs.forEach { song ->
                                    context.contentResolver.delete(song.uri, null, null)
                                }
                            }
                        }.onSuccess {
                            completeSongDeletion(
                                songs = uniqueSongs,
                                parentDirectories = parentDirectories,
                            )
                        }.onFailure { throwable ->
                            val intentSender = when {
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && throwable is RecoverableSecurityException -> {
                                    throwable.userAction.actionIntent.intentSender
                                }
                                else -> null
                            }
                            if (intentSender != null) {
                                pendingSongDeletion = PendingSongDeletion(
                                    songs = uniqueSongs,
                                    parentDirectories = parentDirectories,
                                )
                                deleteSongLauncher.launch(
                                    IntentSenderRequest.Builder(intentSender).build(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    val songMenuActions = remember(playlists, songsById, deleteSongsFromDevice) {
        SongMenuActions(
            playlists = playlists.filterNot { it.isSystem },
            songsById = songsById,
            onAddToPlaylist = { playlistId, song ->
                container.preferenceStore.addSongsToPlaylist(playlistId, listOf(song.id))
            },
            onCreatePlaylist = container.preferenceStore::createPlaylist,
            onAddToQueue = { song ->
                container.playbackManager.enqueueSong(song)
            },
            onDeleteFromLibrary = { song ->
                deleteSongsFromDevice(listOf(song))
            },
        )
    }
    val createPlaylistAndAddSongs = remember(container.preferenceStore) {
        { name: String, songIds: List<Long> ->
            val createdId = container.preferenceStore.createPlaylist(name)
            if (songIds.isNotEmpty()) {
                container.preferenceStore.addSongsToPlaylist(createdId, songIds)
            }
            createdId
        }
    }
    val deleteAlbumFromDevice = remember(deleteSongsFromDevice) {
        { album: Album ->
            deleteSongsFromDevice(album.songs)
        }
    }

    val castPickerController = remember { CastPickerController() }
    CompositionLocalProvider(
        LocalOverscrollFactory provides overscrollFactory,
        LocalSongMenuActions provides songMenuActions,
        LocalChromeHazeState provides chromeHazeState,
        LocalSharedBackIconPainter provides sharedBackIconPainter,
        LocalSharedTopMenuIconPainter provides sharedTopMenuIconPainter,
        LocalAppLanguage provides appLanguage,
        LocalCastPickerController provides castPickerController,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds(),
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.background,
            ) { innerPadding ->
            val topBarHeight = topBarOccupiedHeight()
            val detailTopBarHeight = detailTopBarOccupiedHeight()
            val sharedTopBarHeight = sharedTopBarOccupiedHeight()
            val bottomNavHeight = if (showBottomNavigation) bottomNavigationOccupiedHeight() else 0.dp
            val showSharedTopBarBackdrop = currentRoute != null && currentRoute != PLAYER_ROUTE
            val topContentPadding = if (showTopLevelChrome) {
                topBarHeight + ElovaireSpacing.topBarToFirstContentGap
            } else {
                innerPadding.calculateTopPadding()
            }
            val bottomContentPadding =
                bottomNavHeight +
                    (if (reserveCompactNowPlayingSpace) ElovaireSpacing.miniPlayerReservedHeight else 0.dp) +
                    ElovaireSpacing.scrollTailPadding
            val detailBottomPadding =
                bottomNavHeight +
                    (if (reserveCompactNowPlayingSpace) ElovaireSpacing.miniPlayerReservedHeight else 0.dp) +
                    ElovaireSpacing.scrollTailPadding

            Box(modifier = Modifier.fillMaxSize()) {
                CompositionLocalProvider(
                    LocalUseSharedTopBarBackdrop provides showSharedTopBarBackdrop,
                    LocalSharedTopBarController provides sharedTopBarController,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .hazeSource(chromeHazeState),
                    ) {
                    NavHost(
                        navController = navController,
                        startDestination = HOME_ROUTE,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(navHostBlur),
                        enterTransition = {
                            resolveForwardEnterTransition(
                                transition = ElovaireNavigationTransitions.resolveNavHostTransition(
                                    initialRoute = initialState.destination.route,
                                    targetRoute = targetState.destination.route,
                                    initialFallbackTopLevelRoute = browsingOriginRoute,
                                    targetFallbackTopLevelRoute = selectedBottomRoute,
                                    detailRouteTransitionMode = detailRouteTransitionMode,
                                ),
                                expandOrigin = detailExpandOrigin,
                            )
                        },
                        exitTransition = {
                            resolveForwardExitTransition(
                                transition = ElovaireNavigationTransitions.resolveNavHostTransition(
                                    initialRoute = initialState.destination.route,
                                    targetRoute = targetState.destination.route,
                                    initialFallbackTopLevelRoute = browsingOriginRoute,
                                    targetFallbackTopLevelRoute = selectedBottomRoute,
                                    detailRouteTransitionMode = detailRouteTransitionMode,
                                ),
                            )
                        },
                        popEnterTransition = {
                            resolvePopEnterTransition(
                                transition = ElovaireNavigationTransitions.resolveNavHostTransition(
                                    initialRoute = initialState.destination.route,
                                    targetRoute = targetState.destination.route,
                                    initialFallbackTopLevelRoute = browsingOriginRoute,
                                    targetFallbackTopLevelRoute = selectedBottomRoute,
                                    detailRouteTransitionMode = detailRouteTransitionMode,
                                ),
                            )
                        },
                        popExitTransition = {
                            resolvePopExitTransition(
                                transition = ElovaireNavigationTransitions.resolveNavHostTransition(
                                    initialRoute = initialState.destination.route,
                                    targetRoute = targetState.destination.route,
                                    initialFallbackTopLevelRoute = browsingOriginRoute,
                                    targetFallbackTopLevelRoute = selectedBottomRoute,
                                    detailRouteTransitionMode = detailRouteTransitionMode,
                                ),
                                expandOrigin = detailExpandOrigin,
                            )
                        },
                ) {
                    composable(HOME_ROUTE) {
                        val recentSongs = remember(songsById, playbackState.recentSongIds) {
                            playbackState.recentSongIds.mapNotNull(songsById::get).take(5)
                        }
                        HomeScreen(
                            lastPlayedAlbum = lastPlayedAlbum,
                            lastPlayedPlaylist = lastPlayedPlaylist,
                            songsById = songsById,
                            recentlyAddedAlbums = recentlyAddedAlbums,
                            recentSongs = recentSongs,
                            topPlayedSongs = topPlayedSongs,
                            recentlyPlayedPlaylists = recentlyPlayedPlaylists,
                            favoriteAlbums = favoriteAlbums,
                            jellyfinAlbums = jellyfinAlbums,
                            recentlyPlayedJellyfinAlbums = recentlyPlayedJellyfinAlbums,
                            jellyfinArtists = jellyfinArtists,
                            isJellyfinConnected = isJellyfinConnected,
                            onConnectJellyfin = { navController.navigate(JELLYFIN_SETUP_ROUTE) },
                            playbackState = playbackState,
                            isLibraryLoading = libraryState.isLoading,
                            libraryScanProgress = libraryState.scanProgress,
                            favoriteSongIds = favoriteSongIdSet,
                            topPadding = topContentPadding,
                            bottomPadding = bottomContentPadding,
                            scrollToTopRequestVersion = homeScrollRequestVersion,
                            playInitialReveal = playFirstLaunchHomeReveal,
                            onInitialRevealFinished = {
                                playFirstLaunchHomeReveal = false
                                firstLaunchPermissionExperienceActive = false
                            },
                            onAlbumSelected = { album, origin ->
                                detailExpandOrigin = origin
                                detailRouteTransitionMode = DetailRouteTransitionMode.TileExpand
                                navController.navigate("$ALBUM_ROUTE/${album.id}")
                            },
                            onPlaylistSelected = { playlist ->
                                detailExpandOrigin = ExpandOrigin()
                                detailRouteTransitionMode = DetailRouteTransitionMode.Standard
                                navController.navigate("$PLAYLIST_ROUTE/${playlist.id}")
                            },
                            onPlayAlbum = { album ->
                                container.playbackManager.playAlbum(album)
                            },
                            onPlayPlaylist = { playlist, songs ->
                                songs.firstOrNull()?.let { firstSong ->
                                    container.playbackManager.playSong(
                                        song = firstSong,
                                        collection = songs,
                                        sourceLabel = playlist.name,
                                        sourcePlaylistId = playlist.id,
                                    )
                                    openPlayerIfAllowed(null)
                                }
                            },
                            onSongSelected = { song ->
                                val sourceAlbum = albumsById[song.albumId]
                                if (sourceAlbum != null) {
                                    container.playbackManager.playAlbum(
                                        album = sourceAlbum,
                                        startSongId = song.id,
                                        sourceLabel = sourceAlbum.title,
                                    )
                                } else {
                                    val albumSongs = songsByAlbumId[song.albumId].orEmpty()
                                    container.playbackManager.playSong(
                                        song = song,
                                        collection = albumSongs.ifEmpty { listOf(song) },
                                        sourceLabel = song.album,
                                    )
                                }
                                openPlayerIfAllowed(null)
                            },
                            onPlayTopSong = { song ->
                                container.playbackManager.playSong(
                                    song = song,
                                    collection = topPlayedSongs,
                                    sourceLabel = "Most Played",
                                )
                                openPlayerIfAllowed(null)
                            },
                            onToggleFavorite = { songId ->
                                container.preferenceStore.toggleFavoriteSong(songId)
                            },
                            onArtistSelected = { artistName ->
                                navController.navigate("$ARTIST_ROUTE/${Uri.encode(artistName)}")
                            },
                        )
                    }

                    composable(ALBUMS_ROUTE) {
                        LibraryHubScreen(
                            libraryState = libraryState,
                            topPadding = topContentPadding,
                            bottomPadding = bottomContentPadding,
                            scrollToTopRequestVersion = libraryScrollRequestVersion,
                            onOpenCollection = { kind ->
                                navController.navigate("$LIBRARY_COLLECTION_ROUTE/${kind.name}")
                            },
                            onOpenJellyfin = {
                                navController.navigate(JELLYFIN_LIBRARY_ROUTE)
                            },
                            onAlbumSelected = { album, origin ->
                                detailExpandOrigin = origin
                                detailRouteTransitionMode = DetailRouteTransitionMode.TileExpand
                                navController.navigate("$ALBUM_ROUTE/${album.id}")
                            },
                        )
                    }

                    composable(PLAYLISTS_ROUTE) {
                        PlaylistsScreen(
                            playlists = playlists,
                            libraryState = libraryState,
                            topPadding = topContentPadding,
                            bottomPadding = bottomContentPadding,
                            scrollToTopRequestVersion = playlistsScrollRequestVersion,
                            onRequestCreatePlaylist = { showPlaylistCreateDialog = true },
                            onRenamePlaylist = container.preferenceStore::renamePlaylist,
                            onDeletePlaylists = container.preferenceStore::deletePlaylists,
                            onOpenPlaylist = { playlist, origin ->
                                detailExpandOrigin = origin
                                detailRouteTransitionMode = DetailRouteTransitionMode.Standard
                                navController.navigate("$PLAYLIST_ROUTE/${playlist.id}")
                            },
                        )
                    }

                    composable(SEARCH_ROUTE) {
                        SearchRoute(
                            viewModel = searchViewModel,
                            libraryState = libraryState,
                            favoriteSongIds = favoriteSongIdSet,
                            topPadding = topContentPadding,
                            bottomPadding = bottomContentPadding,
                            scrollToTopRequestVersion = searchScrollRequestVersion,
                            isSearchFieldFocused = searchFieldFocused,
                            onSearchFieldFocusedChange = { searchFieldFocused = it },
                            onSearchQueryActiveChanged = { isSearchQueryActive = it },
                            onPlaySong = { song, queue ->
                                container.playbackManager.playSong(
                                    song = song,
                                    collection = queue,
                                    sourceLabel = searchViewModel.playbackSourceLabelFor(queue, song.album),
                                )
                                openPlayerIfAllowed(null)
                            },
                            onAlbumSelected = { album, origin ->
                                detailExpandOrigin = origin
                                detailRouteTransitionMode = DetailRouteTransitionMode.TileExpand
                                navController.navigate("$ALBUM_ROUTE/${album.id}")
                            },
                            onArtistSelected = { artistName ->
                                navController.navigate("$ARTIST_ROUTE/${Uri.encode(artistName)}")
                            },
                            onToggleFavorite = container.preferenceStore::toggleFavoriteSong,
                        )
                    }

                    composable(
                        route = "$PLAYLIST_ROUTE/{playlistId}",
                        arguments = listOf(navArgument("playlistId") { type = NavType.LongType }),
                    ) { backStackEntry ->
                        val playlistId = backStackEntry.arguments?.getLong("playlistId") ?: 0L
                        val playlist = playlists.firstOrNull { it.id == playlistId }
                        PlaylistDetailScreen(
                            playlist = playlist,
                            librarySongs = libraryState.songs,
                            favoriteSongIds = favoriteSongIdSet,
                            currentSongId = playbackState.currentSong?.id,
                            isCurrentSongPlaying = isPlaybackActuallyPlaying,
                            bottomPadding = detailBottomPadding,
                            onBack = navController::navigateUp,
                            onPlayPlaylist = { songs, sourceLabel ->
                                songs.firstOrNull()?.let { firstSong ->
                                    container.playbackManager.playSong(
                                        song = firstSong,
                                        collection = songs,
                                        sourceLabel = sourceLabel,
                                        sourcePlaylistId = playlist?.id,
                                    )
                                    openPlayerIfAllowed(null)
                                }
                            },
                            onShufflePlaylist = { songs, sourceLabel ->
                                songs.randomOrNull()?.let { firstSong ->
                                    container.playbackManager.playSong(
                                        song = firstSong,
                                        collection = songs,
                                        sourceLabel = sourceLabel,
                                        shuffleEnabled = true,
                                        sourcePlaylistId = playlist?.id,
                                    )
                                    openPlayerIfAllowed(null)
                                }
                            },
                            onSongSelected = { song, queue ->
                                container.playbackManager.playSong(
                                    song = song,
                                    collection = queue,
                                    sourceLabel = playlist?.name ?: queue.playbackSourceLabel(fallbackAlbum = song.album),
                                    sourcePlaylistId = playlist?.id,
                                )
                                openPlayerIfAllowed(null)
                            },
                            onAddSongs = { songIds ->
                                container.preferenceStore.addSongsToPlaylist(playlistId, songIds)
                            },
                            onUpdateSongOrder = { songIds ->
                                container.preferenceStore.updatePlaylistSongIds(playlistId, songIds)
                            },
                            onRenamePlaylist = container.preferenceStore::renamePlaylist,
                            onDeletePlaylist = { targetPlaylistId ->
                                container.preferenceStore.deletePlaylists(setOf(targetPlaylistId))
                            },
                            onToggleFavorite = container.preferenceStore::toggleFavoriteSong,
                        )
                    }

                    composable(
                        route = "$ALBUM_ROUTE/{albumId}",
                        arguments = listOf(navArgument("albumId") { type = NavType.LongType }),
                    ) { backStackEntry ->
                        val albumId = backStackEntry.arguments?.getLong("albumId") ?: 0L
                        val album = libraryState.albums.firstOrNull { it.id == albumId }
                        val previousRoute = navController.previousBackStackEntry?.destination?.route
                        AlbumScreen(
                            album = album,
                            favoriteSongIds = favoriteSongIdSet,
                            currentSongId = playbackState.currentSong?.id,
                            isCurrentSongPlaying = isPlaybackActuallyPlaying,
                            bottomPadding = detailBottomPadding,
                            collapsedTopBarTitle = detailFallbackTitle(previousRoute, appLanguage),
                            onBack = navController::navigateUp,
                            onOpenTagEditor = { selectedAlbum ->
                                navController.navigate("$ALBUM_TAG_EDITOR_ROUTE/${selectedAlbum.id}")
                            },
                            onPlayAlbum = { selectedAlbum ->
                                container.playbackManager.playAlbum(
                                    album = selectedAlbum,
                                    shuffleEnabled = false,
                                )
                            },
                            onShuffleAlbum = { selectedAlbum ->
                                container.playbackManager.playAlbum(
                                    album = selectedAlbum,
                                    shuffleEnabled = true,
                                )
                            },
                            onSongSelected = { selectedSong, songs ->
                                container.playbackManager.playSong(
                                    song = selectedSong,
                                    collection = songs,
                                    sourceLabel = album?.title ?: selectedSong.album,
                                )
                                openPlayerIfAllowed(null)
                            },
                            playlists = playlists,
                            onAddSongsToPlaylist = { playlistId, songIds ->
                                container.preferenceStore.addSongsToPlaylist(playlistId, songIds)
                            },
                            onCreatePlaylist = container.preferenceStore::createPlaylist,
                            playlistSongsById = songsById,
                            onDeleteSongsFromDevice = deleteSongsFromDevice,
                            onToggleFavorite = container.preferenceStore::toggleFavoriteSong,
                            onSetAlbumFavorite = { songIds, favorite ->
                                container.preferenceStore.setFavoriteSongs(songIds, favorite)
                            },
                        )
                    }

                    composable(
                        route = "$ALBUM_TAG_EDITOR_ROUTE/{albumId}",
                        arguments = listOf(navArgument("albumId") { type = NavType.LongType }),
                    ) { backStackEntry ->
                        val albumId = backStackEntry.arguments?.getLong("albumId") ?: 0L
                        val album = libraryState.albums.firstOrNull { it.id == albumId }
                        val tagEditorService = remember(context.applicationContext) {
                            AlbumTagEditorService(context.applicationContext)
                        }
                        val routeScope = rememberCoroutineScope()
                        var pendingWriteRequest by remember(albumId) { mutableStateOf<AlbumTagEditRequest?>(null) }
                        var pickedCoverArtUri by remember(albumId) { mutableStateOf<Uri?>(null) }
                        var autofillSuggestion by remember(albumId) { mutableStateOf<AlbumTagMatchSuggestion?>(null) }
                        var editorStatusMessage by rememberSaveable(albumId) { mutableStateOf<String?>(null) }
                        var isSavingTags by remember(albumId) { mutableStateOf(false) }
                        var isMatchingTags by remember(albumId) { mutableStateOf(false) }
                        var performAlbumTagSave by remember(albumId) {
                            mutableStateOf<(suspend (AlbumTagEditRequest) -> Unit)?>(null)
                        }

                        val albumTagWriteLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                        ) { result ->
                            val pendingRequest = pendingWriteRequest ?: return@rememberLauncherForActivityResult
                            pendingWriteRequest = null
                            if (result.resultCode == Activity.RESULT_OK) {
                                routeScope.launch {
                                    performAlbumTagSave?.invoke(pendingRequest)
                                }
                            }
                        }

                        val coverArtPickerLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.OpenDocument(),
                        ) { uri ->
                            if (uri != null) {
                                pickedCoverArtUri = uri
                            }
                        }
                        performAlbumTagSave = { request ->
                            isSavingTags = true
                            editorStatusMessage = null
                            runCatching {
                                tagEditorService.applyEdits(request)
                            }.onSuccess {
                                container.libraryRepository.refreshChangedFiles(
                                    filePaths = querySongFilePaths(context, request.album.songs).toList(),
                                    enrichMetadata = true,
                                )
                                navController.navigateUp()
                            }.onFailure { throwable ->
                                val recoverableIntentSender = when {
                                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && throwable is RecoverableSecurityException -> {
                                        throwable.userAction.actionIntent.intentSender
                                    }
                                    else -> null
                                }
                                if (recoverableIntentSender != null) {
                                    pendingWriteRequest = request
                                    albumTagWriteLauncher.launch(
                                        IntentSenderRequest.Builder(recoverableIntentSender).build(),
                                    )
                                } else {
                                    editorStatusMessage = throwable.message ?: "Unable to save tags."
                                }
                            }
                            isSavingTags = false
                        }

                        AlbumTagEditorScreen(
                            album = album,
                            appLanguage = appLanguage,
                            isSaving = isSavingTags,
                            isMatching = isMatchingTags,
                            statusMessage = editorStatusMessage,
                            autofillSuggestion = autofillSuggestion,
                            pickedCoverArtUri = pickedCoverArtUri,
                            onBack = navController::navigateUp,
                            onSave = { request ->
                                routeScope.launch {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                        pendingWriteRequest = request
                                        album?.songs
                                            ?.takeIf { it.isNotEmpty() }
                                            ?.let { songsToWrite ->
                                                albumTagWriteLauncher.launch(
                                                    IntentSenderRequest.Builder(
                                                        MediaStore.createWriteRequest(
                                                            context.contentResolver,
                                                            songsToWrite.map(Song::uri),
                                                        ).intentSender,
                                                    ).build(),
                                                )
                                            }
                                            ?: performAlbumTagSave?.invoke(request)
                                    } else {
                                        performAlbumTagSave?.invoke(request)
                                    }
                                }
                            },
                            onAutoMatch = {
                                val targetAlbum = album
                                if (targetAlbum != null) {
                                    routeScope.launch {
                                    isMatchingTags = true
                                    editorStatusMessage = null
                                    autofillSuggestion = runCatching {
                                        tagEditorService.findBestOnlineMatch(targetAlbum)
                                    }.onFailure { throwable ->
                                        editorStatusMessage = throwable.message ?: "Unable to match album online."
                                    }.getOrNull()
                                    if (autofillSuggestion == null && editorStatusMessage == null) {
                                        editorStatusMessage = "No close online match found."
                                    }
                                    isMatchingTags = false
                                }
                                }
                            },
                            onPickCoverArt = {
                                coverArtPickerLauncher.launch(arrayOf("image/*"))
                            },
                        )
                    }

                    composable(
                        route = "$LIBRARY_COLLECTION_ROUTE/{kind}",
                        arguments = listOf(navArgument("kind") { type = NavType.StringType }),
                    ) { backStackEntry ->
                        val kindArg = backStackEntry.arguments?.getString("kind")
                        val kind = kindArg?.let { runCatching { LibraryCollectionKind.valueOf(it) }.getOrNull() }
                            ?: LibraryCollectionKind.Albums
                        LibraryCollectionScreen(
                            kind = kind,
                            libraryState = libraryState,
                            playlists = playlists,
                            songPlayCounts = songPlayCounts,
                            favoriteSongIds = favoriteSongIdSet,
                            albumCollectionLayoutMode = albumCollectionLayoutMode,
                            songCollectionLayoutMode = if (songCollectionGridEnabled) AlbumLayoutMode.Grid else AlbumLayoutMode.Compact,
                            albumSortMode = albumCollectionSortModeName.toAlbumSortMode(),
                            songSortMode = songCollectionSortModeName.toSongSortMode(),
                            currentSongId = playbackState.currentSong?.id,
                            isCurrentSongPlaying = isPlaybackActuallyPlaying,
                            bottomPadding = detailBottomPadding,
                            onBack = navController::navigateUp,
                            onAlbumSelected = { album, origin ->
                                detailExpandOrigin = origin
                                detailRouteTransitionMode = DetailRouteTransitionMode.TileExpand
                                navController.navigate("$ALBUM_ROUTE/${album.id}")
                            },
                            onAddAlbumToQueue = { album ->
                                album.songs.forEach(container.playbackManager::enqueueSong)
                            },
                            onSongSelected = { song, queue ->
                                container.playbackManager.playSong(
                                    song = song,
                                    collection = queue,
                                    sourceLabel = if (kind == LibraryCollectionKind.Songs) {
                                        "all songs"
                                    } else {
                                        queue.playbackSourceLabel(fallbackAlbum = song.album)
                                    },
                                )
                                openPlayerIfAllowed(null)
                            },
                            onToggleFavorite = container.preferenceStore::toggleFavoriteSong,
                            onAddAlbumToPlaylist = { playlistId, album ->
                                container.preferenceStore.addSongsToPlaylist(
                                    playlistId,
                                    album.songs.map(Song::id),
                                )
                            },
                            onCreatePlaylist = container.preferenceStore::createPlaylist,
                            playlistSongsById = songsById,
                            onSetAlbumFavorite = { songIds, favorite ->
                                container.preferenceStore.setFavoriteSongs(songIds, favorite)
                            },
                            onDeleteAlbumFromDevice = deleteAlbumFromDevice,
                            onAlbumCollectionLayoutModeChanged = { mode ->
                                container.preferenceStore.setAlbumCollectionLayoutMode(mode.name)
                            },
                            onSongCollectionLayoutModeChanged = { mode ->
                                container.preferenceStore.setSongCollectionGridEnabled(mode == AlbumLayoutMode.Grid)
                            },
                            onAlbumSortModeChanged = { mode ->
                                container.preferenceStore.setAlbumCollectionSortMode(mode.name)
                            },
                            onSongSortModeChanged = { mode ->
                                container.preferenceStore.setSongCollectionSortMode(mode.name)
                            },
                            onGenreSelected = { genre ->
                                navController.navigate("$GENRE_ROUTE/${Uri.encode(genre)}")
                            },
                            onArtistSelected = { artistName ->
                                navController.navigate("$ARTIST_ROUTE/${Uri.encode(artistName)}")
                            },
                        )
                    }

                    composable(
                        route = "$GENRE_ROUTE/{genre}",
                        arguments = listOf(navArgument("genre") { type = NavType.StringType }),
                    ) { backStackEntry ->
                        val genre = backStackEntry.arguments?.getString("genre")?.let(Uri::decode).orEmpty()
                        GenreAlbumsScreen(
                            genre = genre,
                            libraryState = libraryState,
                            playlists = playlists,
                            layoutMode = albumCollectionLayoutMode,
                            sortMode = albumCollectionSortModeName.toAlbumSortMode(),
                            bottomPadding = detailBottomPadding,
                            onBack = navController::navigateUp,
                            onLayoutModeChanged = { mode ->
                                container.preferenceStore.setAlbumCollectionLayoutMode(mode.name)
                            },
                            onSortModeChanged = { mode ->
                                container.preferenceStore.setAlbumCollectionSortMode(mode.name)
                            },
                            onAlbumSelected = { album, origin ->
                                detailExpandOrigin = origin
                                detailRouteTransitionMode = DetailRouteTransitionMode.TileExpand
                                navController.navigate("$ALBUM_ROUTE/${album.id}")
                            },
                            onAddAlbumToQueue = { album ->
                                album.songs.forEach(container.playbackManager::enqueueSong)
                            },
                            onAddAlbumToPlaylist = { playlistId, album ->
                                container.preferenceStore.addSongsToPlaylist(playlistId, album.songs.map(Song::id))
                            },
                            onCreatePlaylist = container.preferenceStore::createPlaylist,
                            playlistSongsById = songsById,
                            favoriteSongIds = favoriteSongIdSet,
                            onSetAlbumFavorite = { songIds, favorite ->
                                container.preferenceStore.setFavoriteSongs(songIds, favorite)
                            },
                            onDeleteAlbumFromDevice = deleteAlbumFromDevice,
                        )
                    }

                    composable(
                        route = "$ARTIST_ROUTE/{artistName}",
                        arguments = listOf(navArgument("artistName") { type = NavType.StringType }),
                    ) { backStackEntry ->
                        val artistName = backStackEntry.arguments?.getString("artistName")?.let(Uri::decode).orEmpty()
                        ArtistDetailScreen(
                            artistName = artistName,
                            libraryState = libraryState,
                            songPlayCounts = songPlayCounts,
                            favoriteSongIds = favoriteSongIdSet,
                            currentSongId = playbackState.currentSong?.id,
                            isCurrentSongPlaying = isPlaybackActuallyPlaying,
                            bottomPadding = detailBottomPadding,
                            onBack = navController::navigateUp,
                            onSongSelected = { song, queue ->
                                container.playbackManager.playSong(song, queue, sourceLabel = artistName)
                                openPlayerIfAllowed(null)
                            },
                            onAlbumSelected = { album, origin ->
                                detailExpandOrigin = origin
                                detailRouteTransitionMode = DetailRouteTransitionMode.Standard
                                navController.navigate("$ALBUM_ROUTE/${album.id}")
                            },
                            onToggleFavorite = container.preferenceStore::toggleFavoriteSong,
                        )
                    }

                    composable(EQUALIZER_ROUTE) {
                        EqualizerScreen(
                            settings = eqSettings,
                            onBack = navController::navigateUp,
                            onBandChanged = container.preferenceStore::updateBand,
                            onBassChanged = container.preferenceStore::updateBass,
                            onMidrangeChanged = container.preferenceStore::updateMidrange,
                            onTrebleChanged = container.preferenceStore::updateTreble,
                            onSpaciousnessChanged = container.preferenceStore::updateSpaciousness,
                            onSpaciousnessModeChanged = container.preferenceStore::updateSpaciousnessMode,
                            onReverbDurationChanged = container.preferenceStore::updateReverbDurationMs,
                            onReverbProfileChanged = container.preferenceStore::updateReverbProfile,
                            onResetReverb = {
                                container.preferenceStore.updateReverbDurationMs(0)
                                container.preferenceStore.updateReverbProfile(ReverbProfile.Dry)
                            },
                            onApplyPreset = container.preferenceStore::setEqSettings,
                            onReset = container.preferenceStore::resetEqSettings,
                        )
                    }

                    composable(SETTINGS_ROUTE) {
                        SettingsScreen(
                            themeMode = themeMode,
                            textSizePreset = textSizePreset,
                            appLanguage = appLanguage,
                            eqSettings = eqSettings,
                            isJellyfinConnected = isJellyfinConnected,
                            jellyfinHost = jellyfinHost,
                            bottomPadding = detailBottomPadding,
                            onBack = navController::navigateUp,
                            onThemeModeSelected = container.preferenceStore::setThemeMode,
                            onTextSizePresetSelected = container.preferenceStore::setTextSizePreset,
                            onAppLanguageSelected = container.preferenceStore::setAppLanguage,
                            onBassChanged = container.preferenceStore::updateBass,
                            onMidrangeChanged = container.preferenceStore::updateMidrange,
                            onTrebleChanged = container.preferenceStore::updateTreble,
                            onMonoPlaybackChanged = container.preferenceStore::updateMonoPlaybackEnabled,
                            onOpenEqualizer = { navController.navigate(EQUALIZER_ROUTE) },
                            onOpenJellyfinSetup = { navController.navigate(JELLYFIN_SETUP_ROUTE) },
                            onOpenChangelog = { navController.navigate(CHANGELOG_ROUTE) },
                            onScanLibrary = {
                                container.libraryRepository.refresh(
                                    forceMediaIndex = true,
                                    enrichMetadata = true,
                                    showLoadingIndicator = true,
                                )
                            },
                            onCheckForUpdates = {
                                container.appUpdateManager.checkForUpdates(force = true)
                            },
                        )
                    }

                    composable(CHANGELOG_ROUTE) {
                        ChangelogScreen(
                            releases = changelogReleases,
                            onBack = navController::navigateUp,
                        )
                    }

                    composable(ABOUT_ROUTE) {
                        AboutScreen(
                            onBack = navController::navigateUp,
                            bottomPadding = detailBottomPadding,
                        )
                    }

                    composable(JELLYFIN_LIBRARY_ROUTE) {
                        val jellyfinSongs by container.jellyfinRepository.songs.collectAsStateWithLifecycle()
                        val jellyfinAlbums by container.jellyfinRepository.albums.collectAsStateWithLifecycle()
                        val jellyfinArtists by container.jellyfinRepository.artists.collectAsStateWithLifecycle()
                        val jellyfinState = remember(jellyfinSongs, jellyfinAlbums) {
                            LibraryUiState(
                                permissionGranted = true,
                                isLoading = false,
                                songs = jellyfinSongs,
                                albums = jellyfinAlbums,
                                artists = jellyfinArtists,
                            )
                        }
                        LibraryCollectionScreen(
                            kind = LibraryCollectionKind.Albums,
                            libraryState = jellyfinState,
                            playlists = playlists,
                            songPlayCounts = songPlayCounts,
                            favoriteSongIds = favoriteSongIdSet,
                            albumCollectionLayoutMode = albumCollectionLayoutMode,
                            songCollectionLayoutMode = if (songCollectionGridEnabled) AlbumLayoutMode.Grid else AlbumLayoutMode.Compact,
                            albumSortMode = albumCollectionSortModeName.toAlbumSortMode(),
                            songSortMode = songCollectionSortModeName.toSongSortMode(),
                            currentSongId = playbackState.currentSong?.id,
                            isCurrentSongPlaying = isPlaybackActuallyPlaying,
                            bottomPadding = detailBottomPadding,
                            onBack = navController::navigateUp,
                            onAlbumSelected = { album, origin ->
                                detailExpandOrigin = origin
                                detailRouteTransitionMode = DetailRouteTransitionMode.TileExpand
                                navController.navigate("$ALBUM_ROUTE/${album.id}")
                            },
                            onAddAlbumToQueue = { album ->
                                album.songs.forEach(container.playbackManager::enqueueSong)
                            },
                            onSongSelected = { song, queue ->
                                container.playbackManager.playSong(
                                    song = song,
                                    collection = queue,
                                    sourceLabel = "Jellyfin",
                                )
                                openPlayerIfAllowed(null)
                            },
                            onToggleFavorite = container.preferenceStore::toggleFavoriteSong,
                            onAddAlbumToPlaylist = { playlistId, album ->
                                container.preferenceStore.addSongsToPlaylist(
                                    playlistId,
                                    album.songs.map(Song::id),
                                )
                            },
                            onCreatePlaylist = container.preferenceStore::createPlaylist,
                            playlistSongsById = songsById,
                            onSetAlbumFavorite = { songIds, favorite ->
                                container.preferenceStore.setFavoriteSongs(songIds, favorite)
                            },
                            onDeleteAlbumFromDevice = { /* Cannot delete Jellyfin from device this way */ },
                            onAlbumCollectionLayoutModeChanged = { mode ->
                                container.preferenceStore.setAlbumCollectionLayoutMode(mode.name)
                            },
                            onSongCollectionLayoutModeChanged = { mode ->
                                container.preferenceStore.setSongCollectionGridEnabled(mode == AlbumLayoutMode.Grid)
                            },
                            onAlbumSortModeChanged = { mode ->
                                container.preferenceStore.setAlbumCollectionSortMode(mode.name)
                            },
                            onSongSortModeChanged = { mode ->
                                container.preferenceStore.setSongCollectionSortMode(mode.name)
                            },
                            onGenreSelected = { genre ->
                                navController.navigate("$GENRE_ROUTE/${Uri.encode(genre)}")
                            },
                            onArtistSelected = { artistName ->
                                navController.navigate("$ARTIST_ROUTE/${Uri.encode(artistName)}")
                            },
                        )
                    }

                    composable(JELLYFIN_SETUP_ROUTE) {
                        JellyfinSetupScreen(
                            container = container,
                            onBack = navController::navigateUp,
                        )
                    }
                }
                    if (navHostScrimAlpha > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background.copy(alpha = navHostScrimAlpha)),
                        )
                    }
                    }
                    if (showSharedTopBarBackdrop && sharedTopBarSpec != null) {
                        FrostedTopBarBackground(
                            darkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .fillMaxWidth()
                                .height(sharedTopBarHeight)
                                .zIndex(7f),
                        )
                    }
                    if (sharedTopBarSpec != null) {
                        CompositionLocalProvider(LocalRenderSharedTopBarContent provides true) {
                            SharedTopBarOverlay(
                                spec = sharedTopBarSpec,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .fillMaxWidth()
                                    .zIndex(9f),
                            )
                        }
                    }
                    ElovaireAnimatedVisibility(
                        visible = showTopBarMenu,
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(10f),
                        enter = ElovaireMotion.overlayFadeEnter(initialAlpha = 0.86f),
                        exit = ElovaireMotion.overlayFadeExit(),
                        label = "TopBarContextMenuOverlay",
                    ) {
                        TopBarContextMenuOverlay(
                            onDismiss = { showTopBarMenu = false },
                            onOpenSettings = openSettingsFromMenu,
                            onOpenEqualizer = openEqualizerFromMenu,
                            onOpenChangelog = openChangelogSheetFromMenu,
                            onOpenAbout = openAboutFromMenu,
                        )
                    }
                    ElovaireAnimatedVisibility(
                        visible = showChangelogSheet,
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(11f),
                        enter = ElovaireMotion.bottomSheetEnter(),
                        exit = ElovaireMotion.bottomSheetExit(),
                        label = "ChangelogSheetOverlay",
                    ) {
                        ChangelogBottomSheetOverlay(
                            releases = changelogReleases,
                            onDismiss = { showChangelogSheet = false },
                        )
                    }
                    if (showPlaylistCreateDialog) {
                        PlaylistNameDialog(
                            onDismiss = { showPlaylistCreateDialog = false },
                            onConfirm = { name ->
                                val createdId = container.preferenceStore.createPlaylist(name)
                                if (createdId > 0L) {
                                    showPlaylistCreateDialog = false
                                }
                            },
                        )
                    }
                    ElovaireAnimatedVisibility(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .zIndex(7f)
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = topBarHeight + 8.dp,
                            ),
                        visible = (showTopLevelChrome || currentRoute == SETTINGS_ROUTE) &&
                            appUpdateState.availableRelease != null,
                        enter = ElovaireMotion.bannerEnter(),
                        exit = ElovaireMotion.bannerExit(),
                        label = "UpdateBannerVisibility",
                    ) {
                        appUpdateState.availableRelease?.let { release ->
                            UpdateAvailableBanner(
                                release = release,
                                uiState = appUpdateState,
                                onDismiss = container.appUpdateManager::dismissAvailableUpdate,
                                onUpdate = container.appUpdateManager::startUpdate,
                            )
                        }
                    }
                    ElovaireAnimatedVisibility(
                        visible = showFirstLaunchPermissionOverlay,
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(9f),
                        enter = ElovaireMotion.overlayFadeEnter(initialAlpha = 0.82f),
                        exit = ElovaireMotion.overlayFadeExit(targetAlpha = 0.96f),
                        label = "FirstLaunchPermissionOverlayVisibility",
                    ) {
                        FirstLaunchPermissionLoadingScreen(
                            showLoading = true,
                            onRequestPermission = { permissionLauncher.launch(audioPermission()) },
                        )
                    }
                }
                if (canHostCompactNowPlaying) {
                    playbackNowPlayingState.currentSong?.let {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .zIndex(7f)
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = if (showBottomNavigation) bottomNavHeight + 8.dp else navigationBarInsetDp() + 10.dp,
                                ),
                            contentAlignment = Alignment.BottomCenter,
                        ) {
                            CompactNowPlayingDockHost(
                                viewModel = nowPlayingViewModel,
                                visible = showGlobalNowPlaying,
                                suppressEnterAnimation = reenteringFromPlayer,
                                onOpenPlayer = openPlayerIfAllowed,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer {
                                        alpha = if (isPlayerOverlayVisible) {
                                            (1f - (playerTransitionProgress / 0.15f)).coerceIn(0f, 1f)
                                        } else {
                                            1f
                                        }
                                    },
                            )
                        }
                    }
                }
                ElovaireAnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .zIndex(8f)
                        .fillMaxWidth(),
                    visible = showBottomNavigation,
                    enter = if (reenteringFromPlayer) {
                        EnterTransition.None
                    } else {
                        ElovaireMotion.bottomBarEnter()
                    },
                    exit = ElovaireMotion.bottomBarExit(),
                    label = "BottomNavigationVisibility",
                ) {
                    BottomNavigationBar(
                        currentRoute = activeBottomRoute,
                        suppressEnterAnimation = reenteringFromPlayer,
                        destinations = topLevelDestinations,
                        onNavigate = { route ->
                            val currentTopLevelRoute = activeBottomRoute
                            browsingOriginRoute = route
                            selectedBottomRoute = route
                            routeOwnerOverrides[route] = route
                            if (route == currentTopLevelRoute) {
                                if (currentRoute != route) {
                                    val poppedToTabRoot = navController.popBackStack(route, inclusive = false)
                                    if (!poppedToTabRoot) {
                                        navController.navigate(route) {
                                            launchSingleTop = true
                                            restoreState = true
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                        }
                                    }
                                    resetTopLevelTabState(route)
                                } else {
                                    resetTopLevelTabState(route)
                                }
                            } else {
                                navController.navigate(route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                alpha = if (isPlayerOverlayVisible) {
                                    (1f - (playerTransitionProgress / 0.15f)).coerceIn(0f, 1f)
                                } else {
                                    1f
                                }
                            },
                    )
                }
            }
            }
            if (isPlayerOverlayVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clipToBounds()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {},
                        )
                        .zIndex(20f),
                ) {
                    NowPlayingRoute(
                        viewModel = nowPlayingViewModel,
                        playbackManager = container.playbackManager,
                        enrichedSongsById = songsById,
                        isFavorite = playbackNowPlayingState.currentSong?.id in favoriteSongIdSet,
                        playlists = playlists.filterNot { it.isSystem },
                        onBack = { isPlayerOverlayVisible = false },
                        onOpenCurrentAlbum = openCurrentPlayingAlbum,
                        onToggleFavorite = { songId -> container.preferenceStore.toggleFavoriteSong(songId) },
                        onAddCurrentSongToPlaylist = { playlistId, song ->
                            container.preferenceStore.addSongsToPlaylist(playlistId, listOf(song.id))
                        },
                        onCreatePlaylist = container.preferenceStore::createPlaylist,
                        onOpenEqualizer = {
                            isPlayerOverlayVisible = false
                            navController.navigate(EQUALIZER_ROUTE)
                        },
                        eqSettings = eqSettings,
                        onSpaciousnessChanged = container.preferenceStore::updateSpaciousness,
                        transitionSnapshot = nowPlayingTransitionSnapshot,
                        onTransitionProgress = { progress, state ->
                            playerTransitionProgress = progress
                            playerTransitionState = state
                            if (state == PlayerOverlayTransitionState.Expanded && progress >= 0.99f) {
                                nowPlayingTransitionSnapshot = null
                            }
                        },
                        onClearTransitionSnapshot = {
                            nowPlayingTransitionSnapshot = null
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
            CastDevicePickerOverlay(controller = castPickerController)
        }
    }
}

internal fun libraryUiStateOf(
    content: LibraryContentState,
    scan: LibraryScanState,
): LibraryUiState {
    return LibraryUiState(
        permissionGranted = scan.permissionGranted,
        isLoading = scan.isLoading,
        scanProgress = scan.scanProgress,
        songs = content.songs,
        albums = content.albums,
        errorMessage = scan.errorMessage,
    )
}

internal fun playbackUiStateOf(
    nowPlaying: PlaybackNowPlayingState,
    transport: PlaybackTransportState,
    queue: PlaybackQueueState,
    volume: PlaybackVolumeState,
    recent: RecentPlaybackState,
): PlaybackUiState {
    return PlaybackUiState(
        queue = queue.queue,
        currentIndex = queue.currentIndex,
        isPlaying = transport.isPlaying,
        transportShowsPause = transport.transportShowsPause,
        repeatMode = transport.repeatMode,
        shuffleEnabled = transport.shuffleEnabled,
        sourceLabel = nowPlaying.sourceLabel,
        volume = volume.volume,
        audioSessionId = nowPlaying.audioSessionId,
        recentSongIds = recent.recentSongIds,
        recentAlbumIds = recent.recentAlbumIds,
        recentPlaylistIds = recent.recentPlaylistIds,
        sourcePlaylistId = queue.sourcePlaylistId,
        lastPlayedCollectionKind = recent.lastPlayedCollectionKind,
        lastPlayedCollectionId = recent.lastPlayedCollectionId,
    )
}
