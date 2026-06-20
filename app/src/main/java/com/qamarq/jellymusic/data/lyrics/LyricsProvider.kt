package com.qamarq.jellymusic.data.lyrics

internal interface LyricsProvider {
    val providerName: String

    suspend fun search(query: LyricsSearchQuery): List<LyricsCandidate>

    suspend fun getLyrics(
        candidate: LyricsCandidate,
        identity: LyricsIdentity,
    ): ProviderLyricsMatch?
}
