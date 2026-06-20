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
internal fun SettingsScreen(
    themeMode: ThemeMode,
    textSizePreset: TextSizePreset,
    appLanguage: AppLanguage,
    eqSettings: EqSettings,
    bottomPadding: Dp,
    isJellyfinConnected: Boolean = false,
    jellyfinHost: String = "",
    onBack: () -> Unit,
    onThemeModeSelected: (ThemeMode) -> Unit,
    onTextSizePresetSelected: (TextSizePreset) -> Unit,
    onAppLanguageSelected: (AppLanguage) -> Unit,
    onBassChanged: (Float) -> Unit,
    onMidrangeChanged: (Float) -> Unit,
    onTrebleChanged: (Float) -> Unit,
    onMonoPlaybackChanged: (Boolean) -> Unit,
    onOpenEqualizer: () -> Unit,
    onOpenJellyfinSetup: () -> Unit,
    onOpenChangelog: () -> Unit,
    onScanLibrary: () -> Unit,
    onCheckForUpdates: () -> Unit,
) {
    val listState = rememberElovaireLazyListState("settings_screen")
    val copy = remember(appLanguage) { settingsCopy(appLanguage) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        LazyColumn(
            state = listState,
            overscrollEffect = null,
            modifier = Modifier
                .fillMaxSize()
                .ensureSingleItemRubberBand(listState),
            contentPadding = PaddingValues(
                start = 18.dp,
                top = topBarOccupiedHeight() + 8.dp,
                end = 18.dp,
                bottom = bottomPadding + buttonNavigationScrollBoost(),
            ),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item {
                JellyfinStatusCard(
                    isConnected = isJellyfinConnected,
                    host = jellyfinHost,
                    onSetup = onOpenJellyfinSetup,
                )
            }

            item {
                SettingsSectionHeader(
                    title = copy.appearance,
                    iconResId = R.drawable.ic_lucide_palette,
                )
            }

            item {
                ModuleCard {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        SectionTitleRow(
                            title = copy.theme,
                            compact = true,
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            ThemeModeSegmentedPicker(
                                selectedMode = themeMode,
                                onModeSelected = onThemeModeSelected,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 2.dp),
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            SectionTitleRow(
                                title = copy.textSize,
                                compact = true,
                            )
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center,
                            ) {
                                TextSizeStepper(
                                    selectedPreset = textSizePreset,
                                    onPresetSelected = onTextSizePresetSelected,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 2.dp),
                                )
                            }
                        }
                        LanguagePickerRow(
                            selectedLanguage = appLanguage,
                            copy = copy,
                            onLanguageSelected = onAppLanguageSelected,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }

            item {
                SettingsSectionHeader(
                    title = copy.sound,
                    iconResId = R.drawable.ic_lucide_volume_2,
                )
            }

            item {
                ModuleCard {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(30.dp),
                        ) {
                            EqToneKnob(
                                title = uiPhrase(appLanguage, UiPhrase.Bass),
                                value = eqSettings.bass.coerceIn(0f, 1f),
                                valueRange = 0f..1f,
                                accentColor = Color(0xFF2FE08D),
                                modifier = Modifier.weight(1f),
                                onValueChange = onBassChanged,
                            )
                            EqToneKnob(
                                title = uiPhrase(appLanguage, UiPhrase.Midrange),
                                value = eqSettings.midrange.coerceIn(-1f, 1f),
                                valueRange = -1f..1f,
                                accentColor = Color(0xFF39C2FF),
                                modifier = Modifier.weight(1f),
                                onValueChange = onMidrangeChanged,
                            )
                            EqToneKnob(
                                title = uiPhrase(appLanguage, UiPhrase.Treble),
                                value = eqSettings.treble.coerceIn(-1f, 1f),
                                valueRange = -1f..1f,
                                accentColor = Color(0xFFFFB056),
                                modifier = Modifier.weight(1f),
                                onValueChange = onTrebleChanged,
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 2.dp),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            val interactionSource = remember { MutableInteractionSource() }
                            Surface(
                                modifier = Modifier.elovairePressBounce(
                                    interactionSource = interactionSource,
                                    label = "settings_equalizer_button_scale",
                                ),
                                shape = RoundedCornerShape(ElovaireRadii.pill),
                                color = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                            ) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(ElovaireRadii.pill))
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null,
                                            onClick = onOpenEqualizer,
                                        )
                                        .padding(horizontal = 18.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_lucide_audio_waveform),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                    )
                                    Text(
                                        text = copy.equalizer,
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        SettingToggleRow(
                            title = copy.enableMono,
                            subtitle = copy.monoSubtitle,
                            enabled = eqSettings.monoEnabled,
                            onEnabledChanged = onMonoPlaybackChanged,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 2.dp)
                                .align(Alignment.CenterHorizontally),
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 2.dp),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            val interactionSource = remember { MutableInteractionSource() }
                            Surface(
                                modifier = Modifier.elovairePressBounce(
                                    interactionSource = interactionSource,
                                    label = "settings_jellyfin_button_scale",
                                ),
                                shape = RoundedCornerShape(ElovaireRadii.pill),
                                color = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary,
                            ) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(ElovaireRadii.pill))
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null,
                                            onClick = onOpenJellyfinSetup,
                                        )
                                        .padding(horizontal = 18.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_lucide_settings_2),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                    )
                                    Text(
                                        text = "Jellyfin Setup",
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                SettingsSectionHeader(
                    title = copy.otherSettings,
                    iconResId = R.drawable.ic_lucide_settings,
                )
            }

            item {
                ModuleCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        SettingActionRow(
                            title = copy.scanLibrary,
                            subtitle = copy.scanLibrarySubtitle,
                            actionLabel = copy.scan,
                            onAction = onScanLibrary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 2.dp),
                        )
                        SettingActionRow(
                            title = copy.checkUpdates,
                            subtitle = copy.checkUpdatesSubtitle,
                            actionLabel = copy.check,
                            onAction = onCheckForUpdates,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 2.dp),
                        )
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)),
                    )
                    Column(
                        modifier = Modifier.padding(top = 9.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Text("JellyMusic", style = MaterialTheme.typography.titleLarge)
                            Surface(
                                shape = RoundedCornerShape(ElovaireRadii.pill),
                                color = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) {
                                    Color.White.copy(alpha = 0.1f)
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                                },
                                contentColor = MaterialTheme.colorScheme.onSurface,
                                onClick = onOpenChangelog,
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Text(
                                        text = copy.changelog,
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_lucide_chevron_left),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(14.dp)
                                            .rotate(180f),
                                    )
                                }
                            }
                        }
                        Text(
                            text = commonUiCopy(appLanguage).refinedFooter,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        )
                    }
                }
            }
        }
        PinnedBackTopBar(
            title = copy.settings,
            onBack = onBack,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Composable
internal fun LanguagePickerRow(
    selectedLanguage: AppLanguage,
    copy: SettingsLanguageCopy,
    modifier: Modifier = Modifier,
    onLanguageSelected: (AppLanguage) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(
                text = copy.language,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = elovaireScaledSp(16f),
                    fontWeight = FontWeight.SemiBold,
                ),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = copy.currentlyUsed,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            )
        }
        Box {
            val interactionSource = remember { MutableInteractionSource() }
            Surface(
                modifier = Modifier.elovairePressBounce(
                    interactionSource = interactionSource,
                    label = "settings_language_button_scale",
                ),
                shape = RoundedCornerShape(ElovaireRadii.pill),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(ElovaireRadii.pill))
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { expanded = true },
                        )
                        .padding(horizontal = 14.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lucide_languages),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = selectedLanguage.nativeName,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
            }
        }
    }
    if (expanded) {
        LanguageSelectionDialog(
            selectedLanguage = selectedLanguage,
            title = copy.language,
            onDismiss = { expanded = false },
            onConfirm = { language ->
                expanded = false
                onLanguageSelected(language)
            },
        )
    }
}

