package com.laposa.common.features.mediaSource.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewModelScope
import com.laposa.common.features.common.composables.LoadingModal
import com.laposa.common.features.home.ui.LocalSnackbarHost
import com.laposa.common.features.home.ui.content.MediaSourceNotConnected
import com.laposa.common.features.mediaLib.ui.MediaLibrary
import com.laposa.common.features.zeroConf.AddConnectionDialog
import com.laposa.common.features.zeroConf.LocalZeroConfNavigation
import com.laposa.common.features.zeroConf.testDefaultValues
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceDirectory
import com.laposa.domain.mediaSource.model.MediaSourceFile
import com.laposa.domain.networkProtocols.AuthFailException
import ie.laposa.common.R
import kotlinx.coroutines.launch
import com.laposa.domain.mediaSource.model.MediaSource as MediaSourceModel

@Composable
fun MediaSourceItem(
    mediaSource: MediaSourceModel,
    key: String,
    onSelected: (key: String) -> Unit,
    viewModelFactory: MediaSourceItemViewModelFactory,
    index: Int,
) {
    val snackbarHost = LocalSnackbarHost.current
    val zeroConfNavigation = LocalZeroConfNavigation.current

    val viewModel: MediaSourceItemViewModel = viewModelFactory.create(mediaSource)
    val isLoading = viewModel.isLoading.collectAsState().value
    val isDialogVisible = viewModel.isLoginDialogVisible.collectAsState().value

    var dialogDefaultValues by remember {
        mutableStateOf(mapOf<String, String>())
    }

    fun onConnectionFailed(message: String) {
        viewModel.launch {
            snackbarHost.showSnackbar(message = message)
        }
    }

    fun onSubmitted(mediaSource: MediaSource) {
        viewModel.launch {
            viewModel.addAndConnectMediaSource(mediaSource)
            zeroConfNavigation.navigateToMediaLibrary(mediaSource)
        }
    }

    fun showLoginDialog() {
        dialogDefaultValues = mapOf(
            "name" to mediaSource.displayName,
            "hostName" to mediaSource.connectionAddress,
            "userName" to mediaSource.username,
            "password" to mediaSource.password,
            "port" to mediaSource.port.toString(),
            "connectionType" to mediaSource.type.toString()
        ).filter { it.value != null } as Map<String, String>
        viewModel.showLoginDialog()
    }

    fun onMediaSourceSelected() {
        onSelected(key)
        viewModel.launch {
            if (viewModel.isConnected.value) {
                zeroConfNavigation.navigateToMediaLibrary(mediaSource)
            } else {
                try {
                    if (viewModel.connectToMediaSource()) {
                        zeroConfNavigation.navigateToMediaLibrary(mediaSource)
                    } else {
                        showLoginDialog()
                    }
                } catch (e: Exception) {
                    when {
                        e is AuthFailException -> {
                            showLoginDialog()
                        }

                        else -> {
                            onConnectionFailed(e.message ?: "Unknown error")
                        }
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.hideLoginDialog()
        }
    }

    MediaSourceItemContent(
        index = index,
        title = mediaSource.displayName,
        subtitle = mediaSource.connectionAddress.toString(),
        type = mediaSource.type.toString(),
        icon = R.drawable.smb_share,
        onClick = { onMediaSourceSelected() },
    )

    if (isDialogVisible) {
        Dialog(onDismissRequest = { viewModel.hideLoginDialog() }) {
            Box(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .shadow(8.dp, shape = RectangleShape)
            ) {
                AddConnectionDialog(
                    onSubmit = ::onSubmitted,
                    isLoading = isLoading,
                    error = null,
                    defaultValues = dialogDefaultValues,
                ) {
                    viewModel.hideLoginDialog()
                }
            }
        }
    }

    LoadingModal(isLoading)
}