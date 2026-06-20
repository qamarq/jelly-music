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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

@Composable
internal fun SearchRoute(
    viewModel: SearchViewModel,
    libraryState: LibraryUiState,
    favoriteSongIds: Set<Long>,
    topPadding: Dp,
    bottomPadding: Dp,
    scrollToTopRequestVersion: Long,
    isSearchFieldFocused: Boolean,
    onSearchFieldFocusedChange: (Boolean) -> Unit,
    onSearchQueryActiveChanged: (Boolean) -> Unit,
    onPlaySong: (Song, List<Song>) -> Unit,
    onAlbumSelected: (Album, ExpandOrigin) -> Unit,
    onArtistSelected: (String) -> Unit,
    onToggleFavorite: (Long) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SearchScreen(
        libraryState = libraryState,
        state = state,
        favoriteSongIds = favoriteSongIds,
        topPadding = topPadding,
        bottomPadding = bottomPadding,
        scrollToTopRequestVersion = scrollToTopRequestVersion,
        isSearchFieldFocused = isSearchFieldFocused,
        onQueryChange = viewModel::onQueryChange,
        onSearchFieldFocusedChange = onSearchFieldFocusedChange,
        onShowAllSongResultsChange = viewModel::onShowAllSongResultsChange,
        onSearchSongSortModeChange = viewModel::onSearchSongSortModeChange,
        onShowSearchSongSortOptionsChange = viewModel::onShowSearchSongSortOptionsChange,
        onSearchQueryActiveChanged = onSearchQueryActiveChanged,
        onSongSelected = { song, queue ->
            viewModel.rememberArtistSearch(song)
            onPlaySong(song, queue)
        },
        onAlbumSelected = { album, origin, rememberSearch ->
            if (rememberSearch) {
                viewModel.rememberAlbumSearch(album)
            }
            onAlbumSelected(album, origin)
        },
        onArtistSelected = onArtistSelected,
        onToggleFavorite = onToggleFavorite,
        onClearSearchHistory = viewModel::clearSearchHistory,
        onResetSearchUi = viewModel::resetSearchUi,
    )
}

