package com.example.mekanat_new.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.mekanat_new.ui.theme.BrandEmber
import com.example.mekanat_new.ui.theme.BrandInk
import com.example.mekanat_new.ui.theme.BrandPaper
import com.example.mekanat_new.ui.theme.CrimsonPulse
import com.example.mekanat_new.ui.theme.GoldFlame
import com.example.mekanat_new.ui.theme.MekanatDataTypography
import com.example.mekanat_new.ui.theme.WayfindingTeal
import kotlin.math.cos
import kotlin.math.sin

/**
 * Mekanät Official Brand Mark Badge
 * Renders the exact location pin sanctuary emblem with burgundy halo, gold church & cross,
 * rolling olive green hills, and winding white/gold pilgrim road.
 */
@Composable
fun MekanatBrandMark(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    containerColor: Color = Color.Transparent
) {
    Box(
        modifier = modifier
            .size(size)
            .background(containerColor, RoundedCornerShape(10.dp))
            .testTag("brand_mark"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawMekanatAttachedLogo(
                scope = this,
                progress = 1f
            )
        }
    }
}

/**
 * Topbar Brand Header
 * Brand Mark + "Mekanät" Title + "Brand & Theme System" / Subtitle
 */
@Composable
fun MekanatBrandHeader(
    modifier: Modifier = Modifier,
    subtitle: String = "Brand & Theme System"
) {
    Row(
        modifier = modifier.testTag("brand_header"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MekanatBrandMark(size = 36.dp)
        Column {
            Text(
                text = "Mekanät",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = (-0.02).em,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

/**
 * Hero & Splash Screen Large Logo Vector Composable
 * Faithfully animates the drawing of the user's attached logo.
 */
@Composable
fun MekanatLogoVector(
    modifier: Modifier = Modifier,
    showText: Boolean = false,
    isMonochromeDark: Boolean = false,
    drawPathOnly: Boolean = false,
    animationProgress: Float = 1f
) {
    val inkColor = if (isMonochromeDark) Color(0xFFF3F1EA) else BrandInk
    val emberColor = BrandEmber

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Canvas(
            modifier = Modifier.size(180.dp)
        ) {
            drawMekanatAttachedLogo(
                scope = this,
                progress = animationProgress
            )
        }

        if (showText) {
            Spacer(modifier = Modifier.height(16.dp))

            // Display Title: Mekanät (Space Grotesk display style)
            Text(
                text = "Mekanät",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    letterSpacing = (-0.02).em,
                    color = inkColor
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Amharic Subtitle (Noto Sans Ethiopic style)
            Text(
                text = "መካናት",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = emberColor
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Exact Canvas drawing of the user's attached Mekanät logo.
 * Supports smooth stroke-by-stroke ink drawing progression from 0f to 1f,
 * perfectly matching ic_mekanat_logo_notext.xml with exact coordinates and stroke weights.
 */
fun drawMekanatAttachedLogo(
    scope: DrawScope,
    progress: Float = 1f
) {
    with(scope) {
        val w = size.width
        val h = size.height

        fun fx(x: Float): Float = x * (w / 200f)
        fun fy(y: Float): Float = y * (h / 200f)

        val strokeScale = (w / 200f)

        val goldColor = Color(0xFFC59B4B)
        val deepOlive = Color(0xFF3D4728)
        val darkOlive = Color(0xFF495632)
        val oliveBase = Color(0xFF7B8856)
        val oliveLight = Color(0xFF8A9A68)
        val warmOchre = Color(0xFFB49D6B)
        val oliveBrown = Color(0xFF6E7246)
        val roadBorderTan = Color(0xFFBFA67A)
        val roadWhite = Color(0xFFFFFFFF)

        // ----------------------------------------------------
        // 1. ROLLING HILLS (Olive Green Left, Warm Ochre & Brown Right)
        // ----------------------------------------------------
        if (progress > 0.10f) {
            val hillAlpha = ((progress - 0.10f) / 0.40f).coerceIn(0f, 1f)

            // Left Flank Base (#7B8856)
            val leftFlankPath = Path().apply {
                moveTo(fx(67f), fy(98f))
                cubicTo(fx(76f), fy(96f), fx(90f), fy(101f), fx(100f), fy(108f))
                lineTo(fx(100f), fy(156f))
                lineTo(fx(67f), fy(98f))
                close()
            }
            drawPath(path = leftFlankPath, color = oliveBase.copy(alpha = hillAlpha), style = Fill)

            // Left Hill Top Surface (#8A9A68)
            val leftHillSurface = Path().apply {
                moveTo(fx(67f), fy(98f))
                cubicTo(fx(76f), fy(95f), fx(90f), fy(99f), fx(100f), fy(108f))
                lineTo(fx(100f), fy(120f))
                cubicTo(fx(85f), fy(120f), fx(72f), fy(112f), fx(67f), fy(98f))
                close()
            }
            drawPath(path = leftHillSurface, color = oliveLight.copy(alpha = hillAlpha), style = Fill)

            // Right Upper Hill (Warm Ochre / Tan #B49D6B)
            val rightUpperHill = Path().apply {
                moveTo(fx(100f), fy(108f))
                cubicTo(fx(112f), fy(100f), fx(128f), fy(95f), fx(139f), fy(101f))
                lineTo(fx(126f), fy(122f))
                cubicTo(fx(114f), fy(115f), fx(106f), fy(112f), fx(100f), fy(108f))
                close()
            }
            drawPath(path = rightUpperHill, color = warmOchre.copy(alpha = hillAlpha), style = Fill)

            // Right Lower Hill (Olive Brown #6E7246)
            val rightLowerHill = Path().apply {
                moveTo(fx(139f), fy(101f))
                lineTo(fx(100f), fy(156f))
                lineTo(fx(100f), fy(126f))
                cubicTo(fx(112f), fy(126f), fx(126f), fy(122f), fx(139f), fy(101f))
                close()
            }
            drawPath(path = rightLowerHill, color = oliveBrown.copy(alpha = hillAlpha), style = Fill)
        }

        // ----------------------------------------------------
        // 2. WINDING PILGRIM ROAD (Tan Border + White Path)
        // ----------------------------------------------------
        if (progress > 0.20f) {
            val roadAlpha = ((progress - 0.20f) / 0.40f).coerceIn(0f, 1f)

            // Road Left Tan Border / Shadow (#BFA67A)
            val roadTanPath = Path().apply {
                moveTo(fx(100f), fy(108f))
                cubicTo(fx(104f), fy(108f), fx(108f), fy(110f), fx(110f), fy(113f))
                cubicTo(fx(113f), fy(116f), fx(110f), fy(120f), fx(102f), fy(123f))
                cubicTo(fx(94f), fy(127f), fx(80f), fy(131f), fx(82f), fy(139f))
                cubicTo(fx(84f), fy(146f), fx(95f), fy(152f), fx(100f), fy(156f))
                cubicTo(fx(94f), fy(152f), fx(80f), fy(145f), fx(78f), fy(138f))
                cubicTo(fx(76f), fy(129f), fx(90f), fy(124f), fx(98f), fy(121f))
                cubicTo(fx(105f), fy(118f), fx(106f), fy(114f), fx(102f), fy(111f))
                cubicTo(fx(98f), fy(109f), fx(95f), fy(108f), fx(100f), fy(108f))
                close()
            }
            drawPath(path = roadTanPath, color = roadBorderTan.copy(alpha = roadAlpha), style = Fill)

            // Pure White Inner Pilgrim Path (#FFFFFF)
            val roadWhitePath = Path().apply {
                moveTo(fx(100f), fy(108f))
                cubicTo(fx(105f), fy(108f), fx(109f), fy(111f), fx(110f), fy(114f))
                cubicTo(fx(111f), fy(117f), fx(106f), fy(120f), fx(100f), fy(123f))
                cubicTo(fx(92f), fy(127f), fx(87f), fy(131f), fx(89f), fy(138f))
                cubicTo(fx(91f), fy(144f), fx(96f), fy(150f), fx(100f), fy(154f))
                cubicTo(fx(97f), fy(150f), fx(92f), fy(144f), fx(90f), fy(138f))
                cubicTo(fx(88f), fy(132f), fx(94f), fy(128f), fx(100f), fy(125f))
                cubicTo(fx(107f), fy(121f), fx(109f), fy(117f), fx(106f), fy(114f))
                cubicTo(fx(103f), fy(111f), fx(98f), fy(109f), fx(100f), fy(108f))
                close()
            }
            drawPath(path = roadWhitePath, color = roadWhite.copy(alpha = roadAlpha), style = Fill)
        }

        // ----------------------------------------------------
        // 3. UPPER GOLDEN CIRCULAR HALO LOOP (Left around Top to Satellite Dot)
        // ----------------------------------------------------
        val haloProgress = (progress / 0.50f).coerceIn(0f, 1f)
        val haloPath = Path().apply {
            moveTo(fx(67f), fy(98f))
            cubicTo(
                fx(55f), fy(75f),
                fx(62f), fy(48f),
                fx(82f), fy(38f)
            )
            cubicTo(
                fx(93f), fy(33f),
                fx(107f), fy(33f),
                fx(118f), fy(38f)
            )
            cubicTo(
                fx(136f), fy(46f),
                fx(148f), fy(60f),
                fx(152f), fy(70f)
            )
        }
        drawProgressPath(
            path = haloPath,
            color = goldColor,
            strokeWidth = 5.5f * strokeScale,
            progress = haloProgress
        )

        // ----------------------------------------------------
        // 4. ORBIT SATELLITE BEAD (Gold)
        // ----------------------------------------------------
        if (progress > 0.35f) {
            val beadAlpha = ((progress - 0.35f) / 0.25f).coerceIn(0f, 1f)
            drawCircle(
                color = goldColor.copy(alpha = beadAlpha),
                radius = 5.5f * strokeScale,
                center = Offset(fx(154f), fy(70f))
            )
        }

        // ----------------------------------------------------
        // 5. LOWER RIGHT ORBIT SEGMENT (Dark Olive Stroke)
        // ----------------------------------------------------
        val lowerArcProgress = ((progress - 0.30f) / 0.40f).coerceIn(0f, 1f)
        val lowerArcPath = Path().apply {
            moveTo(fx(152f), fy(78f))
            cubicTo(
                fx(151f), fy(86f),
                fx(146f), fy(94f),
                fx(139f), fy(101f)
            )
        }
        drawProgressPath(
            path = lowerArcPath,
            color = darkOlive,
            strokeWidth = 5.5f * strokeScale,
            progress = lowerArcProgress
        )

        // ----------------------------------------------------
        // 6. BOTTOM PIN OUTLINE TIP (Deep Olive Forest #3D4728)
        // ----------------------------------------------------
        val tipProgress = ((progress - 0.25f) / 0.45f).coerceIn(0f, 1f)
        val tipPath = Path().apply {
            moveTo(fx(67f), fy(98f))
            lineTo(fx(100f), fy(156f))
            lineTo(fx(139f), fy(101f))
        }
        drawProgressPath(
            path = tipPath,
            color = deepOlive,
            strokeWidth = 5.5f * strokeScale,
            progress = tipProgress
        )

        // ----------------------------------------------------
        // 7. GOLDEN CHURCH SILHOUETTE CONTOUR (#C59B4B)
        // ----------------------------------------------------
        val churchProgress = ((progress - 0.45f) / 0.45f).coerceIn(0f, 1f)
        val churchPath = Path().apply {
            moveTo(fx(80f), fy(96f))
            lineTo(fx(80f), fy(86f))
            lineTo(fx(90f), fy(80f))
            lineTo(fx(90f), fy(72f))
            cubicTo(
                fx(90f), fy(62f),
                fx(110f), fy(62f),
                fx(110f), fy(72f)
            )
            lineTo(fx(110f), fy(80f))
            lineTo(fx(120f), fy(86f))
            lineTo(fx(120f), fy(96f))
        }
        drawProgressPath(
            path = churchPath,
            color = goldColor,
            strokeWidth = 4.5f * strokeScale,
            progress = churchProgress
        )

        // ----------------------------------------------------
        // 8. LATIN CROSS AT CHURCH APEX (#C59B4B)
        // ----------------------------------------------------
        if (progress > 0.60f) {
            val crossAlpha = ((progress - 0.60f) / 0.40f).coerceIn(0f, 1f)
            // Vertical bar
            drawLine(
                color = goldColor.copy(alpha = crossAlpha),
                start = Offset(fx(100f), fy(48f)),
                end = Offset(fx(100f), fy(64f)),
                strokeWidth = 4.5f * strokeScale,
                cap = StrokeCap.Round
            )
            // Horizontal bar
            drawLine(
                color = goldColor.copy(alpha = crossAlpha),
                start = Offset(fx(94f), fy(54f)),
                end = Offset(fx(106f), fy(54f)),
                strokeWidth = 4.5f * strokeScale,
                cap = StrokeCap.Round
            )
        }

        // ----------------------------------------------------
        // 9. GOLDEN ARCHED DOORWAY / PORTAL (#C59B4B)
        // ----------------------------------------------------
        if (progress > 0.65f) {
            val portalProgress = ((progress - 0.65f) / 0.35f).coerceIn(0f, 1f)
            val portalPath = Path().apply {
                moveTo(fx(94f), fy(107f))
                lineTo(fx(94f), fy(94f))
                cubicTo(fx(94f), fy(89f), fx(106f), fy(89f), fx(106f), fy(94f))
                lineTo(fx(106f), fy(107f))
            }
            drawProgressPath(
                path = portalPath,
                color = goldColor,
                strokeWidth = 4.5f * strokeScale,
                progress = portalProgress
            )
        }
    }
}

/**
 * Animated Ink Loader — Pulse & Orbital Ring for Search and Live States
 */
@Composable
fun MekanatInkLoader(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    showLabel: Boolean = false,
    label: String = "Mekanät...",
    isDark: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mekanat_loader")

    val drawProgress by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ink_progress"
    )

    val orbitAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "satellite_orbit"
    )

    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawMekanatAttachedLogo(
                    scope = this,
                    progress = drawProgress
                )
            }
        }

        if (showLabel && label.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                style = MekanatDataTypography.eyebrow.copy(
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Global Loading Overlay with the Brand Mark Loader.
 */
@Composable
fun GlobalMekanatLoadingOverlay(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    message: String = "Loading..."
) {
    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f)),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shadowElevation = 8.dp,
                modifier = Modifier.padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MekanatInkLoader(size = 64.dp, showLabel = false)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
    }
}

/**
 * Ethiopian Orthodox Cross Vector Composable (with 1.75px stroke system).
 */
@Composable
fun MekanatCrossIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = 1.75f * (w / 24f)

        // Vertical beam
        drawLine(
            color = tint,
            start = Offset(w / 2f, h * 0.12f),
            end = Offset(w / 2f, h * 0.88f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // Horizontal beam
        drawLine(
            color = tint,
            start = Offset(w * 0.22f, h * 0.38f),
            end = Offset(w * 0.78f, h * 0.38f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )

        // Lower diagonal flourish left
        drawLine(
            color = tint,
            start = Offset(w / 2f, h * 0.65f),
            end = Offset(w * 0.32f, h * 0.78f),
            strokeWidth = strokeW * 0.75f,
            cap = StrokeCap.Round
        )

        // Lower diagonal flourish right
        drawLine(
            color = tint,
            start = Offset(w / 2f, h * 0.65f),
            end = Offset(w * 0.68f, h * 0.78f),
            strokeWidth = strokeW * 0.75f,
            cap = StrokeCap.Round
        )

        // Center halo ring
        drawCircle(
            color = tint,
            radius = w * 0.16f,
            center = Offset(w / 2f, h * 0.38f),
            style = Stroke(width = strokeW * 0.8f)
        )
    }
}

/**
 * Helper to draw animated stroke trim progress.
 */
private fun DrawScope.drawProgressPath(
    path: Path,
    color: Color,
    strokeWidth: Float,
    progress: Float
) {
    if (progress >= 1f) {
        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    } else if (progress > 0.001f) {
        val pathMeasure = PathMeasure()
        pathMeasure.setPath(path, false)
        val length = pathMeasure.length
        val endDistance = length * progress.coerceIn(0f, 1f)

        val trimmedPath = Path()
        pathMeasure.getSegment(0f, endDistance, trimmedPath, true)

        drawPath(
            path = trimmedPath,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

