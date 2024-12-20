package com.leic52dg17.chimp.core.cache.common.manager

import com.leic52dg17.chimp.core.cache.channel.ChannelCacheManager
import com.leic52dg17.chimp.core.cache.message.MessageCacheManager
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.message.Message

class CommonCacheManager(
    private val channelCacheManager: ChannelCacheManager,
    private val messageCacheManager: MessageCacheManager
): ICommonCacheManager {
    override fun getChannels(): List<Channel> {
        val messages = messageCacheManager.getCachedMessage()
        val channelsWithoutMessages = channelCacheManager.getCachedChannels()
        return channelsWithoutMessages.map { channel ->
            val messagesForChannel = messages.filter { message -> message.channelId == channel.channelId }
            channel.copy(messages = messagesForChannel)
        }
    }

    override fun getMessages(): List<Message> = messageCacheManager.getCachedMessage()
}