package com.laposa.domain.networkProtocols.smb

import java.io.InputStream

data class InputStreamDataSourcePayload(
    val getInputStream: () -> InputStream?,
    val endOfFile: Long,
    val fullUrl: String,
)
