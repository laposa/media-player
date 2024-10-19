package com.laposa.common.ui

import androidx.compose.material3.SnackbarHostState
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
}

@Composable
fun rememberVideoPlayerAppState(
    navHostController: NavHostController = rememberNavController(),
) = remember(navHostController) {
    VideoPlayerAppState(navHostController)
}

@Composable
fun rememberSnackbarHostState() = remember {
    SnackbarHostState()
}

sealed interface Screen {
    val route: String

    data object Home : Screen {
        override val route = "/home"
    }

    data object Player : Screen {
        override val route = "/player"
    }

    data object DashBoard : Screen {
        override val route: String = "/dashboard"
    }

    data object ZeroConf : Screen {
        override val route: String = "/zeroconf"
    }

    data object Settings : Screen {
        override val route: String = "/setting"
    }

    data object LocalStorage : Screen {
        override val route: String = "/localStorage"
    }
}