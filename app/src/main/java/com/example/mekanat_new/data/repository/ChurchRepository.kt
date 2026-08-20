package com.example.mekanat_new.data.repository

import com.example.mekanat_new.data.local.ChurchDao
import com.example.mekanat_new.data.local.ChurchEntity
import com.example.mekanat_new.data.local.FavoriteDao
import com.example.mekanat_new.data.local.FavoriteEntity
import com.example.mekanat_new.data.local.GubaeDao
import com.example.mekanat_new.data.local.GubaeEventEntity
import com.example.mekanat_new.data.local.SavedNigsDao
import com.example.mekanat_new.data.local.SavedNigsEntity
import com.example.mekanat_new.data.local.SearchHistoryDao
import com.example.mekanat_new.data.local.SearchHistoryEntity
import com.example.mekanat_new.data.local.TabotDao
import com.example.mekanat_new.data.local.TabotEntity
import com.example.mekanat_new.data.model.ChurchDetail
import com.example.mekanat_new.data.model.ChurchWithDistance
import com.example.mekanat_new.data.model.SearchResult
import com.example.mekanat_new.data.model.UpcomingNigs
import com.example.mekanat_new.data.util.EthDate
import com.example.mekanat_new.data.util.EthiopianCalendar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class ChurchRepository(
    private val churchDao: ChurchDao,
    private val tabotDao: TabotDao,
    private val gubaeDao: GubaeDao,
    private val favoriteDao: FavoriteDao,
    private val savedNigsDao: SavedNigsDao,
    private val searchHistoryDao: SearchHistoryDao
) {

    fun nearby(userLat: Double, userLng: Double): Flow<List<ChurchWithDistance>> =
        combine(
            churchDao.getAllVerified(),
            gubaeDao.getAllActive(),
            tabotDao.getAll()
        ) { churches, activeGubaes, allTabots ->
            val activeChurchIds = activeGubaes.map { it.churchId }.toSet()
            val tabotsByChurch = allTabots.groupBy { it.churchId }
            val today = LocalDate.now()

            churches.map { church ->
                val distance = haversine(userLat, userLng, church.latitude, church.longitude)
                val isLive = activeChurchIds.contains(church.id)
                val churchTabots = tabotsByChurch[church.id] ?: emptyList()
                val primaryTabot = churchTabots.firstOrNull()

                val nextNigsStr = primaryTabot?.let { tabot ->
                    val eth = EthiopianCalendar.fromGregorian(today)
                    var next = EthiopianCalendar.toGregorian(EthDate(eth.year, tabot.nigsMonth, tabot.nigsDay))
                    if (next.isBefore(today)) {
                        next = EthiopianCalendar.toGregorian(EthDate(eth.year + 1, tabot.nigsMonth, tabot.nigsDay))
                    }
                    val days = ChronoUnit.DAYS.between(today, next).toInt()
                    val ethMonthName = EthiopianCalendar.monthNames.getOrElse(tabot.nigsMonth - 1) { "Month" }
                    if (days == 0) "Today! ($ethMonthName ${tabot.nigsDay})"
                    else "$ethMonthName ${tabot.nigsDay} (in $days days)"
                }

                ChurchWithDistance(
                    church = church,
                    distanceKm = distance,
                    hasActiveGubae = isLive,
                    primaryTabot = primaryTabot,
                    nextNigsFormatted = nextNigsStr
                )
            }.sortedBy { it.distanceKm }
        }

    fun search(query: String, userLat: Double, userLng: Double): Flow<List<SearchResult>> =
        combine(
            churchDao.search(query.trim()),
            tabotDao.searchTabots(query.trim()),
            gubaeDao.getAllActive()
        ) { churches, matchingTabots, activeGubaes ->
            val activeChurchIds = activeGubaes.map { it.churchId }.toSet()
            val today = LocalDate.now()
            val matchedChurchIds = churches.map { it.id }.toSet()

            // If a tabot matched, but church wasn't in direct church search, retrieve church
            val tabotChurchMap = mutableMapOf<Long, TabotEntity>()
            matchingTabots.forEach { tabot ->
                tabotChurchMap[tabot.churchId] = tabot
            }

            churches.map { church ->
                val dist = haversine(userLat, userLng, church.latitude, church.longitude)
                val matchedTabot = tabotChurchMap[church.id]

                var nextDate: LocalDate? = null
                if (matchedTabot != null) {
                    val eth = EthiopianCalendar.fromGregorian(today)
                    var next = EthiopianCalendar.toGregorian(EthDate(eth.year, matchedTabot.nigsMonth, matchedTabot.nigsDay))
                    if (next.isBefore(today)) {
                        next = EthiopianCalendar.toGregorian(EthDate(eth.year + 1, matchedTabot.nigsMonth, matchedTabot.nigsDay))
                    }
                    nextDate = next
                }

                SearchResult(
                    church = church,
                    distanceKm = dist,
                    matchedTabot = matchedTabot,
                    nextNigsDate = nextDate,
                    hasActiveGubae = activeChurchIds.contains(church.id)
                )
            }.sortedBy { it.distanceKm }
        }

    fun detail(churchId: Long): Flow<ChurchDetail> =
        combine(
            churchDao.getById(churchId),
            tabotDao.getForChurch(churchId),
            gubaeDao.getActiveForChurch(churchId),
            gubaeDao.getHistoryForChurch(churchId),
            favoriteDao.isFavorite(churchId)
        ) { church, tabots, active, history, isFav ->
            ChurchDetail(
                church = church ?: ChurchEntity(
                    id = churchId,
                    name = "Sanctuary",
                    nameAmharic = "",
                    latitude = 9.0306,
                    longitude = 38.7619,
                    region = "Ethiopia",
                    diocese = "Diocese",
                    churchType = "PARISH",
                    description = "",
                    history = "",
                    address = "",
                    contactPhone = null,
                    contactEmail = null
                ),
                tabots = tabots,
                activeGubae = active,
                historyGubae = history,
                isFavorite = isFav
            )
        }

    fun getFavoriteChurches(userLat: Double, userLng: Double): Flow<List<ChurchWithDistance>> =
        combine(
            favoriteDao.getFavoriteChurches(),
            gubaeDao.getAllActive(),
            tabotDao.getAll()
        ) { churches, activeGubaes, allTabots ->
            val activeChurchIds = activeGubaes.map { it.churchId }.toSet()
            val tabotsByChurch = allTabots.groupBy { it.churchId }
            val today = LocalDate.now()

            churches.map { church ->
                val distance = haversine(userLat, userLng, church.latitude, church.longitude)
                val isLive = activeChurchIds.contains(church.id)
                val primaryTabot = tabotsByChurch[church.id]?.firstOrNull()

                val nextNigsStr = primaryTabot?.let { tabot ->
                    val eth = EthiopianCalendar.fromGregorian(today)
                    var next = EthiopianCalendar.toGregorian(EthDate(eth.year, tabot.nigsMonth, tabot.nigsDay))
                    if (next.isBefore(today)) {
                        next = EthiopianCalendar.toGregorian(EthDate(eth.year + 1, tabot.nigsMonth, tabot.nigsDay))
                    }
                    val days = ChronoUnit.DAYS.between(today, next).toInt()
                    val ethMonthName = EthiopianCalendar.monthNames.getOrElse(tabot.nigsMonth - 1) { "Month" }
                    if (days == 0) "Today! ($ethMonthName ${tabot.nigsDay})"
                    else "$ethMonthName ${tabot.nigsDay} (in $days days)"
                }

                ChurchWithDistance(
                    church = church,
                    distanceKm = distance,
                    hasActiveGubae = isLive,
                    primaryTabot = primaryTabot,
                    nextNigsFormatted = nextNigsStr
                )
            }.sortedBy { it.distanceKm }
        }

    fun getUpcomingNigs(): Flow<List<UpcomingNigs>> =
        combine(
            tabotDao.getAll(),
            churchDao.getAllVerified(),
            savedNigsDao.getAllSavedTabotIds()
        ) { tabots, churches, savedTabotIds ->
            val churchesMap = churches.associateBy { it.id }
            val savedSet = savedTabotIds.toSet()
            val today = LocalDate.now()

            tabots.mapNotNull { tabot ->
                val church = churchesMap[tabot.churchId] ?: return@mapNotNull null
                val eth = EthiopianCalendar.fromGregorian(today)
                var next = EthiopianCalendar.toGregorian(EthDate(eth.year, tabot.nigsMonth, tabot.nigsDay))
                if (next.isBefore(today)) {
                    next = EthiopianCalendar.toGregorian(EthDate(eth.year + 1, tabot.nigsMonth, tabot.nigsDay))
                }
                val daysUntil = ChronoUnit.DAYS.between(today, next).toInt()
                UpcomingNigs(
                    tabot = tabot,
                    church = church,
                    nextDate = next,
                    daysUntil = daysUntil,
                    isSaved = savedSet.contains(tabot.id)
                )
            }.sortedBy { it.daysUntil }
        }

    fun getSavedNigs(): Flow<List<UpcomingNigs>> =
        getUpcomingNigs().map { list -> list.filter { it.isSaved } }

    fun getPendingSubmissions(): Flow<List<ChurchEntity>> = churchDao.getPendingSubmissions()

    suspend fun submitChurch(entity: ChurchEntity): Long =
        churchDao.upsert(entity.copy(isVerified = false))

    suspend fun approveSubmission(church: ChurchEntity) =
        churchDao.update(church.copy(isVerified = true))

    suspend fun toggleFavorite(churchId: Long, isFav: Boolean) {
        if (isFav) {
            favoriteDao.remove(churchId)
        } else {
            favoriteDao.add(FavoriteEntity(churchId = churchId))
        }
    }

    suspend fun toggleSavedNigs(tabotId: Long, isSaved: Boolean) {
        if (isSaved) {
            savedNigsDao.remove(tabotId)
        } else {
            savedNigsDao.add(SavedNigsEntity(tabotId = tabotId))
        }
    }

    suspend fun addTabot(tabot: TabotEntity): Long = tabotDao.insert(tabot)

    suspend fun addGubaeEvent(churchId: Long, title: String, description: String?, startEpoch: Long, endEpoch: Long): Long {
        return gubaeDao.insert(
            GubaeEventEntity(
                churchId = churchId,
                title = title,
                description = description,
                startDateEpoch = startEpoch,
                endDateEpoch = endEpoch,
                isActive = true
            )
        )
    }

    fun getRecentSearches(): Flow<List<SearchHistoryEntity>> = searchHistoryDao.getRecentSearches()

    suspend fun recordSearch(query: String, resultCount: Int = 0) {
        val trimmed = query.trim()
        if (trimmed.isNotBlank()) {
            searchHistoryDao.insertSearch(
                SearchHistoryEntity(
                    query = trimmed,
                    timestamp = System.currentTimeMillis(),
                    resultCount = resultCount
                )
            )
        }
    }

    suspend fun deleteSearchHistoryItem(id: Long) {
        searchHistoryDao.deleteById(id)
    }

    suspend fun clearAllSearchHistory() {
        searchHistoryDao.clearAll()
    }

    fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        return haversine(lat1, lon1, lat2, lon2)
    }
}

private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val r = 6371.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
    return r * 2 * atan2(sqrt(a), sqrt(1 - a))
}
