package ie.laposa.common.features.player.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Text
import ie.laposa.common.theme.VideoPlayerTypography
import ie.laposa.common.theme.surfaceDark

@Composable
fun PlayerScreen(viewModel: PlayerScreenViewModel = hiltViewModel()) {
    val selectedMedia = viewModel.selectedMedia.observeAsState()
    val selectedInputStreamDataSourcePayload =
        viewModel.selectedInputStreamDataSourcePayload.collectAsState()

    viewModel.selectedMedia.observeForever {
        println("selectedMedia changed to $it")
    }

    DisposableEffect(Unit) {
        onDispose {
            println("Dispose PlayerScreen")
            viewModel.clearSelectedMedia()
        }
    }

    Box(
        modifier = Modifier.fillMaxHeight(0.66f).fillMaxWidth().background(surfaceDark),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        selectedMedia.value?.let {
            PlayerView(url = it.url)
        } ?: selectedInputStreamDataSourcePayload.value?.let {
            PlayerView(payload = it)
        } ?: run {
            Column {
                Text("No media selected", style = VideoPlayerTypography.titleSmall)
            }
        }
    }

}