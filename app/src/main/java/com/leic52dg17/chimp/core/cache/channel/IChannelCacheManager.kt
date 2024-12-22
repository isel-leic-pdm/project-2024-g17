package com.leic52dg17.chimp.core.cache.channel

import com.leic52dg17.chimp.domain.model.channel.Channel

interface IChannelCacheManager {
    fun forceUpdate(channel: Channel)
    fun removeFromCache(channel: Channel)
    suspend fun registerCallback(callback: (newChannels: List<Channel>) -> Unit)
    suspend fun registerErrorCallback(callback: (errorMessage: String) -> Unit)
    suspend fun unregisterCallbacks()
    suspend fun runCallback()
    suspend fun runErrorCallback(errorMessage: String)
    fun getCachedChannels(): List<Channel>
}