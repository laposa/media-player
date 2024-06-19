package ie.laposa.domain.mediaSource.model.samba

import ie.laposa.domain.mediaSource.MediaSourceProvider
import ie.laposa.domain.mediaSource.model.MediaSource
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.mediaSource.model.MediaSourceFileBase
import ie.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import ie.laposa.domain.networkProtocols.smb.SmbService
import kotlinx.coroutines.flow.StateFlow

class SambaMediaProvider(
    private val smbService: SmbService,
) : MediaSourceProvider() {
    override val filesList: StateFlow<Map<String, List<MediaSourceFile>>> = smbService.filesList
    val shares: StateFlow<Map<MediaSource, List<String>>?> = smbService.shares

    private var currentShare: String? = null

    override suspend fun connectToMediaSource(
        mediaSource: MediaSource,
        userName: String?,
        password: String?
    ): Boolean {
        return smbService.connect(mediaSource, userName, password)
    }

    override suspend fun getFile(fileName: String): InputStreamDataSourcePayload? {
        return smbService.openFile(fileName)
    }

    override suspend fun openShare(shareName: String): List<MediaSourceFileBase> {
        currentShare = shareName
        return smbService.openShare(shareName)
    }

    override suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase> {
        return if (path == "" && currentShare != null) {
            smbService.getCurrentShares()
        } else {
            smbService.getContentOfDirectoryAtPath(path)
        }
    }
}