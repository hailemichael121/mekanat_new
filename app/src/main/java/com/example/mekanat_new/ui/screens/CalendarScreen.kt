package com.example.mekanat_new.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mekanat_new.data.local.ChurchEntity
import com.example.mekanat_new.data.model.ChurchWithDistance
import com.example.mekanat_new.data.model.UpcomingNigs
import com.example.mekanat_new.data.util.EthiopianCalendar
import com.example.mekanat_new.ui.components.MekanatIconBookmarks
import com.example.mekanat_new.ui.components.MekanatIconCalendar
import com.example.mekanat_new.ui.components.MekanatRouteButton
import com.example.mekanat_new.ui.theme.BrandEmber
import com.example.mekanat_new.ui.theme.SignalRed
import com.example.mekanat_new.ui.theme.WayfindingTeal
import com.example.mekanat_new.ui.viewmodel.MekanatUiState
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    uiState: MekanatUiState,
    onSelectDate: (LocalDate) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onResetToToday: () -> Unit,
    onOpenChurchDetail: (Long) -> Unit,
    onStartRoute: (ChurchWithDistance) -> Unit,
    onToggleTabotBookmark: (Long, Boolean) -> Unit,
    onAddNigsFeast: (churchId: Long, nameAmharic: String, nameEnglish: String, nigsMonth: Int, nigsDay: Int, detail: String?, routingDescription: String?) -> Unit = { _, _, _, _, _, _, _ -> },
    onQuickCreateChurchAndAddNigs: (churchName: String, churchAmharic: String?, diocese: String, region: String, churchType: String, latitude: Double, longitude: Double, tabotNameAmharic: String, tabotNameEnglish: String, nigsMonth: Int, nigsDay: Int, detail: String?, routingDescription: String?) -> Unit = { _, _, _, _, _, _, _, _, _, _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    val currentMonth = uiState.calendarMonth
    val today = LocalDate.now()
    val todayEth = EthiopianCalendar.todayEth()

    // Days in current month
    val firstDayOfMonth = currentMonth.withDayOfMonth(1)
    val dayOfWeekOffset = (firstDayOfMonth.dayOfWeek.value % 7) // Sunday=0, Monday=1...
    val lengthOfMonth = currentMonth.lengthOfMonth()

    // Find which dates in this month have celebrations
    val celebrationsByDate = remember(uiState.allUpcomingNigs, currentMonth) {
        uiState.allUpcomingNigs.groupBy { it.nextDate }
    }

    val selectedDateCelebrations = celebrationsByDate[uiState.selectedCalendarDate] ?: emptyList()
    val selectedEthDate = EthiopianCalendar.fromGregorian(uiState.selectedCalendarDate)

    var showAddNigsSheet by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Header
        item {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Liturgical Calendar",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = "Ethiopian Church Feasts & Nigs Schedule",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                modifier = Modifier.clickable { onResetToToday() }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    MekanatIconCalendar(
                                        tint = BrandEmber,
                                        size = 14.dp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Today",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.5.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Month Navigation Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onPreviousMonth, modifier = Modifier.size(36.dp)) {
                            Icon(
                                Icons.Default.ArrowBackIosNew,
                                contentDescription = "Previous Month",
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)} ${currentMonth.year}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            val ethMid = EthiopianCalendar.fromGregorian(currentMonth.withDayOfMonth(15))
                            Text(
                                text = "${ethMid.monthNameAmharic} (${ethMid.monthName}) ${ethMid.year} ዓ.ም.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        IconButton(onClick = onNextMonth, modifier = Modifier.size(36.dp)) {
                            Icon(
                                Icons.Default.ArrowForwardIos,
                                contentDescription = "Next Month",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Weekday Labels
                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                            Text(
                                text = day,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Calendar Grid (42 cells: 6 rows x 7 cols)
                    val totalSlots = 35.coerceAtLeast(((lengthOfMonth + dayOfWeekOffset + 6) / 7) * 7)
                    for (row in 0 until (totalSlots / 7)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            for (col in 0 until 7) {
                                val slotIndex = row * 7 + col
                                val dayNum = slotIndex - dayOfWeekOffset + 1

                                if (dayNum in 1..lengthOfMonth) {
                                    val cellDate = currentMonth.withDayOfMonth(dayNum)
                                    val cellEth = EthiopianCalendar.fromGregorian(cellDate)
                                    val isSelected = cellDate == uiState.selectedCalendarDate
                                    val isToday = cellDate == today
                                    val hasCelebration = celebrationsByDate.containsKey(cellDate)

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(2.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                when {
                                                    isSelected -> MaterialTheme.colorScheme.primary
                                                    isToday -> MaterialTheme.colorScheme.surfaceVariant
                                                    else -> Color.Transparent
                                                }
                                            )
                                            .border(
                                                width = if (isToday && !isSelected) 1.dp else 0.dp,
                                                color = MaterialTheme.colorScheme.outline,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable { onSelectDate(cellDate) }
                                            .testTag("calendar_day_$dayNum"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "$dayNum",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                                    else MaterialTheme.colorScheme.onSurface,
                                                    fontSize = 13.sp
                                                )
                                            )
                                            Text(
                                                text = "${cellEth.day}",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontSize = 9.sp,
                                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            )
                                            if (hasCelebration) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(4.5.dp)
                                                        .background(
                                                            if (isSelected) MaterialTheme.colorScheme.onPrimary else BrandEmber,
                                                            CircleShape
                                                        )
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    Spacer(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Selected Date Panel with Feasts on that Date & Add Nigs Button
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Place,
                                        contentDescription = null,
                                        tint = BrandEmber,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = selectedEthDate.formatAmharic(),
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                }
                                Text(
                                    text = "${uiState.selectedCalendarDate.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)} ${uiState.selectedCalendarDate.dayOfMonth}, ${uiState.selectedCalendarDate.year} (Gregorian)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.5.sp
                                    )
                                )
                            }

                            Button(
                                onClick = { showAddNigsSheet = true },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BrandEmber,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .height(36.dp)
                                    .testTag("add_nigs_button")
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Add Nigs",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Nigs Feasts on this specific clicked date
                        if (selectedDateCelebrations.isNotEmpty()) {
                            Text(
                                text = "Nigs Feasts on this date (${selectedDateCelebrations.size}):",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BrandEmber
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            selectedDateCelebrations.forEach { nigs ->
                                NigsFeastCard(
                                    nigs = nigs,
                                    onOpenChurchDetail = { onOpenChurchDetail(nigs.church.id) },
                                    onStartRoute = {
                                        val match = uiState.nearbyChurches.find { it.church.id == nigs.church.id }
                                            ?: ChurchWithDistance(nigs.church, 0.0)
                                        onStartRoute(match)
                                    },
                                    onToggleBookmark = { onToggleTabotBookmark(nigs.tabot.id, nigs.isSaved) }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "No Nigs Recorded for ${selectedEthDate.monthNameAmharic} ${selectedEthDate.day}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        )
                                        Text(
                                            text = "Know an annual feast or Tabot celebration on this day? Tap '+ Add Nigs' above to contribute!",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: All Upcoming Nigs
        item {
            Text(
                text = "All Upcoming Nigs (Celebration Days)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        listOf("This week", "This month", "Next month").forEach { bucketTitle ->
            val bucketList = uiState.bucketedNigs[bucketTitle] ?: emptyList()
            if (bucketList.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(BrandEmber, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "$bucketTitle (${bucketList.size})",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        bucketList.forEach { nigs ->
                            NigsFeastCard(
                                nigs = nigs,
                                onOpenChurchDetail = { onOpenChurchDetail(nigs.church.id) },
                                onStartRoute = {
                                    val match = uiState.nearbyChurches.find { it.church.id == nigs.church.id }
                                        ?: ChurchWithDistance(nigs.church, 0.0)
                                    onStartRoute(match)
                                },
                                onToggleBookmark = { onToggleTabotBookmark(nigs.tabot.id, nigs.isSaved) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }

    // Add Nigs Modal Sheet Flow
    if (showAddNigsSheet) {
        AddNigsBottomSheet(
            initialEthMonth = selectedEthDate.month,
            initialEthDay = selectedEthDate.day,
            allChurches = uiState.nearbyChurches.map { it.church },
            onDismiss = { showAddNigsSheet = false },
            onAddExistingChurchNigs = { churchId, nameAm, nameEn, month, day, detail, routing ->
                onAddNigsFeast(churchId, nameAm, nameEn, month, day, detail, routing)
                showAddNigsSheet = false
            },
            onAddNewChurchAndNigs = { churchName, churchAm, diocese, region, type, lat, lng, nameAm, nameEn, month, day, detail, routing ->
                onQuickCreateChurchAndAddNigs(churchName, churchAm, diocese, region, type, lat, lng, nameAm, nameEn, month, day, detail, routing)
                showAddNigsSheet = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddNigsBottomSheet(
    initialEthMonth: Int,
    initialEthDay: Int,
    allChurches: List<ChurchEntity>,
    onDismiss: () -> Unit,
    onAddExistingChurchNigs: (churchId: Long, nameAm: String, nameEn: String, month: Int, day: Int, detail: String?, routing: String?) -> Unit,
    onAddNewChurchAndNigs: (churchName: String, churchAm: String?, diocese: String, region: String, type: String, lat: Double, lng: Double, nameAm: String, nameEn: String, month: Int, day: Int, detail: String?, routing: String?) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Form fields
    var tabotNameAmharic by remember { mutableStateOf("") }
    var tabotNameEnglish by remember { mutableStateOf("") }
    var ethMonth by remember { mutableIntStateOf(initialEthMonth) }
    var ethDay by remember { mutableIntStateOf(initialEthDay) }
    var detailText by remember { mutableStateOf("") }
    var routingDescription by remember { mutableStateOf("") }

    // Church selection state
    var churchSearchQuery by remember { mutableStateOf("") }
    var selectedChurch by remember { mutableStateOf<ChurchEntity?>(null) }
    var isAddingNewChurchMode by remember { mutableStateOf(false) }

    // New Church form fields
    var newChurchName by remember { mutableStateOf("") }
    var newChurchAmharic by remember { mutableStateOf("") }
    var newDiocese by remember { mutableStateOf("Addis Ababa Diocese") }
    var newRegion by remember { mutableStateOf("Addis Ababa") }
    var newChurchType by remember { mutableStateOf("Parish Church") }
    var newLatitude by remember { mutableDoubleStateOf(9.03) }
    var newLongitude by remember { mutableDoubleStateOf(38.74) }

    // Filter churches from map based on search query
    val matchingChurches = remember(churchSearchQuery, allChurches) {
        if (churchSearchQuery.isBlank()) allChurches.take(6)
        else allChurches.filter {
            it.name.contains(churchSearchQuery, ignoreCase = true) ||
            (it.nameAmharic?.contains(churchSearchQuery, ignoreCase = true) == true) ||
            it.diocese.contains(churchSearchQuery, ignoreCase = true) ||
            it.region.contains(churchSearchQuery, ignoreCase = true)
        }
    }

    // Common Tabot Presets for fast tapping
    val tabotPresets = listOf(
        "ቅዱስ ሚካኤል" to "St. Michael",
        "ቅድስት ማርያም" to "St. Mary",
        "ቅዱስ ጊዮርጊስ" to "St. George",
        "መድኃኔ ዓለም" to "Medhane Alem",
        "ተክለ ሃይማኖት" to "Tekle Haymanot",
        "በዓለ ወልድ" to "Beale Weld",
        "ገብረ መንፈስ ቅዱስ" to "Abo",
        "ኪዳነ ምሕረት" to "Kidane Mehret",
        "አማኑኤል" to "Emmanuel",
        "ቅዱስ ገብርኤል" to "St. Gabriel",
        "ቅዱስ ዮሐንስ" to "St. John",
        "ቅዱስ ዑራኤል" to "St. Urael"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("add_nigs_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Add Nigs Celebration",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "Register Tabot feast, sanctuary & pilgrimage route",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 1. Feast / Tabot Preset Selection
            Text(
                text = "1. Tabot Feast / Name",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                tabotPresets.forEach { (am, en) ->
                    val isSelected = tabotNameAmharic == am || tabotNameEnglish == en
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            tabotNameAmharic = am
                            tabotNameEnglish = en
                        },
                        label = {
                            Text(
                                text = "$am ($en)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.5.sp
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandEmber,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = tabotNameAmharic,
                    onValueChange = { tabotNameAmharic = it },
                    label = { Text("Name (Amharic)") },
                    placeholder = { Text("e.g. ቅዱስ ሚካኤል") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = tabotNameEnglish,
                    onValueChange = { tabotNameEnglish = it },
                    label = { Text("Name (English)") },
                    placeholder = { Text("e.g. St. Michael") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Ethiopian Date Selection
            Text(
                text = "2. Ethiopian Feast Date (ወር እና ቀን)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Month Picker (1 to 13)
                OutlinedTextField(
                    value = ethMonth.toString(),
                    onValueChange = { str ->
                        val m = str.toIntOrNull()
                        if (m != null && m in 1..13) ethMonth = m
                    },
                    label = { Text("Month (1-13)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                // Day Picker (1 to 30)
                OutlinedTextField(
                    value = ethDay.toString(),
                    onValueChange = { str ->
                        val d = str.toIntOrNull()
                        if (d != null && d in 1..30) ethDay = d
                    },
                    label = { Text("Day (1-30)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            val monthAm = EthiopianCalendar.monthNamesAmharic.getOrElse(ethMonth - 1) { "" }
            val monthEn = EthiopianCalendar.monthNames.getOrElse(ethMonth - 1) { "" }
            Text(
                text = "👉 Selected Date: $monthAm ($monthEn) $ethDay",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = BrandEmber,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Church Sanctuary Selection from Map
            Text(
                text = "3. Church / Sanctuary (Search from Map)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))

            if (!isAddingNewChurchMode) {
                if (selectedChurch != null) {
                    // Selected Church Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.5.dp, BrandEmber),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(
                                    Icons.Default.Church,
                                    contentDescription = null,
                                    tint = BrandEmber,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = selectedChurch!!.name,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "${selectedChurch!!.diocese} • ${selectedChurch!!.churchType}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }

                            IconButton(onClick = { selectedChurch = null }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Change church",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    // Search church input
                    OutlinedTextField(
                        value = churchSearchQuery,
                        onValueChange = { churchSearchQuery = it },
                        label = { Text("Search sanctuary name on map") },
                        placeholder = { Text("e.g. Debre Libanos, Urael, Kidist Maryam") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = BrandEmber)
                        },
                        trailingIcon = {
                            if (churchSearchQuery.isNotEmpty()) {
                                IconButton(onClick = { churchSearchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Matching search results
                    if (matchingChurches.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(6.dp)) {
                                matchingChurches.take(4).forEach { church ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable {
                                                selectedChurch = church
                                                churchSearchQuery = ""
                                            }
                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Church,
                                            contentDescription = null,
                                            tint = WayfindingTeal,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = church.name,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "${church.diocese} • ${church.region}",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    fontSize = 11.sp
                                                )
                                            )
                                        }
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Select",
                                            tint = BrandEmber,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Suggest adding place if not in map
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BrandEmber.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, BrandEmber.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (churchSearchQuery.isNotBlank()) "Can't find '$churchSearchQuery' on map?" else "Can't find your sanctuary?",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Suggest and add this holy place to the map first!",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Button(
                                onClick = {
                                    isAddingNewChurchMode = true
                                    if (churchSearchQuery.isNotBlank()) {
                                        newChurchName = churchSearchQuery
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BrandEmber,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("+ Add Place", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }
            } else {
                // INLINE ADD PLACE FLOW
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = BorderStroke(1.2.dp, BrandEmber),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Place, contentDescription = null, tint = BrandEmber)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Add New Sanctuary First",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            TextButton(onClick = { isAddingNewChurchMode = false }) {
                                Text("Use Existing", style = MaterialTheme.typography.labelSmall.copy(color = BrandEmber))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = newChurchName,
                            onValueChange = { newChurchName = it },
                            label = { Text("Sanctuary / Church Name (English) *") },
                            placeholder = { Text("e.g. Debre Libanos St. Tekle Haymanot") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = newChurchAmharic,
                            onValueChange = { newChurchAmharic = it },
                            label = { Text("Sanctuary Name (Amharic)") },
                            placeholder = { Text("e.g. ደብረ ሊባኖስ አቡነ ተክለ ሃይማኖት አንድነት ገዳም") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = newDiocese,
                                onValueChange = { newDiocese = it },
                                label = { Text("Diocese / ሀገረ ስብከት") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = newRegion,
                                onValueChange = { newRegion = it },
                                label = { Text("Region / ክልል") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Church Type Selector Chips
                        Text(
                            text = "Sanctuary Type:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf("Parish Church", "Ancient Monastery", "Patriarchal Cathedral", "Rock-Hewn Sanctuary").forEach { type ->
                                FilterChip(
                                    selected = newChurchType == type,
                                    onClick = { newChurchType = type },
                                    label = { Text(type, style = MaterialTheme.typography.labelSmall) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = WayfindingTeal,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Spiritual / Celebration Details
            Text(
                text = "4. Celebration Details (መግለጫ)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = detailText,
                onValueChange = { detailText = it },
                label = { Text("Celebration & Mahlet Details") },
                placeholder = { Text("e.g. Mahlet starts at 10:00 PM, Holy Tabot procession at 7:00 AM, Tsebel holy water blessing") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 72.dp),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Routing / Pilgrimage Directions
            Text(
                text = "5. Pilgrimage & Route Directions (የጉዞ አቅጣጫ)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = routingDescription,
                onValueChange = { routingDescription = it },
                label = { Text("Routing, Shuttles & Trails Directions") },
                placeholder = { Text("e.g. Take minibus from Megenagna terminal toward Entoto, alight at gate 2 and follow foot trail 500m") },
                leadingIcon = {
                    Icon(Icons.Default.DirectionsWalk, contentDescription = null, tint = WayfindingTeal)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 72.dp),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Save Action Buttons
            val canSubmit = (tabotNameEnglish.isNotBlank() || tabotNameAmharic.isNotBlank()) &&
                    (selectedChurch != null || (isAddingNewChurchMode && newChurchName.isNotBlank()))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        if (isAddingNewChurchMode) {
                            onAddNewChurchAndNigs(
                                newChurchName,
                                newChurchAmharic,
                                newDiocese,
                                newRegion,
                                newChurchType,
                                newLatitude,
                                newLongitude,
                                tabotNameAmharic,
                                tabotNameEnglish,
                                ethMonth,
                                ethDay,
                                detailText,
                                routingDescription
                            )
                        } else if (selectedChurch != null) {
                            onAddExistingChurchNigs(
                                selectedChurch!!.id,
                                tabotNameAmharic,
                                tabotNameEnglish,
                                ethMonth,
                                ethDay,
                                detailText,
                                routingDescription
                            )
                        }
                    },
                    enabled = canSubmit,
                    modifier = Modifier
                        .weight(1.4f)
                        .height(48.dp)
                        .testTag("submit_nigs_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandEmber,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAddingNewChurchMode) "Add Place & Nigs" else "Save Nigs Feast",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun NigsFeastCard(
    nigs: UpcomingNigs,
    onOpenChurchDetail: () -> Unit,
    onStartRoute: () -> Unit,
    onToggleBookmark: () -> Unit
) {
    val ethMonthName = EthiopianCalendar.monthNames.getOrElse(nigs.tabot.nigsMonth - 1) { "Month" }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenChurchDetail() }
            .testTag("nigs_card_${nigs.tabot.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${nigs.tabot.name} (${nigs.tabot.nameEnglish})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${nigs.church.name} • ${nigs.church.diocese}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.size(32.dp)
                ) {
                    MekanatIconBookmarks(
                        tint = if (nigs.isSaved) BrandEmber else MaterialTheme.colorScheme.onSurfaceVariant,
                        filled = nigs.isSaved,
                        size = 20.dp
                    )
                }
            }

            if (!nigs.tabot.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = nigs.tabot.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (!nigs.tabot.routingDescription.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.DirectionsWalk,
                        contentDescription = "Pilgrimage Route",
                        tint = WayfindingTeal,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = nigs.tabot.routingDescription,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = WayfindingTeal,
                            fontSize = 11.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (nigs.daysUntil == 0) BrandEmber else MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, if (nigs.daysUntil == 0) BrandEmber else MaterialTheme.colorScheme.outline)
                ) {
                    Text(
                        text = if (nigs.daysUntil == 0) "TODAY'S FEAST!" else "In ${nigs.daysUntil} days ($ethMonthName ${nigs.tabot.nigsDay})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (nigs.daysUntil == 0) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                MekanatRouteButton(
                    text = "▲ Route",
                    onClick = onStartRoute,
                    modifier = Modifier.height(34.dp)
                )
            }
        }
    }
}

