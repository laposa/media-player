package ie.laposa.common.features.home.ui.content

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import ie.laposa.common.features.sampleMedia.ui.SampleMediaScreen

@Composable
fun Dashboard(onNavigateToPlayer: () -> Unit) {
    Column {
        SampleMediaScreen(navigateToPlayer = onNavigateToPlayer)
    }
}