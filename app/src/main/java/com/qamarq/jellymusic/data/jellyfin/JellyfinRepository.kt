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
            Song(
                id = dto.Id.hashCode().toLong(),
                title = dto.Name,
                isExplicit = false,
                artist = dto.ArtistItems?.firstOrNull()?.Name ?: "Unknown Artist",
                artistId = dto.ArtistItems?.firstOrNull()?.Id ?: "unknown",
                artistImage = dto.ArtistItems?.firstOrNull()?.Id?.let { id ->
                    client.getImageUrl(id, "").toUri()
                },
                album = dto.Album ?: "Unknown Album",
                releaseYear = dto.ProductionYear,
                genre = dto.Genres?.firstOrNull() ?: "Unknown Genre",
                audioFormat = dto.Container?.uppercase() ?: "STREAM",
                audioQuality = dto.Bitrate?.let { "${it / 1000}kbps" },
                fileName = dto.Name,
                albumId = dto.AlbumId?.hashCode()?.toLong() ?: 0L,
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

        val jellyfinArtists = jellyfinSongs.groupBy { it.artist }.mapNotNull { (artistId, songs) ->
            val firstSong = songs.firstOrNull() ?: return@mapNotNull null
            Artist(
                id = artistId,
                name = firstSong.artist,
                image = firstSong.artistImage
            )
        }

        _songs.value = jellyfinSongs
        _albums.value = jellyfinAlbums
        _artists.value = jellyfinArtists
    }
}
