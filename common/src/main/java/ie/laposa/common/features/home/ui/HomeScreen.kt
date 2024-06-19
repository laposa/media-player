package ie.laposa.common.features.home.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.Surface
import ie.laposa.common.features.menu.ui.Menu
import ie.laposa.common.features.player.ui.PlayerScreen
import ie.laposa.common.features.player.ui.PlayerView

@Composable
fun Home(
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val homeNavController = rememberNavController()

    val homeNavigation = remember {
        HomeNavigation(homeNavController, viewModel::showPlayer)
    }

    ProvideHomeNavController(homeNavigation = homeNavigation) {
        Surface(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 32.dp,
                top = 32.dp,
                bottom = 32.dp
            )
        ) {
            Menu()
            if (viewModel.isPlayerVisible.collectAsState().value) {
                PlayerScreen(
                    viewModel::hidePlayer
                )
            }
        }
    }
}