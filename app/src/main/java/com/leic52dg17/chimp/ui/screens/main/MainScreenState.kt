package com.leic52dg17.chimp.ui.screens.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import com.leic52dg17.chimp.model.channel.Channel

sealed interface MainScreenState {
    data class SubscribedChannels(val showDialog: Boolean, val dialogMessage: String? = null, val channels: List<Channel>? = null): MainScreenState
    data class CreateChannel(val showDialog: Boolean, val dialogMessage: String? = null): MainScreenState
    data class ChannelMessages(val showDialog: Boolean, val dialogMessage: String? = null, val channel: Channel? = null): MainScreenState
    data class GettingChannelMessages(val channel: Channel? = null): MainScreenState
    data object CreatingChannel: MainScreenState
    data object GettingChannels: MainScreenState
    data class ChannelInfo(val channel: Channel): MainScreenState

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