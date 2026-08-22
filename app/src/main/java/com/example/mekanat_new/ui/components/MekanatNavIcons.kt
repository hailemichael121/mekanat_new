package com.example.mekanat_new.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mekanat_new.ui.theme.BrandEmber

/**
 * Tactile haptic feedback helpers throughout Mekanat.
 */
fun HapticFeedback.vibrateSubtle() {
    this.performHapticFeedback(HapticFeedbackType.TextHandleMove)
}

fun HapticFeedback.vibrateClick() {
    this.performHapticFeedback(HapticFeedbackType.LongPress)
}

// =========================================================================
// Mekanät Custom Stroke Icon System — Section 07 of Brand Document
// 24px Grid, 1.75px Stroke, Rounded Joins
// =========================================================================

/**
 * 07 — Church Sanctuary Icon: Ethiopian Orthodox Sanctuary Dome & Cross
 * SVG: 24dp grid, 1.75dp stroke, rounded joins
 */
@Composable
fun MekanatIconChurchSanctuary(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    filled: Boolean = false
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 1.75f * (w / 24f)
        val strokeStyle = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)

        // Sanctuary Contour: Sloped wings, central tower, dome
        val sanctuaryPath = Path().apply {
            moveTo(4.0f * (w / 24f), 20f * (h / 24f))
            lineTo(4.0f * (w / 24f), 14f * (h / 24f))
            lineTo(7.5f * (w / 24f), 11.5f * (h / 24f))
            lineTo(7.5f * (w / 24f), 9.0f * (h / 24f))
            cubicTo(
                7.5f * (w / 24f), 5.5f * (h / 24f),
                16.5f * (w / 24f), 5.5f * (h / 24f),
                16.5f * (w / 24f), 9.0f * (h / 24f)
            )
            lineTo(16.5f * (w / 24f), 11.5f * (h / 24f))
            lineTo(20.0f * (w / 24f), 14f * (h / 24f))
            lineTo(20.0f * (w / 24f), 20f * (h / 24f))
            close()
        }

        if (filled) {
            drawPath(path = sanctuaryPath, color = tint.copy(alpha = 0.18f), style = Fill)
        }
        drawPath(path = sanctuaryPath, color = tint, style = strokeStyle)

        // Latin Cross atop Sanctuary Apex (vertical + horizontal bar)
        drawLine(
            color = tint,
            start = Offset(12f * (w / 24f), 2.0f * (h / 24f)),
            end = Offset(12f * (w / 24f), 6.0f * (h / 24f)),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(9.8f * (w / 24f), 3.6f * (h / 24f)),
            end = Offset(14.2f * (w / 24f), 3.6f * (h / 24f)),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // Arched Portal / Doorway
        val portalPath = Path().apply {
            moveTo(9.8f * (w / 24f), 20f * (h / 24f))
            lineTo(9.8f * (w / 24f), 15f * (h / 24f))
            cubicTo(
                9.8f * (w / 24f), 12.8f * (h / 24f),
                14.2f * (w / 24f), 12.8f * (h / 24f),
                14.2f * (w / 24f), 15f * (h / 24f)
            )
            lineTo(14.2f * (w / 24f), 20f * (h / 24f))
        }
        drawPath(path = portalPath, color = tint, style = strokeStyle)
    }
}

/**
 * 07 — Map Navigation Icon (Defaults to Custom Church Sanctuaries SVG)
 */
@Composable
fun MekanatIconMap(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    MekanatIconChurchSanctuary(modifier = modifier, size = size, tint = tint)
}

/**
 * Real Vector Icons for Travel Modes (No Emojis)
 */
