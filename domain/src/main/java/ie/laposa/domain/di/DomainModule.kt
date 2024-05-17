package ie.laposa.domain.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ie.laposa.domain.zeroConf.ZeroConfService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Singleton
    @Provides
    fun provideZeroConfService(@ApplicationContext appContext: Context): ZeroConfService {
        return ZeroConfService(appContext)
    }
}