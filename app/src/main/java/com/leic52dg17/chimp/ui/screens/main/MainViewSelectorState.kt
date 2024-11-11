package com.leic52dg17.chimp.ui.screens.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import com.leic52dg17.chimp.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.model.channel.Channel
import com.leic52dg17.chimp.model.user.User
import com.leic52dg17.chimp.ui.screens.main.nav.SelectedNavIcon

sealed interface MainViewSelectorState {
    data class SubscribedChannels(val showDialog: Boolean = false, val dialogMessage: String? = null, val channels: List<Channel>? = null, val authenticatedUser: AuthenticatedUser?): MainViewSelectorState
    data class CreateChannel(val showDialog: Boolean = false, val dialogMessage: String? = null, val authenticatedUser: AuthenticatedUser?): MainViewSelectorState
    data class ChannelMessages(val showDialog: Boolean = false, val dialogMessage: String? = null, val channel: Channel? = null, val authenticatedUser: AuthenticatedUser?): MainViewSelectorState
    data class GettingChannelMessages(val channel: Channel? = null): MainViewSelectorState
    data object CreatingChannel: MainViewSelectorState
    data class ChannelInfo(val channel: Channel? = null, val showDialog: Boolean = false, val dialogMessage: String? = null, val authenticatedUser: AuthenticatedUser? = null): MainViewSelectorState
    data object GettingChannelInfo: MainViewSelectorState
    data object Loading: MainViewSelectorState
    data class UserInfo(val user: User, val authenticatedUser: AuthenticatedUser?): MainViewSelectorState
    data object About: MainViewSelectorState
  
    companion object {
        val BooleanSaver: Saver<MutableState<Boolean>, *> = Saver(
            save = { it.value },
            restore = { mutableStateOf(it) }
        )
        val StringSaver: Saver<MutableState<String>, *> = Saver(
            save = { it.value },
            restore = { mutableStateOf(it) }
        )

        val SelectedNavIconSaver: Saver<MutableState<SelectedNavIcon>, *> = Saver(
            save = { it.value },
            restore = { mutableStateOf(it) }
        )

        val FunctionSaver: Saver<MutableState<() -> Unit>, *> = Saver(
            save = { it.value },
            restore = { mutableStateOf(it) }
        )
    }
}