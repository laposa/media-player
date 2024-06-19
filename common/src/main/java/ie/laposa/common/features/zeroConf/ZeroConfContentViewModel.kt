package ie.laposa.common.features.zeroConf

import dagger.hilt.android.lifecycle.HiltViewModel
import ie.laposa.common.features.common.ViewModelBase
import ie.laposa.common.features.common.ui.SavedStateHandleViewModel
import ie.laposa.domain.mediaSource.MediaSourceService
import ie.laposa.domain.mediaSource.model.MediaSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ZeroConfContentViewModel @Inject constructor(
    private val mediaSourceService: MediaSourceService,
    private val savedStateHandleViewModel: SavedStateHandleViewModel
) : ViewModelBase() {
    val mediaSources = mediaSourceService.mediaSources

    private val _connectedMediaSource = MutableStateFlow<MediaSource?>(null)
    val connectedMediaSource: StateFlow<MediaSource?> = _connectedMediaSource

    fun selectMediaSource(mediaSource: MediaSource) {
        _connectedMediaSource.value = mediaSource
    }

    fun fetchMediaSources() {
        launch(false) {
            mediaSourceService.fetchMediaSources()
        }
    }
}