@Composable
internal fun LanguageSelectionDialog(
    selectedLanguage: AppLanguage,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (AppLanguage) -> Unit,
) {
    val listState = rememberElovaireLazyListState("language_picker")
    val languages = remember {
        AppLanguage.entries.sortedBy { it.englishName }
    }
    var pendingLanguage by rememberSaveable(selectedLanguage) { mutableStateOf(selectedLanguage) }
    val visibleRows = 5
    val rowHeight = 56.dp
    val rowSpacing = 2.dp
    val listHeight = (rowHeight * visibleRows) + (rowSpacing * (visibleRows - 1))

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
                            painter = painterResource(id = R.drawable.ic_lucide_languages),
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(listHeight),
                    ) {
                        LazyColumn(
                            state = listState,
                            overscrollEffect = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .ensureSingleItemRubberBand(listState),
                            verticalArrangement = Arrangement.spacedBy(rowSpacing),
                        ) {
                            items(languages, key = { it.name }) { language ->
                                LanguagePickerOptionRow(
                                    language = language,
                                    selected = language == pendingLanguage,
                                    modifier = Modifier.animateItem(
                                        placementSpec = spring(
                                            dampingRatio = 0.76f,
                                            stiffness = 360f,
                                        ),
                                    ),
                                    onClick = { pendingLanguage = language },
                                )
                            }
                        }
                        FastScrollbar(
                            state = listState,
                            topInset = 0.dp,
                            bottomInset = 0.dp,
                            modifier = Modifier.padding(end = 2.dp),
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = uiPhrase(selectedLanguage, UiPhrase.Cancel),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Surface(
                            onClick = { onConfirm(pendingLanguage) },
                            shape = RoundedCornerShape(ElovaireRadii.pill),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.92f),
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ) {
                            Text(
                                text = "OK",
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
internal fun LanguagePickerOptionRow(
    language: AppLanguage,
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
        label = "language_picker_row_highlight",
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(end = 16.dp)
            .clip(RoundedCornerShape(ElovaireRadii.tile))
            .background(highlightColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(22.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lucide_circle),
                    contentDescription = null,
                    tint = if (selected) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.94f)
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.42f)
                    },
                    modifier = Modifier.size(20.dp),
                )
                androidx.compose.animation.AnimatedVisibility(
                    visible = selected,
                    enter = fadeIn(animationSpec = tween(40)) + scaleIn(
                        initialScale = 0.8f,
                        animationSpec = ElovaireMotion.releaseSpringSpec(),
                    ),
                    exit = fadeOut(animationSpec = tween(20)) + scaleOut(
                        targetScale = 0.8f,
                        animationSpec = tween(20),
                    ),
                    label = "language_picker_check",
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lucide_check),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.94f),
                        modifier = Modifier.size(12.dp),
                    )
                }
            }
            Text(
                text = language.nativeName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                ),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
internal fun UpdateAvailableBanner(
    release: AppReleaseInfo,
    uiState: AppUpdateUiState,
    onDismiss: () -> Unit,
    onUpdate: () -> Unit,
) {
    val darkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val primaryTextColor = if (darkTheme) Color.White else InkText
    val secondaryTextColor = if (darkTheme) {
        Color.White.copy(alpha = 0.7f)
    } else {
        InkText.copy(alpha = 0.7f)
    }
    DynamicBackdropSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ElovaireRadii.pill),
        overlayAlpha = 0.7f,
        borderColor = blurSurfaceBorderColor(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 12.dp, top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismiss,
                    ),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Update available",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 22.sp,
                        ),
                        color = primaryTextColor,
                    )
                    Text(
                        text = release.versionName,
                        style = MaterialTheme.typography.labelLarge,
                        color = secondaryTextColor,
                    )
                }
            }
            Surface(
                onClick = onUpdate,
                shape = RoundedCornerShape(ElovaireRadii.pill),
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                enabled = !uiState.isInstalling,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = when {
                            uiState.isInstalling -> "Installing"
                            uiState.isDownloading -> {
                                val percent = ((uiState.downloadProgress ?: 0f) * 100f).roundToInt()
                                "Downloading $percent%"
                            }
                            else -> "Download"
                        },
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lucide_download),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

@Composable
internal fun SettingsSectionHeader(
    title: String,
    iconResId: Int,
) {
    Row(
        modifier = Modifier.padding(top = 6.dp, start = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.92f),
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.92f),
        )
    }
}

@Composable
internal fun TextSizeStepper(
    selectedPreset: TextSizePreset,
    onPresetSelected: (TextSizePreset) -> Unit,
    modifier: Modifier = Modifier,
) {
    val presets = TextSizePreset.entries
    val currentSelectedPreset by rememberUpdatedState(selectedPreset)
    val currentOnPresetSelected by rememberUpdatedState(onPresetSelected)
    val selectedIndex = presets.indexOf(selectedPreset).coerceAtLeast(0)
    val maxIndex = (presets.size - 1).coerceAtLeast(1)
    val knobSize = 20.dp
    val dotColor = MaterialTheme.colorScheme.onSurface
    val knobColor = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        InkText
    } else {
        Color.White
    }
    val lineColor = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        InkText.copy(alpha = 0.18f)
    } else {
        Color.White.copy(alpha = 0.2f)
    }
    var isDragging by remember { mutableStateOf(false) }
    var dragCenterPx by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .horizontalGestureSafe(),
        ) {
            val density = LocalDensity.current
            val maxWidthPx = with(density) { maxWidth.toPx() }
            val stepFraction = selectedIndex.toFloat() / maxIndex.toFloat()
            val knobSizePx = with(density) { knobSize.toPx() }
            val selectedCenterPx = maxWidthPx * stepFraction
            val stepCenters = remember(maxWidthPx, maxIndex) {
                presets.indices.map { index ->
                    if (maxIndex == 0) {
                        maxWidthPx / 2f
                    } else {
                        maxWidthPx * (index.toFloat() / maxIndex.toFloat())
                    }
                }
            }
            LaunchedEffect(selectedCenterPx, maxWidthPx) {
                if (!isDragging) {
                    dragCenterPx = selectedCenterPx
                }
            }
            val knobOffset by animateDpAsState(
                targetValue = with(density) {
                    ((if (isDragging) dragCenterPx else selectedCenterPx) - (knobSizePx / 2f)).toDp()
                },
                animationSpec = if (isDragging) {
                    tween(durationMillis = 60)
                } else {
                    spring(
                        dampingRatio = 0.82f,
                        stiffness = 480f,
                    )
                },
                label = "text_size_knob_offset",
            )
            val updateFromPosition: (Float) -> Unit = { xPosition ->
                val clampedX = xPosition.coerceIn(0f, maxWidthPx)
                dragCenterPx = clampedX
                val targetIndex = stepCenters
                    .withIndex()
                    .minByOrNull { (_, center) -> kotlin.math.abs(center - clampedX) }
                    ?.index
                    ?: presets.indexOf(currentSelectedPreset).coerceAtLeast(0)
                val preset = presets[targetIndex]
                if (preset != currentSelectedPreset) {
                    currentOnPresetSelected(preset)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(maxWidthPx) {
                        detectTapGestures { offset ->
                            if (maxWidthPx > 0f) {
                                updateFromPosition(offset.x)
                            }
                        }
                    }
                    .pointerInput(maxWidthPx, presets.size) {
                        detectHorizontalDragGestures(
                            onDragStart = { offset ->
                                isDragging = true
                                if (maxWidthPx > 0f) {
                                    updateFromPosition(offset.x)
                                }
                            },
                            onHorizontalDrag = { change, _ ->
                                if (maxWidthPx > 0f) {
                                    change.consume()
                                    updateFromPosition(change.position.x)
                                }
                            },
                            onDragEnd = {
                                isDragging = false
                            },
                            onDragCancel = {
                                isDragging = false
                            },
                        )
                    },
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .height(2.dp)
                        .clip(RoundedCornerShape(ElovaireRadii.pill))
                        .background(lineColor),
                )

                Canvas(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val selectedDotRadius = 3.5.dp.toPx()
                    val defaultDotRadius = 2.5.dp.toPx()
                    val centerY = size.height / 2f
                    presets.forEachIndexed { index, _ ->
                        val fraction = if (maxIndex == 0) 0f else index.toFloat() / maxIndex.toFloat()
                        drawCircle(
                            color = dotColor,
                            radius = if (index == selectedIndex) selectedDotRadius else defaultDotRadius,
                            center = Offset(size.width * fraction, centerY),
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .offset { IntOffset(x = knobOffset.roundToPx(), y = 0) }
                        .size(knobSize)
                        .clip(CircleShape)
                        .background(knobColor)
                        .align(Alignment.CenterStart),
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lucide_case_sensitive),
                contentDescription = "Smaller text",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = selectedPreset.name,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f),
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_lucide_a_large_small),
                contentDescription = "Larger text",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(17.dp),
            )
        }
    }
}

internal fun ReverbProfile.displayLabel(): String {
    return when (this) {
        ReverbProfile.Dry -> "Dry"
        ReverbProfile.Wet -> "Wet"
    }
}

