package com.laposa.common.features.home.ui.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.Text
import com.laposa.common.theme.VideoPlayerTypography

@Composable
fun MediaSourceNotConnected(title: String, onConnectClick: () -> Unit) {
    val focusRequester = FocusRequester()

    LaunchedEffect(title) {
        focusRequester.requestFocus()
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$title not connected", style = VideoPlayerTypography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onConnectClick, modifier = Modifier.focusRequester(focusRequester)) {
                Text("Connect")
            }
        }
    }
}