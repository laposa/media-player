package com.laposa.domain.mediaSource.sftp

import com.laposa.domain.mediaSource.MediaSourceProvider
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.networkProtocols.sftp.SFTPService
import com.laposa.domain.networkProtocols.sftp.SftpHttpProxy
import com.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import java.net.URI

class SftpMediaProvider(
    private val sftpService: SFTPService,
) : MediaSourceProvider() {
    private var currentMediaSource: MediaSource? = null
    private var proxy: SftpHttpProxy? = null

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
        // Stop proxy for the old source before switching
        proxy?.stop()
        proxy = null
        currentMediaSource = mediaSource
        return sftpService.connect(
            mediaSource.connectionAddress!!,
            mediaSource.username ?: "",
            mediaSource.password ?: "",
            mediaSource.port ?: 22,
        )
    }

    override suspend fun getFile(fileName: String): InputStreamDataSourcePayload? {
        return sftpService.openFile(fileName)
    }

    override suspend fun getDirectUrl(fileName: String): String? {
        val mediaSource = currentMediaSource ?: return null

        // Start the HTTP proxy if not already running.
        // VLC's built-in SFTP module cannot complete the SSH handshake on Android,
        // so we bridge via a localhost HTTP server that proxies requests through JSch.
        if (proxy == null) {
            proxy = SftpHttpProxy(
                host = mediaSource.connectionAddress ?: return null,
                sftpPort = mediaSource.port ?: 22,
                username = mediaSource.username ?: "",
                password = mediaSource.password ?: "",
            )
            proxy!!.start()
        }

        // Build a proper HTTP URL with the SFTP path, using java.net.URI for safe encoding.
        val path = if (fileName.startsWith("/")) fileName else "/$fileName"
        val uri = URI("http", null, "127.0.0.1", proxy!!.proxyPort, path, null, null)
        return uri.toASCIIString()
    }

    override suspend fun openShare(shareName: String): List<MediaSourceFileBase> {
        return emptyList()
    }

    override suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase> {
        return sftpService.getContentOfDirectoryAtPath(path)
    }
}