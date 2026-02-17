package com.laposa.common.features.zeroConf

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import com.laposa.common.features.mediaSource.ui.MediaSourceItemContent
import com.laposa.domain.mediaSource.model.MediaSource
import ie.laposa.common.R
import kotlinx.coroutines.launch

@Composable
fun ManualMediaSourceItem(
    setScreenContent: ((@Composable () -> Unit)?) -> Unit,
    navigateToNetwork: () -> Unit,
    viewModel: ZeroConfContentViewModel,
) {
    var error by remember {
        mutableStateOf<String?>(null)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var isDialogVisible by remember {
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
        isDialogVisible = true
    }

    if (isDialogVisible) {
        Dialog(onDismissRequest = { isDialogVisible = false }) {
            Box(modifier = Modifier
                .padding(all = 16.dp)
                .shadow(8.dp, shape = RectangleShape)) {
                AddConnectionDialog(
                    onSubmit = ::onSubmitted,
                    isLoading = isLoading,
                    error = error,
                    defaultValues = testDefaultValues,
                ) {
                    isDialogVisible = false
                }
            }

        }

    }
}

val testDefaultValues = mapOf(
    "name" to "Test",
    "hostName" to "192.168.31.226",
    "userName" to "laposa",
    "password" to "Test1234",
    "port" to "22",
)