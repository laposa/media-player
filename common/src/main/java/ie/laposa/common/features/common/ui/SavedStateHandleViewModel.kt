package ie.laposa.common.features.common.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ie.laposa.common.features.mediaLib.model.Media
import ie.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload

class SavedStateHandleViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    fun setSelectedMedia(media: Media) {
        savedStateHandle[KEY_SELECTED_MEDIA] = media
    }

    fun getSelectedMedia(): LiveData<Media?> {
        return savedStateHandle.getLiveData(KEY_SELECTED_MEDIA)
    }

    fun clearSelectedMedia() {
        savedStateHandle.remove<Media>(KEY_SELECTED_MEDIA)
    }

    fun setSelectedInputStreamDataSourceFileName(fileName: String) {
        savedStateHandle[KEY_SELECTED_INPUT_STREAM_DATA_SOURCE] = fileName
    }

    fun getSelectedInputStreamDataSourceFileName(): LiveData<String?> {
        return savedStateHandle.getLiveData(KEY_SELECTED_INPUT_STREAM_DATA_SOURCE)
    }

    fun clearSelectedInputStreamDataSourceFileName() {
        savedStateHandle.remove<InputStreamDataSourcePayload>(KEY_SELECTED_INPUT_STREAM_DATA_SOURCE)
    }


    companion object {
        private const val KEY_SELECTED_MEDIA = "selectedMedia"
        private const val KEY_SELECTED_INPUT_STREAM_DATA_SOURCE = "selectedInputStreamDataSource"
    }
}