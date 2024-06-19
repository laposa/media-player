package ie.laposa.domain.savedState

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import ie.laposa.domain.mediaSource.model.MediaSourceFile
import ie.laposa.domain.recents.RecentMedia
import ie.laposa.domain.recents.RecentMediaCollection
import ie.laposa.domain.rememberLogin.RememberLogin

class SavedStateService(
    private val savedStateHandle: SavedStateHandle,
    private val sharedPreferences: SharedPreferences,
) {
    fun setSelectedMedia(media: MediaSourceFile) {
        savedStateHandle[KEY_SELECTED_MEDIA] = media
    }

    fun getSelectedMedia(): LiveData<MediaSourceFile?> {
        return savedStateHandle.getLiveData(KEY_SELECTED_MEDIA)
    }

    fun clearSelectedMedia() {
        savedStateHandle[KEY_SELECTED_MEDIA] = null
    }

    fun setSelectedInputStreamDataSourceFileName(fileName: String) {
        savedStateHandle[KEY_SELECTED_INPUT_STREAM_DATA_SOURCE] = fileName
    }

    fun getSelectedInputStreamDataSourceFileName(): LiveData<String?> {
        return savedStateHandle.getLiveData(KEY_SELECTED_INPUT_STREAM_DATA_SOURCE)
    }

    fun clearSelectedInputStreamDataSourceFileName() {
        savedStateHandle[KEY_SELECTED_INPUT_STREAM_DATA_SOURCE] = null
    }

    fun setRememberLogin(key: String, rememberLogin: RememberLogin) {
        sharedPreferences.edit(commit = true) {
            putString(key, rememberLogin.toJSON())
        }
    }

    fun getRememberLogin(key: String): RememberLogin? {
        return sharedPreferences.getString(key, null)?.let {
            RememberLogin.fromJSON(it)
        }
    }

    fun clearRememberLogin(key: String) {
        savedStateHandle[key] = null
    }

    fun addRecentMedia(recentMediaCollection: RecentMediaCollection) {
        sharedPreferences.edit(true) {
            putString(KEY_RECENT_MEDIA, recentMediaCollection.toJSON())
        }
    }

    fun getRecentMedia(): RecentMediaCollection? {
        return sharedPreferences.getString(KEY_RECENT_MEDIA, null)?.let {
            RecentMediaCollection.fromJSON(it)
        }
    }

    fun clearRecentMedia() {
        sharedPreferences.edit(true) {
            remove(KEY_RECENT_MEDIA)
        }
    }

    companion object {
        private const val KEY_SELECTED_MEDIA = "selectedMedia"
        private const val KEY_SELECTED_INPUT_STREAM_DATA_SOURCE = "selectedInputStreamDataSource"
        private const val KEY_RECENT_MEDIA = "recentMedia"
    }
}