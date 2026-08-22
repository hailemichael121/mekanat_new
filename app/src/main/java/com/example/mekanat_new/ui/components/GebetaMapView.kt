package com.example.mekanat_new.ui.components

import android.content.Context
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
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
import com.example.mekanat_new.ui.theme.BrandEmber
import com.example.mekanat_new.ui.theme.BrandInk
import com.example.mekanat_new.ui.theme.CanvasBlack
import com.example.mekanat_new.ui.theme.CanvasLight
import com.example.mekanat_new.ui.theme.CrimsonPulse
import com.example.mekanat_new.ui.theme.DarkPin
import com.example.mekanat_new.ui.theme.LightPin
import com.example.mekanat_new.ui.theme.SignalRed
import com.example.mekanat_new.ui.theme.WayfindingTeal
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
    var zoomLevel by remember { mutableIntStateOf(15) } // High street/neighborhood zoom level on initial opening
    var zoomFraction by remember { mutableFloatStateOf(0.2f) }
    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var panOffsetY by remember { mutableFloatStateOf(0f) }
    var mapRotation by remember { mutableFloatStateOf(0f) } // Map rotation bearing in degrees

    val isLightMode = MaterialTheme.colorScheme.background == CanvasLight
    var currentStyle by remember(isLightMode) {
        mutableStateOf(if (isLightMode) GebetaMapStyle.GEBETA_STANDARD else GebetaMapStyle.GEBETA_DARK)
    }
    var showLayerDialog by remember { mutableStateOf(false) }

    // One-Time Strict Spotlight Tour Persistence
    val mapPrefs = remember(context) {
        context.getSharedPreferences("mekanat_map_prefs", Context.MODE_PRIVATE)
    }
    var showTutorialOverlay by remember {
        mutableStateOf(!mapPrefs.getBoolean("has_completed_spotlight_tour_v1", false))
    }

    // Dynamic UI component bounds for Spotlight Tour cutouts
    var rootOffset by remember { mutableStateOf(Offset.Zero) }
    var mapSize by remember { mutableStateOf(Size.Zero) }
    var compassBounds by remember { mutableStateOf<Rect?>(null) }
    var zoomBounds by remember { mutableStateOf<Rect?>(null) }
    var targetBounds by remember { mutableStateOf<Rect?>(null) }

    fun recenterAndZoomToUserLocation() {
        haptic.vibrateClick()
        centerLat = userLat
        centerLng = userLng
        panOffsetX = 0f
        panOffsetY = 0f
        zoomLevel = 16 // High detailed street/sanctuary zoom level
        zoomFraction = 0.35f
        mapRotation = 0f // Align straight North
    }

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
            zoomLevel = max(zoomLevel, 14)
            panOffsetX = 0f
            panOffsetY = 0f
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { coords ->
                rootOffset = coords.positionInRoot()
                mapSize = Size(coords.size.width.toFloat(), coords.size.height.toFloat())
            }
            .testTag("gebeta_map_view")
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, rotation ->
                        // Smooth pinch-to-zoom and two-finger rotation
                        mapRotation = (mapRotation + rotation) % 360f

                        val totalZoom = zoomLevel + zoomFraction + (zoom - 1.0f) * 1.5f
                        val clampedZoom = totalZoom.coerceIn(4.0f, 18.0f)
                        zoomLevel = clampedZoom.toInt()
                        zoomFraction = clampedZoom - zoomLevel

                        panOffsetX += pan.x
                        panOffsetY += pan.y
                    }
                }
                .pointerInput(churches, zoomLevel, zoomFraction, panOffsetX, panOffsetY, centerLat, centerLng, mapRotation) {
                    detectTapGestures { tapOffset ->
                        val rad = Math.toRadians(mapRotation.toDouble())
                        val cosA = kotlin.math.cos(rad).toFloat()
                        val sinA = kotlin.math.sin(rad).toFloat()
                        val cx = size.width / 2f
                        val cy = size.height / 2f

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
                            val dx = screenPos.x - cx
                            val dy = screenPos.y - cy
                            val rotatedX = cx + (dx * cosA - dy * sinA)
                            val rotatedY = cy + (dx * sinA + dy * cosA)
                            val visualPos = Offset(rotatedX, rotatedY)
                            val dist = (visualPos - tapOffset).getDistance()
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
            val diag = kotlin.math.hypot(canvasW.toDouble(), canvasH.toDouble()).toFloat()

            // 0. Fill the underlying unrotated canvas with the base map background color
            drawBaseMapBackground(canvasW, canvasH, diag, currentStyle)

            // Rotate entire map canvas around center according to mapRotation
            rotate(degrees = mapRotation, pivot = Offset(canvasW / 2f, canvasH / 2f)) {
                // 1. Draw Base Map Background covering entire rotated diagonal space
                drawBaseMapBackground(canvasW, canvasH, diag, currentStyle)

                // 2. Compute Visible Gebeta Map Tiles and Render across rotated radius
                val effectiveZoom = (zoomLevel + zoomFraction).coerceIn(4.0f, 18.0f)
                val renderZoom = zoomLevel.coerceIn(4, 18)
                val scale = 2.0.pow(effectiveZoom.toDouble()) * GebetaMapService.TILE_SIZE

                val centerWorldX = (centerLng + 180.0) / 360.0 * scale
                val centerLatRad = Math.toRadians(centerLat.coerceIn(-85.0, 85.0))
                val centerWorldY = (1.0 - kotlin.math.asinh(kotlin.math.tan(centerLatRad)) / PI) / 2.0 * scale

                // Expand tile rendering bounds to full rotated diagonal radius (+ extra buffer) to avoid edge clipping
                val renderRadius = (diag / 2.0) + 256.0
                val leftWorldX = centerWorldX - renderRadius - panOffsetX
                val topWorldY = centerWorldY - renderRadius - panOffsetY
                val rightWorldX = centerWorldX + renderRadius - panOffsetX
                val bottomWorldY = centerWorldY + renderRadius - panOffsetY

                val tileSizeAtZoom = 256.0 * (2.0.pow(zoomFraction.toDouble()))
                val minTileX = floor(leftWorldX / scale * (1 shl renderZoom)).toInt().coerceAtLeast(0)
                val maxTileX = floor(rightWorldX / scale * (1 shl renderZoom)).toInt().coerceAtMost((1 shl renderZoom) - 1)
                val minTileY = floor(topWorldY / scale * (1 shl renderZoom)).toInt().coerceAtLeast(0)
                val maxTileY = floor(bottomWorldY / scale * (1 shl renderZoom)).toInt().coerceAtMost((1 shl renderZoom) - 1)

                // Render visible Gebeta tiles seamlessly
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

                // 3. Draw Ethiopian Highway Network overlay (crisp cartography fallback)
                drawHighDefinitionRoadCorridors(
                    canvasW, canvasH, centerLat, centerLng, zoomLevel, zoomFraction, panOffsetX, panOffsetY, currentStyle
                )

                // 4. Draw Surrounding Place Names, Building Footprints & Location Density
                drawSurroundingPlaceDataAndBuildings(
                    canvasW, canvasH, centerLat, centerLng, zoomLevel, zoomFraction, panOffsetX, panOffsetY, currentStyle, textMeasurer
                )

                // 5. Draw Active Route Polyline (if navigating to sanctuary)
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

                // 6. Draw Ethiopian Orthodox Church Pins with live pulses
                val cullRadius = (diag / 2f) + 80f
                val cX = canvasW / 2f
                val cY = canvasH / 2f
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

                    // Safe culling covering entire rotated area
                    if (abs(markerPos.x - cX) <= cullRadius && abs(markerPos.y - cY) <= cullRadius) {
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

                // 7. Draw Pilgrim GPS Location Dot
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
        }

        // Top Status / Route Calculation Loading Pill
        AnimatedVisibility(
            visible = isCalculatingRoute,
            enter = fadeIn(tween(200)) + slideInVertically(tween(250)) { -it },
            exit = fadeOut(tween(150)) + slideOutVertically(tween(200)) { -it },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 70.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = Color(0xFF1E2228).copy(alpha = 0.94f),
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandEmber.copy(alpha = 0.6f)),
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = BrandEmber,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Calculating Gebeta Navigation Corridor...",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        // Floating Map Controls on Right Side (Pill shaped, 1px Border, Clean Touch Targets)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.End
        ) {
            // North Compass Button (Rotate map by holding/dragging, slide thumb up/down to zoom, tap to reset north)
            NorthCompassFloatingButton(
                mapRotation = mapRotation,
                onResetNorth = {
                    mapRotation = 0f
                },
                onRotateDelta = { delta ->
                    mapRotation = (mapRotation + delta) % 360f
                },
                onZoomDelta = { zDelta ->
                    val totalZoom = zoomLevel + zoomFraction + zDelta
                    val clampedZoom = totalZoom.coerceIn(4.0f, 18.0f)
                    zoomLevel = clampedZoom.toInt()
                    zoomFraction = clampedZoom - zoomLevel
                },
                modifier = Modifier.onGloballyPositioned { coords ->
                    val pos = coords.positionInRoot() - rootOffset
                    compassBounds = Rect(pos.x, pos.y, pos.x + coords.size.width, pos.y + coords.size.height)
                }
            )

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

            // Unified Rounded Zoom Pill (+ and - merged in one sleek capsule)
            GebetaUnifiedZoomPill(
                onZoomIn = {
                    if (zoomLevel < 18) {
                        zoomLevel += 1
                        zoomFraction = 0f
                    }
                },
                onZoomOut = {
                    if (zoomLevel > 4) {
                        zoomLevel -= 1
                        zoomFraction = 0f
                    }
                },
                modifier = Modifier.onGloballyPositioned { coords ->
                    val pos = coords.positionInRoot() - rootOffset
                    zoomBounds = Rect(pos.x, pos.y, pos.x + coords.size.width, pos.y + coords.size.height)
                }
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

            // Recenter on Pilgrim Location (Instantly jumps, straightens North, and zooms into detailed street level)
            GebetaFloatingButton(
                icon = {
                    Icon(
                        Icons.Default.MyLocation,
                        contentDescription = "Recenter and Zoom My Location",
                        modifier = Modifier.size(20.dp),
                        tint = SignalRed
                    )
                },
                onClick = {
                    recenterAndZoomToUserLocation()
                },
                testTag = "gebeta_recenter_button",
                modifier = Modifier.onGloballyPositioned { coords ->
                    val pos = coords.positionInRoot() - rootOffset
                    targetBounds = Rect(pos.x, pos.y, pos.x + coords.size.width, pos.y + coords.size.height)
                }
            )

            // Spotlight Tour Replay / Help Button
            GebetaFloatingButton(
                icon = {
                    Icon(
                        Icons.Default.HelpOutline,
                        contentDescription = "Replay Spotlight Tour",
                        modifier = Modifier.size(19.dp),
                        tint = CanvasBlack.copy(alpha = 0.75f)
                    )
                },
                onClick = {
                    haptic.vibrateClick()
                    showTutorialOverlay = true
                },
                testTag = "gebeta_tour_guide_button"
            )
        }

        // Genuine Spotlight Tour & Coach Mark Overlay for First-Time Users
        GebetaInteractiveMapSpotlightTourOverlay(
            visible = showTutorialOverlay && routeToChurch == null,
            targetBounds = targetBounds,
            compassBounds = compassBounds,
            zoomBounds = zoomBounds,
            mapSize = mapSize,
            onTargetClicked = {
                recenterAndZoomToUserLocation()
            },
            onDismiss = {
                haptic.vibrateSubtle()
                mapPrefs.edit().putBoolean("has_completed_spotlight_tour_v1", true).apply()
                showTutorialOverlay = false
            }
        )
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
private fun NorthCompassFloatingButton(
    mapRotation: Float,
    onResetNorth: () -> Unit,
    onRotateDelta: (Float) -> Unit,
    onZoomDelta: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var lastAngle by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), CircleShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        haptic.vibrateClick()
                        onResetNorth()
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val centerX = size.width / 2f
                        val centerY = size.height / 2f
                        lastAngle = Math.toDegrees(
                            kotlin.math.atan2((offset.y - centerY).toDouble(), (offset.x - centerX).toDouble())
                        ).toFloat()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        // 1. Moving thumb up/down zooms in/out
                        onZoomDelta(-dragAmount.y * 0.015f)

                        // 2. Rotating thumb around compass rotates the map
                        val currentPos = change.position
                        val centerX = size.width / 2f
                        val centerY = size.height / 2f
                        val currentAngle = Math.toDegrees(
                            kotlin.math.atan2((currentPos.y - centerY).toDouble(), (currentPos.x - centerX).toDouble())
                        ).toFloat()
                        var deltaAngle = currentAngle - lastAngle
                        if (deltaAngle > 180f) deltaAngle -= 360f
                        if (deltaAngle < -180f) deltaAngle += 360f
                        lastAngle = currentAngle
                        onRotateDelta(deltaAngle)
                    }
                )
            }
            .testTag("gebeta_north_compass_button"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(32.dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val needleLength = size.width * 0.42f
            val needleWidth = 5.dp.toPx()

            rotate(degrees = -mapRotation, pivot = center) {
                // North needle (Red tip with crisp direction)
                val northPath = Path().apply {
                    moveTo(center.x, center.y - needleLength)
                    lineTo(center.x + needleWidth, center.y)
                    lineTo(center.x - needleWidth, center.y)
                    close()
                }
                drawPath(path = northPath, color = SignalRed)

                // South needle (Silver / Muted tip)
                val southPath = Path().apply {
                    moveTo(center.x, center.y + needleLength)
                    lineTo(center.x + needleWidth, center.y)
                    lineTo(center.x - needleWidth, center.y)
                    close()
                }
                drawPath(path = southPath, color = Color(0xFFCBD5E1))

                // Center pivot cap
                drawCircle(color = Color.White, radius = 3.5.dp.toPx(), center = center)
                drawCircle(color = Color(0xFF1E293B), radius = 2.dp.toPx(), center = center)
            }
        }
    }
}

