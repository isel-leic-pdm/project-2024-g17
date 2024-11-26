package com.leic52dg17.chimp.http.services.channel.requests

import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import kotlinx.serialization.Serializable

@Serializable
data class CreateChannelInvitationRequest(
    val senderId: Int,
    val receiverId: Int,
    val channelId: Int,
    val permissionLevel: PermissionLevel
)
