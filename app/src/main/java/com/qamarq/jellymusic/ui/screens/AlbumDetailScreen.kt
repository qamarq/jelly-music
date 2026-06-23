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
import androidx.compose.foundation.layout.widthIn
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
internal fun AlbumScreen(
    album: Album?,
    favoriteSongIds: Set<Long>,
    currentSongId: Long?,
    isCurrentSongPlaying: Boolean,
    bottomPadding: Dp,
    collapsedTopBarTitle: String,
    playlists: List<Playlist>,
    playlistSongsById: Map<Long, Song>,
    onBack: () -> Unit,
    onOpenTagEditor: (Album) -> Unit,
    onPlayAlbum: (Album) -> Unit,
    onShuffleAlbum: (Album) -> Unit,
    onSongSelected: (Song, List<Song>) -> Unit,
    onAddSongsToPlaylist: (Long, List<Long>) -> Unit,
    onCreatePlaylist: (String) -> Long,
    onDeleteSongsFromDevice: (List<Song>) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onSetAlbumFavorite: (List<Long>, Boolean) -> Unit,
) {
    LaunchedEffect(album?.id) {
        if (album == null) {
            onBack()
        }
    }
    if (album == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Album not found.")
        }
        return
    }

    val language = LocalAppLanguage.current
    val gradient by rememberArtworkGradient(album.artUri)
    val isLightTheme = MaterialTheme.colorScheme.background.luminance() > 0.5f
    val albumSongIds = remember(album.songs) { album.songs.map(Song::id) }
    val discGroups = remember(album.songs) {
        album.songs
            .groupBy { it.discNumber.coerceAtLeast(1) }
            .toSortedMap()
            .entries
            .map { it.key to it.value }
    }
    val showDiscSections = discGroups.size > 1
    val isAlbumFavorite = albumSongIds.isNotEmpty() && albumSongIds.all { it in favoriteSongIds }
    val albumFavoriteBackground = gradient.first()
        .copy(alpha = if (isLightTheme) 0.2f else 0.26f)
        .compositeOver(
            if (isLightTheme) {
                MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
            } else {
                Color.Black.copy(alpha = 0.18f)
            },
        )
    val albumFavoriteTint = if (albumFavoriteBackground.luminance() > 0.56f) InkText else Color.White
    val albumOnSurface = MaterialTheme.colorScheme.onSurface
    val albumInfoPillBackground = gradient.first()
        .copy(alpha = if (isLightTheme) 0.1f else 0.16f)
        .compositeOver(MaterialTheme.colorScheme.surface.copy(alpha = if (isLightTheme) 0.94f else 0.88f))
    val albumInfoPillTint = if (albumInfoPillBackground.luminance() > 0.56f) InkText else Color.White
    val density = LocalDensity.current
    var albumTitleBounds by remember(album.id) { mutableStateOf<androidx.compose.ui.geometry.Rect?>(null) }
    val albumPrimarySong = remember(album.songs) {
        album.songs.firstOrNull()
    }
    val albumYear = remember(album.songs) {
        album.songs.firstNotNullOfOrNull { it.releaseYear }
    }
    val albumGenre = remember(album.songs) {
        album.songs.firstOrNull { it.genre.isNotBlank() && it.genre != "Unknown Genre" }?.genre
    }
    val albumTechnicalReferenceSong = remember(album.songs) {
        album.songs.firstOrNull { !it.audioQuality.isNullOrBlank() }
            ?: album.songs.firstOrNull()
    }
    val albumMetaItems = remember(album) {
        buildList {
            albumYear?.toString()?.let(::add)
            albumGenre
                ?.let(::add)
        }
    }
    val albumMetaText = remember(albumMetaItems, albumOnSurface) {
        buildAnnotatedString {
            albumMetaItems.forEachIndexed { index, item ->
                if (index > 0) {
                    pushStyle(SpanStyle(color = albumOnSurface.copy(alpha = 0.72f)))
                    append("  •  ")
                    pop()
                }
                val isYear = index == 0 && albumYear != null
                pushStyle(
                    SpanStyle(
                        color = if (isYear) albumOnSurface else albumOnSurface.copy(alpha = 0.72f),
                        fontWeight = if (isYear) FontWeight.SemiBold else FontWeight.Normal,
                    ),
                )
                append(item)
                pop()
            }
        }
    }
    val albumFooterText = remember(album.songCount, album.durationMs, albumOnSurface, language) {
        buildAnnotatedString {
            pushStyle(
                SpanStyle(
                    color = albumOnSurface,
                    fontWeight = FontWeight.Normal,
                ),
            )
            append(localizedCountLabel(album.songCount, "track", language))
            pop()
            pushStyle(SpanStyle(color = albumOnSurface.copy(alpha = 0.5f)))
            append("  •  ")
            pop()
            pushStyle(
                SpanStyle(
                    color = albumOnSurface.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Normal,
                ),
            )
            append(formatDuration(album.durationMs))
            pop()
        }
    }
    val detailTopPadding = detailTopBarOccupiedHeight()
    val topBarBottomPx = with(density) { detailTopPadding.roundToPx() }
    val detailTopBarTitle = if ((albumTitleBounds?.top ?: Float.MAX_VALUE) < topBarBottomPx) {
        album.title
    } else {
        collapsedTopBarTitle
    }
    var selectedSongIds by rememberSaveable(album.id) { mutableStateOf(setOf<Long>()) }
    var showPlaylistPicker by rememberSaveable(album.id) { mutableStateOf(false) }
    val selectionModeActive = selectedSongIds.isNotEmpty()
    val selectedSongs = remember(album.songs, selectedSongIds) {
        album.songs.filter { it.id in selectedSongIds }
    }
    BackHandler(enabled = selectionModeActive) {
        selectedSongIds = emptySet()
        showPlaylistPicker = false
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            overscrollEffect = null,
            modifier = Modifier
                .fillMaxSize()
                .ensureSingleItemRubberBand(listState),
            contentPadding = PaddingValues(
                start = 20.dp,
                top = detailTopPadding + ElovaireSpacing.albumHeaderTopGap,
                end = 20.dp,
                bottom = bottomPadding,
            ),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                    ) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .padding(horizontal = 28.dp, vertical = 36.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            gradient.first().copy(alpha = if (isLightTheme) 0.28f else 0.41f),
                                            gradient.last().copy(alpha = if (isLightTheme) 0.1f else 0.14f),
                                            Color.Transparent,
                                        ),
                                        radius = 780f,
                                    ),
                                    shape = RoundedCornerShape(ElovaireRadii.module),
                                )
                                .blur(if (isLightTheme) 32.dp else 40.dp),
                        )
                        Surface(
                            modifier = Modifier.matchParentSize(),
                            shape = RoundedCornerShape(ElovaireRadii.module),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = if (isLightTheme) 0.08f else 0.16f),
                            tonalElevation = 0.dp,
                            shadowElevation = 22.dp,
                        ) {
                            ArtworkImage(
                                uri = album.artUri,
                                title = album.title,
                                modifier = Modifier.fillMaxSize(),
                                cornerRadius = ElovaireRadii.artwork,
                            )
                        }
                        FavoriteSongButton(
                            isFavorite = isAlbumFavorite,
                            tint = albumFavoriteTint,
                            backgroundColor = albumFavoriteBackground,
                            borderColor = Color.White.copy(alpha = 0.16f),
                            frosted = true,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(14.dp),
                            onClick = {
                                onSetAlbumFavorite(albumSongIds, !isAlbumFavorite)
                            },
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .align(Alignment.CenterHorizontally),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = album.title,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = elovaireScaledSp(ALBUM_HEADER_TITLE_TEXT_SIZE_SP),
                                lineHeight = MaterialTheme.typography.displayLarge.lineHeight * 0.8f,
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                albumTitleBounds = coordinates.boundsInWindow()
                            },
                        )
                        Text(
                            text = album.artist,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = elovaireScaledSp(ALBUM_HEADER_ARTIST_TEXT_SIZE_SP),
                                fontWeight = FontWeight.Medium,
                            ),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.76f),
                            textAlign = TextAlign.Center,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text(
                                text = albumMetaText,
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = elovaireScaledSp(12f)),
                                color = albumOnSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Surface(
                                shape = RoundedCornerShape(ElovaireRadii.pill),
                                color = albumInfoPillBackground,
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_lucide_audio_waveform),
                                        contentDescription = null,
                                        tint = albumInfoPillTint.copy(alpha = 0.82f),
                                        modifier = Modifier.size(12.dp),
                                    )
                                    Text(
                                        text = albumTechnicalReferenceSong?.audioFormat ?: "AUDIO",
                                        style = MaterialTheme.typography.labelLarge.copy(fontSize = elovaireScaledSp(11f)),
                                        color = albumInfoPillTint.copy(alpha = 0.94f),
                                        maxLines = 1,
                                    )
                                    Text(
                                        text = albumTechnicalReferenceSong?.audioQuality
                                            ?: if (albumTechnicalReferenceSong?.audioFormat == "STREAM") "Stream"
                                            else "--",
                                        style = MaterialTheme.typography.labelLarge.copy(fontSize = elovaireScaledSp(11f)),
                                        color = albumInfoPillTint.copy(alpha = 0.74f),
                                        maxLines = 1,
                                    )
                                }
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AlbumHeaderPlayButton(
                                tint = Color.White,
                                backgroundColor = RoseAccent,
                                onClick = { onPlayAlbum(album) },
                            )
                            AlbumHeaderActionButton(
                                iconResId = R.drawable.ic_lucide_shuffle,
                                contentDescription = "Shuffle album",
                                tint = Color.White,
                                backgroundColor = RoseAccent.copy(alpha = 0.7f),
                                iconSize = 18.dp,
                                onClick = { onShuffleAlbum(album) },
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(18.dp))
            }

            discGroups.forEachIndexed { discGroupIndex, (discNumber, discSongs) ->
                if (showDiscSections) {
                    item("disc_header_$discNumber") {
                        DiscSectionHeader(discNumber = discNumber)
                    }
                }

                itemsIndexed(
                    items = discSongs,
                    key = { _, song -> song.id },
                    contentType = { _, _ -> "album_song_row" },
                ) { index, song ->
                    GroupedListRowContainer(
                        index = index,
                        lastIndex = discSongs.lastIndex,
                    ) {
                        AlbumSongRow(
                            song = song,
                            trackIndex = if (song.trackNumber > 0) song.trackNumber else index + 1,
                            selectionMode = selectionModeActive,
                            selected = song.id in selectedSongIds,
                            isFavorite = song.id in favoriteSongIds,
                            isCurrentSong = song.id == currentSongId,
                            isPlaybackActive = isCurrentSongPlaying,
                            onClick = {
                                if (selectionModeActive) {
                                    selectedSongIds = selectedSongIds.toggleSelection(song.id)
                                } else {
                                    onSongSelected(song, album.songs)
                                }
                            },
                            onLongPress = {
                                selectedSongIds = selectedSongIds + song.id
                            },
                            onToggleFavorite = { onToggleFavorite(song.id) },
                            showDivider = index != discSongs.lastIndex,
                        )
                    }
                }

                if (showDiscSections && discGroupIndex != discGroups.lastIndex) {
                    item("disc_spacer_$discNumber") {
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp, bottom = 6.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = albumFooterText,
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = elovaireScaledSp(12f)),
                        color = albumOnSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }

        DetailListTopBar(
            title = detailTopBarTitle,
            subtitle = null,
            onBack = onBack,
            actions = listOf(
                TopBarActionSpec(
                    iconResId = R.drawable.ic_lucide_square_pen,
                    contentDescription = "Edit tags",
                    onClick = { onOpenTagEditor(album) },
                ),
            ),
            modifier = Modifier.align(Alignment.TopCenter),
        )
        AnimatedVisibility(
            visible = selectionModeActive,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .zIndex(3f),
            enter = ElovaireMotion.verticalRevealEnter(),
            exit = ElovaireMotion.verticalRevealExit(),
        ) {
            TopBarSelectionMenu(
                topBarHeight = detailTopPadding,
                onAddToPlaylist = { showPlaylistPicker = true },
                onDelete = {
                    onDeleteSongsFromDevice(selectedSongs)
                    selectedSongIds = emptySet()
                },
            )
        }
    }

    if (showPlaylistPicker && selectionModeActive) {
        val language = LocalAppLanguage.current
        PlaylistSelectionDialog(
            title = uiPhrase(language, UiPhrase.AddToPlaylist),
            subtitle = "${localizedCountLabel(selectedSongs.size, "song", language)} ${miscPhrase(language, MiscPhrase.Selected)}",
            playlists = playlists.filterNot { it.isSystem },
            playlistSongsById = playlistSongsById,
            onDismiss = { showPlaylistPicker = false },
            onPlaylistSelected = { playlistId ->
                onAddSongsToPlaylist(playlistId, selectedSongs.map(Song::id))
                selectedSongIds = emptySet()
                showPlaylistPicker = false
            },
            onCreatePlaylist = onCreatePlaylist,
        )
    }
}

