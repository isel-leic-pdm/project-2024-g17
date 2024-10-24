package com.leic52dg17.chimp.model.channel

import com.leic52dg17.chimp.model.message.Message
import com.leic52dg17.chimp.model.user.User

data class Channel(
    val channelId: Int,
    val displayName: String,
    val messages: List<Message>,
    val users: List<User>,
    val isPrivate: Boolean,
    val channelIconUrl: String,
    val channelIconContentDescription: String? = "Channel Icon",
)