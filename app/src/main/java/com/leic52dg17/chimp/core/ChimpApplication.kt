package com.leic52dg17.chimp.core

import android.app.Application
import com.leic52dg17.chimp.http.services.auth.IAuthenticationService
import com.leic52dg17.chimp.http.services.auth.implementations.FakeAuthenticationService
import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.channel.implementations.FakeChannelService
import com.leic52dg17.chimp.http.services.message.IMessageService
import com.leic52dg17.chimp.http.services.message.implementations.FakeMessageService
import com.leic52dg17.chimp.http.services.user.IUserService
import com.leic52dg17.chimp.http.services.user.implementations.FakeUserService

const val TAG = "CHIMP"

interface DependenciesContainer {
    val channelService: IChannelService
    val messageService: IMessageService
    val authenticationService: IAuthenticationService
    val userService: IUserService
}

class ChimpApplication : Application(), DependenciesContainer {
    override val channelService: IChannelService by lazy {
        FakeChannelService()
    }
    override val messageService: IMessageService by lazy {
        FakeMessageService()
    }
    override val authenticationService: IAuthenticationService by lazy {
        FakeAuthenticationService()
    }
    override val userService: IUserService by lazy {
        FakeUserService()
    }
}