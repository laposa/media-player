package ie.laposa.domain.recents

import ie.laposa.domain.savedState.SavedStateService
import ie.laposa.domain.utils.MyQueue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.transform

class RecentMediaService(
    private val savedStateService: SavedStateService
) {
    private val _recentMediaCollection = MutableStateFlow(
        MyQueue.fromList(
            (savedStateService.getRecentMedia() ?: RecentMediaCollection.empty()).items,
            MAXIMUM_COUNT_OF_RECENT_MEDIA
        )
    )

    val recentMediaCollection: Flow<List<RecentMedia>> =
        _recentMediaCollection.transform { it.getAll() }

    fun addRecentMedia(recent: RecentMedia) {
        _recentMediaCollection.value.add(recent)
        savedStateService.addRecentMedia(RecentMediaCollection(_recentMediaCollection.value.getAll()))
        println("Recent media: ${_recentMediaCollection.value.getAll()}")
    }

    private fun getSavedRecentMedia(): RecentMediaCollection? {
        return savedStateService.getRecentMedia()
    }

    companion object {
        private const val MAXIMUM_COUNT_OF_RECENT_MEDIA = 5
    }
}