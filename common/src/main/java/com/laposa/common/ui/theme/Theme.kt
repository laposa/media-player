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
    val PurpleSeedColor = Color(0xFF5E44D3)
    val GreenSeedColor = Color(0xFF386A20)
    val BlueSeedColor = Color(0xFF004A77)
    val RedSeedColor = Color(0xFF9C4146)
    val YellowSeedColor = Color(0xFF616200)

    val colors = listOf(
        GreenSeedColor,
        BlueSeedColor,
        YellowSeedColor,
        RedSeedColor,
        PurpleSeedColor,
    )

    val color = colors.random()
    val colorScheme = if (isInDarkTheme) {
        dark(color.toArgb())
    } else {
        light(color.toArgb())
    }

    MaterialTheme(
        colorScheme = colorScheme.toColorScheme(),
        typography = Typography,
        content = content,
    )
}