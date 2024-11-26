package com.leic52dg17.chimp.http.services.channel.responses

import kotlinx.serialization.Serializable

@Serializable
data class CreateChannelInvitationResponse(
    val channelInvitationId: Int
)
