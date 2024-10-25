package com.leic52dg17.chimp.ui.viewmodels.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.model.channel.Channel
import com.leic52dg17.chimp.model.common.Failure
import com.leic52dg17.chimp.model.common.Success
import com.leic52dg17.chimp.model.user.User
import com.leic52dg17.chimp.ui.screens.main.MainScreenState
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val channelService: IChannelService
) : ViewModel() {
    var state: MainScreenState by mutableStateOf(MainScreenState.SubscribedChannels(false))

    val mockUser = AuthenticatedUser(
        "example_token",
        User(
            1,
            "username1",
            "User 1"
        )
    )

    // HAS TO BE CHANGED ONCE LOGIN WORKS
    var authenticatedUser: AuthenticatedUser by mutableStateOf(mockUser)

    var currentUserSubscribedChannels: List<Channel>? by mutableStateOf(null)

    fun transition(newState: MainScreenState) {
        state = newState
    }

    /**
     *  Channel functions
     */

    fun getCurrentUserSubscribedChannels() {
        val userInfo = authenticatedUser.user
        if (userInfo == null) {
            currentUserSubscribedChannels = null
        } else {
            val userId = userInfo.userId
            transition(MainScreenState.GettingChannels)
            viewModelScope.launch {
                try {
                    currentUserSubscribedChannels = channelService.getUserSubscribedChannels(userId)
                    transition(MainScreenState.SubscribedChannels(false, null, currentUserSubscribedChannels))
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    fun createChannel(
        ownerId: Int,
        name: String,
        isPrivate: Boolean,
        channelIconUrl: String,
        channelIconContentDescription: String
    ) {
        if (state is MainScreenState.CreateChannel) {
            Log.i("MainScreenViewModel", "Creating channel")
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

                        is Success -> {
                            getCurrentUserSubscribedChannels()
                            transition(
                                MainScreenState.SubscribedChannels(
                                    false,
                                    null,
                                    currentUserSubscribedChannels
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    transition(MainScreenState.CreateChannel(true, e.message))
                }
            }
        }
    }

    fun getChannelById(channelId: Int) {
        viewModelScope.launch {
            try {
                val channel = channelService.getChannelInfo(channelId)
                if (channel != null) {
                    transition(MainScreenState.ChannelInfo(channel))
                } else {
                    transition(MainScreenState.SubscribedChannels(true, "Channel does not exist."))
                }
            } catch (e: Exception) {
                transition(MainScreenState.SubscribedChannels(true, e.message))
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