@Composable
internal fun ReverbStepSlider(
    valueMs: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val steps = remember { (0..500 step 50).toList() }
    val currentOnValueChange by rememberUpdatedState(onValueChange)
    val selectedValue = steps.minByOrNull { kotlin.math.abs(it - valueMs.coerceIn(0, 500)) } ?: 0
    val selectedIndex = steps.indexOf(selectedValue).coerceAtLeast(0)
    val maxIndex = (steps.size - 1).coerceAtLeast(1)
    val knobSize = 20.dp
    val dotColor = MaterialTheme.colorScheme.onSurface
    val knobColor = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        InkText
    } else {
        Color.White
    }
    val lineColor = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        InkText.copy(alpha = 0.18f)
    } else {
        Color.White.copy(alpha = 0.2f)
    }
    var isDragging by remember { mutableStateOf(false) }
    var dragCenterPx by remember { mutableFloatStateOf(0f) }

    BoxWithConstraints(
        modifier = modifier
            .height(36.dp)
            .horizontalGestureSafe(),
    ) {
        val density = LocalDensity.current
        val maxWidthPx = with(density) { maxWidth.toPx() }
        val knobSizePx = with(density) { knobSize.toPx() }
        val trackStartPx = knobSizePx / 2f
        val trackWidthPx = (maxWidthPx - knobSizePx).coerceAtLeast(1f)
        val selectedCenterPx = trackStartPx + (trackWidthPx * (selectedIndex.toFloat() / maxIndex.toFloat()))
        val stepCenters = remember(maxWidthPx, maxIndex) {
            steps.indices.map { index ->
                if (maxIndex == 0) {
                    maxWidthPx / 2f
                } else {
                    trackStartPx + (trackWidthPx * (index.toFloat() / maxIndex.toFloat()))
                }
            }
        }
        LaunchedEffect(selectedCenterPx, maxWidthPx) {
            if (!isDragging) {
                dragCenterPx = selectedCenterPx
            }
        }
        val knobOffset by animateDpAsState(
            targetValue = with(density) {
                ((if (isDragging) dragCenterPx else selectedCenterPx) - (knobSizePx / 2f)).toDp()
            },
            animationSpec = if (isDragging) {
                tween(durationMillis = 60)
            } else {
                spring(
                    dampingRatio = 0.82f,
                    stiffness = 480f,
                )
            },
            label = "reverb_step_knob_offset",
        )
        val updateFromPosition: (Float) -> Unit = { xPosition ->
            val clampedX = xPosition.coerceIn(trackStartPx, trackStartPx + trackWidthPx)
            dragCenterPx = clampedX
            val targetIndex = stepCenters
                .withIndex()
                .minByOrNull { (_, center) -> kotlin.math.abs(center - clampedX) }
                ?.index
                ?: selectedIndex
            val targetValue = steps[targetIndex]
            if (targetValue != selectedValue) {
                currentOnValueChange(targetValue)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(maxWidthPx) {
                    detectTapGestures { offset ->
                        if (maxWidthPx > 0f) {
                            updateFromPosition(offset.x)
                        }
                    }
                }
                .pointerInput(maxWidthPx, steps.size) {
                    detectHorizontalDragGestures(
                        onDragStart = { offset ->
                            isDragging = true
                            if (maxWidthPx > 0f) {
                                updateFromPosition(offset.x)
                            }
                        },
                        onHorizontalDrag = { change, _ ->
                            if (maxWidthPx > 0f) {
                                change.consume()
                                updateFromPosition(change.position.x)
                            }
                        },
                        onDragEnd = { isDragging = false },
                        onDragCancel = { isDragging = false },
                    )
                },
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = with(density) { trackStartPx.toDp() })
                    .width(with(density) { trackWidthPx.toDp() })
                    .height(2.dp)
                    .clip(RoundedCornerShape(ElovaireRadii.pill))
                    .background(lineColor),
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                val selectedDotRadius = 3.5.dp.toPx()
                val defaultDotRadius = 2.5.dp.toPx()
                val centerY = size.height / 2f
                    steps.forEachIndexed { index, _ ->
                        val fraction = if (maxIndex == 0) 0f else index.toFloat() / maxIndex.toFloat()
                        drawCircle(
                            color = dotColor,
                            radius = if (index == selectedIndex) selectedDotRadius else defaultDotRadius,
                            center = Offset(trackStartPx + (trackWidthPx * fraction), centerY),
                        )
                    }
                }

            Box(
                modifier = Modifier
                    .offset { IntOffset(x = knobOffset.roundToPx(), y = 0) }
                    .size(knobSize)
                    .clip(CircleShape)
                    .background(knobColor)
                    .align(Alignment.CenterStart),
            )
        }
    }
}

@Composable
internal fun SettingToggleRow(
    title: String,
    subtitle: String,
    enabled: Boolean,
    onEnabledChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
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
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            )
        }
        Spacer(modifier = Modifier.width(18.dp))
        MonoPlaybackToggle(
            checked = enabled,
            onCheckedChange = onEnabledChanged,
        )
    }
}

@Composable
internal fun SettingActionRow(
    title: String,
    subtitle: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
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
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            )
        }
        Spacer(modifier = Modifier.width(18.dp))
        Surface(
            modifier = Modifier.elovairePressBounce(
                interactionSource = interactionSource,
                label = "${actionLabel}_setting_action_scale",
            ),
            shape = RoundedCornerShape(ElovaireRadii.pill),
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Text(
                text = actionLabel,
                modifier = Modifier
                    .clip(RoundedCornerShape(ElovaireRadii.pill))
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onAction,
                    )
                    .padding(horizontal = 16.dp, vertical = 9.dp),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            )
        }
    }
}

@Composable
internal fun MonoPlaybackToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val knobColor = if (checked) {
        Color.White
    } else if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        InkText
    } else {
        Color.White
    }
    val trackColor by animateColorAsState(
        targetValue = if (checked) {
            ToggleEnabledGreen
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) 0.16f else 0.2f)
        },
        animationSpec = tween(60),
        label = "mono_toggle_track",
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 18.dp else 2.dp,
        animationSpec = spring(dampingRatio = 0.82f, stiffness = 420f),
        label = "mono_toggle_thumb_offset",
    )
    Surface(
        onClick = { onCheckedChange(!checked) },
        shape = RoundedCornerShape(percent = 50),
        color = trackColor,
        contentColor = knobColor,
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(24.dp),
        ) {
            Box(
                modifier = Modifier
                    .offset(x = thumbOffset, y = 2.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(knobColor),
            )
        }
    }
}

