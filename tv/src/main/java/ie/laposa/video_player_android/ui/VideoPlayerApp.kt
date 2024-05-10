package ie.laposa.video_player_android.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ie.laposa.video_player_android.features.player.ui.PlayerScreen
import ie.laposa.video_player_android.features.sampleMedia.ui.SampleMediaScreen

@Composable
fun VideoPlayerApp(appState: VideoPlayerAppState = rememberVideoPlayerAppState()) {
    Route(appState)
}

@Composable
private fun GlobalNavigationContainer(
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
        startDestination = Screen.SampleMediaLib.route
    ) {
        composable(Screen.Player.route) { GlobalNavigationContainer(appState = appState) { PlayerScreen() }}
        composable(Screen.SampleMediaLib.route) { GlobalNavigationContainer(appState = appState) { SampleMediaScreen(navigateToPlayer = appState::navigateToPlayer) }}
    }
}
