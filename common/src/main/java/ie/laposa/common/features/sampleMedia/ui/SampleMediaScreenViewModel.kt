package ie.laposa.common.features.sampleMedia.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SampleMediaScreenViewModel @Inject constructor(
) : ViewModel() {
    private val _media = MutableStateFlow(
        listOf(
            MediaSourceFile(
                name = "Big Buck Bunny",
                path = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                thumbnailUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg"
            ),
            MediaSourceFile(
                name = "Elephant Dream",
                path = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                thumbnailUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg"
            ),
            MediaSourceFile(
                name = "For Bigger Blazes For Bigger Blazes For Bigger Blazes For Bigger Blazes",
                path = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                thumbnailUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg"
            ),
            MediaSourceFile(
                name = "For Bigger Escape",
                path = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                thumbnailUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg"
            ),
        ),
    )

    val media: StateFlow<List<MediaSourceFile>> = _media
}