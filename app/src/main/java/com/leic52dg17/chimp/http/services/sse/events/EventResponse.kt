package com.leic52dg17.chimp.http.services.sse.events

import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    val id: Int,
    val data: EventData
)
