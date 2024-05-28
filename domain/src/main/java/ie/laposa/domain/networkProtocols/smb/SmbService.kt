package ie.laposa.domain.networkProtocols.smb

import com.hierynomus.msdtyp.AccessMask
import com.hierynomus.mssmb2.SMB2CreateDisposition
import com.hierynomus.mssmb2.SMB2ShareAccess
import com.hierynomus.smbj.SMBClient
import com.hierynomus.smbj.auth.AuthenticationContext
import com.hierynomus.smbj.session.Session
import com.hierynomus.smbj.share.DiskShare
import com.rapid7.client.dcerpc.mssrvs.ServerService
import com.rapid7.client.dcerpc.transport.SMBTransportFactories
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class SmbService {
    private var client: SMBClient = SMBClient()
    private var _currentSession: Session? = null

    private val _currentFilesList = MutableStateFlow<List<MediaSourceFile>?>(null)
    val filesList: StateFlow<List<MediaSourceFile>?> = _currentFilesList

    private val _currentShares = MutableStateFlow<List<String>?>(null)
    val shares: StateFlow<List<String>?> = _currentShares

    private var _currentShare: DiskShare? = null

    fun connect(
        serverName: String, userName: String? = null, password: String? = null, port: Int = 445
    ) {
        try {
            val connection = client.connect(serverName, port)
            val authContext = if (userName != null && password != null) {
                AuthenticationContext(userName, password.toCharArray(), "")
            } else {
                AuthenticationContext.anonymous()
            }

            _currentSession = connection.authenticate(authContext)

            val transport = SMBTransportFactories.SRVSVC.getTransport(_currentSession)
            val serverService = ServerService(transport)

            val shares = serverService.shares0

            for (share in shares) {
                if (!share.netName.contains("$")) {
                    _currentShares.update {
                        (it ?: emptyList()) + share.netName
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun openShare(shareName: String) {
        withContext(Dispatchers.IO) {
            _currentShare?.close()
            _currentSession?.let {
                _currentShare = (it.connectShare(shareName) as DiskShare)
                _currentShare?.let { share ->
                    _currentShare = share
                    for (fileExtension in videoFilesExtensions) {
                        val list = share.list("/", "*$fileExtension")
                        _currentFilesList.update { filesList ->
                            (filesList
                                ?: emptyList()).plus(list.map { MediaSourceFile(it.fileName) })
                                .distinct()
                        }
                    }
                }
            }
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

            fun read(buffer: ByteArray, bufferOffset: Long, offset: Int, readLength: Int): Int {
                try {
                    return it.read(buffer, bufferOffset, offset, readLength)
                } catch (e: Exception) {
                    e.printStackTrace()
                    return -1
                }
            }

            return@let InputStreamDataSourcePayload(
                it::getInputStream,
                ::read,
                fileInformation.standardInformation.endOfFile
            )
        }
    }

    companion object {
        private const val TAG = "SmbService"
        private val videoFilesExtensions = listOf(
            ".mp4", ".mkv", ".avi", ".mov", ".flv", ".wmv", ".webm", ".m4v"
        )
    }
}