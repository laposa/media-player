package com.laposa.common.features.zeroConf

import com.laposa.common.features.common.ViewModelBase
import com.laposa.domain.mediaSource.MediaSourceService
import com.laposa.domain.mediaSource.model.MediaSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ZeroConfContentViewModel @Inject constructor(
    private val mediaSourceService: MediaSourceService,
) : ViewModelBase() {
    val mediaSources = mediaSourceService.mediaSources
    suspend fun getSavedMediaSources(): List<MediaSource> {
        return mediaSourceService.getSavedMediaSources()
    }

    fun addAndConnectMediaSource(mediaSource: MediaSource) {
        println("Adding and connecting media source: $mediaSource")
        launch {
            mediaSourceService.addAndConnectManualMediaSource(mediaSource)
        }
    }

    fun fetchMediaSources() {
        launch(false) {
            mediaSourceService.fetchMediaSources()
        }
    }
}