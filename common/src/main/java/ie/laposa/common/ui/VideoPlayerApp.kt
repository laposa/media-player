package ie.laposa.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ie.laposa.common.features.home.ui.Home
import ie.laposa.common.features.player.ui.PlayerScreen

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
        composable(Screen.Player.route) { GlobalNavigationContainer(appState = appState) { PlayerScreen({}) } }
    }
}
