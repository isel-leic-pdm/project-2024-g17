/*
package com.leic52dg17.chimp.http.services.channel.implementations

import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.fake.FakeData
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.channel.ChannelInvitation
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import com.leic52dg17.chimp.domain.model.common.failure
import com.leic52dg17.chimp.d/*
package com.leic52dg17.chimp.http.services.channel.implementations

import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.fake.FakeData
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.channel.ChannelInvitation
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import com.leic52dg17.chimp.domain.model.common.failure
import com.leic52dg17.chimp.domain.model.common.success
import com.leic52dg17.chimp.domain.model.message.Message
import java.util.UUID

class FakeChannelService : IChannelService {
    override suspend fun createChannel(
        ownerId: Int,
        name: String,
        isPrivate: Boolean,
        channelIconUrl: String,
        channelIconContentDescription: String
    ): ChannelCreationResult {

        val owner = FakeData.users.find { it.id == ownerId }
            ?: return failure(ChannelCreationError(message = ErrorMessages.USER_NOT_FOUND))

        val newChannel = Channel(
            FakeData.channels.size + 1,
            name,
            ownerId,
            mutableListOf<Message>(),
            mutableListOf(owner),
            isPrivate,
            channelIconUrl
        )
        FakeData.channels.add(newChannel)
        return success(newChannel.channelId)
    }

    override suspend fun createChannelInvitation(
        channelId: Int,
        senderId: Int,
        receiverId: Int,
        permissionLevel: PermissionLevel
    ): UUID {
        val newChannelInvitation = ChannelInvitation(
            UUID.randomUUID(),
            channelId,
            senderId,
            receiverId,
            permissionLevel
        )
        FakeData.channelInvitations.add(newChannelInvitation)
        return newChannelInvitation.invitationId
    }

    override suspend fun getChannelInfo(channelId: Int): Channel? {
        return if (FakeData.channels.any { channel -> channel.channelId == channelId }) {
            FakeData.channels[channelId]
        } else {
            null
        }
    }

    override suspend fun getUserSubscribedChannels(userId: Int): List<Channel>? {
        if (FakeData.users.isEmpty() || !FakeData.users.any { it.id == userId }) return null
        return FakeData.channels.filter { it.users.any { user -> user.id == userId } }
    }

    override suspend fun getChannelById(channelId: Int): Channel? {
        return FakeData.channels.firstOrNull { it.channelId == channelId }
    }

    override suspend fun removeUserFromChannel(
        userId: Int,
        channelId: Int
    ): RemoveUserFromChannelResult {

        val channelIndex = FakeData.channels.indexOfFirst { it.channelId == channelId }
        if (channelIndex == -1) return failure(
            ChannelUpdateError(ErrorMessages.CHANNEL_NOT_FOUND)
        )
        val channel = FakeData.channels[channelIndex]
        val isUserInChannel = channel.users.any { it.id == userId }
        if (!isUserInChannel) return failure(
            ChannelUpdateError(ErrorMessages.USER_NOT_IN_CHANNEL)
        )
        val userChannels = FakeData.userChannel.filter { it.channelId == channelId }
        if (channel.ownerId == userId) {
            val oldestUserChannel = userChannels.minByOrNull { it.joinedAt }
            val leavingUserChannelIndex =
                userChannels.indexOf(userChannels.first { it.userId == userId })

            if (oldestUserChannel == null) return failure(
                ChannelUpdateError(ErrorMessages.INDEXING_ERROR)
            )

            val oldestUserId = oldestUserChannel.userId

            val removedUserChannel =
                channel.copy(users = channel.users.filter { user -> user.id != userId })
            val updatedChannel = removedUserChannel.copy(ownerId = oldestUserId)
            FakeData.userChannel.removeAt(leavingUserChannelIndex)
            FakeData.channels[channelIndex] = updatedChannel
        } else {
            val removedUserChannel =
                channel.copy(users = channel.users.filter { user -> user.id != userId })
            FakeData.channels[channelIndex] = removedUserChannel
        }

        return success(userId)
    }
}*/
omain.model.common.success
import com.leic52dg17.chimp.domain.model.message.Message
import java.util.UUID

class FakeChannelService : IChannelService {
    override suspend fun createChannel(
        ownerId: Int,
        name: String,
        isPrivate: Boolean,
        channelIconUrl: String,
        channelIconContentDescription: String
    ): ChannelCreationResult {

        val owner = FakeData.users.find { it.id == ownerId }
            ?: return failure(ChannelCreationError(message = ErrorMessages.USER_NOT_FOUND))

        val newChannel = Channel(
            FakeData.channels.size + 1,
            name,
            ownerId,
            mutableListOf<Message>(),
            mutableListOf(owner),
            isPrivate,
            channelIconUrl
        )
        FakeData.channels.add(newChannel)
        return success(newChannel.channelId)
    }

    override suspend fun createChannelInvitation(
        channelId: Int,
        senderId: Int,
        receiverId: Int,
        permissionLevel: PermissionLevel
    ): UUID {
        val newChannelInvitation = ChannelInvitation(
            UUID.randomUUID(),
            channelId,
            senderId,
            receiverId,
            permissionLevel
        )
        FakeData.channelInvitations.add(newChannelInvitation)
        return newChannelInvitation.invitationId
    }

    override suspend fun getChannelInfo(channelId: Int): Channel? {
        return if (FakeData.channels.any { channel -> channel.channelId == channelId }) {
            FakeData.channels[channelId]
        } else {
            null
        }
    }

    override suspend fun getUserSubscribedChannels(userId: Int): List<Channel>? {
        if (FakeData.users.isEmpty() || !FakeData.users.any { it.id == userId }) return null
        return FakeData.channels.filter { it.users.any { user -> user.id == userId } }
    }

    override suspend fun getChannelById(channelId: Int): Channel? {
        return FakeData.channels.firstOrNull { it.channelId == channelId }
    }

    override suspend fun removeUserFromChannel(
        userId: Int,
        channelId: Int
    ): RemoveUserFromChannelResult {

        val channelIndex = FakeData.channels.indexOfFirst { it.channelId == channelId }
        if (channelIndex == -1) return failure(
            ChannelUpdateError(ErrorMessages.CHANNEL_NOT_FOUND)
        )
        val channel = FakeData.channels[channelIndex]
        val isUserInChannel = channel.users.any { it.id == userId }
        if (!isUserInChannel) return failure(
            ChannelUpdateError(ErrorMessages.USER_NOT_IN_CHANNEL)
        )
        val userChannels = FakeData.userChannel.filter { it.channelId == channelId }
        if (channel.ownerId == userId) {
            val oldestUserChannel = userChannels.minByOrNull { it.joinedAt }
            val leavingUserChannelIndex =
                userChannels.indexOf(userChannels.first { it.userId == userId })

            if (oldestUserChannel == null) return failure(
                ChannelUpdateError(ErrorMessages.INDEXING_ERROR)
            )

            val oldestUserId = oldestUserChannel.userId

            val removedUserChannel =
                channel.copy(users = channel.users.filter { user -> user.id != userId })
            val updatedChannel = removedUserChannel.copy(ownerId = oldestUserId)
            FakeData.userChannel.removeAt(leavingUserChannelIndex)
            FakeData.channels[channelIndex] = updatedChannel
        } else {
            val removedUserChannel =
                channel.copy(users = channel.users.filter { user -> user.id != userId })
            FakeData.channels[channelIndex] = removedUserChannel
        }

        return success(userId)
    }
}*/
