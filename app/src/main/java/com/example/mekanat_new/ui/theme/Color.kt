package com.example.mekanat_new.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// Mekanät Brand & Theme System — Core 6 Tokens
// ==========================================
val BrandInk = Color(0xFF121214)         // Ink: Text, dark surfaces, default neutral pins
val BrandPaper = Color(0xFFF6F4F0)       // Paper: Light background
val BrandEmber = Color(0xFFFF5A1F)       // Ember: Primary action, brand mark, selection, active tab
val BrandEmberInk = Color(0xFF7A2A0C)    // Ember Ink: Nigs tag text (Light mode)
val BrandEmberInkDark = Color(0xFFFFC7A8)// Ember Ink: Nigs tag text (Dark mode)

val WayfindingTeal = Color(0xFF0FB2A0)   // Wayfinding Teal: GPS dot, route line, "Start route"
val WayfindingTealInk = Color(0xFF083C36)// Teal Ink: Deep contrast text
val CrimsonPulse = Color(0xFFE1344F)     // Crimson Pulse: Live Gubae, urgent/soon Nigs, live banner
val GoldFlame = Color(0xFFF2B705)        // Gold Flame: Saved / bookmark state only
val GoldFlameInk = Color(0xFF3A2A00)     // Dark ink for filled gold icons

// ==========================================
// Light Theme Tokens (data-theme="light")
// ==========================================
val LightBg = Color(0xFFF6F4F0)          // --bg: Paper
val LightBgElev = Color(0xFFFFFFFF)      // --bg-elev: Pure white elevated cards
val LightBgSunk = Color(0xFFEDEAE4)      // --bg-sunk: Sunken pill/filter background
val LightText = Color(0xFF15140F)        // --text: Deep primary text
val LightTextDim = Color(0xFF6B675E)     // --text-dim: Secondary text / metadata
val LightBorder = Color(0xFFE2DED4)      // --border: Subtle warm border
val LightPin = Color(0xFF15140F)         // --pin: Neutral pin on light map

// ==========================================
// Dark Theme Tokens (data-theme="dark")
// ==========================================
val DarkBg = Color(0xFF0E0D0C)           // --bg: Deep neutral dark canvas
val DarkBgElev = Color(0xFF181613)       // --bg-elev: Elevated dark card surface
val DarkBgSunk = Color(0xFF221F1B)       // --bg-sunk: Sunken dark pill background
val DarkText = Color(0xFFF3F1EA)         // --text: Crisp warm white text
val DarkTextDim = Color(0xFF9A9488)      // --text-dim: Muted warm secondary text
val DarkBorder = Color(0xFF2D2A24)       // --border: Subtle dark border
val DarkPin = Color(0xFFF3F1EA)          // --pin: Neutral pin on dark map

// Aliases for seamless backwards compatibility throughout screens
val CanvasBlack = DarkBg
val DarkCanvas = DarkBg
val DarkSurface = DarkBgElev
val DarkSurfaceVariant = DarkBgSunk
val DarkElevated = DarkBgElev
val DarkDivider = DarkBorder
val TextPrimaryDark = DarkText
val TextSecondaryDark = DarkTextDim
val TextMutedDark = DarkTextDim

val CanvasLight = LightBg
val SurfaceLight = LightBgElev
val SurfaceVariantLight = LightBgSunk
val BorderLight = LightBorder
val TextPrimaryLight = LightText
val TextSecondaryLight = LightTextDim
val TextMutedLight = LightTextDim

val SignalRed = CrimsonPulse
val SignalRedDark = Color(0xFFC41E3A)
val SignalRedLight = Color(0xFFFF8A9E)
val SignalRedSubtle = Color(0x24E1344F) // 14% alpha for Live Gubae banner

val StatusGreen = WayfindingTeal
val StatusAmber = GoldFlame
val StatusBlue = WayfindingTeal

val AccentPrimaryLight = BrandEmber
val AccentPrimaryDark = BrandEmber
val AccentPrimaryContainerLight = Color(0x1FFF5A1F) // 12% alpha
val AccentPrimaryContainerDark = Color(0x28FF5A1F)
val AccentTextLight = BrandEmberInk
val AccentTextDark = BrandEmberInkDark

val SanctuaryGreen = WayfindingTeal
val SanctuaryGreenAccent = WayfindingTeal
val SanctuaryGreenContainer = Color(0x1F0FB2A0)
val SanctuaryGreenLight = WayfindingTeal
val SanctuaryGreenMedium = WayfindingTeal
val SurfaceDark = DarkBgElev
val SurfaceVariantDark = DarkBgSunk
val BorderDark = DarkBorder

val GlassSurfaceDark = Color(0xEE181613)
val GlassSurfaceLight = Color(0xF4FFFFFF)




