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
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.common.ServiceException
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
    private val onLogout: () -> Unit,
    initialState: MainViewSelectorState = MainViewSelectorState.Initialized(
        SharedPreferencesHelper.getAuthenticatedUser(
            context
        )
    ),
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
                        val updatedChannels =
                            (state as MainViewSelectorState.SubscribedChannels).channels?.map { channel ->
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
                        if (channel == null) {
                            transition(
                                (state as MainViewSelectorState.ChannelMessages).copy(
                                    showDialog = true,
                                    dialogMessage = ErrorMessages.CHANNEL_NOT_FOUND
                                )
                            )
                        } else {
                            val updatedChannel: Channel =
                                if (event.message.channelId == channel.channelId) {
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

    fun debugLogout() {
        SharedPreferencesHelper.logout(context)
    }

    fun loadChannelMessages() {
        val channel = (state as? MainViewSelectorState.ChannelMessages)?.channel
        transition(MainViewSelectorState.GettingChannelMessages(channel))
        viewModelScope.launch {
            val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(context)
            val currentUser = authenticatedUser?.user
            try {
                if (currentUser == null) {
                    transition(MainViewSelectorState.Unauthenticated)
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
                } else {
                    val channelMessages = mutableListOf<Message>()
                    val storedMessages =
                        SharedPreferencesHelper.getMessages(context, channel.channelId)
                    if (storedMessages.isEmpty()) {
                        val newMessages = messageService.getChannelMessages(channel.channelId)
                        SharedPreferencesHelper.storeMessages(
                            context,
                            channel.channelId,
                            newMessages
                        )
                        channelMessages.addAll(newMessages)
                    }
                    transition(
                        MainViewSelectorState.ChannelMessages(
                            false,
                            channel = channel.copy(messages = channelMessages),
                            authenticatedUser = authenticatedUser
                        )
                    )
                }
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message!!} : Current State -> $state")
                transition(
                    MainViewSelectorState.SubscribedChannels(
                        showDialog = true,
                        dialogMessage = e.message,
                        authenticatedUser = authenticatedUser
                    )
                )
            }
        }
    }

    fun loadChannelInfo() {
        val channel = (state as? MainViewSelectorState.ChannelInfo)?.channel
        val currentUser = SharedPreferencesHelper.getAuthenticatedUser(context)
        transition(MainViewSelectorState.GettingChannelInfo)
        viewModelScope.launch {
            try {
                if (channel == null) transition(
                    MainViewSelectorState.ChannelInfo(
                        showDialog = true,
                        dialogMessage = ErrorMessages.CHANNEL_NOT_FOUND,
                        authenticatedUser = currentUser
                    )
                )
                else if (currentUser == null) {
                    transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                } else transition(
                    MainViewSelectorState.ChannelInfo(
                        channel = channel,
                        authenticatedUser = currentUser
                    )
                )
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message!!} : Current State -> $state")
                transition(
                    MainViewSelectorState.ChannelInfo(
                        showDialog = true,
                        dialogMessage = e.message,
                        authenticatedUser = currentUser
                    )
                )
            }
        }

    }

    fun loadSubscribedChannels() {
        transition(MainViewSelectorState.Loading)
        viewModelScope.launch {
            try {
                val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(context)
                Log.d(TAG, "=== COULD RETRIEVE AUTH USER : $authenticatedUser")
                val currentUser = authenticatedUser?.user
                if (currentUser == null) {
                    transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                }
                val channelsWithoutMessagesOrUsers =
                    channelService.getUserSubscribedChannels(currentUser.id)
                val channels = mutableListOf<Channel>()

                for (channel in channelsWithoutMessagesOrUsers) {
                    Log.i("DEBUG", channel.displayName)
                    val messages = mutableListOf<Message>()
                    val storedMessages =
                        SharedPreferencesHelper.getMessages(context, channel.channelId)
                    if (storedMessages.isEmpty()) {
                        val channelMessages = messageService.getChannelMessages(channel.channelId)
                        messages.addAll(channelMessages)
                    } else {
                        messages.addAll(storedMessages)
                    }
                    val channelUsers = userService.getChannelUsers(channel.channelId)

                    val toAdd = channel.copy(messages = messages, users = channelUsers)
                    channels.add(toAdd)
                }

                Log.i(TAG, "=====DEBUG=====\n GOT CHANNELS: $channels")
                transition(
                    MainViewSelectorState.SubscribedChannels(
                        false,
                        channels = channels,
                        authenticatedUser = authenticatedUser
                    )
                )
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message!!} : Current State -> $state")
                transition(
                    MainViewSelectorState.SubscribedChannels(
                        true,
                        e.message,
                        authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(context)
                    )
                )
            }
        }
    }

    fun createChannel(
        ownerId: Int,
        name: String,
        isPrivate: Boolean,
        channelIconUrl: String
    ) {
        val currentUser = SharedPreferencesHelper.getAuthenticatedUser(context)?.user
        if (currentUser == null) {
            transition(MainViewSelectorState.Unauthenticated)
            return
        }
        if (state is MainViewSelectorState.CreateChannel) {
            transition(MainViewSelectorState.CreatingChannel)
            viewModelScope.launch {
                val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(context)

                if (authenticatedUser == null) {
                    transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                }
                try {
                    channelService.createChannel(
                        ownerId,
                        name,
                        isPrivate,
                        channelIconUrl
                    )
                    transition(MainViewSelectorState.Loading)
                    val channels = channelService.getUserSubscribedChannels(currentUser.id)
                    transition(
                        MainViewSelectorState.SubscribedChannels(
                            false,
                            channels = channels,
                            authenticatedUser = authenticatedUser
                        )
                    )
                } catch (e: ServiceException) {
                    Log.e(TAG, "${e.message!!} : Current State -> $state")
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
            transition(MainViewSelectorState.Unauthenticated)
            return
        }

        viewModelScope.launch {
            try {
                channelService.removeUserFromChannel(userId, channelId)
                transition(
                    MainViewSelectorState.ChannelInfo(
                        channel = channelService.getChannelById(channelId),
                        authenticatedUser = currentUser
                    )
                )
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message!!} : Current State -> $state")
                transition(
                    MainViewSelectorState.ChannelInfo(
                        showDialog = true,
                        dialogMessage = e.message,
                        channel = channelService.getChannelById(channelId),
                        authenticatedUser = currentUser
                    )
                )
            }
        }
    }

    fun inviteUserToChannel(userId: Int, channelId: Int, permission: PermissionLevel) {
        Log.i(TAG, "Inviting user $userId to channel $channelId")
        val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(context)
        val currentUser = authenticatedUser?.user

        if (currentUser == null) {
            transition(MainViewSelectorState.Unauthenticated)
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
                transition(
                    MainViewSelectorState.InvitingUsers(
                        channel,
                        true,
                        "User invited!",
                        authenticatedUser = authenticatedUser
                    )
                )
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message!!} : Current State -> $state")
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
        if (currentUser == null) {
            transition(MainViewSelectorState.Unauthenticated)
            return
        }
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
            try {
                channelService.removeUserFromChannel(userId, channel.channelId)
                val channels = channelService.getUserSubscribedChannels(userId)
                transition(
                    MainViewSelectorState.SubscribedChannels(
                        channels = channels,
                        authenticatedUser = authenticatedUser
                    )
                )
                return@launch
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message!!} : Current State -> $state")
                val channels = channelService.getUserSubscribedChannels(userId)
                transition(
                    MainViewSelectorState.SubscribedChannels(
                        showDialog = true,
                        dialogMessage = e.message,
                        channels = channels,
                        authenticatedUser = authenticatedUser
                    )
                )
            }
        }
    }

    /**
     * De-authentication functions
     */
    fun logout() {
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
        val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(context)
        val currentUser = authenticatedUser?.user
        viewModelScope.launch {
            try {
                if (currentUser == null) {
                    transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                }
                messageService.createMessageInChannel(
                    messageText,
                    channelId,
                    currentUser.id
                )
                val channelWithoutMessages = channelService.getChannelById(channelId)
                val channelMessages = messageService.getChannelMessages(channelId)
                val channelUsers = userService.getChannelUsers(channelId)
                val updatedChannel =
                    channelWithoutMessages.copy(messages = channelMessages, users = channelUsers)
                transition(
                    MainViewSelectorState.ChannelMessages(
                        false,
                        channel = updatedChannel,
                        authenticatedUser = authenticatedUser
                    )
                )
            } catch (e: ServiceException) {
                transition(
                    MainViewSelectorState.SubscribedChannels(
                        true,
                        dialogMessage = e.message,
                        channels = null,
                        authenticatedUser = authenticatedUser
                    )
                )
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
    private val context: Context,
    private val onLogout: () -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewSelectorViewModel(
            channelService,
            messageService,
            userService,
            sseService,
            context,
            onLogout
        ) as T
    }
}