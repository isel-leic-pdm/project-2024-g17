package com.leic52dg17.chimp.model.channel

import com.leic52dg17.chimp.model.common.PermissionLevel
import java.util.UUID

data class ChannelInvitation(
    val invitationId: UUID,
    val channelId: Int,
    val senderId: Int,
    val receiverId: Int,
    val permissionLevel: PermissionLevel
)
