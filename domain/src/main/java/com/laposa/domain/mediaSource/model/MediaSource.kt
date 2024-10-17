package com.laposa.domain.mediaSource.model

import android.net.nsd.NsdServiceInfo
import android.os.Parcelable
import com.google.gson.Gson
import com.laposa.domain.zeroConf.ZeroConfServiceType
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaSource(
    val type: MediaSourceType,
    val sourceName: String,
    val displayName: String,
    val connectionAddress: String? = null,
    val username: String? = null,
    val password: String? = null,
    val port: Int? = null,
    val isConnected: Boolean = false,
) : Parcelable {

    val key: String
        get() = "$type-$displayName-$connectionAddress"

    fun toJSON(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun fromJSON(json: String): MediaSource {
            return Gson().fromJson(json, MediaSource::class.java)
        }

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


    override fun equals(other: Any?): Boolean {
        return if (other is MediaSource) {
            key == other.key
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}

enum class MediaSourceType {
    URL,
    LOCAL_FILE,
    FTP,
    NSF,
    WEB_DAV,
    SFTP,
    SMB;

    val isZeroConf: Boolean
        get() = this == SMB || this == FTP || this == WEB_DAV || this == NSF

    companion object {
        fun fromId(id: String): MediaSourceType {
            return when (id) {
                "URL" -> URL
                "LOCAL_FILE" -> LOCAL_FILE
                "._smb._tcp" -> SMB
                "_ftp._tcp." -> FTP
                "_webdav._tcp." -> WEB_DAV
                "._nfs._tcp" -> NSF
                else -> throw IllegalArgumentException("Unknown media source type: $id")
            }
        }
    }
}