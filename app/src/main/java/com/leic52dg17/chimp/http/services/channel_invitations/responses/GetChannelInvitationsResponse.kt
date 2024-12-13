package com.leic52dg17.chimp.http.services.channel_invitations.responses

import kotlinx.serialization.Serializable

@Serializable
data class GetChannelInvitationsResponse(
    val channelInvitations: List<GetChannelInvitationResponse>
)
