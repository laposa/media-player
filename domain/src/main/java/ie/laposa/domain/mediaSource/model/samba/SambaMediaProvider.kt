package ie.laposa.domain.mediaSource.model.samba

import ie.laposa.domain.mediaSource.MediaSourceProvider
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import ie.laposa.domain.networkProtocols.smb.SmbService
import kotlinx.coroutines.flow.StateFlow

class SambaMediaProvider(
    private val smbService: SmbService,
) : MediaSourceProvider() {
    override val filesList: StateFlow<List<MediaSourceFile>?> = smbService.filesList
    val shares: StateFlow<List<String>?> = smbService.shares
    override suspend fun connectToMediaSource(
        serverName: String,
        userName: String?,
        password: String?
    ) {
        smbService.connect(serverName, userName, password)
    }

    override suspend fun getFile(fileName: String): InputStreamDataSourcePayload? {
        val file = smbService.openFile(fileName)
        println("File opened: $file")
        return file
    }

    suspend fun openShare(shareName: String) {
        smbService.openShare(shareName)
    }
}