@Composable
fun MekanatIconDrive(
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 1.75f * (w / 24f)
        val strokeStyle = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)

        // Car Body Contour
        val carPath = Path().apply {
            moveTo(3f * (w / 24f), 14f * (h / 24f))
            lineTo(5f * (w / 24f), 8f * (h / 24f))
            lineTo(19f * (w / 24f), 8f * (h / 24f))
            lineTo(21f * (w / 24f), 14f * (h / 24f))
            lineTo(21f * (w / 24f), 18f * (h / 24f))
            lineTo(19f * (w / 24f), 18f * (h / 24f))
            lineTo(19f * (w / 24f), 16f * (h / 24f))
            lineTo(5f * (w / 24f), 16f * (h / 24f))
            lineTo(5f * (w / 24f), 18f * (h / 24f))
            lineTo(3f * (w / 24f), 18f * (h / 24f))
            close()
        }
        drawPath(path = carPath, color = tint, style = strokeStyle)

        // Wheels
        drawCircle(color = tint, radius = 1.8f * (w / 24f), center = Offset(6.5f * (w / 24f), 17.5f * (h / 24f)), style = strokeStyle)
        drawCircle(color = tint, radius = 1.8f * (w / 24f), center = Offset(17.5f * (w / 24f), 17.5f * (h / 24f)), style = strokeStyle)
        
        // Windshield
        drawLine(color = tint, start = Offset(7f * (w / 24f), 13f * (h / 24f)), end = Offset(17f * (w / 24f), 13f * (h / 24f)), strokeWidth = strokeW, cap = StrokeCap.Round)
    }
}

@Composable
fun MekanatIconWalk(
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    MekanatIconPilgrimage(modifier = modifier, size = size, tint = tint)
}

/**
 * 07 — Pilgrimage / Walking Mode Icon: Pure Clean Walking Man (No Staff, No Clutter)
 */
@Composable
fun MekanatIconPilgrimage(
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 1.9f * (w / 24f)
        val strokeStyle = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)

        // Head (Filled or crisp outline)
        drawCircle(
            color = tint,
            radius = 2.1f * (w / 24f),
            center = Offset(13.5f * (w / 24f), 4.2f * (h / 24f)),
            style = Fill
        )

        // Torso / Spine leaning slightly forward
        val torsoPath = Path().apply {
            moveTo(13.5f * (w / 24f), 7.2f * (h / 24f))
            lineTo(12.0f * (w / 24f), 13.0f * (h / 24f))
        }
        drawPath(path = torsoPath, color = tint, style = strokeStyle)

        // Stepping Front Leg (Knee forward, foot down)
        val frontLegPath = Path().apply {
            moveTo(12.0f * (w / 24f), 13.0f * (h / 24f))
            lineTo(9.2f * (w / 24f), 16.8f * (h / 24f))
            lineTo(6.5f * (w / 24f), 21.0f * (h / 24f))
        }
        drawPath(path = frontLegPath, color = tint, style = strokeStyle)

        // Trailing Back Leg
        val backLegPath = Path().apply {
            moveTo(12.0f * (w / 24f), 13.0f * (h / 24f))
            lineTo(15.2f * (w / 24f), 16.5f * (h / 24f))
            lineTo(17.2f * (w / 24f), 20.8f * (h / 24f))
        }
        drawPath(path = backLegPath, color = tint, style = strokeStyle)

        // Swinging Front Arm (Bent at elbow forward)
        val frontArmPath = Path().apply {
            moveTo(13.2f * (w / 24f), 8.5f * (h / 24f))
            lineTo(10.5f * (w / 24f), 11.8f * (h / 24f))
            lineTo(9.0f * (w / 24f), 14.5f * (h / 24f))
        }
        drawPath(path = frontArmPath, color = tint, style = strokeStyle)

        // Trailing Back Arm
        val backArmPath = Path().apply {
            moveTo(13.2f * (w / 24f), 8.5f * (h / 24f))
            lineTo(16.5f * (w / 24f), 11.2f * (h / 24f))
            lineTo(18.2f * (w / 24f), 13.8f * (h / 24f))
        }
        drawPath(path = backArmPath, color = tint, style = strokeStyle)
    }
}

