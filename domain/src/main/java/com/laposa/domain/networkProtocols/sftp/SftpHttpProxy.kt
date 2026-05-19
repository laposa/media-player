package com.laposa.domain.networkProtocols.sftp

import android.util.Log
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.net.URLDecoder

/**
 * A minimal localhost HTTP server that proxies SFTP file access for VLC playback.
 *
 * libVLC's built-in SFTP access module fails to complete the SSH handshake on Android.
 * This proxy receives HTTP range requests from VLC (e.g. http://127.0.0.1:PORT/path)
 * and satisfies them by opening the file via JSch, which does work reliably.
 *
 * Usage:
 *   val proxy = SftpHttpProxy(host, sftpPort, username, password)
 *   proxy.start()
 *   val url = "http://127.0.0.1:${proxy.proxyPort}/path/to/file.mp4"
 *   // pass url to VLC
 *   proxy.stop() // when done
 */
class SftpHttpProxy(
    private val host: String,
    private val sftpPort: Int,
    private val username: String,
    private val password: String,
) {
    private var serverSocket: ServerSocket? = null

    @Volatile
    private var running = false

    val proxyPort: Int
        get() = serverSocket?.localPort ?: -1

    fun start() {
        if (running) return
        serverSocket = ServerSocket(0, 10, InetAddress.getByName("127.0.0.1"))
        running = true
        val thread = Thread {
            while (running) {
                try {
                    val client = serverSocket?.accept() ?: break
                    Thread { handleConnection(client) }.apply { isDaemon = true }.start()
                } catch (e: Exception) {
                    if (running) Log.e(TAG, "Proxy accept error", e)
                }
            }
        }
        thread.isDaemon = true
        thread.name = "sftp-http-proxy"
        thread.start()
        Log.d(TAG, "SFTP HTTP proxy started on port $proxyPort")
    }

    fun stop() {
        running = false
        try {
            serverSocket?.close()
        } catch (e: Exception) {
            // ignore
        }
        serverSocket = null
        Log.d(TAG, "SFTP HTTP proxy stopped")
    }

    private fun handleConnection(client: Socket) {
        try {
            client.use { socket ->
                val reader = socket.inputStream.bufferedReader()
                val output = socket.outputStream

                // Read request line: "GET /path HTTP/1.1"
                val requestLine = reader.readLine() ?: return
                Log.d(TAG, "Request: $requestLine")

                val parts = requestLine.trim().split(" ")
                if (parts.size < 2 || parts[0] != "GET") {
                    sendError(output, 400, "Bad Request")
                    return
                }

                val rawPath = parts[1]
                val filePath = URLDecoder.decode(rawPath, "UTF-8")

                // Read headers to find Range
                var rangeStart = 0L
                var rangeEnd = -1L
                var headerLine: String?
                while (true) {
                    headerLine = reader.readLine()
                    if (headerLine.isNullOrEmpty()) break
                    if (headerLine.startsWith("Range:", ignoreCase = true)) {
                        val rangeValue = headerLine.substringAfter("=").trim()
                        val rangeParts = rangeValue.split("-")
                        rangeStart = rangeParts.getOrNull(0)?.trim()?.toLongOrNull() ?: 0L
                        rangeEnd = rangeParts.getOrNull(1)?.trim()?.toLongOrNull() ?: -1L
                    }
                }

                streamFile(filePath, rangeStart, rangeEnd, output)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Connection handling error", e)
        }
    }

    private fun streamFile(filePath: String, rangeStart: Long, rangeEnd: Long, output: OutputStream) {
        val jsch = JSch()
        val session = try {
            jsch.getSession(username, host, sftpPort).also { s ->
                s.setPassword(password)
                s.timeout = 10_000
                s.setConfig("StrictHostKeyChecking", "no")
                s.connect()
            }
        } catch (e: Exception) {
            Log.e(TAG, "SFTP session error for $filePath", e)
            sendError(output, 503, "SFTP connection failed")
            return
        }

        try {
            val channel = session.openChannel("sftp") as ChannelSftp
            channel.connect()
            try {
                val fileSize: Long = try {
                    channel.lstat(filePath).size
                } catch (e: Exception) {
                    Log.e(TAG, "lstat failed for $filePath", e)
                    sendError(output, 404, "Not Found")
                    return
                }

                val effectiveStart = rangeStart.coerceAtLeast(0L)
                val effectiveEnd = if (rangeEnd >= 0) rangeEnd.coerceAtMost(fileSize - 1) else fileSize - 1
                val contentLength = effectiveEnd - effectiveStart + 1
                val isPartial = effectiveStart > 0 || rangeEnd >= 0

                if (isPartial) {
                    val header = buildString {
                        append("HTTP/1.1 206 Partial Content\r\n")
                        append("Content-Type: application/octet-stream\r\n")
                        append("Content-Length: $contentLength\r\n")
                        append("Content-Range: bytes $effectiveStart-$effectiveEnd/$fileSize\r\n")
                        append("Accept-Ranges: bytes\r\n")
                        append("Connection: close\r\n")
                        append("\r\n")
                    }
                    output.write(header.toByteArray(Charsets.US_ASCII))
                } else {
                    val header = buildString {
                        append("HTTP/1.1 200 OK\r\n")
                        append("Content-Type: application/octet-stream\r\n")
                        append("Content-Length: $fileSize\r\n")
                        append("Accept-Ranges: bytes\r\n")
                        append("Connection: close\r\n")
                        append("\r\n")
                    }
                    output.write(header.toByteArray(Charsets.US_ASCII))
                }

                val stream: InputStream = channel.get(filePath, null, effectiveStart)
                val buffer = ByteArray(BUFFER_SIZE)
                var remaining = contentLength
                while (remaining > 0) {
                    val toRead = minOf(buffer.size.toLong(), remaining).toInt()
                    val read = stream.read(buffer, 0, toRead)
                    if (read < 0) break
                    output.write(buffer, 0, read)
                    remaining -= read
                }
                output.flush()
                try {
                    stream.close()
                } catch (e: Exception) {
                    // ignore
                }
            } finally {
                try {
                    channel.disconnect()
                } catch (e: Exception) {
                    // ignore
                }
            }
        } finally {
            try {
                session.disconnect()
            } catch (e: Exception) {
                // ignore
            }
        }
    }

    private fun sendError(output: OutputStream, code: Int, message: String) {
        try {
            val response = "HTTP/1.1 $code $message\r\nContent-Length: 0\r\nConnection: close\r\n\r\n"
            output.write(response.toByteArray(Charsets.US_ASCII))
            output.flush()
        } catch (e: Exception) {
            // ignore
        }
    }

    companion object {
        private const val TAG = "SftpHttpProxy"
        private const val BUFFER_SIZE = 65_536
    }
}
