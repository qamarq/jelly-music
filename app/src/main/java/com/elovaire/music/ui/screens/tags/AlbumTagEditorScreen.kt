package elovaire.music.droidbeauty.app.ui.screens.tags

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import elovaire.music.droidbeauty.app.R
import elovaire.music.droidbeauty.app.data.tags.AlbumTagEditRequest
import elovaire.music.droidbeauty.app.data.tags.AlbumTagMatchSuggestion
import elovaire.music.droidbeauty.app.data.tags.EditableAlbumTrack
import elovaire.music.droidbeauty.app.domain.model.Album
import elovaire.music.droidbeauty.app.domain.model.AppLanguage
import elovaire.music.droidbeauty.app.ui.components.ArtworkImage
import elovaire.music.droidbeauty.app.ui.motion.ElovaireMotion
import elovaire.music.droidbeauty.app.ui.theme.ElovaireRadii
import elovaire.music.droidbeauty.app.ui.theme.ElovaireSpacing
import elovaire.music.droidbeauty.app.ui.theme.RoseAccent
import elovaire.music.droidbeauty.app.ui.theme.elovaireScaledSp
import java.io.File

internal data class AlbumTagEditorDraft(
    val albumTitle: String,
    val albumArtist: String,
    val releaseYear: String,
    val coverArtUri: Uri?,
    val tracks: List<EditableAlbumTrack>,
)

