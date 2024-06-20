package com.laposa.common.features.zeroConf

import dagger.hilt.android.lifecycle.HiltViewModel
import com.laposa.common.features.common.ViewModelBase
import com.laposa.domain.mediaSource.MediaSourceService
import javax.inject.Inject

@HiltViewModel
class ZeroConfContentViewModel @Inject constructor(
    private val mediaSourceService: MediaSourceService,
) : ViewModelBase() {
    val mediaSources = mediaSourceService.mediaSources

    fun fetchMediaSources() {
        launch(false) {
            mediaSourceService.fetchMediaSources()
        }
    }
}