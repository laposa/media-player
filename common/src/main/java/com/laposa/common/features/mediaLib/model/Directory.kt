package com.laposa.common.features.mediaLib.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Directory(
    override val fileName: String,
    override val filePath: String,
    override val isFolder: Boolean = true
) : FileSystemItem(), Parcelable