@Composable
internal fun AlbumTagEditorScreen(
    album: Album?,
    appLanguage: AppLanguage,
    isSaving: Boolean,
    isMatching: Boolean,
    statusMessage: String?,
    onBack: () -> Unit,
    onSave: (AlbumTagEditRequest) -> Unit,
    onAutoMatch: () -> Unit,
    onPickCoverArt: () -> Unit,
    autofillSuggestion: AlbumTagMatchSuggestion?,
    pickedCoverArtUri: Uri?,
) {
    if (album == null) {
        LaunchedEffect(Unit) { onBack() }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = tagEditorCopy(appLanguage).albumNotFound,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        return
    }

    val context = LocalContext.current
    val copy = remember(appLanguage) { tagEditorCopy(appLanguage) }
    var albumTitle by remember(album.id) { mutableStateOf(album.title) }
    var albumArtist by remember(album.id) { mutableStateOf(album.artist) }
    var releaseYear by remember(album.id) { mutableStateOf(album.songs.firstNotNullOfOrNull { it.releaseYear }?.toString().orEmpty()) }
    var selectedCoverArtUri by remember(album.id) { mutableStateOf(album.artUri) }
    val trackDrafts = remember(album.id) {
        mutableStateListOf<EditableAlbumTrack>().apply {
            addAll(
                album.songs.mapIndexed { index, song ->
                    EditableAlbumTrack(
                        songId = song.id,
                        title = song.title,
                        artist = song.artist,
                        trackNumber = song.trackNumber.takeIf { it > 0 } ?: index + 1,
                        discNumber = song.discNumber.takeIf { it > 0 } ?: 1,
                    )
                },
            )
        }
    }

    LaunchedEffect(autofillSuggestion) {
        val suggestion = autofillSuggestion ?: return@LaunchedEffect
        albumTitle = suggestion.albumTitle
        albumArtist = suggestion.albumArtist
        releaseYear = suggestion.releaseYear?.toString().orEmpty()
        selectedCoverArtUri = suggestion.coverArtBytes?.let { bytes ->
            createCoverPreviewFile(context.cacheDir, album.id, bytes)
        } ?: selectedCoverArtUri
        trackDrafts.clear()
        trackDrafts.addAll(suggestion.tracks)
    }
    LaunchedEffect(pickedCoverArtUri) {
        pickedCoverArtUri?.let { selectedCoverArtUri = it }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 18.dp,
                top = editorTopBarHeight() + 16.dp,
                end = 18.dp,
                bottom = 28.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(ElovaireRadii.module),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.42f),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.Top,
                        ) {
                            EditableArtworkCard(
                                artworkUri = selectedCoverArtUri,
                                fallbackArtworkUri = album.artUri,
                                title = albumTitle,
                                onClick = onPickCoverArt,
                            )
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                Text(
                                    text = copy.albumSection,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    text = copy.changeCoverHint,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.76f),
                                )
                                AccentPillButton(
                                    label = copy.changeCover,
                                    iconResId = R.drawable.ic_lucide_disc_album,
                                    onClick = onPickCoverArt,
                                )
                            }
                        }

                        OutlinedTextField(
                            value = albumTitle,
                            onValueChange = { albumTitle = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(copy.albumTitle) },
                            singleLine = true,
                        )
                        OutlinedTextField(
                            value = albumArtist,
                            onValueChange = { albumArtist = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(copy.albumArtist) },
                            singleLine = true,
                        )
                        OutlinedTextField(
                            value = releaseYear,
                            onValueChange = { input ->
                                releaseYear = input.filter(Char::isDigit).take(4)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(copy.releaseYear) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                    }
                }
            }

            item {
                Surface(
                    shape = RoundedCornerShape(ElovaireRadii.module),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.36f),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Text(
                                    text = copy.autoMatchTitle,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    text = copy.autoMatchSubtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.76f),
                                )
                            }
                            AccentPillButton(
                                label = copy.findOnline,
                                iconResId = R.drawable.ic_lucide_search,
                                enabled = !isMatching && !isSaving,
                                loading = isMatching,
                                onClick = onAutoMatch,
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = copy.songSection,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 2.dp),
                )
            }

            itemsIndexed(trackDrafts, key = { _, track -> track.songId }) { index, track ->
                Surface(
                    shape = RoundedCornerShape(ElovaireRadii.card),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.28f),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "${copy.track} ${index + 1}",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = album.songs.getOrNull(index)?.fileName.orEmpty(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                        OutlinedTextField(
                            value = track.title,
                            onValueChange = { value ->
                                trackDrafts[index] = track.copy(title = value)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(copy.songTitle) },
                            singleLine = true,
                        )
                        OutlinedTextField(
                            value = track.artist,
                            onValueChange = { value ->
                                trackDrafts[index] = track.copy(artist = value)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(copy.songArtist) },
                            singleLine = true,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            OutlinedTextField(
                                value = track.trackNumber.toString(),
                                onValueChange = { value ->
                                    val parsed = value.filter(Char::isDigit).toIntOrNull()
                                    trackDrafts[index] = track.copy(trackNumber = parsed ?: track.trackNumber)
                                },
                                modifier = Modifier.weight(1f),
                                label = { Text(copy.trackNumber) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            )
                            OutlinedTextField(
                                value = track.discNumber.toString(),
                                onValueChange = { value ->
                                    val parsed = value.filter(Char::isDigit).toIntOrNull()
                                    trackDrafts[index] = track.copy(discNumber = parsed ?: track.discNumber)
                                },
                                modifier = Modifier.weight(1f),
                                label = { Text(copy.discNumber) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            )
                        }
                    }
                }
            }

            item {
                AnimatedVisibility(
                    visible = !statusMessage.isNullOrBlank(),
                    enter = fadeIn(animationSpec = ElovaireMotion.standardTween(durationMillis = 120)),
                    exit = fadeOut(animationSpec = ElovaireMotion.standardTween(durationMillis = 120)),
                ) {
                    Text(
                        text = statusMessage.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                    )
                }
            }
        }

        AlbumTagEditorTopBar(
            title = copy.editorTitle,
            subtitle = album.title,
            onBack = onBack,
            onAutoMatch = onAutoMatch,
            onSave = {
                onSave(
                    AlbumTagEditRequest(
                        album = album,
                        albumTitle = albumTitle,
                        albumArtist = albumArtist,
                        releaseYear = releaseYear.toIntOrNull(),
                        coverArtUri = selectedCoverArtUri,
                        tracks = trackDrafts.toList(),
                    ),
                )
            },
            matching = isMatching,
            saving = isSaving,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Composable
private fun EditableArtworkCard(
    artworkUri: Uri?,
    fallbackArtworkUri: Uri?,
    title: String,
    onClick: () -> Unit,
) {
    val previewBitmap = rememberPreviewBitmap(artworkUri, fallbackArtworkUri)
    Surface(
        modifier = Modifier
            .size(126.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        shape = RoundedCornerShape(ElovaireRadii.module),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.24f),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                previewBitmap != null -> {
                    Image(
                        bitmap = previewBitmap,
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
                else -> {
                    ArtworkImage(
                        uri = fallbackArtworkUri,
                        title = title,
                        modifier = Modifier.fillMaxSize(),
                        cornerRadius = ElovaireRadii.module,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.16f)),
            )
        }
    }
}

@Composable
private fun rememberPreviewBitmap(
    selectedUri: Uri?,
    fallbackUri: Uri?,
): ImageBitmap? {
    val context = LocalContext.current
    var bitmap by remember(selectedUri, fallbackUri) { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(selectedUri, fallbackUri) {
        bitmap = selectedUri?.let { uri ->
            runCatching {
                context.contentResolver.openInputStream(uri)?.use { input ->
                    BitmapFactory.decodeStream(input)?.asImageBitmap()
                }
            }.getOrNull()
        }
    }
    return bitmap
}

@Composable
private fun AlbumTagEditorTopBar(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
    onAutoMatch: () -> Unit,
    onSave: () -> Unit,
    matching: Boolean,
    saving: Boolean,
    modifier: Modifier = Modifier,
) {
    val darkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = if (darkTheme) MaterialTheme.colorScheme.surface.copy(alpha = 0.86f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 14.dp, end = 14.dp, top = 3.dp, bottom = 13.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            EditorTopBarIconButton(
                iconResId = R.drawable.ic_lucide_chevron_left,
                contentDescription = "Back",
                onClick = onBack,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            EditorTopBarIconButton(
                iconResId = R.drawable.ic_lucide_search,
                contentDescription = "Find online",
                onClick = onAutoMatch,
                enabled = !matching && !saving,
                loading = matching,
            )
            EditorTopBarIconButton(
                iconResId = R.drawable.ic_lucide_check,
                contentDescription = "Save",
                onClick = onSave,
                enabled = !matching && !saving,
                loading = saving,
            )
        }
    }
}

@Composable
private fun EditorTopBarIconButton(
    iconResId: Int,
    contentDescription: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (pressed && enabled) 0.88f else 1f,
        animationSpec = if (pressed && enabled) ElovaireMotion.pressDownSpec() else ElovaireMotion.releaseSpringSpec(),
        label = "editorTopBarActionScale",
    )
    Box(
        modifier = Modifier
            .size(40.dp)
            .scale(scale)
            .clickable(
                enabled = enabled && !loading,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onSurface,
            )
        } else {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(19.dp),
            )
        }
    }
}

