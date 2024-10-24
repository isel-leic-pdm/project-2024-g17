package com.leic52dg17.chimp.core

import android.app.Application
import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.channel.implementations.FakeChannelService

const val TAG = "CHIMP"

interface DependenciesContainer {
    val channelService: IChannelService
}

class ChimpApplication : Application(), DependenciesContainer {
    override val channelService: IChannelService by lazy {
        FakeChannelService()
    }
}