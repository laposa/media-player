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
            MediaSourceFile(
                name = "Caminandes",
                path = "https://upload.wikimedia.org/wikipedia/commons/7/7c/Caminandes_-_Gran_Dillama_-_Blender_Foundation%27s_new_Open_Movie.webm",
                thumbnailUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/Caminandes_-_Gran_Dillama_-_Blender_Foundation%27s_new_Open_Movie.webm/1920px--Caminandes_-_Gran_Dillama_-_Blender_Foundation%27s_new_Open_Movie.webm.jpg",
                type = MediaSourceType.URL
            ),
            MediaSourceFile(
                name = "Charge",
                path = "https://upload.wikimedia.org/wikipedia/commons/7/7a/Charge_-_Blender_Open_Movie-full_movie.webm",
                thumbnailUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7a/Charge_-_Blender_Open_Movie-full_movie.webm/1280px--Charge_-_Blender_Open_Movie-full_movie.webm.jpg",
                type = MediaSourceType.URL
            ),
            MediaSourceFile(
                name = "Coffee Run",
                path = "https://upload.wikimedia.org/wikipedia/commons/3/3f/Coffee_Run_-_Blender_Open_Movie-full_movie.webm",
                thumbnailUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Coffee_Run_-_Blender_Open_Movie-full_movie.webm/1920px--Coffee_Run_-_Blender_Open_Movie-full_movie.webm.jpg",
                type = MediaSourceType.URL
            ),
            MediaSourceFile(
                name = "Cosmos Laundromat",
                path = "https://upload.wikimedia.org/wikipedia/commons/3/36/Cosmos_Laundromat_-_First_Cycle_-_Official_Blender_Foundation_release.webm",
                thumbnailUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/Cosmos_Laundromat_-_First_Cycle_-_Official_Blender_Foundation_release.webm/1920px--Cosmos_Laundromat_-_First_Cycle_-_Official_Blender_Foundation_release.webm.jpg",
                type = MediaSourceType.URL
            ),
            MediaSourceFile(
                name = "Glass Half",
                path = "https://upload.wikimedia.org/wikipedia/commons/0/02/Glass_Half_-_Blender_Open_Movie-full_movie.webm",
                thumbnailUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/02/Glass_Half_-_Blender_Open_Movie-full_movie.webm/1280px--Glass_Half_-_Blender_Open_Movie-full_movie.webm.jpg",
                type = MediaSourceType.URL
            ),
            MediaSourceFile(
                name = "Sprite Fight",
                path = "https://upload.wikimedia.org/wikipedia/commons/7/76/Sprite_Fright_-_Blender_Open_Movie-full_movie.webm",
                thumbnailUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/76/Sprite_Fright_-_Blender_Open_Movie-full_movie.webm/1280px--Sprite_Fright_-_Blender_Open_Movie-full_movie.webm.jpg",
                type = MediaSourceType.URL
            ),
            MediaSourceFile(
                name = "Tears of Steel",
                path = "https://upload.wikimedia.org/wikipedia/commons/1/10/Tears_of_Steel_in_4k_-_Official_Blender_Foundation_release.webm",
                thumbnailUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/10/Tears_of_Steel_in_4k_-_Official_Blender_Foundation_release.webm/1280px--Tears_of_Steel_in_4k_-_Official_Blender_Foundation_release.webm.jpg",
                type = MediaSourceType.URL
            ),
        ),
    )

    val media: StateFlow<List<MediaSourceFile>> = _media
}