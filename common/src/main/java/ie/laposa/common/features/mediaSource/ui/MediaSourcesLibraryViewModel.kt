package ie.laposa.common.features.mediaSource.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import ie.laposa.common.features.common.ViewModelBase
import ie.laposa.common.features.common.ui.SavedStateHandleViewModel
import ie.laposa.domain.mediaSource.MediaSourceService
import ie.laposa.domain.mediaSource.model.MediaSource
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.mediaSource.model.MediaSourceType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _isSambaSharesListVisible = MutableStateFlow(false)
    val isSambaSharesListVisible: StateFlow<Boolean> = _isSambaSharesListVisible

    private var _selectedMediaSource: MediaSource? = null

    val files: StateFlow<List<MediaSourceFile>?> = mediaSourceService.fileList
    val sambaShares: StateFlow<List<String>?> = mediaSourceService.sambaShares
    private val _selectedSambaShare = MutableStateFlow<String?>(null)
    val selectedSambaShare: StateFlow<String?> = _selectedSambaShare

    fun fetchMediaSources() {
        launch(false) {
            mediaSourceService.fetchMediaSources()
        }
    }

    fun onMediaSourceSelected(mediaSource: MediaSource) {
        when (mediaSource.type) {
            is MediaSourceType.ZeroConf.SMB -> {
                _selectedMediaSource = mediaSource
                _isLoginViewModelVisible.value = true
            }

            else -> {
                connectToMediaSource(mediaSource)
            }
        }
    }

    fun onSambaShareSelected(shareName: String) {
        launch {
            mediaSourceService.openShare(shareName)
            _selectedSambaShare.value = shareName
            _isSambaSharesListVisible.value = false
            _isFilesLibraryVisible.value = true
        }
    }

    fun onLoginSubmit(userName: String, password: String) {
        _isLoginViewModelVisible.value = false

        when (_selectedMediaSource?.type) {
            is MediaSourceType.ZeroConf.SMB -> {
                connectToMediaSource(_selectedMediaSource!!, userName, password)
            }

            else -> connectToMediaSource(_selectedMediaSource!!)
        }
    }

    suspend fun onFileSelected(sourceFile: MediaSourceFile) {
        _isFilesLibraryVisible.value = false
        mediaSourceService.getFile(sourceFile.fileName)?.let {
            savedStateHandleViewModel.setSelectedInputStreamDataSourceFileName(sourceFile.fileName)
        }
    }

    private fun connectToMediaSource(
        mediaSource: MediaSource,
        userName: String? = null,
        password: String? = null
    ) {
        launch {
            when (mediaSource.type) {
                is MediaSourceType.ZeroConf.SMB -> {
                    mediaSourceService.connectToMediaSource(mediaSource, userName, password)
                    _isSambaSharesListVisible.value = true
                }

                else -> {
                    mediaSourceService.connectToMediaSource(mediaSource)
                    _isFilesLibraryVisible.value = true
                }
            }
        }
    }
}