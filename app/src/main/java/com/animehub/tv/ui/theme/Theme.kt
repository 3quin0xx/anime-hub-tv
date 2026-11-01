package com.animehub.tv.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import androidx.compose.runtime.Composable

/** Anime Hub TV palette — from the approved mockups. */
val Charcoal = Color(0xFF1A1A1A)
val CharcoalCard = Color(0xFF242424)
val Amber = Color(0xFFFFB042)
val AmberDark = Color(0xFFE89A2E)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFB8B8B8)

/**
 * Translucent slider-dots bar: about 43% black, so hero artwork shows through.
 * In Compose this is Color.Black.copy(alpha = 0.43f) — see HeroSlider.
 */
val DotsBarAlpha = 0.43f

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AnimeHubTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Charcoal,
            surface = CharcoalCard,
            primary = Amber,
            onBackground = TextPrimary,
            onSurface = TextPrimary,
        ),
        content = content,
    )
}
