package com.leic52dg17.chimp.ui.viewmodels.screen.main.functions

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.launch

class CacheCallbacks(private val viewModel: MainViewSelectorViewModel) {
    fun channelSuccessCallback(newChannels: List<Channel>) {
        viewModel.viewModelScope.launch {
            Log.i(
                "CHANNEL_CALLBACK",
                "Channel success callback reached with channels -> $newChannels"
            )
            when (val prevState = viewModel.stateFlow.value) {
                is MainViewSelectorState.GettingSubscribedChannels -> {
                    Log.i("CHANNEL_CALLBACK", "CHANNEL CALLBACK - Coming from $prevState")
                    viewModel.transition(
                        MainViewSelectorState.SubscribedChannels(
                            authenticatedUser = viewModel.userInfoRepository.authenticatedUser.first(),
                            channels = newChannels
                        )
                    )
                }

                is MainViewSelectorState.SubscribedChannels -> {
                    Log.i("CHANNEL_CALLBACK", "CHANNEL CALLBACK - Coming from $prevState")
                    viewModel.transition(
                        MainViewSelectorState.SubscribedChannels(
                            authenticatedUser = prevState.authenticatedUser,
                            channels = newChannels
                        )
                    )
                }

                is MainViewSelectorState.CreatingChannel -> {
                    Log.i("CHANNEL_CALLBACK", "CHANNEL CALLBACK - Coming from $prevState")
                    viewModel.transition(
                        MainViewSelectorState.SubscribedChannels(
                            authenticatedUser = prevState.authenticatedUser,
                            channels = viewModel.getSortedChannels()
                        )
                    )
                }

                else -> {
                    Log.i(
                        "CHANNEL_CALLBACK",
                        "(UNREGISTERED) CHANNEL CALLBACK - Coming from $prevState"
                    )
                }
            }
        }
    }

    fun channelErrorCallback(errorMessage: String) {
        viewModel.viewModelScope.launch {
            val prevState = viewModel.stateFlow.value
            viewModel.transition(
                MainViewSelectorState.Error(errorMessage) { viewModel.transition(prevState) }
            )
        }
    }


    fun messageSuccessCallback(newMessages: List<Message>) {
        viewModel.viewModelScope.launch {
            Log.i(
                "MESSAGE_CALLBACK",
                "Message success callback reached with messages -> $newMessages"
            )
            when (val prevState = viewModel.stateFlow.value) {
                is MainViewSelectorState.ChannelMessages -> {
                    Log.i("MESSAGE_CALLBACK", "Coming from $prevState")
                    viewModel.transition(
                        MainViewSelectorState.ChannelMessages(
                            channel = prevState.channel?.copy(messages = newMessages.filter { it.channelId == prevState.channel.channelId }),
                            authenticatedUser = prevState.authenticatedUser,
                        )
                    )
                }

                is MainViewSelectorState.SubscribedChannels -> {
                    Log.i("MESSAGE_CALLBACK", "Coming from $prevState")
                    val newChannels = viewModel.getSortedChannels()
                    val newState = MainViewSelectorState.SubscribedChannels(
                        channels = newChannels,
                        authenticatedUser = prevState.authenticatedUser
                    )
                    Log.i(
                        "MESSAGE_CALLBACK",
                        "New state latest messages = ${newState.channels?.map { it.messages.lastOrNull()?.text }}"
                    )
                    Log.i(
                        "MESSAGE_CALLBACK",
                        "New channel messages -> ${newChannels?.map { it.messages.lastOrNull() }}"
                    )
                    viewModel.transition(
                        newState
                    )
                }

                else -> {
                    Log.i("MESSAGE_CALLBACK", "Coming from $prevState")
                }
            }
        }
    }

    fun messageErrorCallback(errorMessage: String) {
        viewModel.viewModelScope.launch {
            val prevState = viewModel.stateFlow.value
            viewModel.transition(
                MainViewSelectorState.Error(errorMessage) { viewModel.transition(prevState) }
            )
        }
    }
}