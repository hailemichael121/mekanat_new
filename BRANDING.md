# Mekanät — Brand & Theme System

## 1. Identity & Philosophy
- **Map-first identity**: Monochrome ground, purposeful functional color.
- **Restraint**: Black-and-paper neutrals carry the interface. Color is never decorative — each hue is assigned one job: orientation, action, or alert.
- **Amharic / Ethiopic**: መካናት — የኢትዮጵያ ኦርቶዶክስ አብያተ ክርስቲያናትን ያግኙ።

## 2. Six Functional Color Tokens
1. **Ink (`#121214`)**: Text, dark surfaces, default neutral pins.
2. **Paper (`#F6F4F0`)**: Light canvas background.
3. **Ember (`#FF5A1F`)**: Primary action, brand mark `M`, selected states, active tab accent.
   - `Ember Ink` (`#7A2A0C` light / `#FFC7A8` dark): Nigs tag text.
4. **Wayfinding Teal (`#0FB2A0`)**: GPS pilgrim dot, active route lines, "Start route" button.
   - `Teal Ink` (`#083C36`)
5. **Crimson Pulse (`#E1344F`)**: Live Gubae broadcast, urgent/soon Nigs, live banner, alert dots.
6. **Gold Flame (`#F2B705`)**: Saved / bookmark state only (active favorite filled icon, gold bookmark tags).

## 3. Light & Dark Theme Surface Tokens
### Light (`[data-theme="light"]`)
- `bg`: `#F6F4F0`
- `bg-elev`: `#FFFFFF`
- `bg-sunk`: `#EDEAE4`
- `text`: `#15140F`
- `text-dim`: `#6B675E`
- `border`: `#E2DED4`
- `pin`: `#15140F`

### Dark (`[data-theme="dark"]`)
- `bg`: `#0E0D0C`
- `bg-elev`: `#181613`
- `bg-sunk`: `#221F1B`
- `text`: `#F3F1EA`
- `text-dim`: `#9A9488`
- `border`: `#2D2A24`
- `pin`: `#F3F1EA`

## 4. Typography Hierarchy
- **Display / Headers**: Geometric Display (Space Grotesk style, letter-spacing: -0.02em, bold).
- **UI / Body Copy**: Clean UI Sans (Inter style).
- **Data / Instrumentation**: Clean Monospace (IBM Plex Mono / Monospace) — distances, times, dates, coordinates (`2.7 km · 9 min`).
- **Ethiopic**: Noto Sans Ethiopic quiet, modern glyphs.

## 5. Buttons & Pills
- **Primary**: Background `Ember` (`#FF5A1F`), text `#FFFFFF`, radius 12dp.
- **Secondary**: Transparent, border 1.5dp `border` (`#E2DED4` / `#2D2A24`), text `text`, radius 12dp.
- **Ghost**: Background `bg-sunk` (`#EDEAE4` / `#221F1B`), text `text`, radius 12dp.
- **Route**: Background `Wayfinding Teal` (`#0FB2A0`), text `#FFFFFF`, label "▲ Start route", radius 12dp.
- **Danger Outline**: Transparent, border 1.5dp `Crimson` (`#E1344F`), text `Crimson`, radius 12dp.
- **Disabled**: Background `bg-sunk`, text `text-dim`.
- **Icon Button**: 44dp circular, border 1.5dp `border`, background `bg-elev`. When saved/favorited: filled `Gold Flame` (`#F2B705`) with `#3A2A00` icon.

## 6. Components & Live State
- **Church Card**: `bg-elev`, border 1dp `border`, radius 14dp.
- **Distance Pill**: Monospace font, `bg-sunk`, pill shape (999dp).
- **Nigs Tag**: Border `Ember`, background `Ember` 12% alpha, text `Ember Ink` (`#7A2A0C` light / `#FFC7A8` dark).
- **Live Banner**: Background `Crimson` 14% alpha, border 1dp `Crimson` (`#E1344F`), text `Crimson`, animated pulse dot.

## 7. Map & Wayfinding Language
- **Pilgrim User Dot**: `Wayfinding Teal` (`#0FB2A0`) with pulsing animated ripple.
- **Default Church Pin**: Ink neutral (`#121214` in light, `#F3F1EA` in dark).
- **Live Church Pin**: `Crimson Pulse` (`#E1344F`) with pulse ring.
- **Selected Church Pin**: `Ember` (`#FF5A1F`) (larger, distinct).
- **Route Line**: `Wayfinding Teal` (`#0FB2A0`).
- **Recenter GPS / Target**: Crosshair reticle in `Wayfinding Teal` / `Ember`.
- **Unified Zoom**: Rounded vertical pill with `+` and `-` and subtle divider.

## 8. Custom 24px Hand-Crafted Icons (1.75px stroke, rounded joins)
- **Map**: Compass in circle (`circle cx=12 cy=12 r=9`, compass diamond).
- **Bookmarks**: Silk ribbon swallowtail bookmark.
- **Calendar**: Liturgical calendar codex with binder rings and cross matrix.
- **Profile**: Circle head and shoulder arc.
- **Search**: Circle glass with angled handle.
- **Filter**: Funnel filter polygon.
- **Recenter GPS**: Crosshairs with center point.
- **Route**: Dashed curve with origin/destination points.
- **Favorite / Flame**: Sacred drop flame with inner core.
- **Live Broadcast**: Concentric radiating arcs.
- **Share**: Graph nodes with connection branches.
