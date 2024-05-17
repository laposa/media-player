package ie.laposa.common.features.player.ui

import android.view.KeyEvent
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView as ExoPlayerView

@Composable
fun PlayerView(
    url: String
) {
    val context = LocalContext.current

    val exoPlayer = ExoPlayer.Builder(context).build()

    val mediaSource = remember(url) {
        MediaItem.fromUri(url)
    }

    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.prepare()
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            ExoPlayerView(ctx).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier.focusable()
            .fillMaxWidth()
            .onKeyEvent { keyEvent ->
                when (keyEvent.nativeKeyEvent.keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        when {
                            exoPlayer.isPlaying -> {
                                exoPlayer.pause()
                            }
                            exoPlayer.isPlaying.not() -> {
                                exoPlayer.playWhenReady = true
                            }
                            else -> {
                                exoPlayer.play()
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