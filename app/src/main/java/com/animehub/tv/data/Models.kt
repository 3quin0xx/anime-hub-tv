package com.animehub.tv.data

/** A title shown in the app. `id` is the AniList media id when it came from AniList. */
data class Media(
    val id: Int,
    val title: String,
    val description: String = "",
    val coverUrl: String? = null,
    val bannerUrl: String? = null,
    val ratingOutOf5: Float? = null, // AniList score (0-100) is converted to 0-5
    val likedPercent: Int? = null,
    val genres: List<String> = emptyList(),
    val episodeCount: Int? = null,
    val year: Int? = null,
    val isAnime: Boolean = true,
)

data class Episode(
    val number: Int,
    val title: String,
    val durationMinutes: Int = 24,
)

fun score100To5(score100: Int?): Float? =
    score100?.let { (it / 20f).coerceIn(0f, 5f) }

/** Local progress for Continue Watching / My List. Stored on device only. */
data class WatchProgress(
    val mediaId: Int,
    val episode: Int,
    val progressPercent: Int,
    val minutesLeft: Int,
)