@Composable
internal fun DiscSectionHeader(
    discNumber: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_lucide_disc_3),
            contentDescription = null,
            tint = readableMutedIconColor(),
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = "Disc $discNumber",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
internal fun AnimatedAudioLinesIcon(
    tint: Color,
    animate: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val motionDurationScale = rememberSystemAnimationScale()
    val phase = if (animate && motionDurationScale > 0f) {
        val infiniteTransition = rememberInfiniteTransition(label = "audio_lines_phase")
        val animatedPhase by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = ElovaireMotion.scaleDurationMillis(1180, motionDurationScale).toInt(),
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "audio_lines_value",
        )
        animatedPhase
    } else {
        0.35f
    }
    val baseHeights = floatArrayOf(0.26f, 0.54f, 0.84f, 0.42f, 0.68f, 0.3f)
    val phaseOffsets = floatArrayOf(0f, 0.17f, 0.31f, 0.48f, 0.67f, 0.83f)
    Canvas(modifier = modifier) {
        val lineWidth = size.width / 10f
        val gap = (size.width - (lineWidth * baseHeights.size)) / (baseHeights.size - 1).coerceAtLeast(1)
        val centerY = size.height / 2f
        baseHeights.forEachIndexed { index, baseHeight ->
            val primaryPhase = ((phase + phaseOffsets[index]) % 1f) * 6.2831855f
            val secondaryPhase = ((phase * 1.82f) + (phaseOffsets[index] * 0.58f)) * 6.2831855f
            val animationFactor = (
                ((sin(primaryPhase.toDouble()) + 1.0) * 0.5 * 0.72) +
                    ((sin(secondaryPhase.toDouble()) + 1.0) * 0.5 * 0.28)
                ).toFloat()
            val heightFactor = (baseHeight * 0.66f) + (animationFactor * 0.24f)
            val lineHeight = size.height * heightFactor
            val startX = index * (lineWidth + gap) + (lineWidth / 2f)
            drawLine(
                color = tint,
                start = Offset(startX, centerY - (lineHeight / 2f)),
                end = Offset(startX, centerY + (lineHeight / 2f)),
                strokeWidth = lineWidth,
                cap = StrokeCap.Round,
            )
        }
    }
}

