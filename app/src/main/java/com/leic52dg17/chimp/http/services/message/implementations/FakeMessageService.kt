package com.leic52dg17.chimp.http.services.message.implementations

import com.leic52dg17.chimp.http.services.fake.FakeData
import com.leic52dg17.chimp.http.services.message.IMessageService
import com.leic52dg17.chimp.http.services.message.results.GetChannelMessagesError
import com.leic52dg17.chimp.http.services.message.results.GetChannelMessagesResult
import com.leic52dg17.chimp.model.common.ErrorMessages
import com.leic52dg17.chimp.model.common.failure
import com.leic52dg17.chimp.model.common.success

class FakeMessageService : IMessageService {
    override suspend fun getChannelMessages(channelId: Int): GetChannelMessagesResult {
        val channel = FakeData.channels.firstOrNull { it.channelId == channelId }
        if (channel == null) return failure(GetChannelMessagesError(ErrorMessages.CHANNEL_NOT_FOUND))
        return success(channel.messages)
    }
}