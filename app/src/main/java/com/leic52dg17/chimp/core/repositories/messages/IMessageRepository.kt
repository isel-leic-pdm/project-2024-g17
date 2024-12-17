package com.leic52dg17.chimp.core.repositories.messages

import com.leic52dg17.chimp.domain.model.message.Message

interface IMessageRepository {
    suspend fun storeMessages(messages: List<Message>)
    suspend fun getStoredMessages(): List<Message>
    suspend fun getStoredMessagesForChannel(channelId: Int): List<Message>
    suspend fun getDifferences(old: List<Message>, new: List<Message>): List<Message>
    suspend fun isUpdateDue(oldCache: List<Message>, newCache: List<Message>): Boolean
}