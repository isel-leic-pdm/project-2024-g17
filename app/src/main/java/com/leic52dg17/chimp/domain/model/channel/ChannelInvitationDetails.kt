package com.leic52dg17.chimp.domain.model.channel

import com.leic52dg17.chimp.domain.model.common.PermissionLevel

data class ChannelInvitationDetails(
    val id: Int,
    val channelId: Int,
    val senderId: Int,
    val receiverId: Int,
    val permissionLevel: PermissionLevel,
    val senderUsername: String,
    val channelName: String,
)
