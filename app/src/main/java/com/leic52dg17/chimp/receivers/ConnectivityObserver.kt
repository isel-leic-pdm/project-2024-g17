package com.leic52dg17.chimp.receivers

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ConnectivityObserver(context: Context) {
    private val connectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val _connectivityStatusFlow = MutableStateFlow(false)
    val connectivityStatusFlow: MutableStateFlow<Boolean> get() = _connectivityStatusFlow

    private fun observe() = callbackFlow {
        val callback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(false)
            }
        }

        Log.i(TAG, "Registering network callback")
        connectivityManager.registerDefaultNetworkCallback(callback)

        // Called when there are no more collectors
        awaitClose {
            Log.i(TAG, "Unregistering network callback")
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    fun startObserving(
        onLostCallback: () -> Unit,
        onAvailableCallback: () -> Unit,
        scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    ) {
        scope.launch {
            observe().collectLatest { isConnected ->
                if (isConnected) {
                    Log.i(TAG, "Network available")
                    connectivityStatusFlow.value = true
                    onAvailableCallback()
                } else {
                    Log.i(TAG, "Network lost")
                    connectivityStatusFlow.value = false
                    onLostCallback()
                }
            }
        }
    }

    companion object {
        private const val TAG = "CONNECTIVITY_OBSERVER"
    }
}