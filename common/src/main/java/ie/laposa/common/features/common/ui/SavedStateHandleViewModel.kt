package ie.laposa.common.features.common.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ie.laposa.common.features.mediaLib.model.Media
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.networkProtocols.smb.InputStreamDataSourcePayload
import javax.inject.Inject

class SavedStateHandleViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    fun setSelectedMedia(media: MediaSourceFile) {
        savedStateHandle[KEY_SELECTED_MEDIA] = media
    }

    fun getSelectedMedia(): LiveData<MediaSourceFile?> {
        return savedStateHandle.getLiveData(KEY_SELECTED_MEDIA)
    }

    fun clearSelectedMedia() {
        savedStateHandle[KEY_SELECTED_MEDIA] = null
    }

    fun setSelectedInputStreamDataSourceFileName(fileName: String) {
        savedStateHandle[KEY_SELECTED_INPUT_STREAM_DATA_SOURCE] = fileName
    }

    fun getSelectedInputStreamDataSourceFileName(): LiveData<String?> {
        return savedStateHandle.getLiveData(KEY_SELECTED_INPUT_STREAM_DATA_SOURCE)
    }

    fun clearSelectedInputStreamDataSourceFileName() {
        savedStateHandle[KEY_SELECTED_INPUT_STREAM_DATA_SOURCE] = null
    }


    companion object {
        private const val KEY_SELECTED_MEDIA = "selectedMedia"
        private const val KEY_SELECTED_INPUT_STREAM_DATA_SOURCE = "selectedInputStreamDataSource"
    }
}