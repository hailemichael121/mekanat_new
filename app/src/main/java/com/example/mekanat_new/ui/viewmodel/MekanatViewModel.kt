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
import com.example.mekanat_new.ui.theme.ThemeMode
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

enum class ChurchSortOption(val label: String, val shortLabel: String) {
    DISTANCE_NEAREST("Nearest First", "Distance ↗"),
    DISTANCE_FURTHEST("Furthest First", "Distance ↘"),
    NAME_AZ("Name (A–Z)", "A–Z"),
    NAME_ZA("Name (Z–A)", "Z–A"),
    HISTORICAL("Historical Age", "Age")
}

data class MekanatUiState(
    val userLat: Double = 9.0306,
    val userLng: Double = 38.7619,
    val currentTab: Int = 0, // 0=Map, 1=Bookmarks, 2=Calendar, 3=Profile
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val searchQuery: String = "",
    val activeFilterChip: String = "All",
    val sortOption: ChurchSortOption = ChurchSortOption.DISTANCE_NEAREST,
    val isSearchingLoading: Boolean = false,
    val isRouteReversed: Boolean = false,
    val isLiveNavigating: Boolean = false,
    val liveNavStepIndex: Int = 0,
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
    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    private val _currentTab = MutableStateFlow(0)
    private val _searchQuery = MutableStateFlow("")
    private val _activeFilterChip = MutableStateFlow("All")
    private val _sortOption = MutableStateFlow(ChurchSortOption.DISTANCE_NEAREST)
    private val _isSearchingLoading = MutableStateFlow(false)
    private val _isRouteReversed = MutableStateFlow(false)
    private val _isLiveNavigating = MutableStateFlow(false)
    private val _liveNavStepIndex = MutableStateFlow(0)
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
            _sortOption,
            _selectedChurch,
            _routeToChurch
        ) { query, filter, sort, selected, route ->
            SearchRouteTuple(query, filter, sort, selected, route)
        },
        combine(
            repository.nearby(_userLat.value, _userLng.value),
            _selectedTravelMode,
            _isCalculatingRoute,
            _isRouteNavigationOpen,
            _activeRouteResult
        ) { nearby, mode, isCalculating, isNavOpen, routeResult ->
            RouteModeTuple(nearby, mode, isCalculating, isNavOpen, routeResult)
        },
        combine(
            _currentTab,
            _isMapView,
            _themeMode,
            repository.getFavoriteChurches(_userLat.value, _userLng.value),
            repository.getSavedNigs()
        ) { tab, isMapView, themeMode, favorites, savedNigs ->
            ContentTuple(tab, isMapView, themeMode, favorites, savedNigs)
        },
        combine(
            repository.getPendingSubmissions(),
            repository.getRecentSearches(),
            repository.getUpcomingNigs(),
            _selectedCalendarDate,
            _calendarMonth
        ) { submissions, recentSearches, upcomingNigs, calDate, calMonth ->
            AuxTuple(submissions, recentSearches, upcomingNigs, calDate, calMonth)
        },
        combine(
            _isSearchingLoading,
            _isRouteReversed,
            _isLiveNavigating,
            _liveNavStepIndex,
            _snackbarMessage
        ) { isSearching, isReversed, isLiveNav, stepIndex, snackbar ->
            LiveNavTuple(isSearching, isReversed, isLiveNav, stepIndex, snackbar)
        }
    ) { searchRoute, routeMode, content, aux, liveNav ->
        val filtered = filterAndSortChurches(
            list = routeMode.nearby,
            query = searchRoute.query,
            filter = searchRoute.filter,
            sortOption = searchRoute.sort
        )
        val bucketed = bucketNigs(aux.upcomingNigs)

        val routeDist = routeMode.routeResult?.totalDistanceKm ?: searchRoute.route?.let {
            repository.calculateDistanceKm(_userLat.value, _userLng.value, it.church.latitude, it.church.longitude)
        }

        MekanatUiState(
            userLat = _userLat.value,
            userLng = _userLng.value,
            currentTab = content.tab,
            themeMode = content.themeMode,
            searchQuery = searchRoute.query,
            activeFilterChip = searchRoute.filter,
            sortOption = searchRoute.sort,
            isSearchingLoading = liveNav.isSearchingLoading,
            isRouteReversed = liveNav.isRouteReversed,
            isLiveNavigating = liveNav.isLiveNavigating,
            liveNavStepIndex = liveNav.liveNavStepIndex,
            nearbyChurches = routeMode.nearby,
            filteredChurches = filtered,
            recentSearches = aux.recentSearches,
            selectedChurch = searchRoute.selected,
            routeToChurch = searchRoute.route,
            activeRouteResult = routeMode.routeResult,
            selectedTravelMode = routeMode.mode,
            isCalculatingRoute = routeMode.isCalculating,
            routeDistanceKm = routeDist,
            isMapView = content.isMapView,
            isRouteNavigationOpen = routeMode.isNavOpen,
            favoriteChurches = content.favorites,
            savedNigs = content.savedNigs,
            allUpcomingNigs = aux.upcomingNigs,
            bucketedNigs = bucketed,
            selectedCalendarDate = aux.calDate,
            calendarMonth = aux.calMonth,
            mySubmissions = aux.submissions,
            snackbarMessage = liveNav.snackbar
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MekanatUiState()
    )

    private fun filterAndSortChurches(
        list: List<ChurchWithDistance>,
        query: String,
        filter: String,
        sortOption: ChurchSortOption
    ): List<ChurchWithDistance> {
        val filtered = list.filter { item ->
            val matchesQuery = query.isBlank() ||
                    item.church.name.contains(query, ignoreCase = true) ||
                    (item.church.nameAmharic?.contains(query, ignoreCase = true) == true) ||
                    item.church.region.contains(query, ignoreCase = true) ||
                    item.church.diocese.contains(query, ignoreCase = true) ||
                    (item.primaryTabot?.name?.contains(query, ignoreCase = true) == true) ||
                    (item.primaryTabot?.nameEnglish?.contains(query, ignoreCase = true) == true)

            val matchesFilter = when (filter) {
                "All" -> true
                "Live Gubae", "Live Gubae 🔴" -> item.hasActiveGubae
                "Nearby (<50km)" -> item.distanceKm <= 50.0
                "Monasteries" -> item.church.churchType == "MONASTERY"
                "Cathedrals" -> item.church.churchType == "CATHEDRAL"
                "Rock-Hewn" -> item.church.churchType == "ROCK_HEWN"
                "Parishes" -> item.church.churchType == "PARISH"
                else -> true
            }

            matchesQuery && matchesFilter
        }

        return when (sortOption) {
            ChurchSortOption.DISTANCE_NEAREST -> filtered.sortedBy { it.distanceKm }
            ChurchSortOption.DISTANCE_FURTHEST -> filtered.sortedByDescending { it.distanceKm }
            ChurchSortOption.NAME_AZ -> filtered.sortedBy { it.church.name }
            ChurchSortOption.NAME_ZA -> filtered.sortedByDescending { it.church.name }
            ChurchSortOption.HISTORICAL -> filtered.sortedBy { it.church.id }
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

    fun onAddNigsFeast(
        churchId: Long,
        nameAmharic: String,
        nameEnglish: String,
        nigsMonth: Int,
        nigsDay: Int,
        detail: String?,
        routingDescription: String?
    ) {
        viewModelScope.launch {
            val combinedDesc = buildString {
                if (!detail.isNullOrBlank()) {
                    append(detail.trim())
                }
                if (!routingDescription.isNullOrBlank()) {
                    if (isNotEmpty()) append("\n\nPilgrimage & Route Directions:\n")
                    else append("Pilgrimage & Route Directions:\n")
                    append(routingDescription.trim())
                }
            }.ifBlank { null }

            repository.addTabot(
                TabotEntity(
                    churchId = churchId,
                    name = nameAmharic.ifBlank { nameEnglish },
                    nameEnglish = nameEnglish.ifBlank { nameAmharic },
                    nigsMonth = nigsMonth,
                    nigsDay = nigsDay,
                    description = detail?.trim()?.ifBlank { null } ?: combinedDesc,
                    routingDescription = routingDescription?.trim()?.ifBlank { null }
                )
            )
            showSnackbar("Nigs Feast celebration '$nameEnglish' added successfully!")
        }
    }

    fun onQuickCreateChurchAndAddNigs(
        churchName: String,
        churchAmharic: String?,
        diocese: String,
        region: String,
        churchType: String,
        latitude: Double,
        longitude: Double,
        tabotNameAmharic: String,
        tabotNameEnglish: String,
        nigsMonth: Int,
        nigsDay: Int,
        detail: String?,
        routingDescription: String?
    ) {
        viewModelScope.launch {
            val church = ChurchEntity(
                name = churchName.trim(),
                nameAmharic = churchAmharic?.trim()?.ifBlank { null },
                diocese = diocese.trim().ifBlank { "Orthodox Diocese" },
                region = region.trim().ifBlank { "Ethiopia" },
                churchType = churchType,
                latitude = latitude,
                longitude = longitude,
                description = detail,
                isVerified = true,
                submittedBy = "Pilgrim Community"
            )
            val newChurchId = repository.submitChurch(church)

            val combinedDesc = buildString {
                if (!detail.isNullOrBlank()) {
                    append(detail.trim())
                }
                if (!routingDescription.isNullOrBlank()) {
                    if (isNotEmpty()) append("\n\nPilgrimage & Route Directions:\n")
                    else append("Pilgrimage & Route Directions:\n")
                    append(routingDescription.trim())
                }
            }.ifBlank { null }

            repository.addTabot(
                TabotEntity(
                    churchId = newChurchId,
                    name = tabotNameAmharic.ifBlank { tabotNameEnglish },
                    nameEnglish = tabotNameEnglish.ifBlank { tabotNameAmharic },
                    nigsMonth = nigsMonth,
                    nigsDay = nigsDay,
                    description = detail?.trim()?.ifBlank { null } ?: combinedDesc,
                    routingDescription = routingDescription?.trim()?.ifBlank { null }
                )
            )
            showSnackbar("Sanctuary '$churchName' & Nigs Feast created successfully!")
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

    fun onSetSortOption(option: ChurchSortOption) {
        _sortOption.value = option
    }

    fun onTriggerSearchWithAnimation(query: String) {
        _searchQuery.value = query
        _isSearchingLoading.value = true
        viewModelScope.launch {
            kotlinx.coroutines.delay(1100)
            if (query.isNotBlank()) {
                repository.recordSearch(query, 0)
            }
            _isSearchingLoading.value = false
        }
    }

    fun onDismissSearchLoader() {
        _isSearchingLoading.value = false
    }

    fun onReverseRoute() {
        val currentDest = _routeToChurch.value ?: return
        val newReversed = !_isRouteReversed.value
        _isRouteReversed.value = newReversed

        viewModelScope.launch {
            _isCalculatingRoute.value = true
            try {
                val originLat = if (newReversed) currentDest.church.latitude else _userLat.value
                val originLng = if (newReversed) currentDest.church.longitude else _userLng.value
                val destLat = if (newReversed) _userLat.value else currentDest.church.latitude
                val destLng = if (newReversed) _userLng.value else currentDest.church.longitude
                val destName = if (newReversed) "Current Location" else currentDest.church.name

                val result = GebetaMapService.fetchOrCalculateRoute(
                    originLat = originLat,
                    originLng = originLng,
                    destLat = destLat,
                    destLng = destLng,
                    destName = destName,
                    diocese = currentDest.church.diocese,
                    mode = _selectedTravelMode.value
                )
                _activeRouteResult.value = result
                showSnackbar(if (newReversed) "Route reversed: from ${currentDest.church.name} to My Location" else "Route: from My Location to ${currentDest.church.name}")
            } catch (e: Exception) {
                val fallback = GebetaMapService.calculateGebetaRoute(
                    originLat = if (newReversed) currentDest.church.latitude else _userLat.value,
                    originLng = if (newReversed) currentDest.church.longitude else _userLng.value,
                    destLat = if (newReversed) _userLat.value else currentDest.church.latitude,
                    destLng = if (newReversed) _userLng.value else currentDest.church.longitude,
                    destName = if (newReversed) "Current Location" else currentDest.church.name,
                    diocese = currentDest.church.diocese,
                    mode = _selectedTravelMode.value
                )
                _activeRouteResult.value = fallback
            } finally {
                _isCalculatingRoute.value = false
            }
        }
    }

    fun onToggleLiveNav() {
        _isLiveNavigating.value = !_isLiveNavigating.value
        if (_isLiveNavigating.value) {
            _liveNavStepIndex.value = 0
            showSnackbar("Live navigation started. Follow turn-by-turn guidance.")
        }
    }

    fun onNextNavStep() {
        val maxSteps = (_activeRouteResult.value?.steps?.size ?: 1) - 1
        if (_liveNavStepIndex.value < maxSteps) {
            _liveNavStepIndex.value += 1
        }
    }

    fun onPrevNavStep() {
        if (_liveNavStepIndex.value > 0) {
            _liveNavStepIndex.value -= 1
        }
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun onSetThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    fun onToggleThemeMode() {
        _themeMode.value = when (_themeMode.value) {
            ThemeMode.DARK -> ThemeMode.LIGHT
            ThemeMode.LIGHT -> ThemeMode.SYSTEM
            ThemeMode.SYSTEM -> ThemeMode.DARK
        }
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
    val sort: ChurchSortOption,
    val selected: ChurchWithDistance?,
    val route: ChurchWithDistance?
)

private data class RouteModeTuple(
    val nearby: List<ChurchWithDistance>,
    val mode: GebetaTravelMode,
    val isCalculating: Boolean,
    val isNavOpen: Boolean,
    val routeResult: GebetaRouteResult?
)

private data class ContentTuple(
    val tab: Int,
    val isMapView: Boolean,
    val themeMode: ThemeMode,
    val favorites: List<ChurchWithDistance>,
    val savedNigs: List<UpcomingNigs>
)

private data class AuxTuple(
    val submissions: List<ChurchEntity>,
    val recentSearches: List<SearchHistoryEntity>,
    val upcomingNigs: List<UpcomingNigs>,
    val calDate: LocalDate,
    val calMonth: LocalDate
)

private data class LiveNavTuple(
    val isSearchingLoading: Boolean,
    val isRouteReversed: Boolean,
    val isLiveNavigating: Boolean,
    val liveNavStepIndex: Int,
    val snackbar: String?
)

