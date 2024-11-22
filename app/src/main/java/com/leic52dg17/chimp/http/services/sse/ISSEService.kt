package com.leic52dg17.chimp.http.services.sse

import com.leic52dg17.chimp.http.services.sse.events.Events
import com.leic52dg17.chimp.http.services.sse.results.SSEServiceResult
import kotlinx.coroutines.flow.MutableSharedFlow

interface ISSEService {
    fun listen(eventFlow: MutableSharedFlow<Events>): SSEServiceResult
    fun stopListening()
}