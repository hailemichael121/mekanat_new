package com.example.mekanat_new.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.mekanat_new.data.local.ChurchEntity
import com.example.mekanat_new.data.local.TabotEntity
import com.example.mekanat_new.data.model.ChurchWithDistance
import com.example.mekanat_new.data.util.EthiopianCalendar
import com.example.mekanat_new.data.util.MekanatMedia
import com.example.mekanat_new.ui.components.MekanatCrossIcon
import com.example.mekanat_new.ui.components.MekanatDistancePill
import com.example.mekanat_new.ui.components.MekanatIconBack
import com.example.mekanat_new.ui.components.MekanatIconBookmarks
import com.example.mekanat_new.ui.components.MekanatIconShare
import com.example.mekanat_new.ui.components.MekanatInkLoader
import com.example.mekanat_new.ui.components.MekanatLiveBanner
import com.example.mekanat_new.ui.components.MekanatNigsTag
import com.example.mekanat_new.ui.components.MekanatRouteButton
import com.example.mekanat_new.ui.components.vibrateClick
import com.example.mekanat_new.ui.theme.BrandEmber
import com.example.mekanat_new.ui.theme.CanvasBlack
import com.example.mekanat_new.ui.theme.MekanatDataTypography
import com.example.mekanat_new.ui.theme.SignalRed
import com.example.mekanat_new.ui.theme.SignalRedSubtle
import com.example.mekanat_new.ui.theme.WayfindingTeal
import com.example.mekanat_new.ui.viewmodel.MekanatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChurchDetailScreen(
    churchId: Long,
    viewModel: MekanatViewModel,
    onBack: () -> Unit,
    onStartRoute: (ChurchWithDistance) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val churchDetail by viewModel.getChurchDetailFlow(churchId).collectAsState(initial = null)
    val uiState by viewModel.uiState.collectAsState()
    var showAddGubaeDialog by remember { mutableStateOf(false) }

    val church = churchDetail?.church
    val isFavorite = churchDetail?.isFavorite ?: false
    val tabots = churchDetail?.tabots ?: emptyList()
    val activeGubae = churchDetail?.activeGubae ?: emptyList()

    val distKm = church?.let {
        val match = uiState.nearbyChurches.find { item -> item.church.id == churchId }
        match?.distanceKm ?: 0.0
    } ?: 0.0

    val etaMinutes = (distKm / 45.0 * 60).toInt().coerceAtLeast(5)

    fun shareChurch(ch: ChurchEntity) {
        haptic.vibrateClick()
        val tabotsText = if (tabots.isNotEmpty()) {
            tabots.joinToString(", ") { "${it.name} (${it.nameEnglish})" }
        } else {
            "Saint Mary / Saint George"
        }

        val shareMessage = buildString {
            append(ch.name)
            ch.nameAmharic?.let { append(" | $it") }
            append("\n")
            append("Diocese: ${ch.diocese}, ${ch.region}\n")
            append("GPS Coordinates: ${String.format("%.5f", ch.latitude)}, ${String.format("%.5f", ch.longitude)}\n\n")
            append("Google Maps: https://maps.google.com/?q=${ch.latitude},${ch.longitude}\n")
            append("Gebeta Maps: https://maps.gebeta.app/?lat=${ch.latitude}&lng=${ch.longitude}\n\n")
            append("Holy Tabot(s): $tabotsText\n")
            ch.description?.let {
                append("\nSanctuary History:\n$it\n")
            }
            append("\nShared via Mekanāt (መካናት) — Ethiopian Orthodox Pilgrimage & Sanctuaries")
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            putExtra(Intent.EXTRA_TITLE, ch.name)
            type = "text/plain"
        }
        val chooser = Intent.createChooser(sendIntent, "Share Sanctuary & Coordinates")
        context.startActivity(chooser)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = church?.name ?: "Sanctuary Details",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        haptic.vibrateClick()
                        onBack()
                    }) {
                        MekanatIconBack(tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                actions = {
                    church?.let { ch ->
                        IconButton(
                            onClick = { shareChurch(ch) },
                            modifier = Modifier.testTag("share_church_button")
                        ) {
                            MekanatIconShare(tint = MaterialTheme.colorScheme.onSurface)
                        }

                        IconButton(
                            onClick = {
                                haptic.vibrateClick()
                                viewModel.onToggleChurchFavorite(ch.id, isFavorite)
                            },
                            modifier = Modifier.testTag("favorite_toggle_button")
                        ) {
                            MekanatIconBookmarks(
                                tint = if (isFavorite) BrandEmber else MaterialTheme.colorScheme.onSurface,
                                filled = isFavorite
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            church?.let { ch ->
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${String.format("%.1f", distKm)} km • ~$etaMinutes min driving",
                                style = MekanatDataTypography.instrumentationLarge.copy(
                                    fontSize = 14.sp,
                                    color = WayfindingTeal
                                )
                            )
                            Text(
                                text = "From current GPS position",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        MekanatRouteButton(
                            text = "▲ Start route",
                            onClick = {
                                val item = uiState.nearbyChurches.find { it.church.id == ch.id }
                                    ?: ChurchWithDistance(ch, distKm, activeGubae.isNotEmpty())
                                onStartRoute(item)
                            },
                            modifier = Modifier.testTag("detail_route_button")
                        )
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        if (church == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                MekanatInkLoader(size = 56.dp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Hero Image with Coil & Title Overlay
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            ) {
                                SubcomposeAsyncImage(
                                    model = MekanatMedia.getChurchImageUrl(church.id),
                                    contentDescription = church.name,
                                    contentScale = ContentScale.Crop,
                                    loading = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(MaterialTheme.colorScheme.surfaceVariant),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(28.dp),
                                                color = SignalRed,
                                                strokeWidth = 2.dp
                                            )
                                        }
                                    },
                                    error = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(MaterialTheme.colorScheme.surfaceVariant),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            MekanatCrossIcon(
                                                size = 64.dp,
                                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                            )
                                        }
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Gradient scrim for readability
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color.Black.copy(alpha = 0.6f)
                                                )
                                            )
                                        )
                                )

                                // Church type tag
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = CanvasBlack.copy(alpha = 0.75f),
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = church.churchType.replace("_", " "),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            color = Color.White
                                        ),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            // Header details
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = church.name,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                )
                                church.nameAmharic?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 15.sp
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = SignalRed,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${church.region} • ${church.diocese}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Currently Happening (Live Gubae) Banner
                if (activeGubae.isNotEmpty()) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            activeGubae.forEach { gubae ->
                                MekanatLiveBanner(
                                    text = "CURRENTLY HAPPENING • ${gubae.title.uppercase()}"
                                )
                                gubae.description?.let { desc ->
                                    Card(
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = desc,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                lineHeight = 18.sp
                                            ),
                                            modifier = Modifier.padding(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Sacred Tabots Section with Coil Image Rendering & Feast Details
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Sacred Tabots & Annual Nigs (${tabots.size})",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            tabots.forEachIndexed { index, tabot ->
                                val isNigsSaved = uiState.savedNigs.any { it.tabot.id == tabot.id }
                                val ethMonthName = EthiopianCalendar.monthNames.getOrElse(tabot.nigsMonth - 1) { "Month" }
                                val ethMonthAmharic = EthiopianCalendar.monthNamesAmharic.getOrElse(tabot.nigsMonth - 1) { "" }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Coil Icon / Image for Tabot
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        SubcomposeAsyncImage(
                                            model = MekanatMedia.getTabotImageUrl(tabot.nameEnglish),
                                            contentDescription = tabot.name,
                                            contentScale = ContentScale.Crop,
                                            loading = {
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    CircularProgressIndicator(
                                                        modifier = Modifier.size(18.dp),
                                                        color = BrandEmber,
                                                        strokeWidth = 2.dp
                                                    )
                                                }
                                            },
                                            error = {
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    MekanatCrossIcon(
                                                        size = 28.dp,
                                                        tint = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${tabot.name} (${tabot.nameEnglish})",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = "Annual Nigs: $ethMonthAmharic ${tabot.nigsDay} ($ethMonthName ${tabot.nigsDay})",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = BrandEmber,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 11.5.sp
                                            )
                                        )
                                        tabot.description?.let { desc ->
                                            if (desc.isNotBlank()) {
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = desc,
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        fontSize = 11.sp,
                                                        lineHeight = 15.sp
                                                    ),
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                        tabot.routingDescription?.let { routing ->
                                            if (routing.isNotBlank()) {
                                                Spacer(modifier = Modifier.height(3.dp))
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        Icons.Default.DirectionsWalk,
                                                        contentDescription = null,
                                                        tint = WayfindingTeal,
                                                        modifier = Modifier.size(13.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(3.dp))
                                                    Text(
                                                        text = routing,
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                            color = WayfindingTeal,
                                                            fontSize = 10.5.sp
                                                        ),
                                                        maxLines = 2,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    IconButton(
                                        onClick = { viewModel.onToggleTabotBookmark(tabot.id, isNigsSaved) },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        MekanatIconBookmarks(
                                            tint = if (isNigsSaved) BrandEmber else MaterialTheme.colorScheme.onSurfaceVariant,
                                            filled = isNigsSaved,
                                            size = 20.dp
                                        )
                                    }
                                }

                                if (index < tabots.size - 1) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }
                        }
                    }
                }

                // History & Spiritual Heritage Section
                church.history?.let { hist ->
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.HistoryEdu,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Spiritual History & Tradition",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = hist,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 22.sp,
                                        fontSize = 13.5.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // Description / Overview Section
                church.description?.let { desc ->
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Overview & Sacred Geography",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = desc,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 20.sp,
                                        fontSize = 13.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // Community Gubae Gatherings Action
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Community Gatherings",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        TextButton(onClick = { showAddGubaeDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Announce Gubae", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                // Contact & Location
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Pilgrim Location & Contact",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            church.address?.let {
                                Text(
                                    text = "Address: $it",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 12.5.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            Text(
                                text = "Coordinates: ${String.format("%.4f", church.latitude)}° N, ${String.format("%.4f", church.longitude)}° E",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.5.sp
                                )
                            )

                            church.contactPhone?.let {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Phone: $it",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 12.5.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Gubae Dialog
    if (showAddGubaeDialog) {
        var gubaeTitle by remember { mutableStateOf("") }
        var gubaeDesc by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddGubaeDialog = false },
            title = { Text("Announce Spiritual Gubae", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Announce an upcoming feast, chanting vigil, or teaching gubae at ${church?.name}.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    OutlinedTextField(
                        value = gubaeTitle,
                        onValueChange = { gubaeTitle = it },
                        label = { Text("Event Title (e.g. Annual Youth Gathering)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = gubaeDesc,
                        onValueChange = { gubaeDesc = it },
                        label = { Text("Description & Schedule") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (gubaeTitle.isNotBlank()) {
                            val now = System.currentTimeMillis()
                            val threeDaysLater = now + (3 * 24 * 60 * 60 * 1000L)
                            viewModel.onAddGubaeEvent(
                                churchId = churchId,
                                title = gubaeTitle.trim(),
                                description = gubaeDesc.trim().ifBlank { null },
                                startEpoch = now,
                                endEpoch = threeDaysLater
                            )
                            showAddGubaeDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SignalRed)
                ) {
                    Text("Announce")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddGubaeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
