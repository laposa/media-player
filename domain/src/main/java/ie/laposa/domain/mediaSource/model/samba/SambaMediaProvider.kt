package ie.laposa.domain.mediaSource.model.samba

import ie.laposa.domain.mediaSource.MediaSourceProvider
import ie.laposa.domain.mediaSource.model.MediaSource
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.mediaSource.model.MediaSourceFileBase
import ie.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import ie.laposa.domain.networkProtocols.smb.SmbService
import ie.laposa.domain.rememberLogin.RememberLoginService
import kotlinx.coroutines.flow.StateFlow

class SambaMediaProvider(
    private val smbService: SmbService,
    private val rememberLoginService: RememberLoginService,
) : MediaSourceProvider() {
    override val filesList: StateFlow<Map<String, List<MediaSourceFile>>> = smbService.filesList
    val shares: StateFlow<Map<MediaSource, List<String>>?> = smbService.shares

    private var currentShare: String? = null

    override suspend fun connectToMediaSource(
        mediaSource: MediaSource,
        userName: String?,
        password: String?,
        remember: Boolean,
    ): Boolean {
        val result = smbService.connect(mediaSource, userName, password)

        if (result && remember) {
            rememberLogin(mediaSource, userName, password)
        }

        return result
    }

    override suspend fun getFile(fileName: String): InputStreamDataSourcePayload? {
        return smbService.openFile(fileName)
    }

    override suspend fun openShare(shareName: String): List<MediaSourceFileBase> {
        currentShare = shareName
        return smbService.openShare(shareName)
    }

    override suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase> {
        return if (path == "") {
            smbService.getCurrentShares()
        } else {
            smbService.getContentOfDirectoryAtPath(getPathWithoutShareName(path))
        }
    }

    override suspend fun connectToMediaSourceAsAGuest(mediaSource: MediaSource): Boolean {
        val result = connectToMediaSource(mediaSource, null, null, false)
        return result
    }

    override suspend fun connectToMediaSourceWithRememberedLogin(
        mediaSource: MediaSource
    ): Boolean {
        val result = rememberLoginService.getRememberedLogin(mediaSource.key)?.let {
            connectToMediaSource(mediaSource, it.userName, it.password, false)
        } ?: false

        return result
    }

    private fun getPathWithoutShareName(path: String): String {
        return currentShare?.let { path.removePrefix(it) } ?: path
    }

    private fun rememberLogin(mediaSource: MediaSource, userName: String?, password: String?) {
        if (userName != null && password != null) {
            rememberLoginService.rememberLogin(mediaSource.key, userName, password)
        }
    }
}