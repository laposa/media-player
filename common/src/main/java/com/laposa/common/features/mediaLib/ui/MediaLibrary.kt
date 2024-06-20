package com.laposa.common.features.mediaLib.ui

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import com.laposa.domain.mediaSource.model.MediaSourceDirectory
import com.laposa.domain.mediaSource.model.MediaSourceFile
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.mediaSource.model.MediaSourceShare
import kotlinx.coroutines.flow.StateFlow
import com.laposa.domain.mediaSource.model.MediaSourceGoUp

@Composable
fun MediaLibrary(
    title: String,
    files: StateFlow<List<MediaSourceFileBase>>,
    path: String,
    defaultPathDepthLevel: Int = 1,
    onMediaFileSelect: (MediaSourceFile) -> Unit,
    onMediaDirectorySelect: (MediaSourceDirectory) -> Unit,
    onMediaShareSelect: (MediaSourceShare) -> Unit,
    onGoUp: () -> Unit,
) {
    val currentFiles = files.collectAsState().value

    MediaLibraryInner(
        title,
        currentFiles,
        path,
        defaultPathDepthLevel,
        onMediaFileSelect,
        onMediaDirectorySelect,
        onMediaShareSelect,
        onGoUp
    )
}

@Composable
fun MediaLibrary(
    title: String,
    files: List<MediaSourceFileBase>,
    path: String,
    defaultPathDepthLevel: Int = 1,
    onMediaFileSelect: (MediaSourceFile) -> Unit,
    onMediaDirectorySelect: (MediaSourceDirectory) -> Unit,
    onMediaShareSelect: (MediaSourceShare) -> Unit,
    onGoUp: () -> Unit,
) {
    MediaLibraryInner(
        title,
        files,
        path,
        defaultPathDepthLevel,
        onMediaFileSelect,
        onMediaDirectorySelect,
        onMediaShareSelect,
        onGoUp
    )
}

@Composable
private fun MediaLibraryInner(
    title: String,
    files: List<MediaSourceFileBase>,
    path: String,
    defaultPathDepthLevel: Int = 1,
    onMediaFileSelect: (MediaSourceFile) -> Unit,
    onMediaDirectorySelect: (MediaSourceDirectory) -> Unit,
    onMediaShareSelect: (MediaSourceShare) -> Unit,
    onGoUp: () -> Unit,
) {
    fun getCurrentFiles(): List<MediaSourceFileBase> {
        fun getPathDepthLevel(): Int {
            return path.split("/").size
        }

        if (getPathDepthLevel() >= defaultPathDepthLevel && path.isNotEmpty()) {
            return listOf(MediaSourceGoUp()) + files
        }

        return files
    }

    fun getCurrentTitle(): String {
        return if (path.isNotEmpty()) "$title > ${path.split("/").joinToString(" > ")}" else title
    }

    Column {
        Text(getCurrentTitle(), style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val currentFiles = getCurrentFiles()
            items(currentFiles) { mediaItem ->
                val index = currentFiles.indexOf(mediaItem)
                MediaItem(mediaItem, index) {
                    onMediaSelect(
                        it, onMediaFileSelect, onMediaDirectorySelect, onMediaShareSelect, onGoUp
                    )
                }
            }
        }
    }
}

private fun onMediaSelect(
    media: MediaSourceFileBase,
    onMediaFileSelect: (MediaSourceFile) -> Unit,
    onMediaDirectorySelect: (MediaSourceDirectory) -> Unit,
    onMediaShareSelect: (MediaSourceShare) -> Unit,
    onGoUpSelect: () -> Unit,
) {
    when (media) {
        is MediaSourceFile -> {
            onMediaFileSelect(media)
        }

        is MediaSourceDirectory -> {
            onMediaDirectorySelect(media)
        }

        is MediaSourceShare -> {
            onMediaShareSelect(media)
        }

        is MediaSourceGoUp -> {
            onGoUpSelect()
        }

        else -> {
            throw NotImplementedError(media.name)
        }
    }
}