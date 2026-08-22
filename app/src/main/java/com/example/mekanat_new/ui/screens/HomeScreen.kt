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
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Straight
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.TurnLeft
import androidx.compose.material.icons.filled.TurnRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mekanat_new.data.maps.GebetaRouteStep
import com.example.mekanat_new.data.maps.GebetaTravelMode
import com.example.mekanat_new.data.model.ChurchWithDistance
import com.example.mekanat_new.ui.components.FullScreenSearchOverlayLoader
import com.example.mekanat_new.ui.components.GebetaMapView
import com.example.mekanat_new.ui.components.MekanatDistancePill
import com.example.mekanat_new.ui.components.MekanatIconBookmarks
import com.example.mekanat_new.ui.components.MekanatIconDrive
import com.example.mekanat_new.ui.components.MekanatIconRoute
import com.example.mekanat_new.ui.components.MekanatIconTransit
import com.example.mekanat_new.ui.components.MekanatIconWalk
import com.example.mekanat_new.ui.components.MekanatInkLoader
import com.example.mekanat_new.ui.components.MekanatLiveBanner
import com.example.mekanat_new.ui.components.MekanatNigsTag
import com.example.mekanat_new.ui.components.MekanatRouteButton
import com.example.mekanat_new.ui.components.MekanatSecondaryButton
import com.example.mekanat_new.ui.components.MekanatTag
import com.example.mekanat_new.ui.components.getChurchMarkerCategory
import com.example.mekanat_new.ui.components.vibrateClick
import com.example.mekanat_new.ui.components.vibrateSubtle
import com.example.mekanat_new.ui.theme.BrandEmber
import com.example.mekanat_new.ui.theme.CrimsonPulse
import com.example.mekanat_new.ui.theme.MekanatDataTypography
import com.example.mekanat_new.ui.theme.SignalRed
import com.example.mekanat_new.ui.theme.SignalRedSubtle
import com.example.mekanat_new.ui.theme.StatusGreen
import com.example.mekanat_new.ui.theme.WayfindingTeal
import com.example.mekanat_new.ui.viewmodel.ChurchSortOption
import com.example.mekanat_new.ui.viewmodel.MekanatUiState

