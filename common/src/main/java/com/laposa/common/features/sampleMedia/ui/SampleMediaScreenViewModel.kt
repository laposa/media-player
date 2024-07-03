package com.laposa.common.features.sampleMedia.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.laposa.domain.mediaSource.model.MediaSourceFile
import com.laposa.domain.mediaSource.model.MediaSourceType
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
                thumbnailUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg",
                type = MediaSourceType.URL
            ),
            MediaSourceFile(
                name = "Elephant Dream",
                path = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                thumbnailUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg",
                type = MediaSourceType.URL
            ),
            MediaSourceFile(
                name = "For Bigger Blazes For Bigger Blazes For Bigger Blazes For Bigger Blazes",
                path = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                thumbnailUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg",
                type = MediaSourceType.URL
            ),
            MediaSourceFile(
                name = "For Bigger Escape",
                path = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                thumbnailUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg",
                type = MediaSourceType.URL
            ),
        ),
    )

    val media: StateFlow<List<MediaSourceFile>> = _media
}