package com.laposa.domain.networkProtocols.sftp

import android.util.Log
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.ChannelSftp.LsEntry
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import com.laposa.domain.mediaSource.model.MediaSourceDirectory
import com.laposa.domain.mediaSource.model.MediaSourceFile
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.mediaSource.model.MediaSourceType
import com.laposa.domain.networkProtocols.mediaFileExtensionsList
import com.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SFTPService {
    private val jsch = JSch()
    private var session: Session? = null
    private var channel: ChannelSftp? = null

    suspend fun connect(
        host: String,
        user: String,
        password: String,
        port: Int = 22,
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            session = jsch.getSession(user, host, port)
            session?.setPassword(password)
            session?.setConfig("StrictHostKeyChecking", "no")
            session?.connect()
            openChannel()

            println("Connected to $host:$port as $user")
        } catch (exception: Exception) {
            Log.e(TAG, exception.message ?: "Unknown error")
            return@withContext false
        }

        return@withContext true
    }

    private fun openChannel() {
        channel?.quit()
        channel = session?.openChannel("sftp") as ChannelSftp
        channel?.connect()
        channel?.cd("/")
    }

    suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase> =
        withContext(Dispatchers.IO) {
            val files = mutableListOf<MediaSourceFileBase>()
            openChannel()
            try {
                if (path.isEmpty()) {
                    channel?.cd("/")
                } else {
                    channel?.cd(path)
                }

                (channel?.ls("."))?.filter {
                    (it as LsEntry).filename.first() != '.'
                }?.forEach {
                    val item = it as LsEntry
                    println(item.filename.lowercase().substringAfterLast('.'))
                    if (item.attrs.isDir) {
                        files.add(
                            MediaSourceDirectory(
                                it.filename,
                                path
                            )
                        )
                    } else if (mediaFileExtensionsList.any { extension ->
                            extension.contains(
                                item.filename.lowercase().substringAfterLast('.')
                            )
                        }
                    ) {
                        files.add(
                            MediaSourceFile(
                                item.filename,
                                path,
                                MediaSourceType.SFTP
                            )
                        )
                    }
                }
            } catch (exception: Exception) {
                Log.e(TAG, exception.toString())
            }

            return@withContext files
        }

    suspend fun openFile(fileName: String): InputStreamDataSourcePayload =
        withContext(Dispatchers.IO) {
            openChannel()
            val length = channel?.lstat(fileName)?.size ?: -1
            return@withContext InputStreamDataSourcePayload(
                { channel?.get(fileName) },
                length,
            )
        }

    companion object {
        private const val TAG = "SFTPService"
    }
}