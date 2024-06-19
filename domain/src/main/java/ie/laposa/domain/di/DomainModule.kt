package ie.laposa.domain.di

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ie.laposa.domain.mediaSource.MediaSourceService
import ie.laposa.domain.mediaSource.model.nfs.NfsMediaProvider
import ie.laposa.domain.mediaSource.model.samba.SambaMediaProvider
import ie.laposa.domain.networkProtocols.nfs.NfsService
import ie.laposa.domain.networkProtocols.smb.SmbService
import ie.laposa.domain.recents.RecentMediaService
import ie.laposa.domain.rememberLogin.RememberLoginService
import ie.laposa.domain.savedState.SavedStateService
import ie.laposa.domain.zeroConf.ZeroConfService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Singleton
    @Provides
    fun provideSavedStateHandle(): SavedStateHandle {
        return SavedStateHandle()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext app: Context
    ): SharedPreferences {
        return app.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideSavedStateService(
        savedStateHandle: SavedStateHandle,
        sharedPreferences: SharedPreferences,
    ): SavedStateService {
        return SavedStateService(savedStateHandle, sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideRememberLoginService(
        savedStateService: SavedStateService
    ): RememberLoginService {
        return RememberLoginService(savedStateService)
    }

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
    fun provideNfsService(): NfsService {
        return NfsService()
    }

    @Singleton
    @Provides
    fun provideSambaMediaProvider(
        smbService: SmbService,
        rememberLoginService: RememberLoginService
    ): SambaMediaProvider {
        return SambaMediaProvider(smbService, rememberLoginService)
    }

    @Singleton
    @Provides
    fun provideNfsMediaProvider(nfsService: NfsService): NfsMediaProvider {
        return NfsMediaProvider(nfsService)
    }

    @Singleton
    @Provides
    fun provideMediaSourceService(
        zeroConfService: ZeroConfService,
        smMediaProvider: SambaMediaProvider,
        nfsMediaProvider: NfsMediaProvider,
    ): MediaSourceService {
        return MediaSourceService(zeroConfService, smMediaProvider, nfsMediaProvider)
    }

    @Singleton
    @Provides
    fun provideRecentMediaService(
        savedStateService: SavedStateService,
    ): RecentMediaService {
        return RecentMediaService(savedStateService)
    }
}