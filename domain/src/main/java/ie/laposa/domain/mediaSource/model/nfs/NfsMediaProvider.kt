package ie.laposa.domain.mediaSource.model.nfs

import ie.laposa.domain.mediaSource.MediaSourceProvider
import ie.laposa.domain.mediaSource.model.MediaSource
import ie.laposa.domain.mediaSource.model.MediaSourceFile
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

    override suspend fun connectToMediaSource(
        mediaSource: MediaSource,
        userName: String?,
        password: String?
    ): Boolean {
        nfsService.connect(mediaSource)
        return true
    }

    override suspend fun getFile(fileName: String): InputStreamDataSourcePayload? {
        TODO("Not yet implemented")
    }
}