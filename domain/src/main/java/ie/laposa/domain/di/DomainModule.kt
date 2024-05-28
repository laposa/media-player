package ie.laposa.domain.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ie.laposa.domain.mediaSource.MediaSourceService
import ie.laposa.domain.mediaSource.model.samba.SambaMediaProvider
import ie.laposa.domain.networkProtocols.smb.SmbService
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

    @Singleton
    @Provides
    fun provideSmbService(): SmbService {
        return SmbService()
    }
    
    @Singleton
    @Provides
    fun provideSambaMediaProvider(smbService: SmbService): SambaMediaProvider {
        return SambaMediaProvider(smbService)
    }

    @Singleton
    @Provides
    fun provideMediaSourceService(
        zeroConfService: ZeroConfService,
        smMediaProvider: SambaMediaProvider
    ): MediaSourceService {
        return MediaSourceService(zeroConfService, smMediaProvider)
    }
}