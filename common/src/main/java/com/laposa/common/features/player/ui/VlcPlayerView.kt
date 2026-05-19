package com.laposa.common.features.player.ui

import android.graphics.Bitmap
import android.util.Log.e
import android.view.KeyEvent
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.laposa.domain.mediaSource.model.MediaSourceFile
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.videolan.libvlc.Dialog
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import java.net.URI

private const val CONTROLS_AUTO_HIDE_MS = 3_500L
private const val SEEK_STEP_MS = 10_000L

@Composable
fun VlcPlayerView(
    mediaSourceFile: MediaSourceFile,
    url: String? = null,
    dismiss: () -> Unit,
    saveThumbnail: ((fileName: String, bitmap: Bitmap, progress: Long) -> Unit)? = null,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentials = remember(url) { parseCredentials(url) }

    val libVlc = remember {
        LibVLC(context, ArrayList<String>().apply { add("-vvv") })
    }
    val mediaPlayer = remember { MediaPlayer(libVlc) }

    // ── UI state ────────────────────────────────────────────────────────────
    var isPlaying by remember { mutableStateOf(false) }
    var isBuffering by remember { mutableStateOf(false) }
    var positionMs by remember { mutableLongStateOf(0L) }
    var durationMs by remember { mutableLongStateOf(0L) }
    var showControls by remember { mutableStateOf(false) }
    var autoHideJob by remember { mutableStateOf<Job?>(null) }

    fun scheduleHide() {
        autoHideJob?.cancel()
        autoHideJob = scope.launch {
            delay(CONTROLS_AUTO_HIDE_MS)
            showControls = false
        }
    }

    fun showControls() {
        showControls = true
        scheduleHide()
    }

    fun togglePlayPause() {
        if (mediaPlayer.isPlaying) mediaPlayer.pause() else mediaPlayer.play()
        showControls()
    }

    fun seekBack() {
        mediaPlayer.time = (mediaPlayer.time - SEEK_STEP_MS).coerceAtLeast(0L)
        showControls()
    }

    fun seekForward() {
        val target = mediaPlayer.time + SEEK_STEP_MS
        if (durationMs > 0) mediaPlayer.time = target.coerceAtMost(durationMs)
        showControls()
    }

    // ── SurfaceView ─────────────────────────────────────────────────────────
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

    // ── MediaPlayer events ──────────────────────────────────────────────────
    DisposableEffect(mediaPlayer) {
        val listener = MediaPlayer.EventListener { event ->
            scope.launch {
                when (event.type) {
                    MediaPlayer.Event.Playing -> { isPlaying = true; isBuffering = false }
                    MediaPlayer.Event.Buffering -> isBuffering = event.buffering < 100f
                    MediaPlayer.Event.Paused,
                    MediaPlayer.Event.Stopped -> isPlaying = false
                    MediaPlayer.Event.TimeChanged -> positionMs = event.timeChanged
                    MediaPlayer.Event.LengthChanged -> durationMs = event.lengthChanged
                }
            }
        }
        mediaPlayer.setEventListener(listener)
        onDispose { mediaPlayer.setEventListener(null) }
    }

    // ── Start playback ──────────────────────────────────────────────────────
    LaunchedEffect(url) {
        url?.let {
            isBuffering = true
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

    // ── VLC dialog callbacks ────────────────────────────────────────────────
    DisposableEffect(libVlc, credentials) {
        Dialog.setCallbacks(libVlc, object : Dialog.Callbacks {
            override fun onDisplay(dialog: Dialog.ErrorMessage) {
                e(TAG, "VLC error dialog: ${dialog.title}: ${dialog.text}")
            }
            override fun onDisplay(dialog: Dialog.LoginDialog) {
                val username = credentials?.first
                if (username.isNullOrEmpty()) { dialog.dismiss(); return }
                dialog.postLogin(username, credentials.second.orEmpty(), false)
            }
            override fun onDisplay(dialog: Dialog.QuestionDialog) { dialog.dismiss() }
            override fun onDisplay(dialog: Dialog.ProgressDialog) {}
            override fun onCanceled(dialog: Dialog) {}
            override fun onProgressUpdate(dialog: Dialog.ProgressDialog) {}
        })
        onDispose {
            Dialog.setCallbacks(libVlc, null)
            mediaPlayer.stop()
            mediaPlayer.vlcVout.detachViews()
            mediaPlayer.release()
            libVlc.release()
        }
    }

    // ── Back handler ─────────────────────────────────────────────────────────
    BackHandler {
        if (showControls) {
            showControls = false
            autoHideJob?.cancel()
        } else {
            dismiss()
        }
    }

    // ── Layout ───────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.nativeKeyEvent.action != KeyEvent.ACTION_DOWN) return@onKeyEvent false
                when (keyEvent.nativeKeyEvent.keyCode) {
                    KeyEvent.KEYCODE_BACK -> false // handled by BackHandler
                    KeyEvent.KEYCODE_DPAD_CENTER,
                    KeyEvent.KEYCODE_ENTER -> {
                        // If controls are hidden → show them.
                        // If controls are shown, the focused button handles the event first
                        // (returns true), so this branch only fires when nothing in the
                        // overlay is focused yet.
                        if (!showControls) { showControls(); true } else false
                    }
                    KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> { togglePlayPause(); true }
                    KeyEvent.KEYCODE_MEDIA_PLAY -> { mediaPlayer.play(); showControls(); true }
                    KeyEvent.KEYCODE_MEDIA_PAUSE -> { mediaPlayer.pause(); showControls(); true }
                    KeyEvent.KEYCODE_MEDIA_REWIND,
                    KeyEvent.KEYCODE_MEDIA_STEP_BACKWARD -> { seekBack(); true }
                    KeyEvent.KEYCODE_MEDIA_FAST_FORWARD,
                    KeyEvent.KEYCODE_MEDIA_STEP_FORWARD -> { seekForward(); true }
                    else -> { if (!showControls) showControls(); false }
                }
            }
    ) {
        AndroidView(
            factory = { videoSurfaceView },
            modifier = Modifier.fillMaxSize(),
        )

        if (isBuffering) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.2f),
                strokeWidth = 4.dp,
            )
        }

        VlcPlayerControls(
            visible = showControls,
            title = mediaSourceFile.name,
            isPlaying = isPlaying,
            positionMs = positionMs,
            durationMs = durationMs,
            onPlayPause = ::togglePlayPause,
            onSeekBack = ::seekBack,
            onSeekForward = ::seekForward,
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