@Composable
private fun AccentPillButton(
    label: String,
    iconResId: Int,
    onClick: () -> Unit,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (pressed && enabled) 0.94f else 1f,
        animationSpec = if (pressed && enabled) ElovaireMotion.pressDownSpec() else ElovaireMotion.releaseSpringSpec(),
        label = "tagEditorPillScale",
    )
    Surface(
        modifier = Modifier.scale(scale),
        shape = RoundedCornerShape(ElovaireRadii.pill),
        color = RoseAccent.copy(alpha = if (enabled) 1f else 0.55f),
        onClick = onClick,
        enabled = enabled && !loading,
        interactionSource = interactionSource,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 11.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp),
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = elovaireScaledSp(15f),
                    fontWeight = FontWeight.SemiBold,
                ),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
private fun editorTopBarHeight() = ElovaireSpacing.topBarContentHeight + 28.dp

private fun createCoverPreviewFile(
    cacheDir: File,
    albumId: Long,
    bytes: ByteArray,
): Uri {
    val tempDir = File(cacheDir, "album-tag-cover-preview").apply { mkdirs() }
    val previewFile = File(tempDir, "album-$albumId-${System.nanoTime()}.img")
    previewFile.writeBytes(bytes)
    return Uri.fromFile(previewFile)
}

private data class AlbumTagEditorCopy(
    val editorTitle: String,
    val albumNotFound: String,
    val albumSection: String,
    val albumTitle: String,
    val albumArtist: String,
    val releaseYear: String,
    val changeCover: String,
    val changeCoverHint: String,
    val autoMatchTitle: String,
    val autoMatchSubtitle: String,
    val findOnline: String,
    val songSection: String,
    val track: String,
    val songTitle: String,
    val songArtist: String,
    val trackNumber: String,
    val discNumber: String,
)

private fun tagEditorCopy(language: AppLanguage): AlbumTagEditorCopy {
    return when (language) {
        AppLanguage.Polish -> AlbumTagEditorCopy("Edytuj tagi", "Nie znaleziono albumu.", "Tagi albumu", "Tytuł albumu", "Artysta albumu", "Rok wydania", "Zmień okładkę", "Dotknij, aby wybrać nową okładkę albumu.", "Dopasowanie online", "Wyszukaj metadane albumu i utworów online.", "Znajdź online", "Utwory", "Utwór", "Tytuł utworu", "Artysta utworu", "Numer ścieżki", "Numer dysku")
        else -> AlbumTagEditorCopy("Edit tags", "Album not found.", "Album tags", "Album title", "Album artist", "Release year", "Change cover", "Tap to choose new album artwork.", "Online match", "Find album and track metadata online.", "Find online", "Songs", "Track", "Song title", "Song artist", "Track number", "Disc number")
    }
}
