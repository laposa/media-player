package com.laposa.common.features.zeroConf

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.laposa.common.features.mediaSource.ui.MediaLibraryScreen
import com.laposa.common.ui.Screen
import com.laposa.domain.mediaSource.model.MediaSource

@Composable
fun ZeroConfScreen() {
    val navController = rememberNavController()

    val zeroConfNavigation = remember {
        ZeroConfNavigation(navController = navController)
    }

    ProvideZeroConfNavController(zeroConfNavigation = zeroConfNavigation) {
        NavHost(
            navController = zeroConfNavigation.navController,
            startDestination = Screen.ZeroConf.route
        ) {
            composable(Screen.ZeroConf.route) {
                ZeroConfContent()
            }

            composable(
                Screen.MediaLibrary.route,
                arguments = Screen.MediaLibrary.arguments
            ) { backStackEntry ->
                val media = backStackEntry.arguments?.getParcelable<MediaSource>(
                    "media",
                )
                media?.let {
                    MediaLibraryScreen(it)
                }
            }

        }
    }

}