package com.laposa.common.features.recents.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Card
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import ie.laposa.common.R

@Composable
fun RecentsLib(
    viewModel: RecentsLibViewModel = hiltViewModel()
) {
    val recentMedia = viewModel.recentMedia.collectAsState(initial = emptyList()).value

    val focusRequester = remember {
        FocusRequester()
    }

    if (recentMedia.isEmpty()) return

    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp),
        contentPadding = PaddingValues(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(recentMedia) { media ->
            Card(
                onClick = { },
                modifier = Modifier
                    .height(90.dp)
                    .width(150.dp)
                    .focusRequester(focusRequester)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    media.thumbnailPath?.let { imagePath ->
                        loadBitmapFromStorage(filePath = imagePath)?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.size(300.dp)
                            )
                        }
                    } ?: Icon(
                        painterResource(R.drawable.ic_block),
                        contentDescription = "No thumbnail",
                        tint = Color.White
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = media.file.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                color = Color.White
            )
        }
    }
}

@Composable
fun loadBitmapFromStorage(filePath: String): Bitmap? {
    return remember(filePath) {
        BitmapFactory.decodeFile(filePath)
    }
}
