package ie.laposa.common.features.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class ViewModelBase : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    fun launch(handleLoading: Boolean = true, block: suspend () -> Unit) {
        viewModelScope.launch {
            if (handleLoading) _isLoading.value = true
            block()
            if (handleLoading) _isLoading.value = false
        }
    }
}