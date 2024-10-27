package com.leic52dg17.chimp.http.services.message

import com.leic52dg17.chimp.http.services.message.results.GetChannelMessagesResult

interface IMessageService {
    suspend fun getChannelMessages(channelId: Int): GetChannelMessagesResult
}