package com.example.mekanat_new.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                bottomBar = {
                    MekanatBottomBar(
                        currentTab = uiState.currentTab,
                        onTabSelected = { viewModel.onSelectTab(it) }
                    )
                }
            ) { paddingValues ->
                AnimatedContent(
                    targetState = uiState.currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    label = "tabTransition"
                ) { tab ->
                    when (tab) {
                        0 -> HomeScreen(
                            uiState = uiState,
                            onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                            onCommitSearch = { query, count -> viewModel.onCommitSearch(query, count) },
                            onDeleteSearchHistory = { viewModel.onDeleteSearchHistory(it) },
                            onClearAllSearchHistory = { viewModel.onClearAllSearchHistory() },
                            onFilterChipSelected = { viewModel.onFilterChipSelected(it) },
                            onSelectChurch = { viewModel.onSelectChurch(it) },
                            onStartRoute = { viewModel.onStartRoute(it) },
                            onChangeTravelMode = { viewModel.onChangeTravelMode(it) },
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
                            onToggleTabotBookmark = { id, isSaved -> viewModel.onToggleTabotBookmark(id, isSaved) }
                        )
                        3 -> ProfileScreen(
                            uiState = uiState,
                            onOpenAddChurch = { currentDestination = MekanatDestination.ADD_CHURCH }
                        )
                    }
                }
            }
        }
    }
}
