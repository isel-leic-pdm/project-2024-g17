package com.leic52dg17.chimp.ui.viewmodels.screen.main.functions

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import kotlinx.coroutines.launch

class CacheCallbacks(private val viewModel: MainViewSelectorViewModel) {
    fun channelSuccessCallback(newChannels: List<Channel>) {
        viewModel.viewModelScope.launch {
            Log.i("CALLBACKS", "Channel success callback reached")
            when (val prevState = viewModel.stateFlow.value) {
                is MainViewSelectorState.SubscribedChannels -> {
                    Log.i("CALLBACKS", "CHANNEL CALLBACK - Coming from $prevState")
                    viewModel.transition(
                        MainViewSelectorState.SubscribedChannels(
                            authenticatedUser = prevState.authenticatedUser,
                            channels = newChannels
                        )
                    )
                }

                is MainViewSelectorState.CreatingChannel -> {
                    Log.i("CALLBACKS", "CHANNEL CALLBACK - Coming from $prevState")
                    viewModel.transition(
                        MainViewSelectorState.SubscribedChannels(
                            authenticatedUser = prevState.authenticatedUser,
                            channels = newChannels
                        )
                    )
                }
                else -> {
                    Log.i("CALLBACKS", "(UNREGISTERED) CHANNEL CALLBACK - Coming from $prevState")
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
            Log.i("CALLBACKS", "Message success callback reached")
            when (val prevState = viewModel.stateFlow.value) {
                is MainViewSelectorState.ChannelMessages -> {
                    viewModel.transition(
                        MainViewSelectorState.ChannelMessages(
                            channel = prevState.channel?.copy(messages = newMessages.filter { it.channelId == prevState.channel.channelId }),
                            authenticatedUser = prevState.authenticatedUser,
                        )
                    )
                }
                is MainViewSelectorState.SubscribedChannels -> {
                    val newChannels = prevState.channels?.map { channel ->
                        val messages = newMessages.filter { it.channelId == channel.channelId }
                        channel.copy(messages = messages)
                    }
                    viewModel.transition(
                        MainViewSelectorState.SubscribedChannels(
                            channels = newChannels?.sortedByDescending { it.messages.lastOrNull()?.createdAt },
                            authenticatedUser = prevState.authenticatedUser
                        )
                    )
                }
                else -> {
                    Log.i("CALLBACKS", "Coming from $prevState")
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