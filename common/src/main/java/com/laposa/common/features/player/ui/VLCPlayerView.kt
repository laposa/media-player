package com.laposa.common.features.player.ui

import android.net.Uri
import android.view.KeyEvent
import androidx.annotation.OptIn
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerControlView
import com.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout

@OptIn(UnstableApi::class)
@Composable
fun VLCPlayerView(
    url: String? = null,
    payload: InputStreamDataSourcePayload? = null,
) {
    val videoLayout: VLCVideoLayout = VLCVideoLayout(LocalContext.current)
    val libVLC: LibVLC = LibVLC(LocalContext.current, listOf("-vvv"))
    val mediaPlayer: MediaPlayer = MediaPlayer(libVLC)

    val mediaPlayerView = PlayerControlView(LocalContext.current).apply {
    }

    val inputStreamDataSourceRemembered = remember(payload) {
        payload
    }

    LaunchedEffect(url, inputStreamDataSourceRemembered) {
        url?.let {
            Media(libVLC, Uri.parse(it))
        } ?: inputStreamDataSourceRemembered?.let {
            Media(libVLC, Uri.parse(it.fullUrl))
        }?.let {
            try {
                mediaPlayer.attachViews(videoLayout, null, true, true)
                mediaPlayer.media = it
                it.release()
                mediaPlayer.play()
            } catch (exception: Exception) {
                println(exception.toString())
            }
        }
    }

    LaunchedEffect(inputStreamDataSourceRemembered) {
        inputStreamDataSourceRemembered?.let {

        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
            libVLC.release()
        }
    }

    Box {
        AndroidView(
            factory = { ctx ->
                videoLayout.apply {
                }
            },
            modifier = Modifier
                .focusable()
                .fillMaxWidth()
                .onKeyEvent { keyEvent ->
                    when (keyEvent.nativeKeyEvent.keyCode) {
                        KeyEvent.KEYCODE_DPAD_CENTER -> {
                            when {
                                mediaPlayer.isPlaying -> {
                                    // mediaPlayer.pause()
                                }

                                mediaPlayer.isPlaying.not() -> {
                                    // mediaPlayer.play()
                                }

                                else -> {
                                    // mediaPlayer.play()
                                }
                            }
                            true
                        }

                        else -> {
                            false
                        }
                    }
                }
        )
    }
}