package com.leic52dg17.chimp.http.services.channel.implementations

import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.channel.results.ChannelCreationError
import com.leic52dg17.chimp.http.services.channel.results.ChannelCreationResult
import com.leic52dg17.chimp.http.services.channel.results.ChannelUpdateError
import com.leic52dg17.chimp.http.services.channel.results.RemoveUserFromChannelResult
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
            ownerId,
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

    override suspend fun getChannelInfo(channelId: Int): Channel? {
        return if (FakeData.channels.any { channel -> channel.channelId == channelId }) {
            FakeData.channels[channelId]
        } else {
            null
        }
    }

    override suspend fun getUserSubscribedChannels(userId: Int): List<Channel>? {
        if (FakeData.users.isEmpty() || !FakeData.users.any { it.userId == userId }) return null
        return FakeData.channels.filter { it.users.any { user -> user.userId == userId } }
    }

    override suspend fun getChannelById(channelId: Int): Channel? {
        return FakeData.channels.firstOrNull { it.channelId == channelId }
    }

    override suspend fun removeUserFromChannel(
        userId: Int,
        channelId: Int
    ): RemoveUserFromChannelResult {

        val channelIndex = FakeData.channels.indexOfFirst { it.channelId == channelId }
        if(channelIndex == -1) return failure(
            ChannelUpdateError(ErrorMessages.CHANNEL_NOT_FOUND)
        )
        val channel = FakeData.channels[channelIndex]
        val isUserInChannel = channel.users.any { it.userId == userId }
        if(!isUserInChannel) return failure(
            ChannelUpdateError(ErrorMessages.USER_NOT_IN_CHANNEL)
        )
        val updatedChannel = channel.copy(users = channel.users.filter { user -> user.userId != userId })
        FakeData.channels[channelIndex] = updatedChannel

        return success(userId)
    }
}