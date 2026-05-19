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
                path = "https://upload.wikimedia.org/wikipedia/commons/c/c0/Big_Buck_Bunny_4K.webm",
                thumbnailUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c0/Big_Buck_Bunny_4K.webm/1280px--Big_Buck_Bunny_4K.webm.jpg",
                type = MediaSourceType.URL
            ),
            MediaSourceFile(
                name = "Elephant Dream",
                path = "https://upload.wikimedia.org/wikipedia/commons/2/28/Elephants_Dream_%282006%29_1080p24.webm",
                thumbnailUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/Elephants_Dream_%282006%29_1080p24.webm/1280px--Elephants_Dream_%282006%29_1080p24.webm.jpg",
                type = MediaSourceType.URL
            ),
            MediaSourceFile(
                name = "Hero",
                path = "https://upload.wikimedia.org/wikipedia/commons/a/a9/HERO_-_Blender_Open_Movie-full_movie.webm",
                thumbnailUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/HERO_-_Blender_Open_Movie-full_movie.webm/1920px--HERO_-_Blender_Open_Movie-full_movie.webm.jpg",
                type = MediaSourceType.URL
            ),
            MediaSourceFile(
                name = "Spring",
                path = "https://upload.wikimedia.org/wikipedia/commons/a/a5/Spring_-_Blender_Open_Movie.webm",
                thumbnailUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Spring_-_Blender_Open_Movie.webm/1920px--Spring_-_Blender_Open_Movie.webm.jpg",
                type = MediaSourceType.URL
            ),
        ),
    )

    val media: StateFlow<List<MediaSourceFile>> = _media
}