package ie.laposa.common.features.mediaLib.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import ie.laposa.domain.mediaSource.model.MediaSourceDirectory
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.mediaSource.model.MediaSourceFileBase
import ie.laposa.domain.mediaSource.model.MediaSourceShare
import kotlinx.coroutines.flow.StateFlow
import ie.laposa.domain.mediaSource.model.MediaSourceGoUp

@Composable
fun MediaLibrary(
    title: String,
    files: StateFlow<List<MediaSourceFileBase>>,
    onMediaFileSelect: (MediaSourceFile) -> Unit,
    onMediaDirectorySelect: (MediaSourceDirectory) -> Unit,
    onMediaShareSelect: (MediaSourceShare) -> Unit,
) {
    val currentFiles = files.collectAsState().value

    var path by remember {
        mutableStateOf("")
    }

    fun updatePath(newPath: String) {
        path = newPath
    }

    fun onMediaDirectorySelected(directory: MediaSourceDirectory) {
        updatePath(directory.path)
        onMediaDirectorySelect(directory)
    }

    fun onMediaShareSelected(share: MediaSourceShare) {
        updatePath(share.name)
        onMediaShareSelect(share)
    }

    MediaLibraryInner(
        title,
        currentFiles,
        path,
        onMediaFileSelect,
        ::onMediaDirectorySelected,
        ::onMediaShareSelected
    )
}

@Composable
private fun MediaLibraryInner(
    title: String,
    files: List<MediaSourceFileBase>,
    path: String,
    onMediaFileSelect: (MediaSourceFile) -> Unit,
    onMediaDirectorySelect: (MediaSourceDirectory) -> Unit,
    onMediaShareSelect: (MediaSourceShare) -> Unit,
) {
    fun getCurrentFiles(): List<MediaSourceFileBase> {
        fun getPathDepthLevel(): Int {
            return path.split("/").size
        }

        if (getPathDepthLevel() > 1) {
            return listOf(MediaSourceGoUp()) + files
        }

        return files
    }

    fun onGoUp() {
        fun getDirectoryOneLevelUp(): MediaSourceDirectory {
            val pathSplit = path.split("/")
            val pathOneLevelUpSplit = pathSplit.take(pathSplit.size - 1)
            val currentPath = pathOneLevelUpSplit.joinToString("/")
            val name = pathOneLevelUpSplit.last()

            println("Go up from: $path to: $currentPath")

            return MediaSourceDirectory(
                name = name,
                path = currentPath
            )
        }

        onMediaDirectorySelect(
            getDirectoryOneLevelUp()
        )
    }


    Column {
        Text(title, style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
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
                        it,
                        onMediaFileSelect,
                        onMediaDirectorySelect,
                        onMediaShareSelect,
                        ::onGoUp
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