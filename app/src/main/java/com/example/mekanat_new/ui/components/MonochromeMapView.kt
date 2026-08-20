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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mekanat_new.data.model.ChurchWithDistance
import com.example.mekanat_new.ui.theme.BorderLight
import com.example.mekanat_new.ui.theme.CanvasBlack
import com.example.mekanat_new.ui.theme.SignalRed
import kotlin.math.cos
import kotlin.math.sin

// Geographic bounding box for Ethiopian Horn of Africa
private const val ETHIOPIA_MIN_LAT = 3.4
private const val ETHIOPIA_MAX_LAT = 15.0
private const val ETHIOPIA_MIN_LNG = 33.0
private const val ETHIOPIA_MAX_LNG = 48.0

enum class MonochromeMapTheme {
    LIGHT_REALISTIC,
    MONOCHROME_CLEAN,
    DARK_MINIMAL
}

@Composable
fun MonochromeMapView(
    churches: List<ChurchWithDistance>,
    selectedChurch: ChurchWithDistance?,
    userLat: Double,
    userLng: Double,
    onChurchSelected: (ChurchWithDistance) -> Unit,
    onMapClicked: () -> Unit,
    routeToChurch: ChurchWithDistance? = null,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1.0f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var currentMapTheme by remember { mutableStateOf(MonochromeMapTheme.LIGHT_REALISTIC) }

    val textMeasurer = rememberTextMeasurer()

    // Live Gubae animated pulsing concentric rings
    val infiniteTransition = rememberInfiniteTransition(label = "gubae_pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 38f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseRadius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )
    val secondaryPulseRadius by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 26f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "secondaryPulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("monochrome_map_view")
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(0.65f, 4.8f)
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                }
                .pointerInput(churches, scale, offsetX, offsetY) {
                    detectTapGestures { tapOffset ->
                        val tapped = churches.firstOrNull { item ->
                            val screenPos = projectGeoToScreen(
                                lat = item.church.latitude,
                                lng = item.church.longitude,
                                canvasWidth = size.width.toFloat(),
                                canvasHeight = size.height.toFloat(),
                                scale = scale,
                                offsetX = offsetX,
                                offsetY = offsetY
                            )
                            val dist = (screenPos - tapOffset).getDistance()
                            dist < 40f * scale.coerceAtMost(2f)
                        }

                        if (tapped != null) {
                            onChurchSelected(tapped)
                        } else {
                            onMapClicked()
                        }
                    }
                }
        ) {
            val width = size.width
            val height = size.height

            // 1. Realistic Google Maps-style Base Canvas
            drawRealisticMapBackground(width, height, currentMapTheme)

            // 2. Realistic Ethiopian Highlands, Valleys & Lake Contours
            drawRealisticHighlandsAndLakes(width, height, scale, offsetX, offsetY, currentMapTheme)

            // 3. Realistic Arterial Road Network & City Grids (Google Maps Sketch Style)
            drawRealisticRoadNetwork(width, height, scale, offsetX, offsetY, currentMapTheme)

            // 4. Urban Settlement Footprints / City Nodes
            drawUrbanCityNodes(width, height, scale, offsetX, offsetY, textMeasurer, currentMapTheme)

            // 5. Active Route Polyline (if routing)
            if (routeToChurch != null) {
                val startPos = projectGeoToScreen(userLat, userLng, width, height, scale, offsetX, offsetY)
                val endPos = projectGeoToScreen(
                    routeToChurch.church.latitude,
                    routeToChurch.church.longitude,
                    width,
                    height,
                    scale,
                    offsetX,
                    offsetY
                )
                drawActiveRealisticRoutePolyline(startPos, endPos)
            }

            // 6. Draw Crisp Monochrome SVG Church Markers
            churches.forEach { item ->
                val isSelected = selectedChurch?.church?.id == item.church.id
                val markerPos = projectGeoToScreen(
                    lat = item.church.latitude,
                    lng = item.church.longitude,
                    canvasWidth = width,
                    canvasHeight = height,
                    scale = scale,
                    offsetX = offsetX,
                    offsetY = offsetY
                )

                drawCrispMonochromeChurchPin(
                    pos = markerPos,
                    item = item,
                    isSelected = isSelected,
                    pulseRadius = pulseRadius,
                    secondaryPulseRadius = secondaryPulseRadius,
                    pulseAlpha = pulseAlpha,
                    textMeasurer = textMeasurer,
                    scale = scale,
                    theme = currentMapTheme
                )
            }

            // 7. Animated Glowing GPS User Location Dot
            val userPos = projectGeoToScreen(userLat, userLng, width, height, scale, offsetX, offsetY)
            drawUserGlowingDot(userPos, pulseRadius, pulseAlpha)

            // 8. Subtle Vector Scale Indicator / Lat-Lng Grid
            drawMinimalGrid(width, height, currentMapTheme)
        }

        // Map Control Floating Buttons on Right Side (Perfect Circular, Pilled, 1px Border, No Shadow)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.End
        ) {
            MapCircularControl(
                icon = {
                    Icon(
                        Icons.Default.Layers,
                        contentDescription = "Map Style Layer",
                        modifier = Modifier.size(18.dp),
                        tint = CanvasBlack
                    )
                },
                onClick = {
                    currentMapTheme = when (currentMapTheme) {
                        MonochromeMapTheme.LIGHT_REALISTIC -> MonochromeMapTheme.MONOCHROME_CLEAN
                        MonochromeMapTheme.MONOCHROME_CLEAN -> MonochromeMapTheme.DARK_MINIMAL
                        MonochromeMapTheme.DARK_MINIMAL -> MonochromeMapTheme.LIGHT_REALISTIC
                    }
                },
                testTag = "map_theme_toggle_button"
            )

            MapCircularControl(
                icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Zoom In",
                        modifier = Modifier.size(20.dp),
                        tint = CanvasBlack
                    )
                },
                onClick = { scale = (scale * 1.3f).coerceAtMost(4.8f) },
                testTag = "map_zoom_in_button"
            )

            MapCircularControl(
                icon = {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "Zoom Out",
                        modifier = Modifier.size(20.dp),
                        tint = CanvasBlack
                    )
                },
                onClick = { scale = (scale / 1.3f).coerceAtLeast(0.65f) },
                testTag = "map_zoom_out_button"
            )

            MapCircularControl(
                icon = {
                    Icon(
                        Icons.Default.MyLocation,
                        contentDescription = "Recenter My Location",
                        modifier = Modifier.size(20.dp),
                        tint = SignalRed
                    )
                },
                onClick = {
                    scale = 1.2f
                    offsetX = 0f
                    offsetY = 0f
                },
                testTag = "map_recenter_button"
            )
        }

        // Clean Modern Map Status Tag at Bottom-Left (Pill shape, No Shadow, 1px Border)
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White.copy(alpha = 0.95f),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 80.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(SignalRed, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    Icons.Default.Church,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp),
                    tint = CanvasBlack
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${churches.size} EOTC Sanctuaries",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = CanvasBlack
                    )
                )
            }
        }
    }
}

