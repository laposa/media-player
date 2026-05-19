package com.laposa.domain.mediaSource.samba

import com.laposa.domain.mediaSource.MediaSourceProvider
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceEnterShareName
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.mediaSource.model.MediaSourceShare
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
    private var currentMediaSource: MediaSource? = null

    override suspend fun connectToMediaSource(
        mediaSource: MediaSource,
        remember: Boolean,
    ): Boolean {
        currentMediaSource = mediaSource
        val result = smbService.connect(mediaSource, mediaSource.username, mediaSource.password)

        if (result && remember) {
            savedStateService.addSavedMediaSources(mediaSource)
        }

        return result
    }

    override suspend fun getFile(fileName: String): InputStreamDataSourcePayload? {
        return smbService.openFile(fileName)
    }

    override suspend fun getDirectUrl(fileName: String): String? {
        val ms = currentMediaSource ?: return null
        val share = currentShare ?: return null
        
        val userPass = if (!ms.username.isNullOrEmpty() && !ms.password.isNullOrEmpty()) {
            "${ms.username}:${ms.password}@"
        } else ""
        
        val path = if (fileName.startsWith("/")) fileName else "/$fileName"
        // VLC smb url format: smb://user:password@host/share/path
        return "smb://$userPass${ms.connectionAddress}/$share$path"
    }

    override suspend fun openShare(shareName: String): List<MediaSourceFileBase> {
        currentShare = shareName
        val result = smbService.openShare(shareName)
        // Persist the share name so it appears in future sessions
        currentMediaSource?.let { ms ->
            savedStateService.addManualSmbShare(ms.key, shareName)
        }
        return result
    }

    override suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase> {
        return if (path == "") {

            val enumeratedShares = smbService.getCurrentShares()

            val manualShares = currentMediaSource?.let { ms ->
                savedStateService.getManualSmbShares(ms.key).map { MediaSourceShare(it) }
            } ?: emptyList()

            val allShares = (enumeratedShares + manualShares).distinctBy { it.name }

            when {
                allShares.isEmpty() && smbService.shareEnumerationFailed ->
                    listOf(MediaSourceEnterShareName())
                smbService.shareEnumerationFailed ->
                     listOf(MediaSourceEnterShareName()) + allShares
                else -> allShares
            }
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