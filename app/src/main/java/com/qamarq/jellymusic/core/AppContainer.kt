package com.qamarq.jellymusic.core

import android.annotation.SuppressLint
import android.content.Context
import com.qamarq.jellymusic.data.jellyfin.JellyfinRepository
import com.qamarq.jellymusic.data.library.LibraryContentState
import com.qamarq.jellymusic.data.library.LibraryRepository
import com.qamarq.jellymusic.data.library.MediaStoreScanner
import com.qamarq.jellymusic.data.lyrics.LyricsService
import com.qamarq.jellymusic.data.playback.PlaybackEffectsController
import com.qamarq.jellymusic.data.playback.PlaybackManager
import com.qamarq.jellymusic.data.playback.PlaybackNotificationController
import com.qamarq.jellymusic.data.settings.PreferenceStore
import com.qamarq.jellymusic.data.update.AppUpdateManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@SuppressLint("UnsafeOptInUsageError")
class AppContainer(
    appContext: Context,
) {
    private val applicationContext = appContext.applicationContext
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    val preferenceStore = PreferenceStore(applicationContext)
    val appUpdateManager = AppUpdateManager(
        context = applicationContext,
        scope = appScope,
        preferenceStore = preferenceStore,
    )
    val lyricsService = LyricsService(applicationContext)
    private val playbackEffectsController = PlaybackEffectsController()
    val playbackManager = PlaybackManager(
        context = applicationContext,
        scope = appScope,
        audioProcessorsProvider = playbackEffectsController::audioProcessors,
        hasSignalAlteringEffects = playbackEffectsController::hasSignalAlteringEffects,
        initialRecentSongIds = preferenceStore.recentSongIds.value,
        initialRecentAlbumIds = preferenceStore.recentAlbumIds.value,
        initialLastPlayedCollectionKind = preferenceStore.lastPlayedCollectionKind.value,
        initialLastPlayedCollectionId = preferenceStore.lastPlayedCollectionId.value,
        onRecentPlaybackChanged = preferenceStore::setRecentPlaybackIds,
    )
    private var playbackNotificationController: PlaybackNotificationController? = null
    val libraryRepository = LibraryRepository(
        appContext = applicationContext,
        scanner = MediaStoreScanner(applicationContext),
        scope = appScope,
    )
    val jellyfinRepository = JellyfinRepository(
        preferenceStore = preferenceStore,
        scope = appScope,
    )
    val mergedContentState = combine(
        libraryRepository.contentState,
        jellyfinRepository.songs,
        jellyfinRepository.albums,
        jellyfinRepository.artists
    ) { local, jSongs, jAlbums, jArtists ->
        LibraryContentState(
            songs = local.songs + jSongs,
            albums = local.albums + jAlbums,
            artists = jArtists
        )
    }.stateIn(appScope, SharingStarted.Eagerly, LibraryContentState())

    private val _openPlayerRequests = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val openPlayerRequests: SharedFlow<Unit> = _openPlayerRequests.asSharedFlow()

    init {
        appScope.launch {
            preferenceStore.eqSettings.collect { settings ->
                playbackEffectsController.updateSettings(settings)
                playbackManager.reevaluateAudioOutputPath()
            }
        }
        appScope.launch {
            playbackManager.nowPlayingState
                .map { it.currentSong?.id to it.currentSong?.albumId }
                .distinctUntilChanged()
                .collect { (songId, albumId) ->
                    if (songId != null) {
                        preferenceStore.incrementSongPlayCount(songId)
                    }
                    if (albumId != null) {
                        preferenceStore.incrementAlbumPlayCount(albumId)
                    }
                }
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        if (!enabled && playbackNotificationController == null) return
        notificationController().setNotificationsEnabled(enabled)
    }

    fun requestOpenPlayer() {
        _openPlayerRequests.tryEmit(Unit)
    }

    fun scheduleDeferredStartupWork() {
        appUpdateManager.scheduleStartupMaintenance()
    }

    fun release() {
        playbackNotificationController?.setNotificationsEnabled(false)
        playbackNotificationController = null
        appUpdateManager.release()
        libraryRepository.release()
        playbackManager.release()
    }

    private fun notificationController(): PlaybackNotificationController {
        PlaybackNotificationController.ensureNotificationChannel(applicationContext)
        return playbackNotificationController ?: PlaybackNotificationController(
            context = applicationContext,
            playbackManager = playbackManager,
            scope = appScope,
        ).also { controller ->
            playbackNotificationController = controller
        }
    }
}
