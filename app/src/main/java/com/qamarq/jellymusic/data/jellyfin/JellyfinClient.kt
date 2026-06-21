package com.qamarq.jellymusic.data.jellyfin

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class AuthenticationResponse(
    val AccessToken: String,
    val User: UserResponse,
    val ServerId: String
)

@Serializable
data class UserResponse(
    val Name: String,
    val Id: String
)

@Serializable
data class BaseItemDto(
    val Name: String,
    val Id: String,
    val Type: String,
    val ArtistItems: List<ArtistDto>? = null,
    val AlbumArtists: List<ArtistDto>? = null,
    val Album: String? = null,
    val AlbumId: String? = null,
    val ProductionYear: Int? = null,
    val Genres: List<String>? = null,
    val RunTimeTicks: Long? = null,
    val IndexNumber: Int? = null,
    val ParentIndexNumber: Int? = null,
    val ImageTags: Map<String, String>? = null,
    val Container: String? = null,
    val Bitrate: Int? = null,
    val MediaSources: List<MediaSourceDto>? = null,
)

@Serializable
data class MediaSourceDto(
    val Container: String? = null,
    val Bitrate: Int? = null,
    val MediaStreams: List<MediaStreamDto>? = null,
)

@Serializable
data class MediaStreamDto(
    val Type: String? = null,
    val Codec: String? = null,
    val SampleRate: Int? = null,
    val BitDepth: Int? = null,
    val BitRate: Int? = null,
)

@Serializable
data class ArtistDto(
    val Name: String,
    val Id: String
)

@Serializable
data class ItemsResponse(
    val Items: List<BaseItemDto>,
    val TotalRecordCount: Int
)

class JellyfinClient(
    private val host: String,
    private val accessToken: String = "",
) {
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    private fun buildUrl(path: String) = "${host.trimEnd('/')}/$path"

    private fun Request.Builder.addAuth(): Request.Builder {
        if (accessToken.isNotEmpty()) {
            header("X-Emby-Token", accessToken)
        }
        header("X-Emby-Authorization", "MediaBrowser Client=\"Elovaire\", Device=\"Android\", DeviceId=\"ElovaireDevice\", Version=\"1.0.0\"")
        return this
    }

    suspend fun authenticate(username: String, password: String): AuthenticationResponse? = withContext(Dispatchers.IO) {
        val authRequest = """
            {
                "Username": "$username",
                "Pw": "$password"
            }
        """.trimIndent()

        val request = Request.Builder()
            .url(buildUrl("Users/AuthenticateByName"))
            .post(authRequest.toRequestBody("application/json".toMediaType()))
            .header("X-Emby-Authorization", "MediaBrowser Client=\"Elovaire\", Device=\"Android\", DeviceId=\"ElovaireDevice\", Version=\"1.0.0\"")
            .build()

        return@withContext try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                val body = response.body?.string() ?: return@withContext null
                json.decodeFromString<AuthenticationResponse>(body)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getMusicItems(userId: String): List<BaseItemDto> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(buildUrl("Users/$userId/Items?IncludeItemTypes=Audio&Recursive=true&Fields=ArtistItems,AlbumArtists,Album,ProductionYear,Genres,RunTimeTicks,IndexNumber,ParentIndexNumber,ImageTags,MediaSources,Bitrate"))
            .addAuth()
            .build()

        return@withContext try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val body = response.body?.string() ?: return@withContext emptyList()
                json.decodeFromString<ItemsResponse>(body).Items
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getStreamUrl(itemId: String): String {
        return buildUrl("Audio/$itemId/stream?static=true&api_key=$accessToken")
    }

    fun getImageUrl(itemId: String, tag: String): String {
        val tagParam = if (tag.isNotEmpty()) "&tag=$tag" else ""
        return buildUrl("Items/$itemId/Images/Primary?fillHeight=512&fillWidth=512&quality=90&api_key=$accessToken$tagParam")
    }
}
