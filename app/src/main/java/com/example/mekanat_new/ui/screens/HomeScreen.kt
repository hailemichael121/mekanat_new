package com.example.mekanat_new.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mekanat_new.data.maps.GebetaRouteStep
import com.example.mekanat_new.data.maps.GebetaTravelMode
import com.example.mekanat_new.data.model.ChurchWithDistance
import com.example.mekanat_new.ui.components.GebetaMapView
import com.example.mekanat_new.ui.components.MekanatInkLoader
import com.example.mekanat_new.ui.components.vibrateClick
import com.example.mekanat_new.ui.components.vibrateSubtle
import com.example.mekanat_new.ui.theme.SignalRed
import com.example.mekanat_new.ui.theme.SignalRedSubtle
import com.example.mekanat_new.ui.viewmodel.MekanatUiState

@Composable
fun HomeScreen(
    uiState: MekanatUiState,
    onSearchQueryChange: (String) -> Unit,
    onCommitSearch: (String, Int) -> Unit,
    onDeleteSearchHistory: (Long) -> Unit,
    onClearAllSearchHistory: () -> Unit,
    onFilterChipSelected: (String) -> Unit,
    onSelectChurch: (ChurchWithDistance?) -> Unit,
    onStartRoute: (ChurchWithDistance) -> Unit,
    onChangeTravelMode: (GebetaTravelMode) -> Unit = {},
    onClearRoute: () -> Unit,
    onToggleMapView: () -> Unit,
    onOpenChurchDetail: (Long) -> Unit,
    onOpenAddChurch: () -> Unit,
    onToggleFavorite: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var isNavStepsExpanded by remember { mutableStateOf(false) }
    var isSearchFocused by remember { mutableStateOf(false) }
    var isSearchPillExpanded by remember { mutableStateOf(false) }
    val isSearchOpen = isSearchPillExpanded || isSearchFocused

    val searchPillCornerRadius by animateDpAsState(
        targetValue = if (isSearchOpen) 20.dp else 28.dp,
        animationSpec = spring(dampingRatio = 0.82f, stiffness = Spring.StiffnessMediumLow),
        label = "searchPillCornerRadius"
    )

    val searchPillElevation by animateDpAsState(
        targetValue = if (isSearchOpen) 8.dp else 3.dp,
        animationSpec = spring(dampingRatio = 0.82f, stiffness = Spring.StiffnessMediumLow),
        label = "searchPillElevation"
    )

    val searchPillBorderColor by animateColorAsState(
        targetValue = if (isSearchOpen) SignalRed.copy(alpha = 0.7f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
        animationSpec = tween(durationMillis = 250),
        label = "searchPillBorderColor"
    )

    val filterChips = listOf(
        "All",
        "Live Gubae 🔴",
        "Nearby (<50km)",
        "Monasteries",
        "Cathedrals",
        "Rock-Hewn"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (uiState.isMapView) {
            // High-Definition Map View powered by Gebeta Maps Tile Services
            GebetaMapView(
                churches = uiState.filteredChurches,
                selectedChurch = uiState.selectedChurch,
                userLat = uiState.userLat,
                userLng = uiState.userLng,
                onChurchSelected = { onSelectChurch(it) },
                onMapClicked = {
                    if (isSearchOpen) {
                        isSearchPillExpanded = false
                        isSearchFocused = false
                        focusManager.clearFocus()
                    }
                    onSelectChurch(null)
                },
                routeToChurch = uiState.routeToChurch,
                routeResult = uiState.activeRouteResult,
                isCalculatingRoute = uiState.isCalculatingRoute,
                travelMode = uiState.selectedTravelMode
            )
        } else {
            // List View
            if (uiState.filteredChurches.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        MekanatInkLoader(size = 72.dp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Sanctuaries Found",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try searching for Lalibela, Axum, Gondar, or Maryam.",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 180.dp, bottom = 80.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.filteredChurches, key = { it.church.id }) { item ->
                        ChurchListItemCard(
                            item = item,
                            isFavorite = uiState.favoriteChurches.any { it.church.id == item.church.id },
                            onSelect = {
                                haptic.vibrateClick()
                                onOpenChurchDetail(item.church.id)
                            },
                            onRoute = {
                                haptic.vibrateClick()
                                onStartRoute(item)
                            },
                            onToggleFav = {
                                haptic.vibrateClick()
                                onToggleFavorite(item.church.id, it)
                            }
                        )
                    }
                }
            }
        }

        // Animated Scrim Overlay when search is expanded to smoothly collapse on outside tap
        AnimatedVisibility(
            visible = isSearchOpen,
            enter = fadeIn(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(150))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.28f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        isSearchPillExpanded = false
                        isSearchFocused = false
                        focusManager.clearFocus()
                    }
            )
        }

        // Top Search & Filter Floating Overlay
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Search Input Row: Floating Search Pill + Separated Floating Circle Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Floating Expandable Search Pill
                Surface(
                    shape = RoundedCornerShape(searchPillCornerRadius),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = searchPillElevation,
                    border = BorderStroke(1.dp, searchPillBorderColor),
                    modifier = Modifier
                        .weight(1f)
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = 0.82f,
                                stiffness = Spring.StiffnessMediumLow
                            )
                        )
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    if (isSearchOpen) {
                                        haptic.vibrateSubtle()
                                        isSearchPillExpanded = false
                                        isSearchFocused = false
                                        focusManager.clearFocus()
                                    } else {
                                        isSearchPillExpanded = true
                                    }
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = if (isSearchOpen) Icons.Default.ArrowBack else Icons.Default.Search,
                                    contentDescription = if (isSearchOpen) "Collapse search" else "Search",
                                    tint = if (isSearchOpen) SignalRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = uiState.searchQuery,
                                onValueChange = { query ->
                                    onSearchQueryChange(query)
                                    if (query.isNotBlank()) {
                                        onCommitSearch(query, uiState.filteredChurches.size)
                                    }
                                },
                                placeholder = {
                                    Text(
                                        "Search churches, Tabots, diocese...",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = 13.5.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                },
                                trailingIcon = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (uiState.searchQuery.isNotEmpty()) {
                                            IconButton(
                                                onClick = {
                                                    haptic.vibrateSubtle()
                                                    onSearchQueryChange("")
                                                },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.Clear,
                                                    contentDescription = "Clear search",
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        if (uiState.recentSearches.isNotEmpty()) {
                                            IconButton(
                                                onClick = {
                                                    haptic.vibrateSubtle()
                                                    isSearchPillExpanded = !isSearchOpen
                                                    if (!isSearchPillExpanded) {
                                                        isSearchFocused = false
                                                        focusManager.clearFocus()
                                                    }
                                                },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (isSearchOpen) Icons.Default.KeyboardArrowUp else Icons.Default.History,
                                                    contentDescription = "Toggle Recent Searches",
                                                    tint = if (isSearchOpen) SignalRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(17.dp)
                                                )
                                            }
                                        }
                                    }
                                },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .onFocusChanged { focusState ->
                                        isSearchFocused = focusState.isFocused
                                        if (focusState.isFocused) {
                                            isSearchPillExpanded = true
                                        }
                                    }
                                    .testTag("church_search_input")
                            )
                        }

                        // Expanded Search History Dropdown Menu
                        AnimatedVisibility(
                            visible = isSearchOpen,
                            enter = fadeIn(animationSpec = tween(200, delayMillis = 40)) + expandVertically(
                                animationSpec = spring(dampingRatio = 0.82f, stiffness = Spring.StiffnessMediumLow),
                                expandFrom = Alignment.Top
                            ),
                            exit = fadeOut(animationSpec = tween(140)) + shrinkVertically(
                                animationSpec = spring(dampingRatio = 0.88f, stiffness = Spring.StiffnessMedium),
                                shrinkTowards = Alignment.Top
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(bottom = 8.dp),
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)
                                )

                                // Header Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.History,
                                            contentDescription = null,
                                            tint = SignalRed,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Recent Searches",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        )
                                        if (uiState.recentSearches.isNotEmpty()) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                shape = CircleShape,
                                                color = SignalRed.copy(alpha = 0.12f)
                                            ) {
                                                Text(
                                                    text = "${uiState.recentSearches.size}",
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        color = SignalRed,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 10.sp
                                                    )
                                                )
                                            }
                                        }
                                    }

                                    if (uiState.recentSearches.isNotEmpty()) {
                                        TextButton(
                                            onClick = {
                                                haptic.vibrateClick()
                                                onClearAllSearchHistory()
                                            },
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.DeleteSweep,
                                                contentDescription = null,
                                                tint = SignalRed,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Clear All",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = SignalRed,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            )
                                        }
                                    }
                                }

                                // Recent Search Items
                                if (uiState.recentSearches.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(2.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        uiState.recentSearches.take(5).forEach { historyItem ->
                                            Surface(
                                                shape = RoundedCornerShape(10.dp),
                                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        haptic.vibrateSubtle()
                                                        onSearchQueryChange(historyItem.query)
                                                        isSearchPillExpanded = false
                                                        isSearchFocused = false
                                                        focusManager.clearFocus()
                                                    }
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier.weight(1f)
                                                    ) {
                                                        Icon(
                                                            Icons.Default.History,
                                                            contentDescription = null,
                                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                            modifier = Modifier.size(15.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(10.dp))
                                                        Text(
                                                            text = historyItem.query,
                                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight.Medium,
                                                                color = MaterialTheme.colorScheme.onSurface
                                                            ),
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                    }

                                                    IconButton(
                                                        onClick = {
                                                            haptic.vibrateSubtle()
                                                            onDeleteSearchHistory(historyItem.id)
                                                        },
                                                        modifier = Modifier.size(24.dp)
                                                    ) {
                                                        Icon(
                                                            Icons.Default.Close,
                                                            contentDescription = "Remove search",
                                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                                            modifier = Modifier.size(14.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "No recent searches yet. Explore sacred sanctuaries across Ethiopia.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 11.5.sp
                                        ),
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                                    )
                                }

                                // Quick Sanctuary Discovery Tags
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Quick Sanctuary Discovery",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 10.5.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    val discoveries = listOf("Lalibela", "Debre Damo", "Axum Tsion", "Gishen Maryam", "Live Gubae")
                                    items(discoveries) { discovery ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f),
                                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
                                            modifier = Modifier.clickable {
                                                haptic.vibrateSubtle()
                                                onSearchQueryChange(discovery)
                                                onCommitSearch(discovery, uiState.filteredChurches.size)
                                                isSearchPillExpanded = false
                                                isSearchFocused = false
                                                focusManager.clearFocus()
                                            }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = discovery,
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontWeight = FontWeight.Medium,
                                                        fontSize = 11.sp,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Separated Floating Circle Pill on the Right for View Toggle
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 3.dp,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
                    modifier = Modifier.size(52.dp)
                ) {
                    IconButton(
                        onClick = {
                            haptic.vibrateClick()
                            onToggleMapView()
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("toggle_list_map_button")
                    ) {
                        Icon(
                            imageVector = if (uiState.isMapView) Icons.Default.List else Icons.Default.Map,
                            contentDescription = "Toggle View",
                            tint = if (uiState.isMapView) MaterialTheme.colorScheme.onSurface else SignalRed,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Horizontal Floating Filter Pills (No Solid Background Box)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filterChips) { chip ->
                    val isSelected = uiState.activeFilterChip == chip
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        shadowElevation = if (isSelected) 3.dp else 2.dp,
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.clickable {
                            haptic.vibrateSubtle()
                            onFilterChipSelected(chip)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = chip,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                }
            }
        }

        // Active Route & Gebeta / Realistic Highway Navigation HUD (Top, under search)
        AnimatedVisibility(
            visible = uiState.routeToChurch != null,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 150.dp, start = 16.dp, end = 16.dp)
        ) {
            uiState.routeToChurch?.let { dest ->
                val routeRes = uiState.activeRouteResult
                val distanceText = routeRes?.distanceFormatted ?: "${String.format("%.1f", dest.distanceKm)} km"
                val durationText = routeRes?.durationFormatted ?: "${(dest.distanceKm / 45.0 * 60).toInt().coerceAtLeast(5)} min"

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, SignalRed),
                    shadowElevation = 6.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Header: Title & Close Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(SignalRedSubtle, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (uiState.isCalculatingRoute) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = SignalRed,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        Icons.Default.Navigation,
                                        contentDescription = "Active Route",
                                        tint = SignalRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Navigating to ${dest.church.name}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "$distanceText • $durationText",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = SignalRed,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (routeRes != null && routeRes.isCorridorFallback) "• Gebeta Corridor ⛰️" else "• Gebeta API 🟢",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 10.5.sp
                                        )
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    haptic.vibrateClick()
                                    onClearRoute()
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Cancel Route",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Travel Mode Selector Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            GebetaTravelMode.values().forEach { mode ->
                                val isSelected = uiState.selectedTravelMode == mode
                                val modeLabel = when (mode) {
                                    GebetaTravelMode.DRIVING -> "🚗 Driving"
                                    GebetaTravelMode.WALKING -> "🚶 Pilgrimage"
                                    GebetaTravelMode.TRANSIT -> "🚐 Transit"
                                }

                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (isSelected) SignalRed else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            if (!isSelected) {
                                                haptic.vibrateClick()
                                                onChangeTravelMode(mode)
                                            }
                                        }
                                ) {
                                    Box(
                                        modifier = Modifier.padding(vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = modeLabel,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Expandable Waypoints & Turn Guidance
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isNavStepsExpanded = !isNavStepsExpanded }
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Turn-by-Turn Guidance (${routeRes?.steps?.size ?: 3} steps)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            Icon(
                                imageVector = if (isNavStepsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        AnimatedVisibility(
                            visible = isNavStepsExpanded,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val steps = routeRes?.steps ?: listOf(
                                    GebetaRouteStep(
                                        stepNumber = 1,
                                        instruction = "Depart current GPS location toward highway corridor",
                                        distanceKm = 1.5,
                                        durationMin = 3
                                    ),
                                    GebetaRouteStep(
                                        stepNumber = 2,
                                        instruction = "Continue on main route towards ${dest.church.diocese} diocese",
                                        distanceKm = dest.distanceKm * 0.85,
                                        durationMin = (dest.distanceKm / 45.0 * 60).toInt().coerceAtLeast(5)
                                    ),
                                    GebetaRouteStep(
                                        stepNumber = 3,
                                        instruction = "Arrive at ${dest.church.name} sacred sanctuary gate",
                                        distanceKm = 0.3,
                                        durationMin = 1
                                    )
                                )

                                steps.forEachIndexed { idx, step ->
                                    WaypointStepRow(
                                        stepNumber = "${idx + 1}",
                                        instruction = step.instruction,
                                        distance = step.distanceFormatted
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            haptic.vibrateClick()
                                            val uri = Uri.parse("geo:${dest.church.latitude},${dest.church.longitude}?q=${dest.church.latitude},${dest.church.longitude}(${dest.church.name})")
                                            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                                            try {
                                                context.startActivity(mapIntent)
                                            } catch (e: Exception) {
                                                // Handle no external map handler
                                            }
                                        },
                                        shape = RoundedCornerShape(16.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("External GPS", fontSize = 11.sp)
                                    }

                                    Button(
                                        onClick = {
                                            haptic.vibrateClick()
                                            val gebetaUri = Uri.parse("https://maps.gebeta.app/?lat=${dest.church.latitude}&lng=${dest.church.longitude}")
                                            val browserIntent = Intent(Intent.ACTION_VIEW, gebetaUri)
                                            try {
                                                context.startActivity(browserIntent)
                                            } catch (e: Exception) {
                                                // ignore
                                            }
                                        },
                                        shape = RoundedCornerShape(16.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Gebeta Routing", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Selected Church Floating Bottom Preview Card (Safe Area Pushed Up, Pilled Actions)
        AnimatedVisibility(
            visible = uiState.isMapView && uiState.selectedChurch != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 16.dp, end = 16.dp, bottom = 20.dp)
        ) {
            uiState.selectedChurch?.let { item ->
                ChurchPreviewCard(
                    item = item,
                    onViewDetail = {
                        haptic.vibrateClick()
                        onOpenChurchDetail(item.church.id)
                    },
                    onStartRoute = {
                        haptic.vibrateClick()
                        onStartRoute(item)
                    },
                    onClose = {
                        haptic.vibrateSubtle()
                        onSelectChurch(null)
                    }
                )
            }
        }
    }
}

@Composable
private fun WaypointStepRow(
    stepNumber: String,
    instruction: String,
    distance: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = instruction,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = distance,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.5.sp,
                color = SignalRed
            )
        )
    }
}

@Composable
fun ChurchPreviewCard(
    item: ChurchWithDistance,
    onViewDetail: () -> Unit,
    onStartRoute: () -> Unit,
    onClose: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("church_preview_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Name & Close button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (item.hasActiveGubae) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(bottom = 6.dp)
                                .background(SignalRedSubtle, RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                Icons.Default.FiberManualRecord,
                                contentDescription = "Live",
                                tint = SignalRed,
                                modifier = Modifier.size(8.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "LIVE GUBAE NOW",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SignalRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close preview",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Metadata info row: Distance & Region
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${String.format("%.1f", item.distanceKm)} km away • ${item.church.diocese}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Primary Tabot & Nigs notice
            item.primaryTabot?.let { tabot ->
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Tabot: ${tabot.name} (${tabot.nameEnglish})",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                item.nextNigsFormatted?.let { nigs ->
                    Text(
                        text = "Next Nigs: $nigs",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: View Details & Route (Pilled 24dp buttons)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onViewDetail,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f))
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Details",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Details", fontSize = 13.sp)
                }

                Button(
                    onClick = onStartRoute,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SignalRed,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        Icons.Default.Directions,
                        contentDescription = "Route",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Route", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ChurchListItemCard(
    item: ChurchWithDistance,
    isFavorite: Boolean,
    onSelect: () -> Unit,
    onRoute: () -> Unit,
    onToggleFav: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("church_list_item_${item.church.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (item.hasActiveGubae) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(bottom = 4.dp)
                                .background(SignalRedSubtle, RoundedCornerShape(10.dp))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                Icons.Default.FiberManualRecord,
                                contentDescription = "Live",
                                tint = SignalRed,
                                modifier = Modifier.size(8.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "LIVE GUBAE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SignalRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.5.sp
                                )
                            )
                        }
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
                    onClick = { onToggleFav(isFavorite) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isFavorite) SignalRed else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${item.church.region} • ${item.church.diocese}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            )

            item.primaryTabot?.let { tabot ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tabot: ${tabot.name} (${tabot.nameEnglish})",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Footer Row: Distance & Pilled Route Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${String.format("%.1f", item.distanceKm)} km away",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )

                Button(
                    onClick = onRoute,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SignalRed,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        Icons.Default.Directions,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Route", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
