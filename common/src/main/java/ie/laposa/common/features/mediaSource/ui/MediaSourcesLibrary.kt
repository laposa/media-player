package ie.laposa.common.features.mediaSource.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import ie.laposa.common.features.mediaSource.smb.MediaSourceSambaSharesList
import ie.laposa.domain.mediaSource.model.MediaSource

@Composable
fun MediaSourcesLibrary(
    viewModel: MediaSourcesLibraryViewModel = hiltViewModel(),
    navigateToPlayer: () -> Unit
) {

    val sambaShares = viewModel.sambaShares.collectAsState().value
    val selectedSambaShare = viewModel.selectedSambaShare.collectAsState().value
    val files = viewModel.files.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    LaunchedEffect(true) {
        viewModel.fetchMediaSources()
    }

    fun onMediaSourceSelected(media: MediaSource) {
        viewModel.onMediaSourceSelected(media)
    }

    if (viewModel.isLoginDialogVisible.collectAsState().value) {
        MediaSourceLoginDialog { userName, password ->
            viewModel.onLoginSubmit(userName, password)
        }
    }

    if (viewModel.isSambaSharesListVisible.collectAsState().value) {
        MediaSourceSambaSharesList(
            shareList = sambaShares ?: emptyList(),
            isLoading = isLoading || sambaShares == null,
        ) { shareName ->
            viewModel.onSambaShareSelected(shareName)
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
                navigateToPlayer()
            }
        }
    }

    Row {
        for (source in viewModel.mediaSources.collectAsState().value) {
            MediaSource(source, ::onMediaSourceSelected)
        }
    }
}