package com.leic52dg17.chimp.core.repositories.messages

import com.leic52dg17.chimp.core.repositories.messages.database.MessageDAO
import com.leic52dg17.chimp.core.repositories.messages.model.MessageEntity
import com.leic52dg17.chimp.core.repositories.messages.model.mapToMessage
import com.leic52dg17.chimp.domain.model.message.Message

class MessageRepository(private val messageDAO: MessageDAO) : IMessageRepository {
    override suspend fun storeMessages(messages: List<Message>) {
        val messageEntities = messages.map {
            MessageEntity(
                it.id,
                it.userId,
                it.channelId,
                it.text,
                it.createdAt
            )
        }
        messageDAO.insertAll(messageEntities)
    }

    override suspend fun getStoredMessages(): List<Message> {
        val messageEntities = messageDAO.getAll()
        val messages = messageEntities.map { it.mapToMessage() }
        return messages
    }

    override suspend fun getStoredMessagesForChannel(channelId: Int): List<Message> {
        val messageEntities = messageDAO.getByAllByChannelId(channelId)
        val messages = messageEntities.map { it.mapToMessage() }
        return messages
    }

    override suspend fun getDifferences(old: List<Message>, new: List<Message>): List<Message> {
        return new.filter { message -> !old.contains(message) }
    }

    override suspend fun isUpdateDue(oldCache: List<Message>, newCache: List<Message>): Boolean {
        return getDifferences(oldCache, newCache).isNotEmpty()
    }
}