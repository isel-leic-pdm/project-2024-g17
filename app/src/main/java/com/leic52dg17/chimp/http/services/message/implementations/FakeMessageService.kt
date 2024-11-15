package com.leic52dg17.chimp.http.services.message.implementations

import com.leic52dg17.chimp.http.services.fake.FakeData
import com.leic52dg17.chimp.http.services.message.IMessageService
import com.leic52dg17.chimp.http.services.message.results.GetChannelMessagesError
import com.leic52dg17.chimp.http.services.message.results.GetChannelMessagesResult
import com.leic52dg17.chimp.http.services.message.results.MessageCreationError
import com.leic52dg17.chimp.http.services.message.results.MessageCreationResult
import com.leic52dg17.chimp.model.common.ErrorMessages
import com.leic52dg17.chimp.model.common.failure
import com.leic52dg17.chimp.model.common.success
import com.leic52dg17.chimp.model.message.Message
import java.math.BigInteger
import java.time.Instant

class FakeMessageService : IMessageService {
    override suspend fun getChannelMessages(channelId: Int): GetChannelMessagesResult {
        val channel = FakeData.channels.firstOrNull { it.channelId == channelId }
        if (channel == null) return failure(GetChannelMessagesError(ErrorMessages.CHANNEL_NOT_FOUND))
        return success(channel.messages)
    }

    override suspend fun createMessageInChannel(
        messageText: String,
        channelId: Int,
        senderId: Int
    ): MessageCreationResult {
        val channel = FakeData.channels.firstOrNull { it.channelId == channelId }
        if (channel == null) return failure(MessageCreationError(ErrorMessages.CHANNEL_NOT_FOUND))

        val newMessage = Message(senderId, channelId, messageText, BigInteger(Instant.now().epochSecond.toString()))
        val updatedMessages = channel.messages + newMessage
        val updatedChannel = channel.copy(messages = updatedMessages)

        FakeData.channels[FakeData.channels.indexOf(channel)] = updatedChannel

        return success(true)
    }
}