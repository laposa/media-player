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
import androidx.hilt.navigation.compose.hiltViewModel
import ie.laposa.common.features.home.ui.content.MediaSourceNotConnected
import ie.laposa.domain.mediaSource.model.MediaSource

@Composable
fun MediaSourcesLibrary(
    viewModel: MediaSourcesLibraryViewModel = hiltViewModel(),
    setHomeContent: (@Composable () -> Unit) -> Unit,
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
        setHomeContent {
            MediaSourceNotConnected(media.displayName) {
                viewModel.onMediaSourceSelected(media)
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

    fun onSambaShareSelected(share: String) {
        viewModel.onSambaShareSelected(share) {
            setHomeContent {
                MediaSourceFilesLibary(
                    files = files ?: emptyList(),
                    folderName = share,
                    isLoading = files == null
                ) { file ->
                    viewModel.launch {
                        viewModel.onFileSelected(file)
                        navigateToPlayer?.invoke()
                    }
                }
            }
        }
    }

    Column {
        for (source in viewModel.mediaSources.collectAsState().value.sortedBy { it.type.toString() + it.displayName }) {
            val selectedSambaShares = sambaShares?.get(source) ?: emptyList()

            println("Selected media: $sambaShares")

            MediaSource(
                source,
                ::onMediaSourceSelected,
                selectedSambaShares,
                ::onSambaShareSelected,
                selectedMedia == source && isLoading
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}