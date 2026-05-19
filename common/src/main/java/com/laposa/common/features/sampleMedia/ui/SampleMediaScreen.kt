package com.laposa.common.features.sampleMedia.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.laposa.common.features.mediaLib.ui.MediaLibrary
import com.laposa.common.ui.Screen
import com.laposa.domain.mediaSource.model.MediaSourceDirectory
import com.laposa.domain.mediaSource.model.MediaSourceFile
import com.laposa.domain.mediaSource.model.MediaSourceShare

@Composable
fun SampleMediaScreen(
    viewModel: SampleMediaScreenViewModel = hiltViewModel(),
    navigateToPlayer: (MediaSourceFile) -> Unit
) {
    fun onMediaSelected(media: MediaSourceFile) {
        navigateToPlayer(media)
    }

    fun onDirectorySelected(dir: MediaSourceDirectory) {
        // nothing
    }

    fun onShareSelected(share: MediaSourceShare) {
        // nothing
    }

    fun onGoUp() {
        // nothing
    }

    Column {
        MediaLibrary(
            title = "Samples",
            files = viewModel.media,
            path = "",
            columnBaseSize = 128,
            defaultPathDepthLevel = 1,
            onMediaFileSelect = ::onMediaSelected,
            onMediaDirectorySelect = ::onDirectorySelected,
            onMediaShareSelect = ::onShareSelected,
            onGoUp = ::onGoUp,
        )
    }
}