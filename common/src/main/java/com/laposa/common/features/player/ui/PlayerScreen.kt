package com.laposa.common.features.player.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date

@Composable
fun PlayerScreen(
    hidePlayerScreen: () -> Unit,
    viewModel: PlayerScreenViewModel = hiltViewModel()
) {
    val selectedMedia = viewModel.selectedMedia.observeAsState().value
    val selectedInputStreamDataSourcePayload =
        viewModel.selectedInputStreamDataSourcePayload.collectAsState().value
    val selectedInputStreamDataSourceFileName =
        viewModel.selectedInputStreamDataSourceFileName.observeAsState().value

    val fileName = selectedMedia?.path ?: selectedInputStreamDataSourceFileName ?: "file"

    val context = LocalContext.current

    var shouldDismiss by remember {
        mutableStateOf(false)
    }

    viewModel.selectedMedia.observeForever {
        println("selectedMedia changed to $it")
    }

    fun saveThumbnail(fileName: String, bitmap: Bitmap, progress: Long) {
        viewModel.launch {
            saveBitmapToTempFile(fileName, context, bitmap)?.let {
                viewModel.saveLastPlayedMediaToRecents(it, progress)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelectedMedia()
        }
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
            selectedMedia?.let {
                PlayerView(
                    fileName = fileName,
                    url = it.path,
                    shouldDismiss = shouldDismiss,
                    dismiss = hidePlayerScreen,
                    saveThumbnail = ::saveThumbnail
                )
            } ?: selectedInputStreamDataSourcePayload?.let {
                PlayerView(
                    fileName = fileName,
                    payload = it,
                    shouldDismiss = shouldDismiss,
                    dismiss = hidePlayerScreen,
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