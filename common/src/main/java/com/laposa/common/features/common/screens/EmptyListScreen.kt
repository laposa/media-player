package com.laposa.common.features.common.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.laposa.common.theme.VideoPlayerTypography
import ie.laposa.common.R

@Composable
fun EmptyListScreen(title: String? = null) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.ic_draft),
                contentDescription = "empty screen icon",
                Modifier
                    .size(56.dp),
                tint = MaterialTheme.colorScheme.border
            )
            Text(
                title ?: "No items to show",
                style = VideoPlayerTypography.headlineMedium.copy(color = MaterialTheme.colorScheme.border)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}