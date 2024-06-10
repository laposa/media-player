package ie.laposa.domain.mediaSource.model

import android.net.nsd.NsdServiceInfo
import android.os.Parcelable
import ie.laposa.domain.zeroConf.ZeroConfServiceType
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaSource(
    val type: MediaSourceType,
    val sourceName: String,
    val displayName: String,
    val connectionAddress: String? = null,
) : Parcelable {
    companion object {
        fun fromNSD(service: NsdServiceInfo): MediaSource {
            return MediaSource(
                type = MediaSourceType.ZeroConf.fromId(service.serviceType),
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

@Parcelize
sealed class MediaSourceType : Parcelable {
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
}
