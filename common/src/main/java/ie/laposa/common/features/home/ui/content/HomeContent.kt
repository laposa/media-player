package ie.laposa.common.features.home.ui.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ie.laposa.common.features.home.ui.HomeScreenViewModel
import ie.laposa.common.features.home.ui.LocalHomeNavigation
import ie.laposa.common.features.home.ui.ProvideHomeNavController
import ie.laposa.common.features.player.ui.PlayerScreen
import ie.laposa.common.features.settings.ui.SettingsScreen
import ie.laposa.common.features.zeroConf.ZeroConfContent
import ie.laposa.common.ui.Screen

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
            ZeroConfContent()
        }

        composable(Screen.Player.route) {
            PlayerScreen({})
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}