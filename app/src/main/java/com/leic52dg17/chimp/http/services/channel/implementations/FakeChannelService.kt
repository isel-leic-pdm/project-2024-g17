package com.leic52dg17.chimp.http.services.channel.implementations

import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.channel.results.ChannelCreationError
import com.leic52dg17.chimp.http.services.channel.results.ChannelCreationResult
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

        val owner = users.find { it.userId == ownerId }
            ?: return failure(ChannelCreationError.UserCouldNotBeFound(ErrorMessages.FakeChannelService.USER_NOT_FOUND))

        val newChannel = Channel(
            channels.size + 1,
            name,
            mutableListOf<Message>(),
            mutableListOf(owner),
            isPrivate,
            channelIconUrl,
            channelIconContentDescription
        )
        channels.add(newChannel)
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
        channelInvitations.add(newChannelInvitation)
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
            userRequests.size + 1,
            userId,
            inviteId
        )
        userRequests.add(newUserRequest)
        return true
    }

    override suspend fun getChannelInfo(channelId: Int): Channel? {
        return if (channels.any { channel -> channel.channelId == channelId }) {
            channels[channelId]
        } else {
            null
        }
    }

    override suspend fun getUserSubscribedChannels(userId: Int): List<Channel>? {
        if (users.isEmpty() || !users.any { it.userId == userId }) return null
        return channels.filter { it.users.any { user -> user.userId == userId } }
    }

    companion object {

        val users = mutableListOf(
            User(
                1,
                "username1",
                "User 1"
            ),
            User(
                2,
                "username2",
                "User 2"
            )
        )

        val channelInvitations = mutableListOf(
            ChannelInvitation(
                UUID.randomUUID(),
                1,
                1,
                PermissionLevels.RW,
                1
            )
        )

        val userRequests = mutableListOf<UserRequest>()

        val channels = mutableListOf(
            Channel(
                channelId = 1,
                displayName = "Channel 1",
                messages = listOf(
                    Message(
                        1,
                        1,
                        "Hello world",
                        BigInteger("21031239131231298")
                    )
                ),
                users = listOf(
                    User(
                        1,
                        "username1",
                        "User 1"
                    )
                ),
                channelIconUrl = "https://fake.com/not-real",
                isPrivate = false

            )
        )
    }
}