@Composable
internal fun ThemeModeSegmentedPicker(
    selectedMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val language = LocalAppLanguage.current
    val common = remember(language) { commonUiCopy(language) }
    val options = listOf(ThemeMode.Light, ThemeMode.Dark, ThemeMode.System)
    BoxWithConstraints(
        modifier = modifier
            .height(46.dp)
            .horizontalGestureSafe()
            .clip(RoundedCornerShape(percent = 50))
            .background(
                if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.96f)
                } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                },
            )
            .padding(5.dp),
    ) {
        val selectedIndex = options.indexOf(selectedMode).coerceAtLeast(0)
        val segmentWidth = maxWidth / options.size
        val indicatorOffset by animateDpAsState(
            targetValue = segmentWidth * selectedIndex,
            animationSpec = spring(
                dampingRatio = 0.82f,
                stiffness = 420f,
            ),
            label = "theme_picker_offset",
        )
        val indicatorColor = MaterialTheme.colorScheme.primary

        Box(
            modifier = Modifier
                .offset { IntOffset(x = indicatorOffset.roundToPx(), y = 0) }
                .width(segmentWidth)
                .fillMaxHeight()
                .clip(RoundedCornerShape(percent = 50))
                .background(indicatorColor),
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            options.forEach { option ->
                val selected = option == selectedMode
                val iconResId = when (option) {
                    ThemeMode.Light -> R.drawable.ic_lucide_sun
                    ThemeMode.Dark -> R.drawable.ic_lucide_moon
                    ThemeMode.System -> R.drawable.ic_lucide_settings_2
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(percent = 50))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onModeSelected(option) },
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(id = iconResId),
                            contentDescription = null,
                            tint = if (selected) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f)
                            },
                            modifier = Modifier.size(14.dp),
                        )
                        Text(
                            text = when (option) {
                                ThemeMode.Light -> common.light
                                ThemeMode.Dark -> common.dark
                                ThemeMode.System -> common.system
                            },
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                            color = if (selected) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun DigitalSoundKnob(
    title: String,
    iconResId: Int,
    value: Float,
    modifier: Modifier = Modifier,
    onValueChange: (Float) -> Unit,
) {
    var dragValue by remember(value) { mutableFloatStateOf(value.coerceIn(0f, 1f)) }
    LaunchedEffect(value) {
        dragValue = value.coerceIn(0f, 1f)
    }
    val animatedValue by animateFloatAsState(
        targetValue = dragValue,
        animationSpec = tween(ElovaireMotion.Standard),
        label = "${title}_sound_knob",
    )
    val glowColor = Color(0xFF61F6A2)
    val inactiveDot = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.26f)
    val trackColor = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        InkText.copy(alpha = 0.3f)
    } else {
        Color.White.copy(alpha = 0.3f)
    }
    val activeArcColor = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        InkText
    } else {
        Color.White
    }
    val tickColor = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        InkText.copy(alpha = 0.2f)
    } else {
        Color.White.copy(alpha = 0.2f)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(134.dp)
                .horizontalGestureSafe()
                .pointerInput(title) {
                    detectTapGestures { offset ->
                        val widthPx = size.width.toFloat().coerceAtLeast(1f)
                        val horizontalInsetPx = widthPx * 0.035f
                        val activeWidthPx = (widthPx - (horizontalInsetPx * 2f)).coerceAtLeast(1f)
                        dragValue = ((offset.x - horizontalInsetPx) / activeWidthPx).coerceIn(0f, 1f)
                        onValueChange(dragValue)
                    }
                }
                .pointerInput(title) {
                    detectHorizontalDragGestures(
                        onDragStart = { offset ->
                            val widthPx = size.width.toFloat().coerceAtLeast(1f)
                            val horizontalInsetPx = widthPx * 0.035f
                            val activeWidthPx = (widthPx - (horizontalInsetPx * 2f)).coerceAtLeast(1f)
                            dragValue = ((offset.x - horizontalInsetPx) / activeWidthPx).coerceIn(0f, 1f)
                            onValueChange(dragValue)
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            val widthPx = size.width.toFloat().coerceAtLeast(1f)
                            dragValue = (dragValue + ((dragAmount / widthPx) * 0.99f)).coerceIn(0f, 1f)
                            onValueChange(dragValue)
                        },
                    )
                },
            contentAlignment = Alignment.TopCenter,
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                val strokeWidth = 5.5.dp.toPx()
                val horizontalInset = 8.dp.toPx()
                val topInset = 12.dp.toPx()
                val radius = min(
                    ((size.width - (horizontalInset * 2f)) / 2f).coerceAtLeast(1f),
                    ((size.height - topInset - 8.dp.toPx()) * 0.54f).coerceAtLeast(1f),
                )
                val center = Offset(size.width / 2f, topInset + radius)
                val startAngle = 180f
                val sweepAngle = 180f
                val activeSweep = sweepAngle * animatedValue
                val arcTopLeft = Offset(center.x - radius, center.y - radius)
                val arcSize = Size(radius * 2f, radius * 2f)

                drawArc(
                    color = trackColor,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )

                if (activeSweep > 0f) {
                    drawArc(
                        color = activeArcColor,
                        startAngle = startAngle,
                        sweepAngle = activeSweep,
                        useCenter = false,
                        topLeft = arcTopLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    )
                }

                val tickOuterRadius = (radius - 8.dp.toPx()).coerceAtLeast(1f)
                val tickInnerRadius = (tickOuterRadius - 6.dp.toPx()).coerceAtLeast(1f)
                val tickCount = 30
                repeat(tickCount) { tickIndex ->
                    val fraction = tickIndex / (tickCount - 1).toFloat()
                    val angleDegrees = 180f + (180f * fraction)
                    val angleRadians = Math.toRadians(angleDegrees.toDouble())
                    val start = Offset(
                        x = center.x + (cos(angleRadians) * tickInnerRadius).toFloat(),
                        y = center.y + (sin(angleRadians) * tickInnerRadius).toFloat(),
                    )
                    val end = Offset(
                        x = center.x + (cos(angleRadians) * tickOuterRadius).toFloat(),
                        y = center.y + (sin(angleRadians) * tickOuterRadius).toFloat(),
                    )
                    drawLine(
                        color = tickColor,
                        start = start,
                        end = end,
                        strokeWidth = 1.2.dp.toPx(),
                        cap = StrokeCap.Square,
                    )
                }
            }

            Column(
                modifier = Modifier.padding(top = 34.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = "${(animatedValue * 100f).roundToInt()}",
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = elovaireScaledSp(20f)),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.92f),
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(if (animatedValue > 0f) glowColor else inactiveDot),
                )
            }
        }

        Row(
            modifier = Modifier
                .offset(y = (-28).dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.84f),
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.84f),
            )
        }
    }
}

@Composable
internal fun DetailScreenHeader(
    title: String,
    subtitle: String? = null,
    onBack: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HeaderIconButton(
            iconResId = R.drawable.ic_lucide_chevron_left,
            contentDescription = "Back",
            showBackground = false,
            onClick = onBack,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = elovaireScaledSp(26f)),
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f),
                )
            }
        }
    }
}

internal fun circularKnobValueForOffset(
    offset: Offset,
    size: Size,
    startAngleDegrees: Float,
    sweepAngleDegrees: Float,
): Float {
    if (size.width <= 0f || size.height <= 0f) return 0f
    val centerX = size.width / 2f
    val centerY = size.height / 2f
    val angle = Math.toDegrees(
        atan2(
            (offset.y - centerY).toDouble(),
            (offset.x - centerX).toDouble(),
        ),
    ).toFloat().let { if (it < 0f) it + 360f else it }
    val relative = ((angle - startAngleDegrees) % 360f + 360f) % 360f
    return when {
        relative <= sweepAngleDegrees -> (relative / sweepAngleDegrees).coerceIn(0f, 1f)
        relative < (sweepAngleDegrees + ((360f - sweepAngleDegrees) / 2f)) -> 1f
        else -> 0f
    }
}

