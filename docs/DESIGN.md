# Design notes — Anime Hub TV

These are the decisions locked in from the mockups, so future changes
don't drift back to a web-style layout.

## Home

- Top tabs, left to right: Home, Browse, Search, My List, Settings.
- Hero slider: "Most Liked · Highest Rated", max 5 slides.
- Slider dots: 5 dots, inside the hero preview, bottom centre, on a
  translucent bar (black at 43% opacity). No dots row below the hero.
- No slider arrows, no preview thumbnails, no auto-play label or timer
  in the UI. Auto-advance is off by default in `HeroSlider.kt`; if it is
  enabled later, pause it while Play / Add to My List is focused.
- Below the hero: Trending Now — poster + name under each card, first
  card focused with an amber border. Then Continue Watching.

## Colours and focus

- Background: #1A1A1A charcoal. Cards: #242424.
- Accent / focus: #FFB042 amber. Focused cards get a 3dp amber border.
- Text: white primary, #B8B8B8 secondary. All text is TV-sized, not
  phone-sized.

## What is deliberately NOT in this app

- No copied layout, code, screenshots or artwork from other anime sites.
- No unofficial episode/manga streaming, scraping, or download sources.
- No ads, no revenue code, no account system in v0.1. My List is local
  DataStore only.
