package ie.laposa.common.features.menu.ui.menuSections

import dagger.hilt.android.lifecycle.HiltViewModel
import ie.laposa.common.features.common.ViewModelBase
import ie.laposa.common.features.common.ui.SavedStateHandleViewModel
import ie.laposa.domain.mediaSource.MediaSourceService
import javax.inject.Inject

@HiltViewModel
class ZeroConfMenuSectionViewModel @Inject constructor(
    private val mediaSourceService: MediaSourceService,
    private val savedStateHandleViewModel: SavedStateHandleViewModel
) : ViewModelBase() {
    val mediaSources = mediaSourceService.mediaSources
    fun fetchMediaSources() {
        launch(false) {
            mediaSourceService.fetchMediaSources()
        }
    }


}