@Composable
internal fun EqToneKnob(
    title: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onValueChange: (Float) -> Unit,
) {
    val safeRange = remember(valueRange) {
        if (valueRange.endInclusive > valueRange.start) valueRange else 0f..1f
    }
    val currentOnValueChange by rememberUpdatedState(onValueChange)
    val clampedValue = value.coerceIn(safeRange.start, safeRange.endInclusive)
    val targetFraction = ((clampedValue - safeRange.start) / (safeRange.endInclusive - safeRange.start))
        .coerceIn(0f, 1f)
    var dragFraction by remember { mutableFloatStateOf(targetFraction) }
    LaunchedEffect(targetFraction) {
        dragFraction = targetFraction
    }
    val animatedFraction by animateFloatAsState(
        targetValue = dragFraction,
        animationSpec = tween(ElovaireMotion.Standard),
        label = "${title}_eq_tone_knob",
    )
    val tickIdleColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
    val glowColor = accentColor.copy(alpha = 0.28f)
    val knobFaceColor = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        InkText.copy(alpha = 0.92f)
    } else {
        Color(0xFF1A1A1C)
    }
    val knobEdgeColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    val valueText = remember(clampedValue, safeRange) {
        val percent = if (safeRange.start < 0f) {
            (clampedValue * 100f).roundToInt()
        } else {
            ((clampedValue.coerceAtLeast(0f)) * 100f).roundToInt()
        }
        if (safeRange.start < 0f && percent > 0) "+$percent" else percent.toString()
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .horizontalGestureSafe()
                .pointerInput(title, safeRange.start, safeRange.endInclusive) {
                    detectTapGestures { offset ->
                        val fraction = circularKnobValueForOffset(
                            offset = offset,
                            size = Size(size.width.toFloat(), size.height.toFloat()),
                            startAngleDegrees = 140f,
                            sweepAngleDegrees = 260f,
                        )
                        dragFraction = fraction
                        currentOnValueChange(
                            safeRange.start + ((safeRange.endInclusive - safeRange.start) * fraction),
                        )
                    }
                }
                .pointerInput(title, safeRange.start, safeRange.endInclusive) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val fraction = circularKnobValueForOffset(
                                offset = offset,
                                size = Size(size.width.toFloat(), size.height.toFloat()),
                                startAngleDegrees = 140f,
                                sweepAngleDegrees = 260f,
                            )
                            dragFraction = fraction
                            currentOnValueChange(
                                safeRange.start + ((safeRange.endInclusive - safeRange.start) * fraction),
                            )
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            val fraction = circularKnobValueForOffset(
                                offset = change.position,
                                size = Size(size.width.toFloat(), size.height.toFloat()),
                                startAngleDegrees = 140f,
                                sweepAngleDegrees = 260f,
                            )
                            dragFraction = fraction
                            currentOnValueChange(
                                safeRange.start + ((safeRange.endInclusive - safeRange.start) * fraction),
                            )
                        },
                    )
                },
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 6.dp.toPx()
                val glowWidth = 12.dp.toPx()
                val outerPadding = 9.dp.toPx()
                val radius = ((size.minDimension - outerPadding * 2f) / 2f).coerceAtLeast(1f)
                val center = Offset(size.width / 2f, size.height / 2f)
                val arcTopLeft = Offset(center.x - radius, center.y - radius)
                val arcSize = Size(radius * 2f, radius * 2f)
                val startAngle = 140f
                val sweepAngle = 260f
                val activeSweep = sweepAngle * animatedFraction
                val tickCount = 34
                val tickOuterRadius = radius + 5.dp.toPx()
                val tickInnerRadius = tickOuterRadius - 6.dp.toPx()
                val activeTickCount = (animatedFraction * (tickCount - 1)).roundToInt()

                repeat(tickCount) { tickIndex ->
                    val fraction = tickIndex / (tickCount - 1).toFloat()
                    val angleDegrees = startAngle + (sweepAngle * fraction)
                    val angleRadians = Math.toRadians(angleDegrees.toDouble())
                    val start = Offset(
                        x = center.x + (cos(angleRadians) * tickInnerRadius).toFloat(),
                        y = center.y + (sin(angleRadians) * tickInnerRadius).toFloat(),
                    )
                    val end = Offset(
                        x = center.x + (cos(angleRadians) * tickOuterRadius).toFloat(),
                        y = center.y + (sin(angleRadians) * tickOuterRadius).toFloat(),
                    )
                    drawLine(
                        color = if (tickIndex <= activeTickCount) accentColor else tickIdleColor,
                        start = start,
                        end = end,
                        strokeWidth = 1.2.dp.toPx(),
                        cap = StrokeCap.Square,
                    )
                }
                if (activeSweep > 0f) {
                    drawArc(
                        color = glowColor,
                        startAngle = startAngle,
                        sweepAngle = activeSweep,
                        useCenter = false,
                        topLeft = arcTopLeft,
                        size = arcSize,
                        style = Stroke(width = glowWidth, cap = StrokeCap.Round),
                    )
                    drawArc(
                        color = accentColor,
                        startAngle = startAngle,
                        sweepAngle = activeSweep,
                        useCenter = false,
                        topLeft = arcTopLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    )
                }

                drawCircle(
                    color = knobFaceColor,
                    radius = radius * 0.64f,
                    center = center,
                )
                drawCircle(
                    color = knobEdgeColor,
                    radius = radius * 0.66f,
                    center = center,
                    style = Stroke(width = 1.dp.toPx()),
                )

                val pointerAngle = Math.toRadians((startAngle + activeSweep).toDouble())
                val pointerRadius = radius * 0.56f
                val pointerCenter = Offset(
                    x = center.x + (cos(pointerAngle) * pointerRadius).toFloat(),
                    y = center.y + (sin(pointerAngle) * pointerRadius).toFloat(),
                )
                drawCircle(
                    color = accentColor,
                    radius = 3.dp.toPx(),
                    center = pointerCenter,
                )
            }
            Text(
                text = valueText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = elovaireScaledSp(16f),
                    fontWeight = FontWeight.SemiBold,
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.96f),
            )
        }
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = elovaireScaledSp(10f),
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp,
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        )
    }
}

@Composable
internal fun EqBandSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
) {
    val clampedValue = value.coerceIn(-1f, 1f)
    val accent = if (clampedValue >= 0f) Color(0xFF7D8BFF) else Color(0xFFFF6F61)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .width(46.dp)
                .height(190.dp)
                .clip(RoundedCornerShape(ElovaireRadii.module))
                .background(readableCardSurfaceColor())
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            onValueChange((clampedValue - (dragAmount / 180f)).coerceIn(-1f, 1f))
                        },
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val fraction = 1f - (offset.y / size.height.toFloat())
                        onValueChange(((fraction * 2f) - 1f).coerceIn(-1f, 1f))
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(28.dp),
            ) {
                val trackWidth = 6.dp.toPx()
                val centerX = size.width / 2f
                val centerY = size.height / 2f
                val knobRadius = 8.dp.toPx()
                val travel = (size.height / 2f) - knobRadius - 8.dp.toPx()
                val knobY = centerY - (travel * clampedValue)

                drawRoundRect(
                    color = Color.White.copy(alpha = 0.1f),
                    topLeft = Offset(centerX - (trackWidth / 2f), 8.dp.toPx()),
                    size = Size(trackWidth, size.height - 16.dp.toPx()),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(trackWidth, trackWidth),
                )

                val fillTop = minOf(centerY, knobY)
                val fillBottom = maxOf(centerY, knobY)
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            accent.copy(alpha = 0.94f),
                            accent.copy(alpha = 0.55f),
                        ),
                    ),
                    topLeft = Offset(centerX - (trackWidth / 2f), fillTop),
                    size = Size(trackWidth, (fillBottom - fillTop).coerceAtLeast(trackWidth)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(trackWidth, trackWidth),
                )

                drawCircle(
                    color = accent.copy(alpha = 0.28f),
                    radius = knobRadius * 1.75f,
                    center = Offset(centerX, knobY),
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.96f),
                    radius = knobRadius,
                    center = Offset(centerX, knobY),
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = readableSecondaryTextColor(),
        )
    }
}

@Composable
internal fun EqMacroSliderRow(
    title: String,
    value: Float,
    valueText: String,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = elovaireScaledSp(16f),
                ),
            )
            Text(
                text = valueText,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = elovaireScaledSp(18f)),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.84f),
            )
        }
        ThinContinuousSlider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
        )
    }
}

internal data class EqPresetDefinition(
    val name: String,
    val settings: EqSettings,
)

@Composable
internal fun EqPresetMenu(
    currentSettings: EqSettings,
    onApplyPreset: (EqSettings) -> Unit,
    onReset: () -> Unit,
) {
    val language = LocalAppLanguage.current
    val presets = remember {
        listOf(
            eqPreset("Electronic", 0.40f, 0.58f, 0.42f, 0.26f, 0.10f, 0.08f, 0.16f, 0.24f),
            eqPreset("Jazz", 0.18f, 0.26f, 0.14f, -0.08f, 0.10f, 0.22f, 0.18f, 0.12f),
            eqPreset("Classical", 0.12f, 0.16f, 0.08f, -0.04f, 0.06f, 0.18f, 0.24f, 0.18f),
            eqPreset("Acoustic", 0.10f, 0.14f, 0.06f, -0.12f, 0.14f, 0.20f, 0.18f, 0.10f),
            eqPreset("Pop", 0.22f, 0.28f, 0.16f, -0.06f, 0.18f, 0.24f, 0.18f, 0.10f),
            eqPreset("Rock", 0.28f, 0.22f, 0.10f, -0.12f, 0.08f, 0.18f, 0.28f, 0.22f),
            eqPreset("Metal", 0.22f, 0.18f, 0.08f, -0.14f, 0.12f, 0.24f, 0.30f, 0.26f),
            eqPreset("Vocal", -0.08f, -0.12f, -0.04f, -0.10f, 0.20f, 0.28f, 0.16f, 0.06f),
            eqPreset("R&B", 0.28f, 0.32f, 0.20f, -0.06f, 0.20f, 0.22f, 0.10f, 0.06f),
            eqPreset("Soul", 0.20f, 0.24f, 0.18f, -0.04f, 0.18f, 0.24f, 0.10f, 0.04f),
            eqPreset("Hip-Hop", 0.42f, 0.46f, 0.22f, -0.12f, 0.10f, 0.12f, 0.08f, 0.04f),
        )
    }
    val horizontalScrollState = rememberScrollState()
    val activePresetName = remember(currentSettings, presets) {
        val currentBands = currentSettings.normalizedBandValues()
        presets.firstOrNull { preset -> preset.settings.normalizedBandValues() == currentBands }?.name
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalGestureSafe()
            .horizontalScroll(horizontalScrollState),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        EqPresetPill(
            label = uiPhrase(language, UiPhrase.Reset),
            selected = activePresetName == null && currentSettings != EqSettings(),
            emphasized = true,
            onClick = onReset,
        )
        presets.forEach { preset ->
            EqPresetPill(
                label = preset.name,
                selected = preset.name == activePresetName,
                onClick = {
                    onApplyPreset(
                        currentSettings.copy(
                            bands = preset.settings.bands,
                        ),
                    )
                },
            )
        }
    }
}

