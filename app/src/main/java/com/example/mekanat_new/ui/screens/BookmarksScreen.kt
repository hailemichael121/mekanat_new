package com.example.mekanat_new.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mekanat_new.data.model.ChurchWithDistance
import com.example.mekanat_new.data.model.UpcomingNigs
import com.example.mekanat_new.data.util.EthiopianCalendar
import com.example.mekanat_new.ui.components.MekanatCrossIcon
import com.example.mekanat_new.ui.components.MekanatDistancePill
import com.example.mekanat_new.ui.components.MekanatIconBookmarks
import com.example.mekanat_new.ui.components.MekanatInkLoader
import com.example.mekanat_new.ui.components.MekanatLiveBanner
import com.example.mekanat_new.ui.components.MekanatNigsTag
import com.example.mekanat_new.ui.components.MekanatRouteButton
import com.example.mekanat_new.ui.theme.BrandEmber
import com.example.mekanat_new.ui.theme.CanvasBlack
import com.example.mekanat_new.ui.theme.CrimsonPulse
import com.example.mekanat_new.ui.theme.MekanatDataTypography
import com.example.mekanat_new.ui.theme.SignalRed
import com.example.mekanat_new.ui.theme.WayfindingTeal
import com.example.mekanat_new.ui.viewmodel.MekanatUiState

@Composable
fun BookmarksScreen(
    uiState: MekanatUiState,
    onOpenChurchDetail: (Long) -> Unit,
    onStartRoute: (ChurchWithDistance) -> Unit,
    onToggleChurchFavorite: (Long, Boolean) -> Unit,
    onToggleTabotBookmark: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSubtab by remember { mutableIntStateOf(0) }
    val subtabs = listOf("Saved Churches (${uiState.favoriteChurches.size})", "Saved Nigs (${uiState.savedNigs.size})")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = "Bookmarks & Pilgrimage",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "Saved EOTC sanctuaries and patronal feast reminders",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Segmented Tab Row
                TabRow(
                    selectedTabIndex = selectedSubtab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedSubtab]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    subtabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedSubtab == index,
                            onClick = { selectedSubtab = index },
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (selectedSubtab == index) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selectedSubtab == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }

        // Subtab Content
        if (selectedSubtab == 0) {
            // Saved Churches List
            if (uiState.favoriteChurches.isEmpty()) {
                EmptyBookmarksState(
                    title = "No Saved Churches",
                    subtitle = "Tap the bookmark ribbon on any church on the map or detail screen to save it for your pilgrimage."
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding(),
                    contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 86.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.favoriteChurches, key = { it.church.id }) { item ->
                        SavedChurchItemCard(
                            item = item,
                            onSelect = { onOpenChurchDetail(item.church.id) },
                            onRoute = { onStartRoute(item) },
                            onRemove = { onToggleChurchFavorite(item.church.id, true) }
                        )
                    }
                }
            }
        } else {
            // Saved Nigs List
            if (uiState.savedNigs.isEmpty()) {
                EmptyBookmarksState(
                    title = "No Saved Nigs Days",
                    subtitle = "Save specific Tabots and Nigs celebration dates to get timely countdowns for upcoming feasts."
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding(),
                    contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 86.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.savedNigs, key = { it.tabot.id }) { nigs ->
                        SavedNigsItemCard(
                            nigs = nigs,
                            onSelectChurch = { onOpenChurchDetail(nigs.church.id) },
                            onRoute = {
                                val match = uiState.nearbyChurches.find { it.church.id == nigs.church.id }
                                    ?: ChurchWithDistance(nigs.church, 0.0)
                                onStartRoute(match)
                            },
                            onRemove = { onToggleTabotBookmark(nigs.tabot.id, true) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SavedChurchItemCard(
    item: ChurchWithDistance,
    onSelect: () -> Unit,
    onRoute: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("saved_church_${item.church.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (item.hasActiveGubae) {
                        MekanatLiveBanner(
                            text = "LIVE GUBAE ACTIVE",
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    Text(
                        text = item.church.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    item.church.nameAmharic?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.BookmarkRemove,
                        contentDescription = "Remove bookmark",
                        tint = BrandEmber,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${item.church.region} • ${item.church.diocese}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            )

            item.nextNigsFormatted?.let { nigs ->
                Spacer(modifier = Modifier.height(6.dp))
                MekanatNigsTag(text = "Next Nigs: $nigs")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MekanatDistancePill(distanceText = "${String.format("%.1f", item.distanceKm)} km")

                MekanatRouteButton(
                    text = "▲ Route",
                    onClick = onRoute,
                    modifier = Modifier.height(36.dp)
                )
            }
        }
    }
}

@Composable
fun SavedNigsItemCard(
    nigs: UpcomingNigs,
    onSelectChurch: () -> Unit,
    onRoute: () -> Unit,
    onRemove: () -> Unit
) {
    val ethMonthName = EthiopianCalendar.monthNames.getOrElse(nigs.tabot.nigsMonth - 1) { "Month" }
    val ethMonthAmharic = EthiopianCalendar.monthNamesAmharic.getOrElse(nigs.tabot.nigsMonth - 1) { "" }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectChurch() }
            .testTag("saved_nigs_${nigs.tabot.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Countdown Pill
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (nigs.daysUntil == 0) BrandEmber else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, if (nigs.daysUntil == 0) BrandEmber else MaterialTheme.colorScheme.outline),
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Text(
                            text = if (nigs.daysUntil == 0) "TODAY'S FEAST!" else "In ${nigs.daysUntil} days",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (nigs.daysUntil == 0) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Text(
                        text = "${nigs.tabot.name} (${nigs.tabot.nameEnglish})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = nigs.church.name,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.BookmarkRemove,
                        contentDescription = "Remove Nigs",
                        tint = BrandEmber,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dates: Ethiopian & Gregorian
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Ethiopian Feast Date",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Text(
                            text = "$ethMonthAmharic ${nigs.tabot.nigsDay} ($ethMonthName ${nigs.tabot.nigsDay})",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Gregorian",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Text(
                            text = "${nigs.nextDate.month.name.take(3)} ${nigs.nextDate.dayOfMonth}, ${nigs.nextDate.year}",
                            style = MekanatDataTypography.distancePill.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                MekanatRouteButton(
                    text = "▲ Route to Church",
                    onClick = onRoute,
                    modifier = Modifier.height(36.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyBookmarksState(title: String, subtitle: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            MekanatCrossIcon(size = 56.dp, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
