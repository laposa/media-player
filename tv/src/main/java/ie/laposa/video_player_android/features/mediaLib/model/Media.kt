package ie.laposa.video_player_android.features.mediaLib.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    val title: String,
    val url: String,
    val thumbnailUrl: String,
) : Parcelable