@Composable
internal fun SpaciousnessModeMenu(
    currentMode: SpaciousnessMode,
    spaciousnessAmount: Float,
    onModeSelected: (SpaciousnessMode) -> Unit,
) {
    val language = LocalAppLanguage.current
    val modes = remember {
        listOf(
            SpaciousnessMode.StereoWidth,
            SpaciousnessMode.CrossfeedDepth,
            SpaciousnessMode.EarlyReflectionRoom,
            SpaciousnessMode.Philharmony,
            SpaciousnessMode.HaasSpace,
            SpaciousnessMode.HarmonicAir,
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalGestureSafe()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        modes.forEach { mode ->
            EqPresetPill(
                label = mode.displayLabel(language),
                selected = spaciousnessAmount > 0.001f && mode == currentMode,
                useSubtleIdleBackground = true,
                onClick = {
                    onModeSelected(
                        if (spaciousnessAmount > 0.001f && mode == currentMode) {
                            SpaciousnessMode.Off
                        } else {
                            mode
                        },
                    )
                },
            )
        }
    }
}

@Composable
internal fun EqPresetPill(
    label: String,
    selected: Boolean,
    emphasized: Boolean = false,
    useSubtleIdleBackground: Boolean = false,
    onClick: () -> Unit,
) {
    val backgroundColor = if (emphasized) {
        MaterialTheme.colorScheme.primary
    } else if (selected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
    } else if (useSubtleIdleBackground) {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    }
    val contentColor = if (emphasized) {
        MaterialTheme.colorScheme.onPrimary
    } else if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
    }
    val interactionSource = remember { MutableInteractionSource() }
    Surface(
        modifier = Modifier.elovairePressBounce(
            interactionSource = interactionSource,
            label = "${label}_eq_preset_scale",
        ),
        shape = RoundedCornerShape(ElovaireRadii.pill),
        color = backgroundColor,
    ) {
        Text(
            text = label,
            modifier = Modifier
                .clip(RoundedCornerShape(ElovaireRadii.pill))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                )
                .padding(horizontal = 14.dp, vertical = 9.dp),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
            color = contentColor,
        )
    }
}

@Composable
internal fun EqResponseGraph(
    settings: EqSettings,
    onBandChanged: (Int, Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val eqGraphConfig = remember { EqualizerDspConfig() }
    val graphPointCount = EqualizerDspModel.BAND_COUNT
    val animatedBandValues = List(graphPointCount) { index ->
        val target = settings.bands.getOrElse(index) { 0f }.coerceIn(-1f, 1f)
        val animated by animateFloatAsState(
            targetValue = target,
            animationSpec = tween(120, easing = FastOutSlowInEasing),
            label = "eq_band_$index",
        )
        animated
    }
    val bandValues = remember(animatedBandValues) {
        normalizeEqBandValues(animatedBandValues, graphPointCount)
    }
    val bandFractions = remember { eqBandFractions() }
    val accentColor = Color(0xFF39E38E)
    val guideColor = MaterialTheme.colorScheme.onSurface
    val density = LocalDensity.current
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(ElovaireRadii.module))
            .pointerInput(bandFractions) {
                detectTapGestures { offset ->
                    if (size.width == 0 || size.height == 0) return@detectTapGestures
                    val horizontalPadding = with(density) { EQ_GRAPH_EDGE_PADDING.toPx() }
                    val graphWidth = (size.width.toFloat() - horizontalPadding * 2f).coerceAtLeast(1f)
                    val bandIndex = nearestEqBandIndex(
                        fraction = ((offset.x - horizontalPadding) / graphWidth).coerceIn(0f, 1f),
                        bandFractions = bandFractions,
                    )
                    val verticalFraction = (1f - (offset.y / size.height.toFloat())).coerceIn(0f, 1f)
                    onBandChanged(
                        bandIndex,
                        EqualizerDspModel.graphFractionToNormalized(verticalFraction, eqGraphConfig),
                    )
                }
            }
            .pointerInput(bandFractions) {
                detectDragGestures(
                    onDragStart = { offset ->
                        if (size.width == 0 || size.height == 0) return@detectDragGestures
                        val horizontalPadding = with(density) { EQ_GRAPH_EDGE_PADDING.toPx() }
                        val graphWidth = (size.width.toFloat() - horizontalPadding * 2f).coerceAtLeast(1f)
                        val bandIndex = nearestEqBandIndex(
                            fraction = ((offset.x - horizontalPadding) / graphWidth).coerceIn(0f, 1f),
                            bandFractions = bandFractions,
                        )
                        val normalized = (1f - (offset.y / size.height.toFloat())).coerceIn(0f, 1f)
                        onBandChanged(bandIndex, ((normalized * 2f) - 1f).coerceIn(-1f, 1f))
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        if (size.width == 0 || size.height == 0) return@detectDragGestures
                        val horizontalPadding = with(density) { EQ_GRAPH_EDGE_PADDING.toPx() }
                        val graphWidth = (size.width.toFloat() - horizontalPadding * 2f).coerceAtLeast(1f)
                        val index = nearestEqBandIndex(
                            fraction = ((change.position.x - horizontalPadding) / graphWidth).coerceIn(0f, 1f),
                            bandFractions = bandFractions,
                        )
                        val verticalFraction = (1f - (change.position.y / size.height.toFloat())).coerceIn(0f, 1f)
                        onBandChanged(
                            index,
                            EqualizerDspModel.graphFractionToNormalized(verticalFraction, eqGraphConfig),
                        )
                    },
                )
            },
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val topPadding = size.height * 0.08f
            val bottomPadding = size.height * 0.12f
            val graphHeight = size.height - topPadding - bottomPadding
            val horizontalPadding = EQ_GRAPH_EDGE_PADDING.toPx()
            val graphWidth = (size.width - horizontalPadding * 2f).coerceAtLeast(1f)
            val zeroDbFraction = ((0f - eqGraphConfig.minBandGainDb) / (eqGraphConfig.maxBandGainDb - eqGraphConfig.minBandGainDb))
                .coerceIn(0f, 1f)
            val midY = topPadding + (graphHeight * (1f - zeroDbFraction))

            eqDbLevels().forEach { levelDb ->
                val y = topPadding + (graphHeight * (1f - eqLevelFraction(levelDb, eqGraphConfig)))
                drawLine(
                    color = guideColor.copy(alpha = if (levelDb == 0f) 0.12f else 0.05f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx(),
                )
            }

            bandValues.forEachIndexed { index, band ->
                val x = horizontalPadding + graphWidth * bandFractions.getOrElse(index) { 0f }
                val y = topPadding + (graphHeight * (1f - EqualizerDspModel.bandGraphFraction(band, eqGraphConfig)))
                val trackWidth = 5.dp.toPx()
                val activeWidth = 3.dp.toPx()
                val thumbWidth = 9.dp.toPx()
                val thumbHeight = 24.dp.toPx()
                val activeTop = min(y, midY)
                val activeHeight = max(2.dp.toPx(), kotlin.math.abs(y - midY))
                drawRoundRect(
                    color = accentColor.copy(alpha = 0.08f),
                    topLeft = Offset(x - trackWidth * 1.45f, topPadding - 8.dp.toPx()),
                    size = Size(trackWidth * 2.9f, graphHeight + 16.dp.toPx()),
                    cornerRadius = CornerRadius(trackWidth * 2.9f, trackWidth * 2.9f),
                )
                drawRoundRect(
                    color = guideColor.copy(alpha = 0.05f),
                    topLeft = Offset(x - trackWidth / 2f, topPadding),
                    size = Size(trackWidth, graphHeight),
                    cornerRadius = CornerRadius(trackWidth, trackWidth),
                )
                drawLine(
                    color = accentColor.copy(alpha = 0.18f),
                    start = Offset(x, midY),
                    end = Offset(x, y),
                    strokeWidth = activeWidth * 2.1f,
                    cap = StrokeCap.Round,
                )
                drawRoundRect(
                    color = accentColor,
                    topLeft = Offset(x - activeWidth / 2f, activeTop),
                    size = Size(activeWidth, activeHeight),
                    cornerRadius = CornerRadius(activeWidth, activeWidth),
                )
                drawRoundRect(
                    color = accentColor.copy(alpha = 0.16f),
                    topLeft = Offset(x - thumbWidth * 0.8f, y - thumbHeight / 2f),
                    size = Size(thumbWidth * 1.6f, thumbHeight),
                    cornerRadius = CornerRadius(thumbWidth, thumbWidth),
                )
                drawRoundRect(
                    color = accentColor,
                    topLeft = Offset(x - thumbWidth / 2f, y - thumbHeight / 2f),
                    size = Size(thumbWidth, thumbHeight),
                    cornerRadius = CornerRadius(thumbWidth, thumbWidth),
                )
            }
        }
    }
}

