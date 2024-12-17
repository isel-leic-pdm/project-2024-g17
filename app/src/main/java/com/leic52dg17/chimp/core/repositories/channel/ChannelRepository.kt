package com.leic52dg17.chimp.core.repositories.channel

import android.util.Log
import com.leic52dg17.chimp.core.repositories.channel.database.ChannelDAO
import com.leic52dg17.chimp.core.repositories.channel.model.ChannelEntity
import com.leic52dg17.chimp.core.repositories.messages.IMessageRepository

import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.message.Message

class ChannelRepository(
    private val messageRepository: IMessageRepository,
    private val channelDao: ChannelDAO
) : IChannelRepository {
    override suspend fun storeChannels(channels: List<Channel>) {
        val channelEntities = channels.map {
            ChannelEntity(
                it.channelId,
                it.displayName,
                it.ownerId,
                it.isPrivate,
                it.channelIconUrl
            )
        }
        channelDao.insertAll(channelEntities)
    }

    override suspend fun getStoredChannels(): List<Channel> {
        val channelEntities = channelDao.getAll()
        val messagesForEach: MutableList<List<Message>> = emptyList<List<Message>>().toMutableList()
        val channels: MutableList<Channel> = emptyList<Channel>().toMutableList()

        for (channelEntity in channelEntities) {
            val messageEntityList =
                messageRepository.getStoredMessagesForChannel(channelEntity.channelId)
            val messages = messageEntityList.map {
                Message(
                    it.id,
                    it.userId,
                    it.channelId,
                    it.text,
                    it.createdAt
                )
            }
            messagesForEach.add(messages)
        }

        for (i in 0..<messagesForEach.size) {
            val channelEntity = channelEntities[i]
            val messages = messagesForEach[i]
            val channel = Channel(
                channelEntity.channelId,
                channelEntity.displayName,
                channelEntity.ownerId,
                messages,
                emptyList(),
                channelEntity.isPrivate,
                channelEntity.channelIconUrl
            )
            channels.add(channel)
        }

        return channels
    }

    override suspend fun getDifferences(old: List<Channel>, new: List<Channel>): List<Channel> {
        val differences = new.filter { channel -> !old.contains(channel) }
        return differences
    }

    override suspend fun isUpdateDue(oldCache: List<Channel>, newCache: List<Channel>): Boolean {
        return getDifferences(oldCache, newCache).isNotEmpty()
    }

    companion object {
        const val TAG = "CHANNEL_REPOSITORY"
    }
}