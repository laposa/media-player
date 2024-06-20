package com.laposa.domain.mediaSource

import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceFile
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.mediaSource.model.MediaSourceType
import com.laposa.domain.mediaSource.model.nfs.NfsMediaProvider
import com.laposa.domain.mediaSource.model.samba.SambaMediaProvider
import com.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import com.laposa.domain.zeroConf.ZeroConfService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlin.reflect.KSuspendFunction1

class MediaSourceService(
    private val zeroConf: ZeroConfService,
    private val sambaMediaProvider: SambaMediaProvider,
    nfsMediaProvider: NfsMediaProvider,
) {
    private val _mediaSources = MutableStateFlow(emptyList<MediaSource>())
    val mediaSources: StateFlow<List<MediaSource>> = _mediaSources

    private val _mediaProviders: List<MediaSourceProvider> =
        listOf(sambaMediaProvider, nfsMediaProvider)
    private var _currentMediaProvider: MediaSourceProvider? = null

    private val _currentPath = MutableStateFlow<String>("")
    val currentPath: StateFlow<String> = _currentPath

    private var _currentShareName = ""

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
        userName: String? = null,
        password: String? = null,
        remember: Boolean,
    ) {
        _currentMediaProvider = getMediaProvider(mediaSource)

        when (mediaSource.type) {
            is MediaSourceType.ZeroConf -> {
                connectToZeroConfMediaSource(
                    mediaSource,
                    userName,
                    password,
                    remember
                )
            } else -> {}
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
            is MediaSourceType.ZeroConf.SMB -> {
                _mediaProviders.find { it is SambaMediaProvider }
            }

            is MediaSourceType.ZeroConf.NFS -> {
                _mediaProviders.find { it is NfsMediaProvider }
            }

            else -> null
        }
    }

    private suspend fun connectToZeroConfMediaSource(
        mediaSource: MediaSource,
        userName: String? = null,
        password: String? = null,
        remember: Boolean,
    ) {
        val result = _currentMediaProvider?.connectToMediaSource(
            mediaSource, userName, password, remember
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