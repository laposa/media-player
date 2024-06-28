package com.laposa.domain.di

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.laposa.domain.mediaSource.MediaSourceService
import com.laposa.domain.mediaSource.model.nfs.NfsMediaProvider
import com.laposa.domain.mediaSource.model.samba.SambaMediaProvider
import com.laposa.domain.networkProtocols.nfs.NfsService
import com.laposa.domain.networkProtocols.sftp.SFTPService
import com.laposa.domain.networkProtocols.smb.SmbService
import com.laposa.domain.recents.RecentMediaService
import com.laposa.domain.rememberLogin.RememberLoginService
import com.laposa.domain.savedState.SavedStateService
import com.laposa.domain.zeroConf.ZeroConfService
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
    fun provideSFTPService(): SFTPService {
        return SFTPService()
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