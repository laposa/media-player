package ie.laposa.common.features.player.ui

import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ie.laposa.common.features.common.ViewModelBase
import ie.laposa.common.features.common.ui.SavedStateHandleViewModel
import ie.laposa.common.features.mediaLib.model.Media
import ie.laposa.domain.mediaSource.MediaSourceService
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlayerScreenViewModel @Inject constructor(
    private val savedStateHandleViewModel: SavedStateHandleViewModel,
    mediaSourceService: MediaSourceService,
) : ViewModelBase() {
    val selectedMedia: LiveData<MediaSourceFile?> = savedStateHandleViewModel.getSelectedMedia()

    private val _selectedInputStreamDataSourceFile =
        savedStateHandleViewModel.getSelectedInputStreamDataSourceFileName()

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
        savedStateHandleViewModel.clearSelectedMedia()
        savedStateHandleViewModel.clearSelectedInputStreamDataSourceFileName()
        _selectedInputStreamDataSourcePayload.value = null
    }
}