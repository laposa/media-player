package com.laposa.domain.mediaSource.nfs

import com.laposa.domain.mediaSource.MediaSourceProvider
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.networkProtocols.nfs.NfsService
import com.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload

class NfsMediaProvider(
    private val nfsService: NfsService
) : MediaSourceProvider() {
    override suspend fun connectToMediaSourceAsAGuest(mediaSource: MediaSource): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun connectToMediaSourceWithRememberedLogin(mediaSource: MediaSource): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun connectToMediaSource(
        mediaSource: MediaSource,
        remember: Boolean,
    ): Boolean {
        //nfsService.connect(mediaSource)
        return true
    }

    override suspend fun getFile(fileName: String): InputStreamDataSourcePayload? {
        TODO("Not yet implemented")
    }

    override suspend fun openShare(shareName: String): List<MediaSourceFileBase> {
        TODO("Not yet implemented")
    }

    override suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase> {
        TODO("Not yet implemented")
    }
}