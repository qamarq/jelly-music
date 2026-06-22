package com.qamarq.jellymusic.ui.screens.home

import android.net.Uri
import com.qamarq.jellymusic.ui.screens.ExpandOrigin
import com.qamarq.jellymusic.ui.screens.SectionTitleRow
import com.qamarq.jellymusic.ui.screens.ModuleCard
import com.qamarq.jellymusic.ui.screens.PlaylistArtworkPreview
import com.qamarq.jellymusic.ui.screens.readableSecondaryTextColor
import com.qamarq.jellymusic.ui.screens.DividerLine
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qamarq.jellymusic.R
import com.qamarq.jellymusic.domain.model.Album
import com.qamarq.jellymusic.domain.model.Playlist
import com.qamarq.jellymusic.domain.model.Song
import com.qamarq.jellymusic.ui.components.*
import com.qamarq.jellymusic.ui.i18n.*
import com.qamarq.jellymusic.ui.motion.ElovaireAnimatedContent
import com.qamarq.jellymusic.ui.motion.ElovaireMotion
import com.qamarq.jellymusic.ui.theme.*
import com.qamarq.jellymusic.data.playback.PlaybackUiState
import com.qamarq.jellymusic.domain.model.Artist
import kotlinx.coroutines.delay

enum class HomeScreenState {
    Loading,
    Empty,
    Content,
}

