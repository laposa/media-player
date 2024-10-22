package com.laposa.common.features.zeroConf

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import com.laposa.common.ui.Screen
import com.laposa.domain.mediaSource.model.MediaSource
import com.google.gson.Gson

val LocalZeroConfNavigation = staticCompositionLocalOf<ZeroConfNavigation> {
    error("No ZeroConfNavigation provided")
}

@Composable
fun ProvideZeroConfNavController(
    zeroConfNavigation: ZeroConfNavigation,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalZeroConfNavigation provides zeroConfNavigation) {
        content()
    }
}

class ZeroConfNavigation(
    val navController: NavHostController
) {
    private fun navigateTo(screen: Screen) {
        navController.navigate(screen.route)
    }

    fun navigateToMediaLibrary(mediaSource: MediaSource) {
        val mediaSourceJson = Gson().toJson(mediaSource)
        val route = Screen.MediaLibrary.route.replace("{media}", mediaSourceJson)
        navController.navigate(route)
    }
}