package com.example.mekanat_new.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mekanat_new.data.maps.GebetaMapService
import com.example.mekanat_new.data.maps.GebetaMapStyle
import com.example.mekanat_new.data.maps.GebetaRouteResult
import com.example.mekanat_new.data.maps.GebetaTravelMode
import com.example.mekanat_new.data.maps.TileCoord
import com.example.mekanat_new.data.model.ChurchWithDistance
import com.example.mekanat_new.ui.theme.BorderLight
import com.example.mekanat_new.ui.theme.CanvasBlack
import com.example.mekanat_new.ui.theme.SignalRed
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Modern High-Performance Map View component powered by Gebeta Maps Tile & Direction Services.
 * Supports raster/vector tile streaming, multi-layer switching (Standard, Monochrome, Topo, Dark),
 * smooth pan/zoom gestures, custom Ethiopian Orthodox Church pins with Live Gubae pulses,
 * GPS pilgrim tracking, and real-time Gebeta route polyline overlays.
 */
@Composable
fun GebetaMapView(
    churches: List<ChurchWithDistance>,
    selectedChurch: ChurchWithDistance?,
    userLat: Double,
    userLng: Double,
    onChurchSelected: (ChurchWithDistance) -> Unit,
    onMapClicked: () -> Unit,
    routeToChurch: ChurchWithDistance? = null,
    routeResult: GebetaRouteResult? = null,
    isCalculatingRoute: Boolean = false,
    travelMode: GebetaTravelMode = GebetaTravelMode.DRIVING,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val gebetaService = remember { GebetaMapService.getInstance(context) }

    // Map Camera Coordinates & Zoom State
    var centerLat by remember { mutableStateOf(userLat) }
    var centerLng by remember { mutableStateOf(userLng) }
    var zoomLevel by remember { mutableIntStateOf(7) } // 4 to 17
    var zoomFraction by remember { mutableFloatStateOf(0.0f) }
    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var panOffsetY by remember { mutableFloatStateOf(0f) }

    var currentStyle by remember { mutableStateOf(GebetaMapStyle.GEBETA_STANDARD) }
    var showLayerDialog by remember { mutableStateOf(false) }

    // Tile Bitmap Cache in Compose State for Live Rendering
    val tileBitmapMap = remember { mutableStateMapOf<String, ImageBitmap>() }

    val textMeasurer = rememberTextMeasurer()

    // Smooth Live Gubae pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "gebeta_gubae_pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 36f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseRadius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    // Animated dash flow for Gebeta Route Polyline
    val routeDashPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 36f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "routeDashPhase"
    )

    // Auto-fit route when calculated or selected
    LaunchedEffect(routeResult) {
        routeResult?.let { res ->
            val midLat = (res.originLat + res.destinationLat) / 2.0
            val midLng = (res.originLng + res.destinationLng) / 2.0
            centerLat = midLat
            centerLng = midLng
            panOffsetX = 0f
            panOffsetY = 0f
            val maxSpan = max(abs(res.originLat - res.destinationLat), abs(res.originLng - res.destinationLng))
            zoomLevel = when {
                maxSpan > 6.0 -> 6
                maxSpan > 3.0 -> 7
                maxSpan > 1.5 -> 8
                maxSpan > 0.8 -> 9
                maxSpan > 0.3 -> 11
                maxSpan > 0.1 -> 12
                else -> 14
            }
            zoomFraction = 0f
        }
    }

    // Center camera when selected church changes (without active route)
    LaunchedEffect(selectedChurch) {
        if (routeToChurch == null && selectedChurch != null) {
            centerLat = selectedChurch.church.latitude
            centerLng = selectedChurch.church.longitude
            zoomLevel = max(zoomLevel, 10)
            panOffsetX = 0f
            panOffsetY = 0f
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("gebeta_map_view")
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        // Smooth pinch-to-zoom
                        val totalZoom = zoomLevel + zoomFraction + (zoom - 1.0f) * 1.5f
                        val clampedZoom = totalZoom.coerceIn(4.0f, 17.0f)
                        zoomLevel = clampedZoom.toInt()
                        zoomFraction = clampedZoom - zoomLevel

                        panOffsetX += pan.x
                        panOffsetY += pan.y
                    }
                }
                .pointerInput(churches, zoomLevel, zoomFraction, panOffsetX, panOffsetY, centerLat, centerLng) {
                    detectTapGestures { tapOffset ->
                        val tapped = churches.firstOrNull { item ->
                            val screenPos = GebetaMapService.geoToScreen(
                                lat = item.church.latitude,
                                lng = item.church.longitude,
                                centerLat = centerLat,
                                centerLng = centerLng,
                                zoom = zoomLevel,
                                zoomFraction = zoomFraction,
                                screenWidth = size.width.toFloat(),
                                screenHeight = size.height.toFloat(),
                                offsetX = panOffsetX,
                                offsetY = panOffsetY
                            )
                            val dist = (screenPos - tapOffset).getDistance()
                            dist < 44f
                        }

                        if (tapped != null) {
                            haptic.vibrateClick()
                            onChurchSelected(tapped)
                        } else {
                            onMapClicked()
                        }
                    }
                }
        ) {
            val canvasW = size.width
            val canvasH = size.height

            // 1. Draw Base Map Background
            drawBaseMapBackground(canvasW, canvasH, currentStyle)

            // 2. Compute Visible Gebeta Map Tiles and Render
            val effectiveZoom = (zoomLevel + zoomFraction).coerceIn(4.0f, 17.0f)
            val renderZoom = zoomLevel.coerceIn(4, 17)
            val scale = 2.0.pow(effectiveZoom.toDouble()) * GebetaMapService.TILE_SIZE

            val centerWorldX = (centerLng + 180.0) / 360.0 * scale
            val centerLatRad = Math.toRadians(centerLat.coerceIn(-85.0, 85.0))
            val centerWorldY = (1.0 - kotlin.math.asinh(kotlin.math.tan(centerLatRad)) / PI) / 2.0 * scale

            val leftWorldX = centerWorldX - (canvasW / 2f) - panOffsetX
            val topWorldY = centerWorldY - (canvasH / 2f) - panOffsetY
            val rightWorldX = leftWorldX + canvasW
            val bottomWorldY = topWorldY + canvasH

            val tileSizeAtZoom = 256.0 * (2.0.pow(zoomFraction.toDouble()))
            val minTileX = floor(leftWorldX / scale * (1 shl renderZoom)).toInt().coerceAtLeast(0)
            val maxTileX = floor(rightWorldX / scale * (1 shl renderZoom)).toInt().coerceAtMost((1 shl renderZoom) - 1)
            val minTileY = floor(topWorldY / scale * (1 shl renderZoom)).toInt().coerceAtLeast(0)
            val maxTileY = floor(bottomWorldY / scale * (1 shl renderZoom)).toInt().coerceAtMost((1 shl renderZoom) - 1)

            // Render visible Gebeta tiles
            clipRect(0f, 0f, canvasW, canvasH) {
                for (tx in minTileX..maxTileX) {
                    for (ty in minTileY..maxTileY) {
                        val tileWorldX = (tx.toDouble() / (1 shl renderZoom)) * scale
                        val tileWorldY = (ty.toDouble() / (1 shl renderZoom)) * scale

                        val screenTileX = (canvasW / 2f) + (tileWorldX - centerWorldX).toFloat() + panOffsetX
                        val screenTileY = (canvasH / 2f) + (tileWorldY - centerWorldY).toFloat() + panOffsetY

                        val cacheKey = "${currentStyle.name}_${renderZoom}_${tx}_$ty"
                        val bitmap = tileBitmapMap[cacheKey]

                        if (bitmap != null) {
                            drawImage(
                                image = bitmap,
                                dstOffset = IntOffset(screenTileX.roundToInt(), screenTileY.roundToInt()),
                                dstSize = IntSize(tileSizeAtZoom.roundToInt() + 1, tileSizeAtZoom.roundToInt() + 1)
                            )
                        } else {
                            // Tile is missing or loading: Draw placeholder grid cell
                            drawTilePlaceholder(
                                offset = Offset(screenTileX, screenTileY),
                                size = tileSizeAtZoom.toFloat(),
                                style = currentStyle
                            )

                            // Launch coroutine to load tile from Gebeta service
                            coroutineScope.launch {
                                val loaded = gebetaService.getTileBitmap(renderZoom, tx, ty, currentStyle)
                                if (loaded != null) {
                                    tileBitmapMap[cacheKey] = loaded
                                }
                            }
                        }
                    }
                }
            }

            // 3. Draw Ethiopian Highway Network overlay (crisp cartography fallback)
            drawHighDefinitionRoadCorridors(
                canvasW, canvasH, centerLat, centerLng, zoomLevel, zoomFraction, panOffsetX, panOffsetY, currentStyle
            )

            // 4. Draw Active Route Polyline (if navigating to sanctuary)
            if (routeToChurch != null) {
                val polyPoints = routeResult?.polylinePoints
                val mode = routeResult?.travelMode ?: travelMode

                if (polyPoints != null && polyPoints.size >= 2) {
                    val screenPoints = polyPoints.map { pt ->
                        GebetaMapService.geoToScreen(
                            lat = pt.first,
                            lng = pt.second,
                            centerLat = centerLat,
                            centerLng = centerLng,
                            zoom = zoomLevel,
                            zoomFraction = zoomFraction,
                            screenWidth = canvasW,
                            screenHeight = canvasH,
                            offsetX = panOffsetX,
                            offsetY = panOffsetY
                        )
                    }
                    drawGebetaMultiPointRoutePolyline(
                        points = screenPoints,
                        travelMode = mode,
                        dashPhase = routeDashPhase
                    )
                } else {
                    val startPos = GebetaMapService.geoToScreen(
                        lat = userLat,
                        lng = userLng,
                        centerLat = centerLat,
                        centerLng = centerLng,
                        zoom = zoomLevel,
                        zoomFraction = zoomFraction,
                        screenWidth = canvasW,
                        screenHeight = canvasH,
                        offsetX = panOffsetX,
                        offsetY = panOffsetY
                    )

                    val endPos = GebetaMapService.geoToScreen(
                        lat = routeToChurch.church.latitude,
                        lng = routeToChurch.church.longitude,
                        centerLat = centerLat,
                        centerLng = centerLng,
                        zoom = zoomLevel,
                        zoomFraction = zoomFraction,
                        screenWidth = canvasW,
                        screenHeight = canvasH,
                        offsetX = panOffsetX,
                        offsetY = panOffsetY
                    )

                    drawGebetaRoutePolyline(startPos, endPos, mode, routeDashPhase)
                }
            }

            // 5. Draw Ethiopian Orthodox Church Pins with live pulses
            churches.forEach { item ->
                val isSelected = selectedChurch?.church?.id == item.church.id
                val markerPos = GebetaMapService.geoToScreen(
                    lat = item.church.latitude,
                    lng = item.church.longitude,
                    centerLat = centerLat,
                    centerLng = centerLng,
                    zoom = zoomLevel,
                    zoomFraction = zoomFraction,
                    screenWidth = canvasW,
                    screenHeight = canvasH,
                    offsetX = panOffsetX,
                    offsetY = panOffsetY
                )

                // Only draw if within screen bounds
                if (markerPos.x >= -60f && markerPos.x <= canvasW + 60f &&
                    markerPos.y >= -60f && markerPos.y <= canvasH + 60f
                ) {
                    drawGebetaChurchMarker(
                        pos = markerPos,
                        item = item,
                        isSelected = isSelected,
                        pulseRadius = pulseRadius,
                        pulseAlpha = pulseAlpha,
                        textMeasurer = textMeasurer,
                        zoomLevel = zoomLevel,
                        style = currentStyle
                    )
                }
            }

            // 6. Draw Pilgrim GPS Location Dot
            val pilgrimPos = GebetaMapService.geoToScreen(
                lat = userLat,
                lng = userLng,
                centerLat = centerLat,
                centerLng = centerLng,
                zoom = zoomLevel,
                zoomFraction = zoomFraction,
                screenWidth = canvasW,
                screenHeight = canvasH,
                offsetX = panOffsetX,
                offsetY = panOffsetY
            )
            drawPilgrimGpsMarker(pilgrimPos, pulseRadius, pulseAlpha)
        }

        // Floating Map Controls on Right Side (Pill shaped, 1px Border, Clean Touch Targets)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Layer / Tile Style Switcher
            GebetaFloatingButton(
                icon = {
                    Icon(
                        Icons.Default.Layers,
                        contentDescription = "Gebeta Map Layers",
                        modifier = Modifier.size(20.dp),
                        tint = CanvasBlack
                    )
                },
                onClick = {
                    haptic.vibrateClick()
                    showLayerDialog = true
                },
                testTag = "gebeta_layer_switch_button"
            )

            // Zoom In
            GebetaFloatingButton(
                icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Zoom In",
                        modifier = Modifier.size(22.dp),
                        tint = CanvasBlack
                    )
                },
                onClick = {
                    haptic.vibrateClick()
                    if (zoomLevel < 17) {
                        zoomLevel += 1
                        zoomFraction = 0f
                    }
                },
                testTag = "gebeta_zoom_in_button"
            )

            // Zoom Out
            GebetaFloatingButton(
                icon = {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "Zoom Out",
                        modifier = Modifier.size(22.dp),
                        tint = CanvasBlack
                    )
                },
                onClick = {
                    haptic.vibrateClick()
                    if (zoomLevel > 4) {
                        zoomLevel -= 1
                        zoomFraction = 0f
                    }
                },
                testTag = "gebeta_zoom_out_button"
            )

            // Fit Route in View (when route is active)
            if (routeResult != null || routeToChurch != null) {
                GebetaFloatingButton(
                    icon = {
                        Icon(
                            Icons.Default.AltRoute,
                            contentDescription = "Fit Gebeta Route",
                            modifier = Modifier.size(20.dp),
                            tint = getModeColor(routeResult?.travelMode ?: travelMode)
                        )
                    },
                    onClick = {
                        haptic.vibrateClick()
                        val destLat = routeResult?.destinationLat ?: routeToChurch?.church?.latitude ?: userLat
                        val destLng = routeResult?.destinationLng ?: routeToChurch?.church?.longitude ?: userLng
                        centerLat = (userLat + destLat) / 2.0
                        centerLng = (userLng + destLng) / 2.0
                        panOffsetX = 0f
                        panOffsetY = 0f
                        val maxSpan = max(abs(userLat - destLat), abs(userLng - destLng))
                        zoomLevel = when {
                            maxSpan > 6.0 -> 6
                            maxSpan > 3.0 -> 7
                            maxSpan > 1.5 -> 8
                            maxSpan > 0.8 -> 9
                            maxSpan > 0.3 -> 11
                            maxSpan > 0.1 -> 12
                            else -> 14
                        }
                        zoomFraction = 0f
                    },
                    testTag = "gebeta_fit_route_button"
                )
            }

            // Recenter on Pilgrim Location
            GebetaFloatingButton(
                icon = {
                    Icon(
                        Icons.Default.MyLocation,
                        contentDescription = "Recenter My Location",
                        modifier = Modifier.size(20.dp),
                        tint = SignalRed
                    )
                },
                onClick = {
                    haptic.vibrateClick()
                    centerLat = userLat
                    centerLng = userLng
                    panOffsetX = 0f
                    panOffsetY = 0f
                    zoomLevel = 11
                    zoomFraction = 0f
                },
                testTag = "gebeta_recenter_button"
            )
        }

        // Gebeta Maps Live Service Tag at Bottom Left
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
                    Icons.Default.Public,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp),
                    tint = CanvasBlack
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Gebeta Maps • ${churches.size} Sanctuaries",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = CanvasBlack
                    )
                )
            }
        }
    }

    // Gebeta Map Layers Selector Dialog
    if (showLayerDialog) {
        Dialog(onDismissRequest = { showLayerDialog = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Gebeta Tile Services",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Box(
                            modifier = Modifier
                                .background(SignalRed.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "SDK LIVE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SignalRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    GebetaMapStyle.values().forEach { style ->
                        val isSelected = currentStyle == style
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) SignalRed.copy(alpha = 0.08f) else Color.Transparent,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) SignalRed else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    haptic.vibrateClick()
                                    currentStyle = style
                                    showLayerDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = style.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) SignalRed else MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                    Text(
                                        text = style.description,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = SignalRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GebetaFloatingButton(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .size(46.dp)
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

private fun DrawScope.drawBaseMapBackground(width: Float, height: Float, style: GebetaMapStyle) {
    val bgColor = when (style) {
        GebetaMapStyle.GEBETA_STANDARD -> Color(0xFFF3F4F6)
        GebetaMapStyle.GEBETA_MONOCHROME -> Color(0xFFFAFAFA)
        GebetaMapStyle.GEBETA_TOPOGRAPHIC -> Color(0xFFE8ECEF)
        GebetaMapStyle.GEBETA_DARK -> Color(0xFF0D0D11)
    }
    drawRect(color = bgColor, size = Size(width, height))
}

private fun DrawScope.drawTilePlaceholder(offset: Offset, size: Float, style: GebetaMapStyle) {
    val tileBg = when (style) {
        GebetaMapStyle.GEBETA_STANDARD -> Color(0xFFF0F1F3)
        GebetaMapStyle.GEBETA_MONOCHROME -> Color(0xFFF8F8F9)
        GebetaMapStyle.GEBETA_TOPOGRAPHIC -> Color(0xFFE3E8EC)
        GebetaMapStyle.GEBETA_DARK -> Color(0xFF131318)
    }
    val gridColor = when (style) {
        GebetaMapStyle.GEBETA_DARK -> Color(0x15FFFFFF)
        else -> Color(0x10000000)
    }

    drawRect(color = tileBg, topLeft = offset, size = Size(size, size))
    drawRect(
        color = gridColor,
        topLeft = offset,
        size = Size(size, size),
        style = Stroke(width = 0.5f)
    )
}

/**
 * Arterial Ethiopian Highway network corridors rendered smoothly across the map.
 */
private fun DrawScope.drawHighDefinitionRoadCorridors(
    canvasW: Float,
    canvasH: Float,
    centerLat: Double,
    centerLng: Double,
    zoomLevel: Int,
    zoomFraction: Float,
    panOffsetX: Float,
    panOffsetY: Float,
    style: GebetaMapStyle
) {
    val roadColor = when (style) {
        GebetaMapStyle.GEBETA_DARK -> Color(0x40FFFFFF)
        else -> Color(0x50D4D4D8)
    }

    val corridors = listOf(
        listOf(Pair(9.03, 38.74), Pair(9.71, 38.86), Pair(11.59, 37.39), Pair(12.60, 37.46), Pair(14.12, 38.72)),
        listOf(Pair(9.03, 38.74), Pair(11.13, 39.63), Pair(12.03, 39.04), Pair(13.49, 39.47), Pair(14.12, 38.72)),
        listOf(Pair(12.60, 37.46), Pair(12.03, 39.04)),
        listOf(Pair(9.03, 38.74), Pair(9.31, 42.12)),
        listOf(Pair(9.03, 38.74), Pair(7.05, 38.47))
    )

    corridors.forEach { route ->
        val path = Path()
        route.forEachIndexed { index, (lat, lng) ->
            val pt = GebetaMapService.geoToScreen(
                lat = lat,
                lng = lng,
                centerLat = centerLat,
                centerLng = centerLng,
                zoom = zoomLevel,
                zoomFraction = zoomFraction,
                screenWidth = canvasW,
                screenHeight = canvasH,
                offsetX = panOffsetX,
                offsetY = panOffsetY
            )
            if (index == 0) path.moveTo(pt.x, pt.y) else path.lineTo(pt.x, pt.y)
        }
        drawPath(
            path = path,
            color = roadColor,
            style = Stroke(width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

private fun getModeColor(mode: GebetaTravelMode): Color {
    return when (mode) {
        GebetaTravelMode.DRIVING -> SignalRed
        GebetaTravelMode.WALKING -> Color(0xFF10B981) // Emerald pilgrimage green
        GebetaTravelMode.TRANSIT -> Color(0xFFD97706) // Warm bus transit amber
    }
}

private fun DrawScope.drawGebetaMultiPointRoutePolyline(
    points: List<Offset>,
    travelMode: GebetaTravelMode,
    dashPhase: Float
) {
    if (points.size < 2) return

    val routeColor = getModeColor(travelMode)
    val routePath = Path().apply {
        moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) {
            val p0 = points[i - 1]
            val p1 = points[i]
            // Draw smooth curve segments
            val midX = (p0.x + p1.x) / 2f
            val midY = (p0.y + p1.y) / 2f
            quadraticTo(p0.x, p0.y, midX, midY)
        }
        lineTo(points.last().x, points.last().y)
    }

    // 1. Wide ambient glow background
    drawPath(
        path = routePath,
        color = routeColor.copy(alpha = 0.22f),
        style = Stroke(width = 12f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // 2. Crisp solid casing outline
    drawPath(
        path = routePath,
        color = Color.White.copy(alpha = 0.9f),
        style = Stroke(width = 6.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // 3. Main Route Core
    drawPath(
        path = routePath,
        color = routeColor,
        style = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // 4. Animated dash flow travelling along the route
    drawPath(
        path = routePath,
        color = Color.White.copy(alpha = 0.85f),
        style = Stroke(
            width = 2.2f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(14f, 10f),
                phase = dashPhase
            )
        )
    )

    // 5. Waypoint nodes
    for (i in 1 until points.size - 1) {
        val pt = points[i]
        drawCircle(
            color = Color.White,
            radius = 3.5f,
            center = pt
        )
        drawCircle(
            color = routeColor,
            radius = 2.2f,
            center = pt
        )
    }
}

private fun DrawScope.drawGebetaRoutePolyline(
    start: Offset,
    end: Offset,
    travelMode: GebetaTravelMode,
    dashPhase: Float
) {
    val routeColor = getModeColor(travelMode)
    val mid1 = Offset(start.x + (end.x - start.x) * 0.35f - 10f, start.y + (end.y - start.y) * 0.35f + 6f)
    val mid2 = Offset(start.x + (end.x - start.x) * 0.65f + 10f, start.y + (end.y - start.y) * 0.65f - 6f)

    val routePath = Path().apply {
        moveTo(start.x, start.y)
        cubicTo(mid1.x, mid1.y, mid2.x, mid2.y, end.x, end.y)
    }

    // Outer glow
    drawPath(
        path = routePath,
        color = routeColor.copy(alpha = 0.22f),
        style = Stroke(width = 11f, cap = StrokeCap.Round)
    )

    // Solid casing
    drawPath(
        path = routePath,
        color = Color.White,
        style = Stroke(width = 6f, cap = StrokeCap.Round)
    )

    // Route line with animated dash effect
    drawPath(
        path = routePath,
        color = routeColor,
        style = Stroke(
            width = 3.8f,
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(14f, 8f),
                phase = dashPhase
            )
        )
    )
}

/**
 * Crisp Ethiopian Orthodox Church Pin on Gebeta Maps.
 */
private fun DrawScope.drawGebetaChurchMarker(
    pos: Offset,
    item: ChurchWithDistance,
    isSelected: Boolean,
    pulseRadius: Float,
    pulseAlpha: Float,
    textMeasurer: TextMeasurer,
    zoomLevel: Int,
    style: GebetaMapStyle
) {
    val church = item.church
    val isLive = item.hasActiveGubae

    // Live Gubae pulsing radar ring
    if (isLive) {
        drawCircle(
            color = SignalRed.copy(alpha = pulseAlpha * 0.6f),
            radius = pulseRadius + 18f,
            center = pos
        )
    }

    val pinHeight = if (isSelected) 36f else 28f
    val pinWidth = pinHeight * 0.72f
    val pinTip = pos
    val pinCenter = Offset(pos.x, pos.y - pinHeight * 0.65f)
    val headRadius = pinWidth / 2f

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

    // Shadow
    drawCircle(
        color = Color.Black.copy(alpha = 0.25f),
        radius = headRadius * 0.75f,
        center = Offset(pos.x, pos.y + 2f)
    )

    // Pin Body
    val pinColor = if (isSelected) SignalRed else {
        when (style) {
            GebetaMapStyle.GEBETA_DARK -> Color(0xFFFAFAFA)
            else -> Color(0xFF18181B)
        }
    }
    drawPath(path = pinPath, color = pinColor, style = Fill)
    drawPath(path = pinPath, color = if (isSelected) Color(0xFFB71C1C) else Color(0xFF09090B), style = Stroke(width = 1f))

    // Inner Core
    val coreRadius = headRadius * 0.58f
    drawCircle(color = Color.White, radius = coreRadius, center = pinCenter)

    // Central Ethiopian Cross
    val crossColor = if (isSelected) SignalRed else Color(0xFF18181B)
    val crossArm = coreRadius * 0.65f
    val crossStroke = 1.8f

    drawLine(
        color = crossColor,
        start = Offset(pinCenter.x, pinCenter.y - crossArm),
        end = Offset(pinCenter.x, pinCenter.y + crossArm),
        strokeWidth = crossStroke,
        cap = StrokeCap.Round
    )
    drawLine(
        color = crossColor,
        start = Offset(pinCenter.x - crossArm * 0.75f, pinCenter.y - crossArm * 0.2f),
        end = Offset(pinCenter.x + crossArm * 0.75f, pinCenter.y - crossArm * 0.2f),
        strokeWidth = crossStroke,
        cap = StrokeCap.Round
    )

    // Sanctuary Label Callout
    if (isSelected || zoomLevel >= 11) {
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

        drawRoundRect(
            color = badgeBg,
            topLeft = Offset(badgeX - 8f, badgeY - 3f),
            size = Size(textLayout.size.width + 16f, textLayout.size.height + 6f),
            cornerRadius = CornerRadius(12f, 12f)
        )
        drawRoundRect(
            color = if (isSelected) Color(0xFFB71C1C) else BorderLight,
            topLeft = Offset(badgeX - 8f, badgeY - 3f),
            size = Size(textLayout.size.width + 16f, textLayout.size.height + 6f),
            cornerRadius = CornerRadius(12f, 12f),
            style = Stroke(width = 1f)
        )

        drawText(textLayoutResult = textLayout, topLeft = Offset(badgeX, badgeY))
    }
}

private fun DrawScope.drawPilgrimGpsMarker(pos: Offset, pulseRadius: Float, pulseAlpha: Float) {
    // Pulse
    drawCircle(
        color = SignalRed.copy(alpha = pulseAlpha * 0.5f),
        radius = pulseRadius + 12f,
        center = pos
    )
    // White ring
    drawCircle(color = Color.White, radius = 7.5f, center = pos)
    drawCircle(color = BorderLight, radius = 7.5f, center = pos, style = Stroke(width = 1f))
    // Signal Red core
    drawCircle(color = SignalRed, radius = 5f, center = pos)
}
