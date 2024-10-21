package com.laposa.common.features.common.composables

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color

@Composable
fun FocusableForm(
    scrollState: ScrollState,
    components: List<@Composable (focusRequester: SafeFocusRequester, focusNext: () -> Unit) -> Unit>,
) {
    // Create a list of FocusRequester
    val focusRequesters = remember { List(components.size) { SafeFocusRequester() } }

    // Function to focus the next component
    fun focusNext(currentIndex: Int) {
        var nextIndex = currentIndex + 1

        while (nextIndex < focusRequesters.size && !focusRequesters[nextIndex].isInitialized) {
            nextIndex++
        }

        if (nextIndex < focusRequesters.size) {
            focusRequesters[nextIndex].requestFocus()
        }
    }

    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .verticalScroll(scrollState)
    ) {
        components.forEachIndexed { index, component ->
            component(focusRequesters[index]) { focusNext(index) }
        }
    }
}

class SafeFocusRequester {
    private var _isInitialized = false
    val isInitialized: Boolean get() = _isInitialized

    private val instance: FocusRequester = FocusRequester()
    val focusRequester: FocusRequester
        get() {
            _isInitialized = true
            return instance
        }

    fun requestFocus() {
        focusRequester.requestFocus()
    }

    fun reset() {
        _isInitialized = false
    }
}