@Composable
private fun GebetaFloatingButton(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), CircleShape)
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

private data class MapLandmark(
    val name: String,
    val lat: Double,
    val lng: Double,
    val type: String // "district", "park", "square", "corridor", "building"
)

/**
 * Draws surrounding place names, building footprints, squares, and neighborhood districts
 * dynamically based on zoom and viewport coordinates.
 */
private fun DrawScope.drawSurroundingPlaceDataAndBuildings(
    canvasW: Float,
    canvasH: Float,
    centerLat: Double,
    centerLng: Double,
    zoomLevel: Int,
    zoomFraction: Float,
    panOffsetX: Float,
    panOffsetY: Float,
    style: GebetaMapStyle,
    textMeasurer: TextMeasurer
) {
    if (zoomLevel < 11) return

    val isDark = style == GebetaMapStyle.GEBETA_DARK
    val effectiveZoom = zoomLevel + zoomFraction

    // Real-time Ethiopian Capital & Regional City Core Landmark Data
    val landmarks = listOf(
        MapLandmark("Piazza", 9.0353, 38.7523, "district"),
        MapLandmark("Kazanchis", 9.0192, 38.7661, "district"),
        MapLandmark("Bole", 8.9950, 38.7880, "district"),
        MapLandmark("Arat Kilo", 9.0345, 38.7635, "district"),
        MapLandmark("Sidist Kilo", 9.0478, 38.7620, "district"),
        MapLandmark("Mercato", 9.0310, 38.7380, "district"),
        MapLandmark("Meskel Square", 9.0108, 38.7616, "square"),
        MapLandmark("Mexico Square", 9.0118, 38.7438, "square"),
        MapLandmark("Sarbet", 8.9980, 38.7360, "district"),
        MapLandmark("Friendship Park", 9.0220, 38.7600, "park"),
        MapLandmark("Unity Park", 9.0305, 38.7640, "park"),
        MapLandmark("National Palace", 9.0180, 38.7625, "building"),
        MapLandmark("Entoto Park", 9.0850, 38.7620, "park"),
        MapLandmark("Churchill Ave", 9.0250, 38.7500, "corridor"),
        MapLandmark("Africa Ave", 9.0050, 38.7750, "corridor")
    )

    // 1. Draw stylized building footprints & urban blocks when zoomed in
    if (zoomLevel >= 13) {
        val blockColor = if (isDark) Color(0x1F384556) else Color(0x22CBD5E1)

        // Draw building blocks around city centers
        val blockCoords = listOf(
            Pair(9.035, 38.752), Pair(9.033, 38.755), Pair(9.031, 38.749),
            Pair(9.020, 38.765), Pair(9.018, 38.768), Pair(9.012, 38.760),
            Pair(8.996, 38.786), Pair(8.993, 38.790), Pair(9.046, 38.763),
            Pair(9.030, 38.737), Pair(9.011, 38.745), Pair(8.999, 38.738)
        )

        blockCoords.forEach { (bLat, bLng) ->
            val bScreen = GebetaMapService.geoToScreen(
                lat = bLat,
                lng = bLng,
                centerLat = centerLat,
                centerLng = centerLng,
                zoom = zoomLevel,
                zoomFraction = zoomFraction,
                screenWidth = canvasW,
                screenHeight = canvasH,
                offsetX = panOffsetX,
                offsetY = panOffsetY
            )
            val blockSize = (18f * (1f + (effectiveZoom - 13f) * 0.4f)).coerceIn(12f, 48f)

            if (bScreen.x in -50f..(canvasW + 50f) && bScreen.y in -50f..(canvasH + 50f)) {
                drawRoundRect(
                    color = blockColor,
                    topLeft = Offset(bScreen.x - blockSize / 2f, bScreen.y - blockSize / 2f),
                    size = Size(blockSize, blockSize * 0.75f),
                    cornerRadius = CornerRadius(3f, 3f)
                )
            }
        }
    }

    // 2. Draw surrounding place & district labels
    landmarks.forEach { lmk ->
        val screenPos = GebetaMapService.geoToScreen(
            lat = lmk.lat,
            lng = lmk.lng,
            centerLat = centerLat,
            centerLng = centerLng,
            zoom = zoomLevel,
            zoomFraction = zoomFraction,
            screenWidth = canvasW,
            screenHeight = canvasH,
            offsetX = panOffsetX,
            offsetY = panOffsetY
        )

        if (screenPos.x in -60f..(canvasW + 60f) && screenPos.y in -60f..(canvasH + 60f)) {
            val labelColor = when (lmk.type) {
                "park" -> if (isDark) Color(0xFF6EE7B7) else Color(0xFF047857)
                "square" -> if (isDark) Color(0xFFFDE68A) else Color(0xFFB45309)
                "corridor" -> if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                else -> if (isDark) Color(0xFFE2E8F0) else Color(0xFF334155)
            }

            val textLayout = textMeasurer.measure(
                text = lmk.name,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = if (lmk.type == "district") 10.5.sp else 9.sp,
                    fontWeight = if (lmk.type == "district" || lmk.type == "square") FontWeight.Bold else FontWeight.Medium,
                    color = labelColor
                )
            )

            // Subtle dot indicator
            drawCircle(
                color = labelColor.copy(alpha = 0.75f),
                radius = 2.5f,
                center = screenPos
            )

            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(screenPos.x + 5f, screenPos.y - textLayout.size.height / 2f)
            )
        }
    }
}

