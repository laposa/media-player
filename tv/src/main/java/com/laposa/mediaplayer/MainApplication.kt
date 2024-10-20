package com.laposa.mediaplayer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.embrace.android.embracesdk.Embrace

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Embrace.getInstance().start(this)
    }
}