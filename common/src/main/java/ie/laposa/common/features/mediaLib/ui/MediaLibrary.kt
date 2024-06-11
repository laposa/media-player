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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.MaterialTheme
import ie.laposa.common.features.mediaLib.model.FileSystemItem
import ie.laposa.common.features.mediaLib.model.Media
import ie.laposa.common.features.mediaSource.ui.MediaSourcesLibraryViewModel
import kotlinx.coroutines.flow.map

@Composable
fun MediaLibrary(
    id: String,
    title: String,
    viewModel: MediaSourcesLibraryViewModel = hiltViewModel(),
    onMediaSelected: (FileSystemItem) -> Unit,
) {
    val files =
        viewModel.files.map {
            it[id]?.map {
                Media(
                    fileName = it.fileName,
                    filePath = it.fileName,
                )
            }
        }.collectAsState(initial = emptyList()).value ?: emptyList()

    MediaLibraryInner(title, files, onMediaSelected)
}

@Composable
fun MediaLibrary(
    title: String,
    files: List<FileSystemItem>,
    onMediaSelected: (FileSystemItem) -> Unit,
) {
    MediaLibraryInner(title, files, onMediaSelected)
}

@Composable
private fun MediaLibraryInner(
    title: String,
    files: List<FileSystemItem>,
    onMediaSelected: (FileSystemItem) -> Unit,
) {
    Column {
        Text(title, style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(files) { mediaItem ->
                MediaItem(mediaItem, onMediaSelected)
            }
        }
    }
}