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
import com.leic52dg17.chimp.model.channel.Channel
import com.leic52dg17.chimp.model.common.ErrorMessages
import com.leic52dg17.chimp.model.common.Failure
import com.leic52dg17.chimp.model.common.Success
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import kotlinx.coroutines.launch
import java.util.logging.Logger

class MainViewSelectorViewModel(
    private val channelService: IChannelService,
    private val messageService: IMessageService,
    private val context: Context
) : ViewModel() {
    var state: MainViewSelectorState by mutableStateOf(
        MainViewSelectorState.SubscribedChannels(
            false
        )
    )

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
            if (channel == null) {
                transition(
                    MainViewSelectorState.SubscribedChannels(
                        true,
                        ErrorMessages.CHANNEL_NOT_FOUND
                    )
                )
            }
            else transition(MainViewSelectorState.ChannelMessages(false, channel = channel))
        }
    }

    fun loadChannelInfo() {
        val channel = (state as? MainViewSelectorState.ChannelInfo)?.channel
        val currentUser = SharedPreferencesHelper.getAuthenticatedUser(context)
        transition(MainViewSelectorState.GettingChannelInfo)
        viewModelScope.launch {
            if (channel == null) transition(
                MainViewSelectorState.ChannelInfo(
                    showDialog = true,
                    dialogMessage = ErrorMessages.CHANNEL_NOT_FOUND
                )
            )
            else if (currentUser == null) transition(
                MainViewSelectorState.ChannelInfo(
                    showDialog = true,
                    dialogMessage = ErrorMessages.AUTHENTICATED_USER_NULL
                )
            )
            else transition(
                MainViewSelectorState.ChannelInfo(
                    channel = channel,
                    authenticatedUser = currentUser
                )
            )
        }
    }

    fun loadSubscribedChannels() {
        transition(MainViewSelectorState.GettingChannels)
        viewModelScope.launch {
            val currentUser = SharedPreferencesHelper.getAuthenticatedUser(context)?.user
            if (currentUser == null) {
                transition(
                    MainViewSelectorState.SubscribedChannels(
                        true,
                        ErrorMessages.AUTHENTICATED_USER_NULL
                    )
                )
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
        if (currentUser == null) {
            transition(
                MainViewSelectorState.CreateChannel(
                    true,
                    ErrorMessages.AUTHENTICATED_USER_NULL
                )
            )
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
                            transition(MainViewSelectorState.GettingChannels)
                            val channels =
                                channelService.getUserSubscribedChannels(currentUser.userId)
                            transition(
                                MainViewSelectorState.SubscribedChannels(
                                    false,
                                    channels = channels
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    transition(MainViewSelectorState.CreateChannel(true, e.message))
                }
            }
        }
    }

    fun removeUserFromChannel(
        userId: Int,
        channelId: Int
    ) {
        val currentUser = SharedPreferencesHelper.getAuthenticatedUser(context)
        if (currentUser == null) {
            transition(
                MainViewSelectorState.ChannelInfo(
                    showDialog = true, dialogMessage = ErrorMessages.AUTHENTICATED_USER_NULL
                )
            )
            return
        }

        viewModelScope.launch {
            when (val result = channelService.removeUserFromChannel(userId, channelId)) {
                is Failure -> transition(
                    MainViewSelectorState.ChannelInfo(
                        showDialog = true,
                        dialogMessage = result.value.message
                    )
                )

                is Success -> {
                    transition(
                        MainViewSelectorState.ChannelInfo(
                            channel = channelService.getChannelById(channelId),
                            authenticatedUser = currentUser
                        )
                    )
                }
            }
        }
    }

    fun leaveChannel(
        userId: Int?,
        channel: Channel
    ) {
        val currentUser = SharedPreferencesHelper.getAuthenticatedUser(context)?.user
        if(currentUser == null) {
            transition(MainViewSelectorState.ChannelInfo(showDialog = true, dialogMessage = ErrorMessages.AUTHENTICATED_USER_NULL))
            return
        }

        if(userId == null) {
            transition(
                MainViewSelectorState.ChannelInfo(
                    showDialog = true, dialogMessage = ErrorMessages.USER_NOT_FOUND
                )
            )
            return
        }

        viewModelScope.launch {
            logger.info("Entered!!!!")
            val channels = channelService.getUserSubscribedChannels(userId)
            when(val result = channelService.removeUserFromChannel(userId, channel.channelId)) {
                is Failure -> {
                    transition(
                        MainViewSelectorState.ChannelInfo(
                            showDialog = true,
                            dialogMessage = result.value.message
                        )
                    )
                    return@launch
                }
                is Success -> {
                    transition(
                        MainViewSelectorState.SubscribedChannels(
                            channels = channels
                        )
                    )
                    return@launch
                }
            }
        }
    }

    companion object {
        val logger = Logger.getLogger("MAIN_VIEW_MODEL")
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewSelectorViewModelFactory(
    private val channelService: IChannelService,
    private val messageService: IMessageService,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewSelectorViewModel(
            channelService,
            messageService,
            context
        ) as T
    }
}