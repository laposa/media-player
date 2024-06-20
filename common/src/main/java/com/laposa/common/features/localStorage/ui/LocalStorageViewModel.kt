package com.laposa.common.features.localStorage.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import com.laposa.common.features.common.ViewModelBase
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LocalStorageViewModel @Inject constructor() : ViewModelBase() {

    private val _files = MutableStateFlow<List<MediaSourceFileBase>>(emptyList())
    val files: StateFlow<List<MediaSourceFileBase>> = _files

    private val _path = MutableStateFlow<String>("")
    val path: StateFlow<String> = _path

    fun setFiles(newFiles: List<MediaSourceFileBase>) {
        _files.value = newFiles
    }

    fun setPath(newPath: String) {
        _path.value = newPath
    }
}