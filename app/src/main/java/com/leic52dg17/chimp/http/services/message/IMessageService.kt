package com.leic52dg17.chimp.http.services.message

import com.leic52dg17.chimp.http.services.message.results.GetChannelMessagesResult
import com.leic52dg17.chimp.http.services.message.results.MessageCreationResult

interface IMessageService {
    suspend fun getChannelMessages(channelId: Int): GetChannelMessagesResult
    suspend fun createMessageInChannel(messageText: String, channelId: Int, senderId: Int): MessageCreationResult
}