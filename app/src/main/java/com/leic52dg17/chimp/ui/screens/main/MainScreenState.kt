package com.leic52dg17.chimp.ui.screens.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver

sealed interface MainScreenState {
    data object SubscribedChannels: MainScreenState
    data class CreateChannel(val showDialog: Boolean, val dialogMessage: String? = null): MainScreenState
    data object CreatingChannel: MainScreenState
    data class ChannelChat(val channelId: Int): MainScreenState
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