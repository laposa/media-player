package com.laposa.common.features.mediaLib.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    override val fileName: String,
    override val filePath: String,
    override val isFolder: Boolean = false,
    val thumbnailUrl: String? = null,
) : FileSystemItem(), Parcelable
