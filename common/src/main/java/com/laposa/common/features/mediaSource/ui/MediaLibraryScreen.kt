package com.laposa.common.features.mediaSource.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.laposa.common.features.home.ui.LocalHomeNavigation
import com.laposa.common.features.mediaLib.ui.MediaLibrary
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceDirectory
import com.laposa.domain.mediaSource.model.MediaSourceFile

@Composable
fun MediaLibraryScreen(
    mediaSource: MediaSource,
) {
    val homeNavigation = LocalHomeNavigation.current
    val mediaSourceItemViewModelFactory = LocalMediaSourceItemViewModelFactory.current
    val viewModel: MediaSourceItemViewModel = mediaSourceItemViewModelFactory.create(mediaSource)

    fun playFile(mediaItem: MediaSourceFile) {
        homeNavigation.navigateToPlayer(mediaItem)
    }

    fun openDirectory(directoryItem: MediaSourceDirectory) {
        viewModel.openDirectory(directoryItem)
    }

    fun openShare(share: String) {
        viewModel.launch {
            viewModel.onSambaShareSelected(share)
        }
    }

    MediaLibrary(
        title = mediaSource.displayName,
        files = viewModel.files,
        path = viewModel.path.collectAsState().value,
        columnBaseSize = 98,
        onMediaFileSelect = { file -> playFile(file) },
        onMediaDirectorySelect = { directory -> openDirectory(directory) },
        onMediaShareSelect = { share -> openShare(share.name) },
        onGoUp = viewModel::goUp
    )
}