package com.leic52dg17.chimp.ui.viewmodels.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.http.services.auth.IAuthenticationService
import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.channel.results.ChannelCreationResult
import com.leic52dg17.chimp.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.model.common.Failure
import com.leic52dg17.chimp.model.common.Success
import com.leic52dg17.chimp.ui.screens.main.MainScreenState
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val channelService: IChannelService
) : ViewModel() {
    var state: MainScreenState by mutableStateOf(MainScreenState.SubscribedChannels)

    var authenticatedUser: AuthenticatedUser by mutableStateOf(AuthenticatedUser())

    fun transition(newState: MainScreenState) {
        state = newState
    }

    fun createChannel(
        ownerId: Int,
        name: String,
        isPrivate: Boolean,
        channelIconUrl: String,
        channelIconContentDescription: String
    ) {
        if (state is MainScreenState.CreateChannel) {
            transition(MainScreenState.CreatingChannel)
            viewModelScope.launch {
                try {
                    val result = channelService.createChannel(
                        ownerId,
                        name,
                        isPrivate,
                        channelIconUrl,
                        channelIconContentDescription
                    )
                    when (result) {
                        is Failure -> transition(
                            MainScreenState.CreateChannel(
                                true,
                                result.value.message
                            )
                        )

                        is Success -> transition(MainScreenState.ChannelChat(result.value))
                    }
                } catch (e: Exception) {
                    MainScreenState.CreatingChannel
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MainScreenViewModelFactory(
    private val channelService: IChannelService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainScreenViewModel(
            channelService
        ) as T
    }
}