@Composable
internal fun EqMiniResponseGraph(
    settings: EqSettings,
    modifier: Modifier = Modifier,
) {
    val eqGraphConfig = remember { EqualizerDspConfig() }
    val graphPointCount = EqualizerDspModel.BAND_COUNT
    val animatedBandValues = List(graphPointCount) { index ->
        val target = settings.bands.getOrElse(index) { 0f }.coerceIn(-1f, 1f)
        val animated by animateFloatAsState(
            targetValue = target,
            animationSpec = tween(160, easing = FastOutSlowInEasing),
            label = "eq_mini_band_$index",
        )
        animated
    }
    val bandValues = remember(animatedBandValues) {
        normalizeEqBandValues(animatedBandValues, graphPointCount)
    }
    val bandFractions = remember { eqBandFractions() }
    val accentColor = Color(0xFF39E38E)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(ElovaireRadii.module))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f)),
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val horizontalPadding = 14.dp.toPx()
            val topPadding = size.height * 0.18f
            val bottomPadding = size.height * 0.2f
            val graphHeight = size.height - topPadding - bottomPadding
            val graphWidth = size.width - horizontalPadding * 2f
            val zeroDbFraction = ((0f - eqGraphConfig.minBandGainDb) / (eqGraphConfig.maxBandGainDb - eqGraphConfig.minBandGainDb))
                .coerceIn(0f, 1f)
            val midY = topPadding + (graphHeight * (1f - zeroDbFraction))
            val points = bandValues.mapIndexed { index, band ->
                val x = horizontalPadding + graphWidth * bandFractions.getOrElse(index) { 0f }
                val y = topPadding + (graphHeight * (1f - EqualizerDspModel.bandGraphFraction(band, eqGraphConfig)))
                Offset(x, y)
            }
            if (points.isEmpty()) return@Canvas
            val strokePath = smoothPathFromPoints(points)
            val fillPath = androidx.compose.ui.graphics.Path().apply {
                addPath(strokePath)
                lineTo(points.last().x, midY)
                lineTo(points.first().x, midY)
                close()
            }
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.18f),
                        accentColor.copy(alpha = 0.07f),
                        Color.Transparent,
                    ),
                    startY = 0f,
                    endY = size.height,
                ),
            )
            drawPath(
                path = strokePath,
                color = accentColor.copy(alpha = 0.2f),
                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round),
            )
            drawPath(
                path = strokePath,
                color = accentColor,
                style = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round),
            )
        }
    }
}

@Composable
internal fun EqDbScale(
    modifier: Modifier = Modifier,
) {
    val eqGraphConfig = remember { EqualizerDspConfig() }
    val markerColor = readableSecondaryTextColor().copy(alpha = 0.78f)
    val levels = remember { eqDbLevels() }
    BoxWithConstraints(modifier = modifier) {
        val topPadding = maxHeight * 0.08f
        val bottomPadding = maxHeight * 0.12f
        val graphHeight = maxHeight - topPadding - bottomPadding
        levels.forEach { levelDb ->
            val positionY = topPadding + (graphHeight * (1f - eqLevelFraction(levelDb, eqGraphConfig)))
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(y = positionY - 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = formatEqDbLabel(levelDb),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = elovaireScaledSp(9f)),
                    color = markerColor,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    repeat(3) { markerIndex ->
                        Box(
                            modifier = Modifier
                                .width((5 - markerIndex).dp)
                                .height(1.5.dp)
                                .clip(RoundedCornerShape(ElovaireRadii.pill))
                                .background(markerColor.copy(alpha = 0.65f - (markerIndex * 0.14f))),
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun EqBandFrequencyLabels(
    contentWidth: Dp,
    modifier: Modifier = Modifier,
) {
    val labels = remember {
        EqualizerDspModel.BAND_CENTER_FREQUENCIES_HZ.map(::formatEqFrequencyLabel)
    }
    val bandFractions = remember { eqBandFractions() }
    BoxWithConstraints(
        modifier = modifier
            .width(contentWidth)
            .height(18.dp),
    ) {
        val labelWidth = 36.dp
        val graphWidth = maxWidth - (EQ_GRAPH_EDGE_PADDING * 2)
        labels.forEachIndexed { index, label ->
            val fraction = bandFractions.getOrElse(index) { 0f }
            Box(
                modifier = Modifier
                    .width(labelWidth)
                    .align(Alignment.CenterStart)
                    .offset(x = EQ_GRAPH_EDGE_PADDING + graphWidth * fraction - (labelWidth / 2)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = elovaireScaledSp(9.2f)),
                    color = readableSecondaryTextColor().copy(alpha = 0.88f),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
internal fun EqHorizontalScrollbar(
    scrollState: androidx.compose.foundation.ScrollState,
    contentWidth: Dp,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .horizontalGestureSafe(),
    ) {
        val density = LocalDensity.current
        val viewportWidthPx = with(density) { maxWidth.toPx() }
        val contentWidthPx = with(density) { contentWidth.toPx() }.coerceAtLeast(viewportWidthPx)
        val maxScrollPx = scrollState.maxValue.toFloat().coerceAtLeast(0f)
        val viewportFraction = (viewportWidthPx / contentWidthPx).coerceIn(0.08f, 1f)
        val thumbWidthPx = (viewportWidthPx * viewportFraction).coerceAtLeast(with(density) { 46.dp.toPx() })
        val thumbTravelPx = (viewportWidthPx - thumbWidthPx).coerceAtLeast(0f)
        val thumbOffsetFraction = if (maxScrollPx <= 0f) 0f else (scrollState.value / maxScrollPx).coerceIn(0f, 1f)
        val thumbOffsetPx = thumbTravelPx * thumbOffsetFraction
        val trackColor = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
            InkText.copy(alpha = 0.12f)
        } else {
            Color.White.copy(alpha = 0.14f)
        }
        val thumbColor = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
            InkText.copy(alpha = 0.58f)
        } else {
            Color.White.copy(alpha = 0.62f)
        }
        val updateScrollFromX: (Float) -> Unit = { xPosition ->
            if (maxScrollPx > 0f && viewportWidthPx > 0f) {
                val fraction = ((xPosition - (thumbWidthPx / 2f)) / thumbTravelPx.coerceAtLeast(1f)).coerceIn(0f, 1f)
                val targetScroll = (maxScrollPx * fraction).roundToInt()
                scope.launch {
                    scrollState.scrollTo(targetScroll)
                }
            }
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .pointerInput(viewportWidthPx, maxScrollPx) {
                    detectTapGestures { offset ->
                        updateScrollFromX(offset.x)
                    }
                }
                .pointerInput(viewportWidthPx, maxScrollPx) {
                    detectHorizontalDragGestures(
                        onDragStart = { offset -> updateScrollFromX(offset.x) },
                        onHorizontalDrag = { change, _ ->
                            change.consume()
                            updateScrollFromX(change.position.x)
                        },
                    )
                },
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .height(2.dp)
                    .clip(RoundedCornerShape(ElovaireRadii.pill))
                    .background(trackColor),
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset { IntOffset(x = thumbOffsetPx.roundToInt(), y = 0) }
                    .width(with(density) { thumbWidthPx.toDp() })
                    .height(4.dp)
                    .clip(RoundedCornerShape(ElovaireRadii.pill))
                    .background(thumbColor),
            )
        }
    }
}

@Composable
internal fun SettingsCategoryText(
    title: String,
    @DrawableRes iconResId: Int? = null,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (iconResId != null) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = readableMutedIconColor(),
                modifier = Modifier.size(15.dp),
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.92f),
        )
    }
}

@Composable
internal fun ThinContinuousSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    lineThickness: Dp = 2.dp,
    knobSize: Dp = 20.dp,
    modifier: Modifier = Modifier,
) {
    val currentOnValueChange by rememberUpdatedState(onValueChange)
    val coercedValue = value.coerceIn(valueRange.start, valueRange.endInclusive)
    val fraction = ((coercedValue - valueRange.start) / (valueRange.endInclusive - valueRange.start))
        .coerceIn(0f, 1f)
    val knobColor = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        InkText
    } else {
        Color.White
    }
    val inactiveLineColor = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        InkText.copy(alpha = 0.18f)
    } else {
        Color.White.copy(alpha = 0.2f)
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
            .horizontalGestureSafe(),
    ) {
        val density = LocalDensity.current
        val maxWidthPx = with(density) { maxWidth.toPx() }
        val knobSizePx = with(density) { knobSize.toPx() }
        val trackStartPx = knobSizePx / 2f
        val trackWidthPx = (maxWidthPx - knobSizePx).coerceAtLeast(1f)
        val trackStart = with(density) { trackStartPx.toDp() }
        val trackWidth = with(density) { trackWidthPx.toDp() }
        val activeWidth by animateDpAsState(
            targetValue = trackWidth * fraction,
            animationSpec = tween(durationMillis = 70),
            label = "eq_macro_slider_fill",
        )
        val knobOffset by animateDpAsState(
            targetValue = with(density) { (trackStartPx + trackWidthPx * fraction - knobSizePx / 2f).toDp() },
            animationSpec = tween(durationMillis = 70),
            label = "eq_macro_slider_knob",
        )

        val updateFromX: (Float) -> Unit = { xPosition ->
            if (maxWidthPx > 0f) {
                val normalized = ((xPosition - trackStartPx) / trackWidthPx).coerceIn(0f, 1f)
                val rangedValue = valueRange.start + ((valueRange.endInclusive - valueRange.start) * normalized)
                currentOnValueChange(rangedValue.coerceIn(valueRange.start, valueRange.endInclusive))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(maxWidthPx, valueRange.start, valueRange.endInclusive) {
                    detectTapGestures { offset ->
                        updateFromX(offset.x)
                    }
                }
                .pointerInput(maxWidthPx, valueRange.start, valueRange.endInclusive) {
                    detectHorizontalDragGestures(
                        onDragStart = { offset -> updateFromX(offset.x) },
                        onHorizontalDrag = { change, _ ->
                            change.consume()
                            updateFromX(change.position.x)
                        },
                    )
                },
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = trackStart)
                    .width(trackWidth)
                    .height(lineThickness)
                    .clip(RoundedCornerShape(ElovaireRadii.pill))
                    .background(inactiveLineColor),
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = trackStart)
                    .width(activeWidth)
                    .height(lineThickness)
                    .clip(RoundedCornerShape(ElovaireRadii.pill))
                    .background(knobColor),
            )
            Box(
                modifier = Modifier
                    .offset { IntOffset(x = knobOffset.roundToPx(), y = 0) }
                    .size(knobSize)
                    .clip(CircleShape)
                    .background(knobColor)
                    .align(Alignment.CenterStart),
            )
        }
    }
}

