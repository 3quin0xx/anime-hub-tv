package com.animehub.tv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.animehub.tv.data.Media
import com.animehub.tv.ui.components.MediaCard
import com.animehub.tv.ui.theme.TextPrimary
import com.animehub.tv.ui.theme.TextSecondary

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MyListScreen(
    saved: List<Media>,
    onOpenDetails: (Media) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("My List · ${saved.size} items", color = TextPrimary)
        if (saved.isEmpty()) {
            Text("Nothing saved yet. Add a title from Home or Details.", color = TextSecondary)
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(saved) { media ->
                MediaCard(media = media, onClick = { onOpenDetails(media) })
            }
        }
    }
}