private fun DrawScope.drawBaseMapBackground(canvasW: Float, canvasH: Float, diag: Float, style: GebetaMapStyle) {
    val bgColor = when (style) {
        GebetaMapStyle.GEBETA_STANDARD -> Color(0xFF121214)
        GebetaMapStyle.GEBETA_MONOCHROME -> Color(0xFF121214)
        GebetaMapStyle.GEBETA_TOPOGRAPHIC -> Color(0xFF161619)
        GebetaMapStyle.GEBETA_DARK -> Color(0xFF121214)
    }
    val boxSize = diag * 3.5f
    val left = (canvasW - boxSize) / 2f
    val top = (canvasH - boxSize) / 2f
    drawRect(color = bgColor, topLeft = Offset(left, top), size = Size(boxSize, boxSize))
}

private fun DrawScope.drawTilePlaceholder(offset: Offset, size: Float, style: GebetaMapStyle) {
    val tileBg = when (style) {
        GebetaMapStyle.GEBETA_STANDARD -> Color(0xFF22272B)
        GebetaMapStyle.GEBETA_MONOCHROME -> Color(0xFF262C32)
        GebetaMapStyle.GEBETA_TOPOGRAPHIC -> Color(0xFF20262C)
        GebetaMapStyle.GEBETA_DARK -> Color(0xFF22272B)
    }
    val gridColor = Color(0x12FFFFFF)

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
    return WayfindingTeal
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
 * Dynamic Ethiopian Orthodox Church Marker Categories with tailored visual styling.
 */
enum class ChurchMarkerCategory(
    val title: String,
    val titleAmharic: String,
    val primaryColor: Color,
    val darkPrimaryColor: Color,
    val accentColor: Color,
    val strokeColor: Color,
    val badgeBg: Color,
    val badgeText: Color,
    val iconType: String
) {
    ROCK_HEWN(
        title = "Rock-Hewn",
        titleAmharic = "ፍልፍል",
        primaryColor = Color(0xFFC2410C), // Warm Earth Terracotta / Sandstone
        darkPrimaryColor = Color(0xFFEA580C),
        accentColor = Color(0xFFFED7AA),
        strokeColor = Color(0xFF7C2D12),
        badgeBg = Color(0xFF7C2D12),
        badgeText = Color(0xFFFFEDD5),
        iconType = "rock_hewn"
    ),
    MONASTERY(
        title = "Monastery",
        titleAmharic = "ገዳም",
        primaryColor = Color(0xFF7C3AED), // Royal Mountain Amethyst
        darkPrimaryColor = Color(0xFF8B5CF6),
        accentColor = Color(0xFFDDD6FE),
        strokeColor = Color(0xFF4C1D95),
        badgeBg = Color(0xFF4C1D95),
        badgeText = Color(0xFFF3E8FF),
        iconType = "monastery"
    ),
    CATHEDRAL(
        title = "Cathedral",
        titleAmharic = "ካቴድራል",
        primaryColor = Color(0xFFD97706), // Imperial Patriarchal Gold
        darkPrimaryColor = Color(0xFFF59E0B),
        accentColor = Color(0xFFFEF3C7),
        strokeColor = Color(0xFF78350F),
        badgeBg = Color(0xFF78350F),
        badgeText = Color(0xFFFFFBEB),
        iconType = "cathedral"
    ),
    PARISH(
        title = "Parish",
        titleAmharic = "ደብር",
        primaryColor = Color(0xFF059669), // Ethiopian Sanctuary Pine Emerald
        darkPrimaryColor = Color(0xFF10B981),
        accentColor = Color(0xFFA7F3D0),
        strokeColor = Color(0xFF064E3B),
        badgeBg = Color(0xFF064E3B),
        badgeText = Color(0xFFECFDF5),
        iconType = "parish"
    ),
    LIVE_GUBAE(
        title = "Live Gubae",
        titleAmharic = "ጉባኤ",
        primaryColor = SignalRed, // Radiant Broadcast Crimson
        darkPrimaryColor = Color(0xFFFF4D4D),
        accentColor = Color(0xFFFFCDD2),
        strokeColor = Color(0xFFB71C1C),
        badgeBg = Color(0xFFB71C1C),
        badgeText = Color.White,
        iconType = "live_gubae"
    )
}

/**
 * Resolves ChurchMarkerCategory from entity type and live gathering state.
 */
fun getChurchMarkerCategory(churchType: String, hasActiveGubae: Boolean): ChurchMarkerCategory {
    if (hasActiveGubae) return ChurchMarkerCategory.LIVE_GUBAE
    return when (churchType.uppercase().trim()) {
        "ROCK_HEWN", "ROCK_CUT", "CAVE" -> ChurchMarkerCategory.ROCK_HEWN
        "MONASTERY", "GEDAM" -> ChurchMarkerCategory.MONASTERY
        "CATHEDRAL", "BASILICA" -> ChurchMarkerCategory.CATHEDRAL
        else -> ChurchMarkerCategory.PARISH
    }
}

/**
 * Crisp Ethiopian Orthodox Church Pin on Gebeta Maps with Category-Aware Dynamic Markers & Custom Emblems.
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
    val category = getChurchMarkerCategory(church.churchType, isLive)
    val isDark = style == GebetaMapStyle.GEBETA_DARK

    // 1. Radar Pulse / Halo Aura
    if (isLive) {
        // Multi-ring glowing broadcast wave
        drawCircle(
            color = SignalRed.copy(alpha = pulseAlpha * 0.7f),
            radius = pulseRadius + 22f,
            center = pos
        )
        drawCircle(
            color = SignalRed.copy(alpha = pulseAlpha * 0.35f),
            radius = pulseRadius + 34f,
            center = pos
        )
    } else if (isSelected) {
        drawCircle(
            color = category.primaryColor.copy(alpha = pulseAlpha * 0.45f),
            radius = pulseRadius + 16f,
            center = pos
        )
    }

    // 2. Pin Dimensions & Teardrop Geometry
    val pinHeight = if (isSelected) 40f else 30f
    val pinWidth = pinHeight * 0.74f
    val pinTip = pos
    val pinCenter = Offset(pos.x, pos.y - pinHeight * 0.65f)
    val headRadius = pinWidth / 2f

    val pinPath = Path().apply {
        moveTo(pinTip.x, pinTip.y)
        cubicTo(
            pinTip.x - headRadius * 0.92f, pinTip.y - pinHeight * 0.45f,
            pinCenter.x - headRadius, pinCenter.y + headRadius * 0.35f,
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
            pinCenter.x + headRadius, pinCenter.y + headRadius * 0.35f,
            pinTip.x + headRadius * 0.92f, pinTip.y - pinHeight * 0.45f,
            pinTip.x, pinTip.y
        )
        close()
    }

    // 3. Ground Drop Shadow
    drawCircle(
        color = Color.Black.copy(alpha = 0.28f),
        radius = headRadius * 0.8f,
        center = Offset(pos.x, pos.y + 2.5f)
    )

    // 4. Pin Body Fill & Border (Brand-aligned: Live=Crimson, Selected=Ember, Default=Ink neutral)
    val pinFillColor = if (isSelected) {
        BrandEmber
    } else if (isLive || category == ChurchMarkerCategory.LIVE_GUBAE) {
        CrimsonPulse
    } else if (isDark) {
        DarkPin
    } else {
        LightPin
    }

    val pinStrokeColor = if (isSelected) {
        Color.White
    } else if (isLive || category == ChurchMarkerCategory.LIVE_GUBAE) {
        Color.White
    } else if (isDark) {
        Color(0xFF2D2A24)
    } else {
        Color(0xFFE2DED4)
    }

    drawPath(path = pinPath, color = pinFillColor, style = Fill)
    drawPath(path = pinPath, color = pinStrokeColor, style = Stroke(width = if (isSelected) 2.0f else 1.2f))

    // 5. Inner Emblem Core
    val coreRadius = headRadius * 0.62f
    val innerCoreBg = if (isSelected || isLive) Color.White else if (isDark) Color(0xFF181613) else Color(0xFFFFFFFF)
    drawCircle(color = innerCoreBg, radius = coreRadius, center = pinCenter)
    drawCircle(
        color = pinStrokeColor.copy(alpha = 0.35f),
        radius = coreRadius,
        center = pinCenter,
        style = Stroke(width = 0.8f)
    )

    // 6. Distinct Category Vector Emblem Drawn on Inner Core
    val emblemColor = if (isSelected) BrandEmber else if (isLive) CrimsonPulse else if (isDark) DarkPin else LightPin
    val emblemStroke = if (isSelected) 1.9f else 1.5f

    when (category.iconType) {
        "rock_hewn" -> {
            // Lalibela Monolithic Carved Cross with stepped arch base
            val arm = coreRadius * 0.62f
            // Vertical bar
            drawLine(
                color = emblemColor,
                start = Offset(pinCenter.x, pinCenter.y - arm),
                end = Offset(pinCenter.x, pinCenter.y + arm * 0.6f),
                strokeWidth = emblemStroke,
                cap = StrokeCap.Round
            )
            // Horizontal bar
            drawLine(
                color = emblemColor,
                start = Offset(pinCenter.x - arm * 0.75f, pinCenter.y - arm * 0.15f),
                end = Offset(pinCenter.x + arm * 0.75f, pinCenter.y - arm * 0.15f),
                strokeWidth = emblemStroke,
                cap = StrokeCap.Round
            )
            // Carved stone portal arch below cross
            val archPath = Path().apply {
                moveTo(pinCenter.x - arm * 0.55f, pinCenter.y + arm * 0.75f)
                lineTo(pinCenter.x, pinCenter.y + arm * 0.35f)
                lineTo(pinCenter.x + arm * 0.55f, pinCenter.y + arm * 0.75f)
            }
            drawPath(path = archPath, color = emblemColor, style = Stroke(width = emblemStroke, cap = StrokeCap.Round))
        }

        "monastery" -> {
            // Monastery Mountain Tower Gable & Rising Ascent Cross
            val arm = coreRadius * 0.65f
            // Steeple tower gable
            val towerPath = Path().apply {
                moveTo(pinCenter.x - arm * 0.6f, pinCenter.y + arm * 0.65f)
                lineTo(pinCenter.x, pinCenter.y - arm * 0.2f)
                lineTo(pinCenter.x + arm * 0.6f, pinCenter.y + arm * 0.65f)
            }
            drawPath(path = towerPath, color = emblemColor, style = Stroke(width = emblemStroke, cap = StrokeCap.Round))

            // Ascent Cross at pinnacle
            drawLine(
                color = emblemColor,
                start = Offset(pinCenter.x, pinCenter.y - arm),
                end = Offset(pinCenter.x, pinCenter.y - arm * 0.2f),
                strokeWidth = emblemStroke,
                cap = StrokeCap.Round
            )
            drawLine(
                color = emblemColor,
                start = Offset(pinCenter.x - arm * 0.38f, pinCenter.y - arm * 0.68f),
                end = Offset(pinCenter.x + arm * 0.38f, pinCenter.y - arm * 0.68f),
                strokeWidth = emblemStroke,
                cap = StrokeCap.Round
            )
        }

        "cathedral" -> {
            // Grand Patriarchal Cathedral Crown Dome & Triple Finial Cross
            val arm = coreRadius * 0.65f
            // Cathedral dome arch
            val domePath = Path().apply {
                moveTo(pinCenter.x - arm * 0.7f, pinCenter.y + arm * 0.5f)
                cubicTo(
                    pinCenter.x - arm * 0.65f, pinCenter.y - arm * 0.1f,
                    pinCenter.x + arm * 0.65f, pinCenter.y - arm * 0.1f,
                    pinCenter.x + arm * 0.7f, pinCenter.y + arm * 0.5f
                )
            }
            drawPath(path = domePath, color = emblemColor, style = Stroke(width = emblemStroke, cap = StrokeCap.Round))

            // Central Patriarch Cross
            drawLine(
                color = emblemColor,
                start = Offset(pinCenter.x, pinCenter.y - arm),
                end = Offset(pinCenter.x, pinCenter.y + arm * 0.2f),
                strokeWidth = emblemStroke,
                cap = StrokeCap.Round
            )
            drawLine(
                color = emblemColor,
                start = Offset(pinCenter.x - arm * 0.45f, pinCenter.y - arm * 0.55f),
                end = Offset(pinCenter.x + arm * 0.45f, pinCenter.y - arm * 0.55f),
                strokeWidth = emblemStroke,
                cap = StrokeCap.Round
            )
            // Side finial accents
            drawCircle(color = emblemColor, radius = 1.1f, center = Offset(pinCenter.x - arm * 0.55f, pinCenter.y + arm * 0.05f))
            drawCircle(color = emblemColor, radius = 1.1f, center = Offset(pinCenter.x + arm * 0.55f, pinCenter.y + arm * 0.05f))
        }

        "live_gubae" -> {
            // Broadcasting Soundwaves & Glowing Preaching Cross
            val arm = coreRadius * 0.62f
            // Central live beacon cross
            drawLine(
                color = SignalRed,
                start = Offset(pinCenter.x, pinCenter.y - arm),
                end = Offset(pinCenter.x, pinCenter.y + arm * 0.7f),
                strokeWidth = emblemStroke + 0.3f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = SignalRed,
                start = Offset(pinCenter.x - arm * 0.45f, pinCenter.y - arm * 0.2f),
                end = Offset(pinCenter.x + arm * 0.45f, pinCenter.y - arm * 0.2f),
                strokeWidth = emblemStroke + 0.3f,
                cap = StrokeCap.Round
            )
            // Left & Right broadcasting wave arcs
            val waveRadius = arm * 0.8f
            drawArc(
                color = SignalRed,
                startAngle = 130f,
                sweepAngle = 100f,
                useCenter = false,
                topLeft = Offset(pinCenter.x - waveRadius, pinCenter.y - waveRadius * 0.55f),
                size = Size(waveRadius * 2f, waveRadius * 1.1f),
                style = Stroke(width = 1.2f, cap = StrokeCap.Round)
            )
        }

        else -> {
            // Standard Parish Church - Circular Traditional EOTC Sanctuary & Dome Cross
            val arm = coreRadius * 0.62f
            // Central Parish Cross
            drawLine(
                color = emblemColor,
                start = Offset(pinCenter.x, pinCenter.y - arm),
                end = Offset(pinCenter.x, pinCenter.y + arm * 0.7f),
                strokeWidth = emblemStroke,
                cap = StrokeCap.Round
            )
            drawLine(
                color = emblemColor,
                start = Offset(pinCenter.x - arm * 0.7f, pinCenter.y - arm * 0.2f),
                end = Offset(pinCenter.x + arm * 0.7f, pinCenter.y - arm * 0.2f),
                strokeWidth = emblemStroke,
                cap = StrokeCap.Round
            )
            // Circular sanctuary halo ring
            drawCircle(
                color = emblemColor.copy(alpha = 0.4f),
                radius = arm * 0.38f,
                center = Offset(pinCenter.x, pinCenter.y - arm * 0.2f),
                style = Stroke(width = 1f)
            )
        }
    }

    // 7. Sanctuary Category Callout Badge & Title
    if (isSelected || zoomLevel >= 11) {
        val churchCleanName = church.name.split("(").first().trim()
        val categoryAmharic = category.titleAmharic

        val catLayout = textMeasurer.measure(
            text = categoryAmharic,
            style = TextStyle(
                fontSize = if (isSelected) 9.sp else 8.sp,
                fontWeight = FontWeight.Bold,
                color = category.badgeText
            )
        )

        val nameLayout = textMeasurer.measure(
            text = churchCleanName,
            style = TextStyle(
                fontSize = if (isSelected) 11.sp else 9.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A)
            )
        )

        val badgePaddingH = 8f
        val badgePaddingV = 3.5f
        val catBadgeW = catLayout.size.width + 12f
        val totalBadgeW = catBadgeW + nameLayout.size.width + badgePaddingH * 2f + 4f
        val totalBadgeH = max(catLayout.size.height, nameLayout.size.height) + badgePaddingV * 2f

        val badgeX = pos.x - totalBadgeW / 2f
        val badgeY = pos.y + 7f

        val mainBg = if (isSelected) category.primaryColor else if (isDark) Color(0xFF1E242B) else Color.White
        val mainBorder = if (isSelected) Color.White.copy(alpha = 0.8f) else if (isDark) category.primaryColor.copy(alpha = 0.45f) else category.strokeColor.copy(alpha = 0.35f)

        // Main Container Box
        drawRoundRect(
            color = mainBg,
            topLeft = Offset(badgeX, badgeY),
            size = Size(totalBadgeW, totalBadgeH),
            cornerRadius = CornerRadius(12f, 12f)
        )
        drawRoundRect(
            color = mainBorder,
            topLeft = Offset(badgeX, badgeY),
            size = Size(totalBadgeW, totalBadgeH),
            cornerRadius = CornerRadius(12f, 12f),
            style = Stroke(width = if (isSelected) 1.5f else 1f)
        )

        // Category Tag Pill (Left section of badge)
        val catPillX = badgeX + 3f
        val catPillY = badgeY + 2.5f
        val catPillH = totalBadgeH - 5f

        drawRoundRect(
            color = category.badgeBg,
            topLeft = Offset(catPillX, catPillY),
            size = Size(catBadgeW, catPillH),
            cornerRadius = CornerRadius(8f, 8f)
        )

        // Draw category amharic text
        drawText(
            textLayoutResult = catLayout,
            topLeft = Offset(catPillX + (catBadgeW - catLayout.size.width) / 2f, catPillY + (catPillH - catLayout.size.height) / 2f)
        )

        // Draw sanctuary name text
        val nameX = catPillX + catBadgeW + 6f
        val nameY = badgeY + (totalBadgeH - nameLayout.size.height) / 2f
        drawText(
            textLayoutResult = nameLayout,
            topLeft = Offset(nameX, nameY)
        )
    }
}

@Composable
private fun GestureHintPill(
    emoji: String,
    title: String,
    desc: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 9.5.sp,
                        lineHeight = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 2
                )
            }
        }
    }
}

private fun DrawScope.drawPilgrimGpsMarker(pos: Offset, pulseRadius: Float, pulseAlpha: Float) {
    // Glowing Wayfinding Teal pulse aura
    drawCircle(
        color = WayfindingTeal.copy(alpha = pulseAlpha * 0.45f),
        radius = pulseRadius + 14f,
        center = pos
    )
    // Elevated border ring
    drawCircle(color = Color.White, radius = 8f, center = pos)
    drawCircle(color = BorderLight, radius = 8f, center = pos, style = Stroke(width = 1.2f))
    // Wayfinding Teal core
    drawCircle(color = WayfindingTeal, radius = 5.5f, center = pos)
}

/**
 * Unified rounded vertical capsule combining Zoom In (+) and Zoom Out (-)
 * with a subtle hairline separator and elevated tactile feedback.
 */
@Composable
private fun GebetaUnifiedZoomPill(
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White.copy(alpha = 0.95f),
        shadowElevation = 5.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        modifier = modifier.width(44.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 3.dp)
        ) {
            // Zoom In (+)
            IconButton(
                onClick = {
                    haptic.vibrateClick()
                    onZoomIn()
                },
                modifier = Modifier
                    .size(38.dp)
                    .testTag("gebeta_zoom_in_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Zoom In",
                    modifier = Modifier.size(20.dp),
                    tint = CanvasBlack
                )
            }

            // Subtle Hairline Divider
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .padding(vertical = 1.dp),
                thickness = 0.8.dp,
                color = BorderLight
            )

            // Zoom Out (-)
            IconButton(
                onClick = {
                    haptic.vibrateClick()
                    onZoomOut()
                },
                modifier = Modifier
                    .size(38.dp)
                    .testTag("gebeta_zoom_out_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Zoom Out",
                    modifier = Modifier.size(20.dp),
                    tint = CanvasBlack
                )
            }
        }
    }
}

