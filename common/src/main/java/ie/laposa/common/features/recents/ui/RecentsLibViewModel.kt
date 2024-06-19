package ie.laposa.common.features.recents.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import ie.laposa.common.features.common.ViewModelBase
import ie.laposa.domain.recents.RecentMediaService
import javax.inject.Inject

@HiltViewModel
class RecentsLibViewModel @Inject constructor(
    recentMediaService: RecentMediaService
) : ViewModelBase() {
    val recentMedia = recentMediaService.recentMediaCollection
}