package ie.laposa.common.features.player.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Text
import ie.laposa.common.theme.VideoPlayerTypography
import ie.laposa.common.theme.surfaceDark

@Composable
fun PlayerScreen(
    hidePlayerScreen: () -> Unit,
    viewModel: PlayerScreenViewModel = hiltViewModel()
) {
    val selectedMedia = viewModel.selectedMedia.observeAsState().value
    val selectedInputStreamDataSourcePayload =
        viewModel.selectedInputStreamDataSourcePayload.collectAsState().value

    viewModel.selectedMedia.observeForever {
        println("selectedMedia changed to $it")
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelectedMedia()
            println("TADY TAKY!!!!")
        }
    }

    Dialog(
        onDismissRequest = hidePlayerScreen,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(surfaceDark),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            selectedMedia?.let {
                PlayerView(url = it.path)
            } ?: selectedInputStreamDataSourcePayload?.let {
                PlayerView(payload = it)
            } ?: run {
                Column {
                    Text("No media selected", style = VideoPlayerTypography.titleSmall)
                }
            }
        }
    }
}