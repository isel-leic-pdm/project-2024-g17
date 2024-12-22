package com.leic52dg17.chimp.http.services.channel

import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.common.PermissionLevel

interface IChannelService {
    suspend fun createChannel(
        ownerId: Int,
        name: String,
        isPrivate: Boolean,
        channelIconUrl: String,
    ): Int

    suspend fun createChannelInvitation(
        channelId: Int,
        senderId: Int,
        receiverId: Int,
        permissionLevel: PermissionLevel
    ): Int

    suspend fun getUserSubscribedChannels(userId: Int): List<Channel>

    suspend fun getChannelById(channelId: Int): Channel

    suspend fun removeUserFromChannel(userId: Int, channelId: Int): Int

    suspend fun getUserPermissionsByChannelId(userId: Int, channelId: Int): PermissionLevel

    suspend fun getPublicChannels(
        channelName: String,
        page: Int? = 0,
        limit: Int? = 10
    ): List<Channel>

    suspend fun addUserToChannel(userId: Int, channelId: Int)
}
