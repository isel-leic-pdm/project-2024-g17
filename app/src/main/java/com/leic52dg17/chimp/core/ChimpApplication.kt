package com.leic52dg17.chimp.core

import android.app.Application
import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.channel.implementations.FakeChannelService
import com.leic52dg17.chimp.http.services.message.IMessageService
import com.leic52dg17.chimp.http.services.message.implementations.FakeMessageService

const val TAG = "CHIMP"

interface DependenciesContainer {
    val channelService: IChannelService
    val messageService: IMessageService
}

class ChimpApplication : Application(), DependenciesContainer {
    override val channelService: IChannelService by lazy {
        FakeChannelService()
    }
    override val messageService: IMessageService by lazy {
        FakeMessageService()
    }
}