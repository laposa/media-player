package ie.laposa.common.features.common.di

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ie.laposa.common.features.common.ui.SavedStateHandleViewModel
import ie.laposa.common.features.mediaSource.ui.MediaSourceItemViewModelFactory
import ie.laposa.domain.mediaSource.MediaSourceService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Singleton
    @Provides
    fun provideSavedStateHandle(): SavedStateHandle {
        return SavedStateHandle()
    }

    @Singleton
    @Provides
    fun provideSavedStateHandleViewModel(savedStateHandle: SavedStateHandle): SavedStateHandleViewModel {
        return SavedStateHandleViewModel(savedStateHandle)
    }

    @Provides
    fun provideMediaSourceItemViewModelFactory(
        savedStateHandle: SavedStateHandle,
        mediaSourceService: MediaSourceService,
        savedStateHandleViewModel: SavedStateHandleViewModel
    ): MediaSourceItemViewModelFactory = MediaSourceItemViewModelFactory(
        savedStateHandle, mediaSourceService, savedStateHandleViewModel
    )
}