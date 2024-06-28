package com.laposa.common.features.zeroConf

import dagger.hilt.android.lifecycle.HiltViewModel
import com.laposa.common.features.common.ViewModelBase
import com.laposa.domain.mediaSource.MediaSourceService
import com.laposa.domain.mediaSource.model.ManualMediaSource
import javax.inject.Inject

@HiltViewModel
class ZeroConfContentViewModel @Inject constructor(
    private val mediaSourceService: MediaSourceService,
) : ViewModelBase() {
    val mediaSources = mediaSourceService.mediaSources
    fun addAndConnectMediaSource(mediaSource: ManualMediaSource) {
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