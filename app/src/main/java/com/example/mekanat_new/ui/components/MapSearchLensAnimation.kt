package com.example.mekanat_new.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mekanat_new.ui.theme.SanctuaryGreenAccent
import com.example.mekanat_new.ui.theme.SanctuaryGreenLight
import com.example.mekanat_new.ui.theme.SignalRed
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Realistic Ethiopian Map Parchment with Scanning Magnifying Lens Vector Animation.
 * Features realistic map paper textures, grid coordinates, topographical contours,
 * winding pilgrimage highway corridors, and a smooth scanning magnifying lens.
 */
@Composable
fun MapSearchLensAnimation(
    searchQuery: String = "",
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mapSearch")

    // Smooth sweeping figure-8 parametric motion for the magnifying lens
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "lensMotion"
    )

    // Pulse radar ripple from lens focal center
    val pulseProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarPulse"
    )

    // Gentle floating angle tilt
    val lensAngle by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lensAngle"
    )

    val isDark = MaterialTheme.colorScheme.background.value == Color(0xFF191C20).value ||
            MaterialTheme.colorScheme.surface.value == Color(0xFF22272B).value

    val primaryGreen = MaterialTheme.colorScheme.primary

    val dotCount by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 3.99f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dotCount"
    )
    val dots = ".".repeat(dotCount.toInt())

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Realistic Map Parchment Paper Canvas
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = if (isDark) Color(0xFF1D2228) else Color(0xFFF7F5EE), // Warm Ethiopian Parchment paper tone
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(22.dp))
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val w = size.width
                val h = size.height

                // Draw realistic parchment map background, contours, roads, and sanctuary markers
                drawRealisticMapPaper(w, h, isDark)

                // Lens Center Coordinates: Smooth Lissajous curve across map canvas
                val angle = progress * 2f * PI.toFloat()
                val lensCenterX = w * 0.5f + (w * 0.30f) * sin(angle)
                val lensCenterY = h * 0.5f + (h * 0.26f) * sin(2f * angle) * 0.85f
                val lensRadius = 40.dp.toPx()

                // 1. Radar Pulse Ring projecting onto the map paper
                val pulseRadius = lensRadius * (1f + pulseProgress * 0.8f)
                val pulseAlpha = (1f - pulseProgress).coerceIn(0f, 1f)
                drawCircle(
                    color = primaryGreen.copy(alpha = pulseAlpha * 0.55f),
                    radius = pulseRadius,
                    center = Offset(lensCenterX, lensCenterY),
                    style = Stroke(width = 2.5.dp.toPx())
                )

                // 2. Magnified Spot Under Lens Glass
                val lensClipPath = Path().apply {
                    addOval(androidx.compose.ui.geometry.Rect(
                        center = Offset(lensCenterX, lensCenterY),
                        radius = lensRadius
                    ))
                }

                clipPath(lensClipPath) {
                    // Highlighted radiance backdrop inside lens
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                (if (isDark) SanctuaryGreenLight else primaryGreen).copy(alpha = 0.32f),
                                (if (isDark) Color(0xFF2E6F54) else Color(0xFFD1FAE5)).copy(alpha = 0.20f),
                                Color.Transparent
                            ),
                            center = Offset(lensCenterX, lensCenterY),
                            radius = lensRadius
                        ),
                        radius = lensRadius,
                        center = Offset(lensCenterX, lensCenterY)
                    )

                    // Draw illuminated zoomed contours & sacred cross under lens
                    drawIlluminatedSanctuary(
                        lensCenterX,
                        lensCenterY,
                        lensRadius,
                        isDark,
                        primaryGreen
                    )
                }

                // 3. Magnifying Glass Rim & 3D Shadow
                drawCircle(
                    color = Color.Black.copy(alpha = 0.30f),
                    radius = lensRadius + 2.dp.toPx(),
                    center = Offset(lensCenterX + 3.dp.toPx(), lensCenterY + 4.dp.toPx()),
                    style = Stroke(width = 4.dp.toPx())
                )

                // Metallic Polished Rim
                val rimGradient = Brush.linearGradient(
                    colors = if (isDark) listOf(
                        Color(0xFFE2E8F0),
                        Color(0xFF718096),
                        Color(0xFF2D3748),
                        Color(0xFFA0AEC0)
                    ) else listOf(
                        Color(0xFFD97706), // Antique gold/brass
                        Color(0xFFFDE68A),
                        Color(0xFFB45309),
                        Color(0xFFFDE68A)
                    ),
                    start = Offset(lensCenterX - lensRadius, lensCenterY - lensRadius),
                    end = Offset(lensCenterX + lensRadius, lensCenterY + lensRadius)
                )

                drawCircle(
                    brush = rimGradient,
                    radius = lensRadius,
                    center = Offset(lensCenterX, lensCenterY),
                    style = Stroke(width = 4.5.dp.toPx())
                )

                // 4. Glass Reflection Arc Highlight (Curved light glare)
                val glarePath = Path().apply {
                    addArc(
                        oval = androidx.compose.ui.geometry.Rect(
                            center = Offset(lensCenterX, lensCenterY),
                            radius = lensRadius - 4.dp.toPx()
                        ),
                        startAngleDegrees = 200f,
                        sweepAngleDegrees = 90f
                    )
                }
                drawPath(
                    path = glarePath,
                    color = Color.White.copy(alpha = if (isDark) 0.60f else 0.85f),
                    style = Stroke(width = 2.8.dp.toPx(), cap = StrokeCap.Round)
                )

                // 5. Lens Ergonomic Handle
                val handleAngle = (45f + lensAngle) * (PI.toFloat() / 180f)
                val handleStartX = lensCenterX + (lensRadius - 1f) * cos(handleAngle)
                val handleStartY = lensCenterY + (lensRadius - 1f) * sin(handleAngle)
                val handleLength = 36.dp.toPx()
                val handleEndX = handleStartX + handleLength * cos(handleAngle)
                val handleEndY = handleStartY + handleLength * sin(handleAngle)

                // Handle Shadow
                drawLine(
                    color = Color.Black.copy(alpha = 0.32f),
                    start = Offset(handleStartX + 2.5.dp.toPx(), handleStartY + 3.5.dp.toPx()),
                    end = Offset(handleEndX + 2.5.dp.toPx(), handleEndY + 3.5.dp.toPx()),
                    strokeWidth = 6.5.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Handle Body
                drawLine(
                    brush = Brush.linearGradient(
                        colors = if (isDark) listOf(Color(0xFF4A5568), Color(0xFF1A202C))
                        else listOf(Color(0xFF78350F), Color(0xFF451A03)),
                        start = Offset(handleStartX, handleStartY),
                        end = Offset(handleEndX, handleEndY)
                    ),
                    start = Offset(handleStartX, handleStartY),
                    end = Offset(handleEndX, handleEndY),
                    strokeWidth = 6.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Clean Responsive Searching Text & Animated Status Dots
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(SignalRed, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (searchQuery.isNotBlank()) "Searching for \"$searchQuery\"$dots" else "Searching sanctuaries$dots",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.5.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Scanning Ethiopian diocese, monasteries & holy places...",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        )
    }
}

/**
 * Clean Full-Screen Search Overlay with Neumorphic Blurred Backdrop and Theme Responsiveness:
 * Displays animated curated realistic SVG map paper and smooth searching text,
 * automatically completing the discovery sequence and revealing results.
 */
@Composable
fun FullScreenSearchOverlayLoader(
    searchQuery: String,
    resultCount: Int,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Automatically dismiss after brief smooth sweep (1600ms)
    LaunchedEffect(Unit) {
        delay(1600)
        onDismiss()
    }

    val isDark = MaterialTheme.colorScheme.background.value == Color(0xFF191C20).value ||
            MaterialTheme.colorScheme.surface.value == Color(0xFF22272B).value

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = if (isDark) listOf(
                        Color(0xEE111519),
                        Color(0xF0191C20),
                        Color(0xEE111519)
                    ) else listOf(
                        Color(0xEEF8FAFC),
                        Color(0xF0FFFFFF),
                        Color(0xEEF1F5F9)
                    )
                )
            )
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = if (isDark) 0.92f else 0.96f),
            shadowElevation = 16.dp,
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = if (isDark) Color.White.copy(alpha = 0.08f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            ),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Realistic Map Parchment Paper & Searching Vector Illustration
                MapSearchLensAnimation(
                    searchQuery = searchQuery,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Draws background realistic map features: parchment paper texture, topographical contours,
 * coordinate dashed grid, rivers, and sacred monastery pin points.
 */
private fun DrawScope.drawRealisticMapPaper(width: Float, height: Float, isDark: Boolean) {
    val gridColor = if (isDark) Color(0x18FFFFFF) else Color(0x1878350F)
    val contourColor = if (isDark) Color(0x283E9B74) else Color(0x35854D0E)
    val roadColor = if (isDark) Color(0x40CBD5E1) else Color(0x50B45309)
    val waterColor = if (isDark) Color(0x3538BDF8) else Color(0x450284C7)

    // 1. Grid Lines (Cartographic coordinate lines)
    val stepX = 42.dp.toPx()
    val stepY = 36.dp.toPx()

    var x = 0f
    while (x <= width) {
        drawLine(
            color = gridColor,
            start = Offset(x, 0f),
            end = Offset(x, height),
            strokeWidth = 0.9.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 8f), 0f)
        )
        x += stepX
    }

    var y = 0f
    while (y <= height) {
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = 0.9.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 8f), 0f)
        )
        y += stepY
    }

    // 2. River Blue Ribbon (Abay / Takkaze River bend)
    val riverPath = Path().apply {
        moveTo(0f, height * 0.50f)
        cubicTo(width * 0.35f, height * 0.40f, width * 0.45f, height * 0.85f, width * 0.85f, height * 0.60f)
        cubicTo(width * 0.92f, height * 0.55f, width * 0.96f, height * 0.70f, width, height * 0.68f)
    }
    drawPath(riverPath, color = waterColor, style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round))

    // 3. Topographical Mountain Contour Curves
    val contourPath1 = Path().apply {
        moveTo(0f, height * 0.28f)
        cubicTo(width * 0.25f, height * 0.12f, width * 0.45f, height * 0.48f, width * 0.75f, height * 0.22f)
        cubicTo(width * 0.88f, height * 0.10f, width * 0.95f, height * 0.26f, width, height * 0.24f)
    }
    drawPath(contourPath1, color = contourColor, style = Stroke(width = 1.8.dp.toPx()))

    val contourPath2 = Path().apply {
        moveTo(0f, height * 0.72f)
        cubicTo(width * 0.3f, height * 0.90f, width * 0.55f, height * 0.52f, width * 0.85f, height * 0.80f)
        lineTo(width, height * 0.78f)
    }
    drawPath(contourPath2, color = contourColor, style = Stroke(width = 1.5.dp.toPx()))

    // 4. Winding Ethiopian Highway Route
    val highwayPath = Path().apply {
        moveTo(width * 0.12f, height)
        cubicTo(width * 0.30f, height * 0.65f, width * 0.26f, height * 0.38f, width * 0.58f, height * 0.35f)
        cubicTo(width * 0.76f, height * 0.32f, width * 0.80f, height * 0.10f, width * 0.90f, 0f)
    }
    drawPath(highwayPath, color = roadColor, style = Stroke(width = 3.2.dp.toPx(), cap = StrokeCap.Round))

    // 5. Sacred Sanctuary Cross Pin Dots on Base Map Paper
    val pins = listOf(
        Offset(width * 0.26f, height * 0.30f),
        Offset(width * 0.60f, height * 0.35f),
        Offset(width * 0.78f, height * 0.65f),
        Offset(width * 0.40f, height * 0.72f),
        Offset(width * 0.70f, height * 0.20f)
    )

    pins.forEach { pt ->
        drawCircle(
            color = if (isDark) Color(0xFF3E9B74).copy(alpha = 0.55f) else Color(0xFFB45309).copy(alpha = 0.45f),
            radius = 3.8.dp.toPx(),
            center = pt
        )
    }
}

