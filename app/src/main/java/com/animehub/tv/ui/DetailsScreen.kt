package com.animehub.tv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import com.animehub.tv.data.Media
import com.animehub.tv.data.Repository
import com.animehub.tv.ui.components.MediaCard
import com.animehub.tv.ui.theme.TextPrimary
import com.animehub.tv.ui.theme.TextSecondary

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun DetailsScreen(
    media: Media,
    related: List<Media>,
    onPlay: (Media) -> Unit,
    onToggleMyList: (Media) -> Unit,
    onOpenEpisode: (Media, Int) -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(media.title, color = TextPrimary)
        media.ratingOutOf5?.let { Text("★ %.1f / 5".format(it), color = TextSecondary) }
        if (media.description.isNotBlank()) Text(media.description, color = TextSecondary)
        Row {
            Button(onClick = { onPlay(media) }) { Text("▶ Watch Now") }
            Spacer(Modifier.width(12.dp))
            OutlinedButton(onClick = { onToggleMyList(media) }) { Text("+ Add to My List") }
            Spacer(Modifier.width(12.dp))
            OutlinedButton(onClick = onBack) { Text("Back") }
        }
        Spacer(Modifier.height(8.dp))
        Text("Episodes", color = TextPrimary)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(Repository.episodesFor(media)) { episode ->
                OutlinedButton(onClick = { onOpenEpisode(media, episode.number) }) {
                    Text("Ep ${episode.number} · ${episode.durationMinutes}m")
                }
            }
        }
        Text("More Like This", color = TextPrimary)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(related.filter { it.id != media.id }.take(6)) { item ->
                MediaCard(media = item, onClick = { /* handled by parent in full build */ })
            }
        }
    }
}
