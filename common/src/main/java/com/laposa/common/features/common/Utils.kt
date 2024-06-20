package com.laposa.common.features.common

import android.app.UiModeManager
import android.content.Context
import android.content.Context.UI_MODE_SERVICE
import android.content.res.Configuration

fun isTvDevice(context: Context): Boolean {
    val uiModeManager = context.getSystemService(UI_MODE_SERVICE) as UiModeManager
    return uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION
}