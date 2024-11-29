package com.leic52dg17.chimp.http.services.message.responses

import com.leic52dg17.chimp.domain.model.message.Message
import kotlinx.serialization.Serializable

@Serializable
data class GetMessagesResponse(
    val messages: List<Message>
)