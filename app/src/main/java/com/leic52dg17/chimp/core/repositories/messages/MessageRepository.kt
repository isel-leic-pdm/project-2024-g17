package com.leic52dg17.chimp.core.repositories.messages

import com.leic52dg17.chimp.domain.model.message.Message

class MessageRepository: IMessageRepository {
    override suspend fun registerCallback(callback: () -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun unregisterCallback() {
        TODO("Not yet implemented")
    }

    override suspend fun runCallback() {
        TODO("Not yet implemented")
    }

    override suspend fun storeMessages(channels: List<Message>) {
        TODO("Not yet implemented")
    }

    override suspend fun getStoredMessages(): List<Message> {
        TODO("Not yet implemented")
    }

    override suspend fun getStoredMessagesForChannel(channelId: Int): List<Message> {
        TODO("Not yet implemented")
    }

    override suspend fun getDifferences(old: List<Message>, new: List<Message>): List<Message> {
        TODO("Not yet implemented")
    }

    override suspend fun isUpdateDue(oldCache: List<Message>, newCache: List<Message>): Boolean {
        TODO("Not yet implemented")
    }
}