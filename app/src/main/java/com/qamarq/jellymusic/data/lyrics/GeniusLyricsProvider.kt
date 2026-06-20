package com.qamarq.jellymusic.data.lyrics

import android.util.Log
import com.qamarq.jellymusic.BuildConfig
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

internal class GeniusLyricsProvider(
    private val accessToken: String = BuildConfig.GENIUS_ACCESS_TOKEN,
) : LyricsProvider {
    override val providerName: String = "Genius"

    fun isConfigured(): Boolean = accessToken.isNotBlank()

    override suspend fun search(query: LyricsSearchQuery): List<LyricsCandidate> {
        if (!isConfigured()) return emptyList()
        val variants = query.variants.take(MAX_QUERY_VARIANTS)
        if (variants.isEmpty()) return emptyList()

        return coroutineScope {
            variants
                .map { variant ->
                    async {
                        searchHits(
                            listOf(variant.artist, variant.title, variant.album.orEmpty())
                                .filter { it.isNotBlank() }
                                .joinToString(" "),
                        )
                    }
                }
                .awaitAll()
                .flatten()
        }.distinctBy(LyricsCandidate::providerId)
    }

    override suspend fun getLyrics(
        candidate: LyricsCandidate,
        identity: LyricsIdentity,
    ): ProviderLyricsMatch? {
        val lyricsUrl = candidate.sourceUrl?.takeIf { it.isNotBlank() } ?: return null
        val html = getText(
            url = lyricsUrl,
            accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        ) ?: return null
        val plainLyrics = extractPlainLyrics(html)
        val lines = parsePlainLyrics(plainLyrics).orEmpty()
        if (lines.isEmpty()) return null
        val score = candidate.scoreAgainst(identity)
        return ProviderLyricsMatch(
            payload = LyricsPayload(
                lines = lines,
                isSynced = false,
                providerName = providerName,
                confidence = score,
            ),
            confidence = score,
            providerName = providerName,
        )
    }

    private fun searchHits(query: String): List<LyricsCandidate> {
        if (query.isBlank()) return emptyList()
        val response = getJsonObject(buildSearchUrl(query)) ?: return emptyList()
        val hits = response.optJSONObject("response")?.optJSONArray("hits") ?: return emptyList()
        return buildList {
            repeat(hits.length()) { index ->
                hits.optJSONObject(index)
                    ?.optJSONObject("result")
                    ?.toLyricsCandidate()
                    ?.let(::add)
            }
        }
    }

    private fun extractPlainLyrics(html: String): String {
        val structuredBlocks = extractStructuredLyricsBlocks(html)
        if (structuredBlocks.isNotEmpty()) {
            return structuredBlocks.joinToString("\n") { fragment -> normalizeLyricsHtmlFragment(fragment) }
        }

        val fragments = LYRICS_CONTAINER_REGEX
            .findAll(html)
            .map { match -> match.groupValues[1] }
            .toList()
        if (fragments.isEmpty()) return ""
        return fragments.joinToString("\n", transform = ::normalizeLyricsHtmlFragment)
    }

    private fun extractStructuredLyricsBlocks(html: String): List<String> {
        val directBlocks = HTML_VALUE_REGEX
            .findAll(html)
            .mapNotNull { decodeEscapedJsonString(it.groupValues[1]) }
            .filter { it.contains("<br", ignoreCase = true) || it.contains('\n') }
            .toList()
        if (directBlocks.isNotEmpty()) return directBlocks

        return ESCAPED_HTML_VALUE_REGEX
            .findAll(html)
            .mapNotNull { decodeDoublyEscapedJsonString(it.groupValues[1]) }
            .filter { it.contains("<br", ignoreCase = true) || it.contains('\n') }
            .toList()
    }

    private fun normalizeLyricsHtmlFragment(fragment: String): String {
        return fragment
            .replace(Regex("""\\n"""), "\n")
            .replace(Regex("""\\u003c"""), "<")
            .replace(Regex("""\\u003e"""), ">")
            .replace(Regex("""\\u0026"""), "&")
            .replace(Regex("""(?i)<\s*br\s*/?\s*>"""), "\n")
            .replace(Regex("""(?i)</\s*(div|p|span)\s*>"""), "\n")
            .replace(Regex("""<[^>]+>"""), " ")
    }

    private fun decodeEscapedJsonString(value: String): String? {
        return runCatching {
            JSONObject("""{"value":"$value"}""").getString("value")
        }.getOrNull()
    }

    private fun decodeDoublyEscapedJsonString(value: String): String? {
        val normalized = value
            .replace("""\\u0022""", "\"")
            .replace("""\\u003c""", "<")
            .replace("""\\u003e""", ">")
            .replace("""\\u0026""", "&")
            .replace("""\\n""", "\n")
            .replace("""\""", "\"")
        return decodeEscapedJsonString(normalized)
    }

    private fun buildSearchUrl(query: String): String {
        return "$BASE_URL?q=${URLEncoder.encode(query, StandardCharsets.UTF_8.name())}"
    }

    private fun getJsonObject(url: String): JSONObject? = getText(url)?.let(::JSONObject)

    private fun getText(
        url: String,
        accept: String = "application/json",
    ): String? {
        val connection = (URL(url).openConnection() as? HttpURLConnection) ?: return null
        return runCatching {
            connection.requestMethod = "GET"
            connection.connectTimeout = CONNECT_TIMEOUT_MS
            connection.readTimeout = READ_TIMEOUT_MS
            connection.setRequestProperty(
                "User-Agent",
                "Elovaire/${BuildConfig.VERSION_NAME} (Android; Offline Music Player)",
            )
            connection.setRequestProperty("Accept", accept)
            if (accessToken.isNotBlank()) {
                connection.setRequestProperty("Authorization", "Bearer $accessToken")
            }
            val code = connection.responseCode
            if (code !in 200..299) return@runCatching null
            connection.inputStream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
        }.getOrElse { throwable ->
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "genius lyrics request failed for $url", throwable)
            }
            null
        }.also {
            connection.disconnect()
        }
    }

    private fun JSONObject.toLyricsCandidate(): LyricsCandidate? {
        val id = optFlexibleLong("id")?.takeIf { it > 0L } ?: return null
        val primaryArtist = optJSONObject("primary_artist")
        val albumObject = optJSONObject("album")
        return LyricsCandidate(
            providerId = "genius::$id",
            title = optNullableString("title"),
            artist = primaryArtist?.optNullableString("name").orEmpty(),
            album = albumObject?.optNullableString("name").orEmpty(),
            durationMs = null,
            instrumental = false,
            plainLyrics = "",
            syncedLyrics = "",
            sourceUrl = optNullableString("url").takeIf { it.isNotBlank() },
        )
    }

    private fun JSONObject.optNullableString(name: String): String {
        return if (isNull(name)) "" else optString(name).trim()
    }

    private fun JSONObject.optFlexibleLong(name: String): Long? {
        if (isNull(name)) return null
        val value = opt(name) ?: return null
        return when (value) {
            is Number -> value.toLong()
            is String -> value.trim().toLongOrNull()
            else -> null
        }
    }

    private companion object {
        const val TAG = "GeniusLyrics"
        const val BASE_URL = "https://api.genius.com/search"
        const val CONNECT_TIMEOUT_MS = 500
        const val READ_TIMEOUT_MS = 900
        const val MAX_QUERY_VARIANTS = 4
        val HTML_VALUE_REGEX = Regex(
            """"html"\s*:\s*"((?:\\.|[^"])*)"""",
            setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL),
        )
        val ESCAPED_HTML_VALUE_REGEX = Regex(
            """\\"html\\"\s*:\s*\\"((?:\\\\.|[^"])*)\\"""",
            setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL),
        )
        val LYRICS_CONTAINER_REGEX = Regex(
            """<div[^>]*data-lyrics-container="true"[^>]*>(.*?)</div>""",
            setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL),
        )
    }
}
