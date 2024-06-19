package ie.laposa.domain.networkProtocols.smb

import com.hierynomus.msdtyp.AccessMask
import com.hierynomus.msfscc.FileAttributes
import com.hierynomus.mssmb2.SMB2CreateDisposition
import com.hierynomus.mssmb2.SMB2ShareAccess
import com.hierynomus.protocol.commons.EnumWithValue.EnumUtils
import com.hierynomus.smbj.SMBClient
import com.hierynomus.smbj.auth.AuthenticationContext
import com.hierynomus.smbj.session.Session
import com.hierynomus.smbj.share.DiskShare
import com.rapid7.client.dcerpc.mssrvs.ServerService
import com.rapid7.client.dcerpc.transport.SMBTransportFactories
import ie.laposa.domain.mediaSource.model.MediaSource
import ie.laposa.domain.mediaSource.model.MediaSourceDirectory
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.mediaSource.model.MediaSourceFileBase
import ie.laposa.domain.mediaSource.model.MediaSourceShare
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class SmbService {
    private var client: SMBClient = SMBClient()
    private var _currentSession: Session? = null

    private val _currentFilesList = MutableStateFlow<MutableMap<String, List<MediaSourceFile>>>(
        mutableMapOf()
    )
    val filesList: StateFlow<Map<String, List<MediaSourceFile>>> = _currentFilesList

    private var _currentMediaSource: MediaSource? = null
    private val _currentShares = MutableStateFlow<MutableMap<MediaSource, List<String>>>(
        mutableMapOf()
    )

    val shares: StateFlow<Map<MediaSource, List<String>>> = _currentShares

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
                    _currentShares.update {
                        val currentMediaSourceShares = it[mediaSource] ?: emptyList()
                        it[mediaSource] =
                            (currentMediaSourceShares + share.netName).distinct()
                        it
                    }
                }
            }

            return@withContext true
        } catch (e: Exception) {
            e.printStackTrace()
            throw WrongCredentialsException()
        }
    }

    suspend fun getCurrentShares(): List<MediaSourceShare> = withContext(Dispatchers.IO) {
        return@withContext _currentMediaSource?.let {
            _currentShares.value[it]?.map {
                MediaSourceShare(it)
            }
        } ?: emptyList()
    }

    suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase> =
        withContext(Dispatchers.IO) {
            if (path == "" && _currentShare == null) {
                return@withContext _currentMediaSource?.let {
                    _currentShares.value[it]?.map {
                        MediaSourceShare(it)
                    }
                } ?: emptyList()
            }

            return@withContext _currentShare?.let { share ->
                share.list(path).mapNotNull { item ->
                    println("Path: ${item.fileName}")
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
                        if (videoFilesExtensions.any {
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
                _currentShare = (_currentSession?.connectShare(shareName) as DiskShare)
            }
            /*  _currentSession?.let {
                if (_currentShare?.smbPath?.shareName != shareName || _currentShare == null) {
                     _currentShare?.close()
                     _currentShare = (it.connectShare(shareName) as DiskShare)
                 }

                 _currentShare?.let { share ->
                     for (fileExtension in videoFilesExtensions) {
                         val list = share.list("/", "*$fileExtension")
                         _currentFilesList.update { filesMap ->
                             val currentShareFilesList = filesMap[shareName] ?: emptyList()
                             //TODO: Wrong
                             filesMap[shareName] = currentShareFilesList.plus(list.map {
                                 MediaSourceFile(
                                     it.fileName,
                                     it.fileName
                                 )
                             })
                                 .distinctBy { it.fileName }
                             filesMap
                         }
                     }
                 }
             }*/
            return@withContext getContentOfDirectoryAtPath("/")
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