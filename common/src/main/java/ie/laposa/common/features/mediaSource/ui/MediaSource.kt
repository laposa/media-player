package ie.laposa.common.features.mediaSource.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ie.laposa.common.R
import ie.laposa.common.features.menu.ui.SectionItem
import ie.laposa.domain.mediaSource.model.MediaSourceType
import ie.laposa.domain.mediaSource.model.MediaSource as MediaSourceModel

@Composable
fun MediaSource(
    mediaSource: MediaSourceModel,
    onClick: (mediaSource: MediaSourceModel) -> Unit,
    sharesList: List<String> = emptyList(),
    onShareClick: (share: String) -> Unit,
    isLoading: Boolean = false
) {
    fun getIcon(mediaSource: MediaSourceModel): Int {
        return when (mediaSource.type) {
            is MediaSourceType.ZeroConf.SMB -> R.drawable.smb_share
            is MediaSourceType.ZeroConf.FTP -> R.drawable.dns
            is MediaSourceType.ZeroConf.WebDAV -> R.drawable.dns
            is MediaSourceType.ZeroConf.NFS -> R.drawable.dns
        }
    }

    Column {
        SectionItem(
            title = mediaSource.displayName,
            icon = getIcon(mediaSource),
            onClick = { onClick(mediaSource) },
            isLoading = isLoading,
        )
        Column(modifier = Modifier.padding(start = 16.dp)) {
            for (share in sharesList) {
                SectionItem(
                    title = share,
                    icon = R.drawable.smb_share,
                    onClick = { onShareClick(share) },
                    isLoading = isLoading,
                )
            }
        }
    }

}