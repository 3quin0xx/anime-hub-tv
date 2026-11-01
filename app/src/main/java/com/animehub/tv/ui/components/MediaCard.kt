package com.animehub.tv.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.animehub.tv.data.Media
import com.animehub.tv.ui.theme.Amber
import com.animehub.tv.ui.theme.CharcoalCard
import com.animehub.tv.ui.theme.TextPrimary
import com.animehub.tv.ui.theme.TextSecondary

/**
 * One poster card for TV rows. Focused card gets an amber border —
 * the same rule used everywhere in this app, so D-pad users always
 * know where they are.
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MediaCard(
    media: Media,
    onClick: () -> Unit,
    showName: Boolean = true,
    progressPercent: Int? = null,
    modifier: Modifier = Modifier,
) {
    var focused by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .width(220.dp)
            .onFocusChanged { focused = it.isFocused }
            .border(
                width = if (focused) 3.dp else 0.dp,
                color = if (focused) Amber else Color.Transparent,
                shape = RoundedCornerShape(12.dp),
            )
            .clip(RoundedCornerShape(12.dp))
            .background(CharcoalCard)
            .clickable { onClick() }
            .padding(bottom = 8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color(0xFF333333)),
        ) {
            if (media.coverUrl != null) {
                AsyncImage(
                    model = media.coverUrl,
                    contentDescription = media.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize(),
                )
            }
        }
        if (showName) {
            Text(
                text = media.title,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp),
            )
        }
        if (progressPercent != null) {
            Box(
                modifier = Modifier
                    .padding(start = 8.dp, top = 6.dp, end = 8.dp)
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color(0xFF444444), RoundedCornerShape(2.dp)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressPercent / 100f)
                        .height(4.dp)
                        .background(Amber, RoundedCornerShape(2.dp)),
                )
            }
            Text(
                text = "$progressPercent% watched",
                color = TextSecondary,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp),
            )
        }
    }
}
