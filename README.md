# Mekanat (መካናት) — Ethiopian Orthodox Sanctuary & Pilgrimage Guide

<p align="center">
  <strong>An intuitive, offline-first mobile companion for discovering Ethiopian Orthodox Tewahedo churches, holy Tabots, pilgrimage paths, and liturgical feasts.</strong>
</p>

---

## 📖 Overview

**Mekanat (መካናት)** is a native Android application built with **Kotlin** and **Jetpack Compose** designed for pilgrims, church communities, researchers, and visitors. It bridges traditional Ethiopian Orthodox Tewahedo Church (EOTC) heritage with modern mobile navigation technology:

- 🗺️ **High-Definition Mapping**: Interactive street-level mapping powered by Gebeta Maps with real-time vector corridors, Ethiopian landmark labels (Piazza, Kazanchis, Bole, Arat Kilo, Mercato, Meskel Square, etc.), and building footprints.
- ⛪ **Sanctuary Registry**: Detailed profiles of historical monasteries, cathedrals, rock-hewn churches, and local parishes across Ethiopia (Addis Ababa, Lalibela, Gondar, Axum, Debre Damo, Lake Tana, and beyond).
- 📜 **Sacred Tabot & Feasts**: Comprehensive catalog of Ark Tabot dedications (St. Mary, St. Michael, St. Gabriel, St. George, Medhane Alem, Abuna Gebre Menfes Kidus, Tekle Haymanot) with monthly & annual commemorative dates.
- 📅 **Liturgical Calendar**: Integrated Ge'ez/Ethiopian calendar system tracking daily feasts, fasting seasons (Tsome Nebiyat, Abiy Tsom, Filseta, etc.), and upcoming parish celebrations.
- 🧭 **Turn-by-Turn Pilgrimage Routing**: Walking and driving directions with real-time distance and estimated travel time calculations.
- 📍 **Community Submissions**: Submit new churches, verify historical details, and track approval status with local Room database persistence.

---

## ✨ Key Features

### 1. Interactive Map & Pilgrimage Navigation
- **Custom Tile Engine & Gebeta Maps Integration**: Multi-style map rendering (Standard, Dark, Satellite, and Carto Light) with automatic in-memory tile caching and offline road corridors.
- **Street-Level Initial Focus**: Opens directly at street/neighborhood zoom level (Zoom 15.2) centered on the pilgrim's GPS position.
- **Dynamic North Compass & 360° Rotation**:
  - Rotate map with two-finger gestures or by dragging around the floating North Compass.
  - Quick-tap compass to instantly reset map orientation to True North.
  - Slide thumb vertically on the compass button to smoothly zoom in or out.
- **Surrounding Urban Context**: Dynamic rendering of landmarks, avenues, and building blocks that scale with zoom levels.

### 2. Search & Discovery Experience
- **Fluid Search Bar Expansion**: Focus-aware top bar that smoothly minimizes accessory buttons while typing to maximize screen real estate.
- **Themed Search Lens Overlay**: Frosted discovery backdrop with animated searching states, Ethiopian parchment motifs, and live query feedback.
- **Filter by Region & Tabot**: Instant filtering across dioseces (Addis Ababa, Amhara, Tigray, Oromia, Southern Nations) and dedicated saints.

### 3. Sanctuary Profiles & Holy Tabot Index
- **Deep Historical & Archival Context**: History, architectural classification (Round, Cave, Monolithic, Modern Basilica), founding era, and clergy contacts.
- **Pilgrimage Planning**: Direct routing button, GPS coordinate copy, dial parish office, and one-tap bookmarking for offline visits.
- **Celebration Schedule**: Annual and monthly feast day countdowns with Ge'ez day mapping.

### 4. Liturgical & Ethiopian Calendar
- **Ethiopian Date Conversion**: Accurate synchronization between Gregorian and Ethiopian/Ge'ez calendars (Pagume leap year aware).
- **Daily Saint Celebrations**: Lists sacred commemorations for each day of the Ethiopian month (e.g., 21st Mariam, 12th Michael, 19th Gabriel, 23rd George, 27th Medhane Alem).
- **Fasting Periods**: Highlights major EOTC fasting seasons with liturgical guidelines and scripture readings.

### 5. Personal Bookmarks & Offline Mode
- **Offline Access**: All indexed church data and saved favorites are stored locally on the device using Room SQLite.
- **Pilgrim Collections**: Organize bookmarked sanctuaries with custom tags, personal prayer notes, and visit history.

