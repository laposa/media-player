package com.laposa.common.features.zeroConf

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.laposa.common.features.mediaSource.ui.MediaSourceItemContent
import ie.laposa.common.R

@Composable
fun ManualMediaSourceItem(
    setScreenContent: (@Composable () -> Unit) -> Unit,
    navigateToNetwork: () -> Unit,
    viewModel: ZeroConfContentViewModel = hiltViewModel()
) {
    MediaSourceItemContent(
        index = 0, title = "", type = "ADD", icon = R.drawable.ic_add
    ) {
        setScreenContent {
            AddConnectionDialog(onSubmit = viewModel::addAndConnectMediaSource) {
                navigateToNetwork()
            }
        }
    }
}