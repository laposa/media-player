package com.laposa.domain.mediaSource

import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceFile
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class MediaSourceProvider {
    abstract suspend fun connectToMediaSourceAsAGuest(mediaSource: MediaSource): Boolean
    abstract suspend fun connectToMediaSourceWithRememberedLogin(
        mediaSource: MediaSource
    ): Boolean

    abstract suspend fun connectToMediaSource(
        mediaSource: MediaSource,
        userName: String? = null,
        password: String? = null,
        remember: Boolean,
    ): Boolean

    abstract suspend fun getFile(
        fileName: String
    ): InputStreamDataSourcePayload?

    abstract suspend fun openShare(shareName: String): List<MediaSourceFileBase>

    abstract suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase>
}