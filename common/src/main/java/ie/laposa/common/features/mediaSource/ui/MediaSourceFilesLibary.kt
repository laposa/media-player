package ie.laposa.common.features.mediaSource.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ie.laposa.domain.mediaSource.model.MediaSourceFile

@Composable
fun MediaSourceFilesLibary(
    files: List<MediaSourceFile>,
    folderName: String,
    isLoading: Boolean = false,
    onFileSelected: (MediaSourceFile) -> Unit,
) {
    Column {
        Text(
            text = folderName,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.defaultMinSize(100.dp, 100.dp)) {
            Box(modifier = Modifier.padding(16.dp)) {
                Column {
                    Text(
                        text = "Media files in $folderName",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Box(Modifier.height(16.dp))
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(64.dp)
                                .padding(16.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    } else {
                        LazyColumn(modifier = Modifier.width(300.dp).height(300.dp)) {
                            for (item in files) {
                                item {
                                    Box(
                                        Modifier.fillMaxWidth().height(30.dp)
                                            .clickable { onFileSelected(item) }.border(
                                                1.dp,
                                                MaterialTheme.colorScheme.onSurfaceVariant,
                                                MaterialTheme.shapes.small
                                            )
                                    ) {
                                        Text(
                                            text = item.fileName,
                                            modifier = Modifier.padding(4.dp)
                                                .align(Alignment.CenterStart)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}