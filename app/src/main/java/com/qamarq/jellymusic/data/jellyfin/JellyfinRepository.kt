package com.qamarq.jellymusic.data.jellyfin

import android.net.Uri
import android.util.Log
import com.qamarq.jellymusic.data.settings.PreferenceStore
import com.qamarq.jellymusic.domain.model.Album
import com.qamarq.jellymusic.domain.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import com.qamarq.jellymusic.domain.model.Artist

class JellyfinRepository(
    private val preferenceStore: PreferenceStore,
    private val scope: CoroutineScope,
) {
    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()

    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums.asStateFlow()

    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    val artists: StateFlow<List<Artist>> = _artists.asStateFlow()

    init {
        scope.launch {
            combine(
                preferenceStore.jellyfinHost,
                preferenceStore.jellyfinAccessToken,
                preferenceStore.jellyfinUserId
            ) { host, token, userId ->
                Triple(host, token, userId)
            }.collect { (host, token, userId) ->
                if (host.isNotEmpty() && token.isNotEmpty() && userId.isNotEmpty()) {
                    performRefresh(host, token, userId)
                } else {
                    _songs.value = emptyList()
                    _albums.value = emptyList()
                }
            }
        }
    }

    fun refresh() {
        val host = preferenceStore.jellyfinHost.value
        val token = preferenceStore.jellyfinAccessToken.value
        val userId = preferenceStore.jellyfinUserId.value
        if (host.isNotEmpty() && token.isNotEmpty() && userId.isNotEmpty()) {
            scope.launch {
                performRefresh(host, token, userId)
            }
        }
    }

    private suspend fun performRefresh(host: String, token: String, userId: String) {
        val client = JellyfinClient(host, token)
        val items = client.getMusicItems(userId)

        Log.d("JellyItems", "Fetched $items items from Jellyfin")
        
        val jellyfinSongs = items.map { dto ->
            // Album artist (e.g. "Coldplay") is the correct main performer to display/group by,
            // not the track-level ArtistItems which can list songwriters/featured artists.
            val mainArtist = dto.AlbumArtists?.firstOrNull() ?: dto.ArtistItems?.firstOrNull()
            Song(
                id = stableIdFrom(dto.Id),
                title = dto.Name,
                isExplicit = false,
                artist = mainArtist?.Name ?: "Unknown Artist",
                artistId = mainArtist?.Id ?: "unknown",
                artistImage = mainArtist?.Id?.let { id ->
                    client.getImageUrl(id, "").toUri()
                },
                album = dto.Album ?: "Unknown Album",
                releaseYear = dto.ProductionYear,
                genre = dto.Genres?.firstOrNull() ?: "Unknown Genre",
                audioFormat = dto.Container?.uppercase() ?: "STREAM",
                audioQuality = formatJellyfinAudioQuality(dto),
                fileName = dto.Name,
                albumId = dto.AlbumId?.let(::stableIdFrom) ?: 0L,
                durationMs = (dto.RunTimeTicks ?: 0L) / 10000,
                trackNumber = dto.IndexNumber ?: 0,
                discNumber = dto.ParentIndexNumber ?: 1,
                dateAddedSeconds = 0L, // Not critical for now
                uri = client.getStreamUrl(dto.Id).toUri(),
                artUri = dto.ImageTags?.get("Primary")?.let { tag ->
                    client.getImageUrl(dto.Id, tag).toUri()
                } ?: dto.AlbumId?.let { albumId ->
                    client.getImageUrl(albumId, "").toUri()
                },
                metadataResolved = true
            )
        }

        val jellyfinAlbums = jellyfinSongs.groupBy { it.albumId }.mapNotNull { (albumId, songs) ->
            val firstSong = songs.firstOrNull() ?: return@mapNotNull null
            Album(
                id = albumId,
                title = firstSong.album,
                artist = firstSong.artist,
                artUri = firstSong.artUri,
                songCount = songs.size,
                durationMs = songs.sumOf { it.durationMs },
                songs = songs.sortedBy { it.trackNumber }
            )
        }

        val jellyfinArtists = jellyfinSongs.groupBy { it.artistId ?: it.artist }.mapNotNull { (artistId, songs) ->
            val firstSong = songs.firstOrNull() ?: return@mapNotNull null
            Artist(
                id = artistId ?: firstSong.artist,
                name = firstSong.artist,
                image = firstSong.artistImage
            )
        }.sortedWith(
            compareBy(
                { it.name.equals("Unknown Artist", ignoreCase = true) },
                { it.name.lowercase() },
            ),
        )

        _songs.value = jellyfinSongs
        _albums.value = jellyfinAlbums
        _artists.value = jellyfinArtists
    }

    // Long.hashCode() can be negative, but song/playlist storage treats ids <= 0 as invalid,
    // so mask down to the unsigned 32-bit range to keep ids stable and always positive.
    private fun stableIdFrom(jellyfinId: String): Long = jellyfinId.hashCode().toLong() and 0xFFFFFFFFL

    private val losslessFormats = setOf("FLAC", "ALAC", "WAV", "APE", "WV", "DSF", "DFF")

    private fun formatJellyfinAudioQuality(dto: BaseItemDto): String? {
        val mediaSource = dto.MediaSources?.firstOrNull()
        val audioStream = mediaSource?.MediaStreams?.firstOrNull { it.Type.equals("Audio", ignoreCase = true) }
        val bitDepth = audioStream?.BitDepth?.takeIf { it > 0 }
        val sampleRate = audioStream?.SampleRate?.takeIf { it > 0 }
        val bitrate = mediaSource?.Bitrate ?: dto.Bitrate ?: audioStream?.BitRate
        val sampleRateText = sampleRate?.let(::formatSampleRateKHz)
        val isLossless = (dto.Container ?: mediaSource?.Container)?.uppercase() in losslessFormats

        return when {
            isLossless && bitDepth != null && sampleRateText != null -> "$bitDepth/$sampleRateText"
            bitrate != null && bitrate > 0 && sampleRateText != null -> "${roundKbps(bitrate)}/$sampleRateText"
            bitDepth != null && sampleRateText != null -> "$bitDepth/$sampleRateText"
            sampleRateText != null -> sampleRateText
            bitrate != null && bitrate > 0 -> "${roundKbps(bitrate)}kbps"
            else -> null
        }
    }

    private fun formatSampleRateKHz(sampleRateHz: Int): String {
        val khz = sampleRateHz / 1000f
        val rounded = if (khz % 1f == 0f) khz.toInt().toString() else "%.1f".format(khz)
        return "${rounded}kHz"
    }

    private fun roundKbps(bitrateBitsPerSecond: Int): String {
        val kbps = bitrateBitsPerSecond / 1000f
        val rounded = kotlin.math.round(kbps)
        return if (kotlin.math.abs(kbps - rounded) < 0.5f) rounded.toInt().toString() else "%.1f".format(kbps)
    }
}
