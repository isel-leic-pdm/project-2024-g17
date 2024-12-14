package com.leic52dg17.chimp.ui.viewmodels.screen.main.functions

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.core.shared.SharedPreferencesHelper
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.http.services.common.ServiceErrorTypes
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel.Companion.TAG
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class ChannelFunctions(private val viewModel: MainViewSelectorViewModel) {
    fun loadChannelMessages() {
        viewModel.viewModelScope.launch {
            val channel =
                (viewModel.stateFlow.last() as? MainViewSelectorState.ChannelMessages)?.channel
            viewModel.transition(MainViewSelectorState.GettingChannelMessages(channel))
            val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(viewModel.context)
            val currentUser = authenticatedUser?.user
            try {
                if (currentUser == null || !SharedPreferencesHelper.checkTokenValidity(viewModel.context)) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                }
                if (channel == null) {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = ErrorMessages.CHANNEL_NOT_FOUND) {
                            viewModel.transition(
                                MainViewSelectorState.SubscribedChannels(authenticatedUser = authenticatedUser)
                            )
                        }
                    )
                    return@launch
                } else {
                    val channelMessages = mutableListOf<Message>()
                    val storedMessages =
                        SharedPreferencesHelper.getMessages(viewModel.context, channel.channelId)
                    if (storedMessages.isEmpty()) {
                        val newMessages =
                            viewModel.messageService.getChannelMessages(channel.channelId)
                        SharedPreferencesHelper.storeMessages(
                            viewModel.context,
                            channel.channelId,
                            newMessages
                        )
                        channelMessages.addAll(newMessages)
                    }
                    viewModel.transition(
                        MainViewSelectorState.ChannelMessages(
                            channel = channel.copy(messages = channelMessages),
                            authenticatedUser = authenticatedUser
                        )
                    )
                }
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message!!} : Current State -> $viewModel.state")
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = e.message) {
                            viewModel.transition(
                                MainViewSelectorState.SubscribedChannels(
                                    authenticatedUser = authenticatedUser
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    fun loadChannelInfo() {
        viewModel.viewModelScope.launch {
            val channel = (viewModel.stateFlow.value as? MainViewSelectorState.ChannelInfo)?.channel
            val currentUser = SharedPreferencesHelper.getAuthenticatedUser(viewModel.context)
            if (viewModel.stateFlow.value !is MainViewSelectorState.GettingChannelInfo) {
                viewModel.transition(MainViewSelectorState.GettingChannelInfo)
                try {
                    if (channel == null) viewModel.transition(
                        MainViewSelectorState.Error(message = ErrorMessages.CHANNEL_NOT_FOUND) {
                            viewModel.transition(
                                MainViewSelectorState.SubscribedChannels(
                                    authenticatedUser = currentUser
                                )
                            )
                        }
                    )
                    else if (currentUser == null || !SharedPreferencesHelper.checkTokenValidity(
                            viewModel.context
                        )
                    ) {
                        viewModel.transition(MainViewSelectorState.Unauthenticated)
                        return@launch
                    } else {
                        var updatedChannel: Channel
                        if (channel.users.isEmpty()) {
                            val channelUsers =
                                viewModel.userService.getChannelUsers(channel.channelId)
                            updatedChannel = channel.copy(users = channelUsers)
                        } else {
                            updatedChannel = channel
                        }
                        viewModel.transition(
                            MainViewSelectorState.ChannelInfo(
                                channel = updatedChannel,
                                authenticatedUser = currentUser
                            )
                        )
                    }
                } catch (e: ServiceException) {
                    Log.e(TAG, "${e.message!!} : Current State -> $viewModel.state")
                    if (e.type === ServiceErrorTypes.Unauthorized) {
                        viewModel.transition(MainViewSelectorState.Unauthenticated)
                    } else {
                        viewModel.transition(
                            MainViewSelectorState.ChannelMessages(
                                channel = channel,
                                authenticatedUser = currentUser
                            )
                        )
                    }
                }
            }
        }
    }

    fun loadSubscribedChannels() {
        viewModel.transition(MainViewSelectorState.Loading)
        viewModel.viewModelScope.launch {
            val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(viewModel.context)
            try {
                Log.d(TAG, "=== COULD RETRIEVE AUTH USER : $authenticatedUser")
                val currentUser = authenticatedUser?.user
                if (currentUser == null || !SharedPreferencesHelper.checkTokenValidity(viewModel.context)) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                }
                val channelsWithoutMessagesOrUsers =
                    viewModel.channelService.getUserSubscribedChannels(currentUser.id)
                val channels = mutableListOf<Channel>()

                for (channel in channelsWithoutMessagesOrUsers) {
                    Log.i("DEBUG", channel.displayName)
                    val messages = mutableListOf<Message>()
                    val storedMessages =
                        SharedPreferencesHelper.getMessages(viewModel.context, channel.channelId)
                    if (storedMessages.isEmpty()) {
                        val channelMessages =
                            viewModel.messageService.getChannelMessages(channel.channelId)
                        messages.addAll(channelMessages)
                    } else {
                        messages.addAll(storedMessages)
                    }
                    val channelUsers = viewModel.userService.getChannelUsers(channel.channelId)

                    val toAdd = channel.copy(messages = messages, users = channelUsers)
                    channels.add(toAdd)
                }

                Log.i(TAG, "=====DEBUG=====\n GOT CHANNELS: $channels")
                viewModel.transition(
                    MainViewSelectorState.SubscribedChannels(
                        channels = channels,
                        authenticatedUser = authenticatedUser
                    )
                )
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message!!} : Current State -> ${viewModel.stateFlow.value}")
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    Log.i(TAG, "Transitioning to Unauthenticated")
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = e.message) {
                            viewModel.transition(
                                MainViewSelectorState.SubscribedChannels(
                                    authenticatedUser = authenticatedUser
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    fun createChannel(
        ownerId: Int,
        name: String,
        isPrivate: Boolean,
        channelIconUrl: String
    ) {
            viewModel.viewModelScope.launch {
                val currentUser = SharedPreferencesHelper.getAuthenticatedUser(viewModel.context)?.user
                if (viewModel.stateFlow.value is MainViewSelectorState.CreateChannel) {
                    if (currentUser == null || !SharedPreferencesHelper.checkTokenValidity(viewModel.context)) {
                        viewModel.transition(MainViewSelectorState.Unauthenticated)
                        return@launch
                    }
                    viewModel.transition(MainViewSelectorState.CreatingChannel)
                val authenticatedUser =
                    SharedPreferencesHelper.getAuthenticatedUser(viewModel.context)

                if (authenticatedUser == null || !SharedPreferencesHelper.checkTokenValidity(
                        viewModel.context
                    )
                ) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                }
                try {
                    viewModel.channelService.createChannel(
                        ownerId,
                        name,
                        isPrivate,
                        channelIconUrl
                    )
                    viewModel.transition(MainViewSelectorState.Loading)
                    val channels =
                        viewModel.channelService.getUserSubscribedChannels(currentUser.id)
                    viewModel.transition(
                        MainViewSelectorState.SubscribedChannels(
                            channels = channels,
                            authenticatedUser = authenticatedUser
                        )
                    )
                } catch (e: ServiceException) {
                    Log.e(TAG, "${e.message!!} : Current State -> $viewModel.state")
                    if (e.type === ServiceErrorTypes.Unauthorized) {
                        viewModel.transition(MainViewSelectorState.Unauthenticated)
                    } else {
                        Log.i(TAG, "Transitioning to error! - Create channel")
                        viewModel.transition(
                            MainViewSelectorState.Error(e.message) {
                                viewModel.transition(
                                    MainViewSelectorState.CreateChannel(authenticatedUser = authenticatedUser)
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    fun removeUserFromChannel(
        userId: Int,
        channelId: Int
    ) {
        val currentUser = SharedPreferencesHelper.getAuthenticatedUser(viewModel.context)
        if (currentUser == null || !SharedPreferencesHelper.checkTokenValidity(viewModel.context)) {
            viewModel.transition(MainViewSelectorState.Unauthenticated)
            return
        }

        viewModel.viewModelScope.launch {
            val channel = viewModel.channelService.getChannelById(channelId)
            try {
                viewModel.channelService.removeUserFromChannel(userId, channelId)
                viewModel.transition(
                    MainViewSelectorState.ChannelInfo(
                        channel = channel,
                        authenticatedUser = currentUser
                    )
                )
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message!!} : Current State -> $viewModel.state")
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = e.message) {
                            viewModel.transition(
                                MainViewSelectorState.ChannelInfo(
                                    channel = channel,
                                    authenticatedUser = currentUser
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    fun inviteUserToChannel(userId: Int, channelId: Int, permission: PermissionLevel) {
        Log.i(TAG, "Inviting user $userId to channel $channelId")
        val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(viewModel.context)
        val currentUser = authenticatedUser?.user

        if (currentUser == null || !SharedPreferencesHelper.checkTokenValidity(viewModel.context)) {
            viewModel.transition(MainViewSelectorState.Unauthenticated)
            return
        }

        viewModel.viewModelScope.launch {
            // WE WILL HAVE TO MAKE A PROPER SOLUTION FOR THIS, THIS IS ONLY A WORKAROUND!!!
            var channelNullCheck = true
            val channel = viewModel.channelService.getChannelById(channelId)
            channelNullCheck = false
            try {
                viewModel.channelService.createChannelInvitation(
                    channelId,
                    currentUser.id,
                    userId,
                    permission
                )
                viewModel.transition(
                    MainViewSelectorState.InvitingUsers(
                        channel = channel,
                        showAlertDialog = true,
                        dialogText = "User invited!",
                        authenticatedUser = authenticatedUser
                    )
                )
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message!!} : Current State -> $viewModel.state")
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = e.message) {
                            if (channelNullCheck) {
                                viewModel.transition(
                                    MainViewSelectorState.SubscribedChannels(
                                        authenticatedUser = authenticatedUser
                                    )
                                )
                            } else {
                                viewModel.transition(
                                    MainViewSelectorState.InvitingUsers(
                                        channel = channel,
                                        authenticatedUser = authenticatedUser
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    fun leaveChannel(
        userId: Int?,
        channel: Channel
    ) {
        val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(viewModel.context)
        val currentUser = authenticatedUser?.user
        if (currentUser == null || !SharedPreferencesHelper.checkTokenValidity(viewModel.context)) {
            viewModel.transition(MainViewSelectorState.Unauthenticated)
            return
        }
        if (userId == null) {
            viewModel.transition(
                MainViewSelectorState.Error(message = ErrorMessages.USER_NOT_FOUND) {
                    viewModel.transition(
                        MainViewSelectorState.ChannelInfo(
                            channel = channel,
                            authenticatedUser = authenticatedUser
                        )
                    )
                }
            )
            return
        }


        viewModel.viewModelScope.launch {
            try {
                viewModel.channelService.removeUserFromChannel(userId, channel.channelId)
                val channels = viewModel.channelService.getUserSubscribedChannels(userId)
                viewModel.transition(
                    MainViewSelectorState.SubscribedChannels(
                        channels = channels,
                        authenticatedUser = authenticatedUser
                    )
                )
                return@launch
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message!!} : Current State -> $viewModel.state")
                val channels = viewModel.channelService.getUserSubscribedChannels(userId)
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(e.message) {
                            viewModel.transition(
                                MainViewSelectorState.SubscribedChannels(authenticatedUser = authenticatedUser)
                            )
                        }
                    )
                }
            }
        }
    }
}