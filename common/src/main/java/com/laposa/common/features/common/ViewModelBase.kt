package com.laposa.common.features.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class ViewModelBase : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val className = this.javaClass.name

    fun launch(
        handleLoading: Boolean = true,
        handleError: Boolean = true,
        onError: (Throwable) -> Unit = {},
        block: suspend () -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (handleLoading) _isLoading.value = true
                println("[$className] Is loading 1: ${_isLoading.value}")
                block()
            } catch (e: Throwable) {
                e.printStackTrace()
                if (handleError) onError(e) else throw e
            } finally {
                if (handleLoading) _isLoading.value = false
                println("[$className] Is loading 3: ${_isLoading.value}")
            }
        }
    }
}