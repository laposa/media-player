package ie.laposa.common.features.sampleMedia.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import ie.laposa.common.features.mediaLib.model.FileSystemItem
import ie.laposa.common.features.mediaLib.model.Media
import ie.laposa.common.features.mediaLib.ui.MediaLibrary

@Composable
fun SampleMediaScreen(
    viewModel: SampleMediaScreenViewModel = hiltViewModel(),
    navigateToPlayer: () -> Unit
) {
    fun onMediaSelected(media: FileSystemItem) {
        viewModel.onMediaSelected(media as Media)
        navigateToPlayer()
    }

    Column {
        MediaLibrary("Samples", viewModel.media, ::onMediaSelected)
    }
}