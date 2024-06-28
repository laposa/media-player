package com.laposa.domain.mediaSource.model

import com.google.gson.Gson

data class ManualMediaSource(
    val type: MediaSourceType,
    val hostName: String,
    val port: Int,
    val username: String,
    val password: String,
) {
    fun toJSON(): String {
        return Gson().toJson(this)
    }
    
    companion object {
        fun fromJSON(json: String): ManualMediaSource {
            return Gson().fromJson(json, ManualMediaSource::class.java)
        }
    }
}
