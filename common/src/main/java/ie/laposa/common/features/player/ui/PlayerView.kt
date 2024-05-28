package ie.laposa.common.features.player.ui

import android.net.Uri
import android.view.KeyEvent
import androidx.annotation.OptIn
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
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.extractor.DefaultExtractorsFactory
import ie.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import androidx.media3.ui.PlayerView as ExoPlayerView


@OptIn(UnstableApi::class)
@Composable
fun PlayerView(
    url: String? = null,
    payload: InputStreamDataSourcePayload? = null,
) {
    val context = LocalContext.current

    val exoPlayer = ExoPlayer.Builder(context).build()

    val mediaItem = remember(url) {
        url?.let {
            MediaItem.fromUri(it)
        }
    }

    val inputStreamDataSourceRemembered = remember(payload) {
        payload
    }

    LaunchedEffect(mediaItem) {
        mediaItem?.let {
            exoPlayer.setMediaItem(it)
            exoPlayer.prepare()
        }
    }

    LaunchedEffect(inputStreamDataSourceRemembered) {
        inputStreamDataSourceRemembered?.let {
            val dataSourceFactory = InputStreamDataSourceFactory(it, context)
            val mediaSource =
                ProgressiveMediaSource.Factory(dataSourceFactory, DefaultExtractorsFactory())
                    .createMediaSource(MediaItem.fromUri(Uri.EMPTY))

            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.addAnalyticsListener(EventLogger())
            exoPlayer.prepare()
        }
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