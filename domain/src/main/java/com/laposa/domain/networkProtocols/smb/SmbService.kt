package com.laposa.domain.networkProtocols.smb

import com.hierynomus.msdtyp.AccessMask
import com.hierynomus.msfscc.FileAttributes
import com.hierynomus.mssmb2.SMB2CreateDisposition
import com.hierynomus.mssmb2.SMB2ShareAccess
import com.hierynomus.protocol.commons.EnumWithValue.EnumUtils
import com.hierynomus.smbj.SMBClient
import com.hierynomus.smbj.auth.AuthenticationContext
import com.hierynomus.smbj.session.Session
import com.hierynomus.smbj.share.DiskShare
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceDirectory
import com.laposa.domain.mediaSource.model.MediaSourceFile
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.mediaSource.model.MediaSourceShare
import com.laposa.domain.networkProtocols.mediaFileExtensionsList
import com.rapid7.client.dcerpc.mssrvs.ServerService
import com.rapid7.client.dcerpc.transport.SMBTransportFactories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SmbService {
    private var client: SMBClient = SMBClient()
    private var _currentSession: Session? = null
    private var _currentMediaSource: MediaSource? = null
    private val _currentShares: MutableMap<MediaSource, List<String>> = mutableMapOf()

    private var _currentShare: DiskShare? = null

    suspend fun connect(
        mediaSource: MediaSource,
        userName: String? = null,
        password: String? = null,
        port: Int = 445,
    ) = withContext(Dispatchers.IO) {
        _currentMediaSource = mediaSource
        try {
            val connection = client.connect(mediaSource.connectionAddress, port)
            val authContext =
                if (!userName.isNullOrEmpty() && !password.isNullOrEmpty()) {
                    AuthenticationContext(userName, password.toCharArray(), "")
                } else {
                    AuthenticationContext.guest()
                }

            _currentSession = connection.authenticate(authContext)

            val transport = SMBTransportFactories.SRVSVC.getTransport(_currentSession)
            val serverService = ServerService(transport)

            val shares = serverService.shares0

            for (share in shares) {
                if (!share.netName.contains("$")) {
                    val currentMediaSourceShares = _currentShares[mediaSource] ?: emptyList()
                    _currentShares[mediaSource] =
                        (currentMediaSourceShares + share.netName).distinct()
                }
            }

            // Try to connect to some share to find out if current authentication have enough permissions
            _currentShares.values.firstOrNull()?.let {
                _currentSession?.connectShare(it.first())
            }

            return@withContext true
        } catch (e: Exception) {
            if (e.message?.contains("STATUS_LOGON_FAILURE") == true && userName != null) {
                throw WrongCredentialsException()
            }

            return@withContext false
        }
    }

    suspend fun getCurrentShares(): List<MediaSourceShare> = withContext(Dispatchers.IO) {
        return@withContext _currentMediaSource?.let {
            _currentShares[it]?.map {
                MediaSourceShare(it)
            }
        } ?: emptyList()
    }

    suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase> =
        withContext(Dispatchers.IO) {
            return@withContext getContentOfDirectoryAtPathInner(path)
        }

    private fun getContentOfDirectoryAtPathInner(path: String): List<MediaSourceFileBase> {
        if (path == "" && _currentShare == null) {
            return _currentMediaSource?.let {
                _currentShares[it]?.map {
                    MediaSourceShare(it)
                }
            } ?: emptyList()
        }
        return _currentShare?.let { share ->
            share.list(path).mapNotNull { item ->
                if (EnumUtils.isSet(
                        item.fileAttributes,
                        FileAttributes.FILE_ATTRIBUTE_DIRECTORY
                    ) && item.fileName != "." && item.fileName != ".."
                ) {
                    MediaSourceDirectory(
                        item.fileName,
                        path,
                    )
                } else {
                    if (mediaFileExtensionsList.any {
                            item.fileName.contains(it)
                        }) {
                        MediaSourceFile(
                            item.fileName,
                            path,
                        )
                    } else {
                        null
                    }
                }
            }
        } ?: emptyList()
    }

    suspend fun openShare(shareName: String): List<MediaSourceFileBase> {
        return withContext(Dispatchers.IO) {
            if (_currentShare == null || _currentShare?.smbPath?.shareName != shareName) {
                _currentShare?.close()
                try {
                    _currentShare = (_currentSession?.connectShare(shareName) as DiskShare)
                } catch (exception: Exception) {
                    println(exception.message)
                }
            }
            getContentOfDirectoryAtPathInner("/")
        }
    }

    suspend fun openFile(fileName: String): InputStreamDataSourcePayload? {
        val networkFile = withContext(Dispatchers.IO) {
            _currentShare?.openFile(
                fileName,
                setOf(
                    AccessMask.FILE_READ_DATA,
                    AccessMask.FILE_READ_ATTRIBUTES,
                    AccessMask.FILE_READ_EA
                ),
                null,
                setOf(SMB2ShareAccess.FILE_SHARE_READ),
                SMB2CreateDisposition.FILE_OPEN,
                null
            )
        }

        return networkFile?.let {
            val fileInformation = withContext(Dispatchers.IO) {
                networkFile.fileInformation
            }

            return@let InputStreamDataSourcePayload(
                it::getInputStream,
                fileInformation.standardInformation.endOfFile
            )
        }
    }

    companion object {
        private const val TAG = "SmbService"
    }
}