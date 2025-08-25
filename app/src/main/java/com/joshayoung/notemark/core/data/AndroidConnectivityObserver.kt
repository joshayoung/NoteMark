package com.joshayoung.notemark.core.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import com.joshayoung.notemark.core.domain.ConnectivityObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AndroidConnectivityObserver(
    private val context: Context,
) : ConnectivityObserver {
    private val connectivityManager = context.getSystemService<ConnectivityManager>()!!

    override val isConnected: Flow<Boolean>
        get() =
            callbackFlow {
                val callback =
                    object : ConnectivityManager.NetworkCallback() {
                        override fun onCapabilitiesChanged(
                            network: Network,
                            networkCapabilities: NetworkCapabilities,
                        ) {
                            super.onCapabilitiesChanged(network, networkCapabilities)
                            val connected =
                                networkCapabilities.hasCapability(
                                    // pings a remote server to ensure a connection is available:
                                    NetworkCapabilities.NET_CAPABILITY_VALIDATED,
                                )
                            trySend(connected)
                        }

                        override fun onUnavailable() {
                            super.onUnavailable()
                            trySend(false)
                        }

                        override fun onLost(network: Network) {
                            super.onLost(network)
                            trySend(false)
                        }

                        override fun onAvailable(network: Network) {
                            super.onAvailable(network)
                            trySend(true)
                        }
                    }

                connectivityManager.registerDefaultNetworkCallback(callback)

                // runs when the flow is cancelled:
                awaitClose {
                    connectivityManager.unregisterNetworkCallback(callback)
                }
            }
}