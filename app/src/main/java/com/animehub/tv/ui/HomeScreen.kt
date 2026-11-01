package com.animehub.tv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.animehub.tv.data.Media
import com.animehub.tv.data.Repository
import com.animehub.tv.ui.components.HeroSlider
import com.animehub.tv.ui.components.MediaCard
import com.animehub.tv.ui.theme.TextPrimary

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenDetails: (Media) -> Unit,
    onPlay: (Media) -> Unit,
    onToggleMyList: (Media) -> Unit,
) {
    var hero by remember { mutableStateOf<List<Media>>(emptyList()) }
    var trending by remember { mutableStateOf<List<Media>>(emptyList()) }
    var heroIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        hero = Repository.heroSlides().take(5) // slider is capped at 5
        trending = Repository.trending()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        if (hero.isNotEmpty()) {
            HeroSlider(
                slides = hero,
                currentIndex = heroIndex,
                onPlay = onPlay,
                onToggleMyList = onToggleMyList,
            )
        }
        Text("Trending Now", color = TextPrimary)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(trending) { media ->
                MediaCard(media = media, onClick = { onOpenDetails(media) })
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("Continue Watching", color = TextPrimary)
        // Continue Watching is driven by My List + local progress in a
        // full build; here it reuses trending as a placeholder row so the
        // layout matches the mockup without inventing watch history.
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(trending.take(5)) { media ->
                MediaCard(media = media, onClick = { onOpenDetails(media) }, showName = false)
            }
        }
    }
}
