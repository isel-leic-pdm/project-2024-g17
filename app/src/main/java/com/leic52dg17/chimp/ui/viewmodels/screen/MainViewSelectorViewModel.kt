package com.leic52dg17.chimp.ui.viewmodels.screen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.core.shared.SharedPreferencesHelper
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.common.Failure
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import com.leic52dg17.chimp.domain.model.common.Success
import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.message.IMessageService
import com.leic52dg17.chimp.http.services.sse.ISSEService
import com.leic52dg17.chimp.http.services.sse.events.Events
import com.leic52dg17.chimp.http.services.user.IUserService
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import kotlinx.coroutines.launch

class MainViewSelectorViewModel(
    private val channelService: IChannelService,
    private val messageService: IMessageService,
    private val userService: IUserService,
    private val sseService: ISSEService,
    private val context: Context,
    initialState: MainViewSelectorState = MainViewSelectorState.Initialized(
        SharedPreferencesHelper.getAuthenticatedUser(
            context
        )
    )
) : ViewModel() {
    var state: MainViewSelectorState by mutableStateOf(initialState)

    fun transition(newState: MainViewSelectorState) {
        state = newState
    }

    /**
     *  Channel functions
     */
    private fun handleIncomingEvent(event: Events) {
        when (event) {
            is Events.ChannelMessage -> {
                when (state) {
                    is MainViewSelectorState.SubscribedChannels -> {
                        val updatedChannels = (state as MainViewSelectorState.SubscribedChannels).channels?.map { channel ->
                            if (channel.channelId == event.message.channelId) {
                                channel.copy(messages = channel.messages + event.message)
                            } else {
                                channel
                            }
                        }
                        transition((state as MainViewSelectorState.SubscribedChannels).copy(channels = updatedChannels))
                    }

                    is MainViewSelectorState.ChannelMessages -> {
                        val channel = (state as MainViewSelectorState.ChannelMessages).channel
                        if(channel == null) {
                            transition((state as MainViewSelectorState.ChannelMessages).copy(showDialog = true, dialogMessage = ErrorMessages.CHANNEL_NOT_FOUND))
                        } else {
                            val updatedChannel: Channel = if(event.message.channelId == channel.channelId) {
                                channel.copy(messages = channel.messages + event.message)
                            } else {
                                channel
                            }
                            transition((state as MainViewSelectorState.ChannelMessages).copy(channel = updatedChannel))
                        }
                    }
                    else -> {}
                }
            }

            is Events.Invitation -> TODO()
        }
    }

    fun getEventStream() {
        viewModelScope.launch {
            sseService.eventFlow.collect { event ->
                handleIncomingEvent(event)
                Log.i("MainViewSelectorViewModel", "Received event: $event")
            }
        }
    }

    private fun stopEventStream() {
        sseService.stopListening()
    }

    fun loadChannelMessages() {
        val channel = (state as? MainViewSelectorState.ChannelMessages)?.channel
        transition(MainViewSelectorState.GettingChannelMessages(channel))
        viewModelScope.launch {
            val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(context)
            val currentUser = authenticatedUser?.user

            if (currentUser == null) {
                transition(
                    MainViewSelectorState.SubscribedChannels(
                        true,
                        ErrorMessages.AUTHENTICATED_USER_NULL,
                        authenticatedUser = authenticatedUser
                    )
                )
                return@launch
            }
            if (channel == null) {
                transition(
                    MainViewSelectorState.SubscribedChannels(
                        true,
                        ErrorMessages.CHANNEL_NOT_FOUND,
                        authenticatedUser = authenticatedUser
                    )
                )
                return@launch
            } else transition(
                MainViewSelectorState.ChannelMessages(
                    false,
                    channel = channel,
                    authenticatedUser = authenticatedUser
                )
            )
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
                    dialogMessage = ErrorMessages.CHANNEL_NOT_FOUND,
                    authenticatedUser = currentUser
                )
            )
            else if (currentUser == null) transition(
                MainViewSelectorState.ChannelInfo(
                    showDialog = true,
                    dialogMessage = ErrorMessages.AUTHENTICATED_USER_NULL,
                    authenticatedUser = null
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
        transition(MainViewSelectorState.Loading)
        viewModelScope.launch {
            val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(context)
            val currentUser = authenticatedUser?.user
            if (currentUser == null) {
                transition(
                    MainViewSelectorState.SubscribedChannels(
                        true,
                        ErrorMessages.AUTHENTICATED_USER_NULL,
                        authenticatedUser = null
                    )
                )
                return@launch
            }
            val channels = channelService.getUserSubscribedChannels(currentUser.id)
            transition(
                MainViewSelectorState.SubscribedChannels(
                    false,
                    channels = channels,
                    authenticatedUser = authenticatedUser
                )
            )
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
                    ErrorMessages.AUTHENTICATED_USER_NULL,
                    authenticatedUser = null
                )
            )
            return
        }
        if (state is MainViewSelectorState.CreateChannel) {
            transition(MainViewSelectorState.CreatingChannel)
            viewModelScope.launch {
                val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(context)

                if (authenticatedUser == null) {
                    transition(
                        MainViewSelectorState.CreateChannel(
                            true,
                            ErrorMessages.AUTHENTICATED_USER_NULL,
                            authenticatedUser = null
                        )
                    )
                    return@launch
                }
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
                                result.value.message,
                                authenticatedUser = authenticatedUser
                            )
                        )

                        is Success -> {
                            transition(MainViewSelectorState.Loading)
                            val channels =
                                channelService.getUserSubscribedChannels(currentUser.id)
                            transition(
                                MainViewSelectorState.SubscribedChannels(
                                    false,
                                    channels = channels,
                                    authenticatedUser = authenticatedUser
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    transition(
                        MainViewSelectorState.CreateChannel(
                            true,
                            e.message,
                            authenticatedUser = authenticatedUser
                        )
                    )
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
                    showDialog = true,
                    dialogMessage = ErrorMessages.AUTHENTICATED_USER_NULL,
                    authenticatedUser = null
                )
            )
            return
        }

        viewModelScope.launch {
            when (val result = channelService.removeUserFromChannel(userId, channelId)) {
                is Failure -> transition(
                    MainViewSelectorState.ChannelInfo(
                        showDialog = true,
                        dialogMessage = result.value.message,
                        authenticatedUser = currentUser
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

    fun inviteUserToChannel(userId: Int, channelId: Int, permission: PermissionLevel) {
        Log.i(TAG, "Inviting user $userId to channel $channelId")
        val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(context)
        val currentUser = authenticatedUser?.user

        if (currentUser == null) {
            MainViewSelectorState.ChannelInfo(
                showDialog = true,
                dialogMessage = ErrorMessages.AUTHENTICATED_USER_NULL,
                authenticatedUser = authenticatedUser
            )
            return
        }

        viewModelScope.launch {
            try {
                channelService.createChannelInvitation(
                    channelId,
                    currentUser.id,
                    userId,
                    permission
                )
                val channel = channelService.getChannelById(channelId)
                if (channel != null) {
                    transition(
                        MainViewSelectorState.InvitingUsers(
                            channel,
                            true,
                            "User invited!",
                            authenticatedUser = authenticatedUser
                        )
                    )
                }
            } catch (e: Exception) {
                transition(
                    MainViewSelectorState.SubscribedChannels(
                        true,
                        e.message,
                        authenticatedUser = authenticatedUser
                    )
                )
            }
        }
    }

    fun leaveChannel(
        userId: Int?,
        channel: Channel
    ) {
        val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(context)
        val currentUser = authenticatedUser?.user
        if (currentUser != null) {
            transition(
                MainViewSelectorState.ChannelInfo(
                    showDialog = true,
                    dialogMessage = ErrorMessages.AUTHENTICATED_USER_NULL,
                    authenticatedUser = authenticatedUser
                )
            )

            if (userId == null) {
                transition(
                    MainViewSelectorState.ChannelInfo(
                        showDialog = true, dialogMessage = ErrorMessages.USER_NOT_FOUND,
                        authenticatedUser = authenticatedUser
                    )
                )
                return
            }


            viewModelScope.launch {
                when (val result =
                    channelService.removeUserFromChannel(userId, channel.channelId)) {
                    is Failure -> {
                        transition(
                            MainViewSelectorState.ChannelInfo(
                                showDialog = true,
                                dialogMessage = result.value.message,
                                authenticatedUser = authenticatedUser
                            )
                        )
                        return@launch
                    }

                    is Success -> {
                        val channels = channelService.getUserSubscribedChannels(userId)
                        transition(
                            MainViewSelectorState.SubscribedChannels(
                                channels = channels,
                                authenticatedUser = authenticatedUser
                            )
                        )
                        return@launch
                    }
                }
            }
        }
    }

    /**
     * De-authentication functions
     */
    fun logout(onLogout: () -> Unit) {
        transition(MainViewSelectorState.Loading)
        SharedPreferencesHelper.logout(context)
        stopEventStream()
        onLogout()
    }

    /**
     * User functions
     */
    fun getUserProfile(id: Int) {
        Log.i(TAG, "Getting user profile for user with ID: $id")
        transition(MainViewSelectorState.Loading)
        viewModelScope.launch {
            val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(context)
            try {
                val user = userService.getUserById(id)
                if (user != null) {
                    Log.i(TAG, "Fetched user profile for user with ID: ${user.id}")
                    transition(
                        MainViewSelectorState.UserInfo(
                            user,
                            authenticatedUser = authenticatedUser
                        )
                    )
                } else {
                    Log.e(TAG, "Error fetching user profile for user with ID: $id")
                    transition(
                        MainViewSelectorState.SubscribedChannels(
                            true,
                            "Unexpected error occurred when navigating to user info",
                            authenticatedUser = authenticatedUser
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "Exception thrown in getUserProfile with user id $id")
                transition(
                    MainViewSelectorState.SubscribedChannels(
                        true,
                        e.message,
                        authenticatedUser = authenticatedUser
                    )
                )
            }
        }
    }

    /**
     * Message functions
     */
    fun sendMessage(channelId: Int, messageText: String) {
        transition(MainViewSelectorState.Loading)
        val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(context)
        val currentUser = authenticatedUser?.user
        viewModelScope.launch {
            val channel = channelService.getChannelById(channelId)
            if (channel == null) {
                transition(
                    MainViewSelectorState.ChannelMessages(
                        true,
                        ErrorMessages.CHANNEL_NOT_FOUND,
                        authenticatedUser = authenticatedUser
                    )
                )
            }
            if (currentUser == null) {
                transition(
                    MainViewSelectorState.ChannelMessages(
                        true,
                        ErrorMessages.AUTHENTICATED_USER_NULL,
                        channel,
                        authenticatedUser = authenticatedUser
                    )
                )
                return@launch
            }
            val createMessageResult =
                messageService.createMessageInChannel(
                    messageText,
                    channelId,
                    currentUser.id
                )

            when (createMessageResult) {
                is Failure -> {
                    transition(
                        MainViewSelectorState.ChannelMessages(
                            true,
                            createMessageResult.value.message,
                            channel,
                            authenticatedUser = authenticatedUser
                        )
                    )
                }

                is Success -> {
                    if (channel != null) {
                        val updatedChannel = channelService.getChannelById(channel.channelId)
                        transition(
                            MainViewSelectorState.ChannelMessages(
                                false,
                                channel = updatedChannel,
                                authenticatedUser = authenticatedUser
                            )
                        )
                    } else {
                        transition(
                            MainViewSelectorState.ChannelMessages(
                                true,
                                dialogMessage = ErrorMessages.CHANNEL_NOT_FOUND,
                                authenticatedUser = authenticatedUser
                            )
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "MAIN_VIEW_SELECTOR_VIEW_MODEL"
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewSelectorViewModelFactory(
    private val channelService: IChannelService,
    private val messageService: IMessageService,
    private val userService: IUserService,
    private val sseService: ISSEService,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewSelectorViewModel(
            channelService,
            messageService,
            userService,
            sseService,
            context
        ) as T
    }
}