@Composable
fun MekanatIconTransit(
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 1.75f * (w / 24f)
        val strokeStyle = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)

        // Bus/Transit Body
        drawRoundRect(
            color = tint,
            topLeft = Offset(5f * (w / 24f), 4f * (h / 24f)),
            size = Size(14f * (w / 24f), 15f * (h / 24f)),
            cornerRadius = CornerRadius(2.5f * (w / 24f), 2.5f * (h / 24f)),
            style = strokeStyle
        )

        // Windshield window
        drawLine(color = tint, start = Offset(5f * (w / 24f), 10f * (h / 24f)), end = Offset(19f * (w / 24f), 10f * (h / 24f)), strokeWidth = strokeW, cap = StrokeCap.Round)
        
        // Headlights
        drawCircle(color = tint, radius = 1.2f * (w / 24f), center = Offset(7.5f * (w / 24f), 14.5f * (h / 24f)), style = Fill)
        drawCircle(color = tint, radius = 1.2f * (w / 24f), center = Offset(16.5f * (w / 24f), 14.5f * (h / 24f)), style = Fill)

        // Wheels
        drawLine(color = tint, start = Offset(7f * (w / 24f), 19f * (h / 24f)), end = Offset(7f * (w / 24f), 21.5f * (h / 24f)), strokeWidth = strokeW * 1.3f, cap = StrokeCap.Round)
        drawLine(color = tint, start = Offset(17f * (w / 24f), 19f * (h / 24f)), end = Offset(17f * (w / 24f), 21.5f * (h / 24f)), strokeWidth = strokeW * 1.3f, cap = StrokeCap.Round)
    }
}

/**
 * 07 — Bookmarks Icon: Silk ribbon swallowtail
 * SVG: path d="M7 4.2h10a.8.8 0 0 1 .8.8v14.3l-5.8-3.9-5.8 3.9V5a.8.8 0 0 1 .8-.8z"
 */
@Composable
fun MekanatIconBookmarks(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    filled: Boolean = false
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 1.75f * (w / 24f)

        val path = Path().apply {
            moveTo(7.8f * (w / 24f), 4.2f * (h / 24f))
            lineTo(16.2f * (w / 24f), 4.2f * (h / 24f))
            lineTo(17.8f * (w / 24f), 5.0f * (h / 24f))
            lineTo(17.8f * (w / 24f), 19.3f * (h / 24f))
            lineTo(12.0f * (w / 24f), 15.4f * (h / 24f))
            lineTo(6.2f * (w / 24f), 19.3f * (h / 24f))
            lineTo(6.2f * (w / 24f), 5.0f * (h / 24f))
            close()
        }

        if (filled) {
            drawPath(path = path, color = tint, style = Fill)
        } else {
            drawPath(
                path = path,
                color = tint,
                style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
    }
}

/**
 * 07 — Calendar Icon: Codex calendar with binder rings
 * SVG: rect x=4 y=6 width=16 height=14 rx=3.5; path d="M8 4v4M16 4v4M4 11h16"; circle cx=9 cy=15.3 r=1
 */
@Composable
fun MekanatIconCalendar(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 1.75f * (w / 24f)
        val strokeStyle = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)

        // Rect x=4 y=6 w=16 h=14 rx=3.5
        drawRoundRect(
            color = tint,
            topLeft = Offset(4f * (w / 24f), 6f * (h / 24f)),
            size = Size(16f * (w / 24f), 14f * (h / 24f)),
            cornerRadius = CornerRadius(3.5f * (w / 24f), 3.5f * (h / 24f)),
            style = strokeStyle
        )

        // Binder rings: M8 4v4, M16 4v4
        drawLine(
            color = tint,
            start = Offset(8f * (w / 24f), 4f * (h / 24f)),
            end = Offset(8f * (w / 24f), 8f * (h / 24f)),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(16f * (w / 24f), 4f * (h / 24f)),
            end = Offset(16f * (w / 24f), 8f * (h / 24f)),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // Divider: M4 11h16
        drawLine(
            color = tint,
            start = Offset(4f * (w / 24f), 11f * (h / 24f)),
            end = Offset(20f * (w / 24f), 11f * (h / 24f)),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // Dot: cx=9, cy=15.3, r=1
        drawCircle(
            color = tint,
            radius = 1.2f * (w / 24f),
            center = Offset(9f * (w / 24f), 15.3f * (h / 24f))
        )
    }
}

/**
 * 07 — Profile Icon: Head & shoulder arc
 * SVG: circle cx=12 cy=9 r=3.3; path d="M5.3 20c1-3.6 4-5.4 6.7-5.4s5.7 1.8 6.7 5.4"
 */
@Composable
fun MekanatIconProfile(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 1.75f * (w / 24f)
        val strokeStyle = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)

        // Circle: cx=12, cy=9, r=3.3
        drawCircle(
            color = tint,
            radius = 3.3f * (w / 24f),
            center = Offset(12f * (w / 24f), 9f * (h / 24f)),
            style = strokeStyle
        )

        // Shoulder arc: M5.3 20 c1 -3.6 4 -5.4 6.7 -5.4 s5.7 1.8 6.7 5.4
        val path = Path().apply {
            moveTo(5.3f * (w / 24f), 20f * (h / 24f))
            cubicTo(
                6.3f * (w / 24f), 16.4f * (h / 24f),
                9.3f * (w / 24f), 14.6f * (h / 24f),
                12.0f * (w / 24f), 14.6f * (h / 24f)
            )
            cubicTo(
                14.7f * (w / 24f), 14.6f * (h / 24f),
                17.7f * (w / 24f), 16.4f * (h / 24f),
                18.7f * (w / 24f), 20f * (h / 24f)
            )
        }
        drawPath(path = path, color = tint, style = strokeStyle)
    }
}

