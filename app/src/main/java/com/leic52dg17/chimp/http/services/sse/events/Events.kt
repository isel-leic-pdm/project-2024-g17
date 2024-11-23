package com.leic52dg17.chimp.http.services.sse.events

import com.leic52dg17.chimp.domain.model.channel.ChannelInvitation
import com.leic52dg17.chimp.domain.model.message.Message
import kotlinx.serialization.Serializable

@Serializable
sealed class Events {
    data class ChannelMessage(val message: Message): Events()
    data class Invitation(val channelInvitation: ChannelInvitation): Events()
}