/**
 * Perfectly fitted circular control button for the right side of the map.
 */
@Composable
private fun MapCircularControl(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(1.dp, BorderLight, CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

private fun projectGeoToScreen(
    lat: Double,
    lng: Double,
    canvasWidth: Float,
    canvasHeight: Float,
    scale: Float,
    offsetX: Float,
    offsetY: Float
): Offset {
    val normX = ((lng - ETHIOPIA_MIN_LNG) / (ETHIOPIA_MAX_LNG - ETHIOPIA_MIN_LNG)).toFloat()
    val normY = (1f - ((lat - ETHIOPIA_MIN_LAT) / (ETHIOPIA_MAX_LAT - ETHIOPIA_MIN_LAT))).toFloat()

    val centerX = canvasWidth / 2f
    val centerY = canvasHeight / 2f

    val basePaddingX = canvasWidth * 0.08f
    val basePaddingY = canvasHeight * 0.08f
    val usableW = canvasWidth - basePaddingX * 2
    val usableH = canvasHeight - basePaddingY * 2

    val rawX = basePaddingX + normX * usableW
    val rawY = basePaddingY + normY * usableH

    val scaledX = centerX + (rawX - centerX) * scale + offsetX
    val scaledY = centerY + (rawY - centerY) * scale + offsetY

    return Offset(scaledX, scaledY)
}

/**
 * Realistic Google Maps-style base canvas background.
 */
private fun DrawScope.drawRealisticMapBackground(width: Float, height: Float, theme: MonochromeMapTheme) {
    val bgColor = when (theme) {
        MonochromeMapTheme.LIGHT_REALISTIC -> Color(0xFFF6F6F7)
        MonochromeMapTheme.MONOCHROME_CLEAN -> Color(0xFFFAFAFA)
        MonochromeMapTheme.DARK_MINIMAL -> Color(0xFF09090B)
    }
    drawRect(color = bgColor, size = Size(width, height))
}

/**
 * Realistic Google Maps-style vector topography: Ethiopian Highlands plateau,
 * Lake Tana, Rift Valley lakes, and Blue Nile (Abay River).
 */
private fun DrawScope.drawRealisticHighlandsAndLakes(
    width: Float,
    height: Float,
    scale: Float,
    offsetX: Float,
    offsetY: Float,
    theme: MonochromeMapTheme
) {
    // 1. Ethiopian Central & Northern Highlands Plateau Polygon
    val plateauPoints = listOf(
        projectGeoToScreen(14.6, 38.2, width, height, scale, offsetX, offsetY),
        projectGeoToScreen(14.3, 39.7, width, height, scale, offsetX, offsetY),
        projectGeoToScreen(13.5, 39.8, width, height, scale, offsetX, offsetY),
        projectGeoToScreen(12.6, 39.7, width, height, scale, offsetX, offsetY),
        projectGeoToScreen(11.8, 39.9, width, height, scale, offsetX, offsetY),
        projectGeoToScreen(10.5, 39.8, width, height, scale, offsetX, offsetY),
        projectGeoToScreen(9.0, 39.2, width, height, scale, offsetX, offsetY),
        projectGeoToScreen(8.2, 38.8, width, height, scale, offsetX, offsetY),
        projectGeoToScreen(7.0, 38.2, width, height, scale, offsetX, offsetY),
        projectGeoToScreen(6.2, 36.8, width, height, scale, offsetX, offsetY),
        projectGeoToScreen(7.5, 35.8, width, height, scale, offsetX, offsetY),
        projectGeoToScreen(9.2, 35.2, width, height, scale, offsetX, offsetY),
        projectGeoToScreen(11.5, 36.2, width, height, scale, offsetX, offsetY),
        projectGeoToScreen(12.8, 36.8, width, height, scale, offsetX, offsetY),
        projectGeoToScreen(13.9, 37.6, width, height, scale, offsetX, offsetY)
    )

    val highlandsPath = Path().apply {
        plateauPoints.forEachIndexed { i, pt ->
            if (i == 0) moveTo(pt.x, pt.y) else lineTo(pt.x, pt.y)
        }
        close()
    }

    val highlandsFill = when (theme) {
        MonochromeMapTheme.LIGHT_REALISTIC -> Color(0xFFECEEF0)
        MonochromeMapTheme.MONOCHROME_CLEAN -> Color(0xFFF1F1F3)
        MonochromeMapTheme.DARK_MINIMAL -> Color(0xFF141418)
    }
    val highlandsOutline = when (theme) {
        MonochromeMapTheme.LIGHT_REALISTIC -> Color(0xFFDCDFE3)
        MonochromeMapTheme.MONOCHROME_CLEAN -> Color(0xFFE4E4E7)
        MonochromeMapTheme.DARK_MINIMAL -> Color(0xFF27272A)
    }

    drawPath(path = highlandsPath, color = highlandsFill)
    drawPath(
        path = highlandsPath,
        color = highlandsOutline,
        style = Stroke(
            width = 1.2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f))
        )
    )

    // 2. Lake Tana (Realistic Contour + Island points)
    val tanaCenter = projectGeoToScreen(12.0, 37.3, width, height, scale, offsetX, offsetY)
    val tanaRadius = 26f * scale
    val waterFill = when (theme) {
        MonochromeMapTheme.LIGHT_REALISTIC -> Color(0xFFD3E0EA)
        MonochromeMapTheme.MONOCHROME_CLEAN -> Color(0xFFE2E8F0)
        MonochromeMapTheme.DARK_MINIMAL -> Color(0xFF18202A)
    }
    val waterBorder = when (theme) {
        MonochromeMapTheme.LIGHT_REALISTIC -> Color(0xFFBACCDA)
        MonochromeMapTheme.MONOCHROME_CLEAN -> Color(0xFFCBD5E1)
        MonochromeMapTheme.DARK_MINIMAL -> Color(0xFF263242)
    }

    drawCircle(
        color = waterFill,
        radius = tanaRadius,
        center = tanaCenter
    )
    drawCircle(
        color = waterBorder,
        radius = tanaRadius,
        center = tanaCenter,
        style = Stroke(width = 1.5f)
    )

    // Island dots inside Lake Tana (Dega Estifanos, Tana Qirqos)
    drawCircle(
        color = highlandsFill,
        radius = 3.5f * scale.coerceAtMost(1.8f),
        center = Offset(tanaCenter.x + 4f * scale, tanaCenter.y - 3f * scale)
    )

    // 3. Blue Nile / Abay River Loop
    val nileStart = tanaCenter
    val nileMid1 = projectGeoToScreen(11.3, 37.6, width, height, scale, offsetX, offsetY)
    val nileMid2 = projectGeoToScreen(10.0, 38.1, width, height, scale, offsetX, offsetY)
    val nileMid3 = projectGeoToScreen(10.1, 36.5, width, height, scale, offsetX, offsetY)
    val nileEnd = projectGeoToScreen(11.2, 35.0, width, height, scale, offsetX, offsetY)

    val nilePath = Path().apply {
        moveTo(nileStart.x, nileStart.y)
        cubicTo(nileMid1.x, nileMid1.y, nileMid2.x, nileMid2.y, nileMid3.x, nileMid3.y)
        lineTo(nileEnd.x, nileEnd.y)
    }
    drawPath(
        path = nilePath,
        color = waterFill,
        style = Stroke(width = 2.5f * scale.coerceAtMost(2f), cap = StrokeCap.Round)
    )

    // 4. Rift Valley Lakes (Lake Ziway, Langano, Hawassa, Abaya, Chamo)
    val riftLakes = listOf(
        projectGeoToScreen(8.0, 38.7, width, height, scale, offsetX, offsetY) to 8f,   // Ziway
        projectGeoToScreen(7.6, 38.7, width, height, scale, offsetX, offsetY) to 7f,   // Langano
        projectGeoToScreen(7.0, 38.5, width, height, scale, offsetX, offsetY) to 6f,   // Hawassa
        projectGeoToScreen(6.4, 37.9, width, height, scale, offsetX, offsetY) to 12f,  // Abaya
        projectGeoToScreen(5.8, 37.5, width, height, scale, offsetX, offsetY) to 9f    // Chamo
    )
    riftLakes.forEach { (center, r) ->
        drawCircle(color = waterFill, radius = r * scale, center = center)
        drawCircle(color = waterBorder, radius = r * scale, center = center, style = Stroke(width = 1f))
    }
}

