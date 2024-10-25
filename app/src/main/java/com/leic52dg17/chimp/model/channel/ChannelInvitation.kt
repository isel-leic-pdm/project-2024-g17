package com.leic52dg17.chimp.model.channel

import com.leic52dg17.chimp.model.common.PermissionLevels
import java.util.UUID

data class ChannelInvitation(
    val invitationId: UUID,
    val channelId: Int,
    val senderId: Int,
    val permissionLevel: PermissionLevels,
    val maxUses: Int
)
