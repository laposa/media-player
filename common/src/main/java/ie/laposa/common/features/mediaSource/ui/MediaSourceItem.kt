package ie.laposa.common.features.mediaSource.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import ie.laposa.common.features.home.ui.content.MediaSourceNotConnected
import ie.laposa.common.features.mediaLib.ui.MediaLibrary
import ie.laposa.domain.mediaSource.model.MediaSource as MediaSourceModel
import ie.laposa.domain.mediaSource.model.MediaSourceDirectory
import ie.laposa.domain.mediaSource.model.MediaSourceFile

@Composable
fun MediaSourceItem(
    mediaSource: MediaSourceModel,
    setHomeContent: (@Composable () -> Unit) -> Unit,
    key: String,
    navigateToPlayer: (() -> Unit)? = null,
    selectedKey: String?,
    onSelected: (key: String) -> Unit,
    viewModelFactory: MediaSourceItemViewModelFactory
) {
    val viewModel: MediaSourceItemViewModel = viewModelFactory.create(mediaSource)
    val isLoading = viewModel.isLoading.collectAsState().value

    fun playFile(mediaItem: MediaSourceFile) {
        viewModel.launch {
            viewModel.onFileSelected(mediaItem)
            navigateToPlayer?.invoke()
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

        setHomeContent {
            if (viewModel.isConnected.collectAsState().value) {
                MediaLibrary(mediaSource.displayName,
                    viewModel.files,
                    onMediaFileSelect = { file -> playFile(file) },
                    onMediaDirectorySelect = { directory -> openDirectory(directory) },
                    onMediaShareSelect = { share -> openShare(share.name) })
            } else {
                if (viewModel.isLoginDialogVisible.collectAsState().value) {
                    val error = viewModel.loginDialogError.collectAsState().value
                    MediaSourceLoginDialog(
                        error,
                    ) { userName, password ->
                        viewModel.onLoginSubmit(userName, password)
                    }
                } else {
                    MediaSourceNotConnected(mediaSource.displayName) {
                        viewModel.connectToMediaSource()
                    }
                }
            }
        }
    }

    MediaSource(
        mediaSource,
        ::onMediaSourceSelected,
        isLoading,
        key == selectedKey,
    )
}