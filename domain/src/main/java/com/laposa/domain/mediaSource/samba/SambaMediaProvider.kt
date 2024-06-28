package com.laposa.domain.mediaSource.samba

import com.laposa.domain.mediaSource.MediaSourceProvider
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import com.laposa.domain.networkProtocols.smb.SmbService
import com.laposa.domain.rememberLogin.RememberLoginService
import com.laposa.domain.savedState.SavedStateService

class SambaMediaProvider(
    private val smbService: SmbService,
    private val savedStateService: SavedStateService,
    private val rememberLoginService: RememberLoginService,
) : MediaSourceProvider() {
    private var currentShare: String? = null

    override suspend fun connectToMediaSource(
        mediaSource: MediaSource,
        remember: Boolean,
    ): Boolean {
        val result = smbService.connect(mediaSource, mediaSource.username, mediaSource.password)

        if (result && remember) {
            savedStateService.addSavedMediaSources(mediaSource)
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
        val result = connectToMediaSource(mediaSource, false)
        return result
    }

    override suspend fun connectToMediaSourceWithRememberedLogin(
        mediaSource: MediaSource
    ): Boolean {
        val result = rememberLoginService.getRememberedLogin(mediaSource.key)?.let {
            connectToMediaSource(mediaSource, false)
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