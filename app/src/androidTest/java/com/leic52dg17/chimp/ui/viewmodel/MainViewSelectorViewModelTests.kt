package com.leic52dg17.chimp.ui.viewmodel

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.http.services.channel.implementations.FakeChannelService
import com.leic52dg17.chimp.http.services.message.implementations.FakeMessageService
import com.leic52dg17.chimp.http.services.user.implementations.FakeUserService
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.MainViewSelectorViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class MainViewSelectorViewModelTests {
    private lateinit var vm: MainViewSelectorViewModel
    private val authenticatedUser = AuthenticatedUser()
    private val channelService = FakeChannelService()
    private val messageService = FakeMessageService()
    private val userService = FakeUserService()
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setup() {
        vm = MainViewSelectorViewModel(
            channelService,
            messageService,
            userService,
            context
        )
    }

    @Test
    fun default_state_is_subscribed_channels() {
        assert(vm.state is MainViewSelectorState.SubscribedChannels)
    }

    @Test
    fun can_transition_state() {
        assert(vm.state is MainViewSelectorState.SubscribedChannels)
        vm.transition(MainViewSelectorState.ChannelMessages(authenticatedUser = authenticatedUser))
        assert(vm.state is MainViewSelectorState.ChannelMessages)
    }

    @Test
    fun load_channel_messages_transitions_to_loading_state() {
        assert(vm.state is MainViewSelectorState.SubscribedChannels)
        runBlocking {
            vm.loadSubscribedChannels()
            assert(vm.state is MainViewSelectorState.Loading)
        }
    }
}