package com.laposa.domain.networkProtocols.nfs

import com.emc.ecs.nfsclient.nfs.io.Nfs3File
import com.emc.ecs.nfsclient.nfs.nfs3.Nfs3
import com.emc.ecs.nfsclient.rpc.CredentialUnix
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceDirectory
import com.laposa.domain.mediaSource.model.MediaSourceFile
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.mediaSource.model.MediaSourceType
import com.laposa.domain.networkProtocols.mediaFileExtensionsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NfsService {
    private var nfs3: Nfs3? = null
    private var _host: String = ""
    private var _exportPath: String = ""

    /**
     * Parses the connection address in `host:/export` (NFS-standard colon) notation.
     * If no colon is present the whole string is treated as the host and `/` is the export.
     */
    private fun parseAddress(address: String): Pair<String, String> {
        val colonIdx = address.indexOf(':')
        return if (colonIdx >= 0) {
            val host = address.substring(0, colonIdx)
            val export = address.substring(colonIdx + 1).ifEmpty { "/" }
            host to export
        } else {
            address to "/"
        }
    }

    /**
     * Human-readable error from the last failed [connect] call, or null if the
     * last connection succeeded.
     */
    var lastConnectionError: String? = null
        private set

    suspend fun connect(mediaSource: MediaSource): Boolean = withContext(Dispatchers.IO) {
        if (mediaSource.connectionAddress == null) return@withContext false

        val (host, export) = parseAddress(mediaSource.connectionAddress)
        _host = host
        _exportPath = export
        lastConnectionError = null
        try {
            val credentials = CredentialUnix(0, 0, setOf(0))
            nfs3 = Nfs3(host, export, credentials, 3)
            // Verify connection by listing the export root
            Nfs3File(nfs3!!, "/").list()
            log("Connected to nfs://$host$export")
            true
        } catch (e: Exception) {
            nfs3 = null
            lastConnectionError = buildConnectionErrorMessage(host, export, e)
            log(lastConnectionError!!)
            false
        }
    }

    private fun buildConnectionErrorMessage(host: String, export: String, e: Exception): String {
        val raw = e.message ?: e.toString()
        return when {
            raw.contains("Cannot bind a port < 1024", ignoreCase = true) ->
                // The server returned AUTH_ERROR on the non-privileged attempt, causing the
                // library to retry with a privileged source port, which Android forbids.
                // macOS fix: add 'nfs.server.mount.require_resv_port = 0' to /etc/nfs.conf,
                // then run 'sudo nfsd restart'.
                "NFS connection to $host$export failed: the server requires a privileged " +
                    "source port (< 1024), which Android does not allow. " +
                    "Fix (macOS): add 'nfs.server.mount.require_resv_port = 0' to /etc/nfs.conf, " +
                    "then run 'sudo nfsd restart'."

            raw.contains("returned state: 13", ignoreCase = true) ->
                // MNT3ERR_ACCES — the mount daemon is reachable but is rejecting this client.
                // Causes: client IP not in the export's allowed-hosts list, or the path is not
                // exported at all. Check /etc/exports and showmount -e localhost on the server.
                "NFS connection to $host$export failed: the server denied the mount request " +
                    "(MNT3ERR_ACCES). " +
                    "Fix: ensure '$export' is listed in /etc/exports with no host restriction " +
                    "(e.g. '$export -alldirs -mapall=nobody'), then run 'sudo nfsd stop && sudo nfsd start'."

            raw.contains("returned state: 2", ignoreCase = true) ->
                // MNT3ERR_NOENT — the export path does not exist on the server.
                "NFS connection to $host$export failed: the export path does not exist on the server. " +
                    "Check that '$export' is a real directory and is listed in /etc/exports."

            else ->
                "NFS connection to $host$export failed: $raw"
        }
    }

    suspend fun getContentOfDirectoryAtPath(path: String): List<MediaSourceFileBase> =
        withContext(Dispatchers.IO) {
            val nfs = nfs3 ?: return@withContext emptyList()
            val normalizedPath = path.ifEmpty { "/" }.let {
                if (it.startsWith("/")) it else "/$it"
            }
            try {
                val dir = Nfs3File(nfs, normalizedPath)
                val names = dir.list() ?: return@withContext emptyList()
                names.mapNotNull { name ->
                    if (name == "." || name == "..") return@mapNotNull null
                    val childPath = "${normalizedPath.trimEnd('/')}/$name"
                    try {
                        val child = Nfs3File(nfs, childPath)
                        when {
                            child.isDirectory ->
                                MediaSourceDirectory(name, normalizedPath)
                            mediaFileExtensionsList.any {
                                name.endsWith(it, ignoreCase = true)
                            } ->
                                MediaSourceFile(name, normalizedPath, MediaSourceType.NSF)
                            else -> null
                        }
                    } catch (e: Exception) {
                        log("Could not stat $childPath: ${e.message}")
                        null
                    }
                }
            } catch (e: Exception) {
                log("List failed for $normalizedPath: ${e.message}")
                emptyList()
            }
        }

    fun getDirectUrl(filePath: String): String {
        val path = if (filePath.startsWith("/")) filePath else "/$filePath"
        return "nfs://$_host$_exportPath$path"
    }

    private fun log(message: String) = println("$TAG: $message")

    companion object {
        const val TAG = "NfsService"
    }
}
