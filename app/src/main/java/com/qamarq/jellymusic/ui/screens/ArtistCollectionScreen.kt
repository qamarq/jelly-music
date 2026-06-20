package com.qamarq.jellymusic.ui.screens

import android.util.Log
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qamarq.jellymusic.domain.model.Artist
import com.qamarq.jellymusic.domain.model.Song
import com.qamarq.jellymusic.ui.i18n.LocalAppLanguage
import com.qamarq.jellymusic.ui.i18n.commonUiCopy
import com.qamarq.jellymusic.ui.i18n.localizedCountLabel
import com.qamarq.jellymusic.ui.theme.ElovaireSpacing

@Composable
internal fun ArtistCollectionScreen(
    songs: List<Song>,
    artistsList: List<Artist>,
    bottomPadding: Dp,
    onBack: () -> Unit,
    onArtistSelected: (String) -> Unit,
) {
    val language = LocalAppLanguage.current
    val common = remember(language) { commonUiCopy(language) }
    val scrollState = rememberElovaireScrollState("artist_collection")
    val artists = remember(songs) {
        songs
            .groupBy { it.artist.ifBlank { "Unknown Artist" } }
            .map { (name, artistSongs) ->
                ArtistEntry(
                    name = name,
                    artUri = artistSongs.firstOrNull()?.artUri,
                    albumCount = artistSongs.map { it.albumId }.distinct().size,
                    songCount = artistSongs.size,
                )
            }
            .sortedBy { it.name.lowercase() }
    }
//    val artists = remember(artistsList) {
//        artistsList
//            .map { (_, name, image) ->
//                val artistSongs = songs.filter { it.artist == name }
//                ArtistEntry(
//                    name = name,
//                    artUri = image,
//                    albumCount = artistSongs.map { it.albumId }.distinct().size,
//                    songCount = artistSongs.size
//                )
//            }
//    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    start = 20.dp,
                    top = detailTopBarOccupiedHeight() + ElovaireSpacing.detailListTopGap,
                    end = 20.dp,
                    bottom = bottomPadding,
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ModuleCard {
                Column {
                    artists.forEachIndexed { index, artist ->
                        ArtistRow(
                            artist = artist,
                            onClick = { onArtistSelected(artist.name) },
                        )
                        if (index != artists.lastIndex) {
                            DividerLine()
                        }
                    }
                }
            }
        }
        FastScrollbar(
            state = scrollState,
            topInset = detailTopBarOccupiedHeight() + ElovaireSpacing.detailCompactTopGap,
            bottomInset = bottomPadding + 16.dp,
        )

        DetailListTopBar(
            title = common.artists,
            subtitle = localizedCountLabel(artists.size, "artist", language),
            onBack = onBack,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

