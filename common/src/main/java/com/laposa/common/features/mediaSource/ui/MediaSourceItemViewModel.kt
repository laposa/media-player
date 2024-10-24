package com.laposa.common.features.mediaSource.ui

import androidx.lifecycle.SavedStateHandle
import com.laposa.common.features.common.ViewModelBase
import com.laposa.domain.mediaSource.MediaSourceService
import com.laposa.domain.mediaSource.MediaSourceServiceFactory
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceDirectory
import com.laposa.domain.mediaSource.model.MediaSourceFile
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.mediaSource.model.MediaSourceType
import com.laposa.domain.savedState.SavedStateService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MediaSourceItemViewModel(
    private val selectedMediaSource: MediaSource,
    mediaSourceServiceFactory: MediaSourceServiceFactory,
    private val savedStateService: SavedStateService
) : ViewModelBase() {

    private val mediaSourceService: MediaSourceService =
        mediaSourceServiceFactory.getOrCreate(selectedMediaSource.type)

    private val _isLoginViewModelVisible = MutableStateFlow(false)
    val isLoginDialogVisible: StateFlow<Boolean> = _isLoginViewModelVisible

    private val _files = MutableStateFlow<List<MediaSourceFileBase>>(emptyList())
    val files: StateFlow<List<MediaSourceFileBase>> = _files

    val path: StateFlow<String> = mediaSourceService.currentPath

    private val _loginDialogError = MutableStateFlow<String?>(null)
    val loginDialogError: StateFlow<String?> = _loginDialogError

    private val _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    fun openDirectory(directory: MediaSourceDirectory) {
        launch {
            val files =
                mediaSourceService.getContentOfDirectoryAthPath(directory.name)
            _files.value = files.second
        }
    }

    fun goUp() {
        launch {
            val files = mediaSourceService.goBack()
            _files.value = files.second
        }
    }

    suspend fun onSambaShareSelected(shareName: String) {
        val files = mediaSourceService.openShare(shareName)
        _files.value = files
    }

    fun showLoginDialog() {
        _loginDialogError.value = null
        _isLoginViewModelVisible.value = true
    }

    fun hideLoginDialog() {
        _isLoginViewModelVisible.value = false
    }

    suspend fun connectToMediaSource(): Boolean {
        if (!mediaSourceService.tryToConnectToMediaSource(selectedMediaSource)) {
            return false
        } else {
            onConnectionSuccess()
            return true
        }
    }

    fun onLoginSubmit(userName: String?, password: String?, remember: Boolean) {
        _loginDialogError.value = null

        launch(handleError = false) {
            try {
                when (selectedMediaSource.type) {
                    MediaSourceType.SMB -> {
                        connectToMediaSourceInternal(
                            selectedMediaSource, userName, password, remember
                        )
                    }

                    else -> connectToMediaSourceInternal(selectedMediaSource, null, null, remember)
                }
            } catch (e: Exception) {
                _loginDialogError.value = e.message
            }

            if (_loginDialogError.value == null) {
                _isLoginViewModelVisible.value = false
            }
        }
    }

    suspend fun addAndConnectMediaSource(mediaSource: MediaSource): String? {
        println("Adding and connecting media source: $mediaSource")
        try {
            mediaSourceService.addAndConnectManualMediaSource(mediaSource)
            _files.value = mediaSourceService.getContentOfDirectoryAthPath("").second
            _isConnected.value = true
        } catch (e: Exception) {
            return e.message
        }
        return null
    }

    fun onFileSelected(
        sourceFile: MediaSourceFile, playFile: (MediaSourceFile) -> Unit
    ) {
        playFile(sourceFile)
    }


    private suspend fun connectToMediaSourceInternal(
        mediaSource: MediaSource,
        userName: String? = null,
        password: String? = null,
        remember: Boolean,
    ) {
        mediaSourceService.connectToMediaSource(
            mediaSource.copy(
                username = userName,
                password = password
            ), remember
        )
        onConnectionSuccess()
    }

    private suspend fun onConnectionSuccess() {
        _files.value = mediaSourceService.getContentOfDirectoryAthPath("").second
        _isConnected.value = true
    }
}

class MediaSourceItemViewModelFactory(
    private val savedStateHandle: SavedStateHandle,
    private val mediaSourceServiceFactory: MediaSourceServiceFactory,
    private val savedStateService: SavedStateService,
) {
    private var instances: MutableMap<String, MediaSourceItemViewModel> = mutableMapOf()

    init {
        instances = savedStateHandle[INSTANCES_MAP_KEY] ?: mutableMapOf()
    }

    fun create(mediaSource: MediaSource): MediaSourceItemViewModel {
        val result = instances[getInstanceKey(mediaSource)] ?: createInstance(mediaSource)
        return result
    }

    private fun createInstance(mediaSource: MediaSource): MediaSourceItemViewModel {
        instances[getInstanceKey(mediaSource)] =
            MediaSourceItemViewModel(mediaSource, mediaSourceServiceFactory, savedStateService)
        savedStateHandle[INSTANCES_MAP_KEY] = instances
        return instances[getInstanceKey(mediaSource)]!!
    }

    private fun getInstanceKey(mediaSource: MediaSource): String {
        return mediaSource.key
    }

    companion object {
        const val INSTANCES_MAP_KEY = "MediaSourceItemViewModelFactory"
    }
}