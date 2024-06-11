package ie.laposa.common.features.menu.ui.menuSections

import androidx.compose.runtime.Composable
import ie.laposa.common.features.mediaSource.ui.MediaSourcesLibrary

@Composable
fun ZeroConfMenuSection(setHomeContent: (@Composable () -> Unit) -> Unit) {
    MenuSection("Network") {
        MediaSourcesLibrary(setHomeContent = setHomeContent)
    }
}