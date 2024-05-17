package ie.laposa.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.map

class VideoPlayerAppState(
    val navHostController: NavHostController,
) {
    val currentRoute = navHostController.currentBackStackEntryFlow.map {
        it.destination.route
    }

    private fun navigateTo(route: String) {
        navHostController.navigate(route)
    }

    fun navigateToSampleMediaLib() {
        navigateTo(Screen.SampleMediaLib.route)
    }

    fun navigateToPlayer() {
        navigateTo(Screen.Player.route)
    }
}

@Composable
fun rememberVideoPlayerAppState(
    navHostController: NavHostController = rememberNavController(),
) = remember(navHostController) {
    VideoPlayerAppState(navHostController)
}

sealed interface Screen {
    val route: String

    data object SampleMediaLib : Screen {
        override val route = "/sampleMediaLib"
    }

    data object Player : Screen {
        override val route = "/player"
    }
}