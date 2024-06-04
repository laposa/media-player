package ie.laposa.domain.mediaSource.model.nfs

import ie.laposa.domain.mediaSource.MediaSourceProvider
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.networkProtocols.nfs.NfsService
import ie.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NfsMediaProvider(
    private val nfsService: NfsService
) : MediaSourceProvider() {
    override val filesList: Flow<List<MediaSourceFile>?> = flow {}

    override suspend fun connectToMediaSource(
        serverName: String,
        userName: String?,
        password: String?
    ) {
        nfsService.connect(serverName)
    }

    override suspend fun getFile(fileName: String): InputStreamDataSourcePayload? {
        TODO("Not yet implemented")
    }
}