package com.leic52dg17.chimp.core.cache.channel

import com.leic52dg17.chimp.domain.model.channel.Channel

interface IChannelCacheManager {
    suspend fun startCollection(userId: Int)
    suspend fun stopCollection()
    suspend fun forceUpdate(userId: Int)
    suspend fun registerCallback(callback: (newChannels: List<Channel>) -> Unit)
    suspend fun registerErrorCallback(callback: (errorMessage: String) -> Unit)
    suspend fun unregisterCallbacks()
    suspend fun runCallback()
    suspend fun runErrorCallback(errorMessage: String)
}