@Composable
internal fun SearchScreen(
    libraryState: LibraryUiState,
    state: SearchUiState,
    favoriteSongIds: Set<Long>,
    topPadding: Dp,
    bottomPadding: Dp,
    scrollToTopRequestVersion: Long,
    isSearchFieldFocused: Boolean,
    onQueryChange: (String) -> Unit,
    onSearchFieldFocusedChange: (Boolean) -> Unit,
    onShowAllSongResultsChange: (Boolean) -> Unit,
    onSearchSongSortModeChange: (SearchSongSortMode) -> Unit,
    onShowSearchSongSortOptionsChange: (Boolean) -> Unit,
    onSearchQueryActiveChanged: (Boolean) -> Unit,
    onSongSelected: (Song, List<Song>) -> Unit,
    onAlbumSelected: (Album, ExpandOrigin, Boolean) -> Unit,
    onArtistSelected: (String) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onClearSearchHistory: () -> Unit,
    onResetSearchUi: () -> Unit,
) {
    val language = LocalAppLanguage.current
    val copy = searchCopy(language)
    val listState = rememberElovaireLazyListState("search_screen")
    LaunchedEffect(scrollToTopRequestVersion) {
        if (scrollToTopRequestVersion > 0L) {
            listState.animateScrollToItem(0)
        }
    }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val trimmedQuery = state.query.trim()
    val allSongsListState = rememberElovaireLazyListState("search_all_songs", trimmedQuery, state.searchSongSortMode)
    val isSearchUiActive = trimmedQuery.isNotBlank() || isSearchFieldFocused || state.showAllSongResults
    val collapseAllSongResults: () -> Unit = {
        onShowAllSongResultsChange(false)
        onShowSearchSongSortOptionsChange(false)
    }
    val resetSearchToMain: () -> Unit = {
        onResetSearchUi()
        onSearchFieldFocusedChange(false)
        keyboardController?.hide()
        focusManager.clearFocus(force = true)
    }
    if (state.showAllSongResults && trimmedQuery.isNotBlank()) {
        RegisterSharedTopBar(
            SharedTopBarSpec.Back(
                title = commonUiCopy(language).search,
                onBack = collapseAllSongResults,
                centeredTitle = false,
            ),
        )
    }
    BackHandler(enabled = isSearchUiActive) {
        when {
            state.showSearchSongSortOptions -> onShowSearchSongSortOptionsChange(false)
            state.showAllSongResults && trimmedQuery.isNotBlank() -> collapseAllSongResults()
            else -> resetSearchToMain()
        }
    }
    LaunchedEffect(isSearchUiActive) {
        onSearchQueryActiveChanged(isSearchUiActive)
    }
    LaunchedEffect(trimmedQuery) {
        if (trimmedQuery.isBlank()) {
            onShowAllSongResultsChange(false)
            onShowSearchSongSortOptionsChange(false)
        }
    }
    LaunchedEffect(scrollToTopRequestVersion, state.contentMode) {
        if (scrollToTopRequestVersion > 0L && state.contentMode == SearchContentMode.AllSongs) {
            allSongsListState.animateScrollToItem(0)
        }
    }
    val matchingArtists = remember(state.matchingArtists, language) {
        state.matchingArtists.map { artist ->
            SearchHistoryEntry(
                key = "artist:${artist.name.lowercase()}",
                kind = SearchHistoryKind.Artist,
                title = artist.name,
                subtitle = localizedCountLabel(artist.songCount, "song", language),
                artUri = artist.artUri,
                query = artist.name,
            )
        }
    }

    val searchBar: @Composable () -> Unit = {
        val searchBarContentColor = MaterialTheme.colorScheme.onSurface
        OutlinedTextField(
            value = state.query,
            onValueChange = {
                onQueryChange(it)
                if (it.trim().isBlank()) {
                    onShowAllSongResultsChange(false)
                    onShowSearchSongSortOptionsChange(false)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    onSearchFieldFocusedChange(focusState.isFocused)
                },
            shape = RoundedCornerShape(ElovaireRadii.input),
            singleLine = true,
            placeholder = { Text(copy.placeholder) },
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
                    visible = isSearchUiActive,
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
                                onClick = resetSearchToMain,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lucide_x),
                            contentDescription = copy.clearSearch,
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

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.contentMode == SearchContentMode.AllSongs) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 20.dp,
                        top = topPadding + 8.dp,
                        end = 20.dp,
                    ),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                searchBar()
                SearchSongsResultsHeader(
                    resultCount = state.allMatchingSongs.size,
                    selected = state.searchSongSortMode,
                    expanded = state.showSearchSongSortOptions,
                    onToggleExpanded = {
                        onShowSearchSongSortOptionsChange(!state.showSearchSongSortOptions)
                    },
                    onSelect = { selectedMode ->
                        onSearchSongSortModeChange(selectedMode)
                        onShowSearchSongSortOptionsChange(false)
                    },
                )
                Box(modifier = Modifier.weight(1f)) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(ElovaireRadii.card),
                        color = MaterialTheme.colorScheme.surface,
                    ) {
                        LazyColumn(
                            state = allSongsListState,
                            overscrollEffect = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .ensureSingleItemRubberBand(allSongsListState),
                            contentPadding = PaddingValues(bottom = bottomPadding + 84.dp),
                        ) {
                            itemsIndexed(
                                items = state.allMatchingSongs,
                                key = { _, song -> song.id },
                            ) { index, song ->
                                PlaylistSongRow(
                                    song = song,
                                    isFavorite = song.id in favoriteSongIds,
                                    isCurrentSong = song.id == state.currentSongId,
                                    isPlaybackActive = state.isPlaybackActive,
                                    onClick = {
                                        onSongSelected(song, state.allMatchingSongs)
                                    },
                                    onToggleFavorite = { onToggleFavorite(song.id) },
                                    showDivider = index != state.allMatchingSongs.lastIndex,
                                )
                            }
                        }
                    }
                    FastScrollbar(
                        state = allSongsListState,
                        topInset = 8.dp,
                        bottomInset = bottomPadding + 48.dp,
                    )
                }
            }
        } else {
            LazyColumn(
                state = listState,
                overscrollEffect = null,
                modifier = Modifier
                    .fillMaxSize()
                    .ensureSingleItemRubberBand(listState),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = topPadding + 8.dp,
                    end = 20.dp,
                    bottom = bottomPadding + if (isSearchUiActive) 84.dp else 12.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                item {
                    searchBar()
                }
                item {
                    ElovaireAnimatedContent(
                        targetState = state.contentMode,
                        modifier = Modifier.fillMaxWidth(),
                        transitionSpec = {
                        when {
                            targetState == SearchContentMode.Discover -> {
                                (fadeIn(
                                    animationSpec = ElovaireMotion.contentFadeInSpec(delayMillis = 60),
                                ) + slideInVertically(
                                    animationSpec = ElovaireMotion.offsetSoft(durationMillis = ElovaireMotion.Medium),
                                    initialOffsetY = { it / 14 },
                                )) togetherWith (fadeOut(
                                    animationSpec = ElovaireMotion.contentFadeOutSpec(),
                                ) + slideOutVertically(
                                    animationSpec = ElovaireMotion.offsetSoft(durationMillis = ElovaireMotion.Fast),
                                    targetOffsetY = { -it / 18 },
                                ))
                            }

                            initialState == SearchContentMode.Results && targetState == SearchContentMode.AllSongs -> {
                                ElovaireMotion.fullScreenForwardEnter(
                                    initialOffsetX = { it / 10 },
                                ) togetherWith ElovaireMotion.fullScreenForwardExit()
                            }

                            initialState == SearchContentMode.AllSongs && targetState == SearchContentMode.Results -> {
                                ElovaireMotion.fullScreenBackEnter() togetherWith ElovaireMotion.fullScreenBackExit(
                                    targetOffsetX = { it / 10 },
                                )
                            }

                            initialState == SearchContentMode.Discover -> {
                                (fadeIn(animationSpec = ElovaireMotion.contentFadeInSpec()) +
                                    slideInVertically(
                                        animationSpec = ElovaireMotion.offsetSoft(durationMillis = ElovaireMotion.Standard),
                                        initialOffsetY = { it / 16 },
                                    )) togetherWith fadeOut(
                                    animationSpec = ElovaireMotion.contentFadeOutSpec(),
                                )
                            }

                            else -> ElovaireMotion.softContentTransform()
                        }
                        },
                        label = "SearchScreenContent",
                    ) { mode ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(18.dp),
                        ) {
                            when (mode) {
                                SearchContentMode.AllSongs -> {
                                    Spacer(modifier = Modifier)
                                }

                                SearchContentMode.Discover -> {
                                    if (state.recentSearches.isNotEmpty()) {
                                        SearchHistorySectionHeader(
                                            showClearAction = true,
                                            onClearHistory = onClearSearchHistory,
                                        )
                                        SearchHistoryListCard(
                                            entries = state.recentSearches.take(6),
                                            onAlbumSelected = { albumId ->
                                                libraryState.albums.firstOrNull { it.id == albumId }?.let { album ->
                                                    onAlbumSelected(album, ExpandOrigin(), false)
                                                }
                                            },
                                            onArtistSelected = onArtistSelected,
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 14.dp, bottom = 10.dp),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                            ) {
                                                Text(
                                                    text = searchCopy(language).nothingSearchedTitle,
                                                    style = MaterialTheme.typography.titleLarge,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                )
                                                Text(
                                                    text = searchCopy(language).nothingSearchedMessage,
                                                    style = secondaryBodyTextStyle(),
                                                    color = readableSecondaryTextColor(),
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.fillMaxWidth(0.74f),
                                                )
                                            }
                                        }
                                    }
                                    if (state.suggestedAlbums.isNotEmpty()) {
                                        FavoriteAlbumsModule(
                                            albums = state.suggestedAlbums,
                                            title = searchCopy(language).suggestedAlbumsTitle,
                                            subtitle = searchCopy(language).suggestedAlbumsSubtitle,
                                            iconResId = R.drawable.ic_lucide_eye,
                                            onAlbumSelected = { album, origin ->
                                                onAlbumSelected(album, origin, false)
                                            },
                                        )
                                    }
                                }

                                SearchContentMode.Results -> {
                                    if (matchingArtists.isNotEmpty()) {
                                        SectionTitleRow(
                                            title = commonUiCopy(language).artists,
                                            subtitle = searchCopy(language).matchingArtists(matchingArtists.size),
                                        )
                                        SearchHistoryListCard(
                                            entries = matchingArtists,
                                            onAlbumSelected = { albumId ->
                                                libraryState.albums.firstOrNull { it.id == albumId }?.let { album ->
                                                    onAlbumSelected(album, ExpandOrigin(), false)
                                                }
                                            },
                                            onArtistSelected = onArtistSelected,
                                        )
                                    }

                                    if (state.matchingAlbums.isNotEmpty()) {
                                        ModuleCard {
                                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                                SectionTitleRow(
                                                    title = commonUiCopy(language).albums,
                                                    subtitle = copy.matchingAlbums(state.matchingAlbums.size),
                                                    compact = true,
                                                )
                                                ArtistAlbumGallery(
                                                    albums = state.matchingAlbums,
                                                    onAlbumSelected = { album, origin ->
                                                        onAlbumSelected(album, origin, true)
                                                    },
                                                )
                                            }
                                        }
                                    }

                                    if (state.matchingSongs.isNotEmpty()) {
                                        SearchSongsPreviewHeader(
                                            resultCount = state.allMatchingSongs.size,
                                            showSeeAll = state.allMatchingSongs.size > state.matchingSongs.size,
                                            onShowAll = {
                                                focusManager.clearFocus(force = true)
                                                keyboardController?.hide()
                                                onSearchFieldFocusedChange(false)
                                                onShowAllSongResultsChange(true)
                                            },
                                        )
                                        Column {
                                            state.matchingSongs.forEachIndexed { index, song ->
                                                HomeRecentSongRow(
                                                    song = song,
                                                    isFavorite = song.id in favoriteSongIds,
                                                    onClick = {
                                                        onSongSelected(song, state.matchingSongs)
                                                    },
                                                    onToggleFavorite = { onToggleFavorite(song.id) },
                                                    showDivider = index != state.matchingSongs.lastIndex,
                                                )
                                            }
                                        }
                                    }

                                    if (state.matchingAlbums.isEmpty() && state.matchingSongs.isEmpty() && matchingArtists.isEmpty()) {
                                        EmptyStateCard(
                                            title = searchCopy(language).noResultsTitle,
                                            message = searchCopy(language).noResultsMessage(trimmedQuery),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun SearchQuickPick(
    album: Album,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(ElovaireRadii.module))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ArtworkImage(
            uri = album.artUri,
            title = album.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            cornerRadius = ElovaireRadii.pill,
            showArtworkGlow = true,
        )
        Text(
            text = album.title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = album.artist,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

internal fun searchMatchesComposite(
    query: String,
    fields: List<String>,
): Boolean {
    val normalizedQuery = query
        .trim()
        .lowercase()
    if (normalizedQuery.isBlank()) return true
    val tokens = normalizedQuery
        .split(Regex("\\s+"))
        .filter { it.isNotBlank() }
    if (tokens.isEmpty()) return true
    val haystack = fields
        .asSequence()
        .map { it.trim().lowercase() }
        .filter { it.isNotBlank() }
        .joinToString(separator = " ")
    return tokens.all { token -> haystack.contains(token) }
}

@Composable
internal fun SearchHistorySectionHeader(
    showClearAction: Boolean,
    onClearHistory: () -> Unit,
) {
    val language = LocalAppLanguage.current
    val copy = searchCopy(language)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = copy.recentlySearched,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface,
        )
        AnimatedVisibility(visible = showClearAction) {
            Surface(
                onClick = onClearHistory,
                shape = RoundedCornerShape(ElovaireRadii.pill),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                contentColor = if (MaterialTheme.colorScheme.primary.luminance() > 0.5f) InkText else Color.White,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lucide_trash_2),
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                    )
                    Text(
                        text = copy.clearHistory,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Composable
internal fun SearchSongsPreviewHeader(
    resultCount: Int,
    showSeeAll: Boolean,
    onShowAll: () -> Unit,
) {
    val language = LocalAppLanguage.current
    val copy = searchCopy(language)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SectionTitleRow(
            title = commonUiCopy(language).songs,
            subtitle = copy.matchingSongs(resultCount),
        )
        AnimatedVisibility(visible = showSeeAll) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onShowAll,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lucide_chevron_left),
                    contentDescription = "Show all song results",
                    tint = readableMutedIconColor().copy(alpha = 0.82f),
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(180f),
                )
            }
        }
    }
}

@Composable
internal fun SearchSongsResultsHeader(
    resultCount: Int,
    selected: SearchSongSortMode,
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    onSelect: (SearchSongSortMode) -> Unit,
) {
    val language = LocalAppLanguage.current
    val copy = searchCopy(language)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SectionTitleRow(
                title = commonUiCopy(language).songs,
                subtitle = copy.matchingSongs(resultCount),
                modifier = Modifier.weight(1f),
            )
            Surface(
                onClick = onToggleExpanded,
                shape = RoundedCornerShape(ElovaireRadii.pill),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lucide_arrow_down_up),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                    )
                    Text(
                        text = searchSortModeLabel(selected, language),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                    )
                }
            }
        }
        AnimatedVisibility(visible = expanded) {
            Surface(
                shape = RoundedCornerShape(ElovaireRadii.card),
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column {
                    SearchSongSortMode.entries.forEachIndexed { index, mode ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { onSelect(mode) },
                                )
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                        ) {
                            Text(
                                text = searchSortModeLabel(mode, language),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (mode == selected) FontWeight.SemiBold else FontWeight.Normal,
                                ),
                                color = if (mode == selected) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    readableSecondaryTextColor()
                                },
                            )
                        }
                        if (index != SearchSongSortMode.entries.lastIndex) {
                            DividerLine()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun SearchHistoryListCard(
    entries: List<SearchHistoryEntry>,
    onAlbumSelected: (Long) -> Unit,
    onArtistSelected: (String) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(ElovaireRadii.card),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column {
            entries.forEachIndexed { index, entry ->
                SearchHistoryListRow(
                    entry = entry,
                    onClick = {
                        when (entry.kind) {
                            SearchHistoryKind.Album -> entry.albumId?.let(onAlbumSelected)
                            SearchHistoryKind.Artist -> onArtistSelected(entry.query ?: entry.title)
                        }
                    },
                )
                if (index != entries.lastIndex) {
                    DividerLine()
                }
            }
        }
    }
}