@Composable
internal fun PlaybackActiveArtworkOverlay(
    uri: Uri?,
    title: String,
    modifier: Modifier = Modifier,
) {
    val artworkShape = RoundedCornerShape(ElovaireRadii.artworkSmall)
    val artworkBitmap = rememberArtworkBitmap(uri = uri, size = 256).value
    Box(
        modifier = modifier
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
                shape = artworkShape
                clip = true
            }
            .clip(artworkShape),
        contentAlignment = Alignment.Center,
    ) {
        if (artworkBitmap != null) {
            Image(
                bitmap = artworkBitmap,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        scaleX = 1.08f
                        scaleY = 1.08f
                    }
                    .blur(18.dp),
            )
        } else {
            ArtworkImage(
                uri = uri,
                title = title,
                modifier = Modifier
                    .matchParentSize()
                    .blur(18.dp),
                cornerRadius = ElovaireRadii.artworkSmall,
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.12f)),
        )
        AnimatedAudioLinesIcon(
            tint = Color.White.copy(alpha = 0.82f),
            animate = true,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
internal fun AlbumSongRow(
    song: Song,
    trackIndex: Int,
    selectionMode: Boolean,
    selected: Boolean,
    isFavorite: Boolean,
    isCurrentSong: Boolean,
    isPlaybackActive: Boolean,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    onToggleFavorite: () -> Unit,
    showDivider: Boolean,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick,
                    onLongClick = onLongPress,
                )
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier.width(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (selectionMode) {
                    SelectionIndicatorIcon(
                        selected = selected,
                        modifier = Modifier.size(18.dp),
                    )
                } else {
                    AnimatedContent(
                        targetState = isCurrentSong && isPlaybackActive,
                        transitionSpec = { ElovaireMotion.quickContentSwapTransform() },
                        label = "album_row_track_indicator",
                    ) { showSignal ->
                        if (showSignal) {
                            AnimatedAudioLinesIcon(
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                animate = isPlaybackActive,
                                modifier = Modifier.size(16.dp),
                            )
                        } else {
                            Text(
                                text = trackIndex.toString(),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
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
                        color = if (isCurrentSong) {
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
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
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
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    modifier = Modifier.widthIn(min = 44.dp),
                )
                InlineFavoriteSongButton(
                    isFavorite = isFavorite,
                    tint = MaterialTheme.colorScheme.onSurface,
                    onClick = onToggleFavorite,
                )
                SongOverflowMenuButton(
                    song = song,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        if (showDivider) {
            DividerLine()
        }
    }
}
