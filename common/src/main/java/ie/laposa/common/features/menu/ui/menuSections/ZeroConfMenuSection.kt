package ie.laposa.common.features.menu.ui.menuSections

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import ie.laposa.common.features.mediaSource.ui.LocalMediaSourceItemViewModelFactory
import ie.laposa.common.features.mediaSource.ui.MediaSourceItem

@Composable
fun ZeroConfMenuSection(
    setHomeContent: (@Composable () -> Unit) -> Unit,
    navigateToPlayer: () -> Unit,
    key: String,
    selectedKey: String?,
    onSelected: (key: String) -> Unit,
    viewModel: ZeroConfMenuSectionViewModel = hiltViewModel(),
) {
    val mediaSources = viewModel.mediaSources.collectAsState().value

    LaunchedEffect(true) {
        viewModel.fetchMediaSources()
    }

    val mediaSourceItemViewModelFactory = LocalMediaSourceItemViewModelFactory.current

    MenuSection("Network") {
        Column {
            for (source in mediaSources.sortedBy { it.type.toString() + it.displayName }) {
                MediaSourceItem(
                    mediaSource = source,
                    setHomeContent = setHomeContent,
                    navigateToPlayer = navigateToPlayer,
                    viewModelFactory = mediaSourceItemViewModelFactory,
                    key = "$key-${source.key}",
                    onSelected = onSelected,
                    selectedKey = selectedKey
                )
            }
        }
    }
}