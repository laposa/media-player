package com.laposa.mediaplayer

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import dagger.hilt.android.AndroidEntryPoint
import com.laposa.common.ui.VideoPlayerApp
import com.laposa.common.ui.theme.LaposaVideoPlayerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaposaVideoPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    VideoPlayerApp()
                }
            }
        }
    }
}