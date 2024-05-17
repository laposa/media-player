package ie.laposa.common.features.sampleMedia.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import ie.laposa.common.features.mediaLib.model.Media
import ie.laposa.common.features.mediaLib.ui.MediaLibrary
import ie.laposa.common.features.mediaSource.ui.MediaSource
import ie.laposa.domain.mediaSource.model.MediaSource

@Composable
fun SampleMediaScreen(
    viewModel: SampleMediaScreenViewModel = hiltViewModel(),
    navigateToPlayer: () -> Unit)
{
    LaunchedEffect(true) {
        viewModel.fetchMediaSources()
    }

    fun onMediaSelected(media: Media) {
        viewModel.onMediaSelected(media)
        navigateToPlayer()
    }

    fun onMediaSourceSelected(media: MediaSource) {
        println("Media source selected")
    }

    Column {
        MediaLibrary(viewModel.media,::onMediaSelected)
        Row {
            for (source in viewModel.mediaSources.collectAsState().value) {
                MediaSource(source, ::onMediaSourceSelected)
            }
        }
    }
}