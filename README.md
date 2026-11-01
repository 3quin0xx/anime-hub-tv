# Anime Hub TV

An original Android TV anime discovery app, built from scratch in Kotlin with
Jetpack Compose for TV.

This is **not** a copy of any existing anime website. It contains no copied
code, artwork, or branding from other projects, and it does not stream from
unofficial episode or manga sources. It discovers titles through the public
AniList GraphQL API, keeps a local My List, and links to official trailers
and licensed sources. Playback in this skeleton is a UI placeholder: wire it
to a source you have the rights to use before shipping any video.

## Screens

- **Home** — Top tabs (Home, Browse, Search, My List, Settings), a
  "Most Liked · Highest Rated" hero slider (max 5 slides), with the 5 slider
  dots *inside* the hero preview at the bottom centre, on a translucent bar.
  Below it: Trending Now (poster + name) and Continue Watching.
- **Details** — Poster, rating, description, Watch Now / Add to My List,
  episode list, More Like This.
- **Search** — On-screen keyboard + results grid.
- **My List** — Saved titles with continue-watching progress.
- **Player** — Video controls + episode list. No stream URL is bundled.

## Design

- Dark charcoal background (`#1A1A1A`), warm amber accent (`#FFB042`),
  large TV-readable text, D-pad focus with an amber focus border.
- The slider dots bar is translucent (about 43% black) so the hero artwork
  shows through. There are no slider arrows and no preview thumbnails.
- Sample titles in the fallback data are original placeholders
  (Starlight Journey, Blade of Dawn, Neon Sakura, …). Real titles come from
  AniList when the API is reachable.

## Build

1. Open this folder in Android Studio (Hedgehog or newer).
2. Let Gradle sync. You need the Android SDK with API 34 and an Android TV
   emulator or device (1080p, Leanback / TV profile).
3. Run the `app` configuration on the TV emulator.
4. No API key is needed for public AniList GraphQL reads. If you add a
   login later, keep secrets out of the repo — use `local.properties`.

## Project layout

```
app/src/main/java/com/animehub/tv/
  MainActivity.kt          TV entry point, tab navigation
  data/Models.kt           Media, Episode, rating helpers
  data/AniListApi.kt       GraphQL client (trending, search, media by id)
  data/Repository.kt       Repository + sample fallback data + My List store
  ui/theme/Theme.kt        Colours, typography, focus style
  ui/components/MediaCard.kt
  ui/components/HeroSlider.kt   Dots-inside-hero, translucent bar
  ui/HomeScreen.kt
  ui/DetailsScreen.kt
  ui/SearchScreen.kt
  ui/MyListScreen.kt
  ui/PlayerScreen.kt
```

## License / note

Original code in this repository is provided as a learning skeleton.
Anime titles, artwork and trailers belong to their respective owners.
Do not add unofficial streaming sources, and do not use this app to
distribute copyrighted video or manga.
