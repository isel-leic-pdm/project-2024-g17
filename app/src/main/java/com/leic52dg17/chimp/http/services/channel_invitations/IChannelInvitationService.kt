package com.leic52dg17.chimp.http.services.channel_invitations

import com.leic52dg17.chimp.domain.model.channel.ChannelInvitation
import com.leic52dg17.chimp.domain.model.common.PermissionLevel

interface IChannelInvitationService {
    suspend fun getChannelInvitationById(invitationId: Int): ChannelInvitation
    suspend fun getChannelInvitationsByReceiverId(receiverId: Int): List<ChannelInvitation>
    suspend fun createChannelInvitation(
        senderId: Int,
        receiverId: Int,
        channelId: Int,
        permissionLevel: PermissionLevel
    ): Int
    suspend fun acceptChannelInvitation(invitationId: Int)
    suspend fun rejectChannelInvitation(invitationId: Int)
}