internal fun eqBandFractions(): List<Float> {
    val count = EqualizerDspModel.BAND_COUNT
    if (count <= 1) return emptyList()
    return List(count) { index -> index.toFloat() / (count - 1).toFloat() }
}

internal fun eqDbLevels(): List<Float> = listOf(8f, 4f, 0f, -5f, -10f)

internal fun eqLevelFraction(
    levelDb: Float,
    config: EqualizerDspConfig = EqualizerDspConfig(),
): Float {
    return ((levelDb - config.minBandGainDb) / (config.maxBandGainDb - config.minBandGainDb))
        .coerceIn(0f, 1f)
}

internal fun formatEqDbLabel(levelDb: Float): String {
    return when {
        levelDb > 0f -> "+${levelDb.roundToInt()}"
        levelDb < 0f -> levelDb.roundToInt().toString()
        else -> "0"
    }
}

internal fun nearestEqBandIndex(
    fraction: Float,
    bandFractions: List<Float>,
): Int {
    return bandFractions
        .withIndex()
        .minByOrNull { (_, value) -> kotlin.math.abs(value - fraction) }
        ?.index
        ?: 0
}

internal fun formatEqFrequencyLabel(frequencyHz: Float): String {
    return when {
        frequencyHz >= 1_000f -> {
            val kilo = frequencyHz / 1_000f
            formatEqKiloLabel(kilo)
        }
        frequencyHz % 1f == 0f -> frequencyHz.roundToInt().toString()
        else -> frequencyHz.toString()
    }
}

internal fun formatEqKiloLabel(kiloValue: Float): String {
    val rawLabel = when {
        kiloValue >= 10f || kiloValue % 1f == 0f -> kiloValue.roundToInt().toString()
        (kiloValue * 10f) % 1f == 0f -> String.format(java.util.Locale.ROOT, "%.1f", kiloValue)
        else -> String.format(java.util.Locale.ROOT, "%.2f", kiloValue)
    }
    val formatted = if ('.' in rawLabel) rawLabel.trimEnd('0').trimEnd('.') else rawLabel
    return "${formatted}k"
}

internal fun normalizeEqBandValues(
    values: List<Float>,
    targetCount: Int,
): List<Float> {
    if (values.isEmpty()) return List(targetCount) { 0f }
    return List(targetCount) { index ->
        values.getOrElse(index) { 0f }.coerceIn(-1f, 1f)
    }
}

internal fun eqPreset(
    name: String,
    bass: Float,
    subBass: Float,
    lowBass: Float,
    lowMid: Float,
    presence: Float,
    upperMid: Float,
    brilliance: Float,
    air: Float,
): EqPresetDefinition {
    val bandShape = List(EqualizerDspModel.BAND_COUNT) { index ->
        when (EqualizerDspModel.bandDefinition(index).frequencyHz) {
            in 0f..30f -> bass
            in 30.0001f..60f -> subBass
            in 60.0001f..350f -> lowBass
            in 350.0001f..750f -> lowMid
            in 750.0001f..1_500f -> presence
            in 1_500.0001f..3_000f -> upperMid
            in 3_000.0001f..8_000f -> brilliance
            else -> air
        }.coerceIn(-1f, 1f)
    }
    val settings = EqSettings(
        bands = bandShape,
        bass = 0f,
        treble = 0f,
        spaciousness = 0f,
        spaciousnessMode = SpaciousnessMode.Off,
    ).normalizedEqSettings()
    return EqPresetDefinition(name = name, settings = settings)
}

internal fun EqSettings.normalizedBandValues(): List<Float> {
    return List(EqualizerDspModel.BAND_COUNT) { index ->
        bands.getOrElse(index) { 0f }.coerceIn(-1f, 1f)
    }
}

internal fun EqSettings.normalizedEqSettings(): EqSettings {
    return copy(
        bands = normalizedBandValues(),
        bass = bass.coerceIn(-1f, 1f),
        treble = treble.coerceIn(-1f, 1f),
        spaciousness = spaciousness.coerceIn(0f, 1f),
    )
}

internal fun smoothPathFromPoints(points: List<Offset>): androidx.compose.ui.graphics.Path {
    return androidx.compose.ui.graphics.Path().apply {
        if (points.isEmpty()) return@apply
        moveTo(points.first().x, points.first().y)
        if (points.size == 1) return@apply
        for (index in 1 until points.size) {
            val previous = points[index - 1]
            val current = points[index]
            val midPoint = Offset(
                x = (previous.x + current.x) / 2f,
                y = (previous.y + current.y) / 2f,
            )
            quadraticTo(previous.x, previous.y, midPoint.x, midPoint.y)
        }
        val last = points.last()
        lineTo(last.x, last.y)
    }
}


@Composable
internal fun JellyfinStatusCard(
    isConnected: Boolean,
    host: String,
    onSetup: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isConnected)
        MaterialTheme.colorScheme.secondaryContainer
    else
        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
    val contentColor = if (isConnected)
        MaterialTheme.colorScheme.onSecondaryContainer
    else
        MaterialTheme.colorScheme.onErrorContainer

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ElovaireRadii.card),
        color = containerColor,
        contentColor = contentColor,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(contentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "J",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = contentColor,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Jellyfin",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                )
                Text(
                    text = if (isConnected) host.removePrefix("https://").removePrefix("http://").trimEnd('/')
                           else "Not connected",
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = 0.72f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = contentColor.copy(alpha = 0.18f),
                modifier = Modifier.clickable(onClick = onSetup),
            ) {
                Text(
                    text = if (isConnected) "Manage" else "Set up",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = contentColor,
                )
            }
        }
    }
}
