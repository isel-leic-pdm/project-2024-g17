package com.leic52dg17.chimp.core.repositories.channel

import com.leic52dg17.chimp.core.repositories.channel.database.ChannelDAO
import com.leic52dg17.chimp.core.repositories.channel.model.ChannelEntity
import com.leic52dg17.chimp.core.repositories.messages.IMessageRepository
import com.leic52dg17.chimp.domain.model.channel.Channel

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

    override suspend fun removeChannel(channel: Channel) {
        val channelEntity = ChannelEntity(
            channel.channelId,
            channel.displayName,
            channel.ownerId,
            channel.isPrivate,
            channel.channelIconUrl
        )
        channelDao.delete(channelEntity)
    }

    override suspend fun getStoredChannels(): List<Channel> {
        val channelEntities = channelDao.getAll()
        val channels: MutableList<Channel> = emptyList<Channel>().toMutableList()

        for (channelEntity in channelEntities) {
            val channel = Channel(
                channelEntity.channelId,
                channelEntity.displayName,
                channelEntity.ownerId,
                emptyList(),
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