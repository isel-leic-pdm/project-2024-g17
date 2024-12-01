package com.leic52dg17.chimp.http.services.channel.responses

import com.leic52dg17.chimp.domain.model.channel.ChannelInvitation
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import kotlinx.serialization.Serializable

@Serializable
data class GetChannelInvitationResponse(
    val id: Int,
    val senderId: Int,
    val receiverId: Int,
    val channelId: Int,
    val permissionLevel: String,
    val createdAt: Long
) {
    fun toChannelInvitation(): ChannelInvitation = ChannelInvitation(
        id = id,
        senderId = senderId,
        receiverId = receiverId,
        channelId = channelId,
        permissionLevel = if (permissionLevel == "RW") PermissionLevel.RW else PermissionLevel.RR,
    )
}