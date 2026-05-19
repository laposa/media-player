package com.laposa.domain.mediaSource.nfs

import com.laposa.domain.mediaSource.MediaSourceProvider
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.networkProtocols.nfs.NfsService
import com.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload

class NfsMediaProvider(
    private val nfsService: NfsService,
) : MediaSourceProvider() {

    override suspend fun connectToMediaSourceAsAGuest(mediaSource: MediaSource): Boolean {
        return nfsService.connect(mediaSource)
    }

    override suspend fun connectToMediaSourceWithRememberedLogin(mediaSource: MediaSource): Boolean {
        return nfsService.connect(mediaSource)
    }

    override suspend fun connectToMediaSource(
        mediaSource: MediaSource,
        remember: Boolean,
    ): Boolean {
        return nfsService.connect(mediaSource)
    }

    override suspend fun getFile(fileName: String): InputStreamDataSourcePayload? = null

    override suspend fun getDirectUrl(fileName: String): String {
        return nfsService.getDirectUrl(fileName)
    }

    /** NFS has no concept of sub-shares; the export root is the top level. */
    override suspend fun openShare(shareName: String): List<MediaSourceFileBase> {
        return nfsService.getContentOfDirectoryAtPath("/")
    }

    override suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase> {
        return nfsService.getContentOfDirectoryAtPath(path)
    }
}
