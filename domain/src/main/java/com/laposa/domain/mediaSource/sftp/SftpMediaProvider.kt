package com.laposa.domain.mediaSource.sftp

import com.laposa.domain.mediaSource.MediaSourceProvider
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.networkProtocols.sftp.SFTPService
import com.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload

class SftpMediaProvider(
    private val sftpService: SFTPService,
) : MediaSourceProvider() {
    override suspend fun connectToMediaSourceAsAGuest(mediaSource: MediaSource): Boolean {
        return false
    }

    override suspend fun connectToMediaSourceWithRememberedLogin(mediaSource: MediaSource): Boolean {
        val result = connectToMediaSource(mediaSource, false)
        println("connectToMediaSourceWithRememberedLogin: $mediaSource - $result")
        return result
    }

    override suspend fun connectToMediaSource(
        mediaSource: MediaSource,
        remember: Boolean
    ): Boolean {
        return sftpService.connect(
            mediaSource.connectionAddress!!,
            mediaSource.username ?: "",
            mediaSource.password ?: ""
        )
    }

    override suspend fun getFile(fileName: String): InputStreamDataSourcePayload? {
        return sftpService.openFile(fileName)
    }

    override suspend fun openShare(shareName: String): List<MediaSourceFileBase> {
        return emptyList()
    }

    override suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase> {
        return sftpService.getContentOfDirectoryAtPath(path)
    }
}