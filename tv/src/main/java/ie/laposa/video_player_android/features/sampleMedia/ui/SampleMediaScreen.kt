package ie.laposa.video_player_android.features.sampleMedia.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import ie.laposa.video_player_android.features.mediaLib.model.Media
import ie.laposa.video_player_android.features.mediaLib.ui.MediaLibrary

@Composable
fun SampleMediaScreen(
    viewModel: SampleMediaScreenViewModel = hiltViewModel(),
    navigateToPlayer: () -> Unit)
{

    fun onMediaSelected(media: Media) {
        viewModel.onMediaSelected(media)
        navigateToPlayer()
    }

    MediaLibrary(viewModel.media,::onMediaSelected)
}