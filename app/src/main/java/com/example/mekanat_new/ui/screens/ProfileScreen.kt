package com.example.mekanat_new.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mekanat_new.data.local.ChurchEntity
import com.example.mekanat_new.ui.components.MekanatCrossIcon
import com.example.mekanat_new.ui.components.MekanatIconBookmarks
import com.example.mekanat_new.ui.components.MekanatPrimaryButton
import com.example.mekanat_new.ui.components.MekanatSecondaryButton
import com.example.mekanat_new.ui.components.vibrateClick
import com.example.mekanat_new.ui.components.vibrateSubtle
import com.example.mekanat_new.ui.theme.BrandEmber
import com.example.mekanat_new.ui.theme.CrimsonPulse
import com.example.mekanat_new.ui.theme.GoldFlame
import com.example.mekanat_new.ui.theme.MekanatDataTypography
import com.example.mekanat_new.ui.theme.StatusAmber
import com.example.mekanat_new.ui.theme.StatusGreen
import com.example.mekanat_new.ui.theme.ThemeMode
import com.example.mekanat_new.ui.theme.WayfindingTeal
import com.example.mekanat_new.ui.viewmodel.MekanatUiState

@Composable
fun ProfileScreen(
    uiState: MekanatUiState,
    onOpenAddChurch: () -> Unit,
    onSetThemeMode: (ThemeMode) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Collapsible section states (collapsed by default to keep UI clean)
    var isMapPrefsExpanded by remember { mutableStateOf(false) }
    var isNotifPrefsExpanded by remember { mutableStateOf(false) }
    var isStoragePrefsExpanded by remember { mutableStateOf(false) }
    var isDeveloperPrefsExpanded by remember { mutableStateOf(false) }

    // User preference values
    var isVoiceGuidanceEnabled by remember { mutableStateOf(true) }
    var isAvoidUnpavedEnabled by remember { mutableStateOf(false) }
    var useKilometers by remember { mutableStateOf(true) }

    var isLiveGubaeNotifEnabled by remember { mutableStateOf(true) }
    var isNigsFeastNotifEnabled by remember { mutableStateOf(true) }
    var isTabotNotifEnabled by remember { mutableStateOf(true) }

    var showApiKeyDialog by remember { mutableStateOf(false) }
    var showSignInDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(start = 18.dp, top = 12.dp, end = 18.dp, bottom = 86.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Bar Header (Handle & Actions)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Guest",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "guest_pilgrim",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = BrandEmber.copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BrandEmber.copy(alpha = 0.4f)),
                        modifier = Modifier.padding(start = 2.dp)
                    ) {
                        Text(
                            text = "Guest",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandEmber
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = {
                            haptic.vibrateClick()
                            val shareIntent = android.content.Intent().apply {
                                action = android.content.Intent.ACTION_SEND
                                putExtra(android.content.Intent.EXTRA_TEXT, "Explore Ethiopian Orthodox Sanctuaries & Pilgrimages with Mekanat.")
                                type = "text/plain"
                            }
                            context.startActivity(android.content.Intent.createChooser(shareIntent, "Share Mekanat"))
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Avatar & Stats Header Row
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Avatar with story ring
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(82.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            BrandEmber,
                                            BrandEmber.copy(alpha = 0.4f)
                                        )
                                    )
                                )
                                .padding(2.5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surface),
                                contentAlignment = Alignment.Center
                            ) {
                                MekanatCrossIcon(
                                    tint = BrandEmber,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }

                        // Small status icon overlay
                        Surface(
                            shape = CircleShape,
                            color = BrandEmber,
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(22.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // 3 Stats Columns: Saved, Feasts, Contributions (with IBM Plex Mono numbers)
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileStatItem(
                            count = "${uiState.favoriteChurches.size}",
                            label = "Saved"
                        )
                        ProfileStatItem(
                            count = "${uiState.savedNigs.size}",
                            label = "Feasts"
                        )
                        ProfileStatItem(
                            count = "${uiState.mySubmissions.size}",
                            label = "Submissions"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bio & Identity section
                Text(
                    text = "Guest Pilgrim",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    text = "መካናት • EOTC Sanctuary Registry",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = BrandEmber,
                        fontSize = 12.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Exploring ancient rock-hewn monasteries, sacred tabots, and liturgical feasts across Ethiopia 🇪🇹 ⛪",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.5.sp,
                        lineHeight = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MekanatPrimaryButton(
                        text = "Sign In / Join",
                        onClick = {
                            haptic.vibrateClick()
                            showSignInDialog = true
                        },
                        modifier = Modifier.weight(1f),
                        testTag = "btn_sign_in"
                    )

                    MekanatSecondaryButton(
                        text = "Add Church",
                        onClick = {
                            haptic.vibrateClick()
                            onOpenAddChurch()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        modifier = Modifier.weight(1f),
                        testTag = "add_church_button"
                    )
                }
            }
        }

        // ONE-LINER Appearance & Theme Toggle: Text on left, 3 clean icon buttons on right
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            tint = BrandEmber,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Appearance & Theme",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    // 3 Icon buttons on the right: Light (Sun), Dark (Moon), System (Mobile/Auto)
                    Row(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                RoundedCornerShape(10.dp)
                            )
                            .padding(3.dp),
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ThemeIconButton(
                            icon = Icons.Default.LightMode,
                            contentDescription = "Light Mode",
                            isSelected = uiState.themeMode == ThemeMode.LIGHT,
                            onClick = {
                                haptic.vibrateSubtle()
                                onSetThemeMode(ThemeMode.LIGHT)
                            },
                            testTag = "theme_icon_light"
                        )

                        ThemeIconButton(
                            icon = Icons.Default.DarkMode,
                            contentDescription = "Dark Mode",
                            isSelected = uiState.themeMode == ThemeMode.DARK,
                            onClick = {
                                haptic.vibrateSubtle()
                                onSetThemeMode(ThemeMode.DARK)
                            },
                            testTag = "theme_icon_dark"
                        )

                        ThemeIconButton(
                            icon = Icons.Default.BrightnessAuto,
                            contentDescription = "System Auto",
                            isSelected = uiState.themeMode == ThemeMode.SYSTEM,
                            onClick = {
                                haptic.vibrateSubtle()
                                onSetThemeMode(ThemeMode.SYSTEM)
                            },
                            testTag = "theme_icon_system"
                        )
                    }
                }
            }
        }

        // ==========================================
        // COLLAPSIBLE SETTINGS INTERFACES
        // ==========================================

        // 1. Navigation & Map Preferences (Collapsible Accordion)
        item {
            CollapsibleSettingsCard(
                title = "Map & Navigation Settings",
                icon = Icons.Default.Map,
                isExpanded = isMapPrefsExpanded,
                onToggleExpand = {
                    haptic.vibrateSubtle()
                    isMapPrefsExpanded = !isMapPrefsExpanded
                }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    PreferenceSwitchRow(
                        title = "Voice Navigation Guidance",
                        subtitle = "Spoken turn-by-turn prompts during active routing",
                        icon = Icons.Default.VolumeUp,
                        checked = isVoiceGuidanceEnabled,
                        onCheckedChange = {
                            isVoiceGuidanceEnabled = it
                            haptic.vibrateSubtle()
                        }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                    PreferenceSwitchRow(
                        title = "Avoid Unpaved Mountain Corridors",
                        subtitle = "Prioritize asphalt highway routes when calculating directions",
                        icon = Icons.Default.Directions,
                        checked = isAvoidUnpavedEnabled,
                        onCheckedChange = {
                            isAvoidUnpavedEnabled = it
                            haptic.vibrateSubtle()
                        }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Distance Units",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = if (useKilometers) "Metric (Kilometers / km)" else "Imperial (Miles / mi)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.5.sp
                                )
                            )
                        }

                        Row(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(2.dp)
                        ) {
                            TextButton(
                                onClick = {
                                    haptic.vibrateSubtle()
                                    useKilometers = true
                                },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = if (useKilometers) BrandEmber else Color.Transparent,
                                    contentColor = if (useKilometers) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("KM", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            TextButton(
                                onClick = {
                                    haptic.vibrateSubtle()
                                    useKilometers = false
                                },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = if (!useKilometers) BrandEmber else Color.Transparent,
                                    contentColor = if (!useKilometers) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("MI", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 2. Feasts & Liturgical Alerts (Collapsible Accordion)
        item {
            CollapsibleSettingsCard(
                title = "Sanctuary & Feasts Alerts",
                icon = Icons.Default.NotificationsActive,
                isExpanded = isNotifPrefsExpanded,
                onToggleExpand = {
                    haptic.vibrateSubtle()
                    isNotifPrefsExpanded = !isNotifPrefsExpanded
                }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    PreferenceSwitchRow(
                        title = "Live Spiritual Gubae Notifications",
                        subtitle = "Alert when a nearby sanctuary broadcasts live sermons",
                        icon = Icons.Default.Notifications,
                        checked = isLiveGubaeNotifEnabled,
                        onCheckedChange = {
                            isLiveGubaeNotifEnabled = it
                            haptic.vibrateSubtle()
                        }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                    PreferenceSwitchRow(
                        title = "Annual Nigs Feast Reminders",
                        subtitle = "Get notified 1 day before major regional pilgrimages",
                        icon = Icons.Default.Event,
                        checked = isNigsFeastNotifEnabled,
                        onCheckedChange = {
                            isNigsFeastNotifEnabled = it
                            haptic.vibrateSubtle()
                        }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                    PreferenceSwitchRow(
                        title = "Monthly Tabot Commemorations",
                        subtitle = "Daily saint feast reminders from the Ge'ez Synaxarium",
                        icon = Icons.Default.Event,
                        checked = isTabotNotifEnabled,
                        onCheckedChange = {
                            isTabotNotifEnabled = it
                            haptic.vibrateSubtle()
                        }
                    )
                }
            }
        }

        // 3. Storage, Cache & Data Diagnostics (Collapsible Accordion)
        item {
            CollapsibleSettingsCard(
                title = "Data Storage & Diagnostics",
                icon = Icons.Default.Storage,
                isExpanded = isStoragePrefsExpanded,
                onToggleExpand = {
                    haptic.vibrateSubtle()
                    isStoragePrefsExpanded = !isStoragePrefsExpanded
                }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SettingRow("GPS Position", "Addis Ababa (${String.format("%.4f", uiState.userLat)}, ${String.format("%.4f", uiState.userLng)})")
                    SettingRow("Local Database", "Room SQLite (${uiState.nearbyChurches.size} sanctuaries indexed)")
                    SettingRow("Map Cache", "Active Tile In-Memory Cache")
                    SettingRow("Routing Engine", "Gebeta Maps Direction API (Free Tier)")

                    Spacer(modifier = Modifier.height(4.dp))

                    var cacheRefreshed by remember { mutableStateOf(false) }

                    MekanatSecondaryButton(
                        text = if (cacheRefreshed) "Cache Refreshed ✓" else "Refresh Local Map Tiles & Cache",
                        onClick = {
                            haptic.vibrateClick()
                            cacheRefreshed = true
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Cached, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // 4. Gebeta Maps Engine & Developer Config (Collapsible Accordion)
        item {
            CollapsibleSettingsCard(
                title = "Gebeta Maps Engine & Key",
                icon = Icons.Default.Key,
                isExpanded = isDeveloperPrefsExpanded,
                onToggleExpand = {
                    haptic.vibrateSubtle()
                    isDeveloperPrefsExpanded = !isDeveloperPrefsExpanded
                }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Gebeta Direction & Slippy Tile API is active on the free tier with automated Ethiopian corridor fallback routing.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.5.sp,
                            lineHeight = 15.sp
                        )
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                haptic.vibrateSubtle()
                                showApiKeyDialog = true
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "API Key Configuration",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = "View Status",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = BrandEmber,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        // Community Submissions Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Submissions",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${uiState.mySubmissions.size} items",
                    style = MekanatDataTypography.distanceSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
            }
        }

        if (uiState.mySubmissions.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shadowElevation = 0.5.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No church submissions yet. Tap 'Add Church' to contribute.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(uiState.mySubmissions, key = { it.id }) { sub ->
                SubmissionCard(submission = sub)
            }
        }
    }

    // Sign In / Guest Dialog
    if (showSignInDialog) {
        AlertDialog(
            onDismissRequest = { showSignInDialog = false },
            title = {
                Text("Guest Pilgrim Mode", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "You are currently browsing Mekanat in Guest Mode. All sanctuary bookmarks, search history, and offline maps are saved locally to your device.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = { showSignInDialog = false }) {
                    Text("OK", fontWeight = FontWeight.Bold, color = BrandEmber)
                }
            }
        )
    }

    // Gebeta API Key Configuration Dialog
    if (showApiKeyDialog) {
        AlertDialog(
            onDismissRequest = { showApiKeyDialog = false },
            title = {
                Text("Gebeta Routing (Free Tier)", fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(
                        "Mekanat is pre-configured with the Gebeta Maps Direction API free tier. If you have your own Gebeta API key, it is loaded automatically via BuildConfig / .env.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "• Free Tier: Enabled\n• Fallback Corridor: Active\n• Turn-by-Turn Guidance: Supported",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = WayfindingTeal,
                            lineHeight = 16.sp
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showApiKeyDialog = false }) {
                    Text("Done", fontWeight = FontWeight.Bold, color = BrandEmber)
                }
            }
        )
    }
}

@Composable
private fun CollapsibleSettingsCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onToggleExpand
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = BrandEmber,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun PreferenceSwitchRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = BrandEmber,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}

@Composable
private fun ProfileStatItem(
    count: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count,
            style = MekanatDataTypography.statNumber.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        )
    }
}

@Composable
private fun ThemeIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) BrandEmber else Color.Transparent,
        shadowElevation = if (isSelected) 1.dp else 0.dp,
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(17.dp)
            )
        }
    }
}

@Composable
fun SubmissionCard(submission: ChurchEntity) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = submission.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${submission.region} • ${submission.diocese}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Surface(
                shape = RoundedCornerShape(999.dp),
                color = if (submission.isVerified) StatusGreen.copy(alpha = 0.15f) else StatusAmber.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (submission.isVerified) StatusGreen else StatusAmber)
            ) {
                Text(
                    text = if (submission.isVerified) "APPROVED" else "PENDING",
                    style = MekanatDataTypography.badgeSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (submission.isVerified) StatusGreen else StatusAmber,
                        fontSize = 10.sp
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }
    }
}

@Composable
fun SettingRow(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            text = value,
            style = MekanatDataTypography.distanceSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.5.sp
            )
        )
    }
}

