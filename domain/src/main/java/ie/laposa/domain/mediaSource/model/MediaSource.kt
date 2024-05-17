package ie.laposa.domain.mediaSource.model

import android.net.nsd.NsdServiceInfo
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaSource(
    val type: MediaSourceType,
    val sourceName: String,
    val displayName: String,
) : Parcelable {
    companion object {
        fun fromNSD(service: NsdServiceInfo): MediaSource {
            return MediaSource(
                type = MediaSourceType.ZeroConf.fromId(service.serviceType),
                displayName = service.serviceName,
                sourceName = when(service.serviceType) {
                    "_smb._tcp." -> "SMB"
                    else -> service.serviceType
                },
            )
        }
    }
}

@Parcelize
sealed class MediaSourceType(open val id: String): Parcelable {
    @Parcelize
    sealed class ZeroConf(override val id: String) : MediaSourceType(id) {
        data object SMB : ZeroConf("_smb._tcp.")
        data object FTP : ZeroConf("_ftp._tcp.")
        data object WebDAV : ZeroConf("_webdav._tcp.")
        data object NFS : ZeroConf("_nfs._tcp.")

        companion object {
            fun fromId(id: String): ZeroConf {
                return when (id) {
                    SMB.id -> SMB
                    FTP.id -> FTP
                    WebDAV.id -> WebDAV
                    NFS.id -> NFS
                    else -> throw IllegalArgumentException("Unknown ZeroConf service type: $id")
                }
            }
        }
    }
}
