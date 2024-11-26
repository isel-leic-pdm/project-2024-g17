package com.leic52dg17.chimp.http.services.message.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateMessageRequest(
    val channelId: Int,
    val userId: Int,
    val text: String
)
