package com.laposa.common.features.common.di

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.laposa.common.features.mediaSource.ui.MediaSourceItemViewModelFactory
import com.laposa.domain.mediaSource.MediaSourceService
import com.laposa.domain.savedState.SavedStateService

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