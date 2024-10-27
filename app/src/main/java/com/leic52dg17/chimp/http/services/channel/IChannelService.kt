package com.leic52dg17.chimp.http.services.channel

import com.leic52dg17.chimp.http.services.channel.results.ChannelCreationResult
import com.leic52dg17.chimp.model.channel.Channel
import com.leic52dg17.chimp.model.common.PermissionLevels
import java.util.UUID

interface IChannelService {
    suspend fun createChannel(
        ownerId: Int,
        name: String,
        isPrivate: Boolean,
        channelIconUrl: String,
        channelIconContentDescription: String
    ): ChannelCreationResult

    suspend fun createChannelInvitation(
        channelId: Int,
        senderId: Int,
        permissionLevel: PermissionLevels,
        maxUses: Int
    ): UUID

    suspend fun sendChannelInviteToUser(
        channelId: Int,
        userId: Int,
        senderId: Int,
        permissionLevel: PermissionLevels
    ): Boolean

    suspend fun getChannelInfo(channelId: Int): Channel?

    suspend fun getUserSubscribedChannels(userId: Int): List<Channel>?

    suspend fun getChannel(channelId: Int): Channel?
}