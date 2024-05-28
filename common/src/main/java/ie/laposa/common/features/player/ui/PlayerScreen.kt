package ie.laposa.common.features.player.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.Text

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

    selectedMedia.value?.let {
        PlayerView(url = it.url)
    } ?: selectedInputStreamDataSourcePayload.value?.let {
        PlayerView(payload = it)
    } ?: run {
        Column {
            Text("No media selected")
            Row {
                Button(onClick = { }) {
                    Text("Select media")
                }
            }
        }
    }
}