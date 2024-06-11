package ie.laposa.domain.mediaSource

import ie.laposa.domain.mediaSource.model.MediaSource
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.mediaSource.model.MediaSourceType
import ie.laposa.domain.mediaSource.model.nfs.NfsMediaProvider
import ie.laposa.domain.mediaSource.model.samba.SambaMediaProvider
import ie.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import ie.laposa.domain.zeroConf.ZeroConfService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

class MediaSourceService(
    private val zeroConf: ZeroConfService,
    private val sambaMediaProvider: SambaMediaProvider,
    private val nfsMediaProvider: NfsMediaProvider,
) {
    private val _mediaSources = MutableStateFlow(emptyList<MediaSource>())
    val mediaSources: StateFlow<List<MediaSource>> = _mediaSources

    private val _mediaProviders: List<MediaSourceProvider> =
        listOf(sambaMediaProvider, nfsMediaProvider)
    private var _currentMediaProvider: MediaSourceProvider? = null

    val fileList: StateFlow<List<MediaSourceFile>?> = sambaMediaProvider.filesList
    val sambaShares: StateFlow<Map<MediaSource, List<String>>?> = sambaMediaProvider.shares

    suspend fun openShare(shareName: String) {
        sambaMediaProvider.openShare(shareName)
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
        password: String? = null
    ) {
        _currentMediaProvider = getMediaProvider(mediaSource)

        when (mediaSource.type) {
            is MediaSourceType.ZeroConf -> {
                connectToZeroConfMediaSource(
                    mediaSource,
                    userName,
                    password,
                )
            }
        }
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
        password: String? = null
    ) {
        _currentMediaProvider?.connectToMediaSource(
            mediaSource,
            userName,
            password
        )
    }
}