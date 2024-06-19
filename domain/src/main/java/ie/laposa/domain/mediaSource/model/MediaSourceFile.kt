package ie.laposa.domain.mediaSource.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class MediaSourceFileBase {
    abstract val name: String
}

class MediaSourceShare(
    override val name: String,
) : MediaSourceFileBase()

class MediaSourceGoUp(
    override val name: String = "Go Back"
) : MediaSourceFileBase()

abstract class MediaSourceFileWithPath : MediaSourceFileBase() {
    abstract override val name: String
    abstract val path: String
}

@Parcelize
data class MediaSourceFile(
    override val name: String,
    override val path: String,
    val thumbnailUrl: String? = null,
    val type: MediaSourceType? = null,
) : MediaSourceFileWithPath(), Parcelable

class MediaSourceDirectory(override val name: String, override val path: String) :
    MediaSourceFileWithPath()