/**
 * Draws magnified Ethiopian Cross and glowing coordinate target under the active lens.
 */
private fun DrawScope.drawIlluminatedSanctuary(
    centerX: Float,
    centerY: Float,
    radius: Float,
    isDark: Boolean,
    accentColor: Color
) {
    // Cross center pinpoint
    val crossSize = radius * 0.44f
    val crossColor = if (isDark) Color(0xFF68D391) else Color(0xFF991B1B)

    // Vertical arm
    drawLine(
        color = crossColor,
        start = Offset(centerX, centerY - crossSize),
        end = Offset(centerX, centerY + crossSize),
        strokeWidth = 3.6.dp.toPx(),
        cap = StrokeCap.Round
    )

    // Horizontal arm
    drawLine(
        color = crossColor,
        start = Offset(centerX - crossSize * 0.75f, centerY - crossSize * 0.2f),
        end = Offset(centerX + crossSize * 0.75f, centerY - crossSize * 0.2f),
        strokeWidth = 3.6.dp.toPx(),
        cap = StrokeCap.Round
    )

    // Cross trefoil tips
    drawCircle(color = crossColor, radius = 2.4.dp.toPx(), center = Offset(centerX, centerY - crossSize))
    drawCircle(color = crossColor, radius = 2.2.dp.toPx(), center = Offset(centerX - crossSize * 0.75f, centerY - crossSize * 0.2f))
    drawCircle(color = crossColor, radius = 2.2.dp.toPx(), center = Offset(centerX + crossSize * 0.75f, centerY - crossSize * 0.2f))

    // Crosshair ticks
    val tickLen = 6.5.dp.toPx()
    val tickColor = accentColor.copy(alpha = 0.85f)
    drawLine(color = tickColor, start = Offset(centerX - radius + 6f, centerY), end = Offset(centerX - radius + 6f + tickLen, centerY), strokeWidth = 1.6.dp.toPx())
    drawLine(color = tickColor, start = Offset(centerX + radius - 6f, centerY), end = Offset(centerX + radius - 6f - tickLen, centerY), strokeWidth = 1.6.dp.toPx())
    drawLine(color = tickColor, start = Offset(centerX, centerY - radius + 6f), end = Offset(centerX, centerY - radius + 6f + tickLen), strokeWidth = 1.6.dp.toPx())
    drawLine(color = tickColor, start = Offset(centerX, centerY + radius - 6f), end = Offset(centerX, centerY + radius - 6f - tickLen), strokeWidth = 1.6.dp.toPx())
}