### 6. Profile, Contributions & Customization
- **Theme Engine**: Seamless toggle between Ethiopian Sunrise Gold, Midnight Obsidian Dark, and Clean Light themes.
- **Community Submissions**: Add unlisted rural sanctuaries or neighborhood chapels with diocesan verification workflows.
- **System Health Diagnostics**: Real-time status monitor for GPS precision, database index count, tile cache state, and routing engine.

---

## 🛠️ Architecture & Tech Stack

The application follows modern Android best practices and MVVM (Model-View-ViewModel) architecture:

| Layer | Technologies & Libraries |
| :--- | :--- |
| **Language** | Kotlin 2.0+ with Coroutines & Flow |
| **UI Framework** | Jetpack Compose (Material Design 3) |
| **Local Persistence** | Room Database (SQLite) with KSP compiler |
| **State Management** | Android ViewModel, `StateFlow`, `MutableStateFlow` |
| **Image Loading** | Coil Compose |
| **Mapping Engine** | Custom Web Mercator Canvas Renderer + Gebeta Maps API |
| **Icons & Symbols** | Material 3 Vector Icons & Custom Ethiopian Tewahedo Vectors |
| **Build Tooling** | Gradle Kotlin DSL (`build.gradle.kts`), Java 17 |

### Directory Structure

```
mekanat_new/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/mekanat_new/
│   │   │   ├── data/
│   │   │   │   ├── local/          # Room entities, DAOs, and database definition
│   │   │   │   ├── maps/           # Gebeta Map tile services, caching, & routing
│   │   │   │   ├── model/          # Church, Tabot, Feast, and Event data models
│   │   │   │   ├── repository/     # ChurchRepository and data orchestration
│   │   │   │   └── util/           # Ethiopian calendar and Ge'ez date converters
│   │   │   ├── ui/
│   │   │   │   ├── components/     # GebetaMapView, SearchLens, NavBars, Logos
│   │   │   │   ├── screens/        # Home, Detail, Calendar, Bookmarks, Profile, AddChurch
│   │   │   │   ├── theme/          # ColorScheme, Typography, Shapes, Themes
│   │   │   │   └── viewmodel/      # MekanatViewModel & UI State flows
│   │   │   └── MainActivity.kt     # App entry point & Edge-to-Edge initialization
│   │   └── res/                    # Vector drawables, strings, launcher icons
│   └── build.gradle.kts            # App module configuration & dependencies
├── build.gradle.kts                # Root project configuration
├── settings.gradle.kts             # Project settings & repositories
└── README.md                       # Project documentation
```

---

## 🚀 Getting Started & Build Instructions

### Prerequisites
- **Android Studio** Ladybug (2024.2.1+) or newer
- **JDK**: Java Development Kit 17
- **Android SDK**: API Level 35 (Compile/Target), Minimum API Level 24 (Android 7.0+)

### Building the Project

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/example/mekanat.git
   cd mekanat
   ```

2. **Open in Android Studio**:
   Open the root directory in Android Studio and let Gradle sync all dependencies.

3. **Build the Debug APK**:
   ```bash
   ./gradlew assembleDebug
   ```

4. **Run on Device / Emulator**:
   Select your target Android device or emulator and click **Run** (`Shift + F10`).

---

## 🗺️ Map Controls & Gesture Guide

| Action | Gesture |
| :--- | :--- |
| **Pan / Move** | Single-finger drag on map canvas |
| **Zoom In / Out** | Pinch-to-zoom or slide vertically on the Compass |
| **Rotate Map** | Two-finger twist gesture or drag around the Compass wheel |
| **Reset to North** | Single tap on the floating North Compass button |
| **Switch Map Style** | Tap the Layers icon to switch between Standard, Dark, and Satellite |
| **Recenter GPS** | Tap the My Location target button |
| **Select Sanctuary** | Tap any church pin on the map to open the preview bottom card |

---

## 🤝 Community & Contributions

Contributions to expand the sanctuary database, add Ge'ez hymnology references, or improve offline mapping are welcome!

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/NewSanctuaryFeature`)
3. Commit your changes (`git commit -m 'Add new sanctuary data'`)
4. Push to the branch (`git push origin feature/NewSanctuaryFeature`)
5. Open a Pull Request

---

## 📄 License & Attribution

- **License**: MIT License. See `LICENSE` for details.
- **Cartography**: Powered by OpenStreetMap contributors & Gebeta Maps.
- **Cultural Data**: Curated from Ethiopian Orthodox Tewahedo Church liturgical traditions and historical records.
