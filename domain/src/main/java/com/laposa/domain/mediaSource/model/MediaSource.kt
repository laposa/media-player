package com.laposa.domain.mediaSource.model

import android.net.nsd.NsdServiceInfo
import android.os.Parcelable
import com.laposa.domain.zeroConf.ZeroConfServiceType
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaSource(
    val type: MediaSourceType,
    val sourceName: String,
    val displayName: String,
    val connectionAddress: String? = null,
    val isConnected: Boolean = false,
) : Parcelable {

    val key: String
        get() = "$type-$displayName-$connectionAddress"

    companion object {
        fun fromNSD(service: NsdServiceInfo): MediaSource {
            return MediaSource(
                type = MediaSourceType.fromId(service.serviceType),
                displayName = service.serviceName,
                sourceName = when {
                    ZeroConfServiceType.SMB.isType(service.serviceType) -> "SMB"
                    ZeroConfServiceType.NFS.isType(service.serviceType) -> "NFS"
                    else -> service.serviceType
                },
                connectionAddress = service.host.hostAddress
            )
        }
    }
}

enum class MediaSourceType {
    URL,
    LOCAL_FILE,
    ZERO_CONF_SMB,
    ZERO_CONF_FTP,
    ZERO_CONF_WEBDAV,
    ZERO_CONF_NFS;

    enum class ManualMediaSourceType {
        SFTP,
        SMB,
    }

    val isZeroConf: Boolean
        get() = this == ZERO_CONF_SMB || this == ZERO_CONF_FTP || this == ZERO_CONF_WEBDAV || this == ZERO_CONF_NFS

    companion object {
        fun fromId(id: String): MediaSourceType {
            return when (id) {
                "URL" -> URL
                "LOCAL_FILE" -> LOCAL_FILE
                "._smb._tcp" -> ZERO_CONF_SMB
                "_ftp._tcp." -> ZERO_CONF_FTP
                "_webdav._tcp." -> ZERO_CONF_WEBDAV
                "._nfs._tcp" -> ZERO_CONF_NFS
                else -> throw IllegalArgumentException("Unknown media source type: $id")
            }
        }
    }
}

/*@Parcelize
sealed class MediaSourceType : Parcelable {
    data object Url : MediaSourceType()

    data object LocalFile : MediaSourceType()

    @Parcelize
    sealed class ZeroConf(val type: ZeroConfServiceType) : MediaSourceType() {
        data object SMB : ZeroConf(ZeroConfServiceType.SMB)
        data object FTP : ZeroConf(ZeroConfServiceType.FTP)
        data object WebDAV : ZeroConf(ZeroConfServiceType.WEBDAV)
        data object NFS : ZeroConf(ZeroConfServiceType.NFS)

        companion object {
            fun fromId(id: String): ZeroConf {
                return when {
                    SMB.type.isType(id) -> SMB
                    FTP.type.isType(id) -> FTP
                    WebDAV.type.isType(id) -> WebDAV
                    NFS.type.isType(id) -> NFS
                    else -> throw IllegalArgumentException("Unknown ZeroConf service type: $id")
                }
            }
        }
    }
}*/
