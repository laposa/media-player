package com.laposa.domain.rememberLogin

import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class RememberLogin(
    val userName: String,
    val password: String,
) : Parcelable {
    fun toJSON(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun fromJSON(json: String): RememberLogin {
            return Gson().fromJson(json, RememberLogin::class.java)
        }
    }
}