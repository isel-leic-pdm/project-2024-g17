package com.leic52dg17.chimp.http.services.message

import com.leic52dg17.chimp.domain.model.message.Message

interface IMessageService {
    suspend fun getChannelMessages(channelId: Int): List<Message>
    suspend fun createMessageInChannel(messageText: String, channelId: Int, senderId: Int): Int
    suspend fun getMessageById(id: Int): Message
}