/**
 * Realistic Arterial Road Network connecting major Ethiopian religious centers,
 * styled after Google Maps vector road drawings.
 */
private fun DrawScope.drawRealisticRoadNetwork(
    width: Float,
    height: Float,
    scale: Float,
    offsetX: Float,
    offsetY: Float,
    theme: MonochromeMapTheme
) {
    val roadCasingColor = when (theme) {
        MonochromeMapTheme.LIGHT_REALISTIC -> Color(0xFFD4D4D8)
        MonochromeMapTheme.MONOCHROME_CLEAN -> Color(0xFFE4E4E7)
        MonochromeMapTheme.DARK_MINIMAL -> Color(0xFF27272A)
    }
    val roadCoreColor = when (theme) {
        MonochromeMapTheme.LIGHT_REALISTIC -> Color(0xFFFFFFFF)
        MonochromeMapTheme.MONOCHROME_CLEAN -> Color(0xFFFFFFFF)
        MonochromeMapTheme.DARK_MINIMAL -> Color(0xFF1E1E24)
    }

    val addis = projectGeoToScreen(9.03, 38.74, width, height, scale, offsetX, offsetY)
    val debreLibanos = projectGeoToScreen(9.71, 38.86, width, height, scale, offsetX, offsetY)
    val bahirDar = projectGeoToScreen(11.59, 37.39, width, height, scale, offsetX, offsetY)
    val gondar = projectGeoToScreen(12.60, 37.46, width, height, scale, offsetX, offsetY)
    val lalibela = projectGeoToScreen(12.03, 39.04, width, height, scale, offsetX, offsetY)
    val dessie = projectGeoToScreen(11.13, 39.63, width, height, scale, offsetX, offsetY)
    val mekelle = projectGeoToScreen(13.49, 39.47, width, height, scale, offsetX, offsetY)
    val axum = projectGeoToScreen(14.12, 38.72, width, height, scale, offsetX, offsetY)
    val harar = projectGeoToScreen(9.31, 42.12, width, height, scale, offsetX, offsetY)
    val hawassa = projectGeoToScreen(7.05, 38.47, width, height, scale, offsetX, offsetY)

    val corridors = listOf(
        listOf(addis, debreLibanos, bahirDar, gondar, axum),
        listOf(addis, dessie, lalibela, mekelle, axum),
        listOf(gondar, lalibela),
        listOf(addis, harar),
        listOf(addis, hawassa)
    )

    // Draw road outer casing (for crisp Google Maps contrast)
    corridors.forEach { route ->
        val path = Path().apply {
            route.forEachIndexed { i, pt ->
                if (i == 0) moveTo(pt.x, pt.y) else lineTo(pt.x, pt.y)
            }
        }
        drawPath(
            path = path,
            color = roadCasingColor,
            style = Stroke(width = 4.5f * scale.coerceAtMost(1.8f), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }

    // Draw road white inner core
    corridors.forEach { route ->
        val path = Path().apply {
            route.forEachIndexed { i, pt ->
                if (i == 0) moveTo(pt.x, pt.y) else lineTo(pt.x, pt.y)
            }
        }
        drawPath(
            path = path,
            color = roadCoreColor,
            style = Stroke(width = 2.5f * scale.coerceAtMost(1.8f), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

/**
 * Urban city labels and geometric sketch footprints for major hubs.
 */
private fun DrawScope.drawUrbanCityNodes(
    width: Float,
    height: Float,
    scale: Float,
    offsetX: Float,
    offsetY: Float,
    textMeasurer: TextMeasurer,
    theme: MonochromeMapTheme
) {
    val cities = listOf(
        "Addis Ababa" to projectGeoToScreen(9.03, 38.74, width, height, scale, offsetX, offsetY),
        "Lalibela" to projectGeoToScreen(12.03, 39.04, width, height, scale, offsetX, offsetY),
        "Gondar" to projectGeoToScreen(12.60, 37.46, width, height, scale, offsetX, offsetY),
        "Axum" to projectGeoToScreen(14.12, 38.72, width, height, scale, offsetX, offsetY),
        "Bahir Dar" to projectGeoToScreen(11.59, 37.39, width, height, scale, offsetX, offsetY),
        "Harar" to projectGeoToScreen(9.31, 42.12, width, height, scale, offsetX, offsetY)
    )

    val labelColor = when (theme) {
        MonochromeMapTheme.DARK_MINIMAL -> Color(0xFFA1A1AA)
        else -> Color(0xFF71717A)
    }

    cities.forEach { (cityName, pos) ->
        // City center dot
        drawCircle(
            color = labelColor,
            radius = 3.2f * scale.coerceAtMost(1.5f),
            center = pos
        )

        // Text label
        val textLayout = textMeasurer.measure(
            text = cityName,
            style = TextStyle(
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = labelColor
            )
        )
        drawText(
            textLayoutResult = textLayout,
            topLeft = Offset(pos.x + 6f, pos.y - textLayout.size.height / 2f)
        )
    }
}

/**
 * Route Polyline drawn with clean Signal Red casing and subtle animated path.
 */
private fun DrawScope.drawActiveRealisticRoutePolyline(start: Offset, end: Offset) {
    val mid1 = Offset(start.x + (end.x - start.x) * 0.35f - 12f, start.y + (end.y - start.y) * 0.35f + 8f)
    val mid2 = Offset(start.x + (end.x - start.x) * 0.65f + 12f, start.y + (end.y - start.y) * 0.65f - 8f)

    val routePath = Path().apply {
        moveTo(start.x, start.y)
        cubicTo(mid1.x, mid1.y, mid2.x, mid2.y, end.x, end.y)
    }

    // Outer subtle red glow
    drawPath(
        path = routePath,
        color = SignalRed.copy(alpha = 0.2f),
        style = Stroke(width = 10f, cap = StrokeCap.Round)
    )

    // Solid crisp Signal Red route line with dash animation
    drawPath(
        path = routePath,
        color = SignalRed,
        style = Stroke(
            width = 4f,
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 6f))
        )
    )
}

/**
 * Crisp Monochrome SVG-Drawn Church Marker.
 * - Default State: Pure Monochrome Black/Gray teardrop pin with white center and etched Ethiopian cross cutout.
 * - Selected State: Clean Signal Red SVG pin with white cross, crisp callout card.
 * - Active Gubae: Concentric animated pulsing halo rings expanding outward.
 */
private fun DrawScope.drawCrispMonochromeChurchPin(
    pos: Offset,
    item: ChurchWithDistance,
    isSelected: Boolean,
    pulseRadius: Float,
    secondaryPulseRadius: Float,
    pulseAlpha: Float,
    textMeasurer: TextMeasurer,
    scale: Float,
    theme: MonochromeMapTheme
) {
    val church = item.church
    val isLive = item.hasActiveGubae

    // 1. Live Gubae Concentric Pulsing Rings (Only active during events)
    if (isLive) {
        // Outer expanding pulse
        drawCircle(
            color = SignalRed.copy(alpha = pulseAlpha * 0.55f),
            radius = pulseRadius + 20f,
            center = pos
        )
        // Inner secondary pulse
        drawCircle(
            color = SignalRed.copy(alpha = (1f - (secondaryPulseRadius / 26f)) * 0.4f),
            radius = secondaryPulseRadius + 12f,
            center = pos
        )
    }

    // 2. SVG Pin Geometry Parameters
    val pinHeight = if (isSelected) 36f else 28f
    val pinWidth = pinHeight * 0.72f
    val pinTip = pos
    val pinCenter = Offset(pos.x, pos.y - pinHeight * 0.65f)
    val headRadius = pinWidth / 2f

    // Teardrop Pin Path
    val pinPath = Path().apply {
        moveTo(pinTip.x, pinTip.y)
        cubicTo(
            pinTip.x - headRadius * 0.9f, pinTip.y - pinHeight * 0.45f,
            pinCenter.x - headRadius, pinCenter.y + headRadius * 0.3f,
            pinCenter.x - headRadius, pinCenter.y
        )
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(
                left = pinCenter.x - headRadius,
                top = pinCenter.y - headRadius,
                right = pinCenter.x + headRadius,
                bottom = pinCenter.y + headRadius
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
        cubicTo(
            pinCenter.x + headRadius, pinCenter.y + headRadius * 0.3f,
            pinTip.x + headRadius * 0.9f, pinTip.y - pinHeight * 0.45f,
            pinTip.x, pinTip.y
        )
        close()
    }

    // Pin Shadow
    drawCircle(
        color = Color.Black.copy(alpha = 0.25f),
        radius = headRadius * 0.75f,
        center = Offset(pos.x, pos.y + 2f)
    )

    // Pin Body Fill
    val pinColor = if (isSelected) SignalRed else {
        when (theme) {
            MonochromeMapTheme.DARK_MINIMAL -> Color(0xFFFAFAFA)
            else -> Color(0xFF18181B)
        }
    }
    drawPath(path = pinPath, color = pinColor, style = Fill)

    // Crisp 1px Outline
    val pinOutline = if (isSelected) Color(0xFFB71C1C) else Color(0xFF09090B)
    drawPath(path = pinPath, color = pinOutline, style = Stroke(width = 1f))

    // Inner White Circular Core Cutout
    val coreRadius = headRadius * 0.58f
    val innerCoreColor = if (isSelected) Color.White else Color.White
    drawCircle(
        color = innerCoreColor,
        radius = coreRadius,
        center = pinCenter
    )

    // Etched Ethiopian Orthodox Cross in Center
    val crossColor = if (isSelected) SignalRed else Color(0xFF18181B)
    val crossArm = coreRadius * 0.65f
    val crossStroke = 1.8f

    // Vertical beam
    drawLine(
        color = crossColor,
        start = Offset(pinCenter.x, pinCenter.y - crossArm),
        end = Offset(pinCenter.x, pinCenter.y + crossArm),
        strokeWidth = crossStroke,
        cap = StrokeCap.Round
    )
    // Horizontal beam
    drawLine(
        color = crossColor,
        start = Offset(pinCenter.x - crossArm * 0.75f, pinCenter.y - crossArm * 0.2f),
        end = Offset(pinCenter.x + crossArm * 0.75f, pinCenter.y - crossArm * 0.2f),
        strokeWidth = crossStroke,
        cap = StrokeCap.Round
    )

    // 3. Church Label Callout Badge (Shown when selected or zoomed)
    if (isSelected || scale > 1.6f) {
        val labelText = church.name.split("(").first().trim()
        val textLayout = textMeasurer.measure(
            text = labelText,
            style = TextStyle(
                fontSize = if (isSelected) 11.sp else 9.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else CanvasBlack
            )
        )

        val badgeX = pos.x - textLayout.size.width / 2f
        val badgeY = pos.y + 6f
        val badgeBg = if (isSelected) SignalRed else Color.White

        // Badge Pill Background
        drawRoundRect(
            color = badgeBg,
            topLeft = Offset(badgeX - 8f, badgeY - 3f),
            size = Size(textLayout.size.width + 16f, textLayout.size.height + 6f),
            cornerRadius = CornerRadius(12f, 12f)
        )
        // 1px Badge Border
        drawRoundRect(
            color = if (isSelected) Color(0xFFB71C1C) else BorderLight,
            topLeft = Offset(badgeX - 8f, badgeY - 3f),
            size = Size(textLayout.size.width + 16f, textLayout.size.height + 6f),
            cornerRadius = CornerRadius(12f, 12f),
            style = Stroke(width = 1f)
        )

        drawText(
            textLayoutResult = textLayout,
            topLeft = Offset(badgeX, badgeY)
        )
    }
}

/**
 * Animated GPS User Location Dot.
 */
private fun DrawScope.drawUserGlowingDot(
    pos: Offset,
    pulseRadius: Float,
    pulseAlpha: Float
) {
    // Pulsing radar ring
    drawCircle(
        color = SignalRed.copy(alpha = pulseAlpha * 0.5f),
        radius = pulseRadius + 14f,
        center = pos
    )
    // White outer ring
    drawCircle(
        color = Color.White,
        radius = 8f,
        center = pos
    )
    // 1px Border
    drawCircle(
        color = BorderLight,
        radius = 8f,
        center = pos,
        style = Stroke(width = 1f)
    )
    // Signal Red Dot
    drawCircle(
        color = SignalRed,
        radius = 5.5f,
        center = pos
    )
}

/**
 * Subtle Minimal Grid Lines.
 */
private fun DrawScope.drawMinimalGrid(width: Float, height: Float, theme: MonochromeMapTheme) {
    val gridColor = when (theme) {
        MonochromeMapTheme.DARK_MINIMAL -> Color(0x08FFFFFF)
        else -> Color(0x08000000)
    }
    val step = 80f

    var x = 0f
    while (x < width) {
        drawLine(color = gridColor, start = Offset(x, 0f), end = Offset(x, height), strokeWidth = 1f)
        x += step
    }

    var y = 0f
    while (y < height) {
        drawLine(color = gridColor, start = Offset(0f, y), end = Offset(width, y), strokeWidth = 1f)
        y += step
    }
}
