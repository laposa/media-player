package ie.laposa.domain.mediaSource.model

sealed class MediaSourceFileBase {
    abstract val name: String
}

class MediaSourceShare(
    override val name: String,
) : MediaSourceFileBase()

class MediaSourceGoUp(
    override val name: String = "up"
) : MediaSourceFileBase()

abstract class MediaSourceFileWithPath : MediaSourceFileBase() {
    abstract override val name: String
    abstract val path: String
}

class MediaSourceFile(
    override val name: String,
    override val path: String,
    val thumbnailUrl: String? = null,
) : MediaSourceFileWithPath()

class MediaSourceDirectory(override val name: String, override val path: String) :
    MediaSourceFileWithPath()

