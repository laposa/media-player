package ie.laposa.common.features.mediaLib.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ie.laposa.common.features.mediaLib.model.Media

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MediaItem(
    media: Media,
    onSelect: (Media) -> Unit
) {
    Card(
        onClick = { onSelect(media) },
        modifier = Modifier.height(128.dp).width(128.dp)
    ) {
        GlideImage(
            model = media.thumbnailUrl,
            contentDescription = media.title,
            contentScale = ContentScale.Crop,

        )
    }
}