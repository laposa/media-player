package ie.laposa.common.features.mediaSource.ui

import androidx.lifecycle.SavedStateHandle
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import ie.laposa.common.features.common.ViewModelBase
import ie.laposa.common.features.common.ViewModelFactory
import ie.laposa.common.features.common.ui.SavedStateHandleViewModel
import ie.laposa.domain.mediaSource.MediaSourceService
import ie.laposa.domain.mediaSource.model.MediaSource
import ie.laposa.domain.mediaSource.model.MediaSourceDirectory
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.mediaSource.model.MediaSourceFileBase
import ie.laposa.domain.mediaSource.model.MediaSourceType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MediaSourceItemViewModel(
    private val selectedMediaSource: MediaSource,
    private val mediaSourceService: MediaSourceService,
    private val savedStateHandleViewModel: SavedStateHandleViewModel
) : ViewModelBase() {
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
                mediaSourceService.getContentOfDirectoryAthPath("${directory.path}${directory.name}")
            _files.value = files.second
        }
    }

    fun goUp() {
        launch {
            val files =
                mediaSourceService.goBack()
            _files.value = files.second
        }
    }

    suspend fun onSambaShareSelected(shareName: String) {
        val files = mediaSourceService.openShare(shareName)
        _files.value = files
    }

    private fun showLoginDialog() {
        _loginDialogError.value = null
        _isLoginViewModelVisible.value = true
    }

    fun hideLoginDialog() {
        _isLoginViewModelVisible.value = false
    }

    fun connectToMediaSource() {
        showLoginDialog()
    }

    fun onLoginSubmit(userName: String?, password: String?) {
        _loginDialogError.value = null

        launch(handleError = false) {
            try {
                when (selectedMediaSource.type) {
                    is MediaSourceType.ZeroConf.SMB -> {
                        connectToMediaSourceInternal(selectedMediaSource, userName, password)
                    }

                    else -> connectToMediaSourceInternal(selectedMediaSource)
                }
            } catch (e: Exception) {
                _loginDialogError.value = e.message
            }

            if (_loginDialogError.value == null) {
                _isLoginViewModelVisible.value = false
            }
        }
    }

    suspend fun onFileSelected(
        sourceFile: MediaSourceFile,
        playFile: (MediaSourceFile?, String?) -> Unit
    ) {
        mediaSourceService.getFile(sourceFile.name)?.let {
            savedStateHandleViewModel.setSelectedInputStreamDataSourceFileName(sourceFile.name)
            playFile(null, sourceFile.name)
        }
    }


    private suspend fun connectToMediaSourceInternal(
        mediaSource: MediaSource,
        userName: String? = null,
        password: String? = null
    ) {
        when (mediaSource.type) {
            is MediaSourceType.ZeroConf.SMB -> {
                mediaSourceService.connectToMediaSource(mediaSource, userName, password)
                _files.value = mediaSourceService.getContentOfDirectoryAthPath("").second
            }

            else -> {
                mediaSourceService.connectToMediaSource(mediaSource)
            }
        }

        _isConnected.value = true
    }
}

class MediaSourceItemViewModelFactory(
    private val savedStateHandle: SavedStateHandle,
    private val mediaSourceService: MediaSourceService,
    private val savedStateHandleViewModel: SavedStateHandleViewModel
) {
    private var instances: MutableMap<String, MediaSourceItemViewModel> = mutableMapOf()

    init {
        instances = savedStateHandle[INSTANCES_MAP_KEY] ?: mutableMapOf()
    }

    fun create(mediaSource: MediaSource): MediaSourceItemViewModel {
        val result = instances[getInstanceKey(mediaSource)] ?: createInstance(mediaSource)
        println(instances.keys)
        return result
    }

    private fun createInstance(mediaSource: MediaSource): MediaSourceItemViewModel {
        instances[getInstanceKey(mediaSource)] =
            MediaSourceItemViewModel(mediaSource, mediaSourceService, savedStateHandleViewModel)
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