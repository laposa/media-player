package ie.laposa.common.features.mediaSource.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMap
import androidx.hilt.navigation.compose.hiltViewModel
import ie.laposa.common.features.home.ui.content.MediaSourceNotConnected
import ie.laposa.common.features.mediaLib.model.FileSystemItem
import ie.laposa.common.features.mediaLib.model.Media
import ie.laposa.common.features.mediaLib.ui.MediaLibrary
import ie.laposa.domain.mediaSource.model.MediaSource
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun MediaSourcesLibrary(
    viewModel: MediaSourcesLibraryViewModel = hiltViewModel(),
    setHomeContent: (@Composable () -> Unit) -> Unit,
    navigateToPlayer: (() -> Unit)? = null,
) {
    val sambaShares = viewModel.sambaShares.collectAsState().value
    val selectedMediaSource = viewModel.selectedMediaSource.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val mediaSources = viewModel.mediaSources.collectAsState().value

    var selectedMedia by remember { mutableStateOf<MediaSource?>(null) }

    LaunchedEffect(true) {
        viewModel.fetchMediaSources()
    }

    suspend fun onSambaShareSelected(share: String) {
        viewModel.onSambaShareSelected(share)
    }

    fun playFile(mediaItem: FileSystemItem) {
        viewModel.launch {
            viewModel.onFileSelected(MediaSourceFile(mediaItem.filePath))
            navigateToPlayer?.invoke()
        }
    }

    fun onMediaSourceSelected(media: MediaSource) {
        selectedMedia = media

        if (!media.isConnected) {
            viewModel.launch {
                sambaShares?.get(selectedMediaSource)?.forEach { share ->
                    onSambaShareSelected(share)
                }
            }
        }

        setHomeContent {
            if (media.isConnected) {
                if (selectedMediaSource != null) {
                    Column {
                        sambaShares?.get(selectedMediaSource)?.forEach { share ->
                            MediaLibrary(
                                share,
                                share,
                            ) { mediaItem -> playFile(mediaItem) }
                        }
                    }
                }
            } else {
                MediaSourceNotConnected(media.displayName) {
                    viewModel.onMediaSourceSelected(media)
                }
            }
        }
    }

    if (viewModel.isLoginDialogVisible.collectAsState().value) {
        val error = viewModel.loginDialogError.collectAsState().value
        MediaSourceLoginDialog(
            error,
            onDismiss = viewModel::hideLoginDialog
        ) { userName, password ->
            viewModel.onLoginSubmit(userName, password)
        }
    }

    Column {
        for (source in mediaSources.sortedBy { it.type.toString() + it.displayName }) {
            MediaSource(
                source,
                ::onMediaSourceSelected,
                selectedMedia == source && isLoading
            )
        }
    }
}