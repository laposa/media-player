package com.laposa.domain.mediaSource.model.nfs

import com.laposa.domain.mediaSource.MediaSourceProvider
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceFile
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.networkProtocols.nfs.NfsService
import com.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

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
        userName: String?,
        password: String?,
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