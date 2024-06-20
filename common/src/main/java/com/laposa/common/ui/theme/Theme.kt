package com.laposa.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.tv.material3.MaterialTheme
import com.google.tv.material.catalog.colorutils.Scheme.Companion.dark
import com.google.tv.material.catalog.colorutils.Scheme.Companion.light
import com.google.tv.material.catalog.colorutils.toColorScheme

@Composable
fun LaposaVideoPlayerTheme(
    isInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val GreenSeedColor = Color(0xFF386A20)

    val colorScheme = if (isInDarkTheme) {
        dark(GreenSeedColor.toArgb())
    } else {
        light(GreenSeedColor.toArgb())
    }
    MaterialTheme(
        colorScheme = colorScheme.toColorScheme(),
        typography = Typography,
        content = content,
    )
}