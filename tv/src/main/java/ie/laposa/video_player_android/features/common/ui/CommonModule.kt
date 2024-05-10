package ie.laposa.video_player_android.features.common.ui

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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