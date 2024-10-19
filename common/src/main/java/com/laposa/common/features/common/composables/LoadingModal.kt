package com.laposa.common.features.common.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun LoadingModal(
    isLoading: Boolean
) {
    if (isLoading) {
        Dialog(onDismissRequest = { }) {
            Card(
                modifier = Modifier.size(100.dp, 100.dp),
                colors = CardDefaults.cardColors().copy(
                    containerColor = Color.Transparent,
                ),
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        trackColor = Color.Transparent,
                        color = Color.White,
                    )
                }
            }
        }
    }
}