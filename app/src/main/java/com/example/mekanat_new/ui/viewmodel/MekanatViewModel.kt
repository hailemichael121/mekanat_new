package com.example.mekanat_new.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mekanat_new.data.local.ChurchEntity
import com.example.mekanat_new.data.local.SearchHistoryEntity
import com.example.mekanat_new.data.local.TabotEntity
import com.example.mekanat_new.data.maps.GebetaMapService
import com.example.mekanat_new.data.maps.GebetaRouteResult
import com.example.mekanat_new.data.maps.GebetaTravelMode
import com.example.mekanat_new.data.model.ChurchDetail
import com.example.mekanat_new.data.model.ChurchWithDistance
import com.example.mekanat_new.data.model.SearchResult
import com.example.mekanat_new.data.model.UpcomingNigs
import com.example.mekanat_new.data.repository.ChurchRepository
import com.example.mekanat_new.data.util.EthiopianCalendar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class MekanatUiState(
    val userLat: Double = 9.0306,
    val userLng: Double = 38.7619,
    val currentTab: Int = 0, // 0=Map, 1=Bookmarks, 2=Calendar, 3=Profile
    val searchQuery: String = "",
    val activeFilterChip: String = "All",
    val nearbyChurches: List<ChurchWithDistance> = emptyList(),
    val filteredChurches: List<ChurchWithDistance> = emptyList(),
    val searchResults: List<SearchResult> = emptyList(),
    val recentSearches: List<SearchHistoryEntity> = emptyList(),
    val selectedChurch: ChurchWithDistance? = null,
    val routeToChurch: ChurchWithDistance? = null,
    val activeRouteResult: GebetaRouteResult? = null,
    val selectedTravelMode: GebetaTravelMode = GebetaTravelMode.DRIVING,
    val isCalculatingRoute: Boolean = false,
    val routeDistanceKm: Double? = null,
    val isMapView: Boolean = true,
    val isRouteNavigationOpen: Boolean = false,
    val favoriteChurches: List<ChurchWithDistance> = emptyList(),
    val savedNigs: List<UpcomingNigs> = emptyList(),
    val allUpcomingNigs: List<UpcomingNigs> = emptyList(),
    val bucketedNigs: Map<String, List<UpcomingNigs>> = emptyMap(),
    val selectedCalendarDate: LocalDate = LocalDate.now(),
    val calendarMonth: LocalDate = LocalDate.now().withDayOfMonth(1),
    val mySubmissions: List<ChurchEntity> = emptyList(),
    val snackbarMessage: String? = null
)

