package ie.laposa.common.features.mediaSource.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ie.laposa.common.R
import ie.laposa.common.features.menu.ui.menuSections.SectionItem
import ie.laposa.domain.mediaSource.model.MediaSourceType
import ie.laposa.domain.mediaSource.model.MediaSource as MediaSourceModel

@Composable
fun MediaSource(
    mediaSource: MediaSourceModel,
    onClick: (mediaSource: MediaSourceModel) -> Unit,
    isLoading: Boolean = false,
    isSelected: Boolean = false,
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
            subTitle = mediaSource.type.toString(),
            iconResource = getIcon(mediaSource),
            isLoading = isLoading,
            onClick = { onClick(mediaSource) },
            isSelected = isSelected,
        )
    }
}