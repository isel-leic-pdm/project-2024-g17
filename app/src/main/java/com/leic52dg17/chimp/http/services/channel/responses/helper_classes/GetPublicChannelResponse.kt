package com.leic52dg17.chimp.http.services.channel.responses.helper_classes

import kotlinx.serialization.Serializable

@Serializable
data class GetPublicChannelResponse(
    val id: Int,
    val ownerId: Int,
    val name: String,
    val isPrivate: Boolean
)