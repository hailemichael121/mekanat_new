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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mekanat_new.ui.theme.BorderLight
import com.example.mekanat_new.ui.theme.CanvasBlack
import com.example.mekanat_new.ui.theme.SignalRed
import kotlin.math.cos
import kotlin.math.sin

/**
 * Beautiful Ethiopian Orthodox Cross Vector Composable.
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
        val strokeW = (w / 12f).coerceAtLeast(1.5f)

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
 * Exact Ethiopian Orthodox Pilgrimage mark drawn with crisp black/white/gray SVG strokes.
 */
@Composable
fun MekanatLogoVector(
    modifier: Modifier = Modifier,
    showText: Boolean = false,
    isMonochromeDark: Boolean = false,
    drawPathOnly: Boolean = false,
    animationProgress: Float = 1f
) {
    val primaryColor = if (isMonochromeDark) Color(0xFFFAFAFA) else Color(0xFF09090B)
    val secondaryColor = if (isMonochromeDark) Color(0xFFA1A1AA) else Color(0xFF71717A)
    val hillShade1 = if (isMonochromeDark) Color(0xFF27272A) else Color(0xFFE4E4E7)
    val hillShade2 = if (isMonochromeDark) Color(0xFF3F3F46) else Color(0xFFD4D4D8)
    val hillShade3 = if (isMonochromeDark) Color(0xFF52525B) else Color(0xFFA1A1AA)
    val roadColor = if (isMonochromeDark) Color(0xFFFAFAFA) else Color(0xFFFFFFFF)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Canvas(
            modifier = Modifier.size(160.dp)
        ) {
            drawMekanatMonochromeLogo(
                scope = this,
                primaryColor = primaryColor,
                secondaryColor = secondaryColor,
                hillShade1 = hillShade1,
                hillShade2 = hillShade2,
                hillShade3 = hillShade3,
                roadColor = roadColor,
                drawPathOnly = drawPathOnly,
                progress = animationProgress
            )
        }

        if (showText) {
            Spacer(modifier = Modifier.height(14.dp))

            // Mekanāt Amharic Title
            Text(
                text = "መካናት",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 34.sp,
                    color = primaryColor,
                    letterSpacing = 2.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Animated Monochrome SVG-Drawn Central Loading Component.
 * Traces the church arch, cross, rolling hills, and pilgrim path in pure monochrome strokes.
 */
@Composable
fun MekanatInkLoader(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    showLabel: Boolean = false,
    label: String = "መካናት...",
    isDark: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mekanat_monochrome_loader")

    // Stroke drawing progress (0f to 1f)
    val drawProgress by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "monochrome_ink_progress"
    )

    // Satellite rotation
    val satelliteAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "monochrome_satellite_orbit"
    )

    val primaryColor = if (isDark) Color(0xFFFAFAFA) else Color(0xFF09090B)
    val secondaryColor = if (isDark) Color(0xFFA1A1AA) else Color(0xFF71717A)
    val hillShade1 = if (isDark) Color(0xFF27272A) else Color(0xFFE4E4E7)
    val hillShade2 = if (isDark) Color(0xFF3F3F46) else Color(0xFFD4D4D8)
    val hillShade3 = if (isDark) Color(0xFF52525B) else Color(0xFFA1A1AA)
    val roadColor = if (isDark) Color(0xFFFAFAFA) else Color(0xFFFFFFFF)

    Column(
        modifier = modifier.padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawMekanatMonochromeLogo(
                    scope = this,
                    primaryColor = primaryColor,
                    secondaryColor = secondaryColor,
                    hillShade1 = hillShade1,
                    hillShade2 = hillShade2,
                    hillShade3 = hillShade3,
                    roadColor = roadColor,
                    drawPathOnly = false,
                    progress = drawProgress,
                    orbitAngle = satelliteAngle
                )
            }
        }

        if (showLabel && label.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = secondaryColor,
                    letterSpacing = 1.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Global Loading Overlay with animated Monochrome SVG Logo drawing.
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
                .background(Color.Black.copy(alpha = 0.35f)),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                modifier = Modifier.padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MekanatInkLoader(size = 80.dp, showLabel = false)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CanvasBlack
                        )
                    )
                }
            }
        }
    }
}

/**
 * Core Canvas drawing logic for the pure Monochrome Mekanāt logo.
 */
