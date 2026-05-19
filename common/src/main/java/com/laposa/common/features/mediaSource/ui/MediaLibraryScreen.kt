package com.laposa.common.features.mediaSource.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.Text
import com.laposa.common.features.common.composables.LoadingModal
import com.laposa.common.features.common.composables.MyTextField
import com.laposa.common.features.home.ui.LocalHomeNavigation
import com.laposa.common.features.mediaLib.ui.MediaLibrary
import com.laposa.common.ui.theme.ComponentsTheme
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

    // When arriving here directly from AddConnectionDialog, ViewModel B is freshly created
    // and has never been connected (addAndConnectMediaSource ran on ViewModel A). Trigger
    // connect + content load automatically so the screen isn't empty on first show.
    val isConnected by viewModel.isConnected.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    LaunchedEffect(viewModel) {
        if (!isConnected) {
            viewModel.launch {
                viewModel.connectToMediaSource()
            }
        }
    }

    var showShareNameDialog by remember { mutableStateOf(false) }
    var shareNameInput by remember { mutableStateOf("") }

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

    if (showShareNameDialog) {
        BackHandler { showShareNameDialog = false }
        Dialog(onDismissRequest = { showShareNameDialog = false }) {
            Surface(
                modifier = Modifier.width(500.dp),
                shape = RoundedCornerShape(12.dp),
                colors = SurfaceDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Enter share name",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    MyTextField(
                        value = shareNameInput,
                        onValueChange = { shareNameInput = it },
                        label = { Text("Share name") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ComponentsTheme.textInputColors(),
                        singleLine = true,
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Row {
                        Button(
                            onClick = {
                                showShareNameDialog = false
                                if (shareNameInput.isNotBlank()) {
                                    openShare(shareNameInput.trim())
                                }
                            },
                            border = ButtonDefaults.border(
                                border = Border(
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.border)
                                )
                            ),
                        ) { Text("Connect") }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = { showShareNameDialog = false },
                            border = ButtonDefaults.border(
                                border = Border(
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.border)
                                )
                            ),
                        ) { Text("Cancel") }
                    }
                }
            }
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
        onEnterShareNameSelect = {
            shareNameInput = ""
            showShareNameDialog = true
        },
        onGoUp = viewModel::goUp
    )

    LoadingModal(isLoading)
}