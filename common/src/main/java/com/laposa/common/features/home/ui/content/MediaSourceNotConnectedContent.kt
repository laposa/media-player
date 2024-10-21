package com.laposa.common.features.home.ui.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.Text
import com.laposa.common.features.common.composables.LoadingModal
import com.laposa.common.theme.VideoPlayerTypography
import com.laposa.domain.networkProtocols.AuthFailException
import kotlinx.coroutines.launch

@Composable
fun MediaSourceNotConnected(
    title: String,
    onAuthFail: () -> Unit,
    onConnectionFail: (message: String) -> Unit,
    connect: suspend () -> Unit
) {
    val focusRequester = FocusRequester()
    val coroutineScope = rememberCoroutineScope()

    var isLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(title) {
        focusRequester.requestFocus()
    }

    fun onConnectClicked() {
        coroutineScope.launch {
            try {
                isLoading = true
                connect()
            } catch (e: Exception) {
                when {
                    e is AuthFailException -> onAuthFail()
                    else -> onConnectionFail(e.message ?: "Connection failed")
                }
            } finally {
                isLoading = false
            }
        }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$title not connected", style = VideoPlayerTypography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onConnectClicked() },
                modifier = Modifier.focusRequester(focusRequester)
            ) {
                Text("Connect")
            }
        }
    }

    LoadingModal(isLoading = isLoading)
}