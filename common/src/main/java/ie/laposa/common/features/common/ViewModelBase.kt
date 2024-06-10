package ie.laposa.common.features.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class ViewModelBase : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    fun launch(
        handleLoading: Boolean = true,
        handleError: Boolean = true,
        onError: (Throwable) -> Unit = {},
        block: suspend () -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (handleLoading) _isLoading.value = true
                block()
            } catch (e: Throwable) {
                e.printStackTrace()
                if (handleError) onError(e) else throw e
            } finally {
                if (handleLoading) _isLoading.value = false
            }
        }
    }
}