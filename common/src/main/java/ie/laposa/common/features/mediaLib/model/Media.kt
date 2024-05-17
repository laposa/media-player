package ie.laposa.common.features.mediaLib.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    val title: String,
    val url: String,
    val thumbnailUrl: String,
) : Parcelable