@Composable
internal fun SearchHistoryListRow(
    entry: SearchHistoryEntry,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ArtworkImage(
            uri = entry.artUri,
            title = entry.title,
            modifier = Modifier.size(46.dp),
            cornerRadius = if (entry.kind == SearchHistoryKind.Artist) ElovaireRadii.pill else ElovaireRadii.artworkSmall,
            showArtworkGlow = entry.kind == SearchHistoryKind.Album,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = entry.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = entry.subtitle,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
internal fun SearchCategoryGrid(
    categories: List<Pair<String, Color>>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        categories.chunked(2).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                rowItems.forEach { (label, color) ->
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(148.dp),
                        color = color,
                        shape = RoundedCornerShape(ElovaireRadii.card),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(18.dp),
                            contentAlignment = Alignment.BottomStart,
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun LibraryModeToggle(
    layoutMode: AlbumLayoutMode,
    onLayoutModeChanged: (AlbumLayoutMode) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        ToggleIconChip(
            iconResId = R.drawable.ic_lucide_list,
            selected = layoutMode == AlbumLayoutMode.Compact,
            contentDescription = "Compact list",
            onClick = { onLayoutModeChanged(AlbumLayoutMode.Compact) },
        )
        ToggleIconChip(
            iconResId = R.drawable.ic_lucide_grid_2x2,
            selected = layoutMode == AlbumLayoutMode.Grid,
            contentDescription = "Grid",
            onClick = { onLayoutModeChanged(AlbumLayoutMode.Grid) },
        )
        ToggleIconChip(
            iconResId = R.drawable.ic_lucide_grid_3x3,
            selected = layoutMode == AlbumLayoutMode.DenseGrid,
            contentDescription = "Dense grid",
            onClick = { onLayoutModeChanged(AlbumLayoutMode.DenseGrid) },
        )
    }
}

@Composable
internal fun ToggleIconChip(
    iconResId: Int,
    selected: Boolean,
    contentDescription: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val contentColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.onSurface
        } else {
            readableMutedIconColor()
        },
        animationSpec = tween(ElovaireMotion.Quick),
        label = "toggle_chip_content",
    )
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.88f else 1f,
        animationSpec = if (pressed) {
            ElovaireMotion.pressDownSpec()
        } else {
            ElovaireMotion.bounceSpringSpec()
        },
        label = "toggle_chip_scale",
    )
    val iconScale by animateFloatAsState(
        targetValue = if (pressed) 0.92f else 1f,
        animationSpec = if (pressed) {
            ElovaireMotion.pressDownSpec()
        } else {
            ElovaireMotion.releaseSpringSpec(
                dampingRatio = 0.78f,
                stiffness = 520f,
            )
        },
        label = "toggle_chip_icon_scale",
    )
    Surface(
        modifier = Modifier.scale(scale),
        onClick = onClick,
        shape = RoundedCornerShape(ElovaireRadii.button),
        color = Color.Transparent,
        contentColor = contentColor,
        shadowElevation = 0.dp,
        interactionSource = interactionSource,
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(15.dp)
                    .scale(iconScale),
            )
        }
    }
}
