package ie.laposa.common.features.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import ie.laposa.common.features.mediaLib.model.Media
import ie.laposa.common.ui.Screen
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import kotlinx.coroutines.flow.map

val LocalHomeNavigation = staticCompositionLocalOf<HomeNavigation> {
    error("No Home Nav provided")
}

@Composable
fun ProvideHomeNavController(
    homeNavigation: HomeNavigation,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalHomeNavigation provides homeNavigation) {
        content()
    }
}

class HomeNavigation(
    val homeNavController: NavHostController,
    val goToPlayer: (MediaSourceFile?, String?) -> Unit,
) {
    val currentRoute = homeNavController.currentBackStackEntryFlow.map {
        it.destination.route
    }

    private fun navigateTo(screen: Screen) {
        homeNavController.navigate(screen.route)
    }

    fun navigateToPlayer(fileToPlay: MediaSourceFile?, filePath: String?) {
        //navigateTo(Screen.Player.route)
        goToPlayer(fileToPlay, filePath)
    }

    fun navigateToHome() {
        navigateTo(Screen.DashBoard)
    }

    fun navigateToZeroConf() {
        navigateTo(Screen.ZeroConf)
    }

    fun navigateToSettings() {
        navigateTo(Screen.Settings)
    }
}