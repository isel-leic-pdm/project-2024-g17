package com.leic52dg17.chimp.ui.screens.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import com.leic52dg17.chimp.model.channel.Channel
import com.leic52dg17.chimp.model.user.User

sealed interface MainViewSelectorState {
    data class SubscribedChannels(val showDialog: Boolean, val dialogMessage: String? = null, val channels: List<Channel>? = null): MainViewSelectorState
    data class CreateChannel(val showDialog: Boolean, val dialogMessage: String? = null): MainViewSelectorState
    data class ChannelMessages(val showDialog: Boolean, val dialogMessage: String? = null, val channel: Channel? = null): MainViewSelectorState
    data class GettingChannelMessages(val channel: Channel? = null): MainViewSelectorState
    data object CreatingChannel: MainViewSelectorState
    data object Loading: MainViewSelectorState
    data class ChannelInfo(val channel: Channel): MainViewSelectorState
    data class UserInfo(val user: User): MainViewSelectorState

    companion object {
        val BooleanSaver: Saver<MutableState<Boolean>, *> = Saver(
            save = { it.value },
            restore = { mutableStateOf(it) }
        )
        val StringSaver: Saver<MutableState<String>, *> = Saver(
            save = { it.value },
            restore = { mutableStateOf(it) }
        )
    }
}