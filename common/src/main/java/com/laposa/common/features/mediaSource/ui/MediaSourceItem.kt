package com.laposa.common.features.mediaSource.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.laposa.common.features.home.ui.content.MediaSourceNotConnected
import com.laposa.common.features.mediaLib.ui.MediaLibrary
import com.laposa.domain.mediaSource.model.MediaSourceDirectory
import com.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.common.R
import com.laposa.domain.mediaSource.model.MediaSource as MediaSourceModel

@Composable
fun MediaSourceItem(
    mediaSource: MediaSourceModel,
    setScreenContent: (@Composable () -> Unit) -> Unit,
    key: String,
    navigateToPlayer: ((MediaSourceFile) -> Unit),
    selectedKey: String?,
    onSelected: (key: String) -> Unit,
    viewModelFactory: MediaSourceItemViewModelFactory,
    index: Int,
) {

    val viewModel: MediaSourceItemViewModel = viewModelFactory.create(mediaSource)
    val isLoading = viewModel.isLoading.collectAsState().value

    fun playFile(mediaItem: MediaSourceFile) {
        viewModel.launch {
            viewModel.onFileSelected(mediaItem, navigateToPlayer)
        }
    }

    fun openDirectory(directoryItem: MediaSourceDirectory) {
        viewModel.openDirectory(directoryItem)
    }

    fun openShare(share: String) {
        viewModel.launch {
            viewModel.onSambaShareSelected(share)
        }
    }

    fun onMediaSourceSelected(selected: MediaSourceModel) {
        onSelected(key)

        setScreenContent {
            if (viewModel.isConnected.collectAsState().value) {
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
            } else {
                if (viewModel.isLoginDialogVisible.collectAsState().value) {
                    val error = viewModel.loginDialogError.collectAsState().value
                    MediaSourceLoginDialog(
                        selected.displayName,
                        error,
                        { viewModel.hideLoginDialog() },
                    ) { userName, password, remember ->
                        viewModel.onLoginSubmit(userName, password, remember)
                    }
                } else {
                    MediaSourceNotConnected(mediaSource.displayName) {
                        viewModel.connectToMediaSource()
                    }
                }
            }
        }
    }

    MediaSourceItemContent(
        index = index,
        title = mediaSource.displayName,
        type = mediaSource.type.toString(),
        icon = R.drawable.smb_share,
        onClick = { onMediaSourceSelected(mediaSource) },
    )
}