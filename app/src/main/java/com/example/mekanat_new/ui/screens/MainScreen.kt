package com.example.mekanat_new.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mekanat_new.ui.components.MekanatBottomBar
import com.example.mekanat_new.ui.viewmodel.MekanatViewModel

enum class MekanatDestination {
    SPLASH,
    MAIN,
    DETAIL,
    ADD_CHURCH
}

@Composable
fun MainScreen(
    viewModel: MekanatViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var currentDestination by remember { mutableStateOf(MekanatDestination.SPLASH) }
    var selectedDetailChurchId by remember { mutableStateOf<Long?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    when (currentDestination) {
        MekanatDestination.SPLASH -> {
            SplashScreen(
                onSplashFinished = { currentDestination = MekanatDestination.MAIN }
            )
        }
        MekanatDestination.DETAIL -> {
            selectedDetailChurchId?.let { churchId ->
                ChurchDetailScreen(
                    churchId = churchId,
                    viewModel = viewModel,
                    onBack = {
                        currentDestination = MekanatDestination.MAIN
                        selectedDetailChurchId = null
                    },
                    onStartRoute = { church ->
                        viewModel.onStartRoute(church)
                        currentDestination = MekanatDestination.MAIN
                    }
                )
            } ?: run {
                currentDestination = MekanatDestination.MAIN
            }
        }
        MekanatDestination.ADD_CHURCH -> {
            AddChurchScreen(
                uiState = uiState,
                viewModel = viewModel,
                onBack = { currentDestination = MekanatDestination.MAIN }
            )
        }
        MekanatDestination.MAIN -> {
            Box(modifier = Modifier.fillMaxSize()) {
                AnimatedContent(
                    targetState = uiState.currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    modifier = Modifier.fillMaxSize(),
                    label = "tabTransition"
                ) { tab ->
                    when (tab) {
                        0 -> HomeScreen(
                            uiState = uiState,
                            onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                            onCommitSearch = { query, count -> viewModel.onCommitSearch(query, count) },
                            onTriggerSearchWithAnimation = { viewModel.onTriggerSearchWithAnimation(it) },
                            onDismissSearchLoader = { viewModel.onDismissSearchLoader() },
                            onDeleteSearchHistory = { viewModel.onDeleteSearchHistory(it) },
                            onClearAllSearchHistory = { viewModel.onClearAllSearchHistory() },
                            onFilterChipSelected = { viewModel.onFilterChipSelected(it) },
                            onSetSortOption = { viewModel.onSetSortOption(it) },
                            onSelectChurch = { viewModel.onSelectChurch(it) },
                            onStartRoute = { viewModel.onStartRoute(it) },
                            onChangeTravelMode = { viewModel.onChangeTravelMode(it) },
                            onReverseRoute = { viewModel.onReverseRoute() },
                            onToggleLiveNav = { viewModel.onToggleLiveNav() },
                            onNextNavStep = { viewModel.onNextNavStep() },
                            onPrevNavStep = { viewModel.onPrevNavStep() },
                            onClearRoute = { viewModel.onClearRoute() },
                            onToggleMapView = { viewModel.onToggleMapView() },
                            onOpenChurchDetail = { id ->
                                selectedDetailChurchId = id
                                currentDestination = MekanatDestination.DETAIL
                            },
                            onOpenAddChurch = { currentDestination = MekanatDestination.ADD_CHURCH },
                            onToggleFavorite = { id, isFav -> viewModel.onToggleChurchFavorite(id, isFav) }
                        )
                        1 -> BookmarksScreen(
                            uiState = uiState,
                            onOpenChurchDetail = { id ->
                                selectedDetailChurchId = id
                                currentDestination = MekanatDestination.DETAIL
                            },
                            onStartRoute = { viewModel.onStartRoute(it) },
                            onToggleChurchFavorite = { id, isFav -> viewModel.onToggleChurchFavorite(id, isFav) },
                            onToggleTabotBookmark = { id, isSaved -> viewModel.onToggleTabotBookmark(id, isSaved) }
                        )
                        2 -> CalendarScreen(
                            uiState = uiState,
                            onSelectDate = { viewModel.onSelectCalendarDate(it) },
                            onPreviousMonth = { viewModel.onPreviousCalendarMonth() },
                            onNextMonth = { viewModel.onNextCalendarMonth() },
                            onResetToToday = { viewModel.onResetCalendarToToday() },
                            onOpenChurchDetail = { id ->
                                selectedDetailChurchId = id
                                currentDestination = MekanatDestination.DETAIL
                            },
                            onStartRoute = { viewModel.onStartRoute(it) },
                            onToggleTabotBookmark = { id, isSaved -> viewModel.onToggleTabotBookmark(id, isSaved) },
                            onAddNigsFeast = { churchId, nameAm, nameEn, month, day, detail, routing ->
                                viewModel.onAddNigsFeast(churchId, nameAm, nameEn, month, day, detail, routing)
                            },
                            onQuickCreateChurchAndAddNigs = { churchName, churchAm, diocese, region, type, lat, lng, nameAm, nameEn, month, day, detail, routing ->
                                viewModel.onQuickCreateChurchAndAddNigs(churchName, churchAm, diocese, region, type, lat, lng, nameAm, nameEn, month, day, detail, routing)
                            }
                        )
                        3 -> ProfileScreen(
                            uiState = uiState,
                            onOpenAddChurch = { currentDestination = MekanatDestination.ADD_CHURCH },
                            onSetThemeMode = { viewModel.onSetThemeMode(it) }
                        )
                    }
                }

                // Floating Bottom Navigation Bar directly over content
                MekanatBottomBar(
                    currentTab = uiState.currentTab,
                    onTabSelected = { viewModel.onSelectTab(it) },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )

                // Snackbar Host above bottom bar
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 84.dp)
                )
            }
        }
    }
}
