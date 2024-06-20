package com.laposa.common.features.mediaLib.model

sealed class FileSystemItem {
    abstract val fileName: String
    abstract val filePath: String
    abstract val isFolder: Boolean
}
