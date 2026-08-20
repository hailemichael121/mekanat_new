package com.example.mekanat_new.data.model

import com.example.mekanat_new.data.local.ChurchEntity
import com.example.mekanat_new.data.local.GubaeEventEntity
import com.example.mekanat_new.data.local.TabotEntity
import java.time.LocalDate

data class ChurchWithDistance(
    val church: ChurchEntity,
    val distanceKm: Double,
    val hasActiveGubae: Boolean = false,
    val primaryTabot: TabotEntity? = null,
    val nextNigsFormatted: String? = null
)

data class SearchResult(
    val church: ChurchEntity,
    val distanceKm: Double,
    val matchedTabot: TabotEntity?,
    val nextNigsDate: LocalDate?,
    val hasActiveGubae: Boolean = false
)

data class ChurchDetail(
    val church: ChurchEntity,
    val tabots: List<TabotEntity>,
    val activeGubae: List<GubaeEventEntity>,
    val historyGubae: List<GubaeEventEntity>,
    val isFavorite: Boolean = false
)

data class UpcomingNigs(
    val tabot: TabotEntity,
    val church: ChurchEntity,
    val nextDate: LocalDate,
    val daysUntil: Int,
    val isSaved: Boolean = false
)

enum class ChurchType(val label: String) {
    ROCK_HEWN("Rock-Hewn Sanctuary"),
    MONASTERY("Historic Monastery (ገዳም)"),
    CATHEDRAL("Patriarchal Cathedral"),
    PARISH("Parish Church (ደብር)")
}
