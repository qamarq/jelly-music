package com.qamarq.jellymusic.ui.screens

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import com.qamarq.jellymusic.R
import com.qamarq.jellymusic.domain.model.Playlist
import com.qamarq.jellymusic.domain.model.Song
import com.qamarq.jellymusic.ui.i18n.UiPhrase
import dev.chrisbanes.haze.HazeState
import org.xmlpull.v1.XmlPullParser

internal val aboutLogoImageCache = java.util.concurrent.ConcurrentHashMap<String, androidx.compose.ui.graphics.ImageBitmap>()

internal data class SongMenuActions(
    val playlists: List<Playlist> = emptyList(),
    val songsById: Map<Long, Song> = emptyMap(),
    val onAddToPlaylist: (playlistId: Long, song: Song) -> Unit = { _, _ -> },
    val onCreatePlaylist: (String) -> Long = { -1L },
    val onAddToQueue: (Song) -> Unit = {},
    val onDeleteFromLibrary: (Song) -> Unit = {},
    val deletePhrase: UiPhrase = UiPhrase.DeleteFromLibrary,
)

internal data class PendingSongDeletion(
    val songs: List<Song>,
    val parentDirectories: Set<String> = emptySet(),
)

internal val LocalSongMenuActions = compositionLocalOf { SongMenuActions() }

internal val LocalChromeHazeState = compositionLocalOf<HazeState?> { null }
internal val LocalPlayerHazeState = compositionLocalOf<HazeState?> { null }
internal val LocalUseSharedTopBarBackdrop = compositionLocalOf { false }
internal val LocalSharedTopBarController = compositionLocalOf<SharedTopBarController?> { null }
internal val LocalRenderSharedTopBarContent = compositionLocalOf { false }
internal val LocalSharedBackIconPainter = compositionLocalOf<Painter?> { null }
internal val LocalSharedTopMenuIconPainter = compositionLocalOf<Painter?> { null }

internal sealed interface SharedTopBarSpec {
    data class Unified(
        val title: String,
        val showSettings: Boolean,
        @DrawableRes val supplementalActionIconResId: Int? = null,
        val supplementalActionContentDescription: String? = null,
        val onSupplementalAction: (() -> Unit)? = null,
        val onOpenMenu: () -> Unit,
    ) : SharedTopBarSpec

    data class Back(
        val title: String,
        val onBack: () -> Unit,
        val centeredTitle: Boolean = false,
    ) : SharedTopBarSpec

    data class Detail(
        val title: String,
        val subtitle: String?,
        val onBack: () -> Unit,
        val actions: List<TopBarActionSpec> = emptyList(),
    ) : SharedTopBarSpec
}

internal fun SharedTopBarSpec.visualSignature(): String {
    return when (this) {
        is SharedTopBarSpec.Unified -> "unified|$showSettings|${supplementalActionIconResId ?: 0}|${supplementalActionContentDescription.orEmpty()}"
        is SharedTopBarSpec.Back -> "back|$title|$centeredTitle"
        is SharedTopBarSpec.Detail -> "detail|$title|${subtitle.orEmpty()}|${actions.joinToString { "${it.iconResId}:${it.contentDescription}" }}"
    }
}

internal data class SharedTopBarRegistration(
    val id: Any,
    val spec: SharedTopBarSpec,
)

internal class SharedTopBarController {
    var registration by mutableStateOf<SharedTopBarRegistration?>(null)
}

data class ExpandOrigin(
    val xFraction: Float = 0.5f,
    val yFraction: Float = 0.5f,
)

internal data class TopBarActionSpec(
    @DrawableRes val iconResId: Int,
    val contentDescription: String,
    val onClick: () -> Unit,
)

internal data class AboutScreenModel(
    val sections: List<AboutSection>,
)

internal data class AboutSection(
    val title: String,
    val description: String?,
    val entries: List<AboutEntry>,
)

internal data class AboutEntry(
    val title: String,
    val description: String?,
    val logoUri: String?,
    val links: List<AboutLink>,
)

internal data class AboutLink(
    val label: String,
    val url: String,
)

internal fun Context.loadAboutScreenModel(): AboutScreenModel {
    val parser = resources.getXml(R.xml.info_screen)
    val sections = mutableListOf<AboutSection>()
    var currentSectionTitle = ""
    var currentSectionDescription: String? = null
    var sectionOpen = false
    var currentEntries = mutableListOf<AboutEntry>()
    var currentEntryTitle: String? = null
    var currentEntryDescription: String? = null
    var currentEntryLogoUri: String? = null
    var currentLinks = mutableListOf<AboutLink>()

    fun closeEntry() {
        val entryTitle = currentEntryTitle
        if (!entryTitle.isNullOrBlank()) {
            val entry = AboutEntry(
                title = entryTitle,
                description = currentEntryDescription?.takeIf { it.isNotBlank() },
                logoUri = currentEntryLogoUri?.takeIf { it.isNotBlank() },
                links = currentLinks.toList(),
            )
            if (sectionOpen) {
                currentEntries += entry
            } else {
                sections += AboutSection(
                    title = entry.title,
                    description = null,
                    entries = listOf(entry),
                )
            }
        }
        currentEntryTitle = null
        currentEntryDescription = null
        currentEntryLogoUri = null
        currentLinks = mutableListOf()
    }

    fun closeSection() {
        closeEntry()
        if (sectionOpen && (currentSectionTitle.isNotBlank() || currentEntries.isNotEmpty())) {
            sections += AboutSection(
                title = currentSectionTitle,
                description = currentSectionDescription?.takeIf { it.isNotBlank() },
                entries = currentEntries.toList(),
            )
        }
        sectionOpen = false
        currentSectionTitle = ""
        currentSectionDescription = null
        currentEntries = mutableListOf()
    }

    while (parser.eventType != XmlPullParser.END_DOCUMENT) {
        when (parser.eventType) {
            XmlPullParser.START_TAG -> when (parser.name) {
                "section" -> {
                    closeSection()
                    sectionOpen = true
                    currentSectionTitle = parser.getAttributeValue(null, "title").orEmpty()
                    currentSectionDescription = parser.getAttributeValue(null, "description")
                }

                "entry" -> {
                    closeEntry()
                    currentEntryTitle = parser.getAttributeValue(null, "title")
                    currentEntryDescription = parser.getAttributeValue(null, "description")
                    currentEntryLogoUri = parser.getAttributeValue(null, "logoUrl")
                        ?: parser.getAttributeValue(null, "logoUri")
                }

                "link" -> {
                    val label = parser.getAttributeValue(null, "label").orEmpty()
                    val url = parser.getAttributeValue(null, "url").orEmpty()
                    if (label.isNotBlank() && url.isNotBlank()) {
                        currentLinks += AboutLink(label = label, url = url)
                    }
                }
            }

            XmlPullParser.END_TAG -> when (parser.name) {
                "entry" -> closeEntry()
                "section" -> closeSection()
            }
        }
        parser.next()
    }
    closeSection()
    return AboutScreenModel(sections = sections)
}
