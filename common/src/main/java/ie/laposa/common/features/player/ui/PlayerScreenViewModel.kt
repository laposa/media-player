package ie.laposa.common.features.player.ui

import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ie.laposa.common.features.common.ViewModelBase
import ie.laposa.domain.mediaSource.MediaSourceService
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import ie.laposa.domain.recents.RecentMedia
import ie.laposa.domain.recents.RecentMediaService
import ie.laposa.domain.savedState.SavedStateService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlayerScreenViewModel @Inject constructor(
    private val savedStateService: SavedStateService,
    mediaSourceService: MediaSourceService,
    private val recentMediaService: RecentMediaService,
) : ViewModelBase() {
    val selectedMedia: LiveData<MediaSourceFile?> = savedStateService.getSelectedMedia()

    private val _selectedInputStreamDataSourceFile =
        savedStateService.getSelectedInputStreamDataSourceFileName()

    val selectedInputStreamDataSourceFileName: LiveData<String?> =
        _selectedInputStreamDataSourceFile

    private var _selectedInputStreamDataSourcePayload: MutableStateFlow<InputStreamDataSourcePayload?> =
        MutableStateFlow(null)
    val selectedInputStreamDataSourcePayload: StateFlow<InputStreamDataSourcePayload?> =
        _selectedInputStreamDataSourcePayload

    init {
        _selectedInputStreamDataSourceFile.observeForever {
            it?.let {
                launch {
                    val mediaSourceFile = mediaSourceService.getFile(it)
                    _selectedInputStreamDataSourcePayload.value = mediaSourceFile
                }
            }
        }
    }

    fun clearSelectedMedia() {
        savedStateService.clearSelectedMedia()
        savedStateService.clearSelectedInputStreamDataSourceFileName()
        _selectedInputStreamDataSourcePayload.value = null
    }

    fun saveLastPlayedMediaToRecents(thumbnailFilePath: String, progress: Long) {
        selectedMedia.value?.let { media ->
            media.type?.let { type ->
                recentMediaService.addRecentMedia(
                    RecentMedia(
                        mediaSourceType = type,
                        file = media,
                        thumbnailPath = thumbnailFilePath,
                        thumbnailUrl = null,
                        progress = progress,
                    )
                )
            }
        }
    }
}