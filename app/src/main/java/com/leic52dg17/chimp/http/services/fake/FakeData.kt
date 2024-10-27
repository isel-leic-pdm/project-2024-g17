package com.leic52dg17.chimp.http.services.fake

import com.leic52dg17.chimp.model.channel.Channel
import com.leic52dg17.chimp.model.channel.ChannelInvitation
import com.leic52dg17.chimp.model.common.PermissionLevels
import com.leic52dg17.chimp.model.message.Message
import com.leic52dg17.chimp.model.user.User
import com.leic52dg17.chimp.model.user.UserRequest
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
                ),
                Message(
                    2,
                    1,
                    "Hello universe.",
                    BigInteger("21031239131231350")
                )
            ),
            users = listOf(
                User(
                    1,
                    "username1",
                    "User 1"
                ),
                User(
                    2,
                    "username1",
                    "User 2"
                )
            ),
            channelIconUrl = "https://fake.com/not-real",
            isPrivate = false
        )
    )
}