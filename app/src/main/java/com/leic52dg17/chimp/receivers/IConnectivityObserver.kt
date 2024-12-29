package com.leic52dg17.chimp.receivers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow

interface IConnectivityObserver {
    val connectivityStatusFlow: MutableStateFlow<Boolean>
    fun startObserving(
        onLostCallback: () -> Unit,
        onAvailableCallback: () -> Unit,
        scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    )
}