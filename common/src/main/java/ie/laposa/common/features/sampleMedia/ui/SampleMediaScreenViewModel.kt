package ie.laposa.common.features.sampleMedia.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ie.laposa.common.features.common.ui.SavedStateHandleViewModel
import ie.laposa.common.features.mediaLib.model.Media
import ie.laposa.domain.mediaSource.MediaSourceService
import ie.laposa.domain.mediaSource.model.MediaSource
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SampleMediaScreenViewModel @Inject constructor(
    private val savedStateHandleViewModel: SavedStateHandleViewModel,
) : ViewModel() {
    private val _media = mutableStateListOf(
        Media(
            title = "Big Buck Bunny",
            url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            thumbnailUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg"
        ),
        Media(
            title = "Elephant Dream",
            url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            thumbnailUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg"
        ),
        Media(
            title = "For Bigger Blazes",
            url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            thumbnailUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg"
        ),
        Media(
            title = "For Bigger Escape",
            url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            thumbnailUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg"
        ),
    )

    val media = _media

    fun onMediaSelected(media: Media) {
        savedStateHandleViewModel.setSelectedMedia(media)
    }
}