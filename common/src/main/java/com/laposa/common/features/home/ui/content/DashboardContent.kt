package com.laposa.common.features.home.ui.content

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.laposa.common.features.recents.ui.RecentsLib
import com.laposa.common.features.sampleMedia.ui.SampleMediaScreen
import com.laposa.domain.mediaSource.model.MediaSourceFile

@Composable
fun Dashboard(onNavigateToPlayer: (MediaSourceFile) -> Unit) {
    Column {
        RecentsLib()
        SampleMediaScreen(navigateToPlayer = onNavigateToPlayer)
    }
}