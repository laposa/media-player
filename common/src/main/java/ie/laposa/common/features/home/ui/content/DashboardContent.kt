package ie.laposa.common.features.home.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ie.laposa.common.features.mediaLib.model.Media
import ie.laposa.common.features.sampleMedia.ui.SampleMediaScreen
import ie.laposa.domain.mediaSource.model.MediaSourceFile

@Composable
fun Dashboard(onNavigateToPlayer: (MediaSourceFile?, String?) -> Unit) {
    Column {
        SampleMediaScreen(navigateToPlayer = onNavigateToPlayer)
    }
}