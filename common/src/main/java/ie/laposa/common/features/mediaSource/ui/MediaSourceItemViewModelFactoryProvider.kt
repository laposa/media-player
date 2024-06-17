package ie.laposa.common.features.mediaSource.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf


val LocalMediaSourceItemViewModelFactory = staticCompositionLocalOf<MediaSourceItemViewModelFactory> {
    error("No ViewModelFactory provided")
}
@Composable
fun ProvideMediaSourceItemViewModelFactory(
    myViewModelFactory: MediaSourceItemViewModelFactory,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalMediaSourceItemViewModelFactory provides myViewModelFactory) {
        content()
    }
}