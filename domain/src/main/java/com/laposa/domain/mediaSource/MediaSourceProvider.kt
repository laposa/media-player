package com.laposa.domain.mediaSource

import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload

abstract class MediaSourceProvider {
    abstract suspend fun connectToMediaSourceAsAGuest(mediaSource: MediaSource): Boolean
    abstract suspend fun connectToMediaSourceWithRememberedLogin(
        mediaSource: MediaSource
    ): Boolean

    abstract suspend fun connectToMediaSource(
        mediaSource: MediaSource,
        remember: Boolean,
    ): Boolean

    abstract suspend fun getFile(
        fileName: String
    ): InputStreamDataSourcePayload?

    abstract suspend fun openShare(shareName: String): List<MediaSourceFileBase>

    abstract suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase>
}