package com.example.mekanat_new.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.mekanat_new.data.model.ChurchWithDistance
import com.example.mekanat_new.data.model.UpcomingNigs
import com.example.mekanat_new.data.util.EthiopianCalendar
import com.example.mekanat_new.ui.theme.BorderDark
import com.example.mekanat_new.ui.theme.BorderLight
import com.example.mekanat_new.ui.theme.SignalRed
import com.example.mekanat_new.ui.theme.SignalRedSubtle
import com.example.mekanat_new.ui.viewmodel.MekanatUiState
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

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

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Header
        item {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Liturgical Calendar",
                                style = MaterialTheme.typography.titleLarge.copy(
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

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { onResetToToday() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.CalendarToday,
                                    contentDescription = "Today",
                                    tint = SignalRed,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Today",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
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
                                            .clickable { onSelectDate(cellDate) },
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
                                                        .size(4.dp)
                                                        .background(
                                                            if (isSelected) MaterialTheme.colorScheme.onPrimary else SignalRed,
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

        // Selected Date Panel
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Selected Day: ${selectedEthDate.formatAmharic()}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${uiState.selectedCalendarDate.month.name.take(3)} ${uiState.selectedCalendarDate.dayOfMonth}, ${uiState.selectedCalendarDate.year} (Gregorian)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.5.sp
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (selectedDateCelebrations.isNotEmpty()) SignalRed else MaterialTheme.colorScheme.surface
                        ) {
                            Text(
                                text = if (selectedDateCelebrations.isNotEmpty()) "${selectedDateCelebrations.size} Feast(s)" else "No Nigs",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedDateCelebrations.isNotEmpty()) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                if (selectedDateCelebrations.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
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
                }
            }
        }

        // Section: Upcoming Nigs (Bucketed into This week, This month, Next month)
        item {
            Text(
                text = "Upcoming Nigs (Celebration Days)",
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
                                    .background(SignalRed, CircleShape)
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenChurchDetail() }
            .testTag("nigs_card_${nigs.tabot.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${nigs.tabot.name} (${nigs.tabot.nameEnglish})",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${nigs.church.name} • ${nigs.church.diocese}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (nigs.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save Nigs",
                        tint = if (nigs.isSaved) SignalRed else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (nigs.daysUntil == 0) SignalRed else MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, if (nigs.daysUntil == 0) SignalRed else MaterialTheme.colorScheme.outline)
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

                Button(
                    onClick = onStartRoute,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SignalRed,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(
                        Icons.Default.Directions,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Route", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
