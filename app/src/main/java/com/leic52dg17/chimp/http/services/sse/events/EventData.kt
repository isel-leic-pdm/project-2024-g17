package com.leic52dg17.chimp.http.services.sse.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class EventData(
    val type: MessageTypes,
    val content: JsonElement
)
