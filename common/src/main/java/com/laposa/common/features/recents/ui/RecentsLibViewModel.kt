package com.laposa.common.features.recents.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import com.laposa.common.features.common.ViewModelBase
import com.laposa.domain.recents.RecentMediaService
import javax.inject.Inject

@HiltViewModel
class RecentsLibViewModel @Inject constructor(
    recentMediaService: RecentMediaService
) : ViewModelBase() {
    val recentMedia = recentMediaService.recentMediaCollection
}