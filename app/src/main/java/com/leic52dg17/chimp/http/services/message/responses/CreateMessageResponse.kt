package com.leic52dg17.chimp.http.services.message.responses

import kotlinx.serialization.Serializable

@Serializable
data class CreateMessageResponse(
    val messageId: Int
)