/**
 * Interactive spotlight tour / coach mark overlay for first-time map users.
 * Dims the screen with a darkened scrim, dynamically cuts out the focused icon
 * with PathFillType.EvenOdd, projects animated glowing pulse rings, and positions
 * a contextual coach mark guide with bilingual Ethiopian Orthodox sanctuary support.
 */
@Composable
private fun GebetaInteractiveMapSpotlightTourOverlay(
    visible: Boolean,
    targetBounds: Rect?,
    compassBounds: Rect?,
    zoomBounds: Rect?,
    mapSize: Size,
    onTargetClicked: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var currentStep by remember { mutableIntStateOf(0) }

    // Pulsing animation for the spotlight cutout ring
    val infiniteTransition = rememberInfiniteTransition(label = "spotlight_pulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "spotlightPulseGlow"
    )
    val beaconRadius by infiniteTransition.animateFloat(
        initialValue = 28f,
        targetValue = 68f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spotlightBeacon"
    )
    val beaconAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spotlightBeaconAlpha"
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(250)),
        exit = fadeOut(tween(200)),
        modifier = modifier.fillMaxSize()
    ) {
        val totalSteps = 4
        val stepColor = when (currentStep) {
            0 -> SignalRed
            1 -> BrandEmber
            2 -> WayfindingTeal
            else -> SignalRed
        }

        val stepBadge = when (currentStep) {
            0 -> "1 of 4 • PILGRIM GPS LOCATOR"
            1 -> "2 of 4 • 360° FREE ROTATION"
            2 -> "3 of 4 • UNIFIED ZOOM CAPSULE"
            else -> "4 of 4 • SACRED PINS & LIVE GUBAE"
        }

        val stepTitleEn = when (currentStep) {
            0 -> "Instant Street-Level Snap"
            1 -> "360° Free Map Rotation"
            2 -> "Unified Zoom In & Out"
            else -> "Sacred Pins & Live Broadcasts"
        }

        val stepTitleAm = when (currentStep) {
            0 -> "የአካባቢ መገኛ ማጉያ"
            1 -> "ሙሉ 360° አቅጣጫ ማዞሪያ"
            2 -> "ቀላል ማጉያና ማሳነሻ"
            else -> "የአድባራትና ገዳማት መለያዎች"
        }

        val stepDesc = when (currentStep) {
            0 -> "Tap this target button to instantly snap, straighten North, and zoom directly into your exact mobile location in high-detail street level (16x zoom)."
            1 -> "Twist two fingers anywhere on the map or drag the floating compass dial to smoothly reorient your perspective toward any sanctuary. Tap once to snap straight North."
            2 -> "Pinch with two fingers or tap the unified [+] and [-] capsule on the right to zoom smoothly from nationwide Ethiopia down to holy courtyards and parish gates."
            else -> "Tap any Ethiopian Orthodox cross pin on the map to inspect tabot feasts, calculate turn-by-turn routes via Gebeta, and join live spiritual broadcasts."
        }

        // Determine target center and dimensions for the cutout hole based on step
        val fallbackCenter = Offset(mapSize.width * 0.88f, mapSize.height * 0.5f)
        val targetCutoutCenter = when (currentStep) {
            0 -> targetBounds?.center ?: fallbackCenter
            1 -> compassBounds?.center ?: fallbackCenter
            2 -> zoomBounds?.center ?: fallbackCenter
            else -> Offset(mapSize.width / 2f, mapSize.height * 0.40f)
        }

        // Animate cutout center smoothly (pulling focus dynamically across elements)
        val animSpec = androidx.compose.animation.core.spring<Float>(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioLowBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessLow
        )
        val animCenterX by androidx.compose.animation.core.animateFloatAsState(
            targetValue = targetCutoutCenter.x,
            animationSpec = animSpec,
            label = "spotlightAnimCenterX"
        )
        val animCenterY by androidx.compose.animation.core.animateFloatAsState(
            targetValue = targetCutoutCenter.y,
            animationSpec = animSpec,
            label = "spotlightAnimCenterY"
        )

        // Animate cutout dimensions & corner radius
        val targetWidth = when (currentStep) {
            2 -> (zoomBounds?.width ?: 46f) + 14f
            3 -> 88f
            else -> 64f
        }
        val targetHeight = when (currentStep) {
            2 -> (zoomBounds?.height ?: 96f) + 14f
            3 -> 88f
            else -> 64f
        }
        val targetCornerRad = when (currentStep) {
            2 -> 22f
            else -> targetWidth / 2f
        }

        val animWidth by androidx.compose.animation.core.animateFloatAsState(
            targetValue = targetWidth,
            animationSpec = animSpec,
            label = "spotlightAnimWidth"
        )
        val animHeight by androidx.compose.animation.core.animateFloatAsState(
            targetValue = targetHeight,
            animationSpec = animSpec,
            label = "spotlightAnimHeight"
        )
        val animCornerRad by androidx.compose.animation.core.animateFloatAsState(
            targetValue = targetCornerRad,
            animationSpec = animSpec,
            label = "spotlightAnimCorner"
        )

        val activeCutoutCenter = Offset(animCenterX, animCenterY)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .testTag("gebeta_spotlight_tour_overlay")
        ) {
            // 1. Full-screen whitish scrim canvas with animated EvenOdd cutout hole punched through
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(currentStep, activeCutoutCenter, animWidth, animHeight) {
                        detectTapGestures { tapOffset ->
                            val dx = (tapOffset.x - activeCutoutCenter.x).let { it * it }
                            val dy = (tapOffset.y - activeCutoutCenter.y).let { it * it }
                            val maxRadiusSq = (animWidth / 2f + 16f).let { it * it }
                            val isInsideCutout = (dx + dy) <= maxRadiusSq || 
                                (tapOffset.x in (activeCutoutCenter.x - animWidth / 2f - 10f)..(activeCutoutCenter.x + animWidth / 2f + 10f) &&
                                 tapOffset.y in (activeCutoutCenter.y - animHeight / 2f - 10f)..(activeCutoutCenter.y + animHeight / 2f + 10f))

                            if (isInsideCutout) {
                                haptic.vibrateClick()
                                if (currentStep == 0) {
                                    onTargetClicked()
                                    currentStep = 1
                                } else if (currentStep < totalSteps - 1) {
                                    currentStep += 1
                                } else {
                                    onDismiss()
                                }
                            }
                        }
                    }
            ) {
                val screenPath = Path().apply {
                    addRect(Rect(0f, 0f, size.width, size.height))
                }

                val cutoutRect = Rect(
                    left = animCenterX - animWidth / 2f,
                    top = animCenterY - animHeight / 2f,
                    right = animCenterX + animWidth / 2f,
                    bottom = animCenterY + animHeight / 2f
                )

                val cutoutPath = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = cutoutRect,
                            cornerRadius = CornerRadius(animCornerRad.dp.toPx(), animCornerRad.dp.toPx())
                        )
                    )
                }

                val combinedPath = Path().apply {
                    op(screenPath, cutoutPath, androidx.compose.ui.graphics.PathOperation.Difference)
                }

                // Draw Whitish Scrim Dimming / Frosting the surrounding view
                drawPath(path = combinedPath, color = Color(0xFFFBFBFA).copy(alpha = 0.90f))

                // Draw Radiant Glow Ring around the animated cutout hole
                drawRoundRect(
                    color = stepColor.copy(alpha = 0.50f),
                    topLeft = Offset(cutoutRect.left - pulseGlow, cutoutRect.top - pulseGlow),
                    size = Size(cutoutRect.width + pulseGlow * 2f, cutoutRect.height + pulseGlow * 2f),
                    cornerRadius = CornerRadius((animCornerRad + 4).dp.toPx(), (animCornerRad + 4).dp.toPx()),
                    style = Stroke(width = 3.dp.toPx())
                )

                // Crisp dark border ring defining the cutout aperture
                drawRoundRect(
                    color = Color(0xFF222226),
                    topLeft = Offset(cutoutRect.left - 1.5f, cutoutRect.top - 1.5f),
                    size = Size(cutoutRect.width + 3f, cutoutRect.height + 3f),
                    cornerRadius = CornerRadius(animCornerRad.dp.toPx(), animCornerRad.dp.toPx()),
                    style = Stroke(width = 2.dp.toPx())
                )

                // Radiating beacon pulse waves from the spotlight center
                drawCircle(
                    color = stepColor.copy(alpha = beaconAlpha * 0.6f),
                    radius = beaconRadius + (animWidth / 2f),
                    center = activeCutoutCenter,
                    style = Stroke(width = 1.8.dp.toPx())
                )
            }

            // 2. Focused Coach Mark Overlay - Raw On Whitish Overlay (No Box / No Card / No Border)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(start = 22.dp, end = 22.dp, top = 28.dp, bottom = 96.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 480.dp)
                        .testTag("gebeta_spotlight_raw_content")
                ) {
                    // Header Row: Eyebrow Step Pill + Close (X) Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = stepColor.copy(alpha = 0.16f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = when (currentStep) {
                                        0 -> Icons.Default.GpsFixed
                                        1 -> Icons.Default.Explore
                                        2 -> Icons.Default.ZoomIn
                                        else -> Icons.Default.Church
                                    },
                                    contentDescription = null,
                                    tint = stepColor,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = stepBadge,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = stepColor
                                    )
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(30.dp)
                                .testTag("gebeta_spotlight_dismiss_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Skip Spotlight Tour",
                                tint = Color(0xFF1E1E24),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Bilingual Titles in Jet Black and Accent
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stepTitleEn,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF111115)
                            ),
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Text(
                            text = "• $stepTitleAm",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.5.sp,
                                color = stepColor
                            ),
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Description in clear pure black text
                    Text(
                        text = stepDesc,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.5.sp,
                            lineHeight = 19.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF222228)
                        )
                    )

                    if (currentStep == 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "👉 Tip: Tap the glowing target button or tap Next below!",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = SignalRed,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Navigation Action Row (Back / Step Progress Dots / Next Button)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back / Skip Button
                        if (currentStep > 0) {
                            TextButton(
                                onClick = {
                                    haptic.vibrateClick()
                                    currentStep -= 1
                                },
                                modifier = Modifier.height(38.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Color(0xFF1E1E24)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Back",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = Color(0xFF1E1E24),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                )
                            }
                        } else {
                            TextButton(
                                onClick = onDismiss,
                                modifier = Modifier.height(38.dp)
                            ) {
                                Text(
                                    text = "Skip Tour",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = Color(0xFF55555C),
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 13.sp
                                    )
                                )
                            }
                        }

                        // Step Progress Indicator Dots
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (i in 0 until totalSteps) {
                                val isCurrent = i == currentStep
                                Box(
                                    modifier = Modifier
                                        .height(5.dp)
                                        .width(if (isCurrent) 20.dp else 6.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isCurrent) stepColor
                                            else Color(0xFFD2D2D8)
                                        )
                                )
                            }
                        }

                        // Next / Explore Button
                        Button(
                            onClick = {
                                haptic.vibrateClick()
                                if (currentStep == 0) {
                                    onTargetClicked()
                                }
                                if (currentStep < totalSteps - 1) {
                                    currentStep += 1
                                } else {
                                    onDismiss()
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = stepColor,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .height(38.dp)
                                .testTag("gebeta_spotlight_next_button")
                        ) {
                            Text(
                                text = if (currentStep < totalSteps - 1) "Next" else "Explore",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (currentStep < totalSteps - 1) Icons.Default.ArrowForward else Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
