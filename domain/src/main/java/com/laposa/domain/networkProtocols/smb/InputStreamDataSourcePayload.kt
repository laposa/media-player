package com.laposa.domain.networkProtocols.smb

import java.io.InputStream

data class InputStreamDataSourcePayload(
    val getInputStream: () -> InputStream,
    val read: (buffer: ByteArray, bufferOffset: Long, offset: Int, readLength: Int) -> Int,
    val endOfFile: Long,
)
