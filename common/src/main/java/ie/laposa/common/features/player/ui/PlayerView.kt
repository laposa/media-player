package ie.laposa.common.features.player.ui

import android.graphics.Bitmap
import android.net.Uri
import android.view.KeyEvent
import android.view.SurfaceView
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
import androidx.core.view.drawToBitmap
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.extractor.DefaultExtractorsFactory
import ie.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import androidx.media3.ui.PlayerView as ExoPlayerView


@OptIn(UnstableApi::class)
@Composable
fun PlayerView(
    fileName: String,
    shouldDismiss: Boolean,
    dismiss: () -> Unit,
    url: String? = null,
    payload: InputStreamDataSourcePayload? = null,
    saveThumbnail: ((fileName: String, bitmap: Bitmap, progress: Long) -> Unit)?,
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

    val captureSurfaceView: SurfaceView = remember {
        SurfaceView(context)
    }

    LaunchedEffect(mediaItem) {
        mediaItem?.let {
            exoPlayer.setMediaItem(it)
            exoPlayer.setVideoSurfaceView(captureSurfaceView)
            exoPlayer.prepare()
            exoPlayer.play()
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

            exoPlayer.setVideoSurfaceView(captureSurfaceView)
            exoPlayer.prepare()
            exoPlayer.play()
        }
    }

    if (shouldDismiss) {
        saveThumbnail?.let {
            captureSurfaceView.drawToBitmap().let {
                it(fileName, it, exoPlayer.currentPosition)
            }
        }
        dismiss()
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box {
        AndroidView(factory = { ctx -> captureSurfaceView })
        AndroidView(
            factory = { ctx ->
                ExoPlayerView(ctx).apply {
                    player = exoPlayer
                }
            },
            modifier = Modifier
                .focusable()
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

}