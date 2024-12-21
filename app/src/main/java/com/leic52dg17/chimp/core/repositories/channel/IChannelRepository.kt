package com.leic52dg17.chimp.core.repositories.channel

import com.leic52dg17.chimp.domain.model.channel.Channel

interface IChannelRepository {
    suspend fun storeChannels(channels: List<Channel>)
    suspend fun removeChannel(channel: Channel)
    suspend fun getStoredChannels(): List<Channel>
    suspend fun getDifferences(old: List<Channel>, new: List<Channel>): List<Channel>
    suspend fun isUpdateDue(oldCache: List<Channel>, newCache: List<Channel>): Boolean
}