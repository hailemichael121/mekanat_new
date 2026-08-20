package com.example.mekanat_new

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.mekanat_new.data.local.MekanatDatabase
import com.example.mekanat_new.data.repository.ChurchRepository
import com.example.mekanat_new.ui.screens.MainScreen
import com.example.mekanat_new.ui.theme.MekanatTheme
import com.example.mekanat_new.ui.viewmodel.MekanatViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MekanatViewModel by viewModels {
        val db = MekanatDatabase.getDatabase(applicationContext)
        val repo = ChurchRepository(
            churchDao = db.churchDao(),
            tabotDao = db.tabotDao(),
            gubaeDao = db.gubaeDao(),
            favoriteDao = db.favoriteDao(),
            savedNigsDao = db.savedNigsDao(),
            searchHistoryDao = db.searchHistoryDao()
        )
        MekanatViewModel.Factory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MekanatTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}

