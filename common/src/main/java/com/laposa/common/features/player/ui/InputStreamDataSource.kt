package com.laposa.common.features.player.ui

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.AssetDataSource.AssetDataSourceException
import androidx.media3.datasource.BaseDataSource
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.cache.SimpleCache
import com.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import java.io.IOException
import java.io.InputStream

const val TAG = "InputStreamDataSource"

@OptIn(UnstableApi::class)
class InputStreamDataSource(
    private val payload: InputStreamDataSourcePayload
) : BaseDataSource(true) {
    private var bytesRemaining = 0L
    private var currentOffset = 0L

    private var currentInputStream: InputStream? = null

    private fun log(msg: String) {
        //Log.i(TAG, msg)
    }

    @Throws(AssetDataSourceException::class)
    override fun open(dataSpec: DataSpec): Long {
        log("[Open] position: ${dataSpec.position} | length: ${dataSpec.length}")
        log("[Open] currentOffset: $currentOffset")

        currentInputStream = payload.getInputStream()

        currentInputStream?.skip(dataSpec.position)
        bytesRemaining = payload.endOfFile - currentOffset
        transferStarted(dataSpec)
        log("[Open] bytesRemaining: $bytesRemaining")
        return bytesRemaining
    }

    @Throws(AssetDataSourceException::class)
    override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        log("[Read] offset: $offset | length: $length")

        val bytesRead =
            currentInputStream?.read(buffer, offset, length)?.also {
                if (it == -1) {
                    return C.RESULT_END_OF_INPUT
                } else {
                    currentOffset += it
                }
            } ?: 0

        log("[Read] bytesRead: $bytesRead")
        return bytesRead
    }

    override fun getUri(): Uri {
        return Uri.EMPTY
    }

    @Throws(IOException::class)
    override fun close() {
        log("[Close]")
        currentInputStream?.close()
        currentInputStream = null
    }
}

@UnstableApi
class InputStreamDataSourceFactory @OptIn(UnstableApi::class) constructor(
    private val payload: InputStreamDataSourcePayload,
    private val context: Context,
) : DataSource.Factory {
    override fun createDataSource(): DataSource {
        val cache: SimpleCache = CacheUtil.getCache(context)
        val defaultDataSourceFactory = DataSource.Factory { InputStreamDataSource(payload) }
        return defaultDataSourceFactory.createDataSource()
    }
}