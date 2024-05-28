package ie.laposa.domain.mediaSource

import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import kotlinx.coroutines.flow.Flow

abstract class MediaSourceProvider {
    abstract val filesList: Flow<List<MediaSourceFile>?>
    abstract suspend fun connectToMediaSource(
        serverName: String,
        userName: String? = null,
        password: String? = null
    )

    abstract suspend fun getFile(
        fileName: String
    ): InputStreamDataSourcePayload?
}