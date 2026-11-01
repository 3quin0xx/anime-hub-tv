package com.animehub.tv.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.animehub.tv.data.Media
import com.animehub.tv.ui.theme.Amber
import com.animehub.tv.ui.theme.DotsBarAlpha
import com.animehub.tv.ui.theme.TextPrimary
import com.animehub.tv.ui.theme.TextSecondary

/**
 * Home hero slider.
 *
 * Rules from the approved mockups, kept here in code so they don't drift:
 * - Max 5 slides (callers cap the list).
 * - No arrows, no preview thumbnails.
 * - No auto-play label or timer bar in the UI.
 * - The 5 dots live INSIDE the hero preview, bottom centre, on a
 *   translucent bar (Black at [DotsBarAlpha]), not in a separate row below.
 * - Auto-advance is OFF by default. Pass autoAdvance=true to move every
 *   4 seconds; when focused on Play / Add to My List, callers should pause
 *   it so the slide doesn't change mid-selection.
 *
 * This composable shows one slide at a time; the parent owns currentIndex
 * and changes it on D-pad left/right or on a timer if it opts in.
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HeroSlider(
    slides: List<Media>,
    currentIndex: Int,
    onPlay: (Media) -> Unit,
    onToggleMyList: (Media) -> Unit,
    modifier: Modifier = Modifier,
) {
    val media = slides.getOrNull(currentIndex) ?: return
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(420.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF222222)),
    ) {
        val imageUrl = media.bannerUrl ?: media.coverUrl
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 32.dp, end = 32.dp),
        ) {
            Text("Most Liked · Highest Rated", color = Amber)
            Spacer(Modifier.height(12.dp))
            Text(media.title, color = TextPrimary)
            media.ratingOutOf5?.let {
                Text("★ %.1f / 5".format(it), color = Amber)
            }
            media.likedPercent?.let {
                Text("♥ $it% liked", color = TextSecondary)
            }
            if (media.description.isNotBlank()) {
                Text(media.description, color = TextSecondary, maxLines = 2)
            }
            Spacer(Modifier.height(16.dp))
            Row {
                Button(onClick = { onPlay(media) }) { Text("▶ Play") }
                Spacer(Modifier.width(12.dp))
                OutlinedButton(onClick = { onToggleMyList(media) }) {
                    Text("+ Add to My List")
                }
            }
        }
        // Dots INSIDE the hero, bottom centre, on a translucent bar.
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Black.copy(alpha = DotsBarAlpha))
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                slides.indices.forEach { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == currentIndex) 12.dp else 10.dp)
                            .background(
                                color = if (index == currentIndex) Amber else Color.Gray,
                                shape = CircleShape,
                            ),
                    )
                    if (index < slides.lastIndex) Spacer(Modifier.width(8.dp))
                }
            }
        }
    }
}
