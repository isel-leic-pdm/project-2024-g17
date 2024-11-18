package com.leic52dg17.chimp.http.services.channel

import com.leic52dg17.chimp.http.services.channel.results.ChannelCreationResult
import com.leic52dg17.chimp.http.services.channel.results.RemoveUserFromChannelResult
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
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
        receiverId: Int,
        permissionLevel: PermissionLevel
    ): UUID

    suspend fun getChannelInfo(channelId: Int): Channel?

    suspend fun getUserSubscribedChannels(userId: Int): List<Channel>?

    suspend fun getChannelById(channelId: Int): Channel?

    suspend fun removeUserFromChannel(
        userId: Int,
        channelId: Int
    ): RemoveUserFromChannelResult
}