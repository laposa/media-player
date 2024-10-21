package com.laposa.domain.mediaSource

import androidx.lifecycle.SavedStateHandle
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

class MediaSourceService(
    private val provider: MediaSourceProvider,
    private val zeroConf: ZeroConfService,
    private val savedStateService: SavedStateService,
) {
    private val _mediaSources = MutableStateFlow(emptyList<MediaSource>())
    val mediaSources: StateFlow<List<MediaSource>> = _mediaSources

    private val _currentPath = MutableStateFlow<String>("")
    val currentPath: StateFlow<String> = _currentPath

    private var _currentShareName = ""
    suspend fun addAndConnectManualMediaSource(
        manualMediaSource: MediaSource,
    ) {
        val result = provider.connectToMediaSource(
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
        return _currentPath.value to (provider.getContentOfDirectoryAtPath(
            _currentPath.value
        )
            ?: emptyList())
    }

    suspend fun openShare(shareName: String): List<MediaSourceFileBase> {
        updateCurrentPath(shareName)
        _currentShareName = shareName
        return provider.openShare(shareName)
    }

    suspend fun getFile(fileName: String): InputStreamDataSourcePayload? {
        return provider.getFile(fileName)
    }


    suspend fun connectToMediaSource(
        mediaSource: MediaSource,
        remember: Boolean,
    ) {
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
        return provider.let {
            if (!it.connectToMediaSourceAsAGuest(mediaSource)) {
                if (!it.connectToMediaSourceWithRememberedLogin(mediaSource)) {
                    return@let false
                }
            }

            markMediaSourceAsConnected(mediaSource)
            return@let true
        }
    }

    private suspend fun connectToZeroConfMediaSource(
        mediaSource: MediaSource,
        remember: Boolean,
    ) {
        val result = provider.connectToMediaSource(
            mediaSource, remember
        )

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

class MediaSourceServiceFactory(
    private val savedStateHandle: SavedStateHandle,
    private val zeroConfService: ZeroConfService,
    private val savedStateService: SavedStateService,
    private val sambaMediaProvider: SambaMediaProvider,
    private val nfsMediaProvider: NfsMediaProvider,
    private val sftpMediaProvider: SftpMediaProvider,
) {
    private var instances: MutableMap<String, MediaSourceService> = mutableMapOf()

    init {
        instances = savedStateHandle[INSTANCES_MAP_KEY] ?: mutableMapOf()
    }

    fun getOrCreate(mediaSourceType: MediaSourceType): MediaSourceService {
        val provider = getMediaSourceProvider(mediaSourceType)
        val result = instances[getInstanceKey(provider)] ?: createInstance(provider)
        return result
    }

    private fun createInstance(provider: MediaSourceProvider): MediaSourceService {
        instances[getInstanceKey(provider)] = MediaSourceService(
            provider,
            zeroConfService,
            savedStateService
        )
        savedStateHandle[INSTANCES_MAP_KEY] = instances
        return instances[getInstanceKey(provider)]!!
    }

    private fun getInstanceKey(provider: MediaSourceProvider): String {
        return provider::class.simpleName!!
    }

    private fun getMediaSourceProvider(mediaSourceType: MediaSourceType): MediaSourceProvider {
        return when (mediaSourceType) {
            MediaSourceType.SMB -> sambaMediaProvider
            MediaSourceType.NSF -> nfsMediaProvider
            MediaSourceType.SFTP -> sftpMediaProvider
            else -> throw IllegalArgumentException("Unknown media source type")
        }
    }

    companion object {
        const val INSTANCES_MAP_KEY = "MediaSourceServiceFactory"
    }
}

