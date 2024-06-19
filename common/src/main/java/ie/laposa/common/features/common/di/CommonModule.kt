package ie.laposa.common.features.common.di

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ie.laposa.common.features.mediaSource.ui.MediaSourceItemViewModelFactory
import ie.laposa.domain.mediaSource.MediaSourceService
import ie.laposa.domain.savedState.SavedStateService

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {
    @Provides
    fun provideMediaSourceItemViewModelFactory(
        savedStateHandle: SavedStateHandle,
        mediaSourceService: MediaSourceService,
        savedStateService: SavedStateService,
    ): MediaSourceItemViewModelFactory = MediaSourceItemViewModelFactory(
        savedStateHandle, mediaSourceService, savedStateService
    )
}