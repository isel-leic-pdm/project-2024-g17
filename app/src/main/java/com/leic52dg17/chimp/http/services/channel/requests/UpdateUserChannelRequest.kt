package com.leic52dg17.chimp.http.services.channel.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserChannelRequest (
    val userId: Int,
    val channelId: Int
)