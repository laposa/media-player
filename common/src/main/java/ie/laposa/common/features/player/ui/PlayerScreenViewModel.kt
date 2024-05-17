package ie.laposa.common.features.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ie.laposa.common.features.common.ui.SavedStateHandleViewModel
import ie.laposa.common.features.mediaLib.model.Media
import javax.inject.Inject

@HiltViewModel
class PlayerScreenViewModel @Inject constructor(
    savedStateHandleViewModel: SavedStateHandleViewModel
): ViewModel() {
    init {
        println("PlayerScreenViewModel created")
    }

    val selectedMedia : LiveData<Media?> = savedStateHandleViewModel.getSelectedMedia()
}