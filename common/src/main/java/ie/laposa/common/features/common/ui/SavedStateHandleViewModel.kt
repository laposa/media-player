package ie.laposa.common.features.common.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ie.laposa.common.features.mediaLib.model.Media

class SavedStateHandleViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    fun setSelectedMedia(media: Media) {
        savedStateHandle["selectedMedia"] = media
    }

    fun getSelectedMedia(): LiveData<Media?> {
        return savedStateHandle.getLiveData("selectedMedia")
    }

    fun clearSelectedMedia() {
        savedStateHandle.remove<Media>("selectedMedia")
    }
}