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
internal fun readableSecondaryTextColor(): Color {
    return if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        InkText.copy(alpha = 0.82f)
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.92f)
    }
}

@Composable
internal fun secondaryBodyTextStyle(): TextStyle {
    return MaterialTheme.typography.bodyLarge.copy(
        lineHeight = elovaireScaledSp(19.2f),
    )
}

@Composable
internal fun readableMutedIconColor(): Color {
    return if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        InkText.copy(alpha = 0.78f)
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
    }
}

@Composable
internal fun readableCardSurfaceColor(): Color {
    return MaterialTheme.colorScheme.surface
}

@Composable
internal fun readableCardBorderColor(): Color {
    return if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        InkText.copy(alpha = 0.1f)
    } else {
        Color.White.copy(alpha = 0.07f)
    }
}

@Composable
internal fun ModuleCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ElovaireRadii.module),
        color = readableCardSurfaceColor(),
        shadowElevation = 0.dp,
    ) {
        Box(
            modifier = Modifier
                .background(readableCardSurfaceColor())
                .border(
                    width = 1.dp,
                    color = readableCardBorderColor(),
                    shape = RoundedCornerShape(ElovaireRadii.module),
                )
                .padding(18.dp),
        ) {
            content()
        }
    }
}

@Composable
internal fun SectionTitleRow(
    title: String,
    subtitle: String? = null,
    compact: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 6.dp),
    ) {
        Text(
            text = title,
            style = if (compact) {
                MaterialTheme.typography.titleLarge.copy(fontSize = elovaireScaledSp(16f))
            } else {
                MaterialTheme.typography.headlineMedium
            },
        )
        if (!subtitle.isNullOrBlank()) {
            Text(
                text = subtitle,
                style = if (compact) {
                    MaterialTheme.typography.labelLarge
                } else {
                    secondaryBodyTextStyle()
                },
                color = readableSecondaryTextColor(),
            )
        }
    }
}