class MekanatViewModel(
    private val repository: ChurchRepository
) : ViewModel() {

    private val _userLat = MutableStateFlow(9.0306) // Default Addis Ababa
    private val _userLng = MutableStateFlow(38.7619)
    private val _currentTab = MutableStateFlow(0)
    private val _searchQuery = MutableStateFlow("")
    private val _activeFilterChip = MutableStateFlow("All")
    private val _selectedChurch = MutableStateFlow<ChurchWithDistance?>(null)
    private val _routeToChurch = MutableStateFlow<ChurchWithDistance?>(null)
    private val _activeRouteResult = MutableStateFlow<GebetaRouteResult?>(null)
    private val _selectedTravelMode = MutableStateFlow(GebetaTravelMode.DRIVING)
    private val _isCalculatingRoute = MutableStateFlow(false)
    private val _isMapView = MutableStateFlow(true)
    private val _isRouteNavigationOpen = MutableStateFlow(false)
    private val _selectedCalendarDate = MutableStateFlow(LocalDate.now())
    private val _calendarMonth = MutableStateFlow(LocalDate.now().withDayOfMonth(1))
    private val _snackbarMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<MekanatUiState> = combine(
        combine(
            _searchQuery,
            _activeFilterChip,
            _selectedChurch,
            _routeToChurch,
            _activeRouteResult
        ) { query, filter, selected, route, routeResult ->
            SearchRouteTuple(query, filter, selected, route, routeResult)
        },
        combine(
            repository.nearby(_userLat.value, _userLng.value),
            _selectedTravelMode,
            _isCalculatingRoute,
            _isRouteNavigationOpen
        ) { nearby, mode, isCalculating, isNavOpen ->
            RouteModeTuple(nearby, mode, isCalculating, isNavOpen)
        },
        combine(
            _currentTab,
            _isMapView,
            repository.getFavoriteChurches(_userLat.value, _userLng.value),
            repository.getSavedNigs(),
            repository.getUpcomingNigs()
        ) { tab, isMapView, favorites, savedNigs, upcomingNigs ->
            ContentTuple(tab, isMapView, favorites, savedNigs, upcomingNigs)
        },
        combine(
            repository.getPendingSubmissions(),
            repository.getRecentSearches(),
            _selectedCalendarDate,
            _calendarMonth,
            _snackbarMessage
        ) { submissions, recentSearches, calDate, calMonth, snackbar ->
            AuxTuple(submissions, recentSearches, calDate, calMonth, snackbar)
        }
    ) { searchRoute, routeMode, content, aux ->
        val filtered = filterChurches(routeMode.nearby, searchRoute.query, searchRoute.filter)
        val bucketed = bucketNigs(content.upcomingNigs)

        val routeDist = searchRoute.routeResult?.totalDistanceKm ?: searchRoute.route?.let {
            repository.calculateDistanceKm(_userLat.value, _userLng.value, it.church.latitude, it.church.longitude)
        }

        MekanatUiState(
            userLat = _userLat.value,
            userLng = _userLng.value,
            currentTab = content.tab,
            searchQuery = searchRoute.query,
            activeFilterChip = searchRoute.filter,
            nearbyChurches = routeMode.nearby,
            filteredChurches = filtered,
            recentSearches = aux.recentSearches,
            selectedChurch = searchRoute.selected,
            routeToChurch = searchRoute.route,
            activeRouteResult = searchRoute.routeResult,
            selectedTravelMode = routeMode.mode,
            isCalculatingRoute = routeMode.isCalculating,
            routeDistanceKm = routeDist,
            isMapView = content.isMapView,
            isRouteNavigationOpen = routeMode.isNavOpen,
            favoriteChurches = content.favorites,
            savedNigs = content.savedNigs,
            allUpcomingNigs = content.upcomingNigs,
            bucketedNigs = bucketed,
            selectedCalendarDate = aux.calDate,
            calendarMonth = aux.calMonth,
            mySubmissions = aux.submissions,
            snackbarMessage = aux.snackbar
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MekanatUiState()
    )

    private fun filterChurches(
        list: List<ChurchWithDistance>,
        query: String,
        filter: String
    ): List<ChurchWithDistance> {
        return list.filter { item ->
            val matchesQuery = query.isBlank() ||
                    item.church.name.contains(query, ignoreCase = true) ||
                    (item.church.nameAmharic?.contains(query, ignoreCase = true) == true) ||
                    item.church.region.contains(query, ignoreCase = true) ||
                    item.church.diocese.contains(query, ignoreCase = true) ||
                    (item.primaryTabot?.name?.contains(query, ignoreCase = true) == true) ||
                    (item.primaryTabot?.nameEnglish?.contains(query, ignoreCase = true) == true)

            val matchesFilter = when (filter) {
                "All" -> true
                "Live Gubae 🔴" -> item.hasActiveGubae
                "Nearby (<50km)" -> item.distanceKm <= 50.0
                "Monasteries" -> item.church.churchType == "MONASTERY"
                "Cathedrals" -> item.church.churchType == "CATHEDRAL"
                "Rock-Hewn" -> item.church.churchType == "ROCK_HEWN"
                "Parishes" -> item.church.churchType == "PARISH"
                else -> true
            }

            matchesQuery && matchesFilter
        }
    }

    private fun bucketNigs(list: List<UpcomingNigs>): Map<String, List<UpcomingNigs>> {
        return mapOf(
            "This week" to list.filter { it.daysUntil in 0..7 },
            "This month" to list.filter { it.daysUntil in 8..31 },
            "Next month" to list.filter { it.daysUntil in 32..62 }
        )
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCommitSearch(query: String, count: Int = 0) {
        _searchQuery.value = query
        if (query.isNotBlank()) {
            viewModelScope.launch {
                repository.recordSearch(query, count)
            }
        }
    }

    fun onDeleteSearchHistory(id: Long) {
        viewModelScope.launch {
            repository.deleteSearchHistoryItem(id)
        }
    }

    fun onClearAllSearchHistory() {
        viewModelScope.launch {
            repository.clearAllSearchHistory()
            showSnackbar("Search history cleared")
        }
    }

    fun onToggleRouteNavigationDetails(open: Boolean? = null) {
        _isRouteNavigationOpen.value = open ?: !_isRouteNavigationOpen.value
    }

    fun onFilterChipSelected(filter: String) {
        _activeFilterChip.value = filter
    }

    fun onSelectChurch(church: ChurchWithDistance?) {
        _selectedChurch.value = church
    }

    fun onStartRoute(
        church: ChurchWithDistance,
        mode: GebetaTravelMode = _selectedTravelMode.value
    ) {
        _routeToChurch.value = church
        _selectedChurch.value = church
        _selectedTravelMode.value = mode
        _currentTab.value = 0 // Navigate to Map tab
        _isMapView.value = true
        _isRouteNavigationOpen.value = true

        viewModelScope.launch {
            _isCalculatingRoute.value = true
            try {
                val result = GebetaMapService.fetchOrCalculateRoute(
                    originLat = _userLat.value,
                    originLng = _userLng.value,
                    destLat = church.church.latitude,
                    destLng = church.church.longitude,
                    destName = church.church.name,
                    diocese = church.church.diocese,
                    mode = mode
                )
                _activeRouteResult.value = result
                val distFormatted = String.format("%.1f", result.totalDistanceKm)
                val etaHours = result.etaMinutes / 60
                val etaRemMin = result.etaMinutes % 60
                val etaFormatted = if (etaHours > 0) "${etaHours}h ${etaRemMin}m" else "${result.etaMinutes}m"
                showSnackbar("Route to ${church.church.name} ready ($distFormatted km • ~$etaFormatted)")
            } catch (e: Exception) {
                // Fallback route calculation
                val fallback = GebetaMapService.calculateGebetaRoute(
                    originLat = _userLat.value,
                    originLng = _userLng.value,
                    destLat = church.church.latitude,
                    destLng = church.church.longitude,
                    destName = church.church.name,
                    diocese = church.church.diocese,
                    mode = mode
                )
                _activeRouteResult.value = fallback
                showSnackbar("Route generated via Ethiopian Highway Network corridor")
            } finally {
                _isCalculatingRoute.value = false
            }
        }
    }

    fun onChangeTravelMode(mode: GebetaTravelMode) {
        if (_selectedTravelMode.value == mode) return
        _selectedTravelMode.value = mode
        val currentDest = _routeToChurch.value
        if (currentDest != null) {
            onStartRoute(currentDest, mode)
        }
    }

    fun onClearRoute() {
        _routeToChurch.value = null
        _activeRouteResult.value = null
        _isRouteNavigationOpen.value = false
    }

    fun onToggleMapView() {
        _isMapView.value = !_isMapView.value
    }

    fun onSelectTab(tabIndex: Int) {
        _currentTab.value = tabIndex
    }

    fun onToggleChurchFavorite(churchId: Long, isCurrentlyFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(churchId, isCurrentlyFav)
            showSnackbar(if (isCurrentlyFav) "Removed church from Bookmarks" else "Saved church to Bookmarks!")
        }
    }

    fun onToggleTabotBookmark(tabotId: Long, isCurrentlySaved: Boolean) {
        viewModelScope.launch {
            repository.toggleSavedNigs(tabotId, isCurrentlySaved)
            showSnackbar(if (isCurrentlySaved) "Removed Nigs from saved celebrations" else "Saved Nigs celebration reminder!")
        }
    }

    fun onSelectCalendarDate(date: LocalDate) {
        _selectedCalendarDate.value = date
    }

    fun onPreviousCalendarMonth() {
        _calendarMonth.value = _calendarMonth.value.minusMonths(1)
    }

    fun onNextCalendarMonth() {
        _calendarMonth.value = _calendarMonth.value.plusMonths(1)
    }

    fun onResetCalendarToToday() {
        _selectedCalendarDate.value = LocalDate.now()
        _calendarMonth.value = LocalDate.now().withDayOfMonth(1)
    }

    fun onSubmitNewChurch(
        name: String,
        nameAmharic: String?,
        latitude: Double,
        longitude: Double,
        region: String,
        diocese: String,
        churchType: String,
        description: String?,
        history: String?,
        address: String?,
        contactPhone: String?,
        contactEmail: String?,
        tabotNames: List<String>,
        nigsMonth: Int,
        nigsDay: Int
    ) {
        viewModelScope.launch {
            val entity = ChurchEntity(
                name = name,
                nameAmharic = nameAmharic,
                latitude = latitude,
                longitude = longitude,
                region = region.ifBlank { "Ethiopia" },
                diocese = diocese.ifBlank { "Orthodox Diocese" },
                churchType = churchType,
                description = description,
                history = history,
                address = address,
                contactPhone = contactPhone,
                contactEmail = contactEmail,
                isVerified = false,
                submittedBy = "Community Pilgrim"
            )
            val newChurchId = repository.submitChurch(entity)

            // Insert submitted tabots
            tabotNames.filter { it.isNotBlank() }.forEach { tName ->
                repository.addTabot(
                    TabotEntity(
                        churchId = newChurchId,
                        name = tName,
                        nameEnglish = tName,
                        nigsMonth = nigsMonth,
                        nigsDay = nigsDay
                    )
                )
            }

            showSnackbar("Sanctuary '$name' submitted! Review status: PENDING")
        }
    }

    fun onAddGubaeEvent(churchId: Long, title: String, description: String?, startEpoch: Long, endEpoch: Long) {
        viewModelScope.launch {
            repository.addGubaeEvent(churchId, title, description, startEpoch, endEpoch)
            showSnackbar("Live Gubae event announced successfully!")
        }
    }

    fun setUserLocation(lat: Double, lng: Double) {
        _userLat.value = lat
        _userLng.value = lng
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun getChurchDetailFlow(churchId: Long) = repository.detail(churchId)

    class Factory(private val repository: ChurchRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MekanatViewModel(repository) as T
        }
    }
}

private data class SearchRouteTuple(
    val query: String,
    val filter: String,
    val selected: ChurchWithDistance?,
    val route: ChurchWithDistance?,
    val routeResult: GebetaRouteResult?
)

private data class RouteModeTuple(
    val nearby: List<ChurchWithDistance>,
    val mode: GebetaTravelMode,
    val isCalculating: Boolean,
    val isNavOpen: Boolean
)

private data class ContentTuple(
    val tab: Int,
    val isMapView: Boolean,
    val favorites: List<ChurchWithDistance>,
    val savedNigs: List<UpcomingNigs>,
    val upcomingNigs: List<UpcomingNigs>
)

private data class AuxTuple(
    val submissions: List<ChurchEntity>,
    val recentSearches: List<SearchHistoryEntity>,
    val calDate: LocalDate,
    val calMonth: LocalDate,
    val snackbar: String?
)

