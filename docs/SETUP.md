# Setup

1. Open `anime-hub-tv` in Android Studio.
2. Sync Gradle (first sync downloads Compose for TV, Coil, OkHttp, etc.).
3. Create an Android TV emulator: Pixel, 1080p, API 34, TV profile.
   Or connect a real Android TV / Google TV device with USB debugging.
4. Run `app`. The Home screen will load AniList trending/highest-rated
   titles if online, otherwise the sample placeholders in
   `Repository.kt` so the layout can still be checked offline.
5. To add real playback: in `PlayerScreen.kt`, replace the placeholder
   with a Media3 ExoPlayer view fed by a licensed URL you control.
   Do not add unofficial streaming APIs.

## Troubleshooting

- Gradle sync fails on Compose for TV versions: check that the Google
  repository is enabled and that you are on Android Studio Hedgehog+.
- "Leanback launcher" missing on a phone emulator: use a TV emulator —
  the manifest requires `android.software.leanback`.
- Blank posters: AniList cover URLs need INTERNET permission (already in
  the manifest) and Coil; check the emulator has network access.