@Composable
internal fun MutedSectionHeader(
    title: String,
    iconResId: Int,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = readableMutedIconColor(),
            modifier = Modifier.size(15.dp),
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
internal fun FavoriteAlbumsModule(
    albums: List<Album>,
    title: String = "Your favorite albums",
    subtitle: String = "Music you come back to frequently",
    iconResId: Int = R.drawable.ic_lucide_star,
    onAlbumSelected: (Album, ExpandOrigin) -> Unit,
) {
    val darkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val backgroundColor = if (darkTheme) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.52f)
    } else {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
    }
    val borderColor = if (darkTheme) {
        Color.White.copy(alpha = 0.07f)
    } else {
        InkText.copy(alpha = 0.08f)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(ElovaireRadii.module))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(ElovaireRadii.module),
            )
            .padding(start = 14.dp, end = 14.dp, top = 16.dp, bottom = 14.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = iconResId),
                        contentDescription = null,
                        tint = readableMutedIconColor(),
                        modifier = Modifier
                            .size(18.dp),
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelLarge,
                            color = readableSecondaryTextColor(),
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                albums.chunked(2).take(3).forEach { rowAlbums ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        rowAlbums.forEach { album ->
                            FavoriteAlbumCompactCell(
                                album = album,
                                modifier = Modifier.weight(1f),
                                onOpen = { origin -> onAlbumSelected(album, origin) },
                            )
                        }
                        repeat((2 - rowAlbums.size).coerceAtLeast(0)) {
                            SpacerTile(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun FavoriteAlbumCompactCell(
    album: Album,
    modifier: Modifier = Modifier,
    onOpen: (ExpandOrigin) -> Unit,
) {
    val screenSizePx = screenContainerSizePx()
    val screenWidthPx = screenSizePx.width.toFloat()
    val screenHeightPx = screenSizePx.height.toFloat()
    val language = LocalAppLanguage.current
    var bounds by remember { mutableStateOf<androidx.compose.ui.geometry.Rect?>(null) }
    val cellColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)

    Surface(
        modifier = modifier
            .onGloballyPositioned { bounds = it.boundsInWindow() },
        shape = RoundedCornerShape(6.dp),
        color = cellColor,
        onClick = { onOpen(bounds.toExpandOrigin(screenWidthPx, screenHeightPx)) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ArtworkImage(
                uri = album.artUri,
                title = album.title,
                modifier = Modifier.size(48.dp),
                cornerRadius = ElovaireRadii.artworkSmall,
                showArtworkGlow = true,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = album.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 0.72f,
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = album.artist,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
internal fun CompactSongTile(
    song: Song,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        color = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.78f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.34f)
        },
        shape = RoundedCornerShape(ElovaireRadii.tile),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ArtworkImage(
                uri = song.artUri,
                title = song.album,
                modifier = Modifier.size(48.dp),
                cornerRadius = ElovaireRadii.artworkSmall,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                )
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.labelLarge,
                    color = readableSecondaryTextColor(),
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
internal fun SongGridCard(
    song: Song,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ArtworkImage(
            uri = song.artUri,
            title = song.album,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            cornerRadius = ElovaireRadii.artwork,
            showArtworkGlow = true,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.labelLarge,
                    color = readableSecondaryTextColor(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            InlineFavoriteSongButton(
                isFavorite = isFavorite,
                tint = MaterialTheme.colorScheme.onSurface,
                onClick = onToggleFavorite,
            )
        }
    }
}

@Composable
internal fun ArtistGridCard(
    artist: ArtistEntry,
    onClick: () -> Unit,
) {
    val language = LocalAppLanguage.current
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ArtworkImage(
            uri = artist.artUri,
            title = artist.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            cornerRadius = ElovaireRadii.pill,
        )
        Text(
            text = artist.name,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = "${localizedCountLabel(artist.albumCount, "album", language)}  •  ${localizedCountLabel(artist.songCount, "song", language)}",
            style = MaterialTheme.typography.labelLarge,
            color = readableSecondaryTextColor(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
internal fun ArtistRow(
    artist: ArtistEntry,
    onClick: () -> Unit,
) {
    val language = LocalAppLanguage.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ArtworkImage(
            uri = artist.artUri,
            title = artist.name,
            modifier = Modifier.size(50.dp),
            cornerRadius = ElovaireRadii.pill,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Text(
                text = artist.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${localizedCountLabel(artist.albumCount, "album", language)}  •  ${localizedCountLabel(artist.songCount, "song", language)}",
                style = MaterialTheme.typography.labelLarge,
                color = readableSecondaryTextColor(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
internal fun GenreRow(
    genre: GenreEntry,
    onClick: () -> Unit,
) {
    val language = LocalAppLanguage.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            shape = CircleShape,
            color = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.82f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.52f)
            },
            modifier = Modifier.size(44.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lucide_gallery_vertical_end),
                    contentDescription = null,
                    tint = readableMutedIconColor().copy(alpha = 0.9f),
                    modifier = Modifier.size(18.dp),
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Text(
                text = genre.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = localizedCountLabel(genre.albumCount, "album", language),
                style = MaterialTheme.typography.labelLarge,
                color = readableSecondaryTextColor(),
                maxLines = 1,
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_lucide_chevron_left),
            contentDescription = null,
            tint = readableMutedIconColor().copy(alpha = 0.5f),
            modifier = Modifier
                .size(18.dp)
                .rotate(180f),
        )
    }
}

@Composable
internal fun SpacerTile(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier)
}

@Composable
internal fun AlbumPosterGrid(
    albums: List<Album>,
    onAlbumSelected: (Album, ExpandOrigin) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        albums.chunked(2).forEach { rowAlbums ->
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                rowAlbums.forEach { album ->
                    AlbumGridCard(
                        album = album,
                        modifier = Modifier.weight(1f),
                        onOpen = { origin -> onAlbumSelected(album, origin) },
                    )
                }
                repeat((2 - rowAlbums.size).coerceAtLeast(0)) {
                    SpacerTile(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
internal fun RecentSongRow(
    song: Song,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        color = readableCardSurfaceColor(),
        shape = RoundedCornerShape(ElovaireRadii.card),
        shadowElevation = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) 6.dp else 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ArtworkImage(
                uri = song.artUri,
                title = song.album,
                modifier = Modifier.size(52.dp),
                cornerRadius = ElovaireRadii.artwork,
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                )
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodyLarge,
                    color = readableSecondaryTextColor(),
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
internal fun ExplicitTitleText(
    title: String,
    isExplicit: Boolean,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    val badgeFontSize = remember(style.fontSize) {
        if (style.fontSize == androidx.compose.ui.unit.TextUnit.Unspecified) 10.sp else (style.fontSize.value * 0.56f).sp
    }
    val titleText = remember(title, isExplicit) {
        buildAnnotatedString {
            append(title)
            if (isExplicit) {
                append(" ")
                pushStyle(
                    SpanStyle(
                        fontSize = badgeFontSize,
                        fontWeight = FontWeight.Medium,
                    ),
                )
                append("🅴")
                pop()
            }
        }
    }
    Text(
        text = titleText,
        style = style,
        color = color,
        maxLines = maxLines,
        overflow = overflow,
        modifier = modifier,
    )
}

@Composable
internal fun SearchSongRow(
    song: Song,
    onClick: () -> Unit,
    showDivider: Boolean,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
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
                modifier = Modifier.width(78.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = formatDuration(song.durationMs),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f),
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

@Composable
internal fun HomeRecentSongRow(
    song: Song,
    isFavorite: Boolean,
    isCurrentSong: Boolean = false,
    isPlaybackActive: Boolean = false,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    showDivider: Boolean,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 2.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.Center,
            ) {
                ArtworkImage(
                    uri = song.artUri,
                    title = song.title,
                    modifier = Modifier.matchParentSize(),
                    cornerRadius = ElovaireRadii.artworkSmall,
                    showArtworkGlow = true,
                )
                androidx.compose.animation.AnimatedVisibility(
                    visible = isCurrentSong && isPlaybackActive,
                    enter = fadeIn(animationSpec = tween(60)),
                    exit = fadeOut(animationSpec = tween(60)),
                ) {
                    PlaybackActiveArtworkOverlay(
                        uri = song.artUri,
                        title = song.title,
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
                modifier = Modifier.width(100.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = formatDuration(song.durationMs),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1f)
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

@Composable
internal fun RecentAlbumGrid(
    albums: List<Album>,
    onAlbumSelected: (Album, ExpandOrigin) -> Unit,
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        overscrollEffect = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(378.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        items(albums, key = { it.id }) { album ->
            AlbumGridCard(
                album = album,
                modifier = Modifier.width(164.dp),
                onOpen = { origin -> onAlbumSelected(album, origin) },
            )
        }
    }
}

@Composable
internal fun SelectionIndicatorIcon(
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val tint = MaterialTheme.colorScheme.onSurface
    Box(
        modifier = modifier.size(20.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(tint),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lucide_check),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(12.dp),
                )
            }
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_lucide_circle),
                contentDescription = null,
                tint = tint.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
internal fun AlbumGridCard(
    album: Album,
    modifier: Modifier = Modifier,
    selectionMode: Boolean = false,
    selected: Boolean = false,
    showText: Boolean = true,
    artworkCornerRadius: Dp = ElovaireRadii.artwork,
    showArtworkGlow: Boolean = true,
    onOpen: (ExpandOrigin) -> Unit,
    onLongPress: (() -> Unit)? = null,
) {
    val screenSizePx = screenContainerSizePx()
    val screenWidthPx = screenSizePx.width.toFloat()
    val screenHeightPx = screenSizePx.height.toFloat()
    val language = LocalAppLanguage.current
    var bounds by remember { mutableStateOf<androidx.compose.ui.geometry.Rect?>(null) }

    Column(
        modifier = modifier
            .onGloballyPositioned { bounds = it.boundsInWindow() }
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onOpen(bounds.toExpandOrigin(screenWidthPx, screenHeightPx)) },
                onLongClick = { onLongPress?.invoke() },
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        ) {
            ArtworkImage(
                uri = album.artUri,
                title = album.title,
                modifier = Modifier.matchParentSize(),
                cornerRadius = artworkCornerRadius,
                showArtworkGlow = showArtworkGlow,
            )
            if (selectionMode) {
                SelectionIndicatorIcon(
                    selected = selected,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                )
            }
        }
        if (showText) {
            Column(
                modifier = Modifier.padding(horizontal = 2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
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
            }
        }
    }
}

@Composable
internal fun CompactAlbumRow(
    album: Album,
    selectionMode: Boolean = false,
    selected: Boolean = false,
    isFavorite: Boolean = false,
    showFavoriteButton: Boolean = false,
    playlists: List<Playlist> = emptyList(),
    playlistSongsById: Map<Long, Song> = emptyMap(),
    onOpen: (ExpandOrigin) -> Unit,
    onToggleFavorite: (() -> Unit)? = null,
    onAddToQueue: (() -> Unit)? = null,
    onAddToPlaylist: ((Long) -> Unit)? = null,
    onCreatePlaylist: ((String) -> Long)? = null,
    onDeleteAlbum: (() -> Unit)? = null,
    onLongPress: (() -> Unit)? = null,
) {
    val screenSizePx = screenContainerSizePx()
    val screenWidthPx = screenSizePx.width.toFloat()
    val screenHeightPx = screenSizePx.height.toFloat()
    val language = LocalAppLanguage.current
    var bounds by remember { mutableStateOf<androidx.compose.ui.geometry.Rect?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { bounds = it.boundsInWindow() }
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onOpen(bounds.toExpandOrigin(screenWidthPx, screenHeightPx)) },
                onLongClick = { onLongPress?.invoke() },
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(62.dp),
            ) {
                ArtworkImage(
                    uri = album.artUri,
                    title = album.title,
                    modifier = Modifier.matchParentSize(),
                    cornerRadius = ElovaireRadii.artworkSmall,
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp),
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
                            append(localizedCountLabel(album.songCount, "track", language))
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
            if (selectionMode) {
                Box(
                    modifier = Modifier.padding(end = 10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    SelectionIndicatorIcon(selected = selected)
                }
            } else if (showFavoriteButton && onToggleFavorite != null) {
                AnimatedVisibility(
                    visible = !selectionMode,
                    enter = fadeIn(animationSpec = ElovaireMotion.fadeMedium()),
                    exit = fadeOut(animationSpec = ElovaireMotion.fadeFast()),
                ) {
                    Row(
                        modifier = Modifier.padding(end = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        InlineFavoriteSongButton(
                            isFavorite = isFavorite,
                            tint = MaterialTheme.colorScheme.onSurface,
                            onClick = onToggleFavorite,
                        )
                        if (onAddToQueue != null && onAddToPlaylist != null && onDeleteAlbum != null) {
                            AlbumOverflowMenuButton(
                                album = album,
                                playlists = playlists,
                                playlistSongsById = playlistSongsById,
                                tint = MaterialTheme.colorScheme.onSurface,
                                onAddToQueue = onAddToQueue,
                                onAddToPlaylist = onAddToPlaylist,
                                onCreatePlaylist = onCreatePlaylist,
                                onDeleteAlbum = onDeleteAlbum,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun EmptyStateCard(
    title: String,
    message: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ElovaireRadii.card),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = readableSecondaryTextColor(),
            )
        }
    }
}

@Composable
internal fun PlaylistLaneCard(
    title: String,
    detail: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(ElovaireRadii.module),
        color = readableCardSurfaceColor(),
        shadowElevation = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) 8.dp else 8.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(ElovaireRadii.artworkSmall))
                    .background(
                        if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.84f)
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f)
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lucide_play),
                    contentDescription = null,
                    tint = readableMutedIconColor(),
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = detail,
                style = MaterialTheme.typography.bodyLarge,
                color = readableSecondaryTextColor(),
            )
        }
    }
}