private fun drawMekanatMonochromeLogo(
    scope: DrawScope,
    primaryColor: Color,
    secondaryColor: Color,
    hillShade1: Color,
    hillShade2: Color,
    hillShade3: Color,
    roadColor: Color,
    drawPathOnly: Boolean = false,
    progress: Float = 1f,
    orbitAngle: Float? = null
) {
    with(scope) {
        val w = size.width
        val h = size.height

        fun fx(x: Float): Float = x * (w / 100f)
        fun fy(y: Float): Float = y * (h / 100f)

        val strokeWidthScale = (w / 100f) * 2.4f

        // 1. LEFT PIN ARCH (Monochrome Primary)
        val leftArchPath = Path().apply {
            moveTo(fx(32f), fy(54f))
            cubicTo(
                fx(27f), fy(42f),
                fx(32f), fy(18f),
                fx(50f), fy(14f)
            )
            cubicTo(
                fx(58f), fy(14f),
                fx(65f), fy(18f),
                fx(70f), fy(24f)
            )
        }
        drawProgressPath(
            path = leftArchPath,
            color = primaryColor,
            strokeWidth = strokeWidthScale,
            progress = progress
        )

        // 2. RIGHT PIN ARCH & BOTTOM TIP (Monochrome Primary)
        val rightArchPath = Path().apply {
            moveTo(fx(70f), fy(24f))
            cubicTo(
                fx(77f), fy(32f),
                fx(78f), fy(44f),
                fx(71f), fy(56f)
            )
            lineTo(fx(50f), fy(90f))
            lineTo(fx(32f), fy(54f))
        }
        drawProgressPath(
            path = rightArchPath,
            color = primaryColor,
            strokeWidth = strokeWidthScale,
            progress = progress
        )

        // 3. SATELLITE ORBIT DOT
        val dotRadius = fx(2.8f)
        if (orbitAngle != null) {
            val center = Offset(fx(50f), fy(40f))
            val orbitRadius = fx(34f)
            val rad = Math.toRadians(orbitAngle.toDouble()).toFloat()
            val dotCenter = Offset(
                center.x + orbitRadius * cos(rad),
                center.y + orbitRadius * sin(rad)
            )
            drawCircle(
                color = primaryColor,
                radius = dotRadius,
                center = dotCenter
            )
        } else {
            drawCircle(
                color = primaryColor,
                radius = dotRadius,
                center = Offset(fx(78f), fy(29f))
            )
        }

        // 4. ROLLING HILLS (Monochrome Gray Steps)
        if (progress > 0.35f && !drawPathOnly) {
            val hillAlpha = ((progress - 0.35f) / 0.65f).coerceIn(0f, 1f)

            // Left Hill
            val leftHillPath = Path().apply {
                moveTo(fx(32f), fy(54f))
                quadraticTo(fx(42f), fy(50f), fx(49f), fy(60f))
                lineTo(fx(40f), fy(70f))
                quadraticTo(fx(34f), fy(62f), fx(32f), fy(54f))
                close()
            }
            drawPath(
                path = leftHillPath,
                color = hillShade1.copy(alpha = hillAlpha),
                style = Fill
            )

            // Right Hill Upper
            val rightHillUpperPath = Path().apply {
                moveTo(fx(51f), fy(60f))
                quadraticTo(fx(60f), fy(51f), fx(71f), fy(56f))
                lineTo(fx(66f), fy(66f))
                quadraticTo(fx(58f), fy(62f), fx(51f), fy(60f))
                close()
            }
            drawPath(
                path = rightHillUpperPath,
                color = hillShade2.copy(alpha = hillAlpha),
                style = Fill
            )

            // Right Hill Lower
            val rightHillLowerPath = Path().apply {
                moveTo(fx(66f), fy(66f))
                lineTo(fx(50f), fy(90f))
                lineTo(fx(44f), fy(75f))
                quadraticTo(fx(56f), fy(76f), fx(66f), fy(66f))
                close()
            }
            drawPath(
                path = rightHillLowerPath,
                color = hillShade3.copy(alpha = hillAlpha),
                style = Fill
            )

            // 5. WINDING PILGRIM PATH
            val roadPath = Path().apply {
                moveTo(fx(46f), fy(58f))
                lineTo(fx(54f), fy(58f))
                cubicTo(fx(52f), fy(63f), fx(44f), fy(65f), fx(42f), fy(71f))
                cubicTo(fx(40f), fy(76f), fx(52f), fy(82f), fx(50f), fy(90f))
                cubicTo(fx(47f), fy(82f), fx(43f), fy(77f), fx(44f), fy(71f))
                cubicTo(fx(45f), fy(66f), fx(50f), fy(62f), fx(46f), fy(58f))
                close()
            }
            drawPath(
                path = roadPath,
                color = roadColor.copy(alpha = hillAlpha),
                style = Fill
            )
        }

        // 6. CHURCH SILHOUETTE - CROSS (Primary)
        val crossVerticalPath = Path().apply {
            moveTo(fx(50f), fy(22f))
            lineTo(fx(50f), fy(33f))
        }
        drawProgressPath(
            path = crossVerticalPath,
            color = primaryColor,
            strokeWidth = strokeWidthScale,
            progress = progress
        )

        val crossHorizontalPath = Path().apply {
            moveTo(fx(45f), fy(26f))
            lineTo(fx(55f), fy(26f))
        }
        drawProgressPath(
            path = crossHorizontalPath,
            color = primaryColor,
            strokeWidth = strokeWidthScale,
            progress = progress
        )

        // 7. CHURCH BUILDING OUTLINE (Primary)
        val churchOutlinePath = Path().apply {
            moveTo(fx(38f), fy(58f))
            lineTo(fx(38f), fy(44f))
            lineTo(fx(43f), fy(44f))
            cubicTo(fx(43f), fy(37f), fx(57f), fy(37f), fx(57f), fy(44f))
            lineTo(fx(62f), fy(44f))
            lineTo(fx(62f), fy(58f))
        }
        drawProgressPath(
            path = churchOutlinePath,
            color = primaryColor,
            strokeWidth = strokeWidthScale,
            progress = progress
        )

        // 8. CHURCH ARCHED PORTAL / DOORWAY (Primary)
        val doorwayPath = Path().apply {
            moveTo(fx(46f), fy(58f))
            lineTo(fx(46f), fy(51f))
            cubicTo(fx(46f), fy(47f), fx(54f), fy(47f), fx(54f), fy(51f))
            lineTo(fx(54f), fy(58f))
        }
        drawProgressPath(
            path = doorwayPath,
            color = primaryColor,
            strokeWidth = strokeWidthScale,
            progress = progress
        )
    }
}

/**
 * Draws a path with animated stroke trim progress (Ink-drawn effect).
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
    } else {
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
