package com.animehub.tv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.animehub.tv.ui.theme.TextPrimary
import com.animehub.tv.ui.theme.TextSecondary

/**
 * Player UI only. This skeleton deliberately ships NO stream URL and
 * NO unofficial source. To play real video, pass a licensed URL you
 * have rights to (for example via Media3 ExoPlayer) and replace the
 * placeholder box below with the player view. Do not wire this screen
 * to unofficial anime/manga streaming APIs.
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun PlayerScreen(
    media: Media,
    episode: Int,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Episode $episode — ${media.title}", color = TextPrimary)
        Text(
            "Player placeholder. Connect a licensed video source here.",
            color = TextSecondary,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {}) { Text("▶ Play") }
            OutlinedButton(onClick = onBack) { Text("Back") }
        }
        Text("Episodes", color = TextPrimary)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Repository.episodesFor(media)) { ep ->
                OutlinedButton(onClick = {}) {
                    Text("Episode ${ep.number} · ${ep.durationMinutes}m")
                }
            }
        }
    }
}
