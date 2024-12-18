package com.laposa.common.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.laposa.common.features.home.ui.Home

@Composable
fun VideoPlayerApp(appState: VideoPlayerAppState = rememberVideoPlayerAppState()) {
    Route(appState)
}

@Composable
fun GlobalNavigationContainer(
    appState: VideoPlayerAppState,
    content: @Composable () -> Unit
) {
    val currentRoute = appState.currentRoute.collectAsState(initial = null)
    content()
}

@Composable
private fun Route(appState: VideoPlayerAppState) {
    NavHost(
        navController = appState.navHostController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            GlobalNavigationContainer(appState = appState) {
                Home()
            }
        }
    }
}
