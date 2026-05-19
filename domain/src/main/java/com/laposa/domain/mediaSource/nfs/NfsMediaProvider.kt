package com.laposa.domain.mediaSource.nfs

import com.laposa.domain.mediaSource.MediaSourceProvider
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.networkProtocols.nfs.NfsService
import com.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload

class NfsMediaProvider(
    private val nfsService: NfsService
) : MediaSourceProvider() {
    private var currentMediaSource: MediaSource? = null

    override suspend fun connectToMediaSourceAsAGuest(mediaSource: MediaSource): Boolean {
        return true
    }

    override suspend fun connectToMediaSourceWithRememberedLogin(mediaSource: MediaSource): Boolean {
        return true
    }

    override suspend fun connectToMediaSource(
        mediaSource: MediaSource,
        remember: Boolean,
    ): Boolean {
        currentMediaSource = mediaSource
        return true
    }

    override suspend fun getFile(fileName: String): InputStreamDataSourcePayload? {
        return null
    }

    override suspend fun getDirectUrl(fileName: String): String? {
        val ms = currentMediaSource ?: return null
        val path = if (fileName.startsWith("/")) fileName else "/$fileName"
        return "nfs://${ms.connectionAddress}$path"
    }

    override suspend fun openShare(shareName: String): List<MediaSourceFileBase> {
        return emptyList()
    }

    override suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase> {
        return emptyList()
    }
}