package ie.laposa.common.features.sampleMedia.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import ie.laposa.common.features.mediaLib.ui.MediaLibrary
import ie.laposa.domain.mediaSource.model.MediaSourceDirectory
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.mediaSource.model.MediaSourceShare

@Composable
fun SampleMediaScreen(
    viewModel: SampleMediaScreenViewModel = hiltViewModel(),
    navigateToPlayer: (MediaSourceFile?, String?) -> Unit
) {
    fun onMediaSelected(media: MediaSourceFile) {
        navigateToPlayer(media, null)
    }

    fun onDirectorySelected(dir: MediaSourceDirectory) {
        // nothing
    }

    fun onShareSelected(share: MediaSourceShare) {
        // nothing
    }

    fun onGoUp() {
        // nothing
    }

    Column {
        MediaLibrary(
            "Samples",
            viewModel.media,
            "",
            1,
            ::onMediaSelected,
            ::onDirectorySelected,
            ::onShareSelected,
            ::onGoUp,
        )
    }
}