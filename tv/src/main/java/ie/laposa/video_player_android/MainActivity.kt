package ie.laposa.video_player_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.tv.material3.Surface
import dagger.hilt.android.AndroidEntryPoint
import ie.laposa.common.features.mediaSource.ui.MediaSourceItemViewModel
import ie.laposa.common.features.mediaSource.ui.MediaSourceItemViewModelFactory
import ie.laposa.common.features.mediaSource.ui.ProvideMediaSourceItemViewModelFactory
import ie.laposa.common.ui.VideoPlayerApp
import ie.laposa.common.ui.theme.LaposaVideoPlayerTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var mediaSourceItemViewModelFactory: MediaSourceItemViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LaposaVideoPlayerTheme(isInDarkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape,
                ) {
                    ProvideMediaSourceItemViewModelFactory(myViewModelFactory = mediaSourceItemViewModelFactory) {
                        VideoPlayerApp()
                    }
                }
            }
        }
    }
}