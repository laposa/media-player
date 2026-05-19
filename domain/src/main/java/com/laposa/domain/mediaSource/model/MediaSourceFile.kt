package com.laposa.domain.mediaSource.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

sealed class MediaSourceFileBase {
    abstract val name: String
}

class MediaSourceShare(
    override val name: String,
) : MediaSourceFileBase()

class MediaSourceGoUp(
    override val name: String = ""
) : MediaSourceFileBase()

/**
 * Injected into the share list when automatic share enumeration fails (SRVSVC not accessible).
 * The UI should handle a click on this item by prompting the user to type a share name.
 */
class MediaSourceEnterShareName : MediaSourceFileBase() {
    override val name: String = "Enter share name"
}

abstract class MediaSourceFileWithPath : MediaSourceFileBase() {
    abstract override val name: String
    abstract val path: String
}

@Parcelize
data class MediaSourceFile(
    override val name: String,
    override val path: String,
    val type: MediaSourceType,
    val thumbnailUrl: String? = null,
) : MediaSourceFileWithPath(), Parcelable {
    @IgnoredOnParcel
    val fullPath: String = when {
        path.isEmpty() -> name
        path.last() == '/' && name.first() != '/' -> "$path$name"
        path.last() != '/' && name.first() == '/' -> "$path$name"
        type == MediaSourceType.LOCAL_FILE -> path
        else -> "$path/$name"
    }
}

class MediaSourceDirectory(override val name: String, override val path: String) :
    MediaSourceFileWithPath()

