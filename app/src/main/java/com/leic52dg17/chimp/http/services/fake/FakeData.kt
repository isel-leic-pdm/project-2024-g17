package com.leic52dg17.chimp.http.services.fake

import com.leic52dg17.chimp.model.channel.Channel
import com.leic52dg17.chimp.model.channel.ChannelInvitation
import com.leic52dg17.chimp.model.channel.UserChannel
import com.leic52dg17.chimp.model.message.Message
import com.leic52dg17.chimp.model.user.User
import com.leic52dg17.chimp.model.user.UserRequest
import java.math.BigInteger

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

    val channelInvitations = mutableListOf<ChannelInvitation>()

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
}