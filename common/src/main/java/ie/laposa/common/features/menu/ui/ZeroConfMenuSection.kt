package ie.laposa.common.features.menu.ui

import androidx.compose.runtime.Composable
import ie.laposa.common.features.mediaSource.ui.MediaSourcesLibrary

@Composable
fun ZeroConfMenuSection() {
    MenuSection("Network") {
        MediaSourcesLibrary()
    }
}