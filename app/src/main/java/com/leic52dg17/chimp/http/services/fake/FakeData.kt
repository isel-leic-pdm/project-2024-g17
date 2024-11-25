package com.leic52dg17.chimp.http.services.fake

import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.channel.ChannelInvitation
import com.leic52dg17.chimp.domain.model.channel.UserChannel
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.domain.model.user.User
import java.math.BigInteger
import java.util.UUID

object FakeData {
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
        ),
        User(
            3,
            "username3",
            "User 3"
        ),
        User(
            4,
            "username4",
            "User 4"
        )
    )

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
                ),
                Message(
                    2,
                    1,
                    "Hello universe.",
                    BigInteger("21031239131231350")
                )
            ),
            users = listOf(
                users[0],
                users[1],
                users[2]
            ),
            channelIconUrl = "https://fake.com/not-real",
            isPrivate = false,
            ownerId = 1
        )
    )

    val userChannel = mutableListOf(
        UserChannel(
            1,
            1,
            BigInteger("1603999046")
        ),
        UserChannel(
            2,
            1,
            BigInteger("1603999300")
        ),
        UserChannel(
            3,
            1,
            BigInteger("1603999100")
        )
    )
    val channelInvitations = mutableListOf<ChannelInvitation>()

    val invitations = mutableListOf(
        ChannelInvitation(
            invitationId = UUID.randomUUID(),
            channelId = 1,
            senderId = 2,
            receiverId = 1,
            permissionLevel = PermissionLevel.RW
        ),
        ChannelInvitation(
            invitationId = UUID.randomUUID(),
            channelId = 2,
            senderId = 2,
            receiverId = 1,
            permissionLevel = PermissionLevel.RW
        ),
        ChannelInvitation(
            invitationId = UUID.randomUUID(),
            channelId = 3,
            senderId = 3,
            receiverId = 1,
            permissionLevel = PermissionLevel.RW
        ),
        ChannelInvitation(
            invitationId = UUID.randomUUID(),
            channelId = 4,
            senderId = 4,
            receiverId = 1,
            permissionLevel = PermissionLevel.RW
        ),
        ChannelInvitation(
            invitationId = UUID.randomUUID(),
            channelId = 5,
            senderId = 1,
            receiverId = 1,
            permissionLevel = PermissionLevel.RW
        )
    )
}