package com.leic52dg17.chimp.http.services.channel.implementations

import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.channel.results.ChannelCreationError
import com.leic52dg17.chimp.http.services.channel.results.ChannelCreationResult
import com.leic52dg17.chimp.http.services.fake.FakeData
import com.leic52dg17.chimp.model.channel.Channel
import com.leic52dg17.chimp.model.channel.ChannelInvitation
import com.leic52dg17.chimp.model.common.ErrorMessages
import com.leic52dg17.chimp.model.common.PermissionLevels
import com.leic52dg17.chimp.model.common.failure
import com.leic52dg17.chimp.model.common.success
import com.leic52dg17.chimp.model.message.Message
import com.leic52dg17.chimp.model.user.User
import com.leic52dg17.chimp.model.user.UserRequest
import java.math.BigInteger
import java.util.UUID

class FakeChannelService : IChannelService {
    override suspend fun createChannel(
        ownerId: Int,
        name: String,
        isPrivate: Boolean,
        channelIconUrl: String,
        channelIconContentDescription: String
    ): ChannelCreationResult {

        val owner = FakeData.users.find { it.userId == ownerId }
            ?: return failure(ChannelCreationError(message = ErrorMessages.USER_NOT_FOUND))

        val newChannel = Channel(
            FakeData.channels.size + 1,
            name,
            mutableListOf<Message>(),
            mutableListOf(owner),
            isPrivate,
            channelIconUrl,
            channelIconContentDescription
        )
        FakeData.channels.add(newChannel)
        return success(newChannel.channelId)
    }

    override suspend fun createChannelInvitation(
        channelId: Int,
        senderId: Int,
        permissionLevel: PermissionLevels,
        maxUses: Int
    ): UUID {
        val newChannelInvitation = ChannelInvitation(
            UUID.randomUUID(),
            channelId,
            senderId,
            permissionLevel,
            maxUses
        )
        FakeData.channelInvitations.add(newChannelInvitation)
        return newChannelInvitation.invitationId
    }

    override suspend fun sendChannelInviteToUser(
        channelId: Int,
        userId: Int,
        senderId: Int,
        permissionLevel: PermissionLevels
    ): Boolean {
        val inviteId = createChannelInvitation(
            channelId,
            senderId,
            permissionLevel,
            1
        )
        val newUserRequest = UserRequest(
            FakeData.userRequests.size + 1,
            userId,
            inviteId
        )
        FakeData.userRequests.add(newUserRequest)
        return true
    }

    override suspend fun getUserSubscribedChannels(userId: Int): List<Channel>? {
        if (FakeData.users.isEmpty() || !FakeData.users.any { it.userId == userId }) return null
        return FakeData.channels.filter { it.users.any { user -> user.userId == userId } }
    }

    override suspend fun getChannel(channelId: Int): Channel? {
        return FakeData.channels.firstOrNull { it.channelId == channelId }
    }
}