@Composable
fun HomeScreen(
    uiState: MekanatUiState,
    onSearchQueryChange: (String) -> Unit,
    onCommitSearch: (String, Int) -> Unit,
    onTriggerSearchWithAnimation: (String) -> Unit = {},
    onDismissSearchLoader: () -> Unit = {},
    onDeleteSearchHistory: (Long) -> Unit,
    onClearAllSearchHistory: () -> Unit,
    onFilterChipSelected: (String) -> Unit,
    onSetSortOption: (ChurchSortOption) -> Unit = {},
    onSelectChurch: (ChurchWithDistance?) -> Unit,
    onStartRoute: (ChurchWithDistance) -> Unit,
    onChangeTravelMode: (GebetaTravelMode) -> Unit = {},
    onReverseRoute: () -> Unit = {},
    onToggleLiveNav: () -> Unit = {},
    onNextNavStep: () -> Unit = {},
    onPrevNavStep: () -> Unit = {},
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
    var showSortMenu by remember { mutableStateOf(false) }
    var navOriginQuery by remember(uiState.routeToChurch, uiState.isRouteReversed) {
        mutableStateOf(if (uiState.isRouteReversed) (uiState.routeToChurch?.church?.name ?: "") else "Current Location (GPS)")
    }
    var navDestQuery by remember(uiState.routeToChurch, uiState.isRouteReversed) {
        mutableStateOf(if (uiState.isRouteReversed) "Current Location (GPS)" else (uiState.routeToChurch?.church?.name ?: ""))
    }
    var activeNavSearchTarget by remember { mutableStateOf<String?>(null) }
    val isSearchOpen = isSearchPillExpanded || isSearchFocused

    val searchPillCornerRadius by animateDpAsState(
        targetValue = if (isSearchOpen) 20.dp else 28.dp,
        animationSpec = spring(dampingRatio = 0.82f, stiffness = Spring.StiffnessMediumLow),
        label = "searchPillCornerRadius"
    )

    val searchPillElevation by animateDpAsState(
        targetValue = if (isSearchOpen) 6.dp else 2.dp,
        animationSpec = spring(dampingRatio = 0.82f, stiffness = Spring.StiffnessMediumLow),
        label = "searchPillElevation"
    )

    val filterChips = listOf(
        "All",
        "Live Gubae",
        "Nearby (<50km)",
        "Monasteries",
        "Cathedrals",
        "Rock-Hewn"
    )

    fun submitSearch(query: String) {
        val trimmed = query.trim()
        if (trimmed.isNotBlank()) {
            haptic.vibrateClick()
            isSearchPillExpanded = false
            isSearchFocused = false
            focusManager.clearFocus()
            onTriggerSearchWithAnimation(trimmed)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (uiState.isMapView) Color(0xFF191C20) else MaterialTheme.colorScheme.background)
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
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 126.dp, bottom = 80.dp),
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
                    .background(Color.Black.copy(alpha = 0.32f))
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

        // Top Search & Filter Floating Overlay (Only shown when not routing)
        if (uiState.routeToChurch == null) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
            // Search Input Row: Floating Search Pill + Separated Floating Circle View Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Floating Expandable Search Pill
                Surface(
                    shape = RoundedCornerShape(searchPillCornerRadius),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = searchPillElevation,
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
                                .padding(horizontal = 12.dp),
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
                                        submitSearch(uiState.searchQuery)
                                    }
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = if (isSearchOpen) Icons.Default.ArrowBack else Icons.Default.Search,
                                    contentDescription = if (isSearchOpen) "Collapse search" else "Search",
                                    tint = if (isSearchOpen) SignalRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            OutlinedTextField(
                                value = uiState.searchQuery,
                                onValueChange = { query ->
                                    onSearchQueryChange(query)
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
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        submitSearch(uiState.searchQuery)
                                    }
                                ),
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

                                        // Submit button on search bar
                                        IconButton(
                                            onClick = { submitSearch(uiState.searchQuery) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = "Run Search",
                                                tint = SignalRed,
                                                modifier = Modifier.size(18.dp)
                                            )
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
                            enter = fadeIn(animationSpec = tween(180)) + expandVertically(
                                animationSpec = spring(dampingRatio = 0.82f, stiffness = Spring.StiffnessMediumLow),
                                expandFrom = Alignment.Top
                            ),
                            exit = fadeOut(animationSpec = tween(120)) + shrinkVertically(
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
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
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
                                            tint = MaterialTheme.colorScheme.primary,
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
                                                color = MaterialTheme.colorScheme.primaryContainer
                                            ) {
                                                Text(
                                                    text = "${uiState.recentSearches.size}",
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
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
                                                        submitSearch(historyItem.query)
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
                                    val discoveries = listOf("Lalibela", "Debre Damo", "Axum Tsion", "Gishen Maryam", "Gondar", "Live Gubae")
                                    items(discoveries) { discovery ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f),
                                            modifier = Modifier.clickable {
                                                haptic.vibrateSubtle()
                                                onSearchQueryChange(discovery)
                                                submitSearch(discovery)
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

                // Separated Floating Circle Pill on the Right for View Toggle with Sanctuary Count Badge
                AnimatedVisibility(
                    visible = !isSearchFocused && !isSearchPillExpanded,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier.size(52.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surface,
                                shadowElevation = 2.dp,
                                modifier = Modifier.fillMaxSize()
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
                                        tint = if (uiState.isMapView) MaterialTheme.colorScheme.primary else SignalRed,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            // Sanctuary Count Badge overlay on top-right of the toggle button (Filled Orangy with White Text)
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = BrandEmber,
                                shadowElevation = 3.dp,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 4.dp, y = (-3).dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .background(Color.White, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(3.5.dp))
                                    Text(
                                        text = "${uiState.filteredChurches.size}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.5.sp,
                                            color = Color.White
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Unified Single-Row Filter & Sort Strip: "Nearest" Sort pill on same row as "All", "Live Gubae", etc.
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // First Item: Sort / Nearest Pill with Dropdown Menu (Filled Pill)
                item {
                    val isSortCustom = uiState.sortOption != ChurchSortOption.DISTANCE_NEAREST
                    Box {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSortCustom) BrandEmber else MaterialTheme.colorScheme.surface,
                            border = if (!isSortCustom) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)) else null,
                            shadowElevation = if (isSortCustom) 3.dp else 1.5.dp,
                            modifier = Modifier.clickable {
                                haptic.vibrateSubtle()
                                showSortMenu = true
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = "Sort",
                                    tint = if (isSortCustom) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = when (uiState.sortOption) {
                                        ChurchSortOption.DISTANCE_NEAREST -> "Nearest"
                                        ChurchSortOption.DISTANCE_FURTHEST -> "Furthest"
                                        ChurchSortOption.NAME_AZ -> "Name A-Z"
                                        ChurchSortOption.NAME_ZA -> "Name Z-A"
                                        ChurchSortOption.HISTORICAL -> "Oldest"
                                    },
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSortCustom) FontWeight.Bold else FontWeight.SemiBold,
                                        fontSize = 11.5.sp,
                                        color = if (isSortCustom) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Nearest First (Distance ↑)") },
                                onClick = {
                                    onSetSortOption(ChurchSortOption.DISTANCE_NEAREST)
                                    showSortMenu = false
                                },
                                leadingIcon = {
                                    if (uiState.sortOption == ChurchSortOption.DISTANCE_NEAREST) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = BrandEmber)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Furthest First (Distance ↓)") },
                                onClick = {
                                    onSetSortOption(ChurchSortOption.DISTANCE_FURTHEST)
                                    showSortMenu = false
                                },
                                leadingIcon = {
                                    if (uiState.sortOption == ChurchSortOption.DISTANCE_FURTHEST) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = BrandEmber)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sanctuary Name (A - Z)") },
                                onClick = {
                                    onSetSortOption(ChurchSortOption.NAME_AZ)
                                    showSortMenu = false
                                },
                                leadingIcon = {
                                    if (uiState.sortOption == ChurchSortOption.NAME_AZ) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = BrandEmber)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sanctuary Name (Z - A)") },
                                onClick = {
                                    onSetSortOption(ChurchSortOption.NAME_ZA)
                                    showSortMenu = false
                                },
                                leadingIcon = {
                                    if (uiState.sortOption == ChurchSortOption.NAME_ZA) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = BrandEmber)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Spiritual Antiquity (Oldest First)") },
                                onClick = {
                                    onSetSortOption(ChurchSortOption.HISTORICAL)
                                    showSortMenu = false
                                },
                                leadingIcon = {
                                    if (uiState.sortOption == ChurchSortOption.HISTORICAL) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = BrandEmber)
                                    }
                                }
                            )
                        }
                    }
                }

                // Category Filter Pills (Solid Filled Pills: Orangy when active, Solid Surface with crisp border when inactive)
                items(filterChips) { chip ->
                    val isSelected = uiState.activeFilterChip == chip
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) BrandEmber else MaterialTheme.colorScheme.surface,
                        border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)) else null,
                        shadowElevation = if (isSelected) 3.5.dp else 1.5.dp,
                        modifier = Modifier.clickable {
                            haptic.vibrateSubtle()
                            onFilterChipSelected(chip)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 13.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .background(Color.White, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                            }
                            Text(
                                text = chip,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                }
            }
        }
    }

        // Dedicated Directions Mode: Top Floating Origin & Destination Search Bar (Pushed lower, divider, search inputs)
        AnimatedVisibility(
            visible = uiState.routeToChurch != null,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 14.dp, end = 14.dp, top = 16.dp, bottom = 8.dp)
        ) {
            uiState.routeToChurch?.let { dest ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 8.dp,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Visual Path Node Indicator (Green Dot -> Vertical Line -> Orange Dot)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(start = 4.dp, end = 10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(9.dp)
                                        .background(StatusGreen, CircleShape)
                                )
                                Box(
                                    modifier = Modifier
                                        .width(1.5.dp)
                                        .height(18.dp)
                                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))
                                )
                                Box(
                                    modifier = Modifier
                                        .size(9.dp)
                                        .background(BrandEmber, CircleShape)
                                )
                            }

                            // Origin & Destination Search Inputs Column
                            Column(modifier = Modifier.weight(1f)) {
                                // Origin Search Input
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(32.dp)
                                ) {
                                    BasicTextField(
                                        value = navOriginQuery,
                                        onValueChange = {
                                            navOriginQuery = it
                                            activeNavSearchTarget = "origin"
                                        },
                                        singleLine = true,
                                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.5.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        ),
                                        cursorBrush = SolidColor(BrandEmber),
                                        modifier = Modifier
                                            .weight(1f)
                                            .onFocusChanged {
                                                if (it.isFocused) {
                                                    activeNavSearchTarget = "origin"
                                                }
                                            }
                                            .testTag("nav_origin_input"),
                                        decorationBox = { innerTextField ->
                                            if (navOriginQuery.isEmpty()) {
                                                Text(
                                                    text = "Starting point (GPS or search...)",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontSize = 12.5.sp,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                                    )
                                                )
                                            }
                                            innerTextField()
                                        }
                                    )

                                    if (navOriginQuery.isNotEmpty() && activeNavSearchTarget == "origin") {
                                        IconButton(
                                            onClick = { navOriginQuery = "" },
                                            modifier = Modifier.size(22.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Clear,
                                                contentDescription = "Clear Origin",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(13.dp)
                                            )
                                        }
                                    }
                                }

                                // Visual Divider Between Start and Destination
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )

                                // Destination Search Input
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(32.dp)
                                ) {
                                    BasicTextField(
                                        value = navDestQuery,
                                        onValueChange = {
                                            navDestQuery = it
                                            activeNavSearchTarget = "dest"
                                        },
                                        singleLine = true,
                                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.5.sp,
                                            color = BrandEmber
                                        ),
                                        cursorBrush = SolidColor(BrandEmber),
                                        modifier = Modifier
                                            .weight(1f)
                                            .onFocusChanged {
                                                if (it.isFocused) {
                                                    activeNavSearchTarget = "dest"
                                                }
                                            }
                                            .testTag("nav_dest_input"),
                                        decorationBox = { innerTextField ->
                                            if (navDestQuery.isEmpty()) {
                                                Text(
                                                    text = "Destination sanctuary...",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontSize = 12.5.sp,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                                    )
                                                )
                                            }
                                            innerTextField()
                                        }
                                    )

                                    if (navDestQuery.isNotEmpty() && activeNavSearchTarget == "dest") {
                                        IconButton(
                                            onClick = { navDestQuery = "" },
                                            modifier = Modifier.size(22.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Clear,
                                                contentDescription = "Clear Destination",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(13.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            // Swap / Reverse Origin & Destination Toggle Switch Button
                            IconButton(
                                onClick = {
                                    haptic.vibrateClick()
                                    activeNavSearchTarget = null
                                    focusManager.clearFocus()
                                    onReverseRoute()
                                },
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                                    .testTag("btn_swap_route")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapVert,
                                    contentDescription = "Reverse Origin and Destination",
                                    tint = BrandEmber,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            // Close / Exit Route Button
                            IconButton(
                                onClick = {
                                    haptic.vibrateClick()
                                    activeNavSearchTarget = null
                                    focusManager.clearFocus()
                                    onClearRoute()
                                },
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f), CircleShape)
                                    .testTag("btn_close_route")
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Cancel Route",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    // Interactive Suggestions Dropdown Card
                    AnimatedVisibility(
                        visible = activeNavSearchTarget != null,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        val currentSearchQuery = if (activeNavSearchTarget == "origin") navOriginQuery else navDestQuery
                        val suggestions = remember(currentSearchQuery, uiState.filteredChurches) {
                            val query = currentSearchQuery.trim()
                            if (query.isEmpty() || query.equals("Current Location (GPS)", ignoreCase = true)) {
                                uiState.filteredChurches.take(6)
                            } else {
                                uiState.filteredChurches.filter {
                                    it.church.name.contains(query, ignoreCase = true) ||
                                    (it.church.nameAmharic?.contains(query, ignoreCase = true) == true) ||
                                    it.church.diocese.contains(query, ignoreCase = true) ||
                                    it.church.churchType.contains(query, ignoreCase = true)
                                }.take(8)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            shadowElevation = 8.dp,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .fillMaxWidth()
                                .heightIn(max = 240.dp)
                        ) {
                            LazyColumn(
                                contentPadding = PaddingValues(vertical = 4.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (activeNavSearchTarget == "origin") {
                                    item {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    haptic.vibrateClick()
                                                    navOriginQuery = "Current Location (GPS)"
                                                    activeNavSearchTarget = null
                                                    focusManager.clearFocus()
                                                    if (uiState.isRouteReversed) {
                                                        onReverseRoute()
                                                    }
                                                }
                                                .padding(horizontal = 14.dp, vertical = 9.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.MyLocation,
                                                contentDescription = "Current Location",
                                                tint = StatusGreen,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(
                                                    text = "Current Location (GPS)",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = StatusGreen
                                                    )
                                                )
                                                Text(
                                                    text = "Addis Ababa / My Device Position",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                )
                                            }
                                        }
                                        HorizontalDivider(
                                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                            thickness = 0.5.dp
                                        )
                                    }
                                }

                                items(suggestions, key = { it.church.id }) { suggestion ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                haptic.vibrateClick()
                                                if (activeNavSearchTarget == "origin") {
                                                    navOriginQuery = suggestion.church.name
                                                    activeNavSearchTarget = null
                                                    focusManager.clearFocus()
                                                } else {
                                                    navDestQuery = suggestion.church.name
                                                    activeNavSearchTarget = null
                                                    focusManager.clearFocus()
                                                    onStartRoute(suggestion)
                                                }
                                            }
                                            .padding(horizontal = 14.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Place,
                                            contentDescription = null,
                                            tint = if (activeNavSearchTarget == "dest") BrandEmber else StatusGreen,
                                            modifier = Modifier.size(17.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = suggestion.church.name,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 12.5.sp,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "${suggestion.church.nameAmharic ?: ""} • ${suggestion.church.diocese}",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 10.5.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        val distFormatted = if (suggestion.distanceKm < 1.0) {
                                            "${(suggestion.distanceKm * 1000).toInt()} m"
                                        } else {
                                            "%.1f km".format(suggestion.distanceKm)
                                        }
                                        MekanatDistancePill(
                                            distanceText = distFormatted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Dedicated Directions Mode: Bottom Navigation Controls & Travel Options Sheet
        AnimatedVisibility(
            visible = uiState.routeToChurch != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 80.dp)
        ) {
            uiState.routeToChurch?.let { dest ->
                val routeRes = uiState.activeRouteResult
                val distanceText = routeRes?.distanceFormatted ?: "${String.format("%.1f", dest.distanceKm)} km"
                val durationText = routeRes?.durationFormatted ?: "${(dest.distanceKm / 45.0 * 60).toInt().coerceAtLeast(5)} min"

                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 10.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Travel Mode Selector Pills (Drive / Walk / Transit)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            GebetaTravelMode.values().forEach { mode ->
                                val isSelected = uiState.selectedTravelMode == mode
                                val modeTitle = when (mode) {
                                    GebetaTravelMode.DRIVING -> "Drive"
                                    GebetaTravelMode.WALKING -> "Pilgrim"
                                    GebetaTravelMode.TRANSIT -> "Transit"
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
                                    Row(
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        when (mode) {
                                            GebetaTravelMode.DRIVING -> MekanatIconDrive(
                                                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                                size = 15.dp
                                            )
                                            GebetaTravelMode.WALKING -> MekanatIconWalk(
                                                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                                size = 15.dp
                                            )
                                            GebetaTravelMode.TRANSIT -> MekanatIconTransit(
                                                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                                size = 15.dp
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(
                                            text = modeTitle,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Live Guidance Turn Banner OR Static Overview with Start Button
                        if (uiState.isLiveNavigating) {
                            val steps = routeRes?.steps ?: listOf(
                                GebetaRouteStep(1, "Head forward toward highway corridor", 1.2, 3),
                                GebetaRouteStep(2, "Follow main road toward destination", dest.distanceKm * 0.8, 15),
                                GebetaRouteStep(3, "Arrive at sacred sanctuary gate", 0.3, 1)
                            )
                            val currentStepIndex = uiState.liveNavStepIndex.coerceIn(0, (steps.size - 1).coerceAtLeast(0))
                            val currentStep = steps.getOrElse(currentStepIndex) { steps.first() }

                            val directionIcon = when {
                                currentStep.instruction.contains("left", ignoreCase = true) -> Icons.Default.TurnLeft
                                currentStep.instruction.contains("right", ignoreCase = true) -> Icons.Default.TurnRight
                                currentStep.instruction.contains("arrive", ignoreCase = true) -> Icons.Default.Place
                                else -> Icons.Default.Straight
                            }

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = StatusGreen,
                                contentColor = Color.White,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = directionIcon,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(28.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = currentStep.instruction,
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.5.sp,
                                                    color = Color.White
                                                ),
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "In ${currentStep.distanceFormatted} • Step ${currentStepIndex + 1} of ${steps.size}",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Color.White.copy(alpha = 0.85f),
                                                    fontSize = 11.5.sp
                                                )
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            IconButton(
                                                onClick = {
                                                    haptic.vibrateSubtle()
                                                    onPrevNavStep()
                                                },
                                                enabled = currentStepIndex > 0,
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.NavigateBefore,
                                                    contentDescription = "Previous step",
                                                    tint = if (currentStepIndex > 0) Color.White else Color.White.copy(alpha = 0.4f),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }

                                            IconButton(
                                                onClick = {
                                                    haptic.vibrateSubtle()
                                                    onNextNavStep()
                                                },
                                                enabled = currentStepIndex < steps.size - 1,
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.NavigateNext,
                                                    contentDescription = "Next step",
                                                    tint = if (currentStepIndex < steps.size - 1) Color.White else Color.White.copy(alpha = 0.4f),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }

                                        Button(
                                            onClick = {
                                                haptic.vibrateClick()
                                                onToggleLiveNav()
                                            },
                                            shape = RoundedCornerShape(16.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.White,
                                                contentColor = StatusGreen
                                            ),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                            modifier = Modifier.height(32.dp)
                                        ) {
                                            Icon(Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Stop Live", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        } else {
                            // Static Route Overview + Start Live Guidance CTA
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "$distanceText • $durationText",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = SignalRed,
                                            fontSize = 14.sp
                                        )
                                    )
                                    Text(
                                        text = if (routeRes != null && routeRes.isCorridorFallback) "Gebeta Corridor Route" else "Gebeta Real-Time Routing",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 11.sp
                                        )
                                    )
                                }

                                Button(
                                    onClick = {
                                        haptic.vibrateClick()
                                        onToggleLiveNav()
                                    },
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = StatusGreen,
                                        contentColor = Color.White
                                    ),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                    modifier = Modifier.height(38.dp)
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Start Navigation", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }

                        // Expandable Waypoints & External Nav Links
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isNavStepsExpanded = !isNavStepsExpanded }
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Route Waypoints (${routeRes?.steps?.size ?: 3} steps)",
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
                                    .padding(top = 6.dp),
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
                                        Text("Gebeta Web", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Selected Church Floating Bottom Preview Card (Only visible when NOT routing)
        AnimatedVisibility(
            visible = uiState.routeToChurch == null && uiState.isMapView && uiState.selectedChurch != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 80.dp)
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

        // Full Screen Search Animation Overlay Loader triggered on search submission
        if (uiState.isSearchingLoading) {
            FullScreenSearchOverlayLoader(
                searchQuery = uiState.searchQuery,
                resultCount = uiState.filteredChurches.size,
                onDismiss = onDismissSearchLoader
            )
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
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
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
                    val category = getChurchMarkerCategory(item.church.churchType, item.hasActiveGubae)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        // Category Badge Pill
                        MekanatTag(text = "${category.titleAmharic} • ${category.title.uppercase()}")

                        if (item.hasActiveGubae) {
                            MekanatLiveBanner(
                                text = "LIVE GUBAE",
                                modifier = Modifier
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MekanatDistancePill(distanceText = "${String.format("%.1f", item.distanceKm)} km")
                Text(
                    text = item.church.diocese,
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
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Tabot: ${tabot.name} (${tabot.nameEnglish})",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
                item.nextNigsFormatted?.let { nigs ->
                    Spacer(modifier = Modifier.height(4.dp))
                    MekanatNigsTag(text = "Next Nigs: $nigs")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: View Details & Route
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MekanatSecondaryButton(
                    text = "Details",
                    onClick = onViewDetail,
                    modifier = Modifier.weight(1f)
                )

                MekanatRouteButton(
                    text = "▲ Start route",
                    onClick = onStartRoute,
                    modifier = Modifier.weight(1f)
                )
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
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                        MekanatLiveBanner(
                            text = "LIVE GUBAE",
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
                    onClick = { onToggleFav(isFavorite) },
                    modifier = Modifier.size(32.dp)
                ) {
                    MekanatIconBookmarks(
                        tint = if (isFavorite) BrandEmber else MaterialTheme.colorScheme.onSurfaceVariant,
                        filled = isFavorite,
                        size = 20.dp
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

            // Footer Row: Distance Pill & Route Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MekanatDistancePill(distanceText = "${String.format("%.1f", item.distanceKm)} km away")

                MekanatRouteButton(
                    text = "▲ Route",
                    onClick = onRoute,
                    modifier = Modifier.height(38.dp)
                )
            }
        }
    }
}
