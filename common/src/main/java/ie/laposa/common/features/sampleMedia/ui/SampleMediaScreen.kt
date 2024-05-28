package ie.laposa.common.features.sampleMedia.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import ie.laposa.common.features.mediaLib.model.Media
import ie.laposa.common.features.mediaLib.ui.MediaLibrary
import ie.laposa.common.features.mediaSource.ui.MediaSourcesLibrary

@Composable
fun SampleMediaScreen(
    viewModel: SampleMediaScreenViewModel = hiltViewModel(),
    navigateToPlayer: () -> Unit
) {
    fun onMediaSelected(media: Media) {
        viewModel.onMediaSelected(media)
        navigateToPlayer()
    }

    Column {
        MediaLibrary(viewModel.media, ::onMediaSelected)
        MediaSourcesLibrary(navigateToPlayer = navigateToPlayer)
    }
}