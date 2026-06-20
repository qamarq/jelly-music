package com.qamarq.jellymusic.ui.screens.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qamarq.jellymusic.R
import com.qamarq.jellymusic.domain.model.Album
import com.qamarq.jellymusic.ui.components.*
import com.qamarq.jellymusic.ui.i18n.*
import com.qamarq.jellymusic.ui.screens.ExpandOrigin
import com.qamarq.jellymusic.ui.screens.home.AlbumGridCard
import com.qamarq.jellymusic.data.library.LibraryUiState

enum class LibraryCollectionKind {
    Songs, Albums, Artists, Genres
}

@Composable
internal fun LibraryHubScreen(
    libraryState: LibraryUiState,
    topPadding: Dp,
    bottomPadding: Dp,
    scrollToTopRequestVersion: Long,
    onOpenCollection: (LibraryCollectionKind) -> Unit,
    onOpenJellyfin: () -> Unit,
    onAlbumSelected: (Album, ExpandOrigin) -> Unit,
) {
    val language = LocalAppLanguage.current
    val common = remember(language) { commonUiCopy(language) }
    val totalSongs = libraryState.songs.size
    val totalAlbums = libraryState.albums.size
    
    val recentlyAddedAlbums = remember(libraryState.albums) {
        libraryState.albums.sortedByDescending { it.songs.maxOfOrNull { s -> s.dateAddedSeconds } ?: 0L }.take(8)
    }

    val listState = rememberLazyListState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                top = topPadding + 8.dp,
                end = 20.dp,
                bottom = bottomPadding + 12.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                ModuleCard {
                    Column {
                        LibraryHubRow(
                            iconResId = R.drawable.ic_lucide_music,
                            title = common.songs,
                            detail = "${localizedCountLabel(totalSongs, "song", language)} ${common.inYourLibrary}",
                            onClick = { onOpenCollection(LibraryCollectionKind.Songs) },
                        )
                        DividerLine()
                        LibraryHubRow(
                            iconResId = R.drawable.ic_lucide_disc_album,
                            title = common.albums,
                            detail = localizedCountLabel(totalAlbums, "album", language),
                            onClick = { onOpenCollection(LibraryCollectionKind.Albums) },
                        )
                        DividerLine()
                        LibraryHubRow(
                            iconResId = R.drawable.ic_lucide_settings_2,
                            title = "Jellyfin",
                            detail = "Browse your Jellyfin library",
                            onClick = onOpenJellyfin,
                        )
                    }
                }
            }

            if (recentlyAddedAlbums.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        SectionTitleRow(
                            title = "Recently Added",
                            subtitle = "Newest additions to your library"
                        )
                        recentlyAddedAlbums.chunked(2).forEach { rowAlbums ->
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                rowAlbums.forEach { album ->
                                    AlbumGridCard(
                                        album = album,
                                        modifier = Modifier.weight(1f),
                                        onOpen = { origin -> onAlbumSelected(album, origin) },
                                    )
                                }
                                if (rowAlbums.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LibraryHubRow(
    iconResId: Int,
    title: String,
    detail: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.88f),
            modifier = Modifier.size(20.dp),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = detail,
                style = MaterialTheme.typography.labelLarge,
                color = readableSecondaryTextColor(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_lucide_chevron_left),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            modifier = Modifier
                .size(18.dp)
                .rotate(180f),
        )
    }
}
