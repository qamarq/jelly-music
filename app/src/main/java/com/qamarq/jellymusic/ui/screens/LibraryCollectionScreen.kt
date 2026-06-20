package com.qamarq.jellymusic.ui.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.qamarq.jellymusic.data.library.LibraryUiState
import com.qamarq.jellymusic.domain.model.Album
import com.qamarq.jellymusic.domain.model.Playlist
import com.qamarq.jellymusic.domain.model.Song
import com.qamarq.jellymusic.ui.i18n.LocalAppLanguage
import com.qamarq.jellymusic.ui.i18n.commonUiCopy
import com.qamarq.jellymusic.ui.i18n.localizedCountLabel

@Composable
internal fun LibraryCollectionScreen(
    kind: LibraryCollectionKind,
    libraryState: LibraryUiState,
    playlists: List<Playlist>,
    songPlayCounts: Map<Long, Int>,
    favoriteSongIds: Set<Long>,
    albumCollectionLayoutMode: AlbumLayoutMode,
    songCollectionLayoutMode: AlbumLayoutMode,
    albumSortMode: AlbumSortMode,
    songSortMode: SongSortMode,
    currentSongId: Long?,
    isCurrentSongPlaying: Boolean,
    bottomPadding: Dp,
    onBack: () -> Unit,
    onAlbumSelected: (Album, ExpandOrigin) -> Unit,
    onAddAlbumToQueue: (Album) -> Unit,
    onSongSelected: (Song, List<Song>) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onAddAlbumToPlaylist: (Long, Album) -> Unit,
    onCreatePlaylist: (String) -> Long,
    playlistSongsById: Map<Long, Song>,
    onSetAlbumFavorite: (List<Long>, Boolean) -> Unit,
    onDeleteAlbumFromDevice: (Album) -> Unit,
    onAlbumCollectionLayoutModeChanged: (AlbumLayoutMode) -> Unit,
    onSongCollectionLayoutModeChanged: (AlbumLayoutMode) -> Unit,
    onAlbumSortModeChanged: (AlbumSortMode) -> Unit,
    onSongSortModeChanged: (SongSortMode) -> Unit,
    onGenreSelected: (String) -> Unit,
    onArtistSelected: (String) -> Unit,
) {
    val language = LocalAppLanguage.current
    val common = remember(language) { commonUiCopy(language) }
    when (kind) {
        LibraryCollectionKind.Songs -> SongCollectionScreen(
            songs = libraryState.songs,
            favoriteSongIds = favoriteSongIds,
            sortMode = songSortMode,
            currentSongId = currentSongId,
            isCurrentSongPlaying = isCurrentSongPlaying,
            bottomPadding = bottomPadding,
            onBack = onBack,
            onSortModeChanged = onSongSortModeChanged,
            onSongSelected = onSongSelected,
            onToggleFavorite = onToggleFavorite,
        )

        LibraryCollectionKind.Albums -> Box(modifier = Modifier.fillMaxSize()) {
            AlbumCollectionContent(
                albums = libraryState.albums,
                playlists = playlists,
                layoutMode = albumCollectionLayoutMode,
                sortMode = albumSortMode,
                topPadding = detailTopBarOccupiedHeight(),
                bottomPadding = bottomPadding,
                title = common.albums,
                subtitle = "Alphabetical by album artist, then album title",
                            onLayoutModeChanged = onAlbumCollectionLayoutModeChanged,
                            onSortModeChanged = onAlbumSortModeChanged,
                            onAlbumSelected = onAlbumSelected,
                            onAddAlbumToQueue = onAddAlbumToQueue,
                            onAddAlbumToPlaylist = onAddAlbumToPlaylist,
                onCreatePlaylist = onCreatePlaylist,
                playlistSongsById = playlistSongsById,
                favoriteSongIds = favoriteSongIds,
                onSetAlbumFavorite = onSetAlbumFavorite,
                onDeleteAlbumFromDevice = onDeleteAlbumFromDevice,
            )
            DetailListTopBar(
                title = common.albums,
                subtitle = localizedCountLabel(libraryState.albums.size, "album", language),
                onBack = onBack,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }

        LibraryCollectionKind.Artists -> ArtistCollectionScreen(
            songs = libraryState.songs,
            artistsList = libraryState.artists,
            bottomPadding = bottomPadding,
            onBack = onBack,
            onArtistSelected = onArtistSelected,
        )

        LibraryCollectionKind.Genres -> GenreCollectionScreen(
            songs = libraryState.songs,
            bottomPadding = bottomPadding,
            onBack = onBack,
            onGenreSelected = onGenreSelected,
        )
    }
}

