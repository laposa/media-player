package ie.laposa.common.features.sampleMedia.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import ie.laposa.common.features.mediaLib.model.FileSystemItem
import ie.laposa.common.features.mediaLib.model.Media
import ie.laposa.common.features.mediaLib.ui.MediaLibrary
import ie.laposa.domain.mediaSource.model.MediaSourceDirectory
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.mediaSource.model.MediaSourceFileBase
import ie.laposa.domain.mediaSource.model.MediaSourceShare
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SampleMediaScreen(
    viewModel: SampleMediaScreenViewModel = hiltViewModel(),
    navigateToPlayer: () -> Unit
) {
    fun onMediaSelected(media: MediaSourceFile) {
        viewModel.onMediaSelected(media)
        navigateToPlayer()
    }

    fun onDirectorySelected(dir: MediaSourceDirectory) {
        // nothing
    }

    fun onShareSelected(share: MediaSourceShare) {
        // nothing
    }

    Column {
        MediaLibrary(
            "Samples",
            viewModel.media,
            ::onMediaSelected,
            ::onDirectorySelected,
            ::onShareSelected
        )
    }
}