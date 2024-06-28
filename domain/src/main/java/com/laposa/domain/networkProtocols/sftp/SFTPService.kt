package com.laposa.domain.networkProtocols.sftp

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session

class SFTPService {
    private val jsch = JSch()
    private var session: Session? = null
    private var channel: ChannelSftp? = null

    fun connect(
        host: String,
        user: String,
        password: String,
        port: Int = 22,
    ) {
        session = jsch.getSession(user, host, port)
        session?.setPassword(password)
        session?.setConfig("StrictHostKeyChecking", "no")
        session?.connect()
        channel = session?.openChannel("sftp") as ChannelSftp
        channel?.connect()

        println("Connected to $host:$port as $user")

        channel?.cd("/")
        channel?.ls(".")?.forEach {
            println(it)
        }
    }
}