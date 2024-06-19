package ie.laposa.common.features.home.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ie.laposa.common.features.common.ui.SavedStateHandleViewModel
import ie.laposa.common.features.mediaLib.model.Media
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val savedStateHandleViewModel: SavedStateHandleViewModel
) : ViewModel() {
    private var _content: MutableStateFlow<(@Composable () -> Unit)?> = MutableStateFlow(null)
    val content: StateFlow<(@Composable () -> Unit)?> = _content

    private val _isPlayerVisible = MutableStateFlow(false)
    val isPlayerVisible: StateFlow<Boolean> = _isPlayerVisible

    fun showPlayer(fileToPlay: MediaSourceFile?, filePath: String?) {
        fileToPlay?.let {
            savedStateHandleViewModel.setSelectedMedia(fileToPlay)
        }

        filePath?.let {
            savedStateHandleViewModel.setSelectedInputStreamDataSourceFileName(filePath)
        }

        _isPlayerVisible.value = true
    }

    fun hidePlayer() {
        println("TADY JSEM KURVA")
        savedStateHandleViewModel.clearSelectedMedia()
        savedStateHandleViewModel.clearSelectedInputStreamDataSourceFileName()
        _isPlayerVisible.value = false
    }

    fun setContent(content: @Composable () -> Unit) {
        _content.value = content
    }
}