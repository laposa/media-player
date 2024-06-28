package com.laposa.domain.mediaSource

import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.mediaSource.model.MediaSourceType
import com.laposa.domain.mediaSource.nfs.NfsMediaProvider
import com.laposa.domain.mediaSource.samba.SambaMediaProvider
import com.laposa.domain.mediaSource.sftp.SftpMediaProvider
import com.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import com.laposa.domain.savedState.SavedStateService
import com.laposa.domain.zeroConf.ZeroConfService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

class MediaSourceService(
    private val zeroConf: ZeroConfService,
    private val sambaMediaProvider: SambaMediaProvider,
    private val sftpMediaProvider: SftpMediaProvider,
    nfsMediaProvider: NfsMediaProvider,
    private val savedStateService: SavedStateService,
) {
    private val _mediaSources = MutableStateFlow(emptyList<MediaSource>())
    val mediaSources: StateFlow<List<MediaSource>> = _mediaSources

    private val _mediaProviders: List<MediaSourceProvider> =
        listOf(sambaMediaProvider, nfsMediaProvider, sftpMediaProvider)
    private var _currentMediaProvider: MediaSourceProvider? = null

    private val _currentPath = MutableStateFlow<String>("")
    val currentPath: StateFlow<String> = _currentPath

    private var _currentShareName = ""

    suspend fun getSavedMediaSources(): List<MediaSource> {
        return savedStateService.getSavedMediaSources()
    }

    suspend fun addAndConnectManualMediaSource(
        manualMediaSource: MediaSource,
    ) {
        val result = sftpMediaProvider.connectToMediaSource(
            manualMediaSource,
            true
        )

        if (result) {
            savedStateService.addSavedMediaSources(manualMediaSource)
        }
    }

    private fun updateCurrentPath(newPath: String, fromGoBack: Boolean = false) {
        if (newPath != _currentPath.value && !fromGoBack) {
            _currentPath.value += if (!newPath.contains("/") && _currentPath.value.isNotEmpty()) "/$newPath" else newPath
        } else {
            _currentPath.value = newPath
        }
    }

    private fun getOneLevelUpFromCurrentPath(): String {
        val pathSplit = _currentPath.value.split("/")
        val pathOneLevelUpSplit = pathSplit.take(pathSplit.size - 1)
        return pathOneLevelUpSplit.joinToString("/")
    }


    suspend fun goBack(): Pair<String, List<MediaSourceFileBase>> {
        updateCurrentPath(getOneLevelUpFromCurrentPath(), true)
        return getContentOfDirectoryAthPath(_currentPath.value)
    }

    suspend fun getContentOfDirectoryAthPath(path: String): Pair<String, List<MediaSourceFileBase>> {
        updateCurrentPath(path)
        return _currentPath.value to (_currentMediaProvider?.getContentOfDirectoryAtPath(
            _currentPath.value
        )
            ?: emptyList())
    }

    suspend fun openShare(shareName: String): List<MediaSourceFileBase> {
        updateCurrentPath(shareName)
        _currentShareName = shareName
        return sambaMediaProvider.openShare(shareName)
    }

    suspend fun getFile(fileName: String): InputStreamDataSourcePayload? {
        return _currentMediaProvider?.getFile(fileName)
    }

    suspend fun fetchMediaSources() {
        zeroConf.startDiscovery()

        zeroConf.discoveredServices.collectLatest { services ->
            _mediaSources.value = services.map {
                MediaSource.fromNSD(it)
            }
        }
    }

    suspend fun connectToMediaSource(
        mediaSource: MediaSource,
        remember: Boolean,
    ) {
        _currentMediaProvider = getMediaProvider(mediaSource)

        when {
            mediaSource.type.isZeroConf -> {
                connectToZeroConfMediaSource(
                    mediaSource,
                    remember
                )
            }

            else -> {}
        }
    }

    suspend fun tryToConnectToMediaSource(mediaSource: MediaSource): Boolean {
        _currentMediaProvider = getMediaProvider(mediaSource)

        return _currentMediaProvider?.let {
            if (!it.connectToMediaSourceAsAGuest(mediaSource)) {
                if (!it.connectToMediaSourceWithRememberedLogin(mediaSource)) {
                    return@let false
                }
            }

            markMediaSourceAsConnected(mediaSource)
            return@let true
        } ?: false
    }

    private fun getMediaProvider(mediaSource: MediaSource): MediaSourceProvider? {
        return when (mediaSource.type) {

            MediaSourceType.SMB -> {
                _mediaProviders.find { it is SambaMediaProvider }
            }

            MediaSourceType.NSF -> {
                _mediaProviders.find { it is NfsMediaProvider }
            }

            MediaSourceType.SFTP -> {
                _mediaProviders.find { it is SftpMediaProvider }
            }

            else -> throw IllegalArgumentException("Unsupported media source type")
        } ?: throw IllegalArgumentException("Could not find media provider for ${mediaSource.type}")
    }

    private suspend fun connectToZeroConfMediaSource(
        mediaSource: MediaSource,
        remember: Boolean,
    ) {
        val result = _currentMediaProvider?.connectToMediaSource(
            mediaSource, remember
        ) == true

        if (result) {
            markMediaSourceAsConnected(mediaSource)
        }
    }

    private fun markMediaSourceAsConnected(mediaSource: MediaSource) {
        _mediaSources.value = _mediaSources.value.map {
            if (it == mediaSource) {
                it.copy(isConnected = true)
            } else {
                it
            }
        }
    }
}