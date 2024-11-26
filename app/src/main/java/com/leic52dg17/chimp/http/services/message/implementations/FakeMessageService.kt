/*
package com.leic52dg17.chimp.http.services.message.implementations

import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.common.failure
import com.leic52dg17.chimp.domain.model.common.success
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.http.services.fake.FakeData
import com.leic52dg17.chimp.http.services.message.IMessageService
import java.time.Instant

class FakeMessageService : IMessageService {
    override suspend fun getChannelMessages(channelId: Int): List<Message> {
        val channel = FakeData.channels.firstOrNull { it.channelId == channelId }
        if (channel == null) throw ServiceException(ErrorMessages.CHANNEL_NOT_FOUND)
        return channel.messages
    }

    override suspend fun createMessageInChannel(
        messageText: String,
        channelId: Int,
        senderId: Int
    ): MessageCreationResult {
        val channel = FakeData.channels.firstOrNull { it.channelId == channelId }
        if (channel == null) return failure(MessageCreationError(ErrorMessages.CHANNEL_NOT_FOUND))

        val newMessage = Message(channel.messages.size + 1,senderId, channelId, messageText, Instant.now().epochSecond)
        val updatedMessages = channel.messages + newMessage
        val updatedChannel = channel.copy(messages = updatedMessages)

        FakeData.channels[FakeData.channels.indexOf(channel)] = updatedChannel

        return success(true)
    }
}*/