/**
 * 07 — Search Icon: Magnifying glass
 * SVG: circle cx=10.3 cy=10.3 r=6.3; path d="M19 19l-3.8-3.8"
 */
@Composable
fun MekanatIconSearch(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 1.75f * (w / 24f)
        val strokeStyle = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)

        drawCircle(
            color = tint,
            radius = 6.3f * (w / 24f),
            center = Offset(10.3f * (w / 24f), 10.3f * (h / 24f)),
            style = strokeStyle
        )
        drawLine(
            color = tint,
            start = Offset(15.2f * (w / 24f), 15.2f * (h / 24f)),
            end = Offset(19.0f * (w / 24f), 19.0f * (h / 24f)),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }
}

/**
 * 07 — Filter Icon: Funnel filter
 * SVG: path d="M4 5.5h16l-6.2 7.3v5.2l-3.6 1.8v-7z"
 */
@Composable
fun MekanatIconFilter(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 1.75f * (w / 24f)

        val path = Path().apply {
            moveTo(4.0f * (w / 24f), 5.5f * (h / 24f))
            lineTo(20.0f * (w / 24f), 5.5f * (h / 24f))
            lineTo(13.8f * (w / 24f), 12.8f * (h / 24f))
            lineTo(13.8f * (w / 24f), 18.0f * (h / 24f))
            lineTo(10.2f * (w / 24f), 19.8f * (h / 24f))
            lineTo(10.2f * (w / 24f), 12.8f * (h / 24f))
            close()
        }
        drawPath(
            path = path,
            color = tint,
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

/**
 * 07 — Recenter GPS Icon: Reticle crosshair with center point
 * SVG: circle cx=12 cy=12 r=2.2; path d="M12 2.8v3M12 18.2v3M2.8 12h3M18.2 12h3"; circle cx=12 cy=12 r=7.2
 */
@Composable
fun MekanatIconRecenter(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 1.75f * (w / 24f)
        val strokeStyle = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)

        // Center dot
        drawCircle(
            color = tint,
            radius = 2.2f * (w / 24f),
            center = Offset(12f * (w / 24f), 12f * (h / 24f)),
            style = Fill
        )

        // Outer ring
        drawCircle(
            color = tint,
            radius = 7.2f * (w / 24f),
            center = Offset(12f * (w / 24f), 12f * (h / 24f)),
            style = strokeStyle
        )

        // Crosshairs
        drawLine(color = tint, start = Offset(12f * (w / 24f), 2.8f * (h / 24f)), end = Offset(12f * (w / 24f), 5.8f * (h / 24f)), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawLine(color = tint, start = Offset(12f * (w / 24f), 18.2f * (h / 24f)), end = Offset(12f * (w / 24f), 21.2f * (h / 24f)), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawLine(color = tint, start = Offset(2.8f * (w / 24f), 12f * (h / 24f)), end = Offset(5.8f * (w / 24f), 12f * (h / 24f)), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawLine(color = tint, start = Offset(18.2f * (w / 24f), 12f * (h / 24f)), end = Offset(21.2f * (w / 24f), 12f * (h / 24f)), strokeWidth = strokeW, cap = StrokeCap.Round)
    }
}

/**
 * 07 — Route Wayfinding Icon: Clean wayfinding path connecting starting waypoint node to destination pin
 */
@Composable
fun MekanatIconRoute(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 2.0f * (w / 24f)
        val strokeStyle = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)

        // Starting Waypoint Ring & Node (bottom-left)
        drawCircle(
            color = tint,
            radius = 2.6f * (w / 24f),
            center = Offset(6f * (w / 24f), 18f * (h / 24f)),
            style = strokeStyle
        )
        drawCircle(
            color = tint,
            radius = 1.2f * (w / 24f),
            center = Offset(6f * (w / 24f), 18f * (h / 24f)),
            style = Fill
        )

        // Destination Waypoint Pin (top-right)
        val destPinPath = Path().apply {
            moveTo(18f * (w / 24f), 12.5f * (h / 24f))
            cubicTo(
                14.5f * (w / 24f), 9.0f * (h / 24f),
                14.5f * (w / 24f), 4.5f * (h / 24f),
                18f * (w / 24f), 4.5f * (h / 24f)
            )
            cubicTo(
                21.5f * (w / 24f), 4.5f * (h / 24f),
                21.5f * (w / 24f), 9.0f * (h / 24f),
                18f * (w / 24f), 12.5f * (h / 24f)
            )
            close()
        }
        drawPath(path = destPinPath, color = tint, style = strokeStyle)
        drawCircle(
            color = tint,
            radius = 1.2f * (w / 24f),
            center = Offset(18f * (w / 24f), 7.2f * (h / 24f)),
            style = Fill
        )

        // S-Curve Connection Highway
        val routePath = Path().apply {
            moveTo(6f * (w / 24f), 15.2f * (h / 24f))
            cubicTo(
                6f * (w / 24f), 10.5f * (h / 24f),
                18f * (w / 24f), 16.5f * (h / 24f),
                18f * (w / 24f), 12.5f * (h / 24f)
            )
        }
        drawPath(
            path = routePath,
            color = tint,
            style = Stroke(
                width = strokeW,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f * (w / 24f), 3.5f * (w / 24f)), 0f)
            )
        )
    }
}

