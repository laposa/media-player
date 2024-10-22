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
import com.laposa.domain.mediaSource.model.MediaSource

@Composable
fun ZeroConfContent(
    viewModel: ZeroConfContentViewModel = hiltViewModel()
) {
    val mediaSources = viewModel.mediaSources.collectAsState().value
    val mediaSourceItemViewModelFactory = LocalMediaSourceItemViewModelFactory.current
    val homeNavigation = LocalHomeNavigation.current
    val isLoading = viewModel.isLoading.collectAsState().value

    LaunchedEffect(isLoading) {
        println("In ZeroConfContent - isLoading: $isLoading")
    }

    var savedMediaSources by remember {
        mutableStateOf<List<MediaSource>?>(null)
    }

    var mediaSourcesWithoutSaved by remember {
        mutableStateOf<List<MediaSource>>(listOf())
    }

    var content by remember {
        mutableStateOf<(@Composable () -> Unit)?>(null)
    }

    LaunchedEffect(true) {
        viewModel.fetchMediaSources()
        savedMediaSources = viewModel.getSavedMediaSources()
    }

    LaunchedEffect(mediaSources, savedMediaSources) {
        mediaSourcesWithoutSaved = mediaSources - savedMediaSources.orEmpty().toSet()
    }

    fun setContent(newContent: (@Composable () -> Unit)?) {
        content = newContent
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp),
        contentPadding = PaddingValues(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ManualMediaSourceItem(
                ::setContent,
                homeNavigation::navigateToZeroConf,
                viewModel,
            )
        }
        itemsIndexed(savedMediaSources ?: emptyList()) { index, source ->
            MediaSourceItem(
                mediaSource = source,
                key = source.key,
                onSelected = {},
                viewModelFactory = mediaSourceItemViewModelFactory,
                index = index + 1,
            )
        }
        itemsIndexed(mediaSourcesWithoutSaved.sortedBy { it.type.toString() + it.displayName }) { index, source ->
            MediaSourceItem(
                mediaSource = source,
                key = source.key,
                onSelected = {},
                viewModelFactory = mediaSourceItemViewModelFactory,
                index = index + 1,
            )
        }

    }
}