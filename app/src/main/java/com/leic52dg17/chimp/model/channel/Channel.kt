package com.leic52dg17.chimp.model.channel

import com.leic52dg17.chimp.model.User
import com.leic52dg17.chimp.model.message.Message

data class Channel(
    val channelId: Int,
    val displayName: String,
    val messages: List<Message>,
    val users: List<User>,
    val channelIconUrl: String,
    val channelIconContentDescription: String?,
)