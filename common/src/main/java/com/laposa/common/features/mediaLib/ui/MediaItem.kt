package com.laposa.common.features.mediaLib.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import com.laposa.domain.mediaSource.model.MediaSourceDirectory
import com.laposa.domain.mediaSource.model.MediaSourceFile
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.mediaSource.model.MediaSourceGoUp
import com.laposa.domain.mediaSource.model.MediaSourceShare
import ie.laposa.common.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MediaItem(
    media: MediaSourceFileBase,
    index: Int,
    onSelect: (MediaSourceFileBase) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    val subTitle =
        when (media) {
            is MediaSourceGoUp -> "BACK"
            else -> null
        }

    LaunchedEffect(media) {
        if (index == 0) {
            focusRequester.requestFocus()
        }
    }

    Column {
        Card(
            onClick = { onSelect(media) },
            modifier = Modifier
                .height(90.dp)
                .width(150.dp)
                .focusRequester(focusRequester)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (media is MediaSourceFile) {
                        if (media.thumbnailUrl != null) {
                            GlideImage(
                                model = media.thumbnailUrl,
                                contentDescription = media.name,
                                contentScale = ContentScale.Crop,
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_media),
                                contentDescription = media.name,
                                Modifier
                                    .size(56.dp)
                            )
                        }
                    } else {
                        Icon(
                            painter = painterResource(getMediaItemIconId(media)),
                            contentDescription = media.name,
                            Modifier
                                .size(56.dp)
                        )
                    }
                    if (subTitle != null) {
                        Text(
                            subTitle,
                            style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = media.name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

fun getMediaItemIconId(media: MediaSourceFileBase): Int {
    return when (media) {
        is MediaSourceFile -> R.drawable.ic_media
        is MediaSourceDirectory -> R.drawable.ic_folder
        is MediaSourceShare -> R.drawable.smb_share
        is MediaSourceGoUp -> R.drawable.ic_move_up
        else -> R.drawable.ic_block
    }
}