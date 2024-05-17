package ie.laposa.common.features.common.di

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.skyost.bonsoir.discovery.BonsoirServiceDiscovery
import ie.laposa.common.features.common.ui.SavedStateHandleViewModel
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {
    @Singleton
    @Provides
    fun provideSavedStateHandleViewModel(): SavedStateHandleViewModel {
        val savedStateHandle = SavedStateHandle()
        return SavedStateHandleViewModel(savedStateHandle)
    }
}