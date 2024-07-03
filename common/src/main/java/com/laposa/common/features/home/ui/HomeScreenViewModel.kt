package com.laposa.common.features.home.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.laposa.domain.mediaSource.model.MediaSourceFile
import com.laposa.domain.mediaSource.model.MediaSourceType
import com.laposa.domain.savedState.SavedStateService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
) : ViewModel() {
    private var _content: MutableStateFlow<(@Composable () -> Unit)?> = MutableStateFlow(null)
    val content: StateFlow<(@Composable () -> Unit)?> = _content

    private val _fileToPlay = MutableStateFlow<MediaSourceFile?>(null)
    val fileToPlay: StateFlow<MediaSourceFile?> = _fileToPlay

    fun showPlayer(fileToPlay: MediaSourceFile) {
        _fileToPlay.value = fileToPlay
    }

    fun hidePlayer() {
        _fileToPlay.value = null
    }
}