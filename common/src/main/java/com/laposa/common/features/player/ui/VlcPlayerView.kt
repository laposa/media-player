package com.laposa.common.features.player.ui

import android.graphics.Bitmap
import android.util.Log.e
import android.view.KeyEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.laposa.domain.mediaSource.model.MediaSourceFile
import org.videolan.libvlc.Dialog
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import java.net.URI

@Composable
fun VlcPlayerView(
    mediaSourceFile: MediaSourceFile,
    url: String? = null,
    dismiss: () -> Unit,
    saveThumbnail: ((fileName: String, bitmap: Bitmap, progress: Long) -> Unit)? = null,
) {
    val context = LocalContext.current
    val credentials = remember(url) { parseCredentials(url) }

    val libVlc = remember { LibVLC(context, ArrayList<String>().apply {
        add("-vvv") // Verbose logging
    }) }
    val mediaPlayer = remember { MediaPlayer(libVlc) }

    val videoSurfaceView = remember {
        SurfaceView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder) {}
                override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                    mediaPlayer.vlcVout.setWindowSize(width, height)
                }
                override fun surfaceDestroyed(holder: SurfaceHolder) {}
            })
        }
    }

    LaunchedEffect(url) {
       url?.let {
            println("Playing URL: $it")
            val vout = mediaPlayer.vlcVout
            vout.setVideoView(videoSurfaceView)
            vout.attachViews()

            val media = Media(libVlc, it.toUri())
            media.addOption(":network-caching=1500")

            mediaPlayer.media = media
            media.release()
            
            mediaPlayer.play()
        }
    }

    DisposableEffect(libVlc, credentials) {
        Dialog.setCallbacks(libVlc, object : Dialog.Callbacks {
            override fun onDisplay(dialog: Dialog.ErrorMessage) {
                e(TAG , "VLC error dialog: ${dialog.title}: ${dialog.text}")
            }

            override fun onDisplay(dialog: Dialog.LoginDialog) {
                val username = credentials?.first
                if (username.isNullOrEmpty()) {
                    dialog.dismiss()
                    return
                }

                dialog.postLogin(username, credentials.second.orEmpty(), false)
            }

            override fun onDisplay(dialog: Dialog.QuestionDialog) {
                dialog.dismiss()
            }

            override fun onDisplay(dialog: Dialog.ProgressDialog) {
            }

            override fun onCanceled(dialog: Dialog) {
            }

            override fun onProgressUpdate(dialog: Dialog.ProgressDialog) {
            }
        })

        onDispose {
            Dialog.setCallbacks(libVlc, null)
            mediaPlayer.stop()
            mediaPlayer.vlcVout.detachViews()
            mediaPlayer.release()
            libVlc.release()
        }
    }

    BackHandler {
        dismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                    when (keyEvent.nativeKeyEvent.keyCode) {
                        KeyEvent.KEYCODE_BACK -> {
                            dismiss()
                            true
                        }
                        KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                            if (mediaPlayer.isPlaying) {
                                mediaPlayer.pause()
                            } else {
                                mediaPlayer.play()
                            }
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }
    ) {
        AndroidView(
            factory = { videoSurfaceView },
            modifier = Modifier.fillMaxSize()
        )
    }
}

private fun parseCredentials(url: String?): Pair<String, String?>? {
    if (url.isNullOrEmpty()) return null

    val userInfo = runCatching { URI(url).userInfo }.getOrNull() ?: return null
    val parts = userInfo.split(':', limit = 2)
    val username = parts.firstOrNull()?.takeIf { it.isNotEmpty() } ?: return null
    val password = parts.getOrNull(1)

    return username to password
}

private const val TAG = "VlcPlayerView"
