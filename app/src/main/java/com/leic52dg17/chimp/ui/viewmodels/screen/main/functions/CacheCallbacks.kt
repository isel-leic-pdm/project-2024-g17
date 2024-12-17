package com.leic52dg17.chimp.ui.viewmodels.screen.main.functions

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class CacheCallbacks(private val viewModel: MainViewSelectorViewModel) {
    fun channelSuccessCallback(newChannels: List<Channel>) {
        viewModel.viewModelScope.launch {
            when(val prevState = viewModel.stateFlow.value) {
                is MainViewSelectorState.SubscribedChannels -> {
                    viewModel.transition(
                        MainViewSelectorState.SubscribedChannels(authenticatedUser = prevState.authenticatedUser, channels = newChannels)
                    )
                }
                else -> {}
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
}