package com.laposa.domain.recents

import com.google.gson.Gson
import com.laposa.domain.mediaSource.model.MediaSourceFile
import com.laposa.domain.mediaSource.model.MediaSourceType

data class RecentMediaCollection(
    val items: List<RecentMedia>
) {
    fun toJSON(): String {
        return items.joinToString(";") { it.toJSON() }
    }

    companion object {
        fun fromJSON(json: String): RecentMediaCollection {
            return RecentMediaCollection(
                items = json.split(";").map {
                    println("TADY 7: $it")
                    RecentMedia.fromJSON(it)
                }
            )
        }

        fun empty(): RecentMediaCollection = RecentMediaCollection(emptyList())
    }
}

data class RecentMedia(
    val mediaSourceType: MediaSourceType,
    val file: MediaSourceFile,
    val thumbnailPath: String?,
    val thumbnailUrl: String?,
    val progress: Long,
) {
    fun toJSON(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun fromJSON(json: String): RecentMedia {
            return Gson().fromJson(json, RecentMedia::class.java)
        }
    }
}
