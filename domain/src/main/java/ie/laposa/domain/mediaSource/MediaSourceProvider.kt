package ie.laposa.domain.mediaSource

import ie.laposa.domain.mediaSource.model.MediaSource
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.mediaSource.model.MediaSourceFileBase
import ie.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class MediaSourceProvider {
    abstract val filesList: StateFlow<Map<String, List<MediaSourceFile>>>
    abstract suspend fun connectToMediaSource(
        mediaSource: MediaSource,
        userName: String? = null,
        password: String? = null
    ): Boolean

    abstract suspend fun getFile(
        fileName: String
    ): InputStreamDataSourcePayload?

    abstract suspend fun openShare(shareName: String): List<MediaSourceFileBase>

    abstract suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase>
}