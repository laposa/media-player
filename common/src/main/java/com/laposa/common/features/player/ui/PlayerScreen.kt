package com.laposa.common.features.player.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Text
import com.laposa.common.theme.VideoPlayerTypography
import com.laposa.common.theme.surfaceDark
import com.laposa.domain.mediaSource.model.MediaSourceFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun PlayerScreen(
    fileToPlay: MediaSourceFile,
    hidePlayerScreen: () -> Unit,
    viewModel: PlayerScreenViewModel = hiltViewModel()
) {
    viewModel.setFileToPlay(fileToPlay)

    val fileName = viewModel.currentMediaFile.collectAsState().value?.name ?: ""
    val url = viewModel.url.collectAsState().value
    val payload = viewModel.payload.collectAsState().value

    val context = LocalContext.current

    var shouldDismiss by remember {
        mutableStateOf(false)
    }

    fun saveThumbnail(fileName: String, bitmap: Bitmap, progress: Long) {
        viewModel.launch {
            saveBitmapToTempFile(fileName, context, bitmap)?.let {
                viewModel.saveLastPlayedMediaToRecents(it, progress)
            }
        }
    }

    fun onDismiss() {
        hidePlayerScreen()
        viewModel.clearFileToPlay()
    }

    Dialog(
        onDismissRequest = { shouldDismiss = true },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(surfaceDark),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            url?.let {
                PlayerView(
                    fileName = fileName,
                    url = it,
                    shouldDismiss = shouldDismiss,
                    dismiss = ::onDismiss,
                    saveThumbnail = ::saveThumbnail
                )
            } ?: payload?.let {
                PlayerView(
                    fileName = fileName,
                    payload = it,
                    shouldDismiss = shouldDismiss,
                    dismiss = ::onDismiss,
                    saveThumbnail = ::saveThumbnail
                )
            } ?: run {
                Column {
                    Text("No media selected", style = VideoPlayerTypography.titleSmall)
                }
            }
        }
    }
}

suspend fun saveBitmapToTempFile(fileName: String, context: Context, bitmap: Bitmap): String? =
    withContext(Dispatchers.IO) {
        try {
            // Create a temporary file
            val tempFile = File.createTempFile(fileName, ".png", context.cacheDir)

            // Write the bitmap to the file
            val outputStream = FileOutputStream(tempFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            // Return the file path
            tempFile.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }