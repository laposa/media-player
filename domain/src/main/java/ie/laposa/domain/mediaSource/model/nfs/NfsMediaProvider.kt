package ie.laposa.domain.mediaSource.model.nfs

import ie.laposa.domain.mediaSource.MediaSourceProvider
import ie.laposa.domain.mediaSource.model.MediaSource
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.mediaSource.model.MediaSourceFileBase
import ie.laposa.domain.networkProtocols.nfs.NfsService
import ie.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class NfsMediaProvider(
    private val nfsService: NfsService
) : MediaSourceProvider() {
    override val filesList: StateFlow<Map<String, List<MediaSourceFile>>> = MutableStateFlow(
        emptyMap()
    )

    override suspend fun connectToMediaSourceAsAGuest(mediaSource: MediaSource): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun connectToMediaSourceWithRememberedLogin(mediaSource: MediaSource): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun connectToMediaSource(
        mediaSource: MediaSource,
        userName: String?,
        password: String?,
        remember: Boolean,
    ): Boolean {
        //nfsService.connect(mediaSource)
        return true
    }

    override suspend fun getFile(fileName: String): InputStreamDataSourcePayload? {
        TODO("Not yet implemented")
    }

    override suspend fun openShare(shareName: String): List<MediaSourceFileBase> {
        TODO("Not yet implemented")
    }

    override suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase> {
        TODO("Not yet implemented")
    }
}