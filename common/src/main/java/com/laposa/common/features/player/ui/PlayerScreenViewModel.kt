package com.laposa.common.features.player.ui

import com.laposa.common.features.common.ViewModelBase
import com.laposa.domain.mediaSource.MediaSourceServiceFactory
import com.laposa.domain.mediaSource.model.MediaSourceFile
import com.laposa.domain.mediaSource.model.MediaSourceType
import com.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import com.laposa.domain.recents.RecentMedia
import com.laposa.domain.recents.RecentMediaService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlayerScreenViewModel @Inject constructor(
    private val mediaSourceServiceFactory: MediaSourceServiceFactory,
    private val recentMediaService: RecentMediaService,
) : ViewModelBase() {
    private var _currentMediaFile: MutableStateFlow<MediaSourceFile?> = MutableStateFlow(null)
    val currentMediaFile: StateFlow<MediaSourceFile?> = _currentMediaFile

    private val _payload: MutableStateFlow<InputStreamDataSourcePayload?> = MutableStateFlow(null)
    val payload: StateFlow<InputStreamDataSourcePayload?> = _payload

    private val _url: MutableStateFlow<String?> = MutableStateFlow(null)
    val url: StateFlow<String?> = _url

    fun saveLastPlayedMediaToRecents(thumbnailFilePath: String, progress: Long) {
        _currentMediaFile.value?.let { media ->
            recentMediaService.addRecentMedia(
                RecentMedia(
                    mediaSourceType = media.type,
                    file = media,
                    thumbnailPath = thumbnailFilePath,
                    thumbnailUrl = null,
                    progress = progress,
                )
            )
        }
    }

    fun setFileToPlay(fileToPlay: MediaSourceFile) {
        println("Full Path: ${fileToPlay.fullPath}")
        if(_currentMediaFile.value == fileToPlay) return
        _currentMediaFile.value = fileToPlay
        _url.value = null
        _payload.value = null

        launch {
            when (fileToPlay.type) {
                MediaSourceType.URL -> {
                    _url.value = fileToPlay.path
                }

                else -> {
                    val mediaSourceService = mediaSourceServiceFactory.getOrCreate(fileToPlay.type)
                    
                    // Try to get direct URL first (for VLC)
                    val directUrl = mediaSourceService.getDirectUrl(fileToPlay.fullPath)
                    println("Direct URL found: $directUrl")

                    if (directUrl != null) {
                        _url.value = directUrl
                    } else {
                        val payload = mediaSourceService.getFile(fileToPlay.fullPath)
                        _payload.value = payload
                    }
                }
            }
        }
    }

    fun clearFileToPlay() {
        _currentMediaFile.value = null
        _url.value = null
        _payload.value = null
    }
}