@Composable
fun HomeScreen(
    lastPlayedAlbum: Album?,
    lastPlayedPlaylist: Playlist?,
    songsById: Map<Long, Song>,
    recentlyAddedAlbums: List<Album>,
    recentSongs: List<Song>,
    topPlayedSongs: List<Song> = emptyList(),
    recentlyPlayedPlaylists: List<Playlist> = emptyList(),
    favoriteAlbums: List<Album>,
    jellyfinAlbums: List<Album> = emptyList(),
    recentlyPlayedJellyfinAlbums: List<Album> = emptyList(),
    jellyfinArtists: List<Artist> = emptyList(),
    isJellyfinConnected: Boolean = false,
    playbackState: PlaybackUiState,
    isLibraryLoading: Boolean,
    libraryScanProgress: Float,
    favoriteSongIds: Set<Long>,
    topPadding: Dp,
    bottomPadding: Dp,
    scrollToTopRequestVersion: Long,
    playInitialReveal: Boolean,
    onInitialRevealFinished: () -> Unit,
    onAlbumSelected: (Album, ExpandOrigin) -> Unit,
    onPlaylistSelected: (Playlist) -> Unit,
    onPlayAlbum: (Album) -> Unit,
    onPlayPlaylist: (Playlist, List<Song>) -> Unit,
    onSongSelected: (Song) -> Unit,
    onPlayTopSong: (Song) -> Unit = {},
    onToggleFavorite: (Long) -> Unit,
    onConnectJellyfin: () -> Unit = {},
    onArtistSelected: (String) -> Unit,
) {
    val listState = rememberLazyListState()
    val language = LocalAppLanguage.current
    val homeCopy = remember(language) { homeCopy(language) }
    var revealModules by rememberSaveable(playInitialReveal) { mutableStateOf(!playInitialReveal) }
    
    // Theme-adaptive background blur color based on the first featured album
    val featuredAlbum = jellyfinAlbums.firstOrNull() ?: lastPlayedAlbum
    val featuredGradient = rememberArtworkGradient(featuredAlbum?.artUri).value
    val bgBaseColor = MaterialTheme.colorScheme.background
    
    LaunchedEffect(playInitialReveal) {
        if (playInitialReveal) {
            revealModules = false
            delay(100)
            revealModules = true
            onInitialRevealFinished()
        } else {
            revealModules = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Immersive background blur
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(bgBaseColor)
            }
            if (featuredGradient.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                        .blur(100.dp)
                        .alpha(0.4f)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(featuredGradient.first(), Color.Transparent)
                            )
                        )
                )
            }
        }

        ElovaireAnimatedContent(
            targetState = when {
                isLibraryLoading && jellyfinAlbums.isEmpty() && recentlyAddedAlbums.isEmpty() -> HomeScreenState.Loading
                !isLibraryLoading && jellyfinAlbums.isEmpty() && recentlyAddedAlbums.isEmpty() && recentSongs.isEmpty() -> HomeScreenState.Empty
                else -> HomeScreenState.Content
            },
            label = "HomeLoadingTransition",
        ) { state ->
            when (state) {
                HomeScreenState.Loading -> HomeLoadingView(homeCopy, libraryScanProgress)
                HomeScreenState.Empty -> HomeEmptyView(homeCopy)
                HomeScreenState.Content -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = topPadding + 8.dp,
                            bottom = bottomPadding + 100.dp,
                        ),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        // Jellyfin connect banner (when not connected)
                        if (!isJellyfinConnected) {
                            item(key = "jellyfin_connect") {
                                JellyfinConnectCard(
                                    modifier = Modifier.padding(horizontal = 20.dp),
                                    onConnect = onConnectJellyfin,
                                )
                            }
                        }

                        // Featured Jellyfin Section
                        if (jellyfinAlbums.isNotEmpty()) {
                            item(key = "featured_jellyfin") {
                                FeaturedJellyfinSection(
                                    albums = jellyfinAlbums.take(5),
                                    onAlbumSelected = onAlbumSelected,
                                    onPlayAlbum = onPlayAlbum
                                )
                            }
                        }

                        // Quick Picks (Spotify-like 2x3 grid: recently listened Jellyfin albums and playlists)
                        item(key = "quick_picks") {
                            val quickPicks = buildList {
                                recentlyPlayedJellyfinAlbums.forEach { add(HomeQuickPick.AlbumPick(it)) }
                                recentlyPlayedPlaylists.forEach { playlist ->
                                    add(
                                        HomeQuickPick.PlaylistPick(
                                            playlist = playlist,
                                            songs = playlist.songIds.mapNotNull(songsById::get),
                                        ),
                                    )
                                }
                            }.distinctBy { it.key }.take(6)
                            if (quickPicks.isNotEmpty()) {
                                QuickPicksGrid(
                                    items = quickPicks,
                                    onAlbumSelected = onAlbumSelected,
                                    onPlaylistSelected = onPlaylistSelected,
                                )
                            }
                        }

                        // Artists row (circles)
                        if (jellyfinArtists.isNotEmpty()) {
                            item(key = "artists_row") {
                                ArtistsCircleRow(
                                    artists = jellyfinArtists,
                                    modifier = Modifier.padding(horizontal = 20.dp),
                                    onArtistSelected
                                )
                            }
                        }

                        // Most Played songs
                        if (topPlayedSongs.isNotEmpty()) {
                            item(key = "most_played") {
                                MostPlayedSongsSection(
                                    songs = topPlayedSongs,
                                    onSongSelected = onPlayTopSong,
                                )
                            }
                        }

                        // Continue Listening / Last Played
                        if (lastPlayedAlbum != null || lastPlayedPlaylist != null) {
                            item(key = "continue_listening") {
                                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                                    SectionTitleRow(
                                        title = "Continue Listening",
                                        subtitle = "Jump back in where you left off"
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    if (lastPlayedAlbum != null) {
                                        LastPlayedAlbumModule(
                                            album = lastPlayedAlbum,
                                            onOpen = { onAlbumSelected(lastPlayedAlbum, it) },
                                            onPlay = { onPlayAlbum(lastPlayedAlbum) }
                                        )
                                    } else if (lastPlayedPlaylist != null) {
                                        val songs = lastPlayedPlaylist.songIds.mapNotNull { songsById[it] }
                                        LastPlayedPlaylistModule(
                                            playlist = lastPlayedPlaylist,
                                            songs = songs,
                                            onOpen = { onPlaylistSelected(lastPlayedPlaylist) },
                                            onPlay = { onPlayPlaylist(lastPlayedPlaylist, songs) }
                                        )
                                    }
                                }
                            }
                        }

                        // Jellyfin Favorites
                        if (favoriteAlbums.isNotEmpty()) {
                            item(key = "jellyfin_favorites") {
                                HorizontalAlbumSection(
                                    title = "Jellyfin Favorites",
                                    albums = favoriteAlbums,
                                    onAlbumSelected = onAlbumSelected
                                )
                            }
                        }

                        // Recently Played Songs
                        item(key = "home_recently_played") {
                            Column(modifier = Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_lucide_circle_play),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp),
                                    )
                                    Text(
                                        text = homeCopy.recentlyPlayedSongsTitle,
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    )
                                }
                                if (recentSongs.isEmpty()) {
                                    Text(
                                        text = homeCopy.recentlyPlayedSongsEmpty,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = readableSecondaryTextColor(),
                                    )
                                } else {
                                    ModuleCard {
                                        Column {
                                            recentSongs.take(5).forEachIndexed { index, song ->
                                                HomeRecentSongRow(
                                                    song = song,
                                                    isFavorite = song.id in favoriteSongIds,
                                                    onClick = { onSongSelected(song) },
                                                    onToggleFavorite = { onToggleFavorite(song.id) },
                                                    showDivider = index != recentSongs.take(5).lastIndex,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Local Library (as addition)
                        if (recentlyAddedAlbums.isNotEmpty()) {
                            item(key = "local_library") {
                                HorizontalAlbumSection(
                                    title = "Local Library",
                                    subtitle = "Recently added to your device",
                                    albums = recentlyAddedAlbums,
                                    onAlbumSelected = onAlbumSelected
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturedJellyfinSection(
    albums: List<Album>,
    onAlbumSelected: (Album, ExpandOrigin) -> Unit,
    onPlayAlbum: (Album) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Featured for You",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(albums) { album ->
                FeaturedAlbumCard(
                    album = album,
                    onClick = { onAlbumSelected(album, ExpandOrigin()) },
                    onPlay = { onPlayAlbum(album) }
                )
            }
        }
    }
}

@Composable
fun FeaturedAlbumCard(
    album: Album,
    onClick: () -> Unit,
    onPlay: () -> Unit
) {
    val gradient = rememberArtworkGradient(album.artUri).value
    val cardColor = if (gradient.isNotEmpty()) gradient.first().copy(alpha = 0.8f).compositeOver(Color.Black) else MaterialTheme.colorScheme.surface
    
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
    ) {
        ArtworkImage(
            uri = album.artUri,
            title = album.title,
            modifier = Modifier.fillMaxSize(),
            cornerRadius = 0.dp
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        startY = 50f
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = album.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Color.White),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = album.artist,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        IconButton(
            onClick = onPlay,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
                .size(44.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_lucide_play),
                contentDescription = "Play",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

internal sealed class HomeQuickPick(val key: String) {
    data class AlbumPick(val album: Album) : HomeQuickPick("album:${album.id}")
    data class PlaylistPick(val playlist: Playlist, val songs: List<Song>) : HomeQuickPick("playlist:${playlist.id}")
}

@Composable
internal fun QuickPicksGrid(
    items: List<HomeQuickPick>,
    onAlbumSelected: (Album, ExpandOrigin) -> Unit,
    onPlaylistSelected: (Playlist) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        items.chunked(2).forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { pick ->
                    QuickPickItem(
                        pick = pick,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            when (pick) {
                                is HomeQuickPick.AlbumPick -> onAlbumSelected(pick.album, ExpandOrigin())
                                is HomeQuickPick.PlaylistPick -> onPlaylistSelected(pick.playlist)
                            }
                        },
                    )
                }
                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
internal fun QuickPickItem(
    pick: HomeQuickPick,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(56.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            when (pick) {
                is HomeQuickPick.AlbumPick -> ArtworkImage(
                    uri = pick.album.artUri,
                    title = pick.album.title,
                    modifier = Modifier.size(56.dp),
                    cornerRadius = 0.dp
                )
                is HomeQuickPick.PlaylistPick -> PlaylistArtworkPreview(
                    songs = pick.songs,
                    title = pick.playlist.name,
                    modifier = Modifier.size(56.dp),
                )
            }
            Text(
                text = when (pick) {
                    is HomeQuickPick.AlbumPick -> pick.album.title
                    is HomeQuickPick.PlaylistPick -> pick.playlist.name
                },
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 12.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun HorizontalAlbumSection(
    title: String,
    subtitle: String? = null,
    albums: List<Album>,
    onAlbumSelected: (Album, ExpandOrigin) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitleRow(
            title = title,
            subtitle = subtitle,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(albums) { album ->
                AlbumGridCard(
                    album = album,
                    modifier = Modifier.width(140.dp),
                    onOpen = { onAlbumSelected(album, it) }
                )
            }
        }
    }
}

@Composable
internal fun HomeLoadingView(copy: HomeCopy, progress: Float) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(18.dp))
        Text(text = copy.indexingTitle, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
internal fun HomeEmptyView(copy: HomeCopy) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = copy.emptyLibraryTitle, style = MaterialTheme.typography.titleLarge)
        Text(text = copy.emptyLibraryMessage, textAlign = TextAlign.Center)
    }
}

// These were extracted from the main file logic
@Composable
fun LastPlayedAlbumModule(
    album: Album,
    onOpen: (ExpandOrigin) -> Unit,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val gradient = rememberArtworkGradient(album.artUri).value
    val darkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val baseTint = if (darkTheme) Color(0xFF141414).copy(alpha = 0.82f) else Color.White.copy(alpha = 0.82f)
    val albumTint = if (gradient.isNotEmpty()) gradient.first().copy(alpha = 0.46f) else Color.Transparent

    Box(
        modifier = modifier
            .clickable(onClick = { onOpen(ExpandOrigin()) })
            .clip(RoundedCornerShape(ElovaireRadii.module))
            .background(baseTint)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = if (darkTheme) 0.05f else 0.04f),
                shape = RoundedCornerShape(ElovaireRadii.module),
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ArtworkImage(
                uri = album.artUri,
                title = album.title,
                modifier = Modifier.size(88.dp),
                cornerRadius = ElovaireRadii.artwork,
                showArtworkGlow = true,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = album.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = album.artist,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            IconButton(onClick = onPlay, modifier = Modifier.background(MaterialTheme.colorScheme.primary, CircleShape)) {
                Icon(painterResource(R.drawable.ic_lucide_play), contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun LastPlayedPlaylistModule(
    playlist: Playlist,
    songs: List<Song>,
    onOpen: () -> Unit,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Similar to Album module but for playlist
    ModuleCard(modifier = modifier.clickable(onClick = onOpen)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            PlaylistArtworkPreview(songs = songs, title = playlist.name, modifier = Modifier.size(88.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(playlist.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("${songs.size} songs", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onPlay, modifier = Modifier.background(MaterialTheme.colorScheme.primary, CircleShape)) {
                Icon(painterResource(R.drawable.ic_lucide_play), contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun HomeRecentSongRow(
    song: Song,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    showDivider: Boolean,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ArtworkImage(
                uri = song.artUri,
                title = song.title,
                modifier = Modifier.size(48.dp),
                cornerRadius = ElovaireRadii.artworkSmall,
            )
            Column(modifier = Modifier.weight(1f)) {
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
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    painter = painterResource(if (isFavorite) R.drawable.ic_lucide_star_filled else R.drawable.ic_lucide_star),
                    contentDescription = null,
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (showDivider) DividerLine()
    }
}

@Composable
fun AlbumGridCard(
    album: Album,
    modifier: Modifier = Modifier,
    onOpen: (ExpandOrigin) -> Unit,
) {
    Column(
        modifier = modifier
            .clickable(onClick = { onOpen(ExpandOrigin()) })
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ArtworkImage(
            uri = album.artUri,
            title = album.title,
            modifier = Modifier.aspectRatio(1f).fillMaxWidth(),
            cornerRadius = ElovaireRadii.artwork,
            showArtworkGlow = true,
        )
        Column {
            Text(
                text = album.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
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

@Composable
internal fun MostPlayedSongsSection(
    songs: List<Song>,
    onSongSelected: (Song) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitleRow(title = "Most Played", modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(songs, key = { it.id }) { song ->
                Column(
                    modifier = Modifier
                        .width(120.dp)
                        .clickable(onClick = { onSongSelected(song) }),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ArtworkImage(
                        uri = song.artUri,
                        title = song.title,
                        modifier = Modifier.aspectRatio(1f).fillMaxWidth(),
                        cornerRadius = ElovaireRadii.artwork,
                        showArtworkGlow = true,
                    )
                    Column {
                        Text(
                            text = song.title,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
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
                }
            }
        }
    }
}

@Composable
internal fun ArtistsCircleRow(
    artists: List<Artist>,
    modifier: Modifier = Modifier,
    onArtistSelected: (String) -> Unit
) {
    Column(modifier = modifier) {
        SectionTitleRow(title = "Artists")
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(artists) { artist ->
                ArtistCircleItem(name = artist.name, uri = artist.image, onArtistSelected)
            }
        }
    }
}

@Composable
private fun ArtistCircleItem(name: String, uri: Uri?, onArtistSelected: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(72.dp)
            .clickable(onClick = { onArtistSelected(name) })
    ) {
        ArtworkImage(
            uri = uri,
            title = name,
            modifier = Modifier.size(50.dp),
            cornerRadius = ElovaireRadii.pill,
        )

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
internal fun JellyfinConnectCard(
    onConnect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "J", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Connect Jellyfin",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                )
                Text(
                    text = "Stream your media server library",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.clickable(onClick = onConnect),
            ) {
                Text(
                    text = "Set up",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                )
            }
        }
    }
}
