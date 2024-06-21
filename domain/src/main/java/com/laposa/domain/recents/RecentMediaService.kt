package com.laposa.domain.recents

import com.laposa.domain.savedState.SavedStateService
import com.laposa.domain.utils.MyQueue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.transform

class RecentMediaService(
    private val savedStateService: SavedStateService
) {
    private var _recentMediaQueue: MyQueue<RecentMedia> = MyQueue.fromList(
        getSavedRecentMedia()?.items ?: emptyList(),
        MAXIMUM_COUNT_OF_RECENT_MEDIA
    )

    private val _recentMediaCollection: MutableStateFlow<List<RecentMedia>> = MutableStateFlow(
        _recentMediaQueue.getAll()
    )

    val recentMediaCollection: StateFlow<List<RecentMedia>> = _recentMediaCollection

    fun addRecentMedia(recent: RecentMedia) {
        _recentMediaQueue.add(recent)
        _recentMediaCollection.value = _recentMediaQueue.getAll()
        savedStateService.addRecentMedia(RecentMediaCollection(_recentMediaCollection.value))
    }

    private fun getSavedRecentMedia(): RecentMediaCollection? {
        return savedStateService.getRecentMedia()
    }

    companion object {
        private const val MAXIMUM_COUNT_OF_RECENT_MEDIA = 5
    }
}