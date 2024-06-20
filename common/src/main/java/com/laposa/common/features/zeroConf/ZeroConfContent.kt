package com.laposa.common.features.zeroConf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.laposa.common.features.home.ui.LocalHomeNavigation
import com.laposa.common.features.mediaSource.ui.LocalMediaSourceItemViewModelFactory
import com.laposa.common.features.mediaSource.ui.MediaSourceItem

@Composable
fun ZeroConfContent(
    viewModel: ZeroConfContentViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        viewModel.fetchMediaSources()
    }

    val mediaSources = viewModel.mediaSources.collectAsState().value
    val mediaSourceItemViewModelFactory = LocalMediaSourceItemViewModelFactory.current
    val homeNavigation = LocalHomeNavigation.current

    var content by remember {
        mutableStateOf<(@Composable () -> Unit)?>(null)
    }

    fun setContent(newContent: @Composable () -> Unit) {
        content = newContent
    }

    if (content != null) {
        content!!()
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(mediaSources.sortedBy { it.type.toString() + it.displayName }) { index, source ->
                MediaSourceItem(
                    mediaSource = source,
                    setHomeContent = ::setContent,
                    key = source.key,
                    selectedKey = null,
                    onSelected = {},
                    viewModelFactory = mediaSourceItemViewModelFactory,
                    index = index,
                    navigateToPlayer = homeNavigation::navigateToPlayer
                )
            }
        }
    }
}