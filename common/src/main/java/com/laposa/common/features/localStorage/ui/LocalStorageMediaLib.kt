package com.laposa.common.features.localStorage.ui

import android.os.Environment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.fastMap
import com.laposa.common.features.common.screens.EmptyListScreen
import com.laposa.common.features.mediaLib.ui.MediaLibrary
import com.laposa.domain.mediaSource.model.MediaSourceDirectory
import com.laposa.domain.mediaSource.model.MediaSourceFile
import com.laposa.domain.mediaSource.model.MediaSourceFileBase
import com.laposa.domain.mediaSource.model.MediaSourceShare
import com.laposa.domain.mediaSource.model.MediaSourceType
import java.io.File

@Composable
fun LocalStorageMediaLib(
    playFile: (MediaSourceFile) -> Unit,
) {
    val rootPath = Environment.getExternalStorageDirectory().path
    var currentPath by remember { mutableStateOf(rootPath) }

    val currentFiles = remember(currentPath) {
        getFilesAndFolders(currentPath)
    }

    fun onFileSelect(file: MediaSourceFile) {
        playFile(file)
    }

    fun onDirectorySelect(directory: MediaSourceDirectory) {
        currentPath = currentFiles[directory]?.path ?: currentPath
    }

    fun onShareSelect(share: MediaSourceShare) {}

    fun onGoUp() {
        currentPath = File(currentPath).parent ?: rootPath
    }

    if (currentFiles.keys.isNotEmpty()) {
        MediaLibrary(
            title = "Local Storage",
            files = currentFiles.keys.toList(),
            path = currentPath,
            defaultPathDepthLevel = rootPath.split("/").size + 1,
            onMediaFileSelect = ::onFileSelect,
            onMediaDirectorySelect = ::onDirectorySelect,
            onMediaShareSelect = ::onShareSelect,
            onGoUp = ::onGoUp
        )
    } else {
        EmptyListScreen()
    }
}

fun getFilesAndFolders(path: String): Map<MediaSourceFileBase, File> {
    val directory = File(path)

    return (directory.listFiles()?.toList() ?: emptyList()).fastMap {
        if (it.isDirectory) {
            MediaSourceDirectory(
                name = it.name,
                path = it.absolutePath,
            ) to it
        } else {
            MediaSourceFile(
                name = it.name,
                path = it.absolutePath,
                type = MediaSourceType.LocalFile
            ) to it
        }
    }.toMap()
}