package com.laposa.common.features.player.ui

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.ExoDatabaseProvider
import androidx.media3.datasource.cache.CacheEvictor
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

@UnstableApi
object CacheUtil {
    private var simpleCache: SimpleCache? = null

    fun getCache(context: Context): SimpleCache {
        if (simpleCache == null) {
            val cacheSize: Long = 100 * 1024 * 1024 // 100 MB
            val cacheDir = File(context.cacheDir, "media")
            val evictor: CacheEvictor = LeastRecentlyUsedCacheEvictor(cacheSize)
            val databaseProvider = ExoDatabaseProvider(context)
            simpleCache = SimpleCache(cacheDir, evictor, databaseProvider)
        }
        return simpleCache!!
    }
}