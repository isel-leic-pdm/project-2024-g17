package com.leic52dg17.chimp.ui.viewmodels.screen

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.core.shared.SharedPreferencesHelper
import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.message.IMessageService
import com.leic52dg17.chimp.http.services.user.IUserService
import com.leic52dg17.chimp.model.common.ErrorMessages
import com.leic52dg17.chimp.model.common.Failure
import com.leic52dg17.chimp.model.common.Success
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import kotlinx.coroutines.launch

class MainViewSelectorViewModel(
    private val channelService: IChannelService,
    private val messageService: IMessageService,
    private val userService: IUserService,
    private val context: Context
) : ViewModel() {
    var state: MainViewSelectorState by mutableStateOf(MainViewSelectorState.SubscribedChannels(false))

    fun transition(newState: MainViewSelectorState) {
        state = newState
    }

    /**
     *  Channel functions
     */

    fun loadChannelMessages() {
        val channel = (state as? MainViewSelectorState.ChannelMessages)?.channel
        transition(MainViewSelectorState.GettingChannelMessages(channel))
        viewModelScope.launch {
            //val channel = channelService.getChannel(channelId)
            if(channel == null) transition(MainViewSelectorState.SubscribedChannels(true, ErrorMessages.CHANNEL_NOT_FOUND))
            else transition(MainViewSelectorState.ChannelMessages(false, channel = channel))
        }
    }

    fun loadSubscribedChannels() {
        transition(MainViewSelectorState.Loading)
        viewModelScope.launch {
            val currentUser = SharedPreferencesHelper.getAuthenticatedUser(context)?.user
            if(currentUser == null) {
                transition(MainViewSelectorState.SubscribedChannels(true, ErrorMessages.AUTHENTICATED_USER_NULL))
                return@launch
            }
            val channels = channelService.getUserSubscribedChannels(currentUser.userId)
            transition(MainViewSelectorState.SubscribedChannels(false, channels = channels))
        }
    }

    fun createChannel(
        ownerId: Int,
        name: String,
        isPrivate: Boolean,
        channelIconUrl: String,
        channelIconContentDescription: String
    ) {
        val currentUser = SharedPreferencesHelper.getAuthenticatedUser(context)?.user
        if(currentUser == null) {
            transition(MainViewSelectorState.CreateChannel(true, ErrorMessages.AUTHENTICATED_USER_NULL))
            return
        }
        if (state is MainViewSelectorState.CreateChannel) {
            transition(MainViewSelectorState.CreatingChannel)
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
                            MainViewSelectorState.CreateChannel(
                                true,
                                result.value.message
                            )
                        )

                        is Success -> {
                            transition(MainViewSelectorState.Loading)
                            val channels = channelService.getUserSubscribedChannels(currentUser.userId)
                            transition(MainViewSelectorState.SubscribedChannels(false, channels = channels))
                        }
                    }
                } catch (e: Exception) {
                    transition(MainViewSelectorState.CreateChannel(true, e.message))
                }
            }
        }
    }

    fun getChannelById(channelId: Int) {
        viewModelScope.launch {
            try {
                val channel = channelService.getChannelInfo(channelId)
                if (channel != null) {
                    transition(MainViewSelectorState.ChannelInfo(channel))
                } else {
                    transition(MainViewSelectorState.SubscribedChannels(true, "Channel does not exist."))
                }
            } catch (e: Exception) {
                transition(MainViewSelectorState.SubscribedChannels(true, e.message))
            }
        }
    }

    fun logout() {
        transition(MainViewSelectorState.Loading)
        SharedPreferencesHelper.logout(context)
    }

    fun getUserProfile(id: Int) {
        transition(MainViewSelectorState.Loading)
        viewModelScope.launch {
            try {
                val user = userService.getUserById(id)
                if (user != null) {
                    transition(MainViewSelectorState.UserInfo(user))
                } else {
                    transition(MainViewSelectorState.SubscribedChannels(true, "Unexpected error occurred when navigating to user info"))
                }
            } catch (e: Exception) {
                transition(MainViewSelectorState.SubscribedChannels(true, e.message))
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewSelectorViewModelFactory(
    private val channelService: IChannelService,
    private val messageService: IMessageService,
    private val userService: IUserService,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewSelectorViewModel(
            channelService,
            messageService,
            userService,
            context,
        ) as T
    }
}