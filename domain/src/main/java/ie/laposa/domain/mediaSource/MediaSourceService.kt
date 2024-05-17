package ie.laposa.domain.mediaSource

import ie.laposa.domain.mediaSource.model.MediaSource
import ie.laposa.domain.mediaSource.model.MediaSourceType
import ie.laposa.domain.zeroConf.ZeroConfService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class MediaSourceService @Inject constructor(
    private val zeroConf: ZeroConfService
) {
    private val _mediaSources = MutableStateFlow(emptyList<MediaSource>())
    val mediaSources : StateFlow<List<MediaSource>> = _mediaSources
    suspend fun fetchMediaSources() {
        zeroConf.startDiscovery()

        zeroConf.discoveredServices.collectLatest { services ->
            _mediaSources.value = services.map {
                MediaSource.fromNSD(it)
            }
        }
    }
}