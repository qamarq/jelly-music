package elovaire.music.app.data.lyrics

enum class SyncedLyricsTimingProfile {
    ExactIntervals,
}

data class LyricsIdentity(
    val title: String,
    val artist: String,
    val album: String,
    val durationMs: Long,
    val mediaId: String?,
    val contentUri: String?,
    val normalizedTitle: String,
    val normalizedArtist: String,
    val normalizedAlbum: String,
    val normalizedLookupKey: String,
    val cacheKeys: List<String>,
)

data class LyricsQueryVariant(
    val artist: String,
    val title: String,
    val album: String? = null,
)

data class LyricsSearchQuery(
    val identity: LyricsIdentity,
    val variants: List<LyricsQueryVariant>,
)

data class LyricsCandidate(
    val providerId: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationMs: Long?,
    val instrumental: Boolean,
    val plainLyrics: String,
    val syncedLyrics: String,
)

data class LyricsLine(
    val text: String,
    val startTimeMs: Long?,
    val endTimeMs: Long? = null,
    val index: Int = 0,
)

data class LyricsPayload(
    val lines: List<LyricsLine>,
    val isSynced: Boolean,
    val displayTimingOffsetMs: Long = 0L,
    val timingProfile: SyncedLyricsTimingProfile = SyncedLyricsTimingProfile.ExactIntervals,
    val providerName: String? = null,
    val confidence: Int = 0,
) {
    fun currentLineIndexAt(
        positionMs: Long,
        timingOffsetMs: Long = 0L,
        switchGraceMs: Long = 300L,
    ): Int? {
        if (!isSynced) return null
        val correctedPositionMs = (
            positionMs -
                displayTimingOffsetMs -
                timingOffsetMs -
                switchGraceMs
            ).coerceAtLeast(0L)
        val firstTimestampMs = lines.firstNotNullOfOrNull { it.startTimeMs } ?: return null
        if (correctedPositionMs < firstTimestampMs) return null
        return resolveActiveLyricLineIndex(
            lines = lines,
            positionMs = correctedPositionMs,
            timingOffsetMs = 0L,
        )
    }

    fun currentLineAt(
        positionMs: Long,
        timingOffsetMs: Long = 0L,
        switchGraceMs: Long = 300L,
    ): LyricsLine? = currentLineIndexAt(positionMs, timingOffsetMs, switchGraceMs)?.let(lines::get)
}

sealed interface LyricsResult {
    data class Found(val payload: LyricsPayload) : LyricsResult
    data object NotFound : LyricsResult
}

internal enum class LyricsLookupState {
    Idle,
    Loading,
    FoundSynced,
    FoundUnsynced,
    NotFound,
    Error,
}

internal data class LyricsLookupOutcome(
    val result: LyricsResult,
    val cacheTtlMs: Long?,
    val state: LyricsLookupState,
    val providerName: String? = null,
    val confidence: Int = 0,
)

internal data class LyricsCacheEntry(
    val result: LyricsResult,
    val expiresAtMillis: Long,
    val providerName: String? = null,
    val confidence: Int = 0,
) {
    fun isExpired(nowMillis: Long = System.currentTimeMillis()): Boolean = nowMillis >= expiresAtMillis
}

internal data class ProviderLyricsMatch(
    val payload: LyricsPayload,
    val confidence: Int,
    val providerName: String,
)

internal data class LocalLyricsMatch(
    val payload: LyricsPayload,
)
