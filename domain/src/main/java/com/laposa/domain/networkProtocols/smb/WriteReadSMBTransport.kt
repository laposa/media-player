package com.laposa.domain.networkProtocols.smb

import com.rapid7.client.dcerpc.transport.RPCTransport
import com.rapid7.helper.smbj.share.NamedPipe

/**
 * An RPCTransport that uses separate SMB2_WRITE + SMB2_READ instead of
 * FSCTL_PIPE_TRANSCEIVE (IOCTL) to communicate over a named pipe.
 *
 * Some SMB servers (e.g. Samba with restrictive IOCTL settings) deny
 * FSCTL_PIPE_TRANSCEIVE while still supporting normal write/read on named pipes.
 * This transport is used as a fallback when the default [SMBTransport] fails.
 */
internal class WriteReadSMBTransport(private val pipe: NamedPipe) : RPCTransport() {

    /**
     * Sends [packetOut] via SMB2_WRITE, then reads the response via SMB2_READ.
     * This is semantically equivalent to FSCTL_PIPE_TRANSCEIVE but uses two
     * separate SMB2 messages instead of one IOCTL.
     */
    override fun transact(packetOut: ByteArray, packetIn: ByteArray): Int {
        pipe.write(packetOut)
        val response = pipe.read()
        System.arraycopy(response, 0, packetIn, 0, response.size)
        return response.size
    }

    override fun write(packetOut: ByteArray) {
        pipe.write(packetOut)
    }

    override fun read(packetIn: ByteArray): Int {
        val response = pipe.read()
        System.arraycopy(response, 0, packetIn, 0, response.size)
        return response.size
    }
}
