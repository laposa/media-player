package ie.laposa.domain.zeroConf

import android.content.Context
import android.content.Context.NSD_SERVICE
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

private const val TAG = "ZeroConfService"

class ZeroConfService @Inject constructor(context: Context) {
    private val serviceTypes = listOf(
        "_smb._tcp.",
        "_ftp._tcp.",
        "_webdav._tcp.",
        "_nfs._tcp.",
    );

    private val nsdManager: NsdManager = (context.getSystemService(NSD_SERVICE) as NsdManager)
    private val lock = Mutex()

    private val _discoveredServices: MutableStateFlow<List<NsdServiceInfo>> = MutableStateFlow(
        emptyList()
    )

    val discoveredServices: StateFlow<List<NsdServiceInfo>> = _discoveredServices
    suspend fun startDiscovery() = coroutineScope {
        Log.i(TAG, "Starting discovery")

        for (serviceType in serviceTypes) {
            nsdManager.discoverServices(
                serviceType,
                NsdManager.PROTOCOL_DNS_SD,
                getDiscoveryListener(this)
            )
        }
    }

    private fun getDiscoveryListener(coroutineScope: CoroutineScope) =
        object : NsdManager.DiscoveryListener {
            // Called as soon as service discovery begins.
            override fun onDiscoveryStarted(regType: String) {
                Log.d(TAG, "Service discovery started")
            }

            override fun onServiceFound(service: NsdServiceInfo) {
                Log.d(
                    TAG,
                    "Service discovery success | ${service.serviceName}: ${service.attributes}"
                )

                coroutineScope.launch(start = CoroutineStart.UNDISPATCHED) {
                    lock.withLock {
                        _discoveredServices.emit((_discoveredServices.value + service).distinctBy {
                            it.serviceName
                        })
                    }
                }
            }

            override fun onServiceLost(service: NsdServiceInfo) {
                Log.e(TAG, "service lost: $service")
            }

            override fun onDiscoveryStopped(serviceType: String) {
                Log.i(TAG, "Discovery stopped: $serviceType")
            }

            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e(TAG, "Discovery failed: Error code:$errorCode")
                nsdManager.stopServiceDiscovery(this)
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e(TAG, "Discovery failed: Error code:$errorCode")
                nsdManager.stopServiceDiscovery(this)
            }
        }

    private fun createResolveListener(coroutineScope: CoroutineScope) =
        object : NsdManager.ResolveListener {
            override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.e(TAG, "Resolve failed: $errorCode")
            }

            override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                coroutineScope.launch(start = CoroutineStart.UNDISPATCHED) {
                    Log.i(TAG, "Resolve Succeeded. $serviceInfo")
                    lock.withLock {
                        val services = _discoveredServices.value as MutableList
                        val index = services.indexOfFirst { it.serviceName == serviceInfo.serviceName }
                        if (index != -1) {
                            services.removeAt(index)
                        }
                        _discoveredServices.emit(services + serviceInfo)
                    }
                }
            }
        }
}