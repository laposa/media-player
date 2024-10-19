package com.laposa.common.features.home.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackbarHost = staticCompositionLocalOf<SnackbarHostState> {
    error("No SnackbarHost provided")
}

@Composable
fun ProvideHomeSnackbarHost(
    snackbarHostState: SnackbarHostState,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalSnackbarHost provides snackbarHostState) {
        content()
    }
}