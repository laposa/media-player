package ie.laposa.common.features.mediaSource.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import ie.laposa.domain.mediaSource.model.MediaSource

@Composable
fun MediaSourcesLibrary(
    viewModel: MediaSourcesLibraryViewModel = hiltViewModel(),
    navigateToPlayer: (() -> Unit)? = null,
) {

    val sambaShares = viewModel.sambaShares.collectAsState().value
    val selectedSambaShare = viewModel.selectedSambaShare.collectAsState().value
    val files = viewModel.files.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    var selectedMedia by remember { mutableStateOf<MediaSource?>(null) }

    LaunchedEffect(true) {
        viewModel.fetchMediaSources()
    }

    fun onMediaSourceSelected(media: MediaSource) {
        selectedMedia = media
        viewModel.onMediaSourceSelected(media)
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

    if (viewModel.isFilesLibraryVisible.collectAsState().value) {
        MediaSourceFilesLibary(
            files = files ?: emptyList(),
            folderName = selectedSambaShare ?: "",
            isLoading = isLoading || files == null
        ) { file ->
            viewModel.launch {
                viewModel.onFileSelected(file)
                navigateToPlayer?.invoke()
            }
        }
    }

    Column {
        for (source in viewModel.mediaSources.collectAsState().value.sortedBy { it.displayName }) {
            val selectedSambaShares =
                if (selectedMedia == source && sambaShares != null) sambaShares else emptyList()

            MediaSource(
                source,
                ::onMediaSourceSelected,
                selectedSambaShares,
                viewModel::onSambaShareSelected,
                selectedMedia == source && isLoading
            )
        }
    }
}