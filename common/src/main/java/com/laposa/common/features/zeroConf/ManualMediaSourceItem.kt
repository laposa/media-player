package com.laposa.common.features.zeroConf

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.laposa.common.features.mediaSource.ui.MediaSourceItemContent
import com.laposa.domain.mediaSource.model.MediaSource
import ie.laposa.common.R
import kotlinx.coroutines.launch

@Composable
fun ManualMediaSourceItem(
    setScreenContent: (@Composable () -> Unit) -> Unit,
    navigateToNetwork: () -> Unit,
    viewModel: ZeroConfContentViewModel,
) {
    var error by remember {
        mutableStateOf<String?>(null)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    fun onSubmitted(mediaSource: MediaSource) {
        viewModel.viewModelScope.launch {
            isLoading = true
            error = viewModel.addAndConnectMediaSource(mediaSource)
            isLoading = false
        }
    }

    MediaSourceItemContent(
        index = 0, title = "", type = "ADD", icon = R.drawable.ic_add
    ) {
        setScreenContent {
            AddConnectionDialog(
                onSubmit = ::onSubmitted,
                isLoading = isLoading,
                error = error,
            ) {
                navigateToNetwork()
            }
        }
    }
}