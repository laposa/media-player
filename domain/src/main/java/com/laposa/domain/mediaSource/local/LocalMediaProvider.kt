package com.laposa.domain.mediaSource.local

import com.laposa.domain.mediaSource.MediaSourceProvider
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import java.io.File

class LocalMediaProvider : MediaSourceProvider() {
    override suspend fun connectToMediaSourceAsAGuest(mediaSource: MediaSource): Boolean = true
    override suspend fun connectToMediaSourceWithRememberedLogin(mediaSource: MediaSource): Boolean = true
    override suspend fun connectToMediaSource(mediaSource: MediaSource, remember: Boolean): Boolean = true

    override suspend fun getFile(fileName: String): InputStreamDataSourcePayload? = null

    override suspend fun getDirectUrl(fileName: String): String? {
        // VLC can play local files using the file:// scheme or direct path
        return if (File(fileName).exists()) {
            "file://$fileName"
        } else {
            null
        }
    }

    override suspend fun openShare(shareName: String): List<MediaSourceFileBase> = emptyList()
    override suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase> = emptyList()
}
