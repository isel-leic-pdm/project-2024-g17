package com.leic52dg17.chimp.http.services.channel.responses

import kotlinx.serialization.Serializable

@Serializable
data class GetChannelResponse (
    val id: Int,
    val ownerId: Int,
    val name: String,
    val isPrivate: Boolean
)