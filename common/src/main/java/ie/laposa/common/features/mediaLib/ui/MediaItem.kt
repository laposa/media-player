package ie.laposa.common.features.mediaLib.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ie.laposa.common.R
import ie.laposa.common.features.mediaLib.model.Directory
import ie.laposa.common.features.mediaLib.model.FileSystemItem
import ie.laposa.common.features.mediaLib.model.Media

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MediaItem(
    media: FileSystemItem,
    onSelect: (FileSystemItem) -> Unit,
) {
    Column {
        Card(
            onClick = { onSelect(media) },
            modifier = Modifier
                .height(90.dp)
                .width(150.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                when (media) {
                    is Media -> {
                        if (media.thumbnailUrl != null) {
                            GlideImage(
                                model = media.thumbnailUrl,
                                contentDescription = media.fileName,
                                contentScale = ContentScale.Crop,
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_media),
                                contentDescription = media.fileName,
                                Modifier
                                    .size(56.dp)
                            )
                        }
                    }

                    is Directory -> {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_folder),
                            contentDescription = media.fileName
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = media.fileName,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            color = Color.White
        )
    }
}