package com.laposa.common.features.mediaSource.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import ie.laposa.common.R
import com.laposa.common.features.home.ui.content.MediaSourceNotConnected
import com.laposa.common.features.mediaLib.ui.MediaLibrary
import com.laposa.domain.mediaSource.model.MediaSource as MediaSourceModel
import com.laposa.domain.mediaSource.model.MediaSourceDirectory
import com.laposa.domain.mediaSource.model.MediaSourceFile

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