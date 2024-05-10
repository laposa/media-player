package ie.laposa.video_player_android.features.player.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.tv.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Button

@Composable
fun PlayerScreen(viewModel: PlayerScreenViewModel = hiltViewModel()) {
    val selectedMedia = viewModel.selectedMedia.observeAsState()

    viewModel.selectedMedia.observeForever {
        println("selectedMedia changed to $it")
    }

    selectedMedia.value?.let {
        PlayerView(it.url)
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