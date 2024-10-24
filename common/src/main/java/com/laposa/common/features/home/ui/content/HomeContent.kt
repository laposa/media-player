package com.laposa.common.features.home.ui.content

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.laposa.common.features.home.ui.HomeScreenViewModel
import com.laposa.common.features.home.ui.LocalHomeNavigation
import com.laposa.common.features.localStorage.ui.LocalStorageContent
import com.laposa.common.features.settings.ui.SettingsScreen
import com.laposa.common.features.zeroConf.ZeroConfScreen
import com.laposa.common.ui.Screen

@Composable
fun HomeContent(
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val homeNavigation = LocalHomeNavigation.current

    NavHost(
        navController = homeNavigation.homeNavController, startDestination = Screen.DashBoard.route
    ) {
        composable(Screen.DashBoard.route) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Dashboard(
                    onNavigateToPlayer = homeNavigation::navigateToPlayer
                )
            }
        }

        composable(Screen.ZeroConf.route) {
            ZeroConfScreen()
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(Screen.LocalStorage.route) {
            LocalStorageContent()
        }
    }
}