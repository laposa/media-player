package ie.laposa.common.features.home.ui.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import ie.laposa.common.features.home.ui.HomeScreenViewModel

@Composable
fun HomeContent(
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val currentContent = viewModel.content.collectAsState()
    currentContent.value?.invoke()
}