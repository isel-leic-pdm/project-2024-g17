package com.leic52dg17.chimp.core.cache.common.manager

import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.message.Message

interface ICommonCacheManager {
    fun getChannels(): List<Channel>
    fun getMessages(): List<Message>
}