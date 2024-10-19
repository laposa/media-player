package com.laposa.common.features.zeroConf

import com.laposa.common.features.common.ViewModelBase
import com.laposa.domain.mediaSource.MediaSourceServiceFactory
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.savedState.SavedStateService
import com.laposa.domain.zeroConf.ZeroConfService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ZeroConfContentViewModel @Inject constructor(
    private val zeroConfService: ZeroConfService,
    private val savedStateService: SavedStateService,
    private val mediaSourceServiceFactory: MediaSourceServiceFactory,
) : ViewModelBase() {

    val mediaSources = zeroConfService.mediaSources

    fun getSavedMediaSources(): List<MediaSource> {
        return savedStateService.getSavedMediaSources()
    }

    suspend fun addAndConnectMediaSource(mediaSource: MediaSource): String? {
        println("Adding and connecting media source: $mediaSource")
        val mediaSourceService = mediaSourceServiceFactory.getOrCreate(mediaSource.type)
        try {
            mediaSourceService.addAndConnectManualMediaSource(mediaSource)
        } catch (e: Exception) {
            return e.message
        }
        return null
    }

    fun fetchMediaSources() {
        launch(false) {
            zeroConfService.fetchMediaSources()
        }
    }
}