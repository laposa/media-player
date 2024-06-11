package ie.laposa.common.features.home.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class HomeScreenViewModel @Inject constructor() : ViewModel() {
    private var _content: MutableStateFlow<(@Composable () -> Unit)?> = MutableStateFlow(null)
    val content: StateFlow<(@Composable () -> Unit)?> = _content

    fun setContent(content: @Composable () -> Unit) {
        _content.value = content
    }
}