/**
 * 07 — Favorite / Gold Flame Icon: Drop flame
 * SVG: path d="M12 3.3c-2.1 2.9-4.9 4.8-4.9 8.4a4.9 4.9 0 0 0 9.8 0c0-1.8-.9-2.9-1.9-3.8.3 1.4-.5 1.9-1 1.1-.6-1-1-2.6-2-5.7z"
 */
@Composable
fun MekanatIconFavorite(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    filled: Boolean = false
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 1.75f * (w / 24f)

        val path = Path().apply {
            moveTo(12f * (w / 24f), 3.3f * (h / 24f))
            cubicTo(9.9f * (w / 24f), 6.2f * (h / 24f), 7.1f * (w / 24f), 8.1f * (h / 24f), 7.1f * (w / 24f), 11.7f * (h / 24f))
            cubicTo(7.1f * (w / 24f), 14.4f * (h / 24f), 9.3f * (w / 24f), 16.6f * (h / 24f), 12.0f * (w / 24f), 16.6f * (h / 24f))
            cubicTo(14.7f * (w / 24f), 16.6f * (h / 24f), 16.9f * (w / 24f), 14.4f * (h / 24f), 16.9f * (w / 24f), 11.7f * (h / 24f))
            cubicTo(16.9f * (w / 24f), 9.9f * (h / 24f), 16.0f * (w / 24f), 8.8f * (h / 24f), 15.0f * (w / 24f), 7.9f * (h / 24f))
            cubicTo(15.3f * (w / 24f), 9.3f * (h / 24f), 14.5f * (w / 24f), 9.8f * (h / 24f), 14.0f * (w / 24f), 9.0f * (h / 24f))
            cubicTo(13.4f * (w / 24f), 8.0f * (h / 24f), 13.0f * (w / 24f), 6.4f * (h / 24f), 12.0f * (w / 24f), 3.3f * (h / 24f))
            close()
        }

        if (filled) {
            drawPath(path = path, color = tint, style = Fill)
        } else {
            drawPath(
                path = path,
                color = tint,
                style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
    }
}

/**
 * 07 — Live Event Broadcast Waves Icon
 * SVG: circle cx=12 cy=12 r=1.5; path d="M8.6 8.6a5 5 0 0 1 6.8 0" opacity=.85; path d="M6 6a9 9 0 0 1 12 0" opacity=.45
 */
@Composable
fun MekanatIconLiveEvent(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 1.75f * (w / 24f)

        // Center dot
        drawCircle(color = tint, radius = 1.8f * (w / 24f), center = Offset(12f * (w / 24f), 13f * (h / 24f)), style = Fill)

        // Inner arc
        val innerPath = Path().apply {
            moveTo(8.6f * (w / 24f), 9.6f * (h / 24f))
            cubicTo(10.0f * (w / 24f), 7.8f * (h / 24f), 14.0f * (w / 24f), 7.8f * (h / 24f), 15.4f * (w / 24f), 9.6f * (h / 24f))
        }
        drawPath(path = innerPath, color = tint.copy(alpha = 0.9f), style = Stroke(width = strokeW, cap = StrokeCap.Round))

        // Outer arc
        val outerPath = Path().apply {
            moveTo(6.0f * (w / 24f), 7.0f * (h / 24f))
            cubicTo(8.0f * (w / 24f), 4.2f * (h / 24f), 16.0f * (w / 24f), 4.2f * (h / 24f), 18.0f * (w / 24f), 7.0f * (h / 24f))
        }
        drawPath(path = outerPath, color = tint.copy(alpha = 0.55f), style = Stroke(width = strokeW, cap = StrokeCap.Round))
    }
}

