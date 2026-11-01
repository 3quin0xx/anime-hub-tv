package com.animehub.tv.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

/**
 * Minimal public AniList GraphQL client. No key is needed for public reads.
 * Queries are deliberately small: trending, search, and one media by id.
 * If the network fails, callers fall back to [Repository.sampleData].
 */
object AniListApi {
    private const val URL = "https://graphql.anilist.co"
    private val client = OkHttpClient()
    private val jsonType = "application/json".toMediaType()

    private suspend fun query(query: String, variables: JSONObject): JSONObject? =
        withContext(Dispatchers.IO) {
            val body = JSONObject().apply {
                put("query", query)
                put("variables", variables)
            }.toString().toRequestBody(jsonType)
            val request = Request.Builder().url(URL).post(body).build()
            runCatching {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) return@runCatching null
                    JSONObject(response.body?.string().orEmpty())
                }
            }.getOrNull()
        }

    private fun parseMedia(obj: JSONObject): Media {
        val title = obj.optJSONObject("title")
        val cover = obj.optJSONObject("coverImage")
        val banner = obj.optString("bannerImage").takeIf { it.isNotBlank() }
        return Media(
            id = obj.optInt("id"),
            title = title?.optString("userPreferred").orEmpty()
                .ifBlank { title?.optString("romaji").orEmpty() },
            description = obj.optString("description").replace(Regex("<[^>]+>"), ""),
            coverUrl = cover?.optString("large"),
            bannerUrl = banner,
            ratingOutOf5 = score100To5(obj.optInt("averageScore").takeIf { it > 0 }),
            genres = obj.optJSONArray("genres")?.let { arr ->
                List(arr.length()) { arr.optString(it) }
            } ?: emptyList(),
            episodeCount = obj.optInt("episodes").takeIf { it > 0 },
            year = obj.optJSONObject("startDate")?.optInt("year")?.takeIf { it > 0 },
            isAnime = obj.optString("type") != "MANGA",
        )
    }

    suspend fun trending(perPage: Int = 12): List<Media> {
        val q = """
            query(${'$'}page: Int, ${'$'}perPage: Int) {
              Page(page: ${'$'}page, perPage: ${'$'}perPage) {
                media(sort: TRENDING_DESC, type: ANIME) {
                  id title { userPreferred romaji }
                  description averageScore genres episodes
                  startDate { year }
                  coverImage { large } bannerImage
                }
              }
            }
        """.trimIndent()
        val vars = JSONObject().apply { put("page", 1); put("perPage", perPage) }
        val data = query(q, vars)?.optJSONObject("data")
            ?.optJSONObject("Page")?.optJSONArray("media") ?: return emptyList()
        return List(data.length()) { parseMedia(data.getJSONObject(it)) }
    }

    suspend fun mostLiked(perPage: Int = 5): List<Media> {
        // Highest-rated first, capped at 5 slides — matches the Home hero slider.
        val q = """
            query(${'$'}page: Int, ${'$'}perPage: Int) {
              Page(page: ${'$'}page, perPage: ${'$'}perPage) {
                media(sort: SCORE_DESC, type: ANIME) {
                  id title { userPreferred romaji }
                  description averageScore genres episodes
                  startDate { year }
                  coverImage { large } bannerImage
                }
              }
            }
        """.trimIndent()
        val vars = JSONObject().apply { put("page", 1); put("perPage", perPage) }
        val data = query(q, vars)?.optJSONObject("data")
            ?.optJSONObject("Page")?.optJSONArray("media") ?: return emptyList()
        return List(data.length()) { parseMedia(data.getJSONObject(it)) }
    }

    suspend fun search(text: String, perPage: Int = 20): List<Media> {
        val q = """
            query(${'$'}search: String, ${'$'}perPage: Int) {
              Page(page: 1, perPage: ${'$'}perPage) {
                media(search: ${'$'}search, type: ANIME) {
                  id title { userPreferred romaji }
                  description averageScore genres episodes
                  startDate { year }
                  coverImage { large } bannerImage
                }
              }
            }
        """.trimIndent()
        val vars = JSONObject().apply { put("search", text); put("perPage", perPage) }
        val data = query(q, vars)?.optJSONObject("data")
            ?.optJSONObject("Page")?.optJSONArray("media") ?: return emptyList()
        return List(data.length()) { parseMedia(data.getJSONObject(it)) }
    }
}
