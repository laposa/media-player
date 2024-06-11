package ie.laposa.common.features.mediaLib.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.MaterialTheme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ie.laposa.common.features.mediaLib.model.Media

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MediaItem(
    media: Media,
    onSelect: (Media) -> Unit,
) {
    Column {
        Card(
            onClick = { onSelect(media) },
            Modifier.height(138.dp).width(200.dp)
        ) {
            GlideImage(
                model = media.thumbnailUrl,
                contentDescription = media.title,
                contentScale = ContentScale.Crop,
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = media.title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            color = Color.White
        )
    }
}