package com.laposa.common.features.common.composables

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun getActivity(): Activity? {
    val context = LocalContext.current
    return if (context is Activity) {
        context
    } else {
        null
    }
}