package ie.laposa.common.features.sampleMedia.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ie.laposa.common.features.common.ui.SavedStateHandleViewModel
import ie.laposa.common.features.mediaLib.model.Media
import javax.inject.Inject

@HiltViewModel
class SampleMediaScreenViewModel @Inject constructor(
    private val savedStateHandleViewModel: SavedStateHandleViewModel,
) : ViewModel() {
    private val _media = mutableStateListOf(
        Media(
            fileName = "Big Buck Bunny",
            filePath = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            thumbnailUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg"
        ),
        Media(
            fileName = "Elephant Dream",
            filePath = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            thumbnailUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg"
        ),
        Media(
            fileName = "For Bigger Blazes For Bigger Blazes For Bigger Blazes For Bigger Blazes",
            filePath = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            thumbnailUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg"
        ),
        Media(
            fileName = "For Bigger Escape",
            filePath = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            thumbnailUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg"
        ),
    )

    val media = _media

    fun onMediaSelected(media: Media) {
        savedStateHandleViewModel.setSelectedMedia(media)
    }
}