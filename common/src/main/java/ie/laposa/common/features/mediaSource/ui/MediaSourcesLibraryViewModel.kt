package ie.laposa.common.features.mediaSource.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import ie.laposa.common.features.common.ViewModelBase
import ie.laposa.common.features.common.ui.SavedStateHandleViewModel
import ie.laposa.domain.mediaSource.MediaSourceService
import ie.laposa.domain.mediaSource.model.MediaSource
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.mediaSource.model.MediaSourceType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MediaSourcesLibraryViewModel @Inject constructor(
    private val mediaSourceService: MediaSourceService,
    private val savedStateHandleViewModel: SavedStateHandleViewModel
) : ViewModelBase() {
    val mediaSources = mediaSourceService.mediaSources

    private val _isLoginViewModelVisible = MutableStateFlow(false)
    val isLoginDialogVisible: StateFlow<Boolean> = _isLoginViewModelVisible

    private val _isFilesLibraryVisible = MutableStateFlow(false)
    val isFilesLibraryVisible: StateFlow<Boolean> = _isFilesLibraryVisible

    private var _selectedMediaSource: MediaSource? = null

    val files: StateFlow<List<MediaSourceFile>?> = mediaSourceService.fileList
    val sambaShares: StateFlow<Map<MediaSource, List<String>>?> =
        mediaSourceService.sambaShares

    val sambaSharesForSelectedMediaSource: Flow<List<String>?> = sambaShares.map {
        it?.get(_selectedMediaSource)
    }

    private val _selectedSambaShare = MutableStateFlow<String?>(null)
    val selectedSambaShare: StateFlow<String?> = _selectedSambaShare

    private val _loginDialogError = MutableStateFlow<String?>(null)
    val loginDialogError: StateFlow<String?> = _loginDialogError

    fun fetchMediaSources() {
        launch(false) {
            mediaSourceService.fetchMediaSources()
        }
    }

    fun onMediaSourceSelected(mediaSource: MediaSource) {
        _selectedMediaSource = mediaSource
        showLoginDialog()
    }

    fun onSambaShareSelected(shareName: String, onSuccess: () -> Unit) {
        launch {
            mediaSourceService.openShare(shareName)
            _selectedSambaShare.value = shareName
            _isFilesLibraryVisible.value = true
            onSuccess()
        }
    }

    private fun showLoginDialog() {
        _loginDialogError.value = null
        _isLoginViewModelVisible.value = true
    }

    fun hideLoginDialog() {
        _isLoginViewModelVisible.value = false
    }

    fun onLoginSubmit(userName: String?, password: String?) {
        _loginDialogError.value = null

        launch(handleError = false) {
            try {
                when (_selectedMediaSource?.type) {
                    is MediaSourceType.ZeroConf.SMB -> {
                        connectToMediaSource(_selectedMediaSource!!, userName, password)
                    }

                    else -> connectToMediaSource(_selectedMediaSource!!)
                }
            } catch (e: Exception) {
                _loginDialogError.value = e.message
            }

            if (_loginDialogError.value == null) {
                _isLoginViewModelVisible.value = false
            }
        }
    }

    suspend fun onFileSelected(sourceFile: MediaSourceFile) {
        _isFilesLibraryVisible.value = false
        mediaSourceService.getFile(sourceFile.fileName)?.let {
            savedStateHandleViewModel.setSelectedInputStreamDataSourceFileName(sourceFile.fileName)
        }
    }

    private suspend fun connectToMediaSource(
        mediaSource: MediaSource,
        userName: String? = null,
        password: String? = null
    ) {
        when (mediaSource.type) {
            is MediaSourceType.ZeroConf.SMB -> {
                mediaSourceService.connectToMediaSource(mediaSource, userName, password)
            }

            else -> {
                mediaSourceService.connectToMediaSource(mediaSource)
                _isFilesLibraryVisible.value = true
            }
        }
    }
}