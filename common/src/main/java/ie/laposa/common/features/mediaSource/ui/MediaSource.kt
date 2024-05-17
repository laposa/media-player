package ie.laposa.common.features.mediaSource.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ie.laposa.common.R
import ie.laposa.domain.mediaSource.model.MediaSourceType
import ie.laposa.domain.mediaSource.model.MediaSource as MediaSourceModel

@Composable
fun MediaSource(mediaSource: MediaSourceModel, onClick: (mediaSource: MediaSourceModel) -> Unit) {
    fun getIcon(mediaSource: MediaSourceModel): Int {
        return when (mediaSource.type) {
            is MediaSourceType.ZeroConf.SMB -> R.drawable.smb_share
            is MediaSourceType.ZeroConf.FTP -> R.drawable.dns
            is MediaSourceType.ZeroConf.WebDAV -> R.drawable.dns
            is MediaSourceType.ZeroConf.NFS -> R.drawable.dns
        }
    }

    Card(
        onClick = { onClick(mediaSource) },
        modifier = Modifier.width(150.dp).height(150.dp).padding(16.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Column {
                Text(mediaSource.displayName, Modifier.padding(8.dp))
                Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(mediaSource.sourceName, Modifier.padding(8.dp))
                    Icon(
                        painter = painterResource(getIcon(mediaSource)),
                        contentDescription = mediaSource.type.toString(),
                        modifier = Modifier.width(42.dp).height(42.dp).padding(PaddingValues(end = 8.dp, bottom = 8.dp))
                    )
                }
            }
        }
    }


}