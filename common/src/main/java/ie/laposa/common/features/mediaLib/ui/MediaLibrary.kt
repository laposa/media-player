package ie.laposa.common.features.mediaLib.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import ie.laposa.common.features.mediaLib.model.Media

@Composable
fun MediaLibrary(
    mediaItems: List<Media>,
    onMediaSelected: (Media) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(mediaItems) { mediaItem ->
            MediaItem(mediaItem, onMediaSelected)
        }
    }
}