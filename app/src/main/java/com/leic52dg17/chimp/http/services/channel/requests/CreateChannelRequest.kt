package com.leic52dg17.chimp.http.services.channel.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateChannelRequest(
    val ownerId: Int,
    val name: String,
    val isPrivate: Boolean
)
