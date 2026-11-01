package com.animehub.tv.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("anime_hub_prefs")
private val MY_LIST_KEY = stringPreferencesKey("my_list_ids")

/**
 * Single place the UI gets data from. AniList first, sample data if offline.
 * My List is a comma-separated list of media ids in DataStore — no account,
 * no server, no copied user database.
 */
object Repository {

    // Original placeholder titles, used only when AniList is unreachable.
    // They are NOT real AniList entries; replace them by going online.
    val sampleHero = listOf(
        Media(9001, "Starlight Journey", "A young explorer ventures through the glowing wilds, seeking lost starlight.", ratingOutOf5 = 4.9f, likedPercent = 98, genres = listOf("Adventure", "Fantasy"), episodeCount = 12, year = 2024),
        Media(9002, "Blade of Dawn", "A lone sword-bearer guards the last blossoming village.", ratingOutOf5 = 4.8f, likedPercent = 96, genres = listOf("Action", "Fantasy"), episodeCount = 13, year = 2024),
        Media(9003, "Neon Sakura", "A city courier finds a message hidden in the spring lights.", ratingOutOf5 = 4.7f, likedPercent = 94, genres = listOf("Sci-Fi", "Drama"), episodeCount = 12, year = 2024),
        Media(9004, "Forest Wanderer", "A traveller maps a forest that rearranges itself each night.", ratingOutOf5 = 4.6f, likedPercent = 93, genres = listOf("Adventure"), episodeCount = 10, year = 2023),
        Media(9005, "Cloud Summit", "Climbers chase a peak that only appears above the clouds.", ratingOutOf5 = 4.5f, likedPercent = 91, genres = listOf("Adventure", "Drama"), episodeCount = 12, year = 2023),
    )

    val sampleTrending = listOf(
        Media(9011, "Blade of Dawn"), Media(9012, "Neon Sakura"),
        Media(9013, "Forest Wanderer"), Media(9014, "Cloud Summit"),
        Media(9015, "Harbor Dusk"), Media(9016, "Winter Oath"),
    )

    suspend fun heroSlides(): List<Media> {
        val fromApi = AniListApi.mostLiked(5)
        return fromApi.ifEmpty { sampleHero }
    }

    suspend fun trending(): List<Media> {
        val fromApi = AniListApi.trending(12)
        return fromApi.ifEmpty { sampleTrending }
    }

    suspend fun search(text: String): List<Media> {
        if (text.isBlank()) return emptyList()
        val fromApi = AniListApi.search(text)
        return fromApi.ifEmpty {
            (sampleHero + sampleTrending).filter {
                it.title.contains(text, ignoreCase = true)
            }
        }
    }

    fun episodesFor(media: Media): List<Episode> {
        val count = media.episodeCount ?: 12
        return (1..count).map { Episode(it, "Episode $it") }
    }

    fun myListIds(context: Context): Flow<Set<Int>> =
        context.dataStore.data.map { prefs ->
            prefs[MY_LIST_KEY]?.split(",")?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        }

    suspend fun toggleMyList(context: Context, mediaId: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[MY_LIST_KEY]?.split(",")?.mapNotNull { it.toIntOrNull() }?.toMutableSet()
                ?: mutableSetOf()
            if (mediaId in current) current.remove(mediaId) else current.add(mediaId)
            prefs[MY_LIST_KEY] = current.joinToString(",")
        }
    }
}