/**
 * 07 — Back Icon: Angle arrow
 * SVG: path d="M15 5l-6.5 7L15 19"
 */
@Composable
fun MekanatIconBack(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 1.75f * (w / 24f)

        val path = Path().apply {
            moveTo(15f * (w / 24f), 5f * (h / 24f))
            lineTo(8.5f * (w / 24f), 12f * (h / 24f))
            lineTo(15f * (w / 24f), 19f * (h / 24f))
        }
        drawPath(
            path = path,
            color = tint,
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

/**
 * 07 — Share Icon: 3 Nodes Connected
 * SVG: circle cx=6 cy=12 r=1.9; circle cx=18 cy=6 r=1.9; circle cx=18 cy=18 r=1.9; path d="M7.7 11l8.6-4M7.7 13l8.6 4"
 */
@Composable
fun MekanatIconShare(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 1.75f * (w / 24f)
        val strokeStyle = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)

        drawCircle(color = tint, radius = 2.0f * (w / 24f), center = Offset(6f * (w / 24f), 12f * (h / 24f)), style = strokeStyle)
        drawCircle(color = tint, radius = 2.0f * (w / 24f), center = Offset(18f * (w / 24f), 6f * (h / 24f)), style = strokeStyle)
        drawCircle(color = tint, radius = 2.0f * (w / 24f), center = Offset(18f * (w / 24f), 18f * (h / 24f)), style = strokeStyle)

        drawLine(color = tint, start = Offset(7.7f * (w / 24f), 11f * (h / 24f)), end = Offset(16.3f * (w / 24f), 7f * (h / 24f)), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawLine(color = tint, start = Offset(7.7f * (w / 24f), 13f * (h / 24f)), end = Offset(16.3f * (w / 24f), 17f * (h / 24f)), strokeWidth = strokeW, cap = StrokeCap.Round)
    }
}

/**
 * 07 — Close (X) Icon
 */
@Composable
fun MekanatIconClose(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 1.75f * (w / 24f)

        drawLine(color = tint, start = Offset(6f * (w / 24f), 6f * (h / 24f)), end = Offset(18f * (w / 24f), 18f * (h / 24f)), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawLine(color = tint, start = Offset(18f * (w / 24f), 6f * (h / 24f)), end = Offset(6f * (w / 24f), 18f * (h / 24f)), strokeWidth = strokeW, cap = StrokeCap.Round)
    }
}

// Nav helpers for backwards compatibility
@Composable
fun NavSanctuaryIcon(isSelected: Boolean, modifier: Modifier = Modifier, size: Dp = 24.dp, tint: Color = MaterialTheme.colorScheme.primary) {
    MekanatIconMap(modifier = modifier, size = size, tint = tint)
}

@Composable
fun NavSavedIcon(isSelected: Boolean, modifier: Modifier = Modifier, size: Dp = 24.dp, tint: Color = MaterialTheme.colorScheme.primary) {
    MekanatIconBookmarks(modifier = modifier, size = size, tint = tint, filled = isSelected)
}

@Composable
fun NavCalendarIcon(isSelected: Boolean, modifier: Modifier = Modifier, size: Dp = 24.dp, tint: Color = MaterialTheme.colorScheme.primary) {
    MekanatIconCalendar(modifier = modifier, size = size, tint = tint)
}

@Composable
fun NavProfileIcon(isSelected: Boolean, modifier: Modifier = Modifier, size: Dp = 24.dp, tint: Color = MaterialTheme.colorScheme.primary) {
    MekanatIconProfile(modifier = modifier, size = size, tint = tint)
}

private data class NavTabItem(
    val index: Int,
    val label: String,
    val testTag: String,
    val icon: @Composable (isSelected: Boolean, tint: Color) -> Unit
)

/**
 * 09 — Navigation: Four tabs, one active accent (Ember #FF5A1F)
 * Consistent, purposeful, never decorative.
 */
@Composable
fun MekanatBottomBar(
    currentTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    val tabs = listOf(
        NavTabItem(
            index = 0,
            label = "Map",
            testTag = "nav_tab_map",
            icon = { isSelected, tint -> MekanatIconMap(tint = tint) }
        ),
        NavTabItem(
            index = 1,
            label = "Bookmarks",
            testTag = "nav_tab_bookmarks",
            icon = { isSelected, tint -> MekanatIconBookmarks(tint = tint, filled = isSelected) }
        ),
        NavTabItem(
            index = 2,
            label = "Calendar",
            testTag = "nav_tab_calendar",
            icon = { isSelected, tint -> MekanatIconCalendar(tint = tint) }
        ),
        NavTabItem(
            index = 3,
            label = "Profile",
            testTag = "nav_tab_profile",
            icon = { isSelected, tint -> MekanatIconProfile(tint = tint) }
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("bottom_navigation_bar"),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shadowElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { tab ->
                    val isSelected = currentTab == tab.index

                    val contentColor by animateColorAsState(
                        targetValue = if (isSelected)
                            BrandEmber
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f),
                        animationSpec = tween(180),
                        label = "tabContentColor"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                haptic.vibrateClick()
                                onTabSelected(tab.index)
                            }
                            .testTag(tab.testTag),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            tab.icon(isSelected, contentColor)

                            if (isSelected) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = tab.label,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = contentColor
                                    ),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
