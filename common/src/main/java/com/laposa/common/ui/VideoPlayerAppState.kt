package com.laposa.common.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceNavType
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
    val arguments: List<NamedNavArgument> get() = emptyList()

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

    data object MediaLibrary : Screen {
        override val route: String = "/zeroconf/mediaLibrary?media={media}"
        override val arguments = listOf(
            navArgument("media") {
                type = MediaSourceNavType()
            }
        )
    }

    data object Settings : Screen {
        override val route: String = "/setting"
    }

    data object LocalStorage : Screen {
        override val route: String = "/localStorage"
    }
}