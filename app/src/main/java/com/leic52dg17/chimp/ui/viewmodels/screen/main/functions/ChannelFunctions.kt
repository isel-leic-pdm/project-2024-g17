package com.leic52dg17.chimp.ui.viewmodels.screen.main.functions

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.core.cache.channel.IChannelCacheManager
import com.leic52dg17.chimp.core.cache.message.IMessageCacheManager
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import com.leic52dg17.chimp.http.services.common.ServiceErrorTypes
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel.Companion.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChannelFunctions(
    private val viewModel: MainViewSelectorViewModel,
    private val channelCacheManager: IChannelCacheManager,
    private val messageCacheManager: IMessageCacheManager
) {
    fun loadChannelMessages() {
        viewModel.viewModelScope.launch {
            val channel =
                (viewModel.stateFlow.value as? MainViewSelectorState.ChannelMessages)?.channel
            Log.i(TAG, "Loading channel messages for channel with ID: ${channel?.channelId}")
            viewModel.transition(MainViewSelectorState.GettingChannelMessages(channel))
            val authenticatedUser = viewModel.userInfoRepository.authenticatedUser.first()
            try {
                if (authenticatedUser?.user == null || !viewModel.userInfoRepository.checkTokenValidity()) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                }
                if (channel == null) {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = ErrorMessages.CHANNEL_NOT_FOUND) {
                            viewModel.transition(
                                MainViewSelectorState.SubscribedChannels(
                                    authenticatedUser = authenticatedUser,
                                    channels = viewModel.cacheManager.getChannels()
                                )
                            )
                        }
                    )
                    return@launch
                } else {
                    val channelMessages = messageCacheManager.getCachedMessage()
                        .filter { it.channelId == channel.channelId }
                    Log.i(TAG, "Channel messages -> $channelMessages")
                    viewModel.transition(
                        MainViewSelectorState.ChannelMessages(
                            channel = channel.copy(messages = channelMessages),
                            authenticatedUser = authenticatedUser
                        )
                    )
                    return@launch
                }
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message} : Current State -> $viewModel.state")
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = e.message) {
                            viewModel.transition(
                                MainViewSelectorState.SubscribedChannels(
                                    authenticatedUser = authenticatedUser,
                                    channels = viewModel.cacheManager.getChannels()
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
            Log.i(TAG, "Loading channel info for channel - $channel")
            val currentUser = viewModel.userInfoRepository.authenticatedUser.first()
            if (viewModel.stateFlow.value !is MainViewSelectorState.GettingChannelInfo) {
                viewModel.transition(MainViewSelectorState.Loading)
                try {
                    if (channel == null) viewModel.transition(
                        MainViewSelectorState.Error(message = ErrorMessages.CHANNEL_NOT_FOUND) {
                            viewModel.transition(
                                MainViewSelectorState.SubscribedChannels(
                                    authenticatedUser = currentUser,
                                    channels = viewModel.cacheManager.getChannels()
                                )
                            )
                        }
                    )
                    else if (currentUser == null || !viewModel.userInfoRepository.checkTokenValidity()) {
                        viewModel.transition(MainViewSelectorState.Unauthenticated)
                        return@launch
                    } else {
                        val channelUsers = viewModel.userService.getChannelUsers(channel.channelId)
                        val updatedChannel = channel.copy(users = channelUsers)
                        viewModel.transition(
                            MainViewSelectorState.ChannelInfo(
                                channel = updatedChannel,
                                authenticatedUser = currentUser
                            )
                        )
                    }
                } catch (e: ServiceException) {
                    Log.e(TAG, "${e.message} : Current State -> $viewModel.state")
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
        viewModel.viewModelScope.launch {
            val authenticatedUser = viewModel.userInfoRepository.authenticatedUser.first()
            try {
                Log.d(TAG, "=== COULD RETRIEVE AUTH USER : $authenticatedUser")
                val currentUser = authenticatedUser?.user
                if (currentUser == null || !viewModel.userInfoRepository.checkTokenValidity()) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                }

                viewModel.transition(MainViewSelectorState.Loading)
                val channels = channelCacheManager.getCachedChannels()
                Log.i(TAG, "GOT CHANNELS: $channels")
                viewModel.transition(
                    MainViewSelectorState.SubscribedChannels(
                        channels = channels,
                        authenticatedUser = authenticatedUser
                    )
                )
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message} : Current State -> ${viewModel.stateFlow.value}")
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    Log.i(TAG, "Transitioning to Unauthenticated")
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = e.message) {
                            viewModel.transition(
                                MainViewSelectorState.SubscribedChannels(
                                    authenticatedUser = authenticatedUser,
                                    channels = viewModel.cacheManager.getChannels()
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
            val authenticatedUser = viewModel.userInfoRepository.authenticatedUser.first()
            if (viewModel.stateFlow.value is MainViewSelectorState.CreateChannel) {
                if (authenticatedUser?.user == null || !viewModel.userInfoRepository.checkTokenValidity()) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                }
                viewModel.transition(MainViewSelectorState.CreatingChannel(authenticatedUser = authenticatedUser))

                if (!viewModel.userInfoRepository.checkTokenValidity()) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                }
                try {
                    val channelId = viewModel.channelService.createChannel(
                        ownerId,
                        name,
                        isPrivate,
                        channelIconUrl
                    )
                    val channelToAdd = viewModel.channelService.getChannelById(channelId)
                    channelCacheManager.forceUpdate(channelToAdd)
                } catch (e: ServiceException) {
                    Log.e(TAG, "${e.message} : Current State -> $viewModel.state")
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
        viewModel.viewModelScope.launch {
            val currentUser = viewModel.userInfoRepository.authenticatedUser.first()
            if (currentUser == null || !viewModel.userInfoRepository.checkTokenValidity()) {
                viewModel.transition(MainViewSelectorState.Unauthenticated)
                return@launch
            }

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
                Log.e(TAG, "${e.message} : Current State -> $viewModel.state")
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
        viewModel.viewModelScope.launch {
            val authenticatedUser = viewModel.userInfoRepository.authenticatedUser.first()

            if (authenticatedUser?.user == null || !viewModel.userInfoRepository.checkTokenValidity()) {
                viewModel.transition(MainViewSelectorState.Unauthenticated)
                return@launch
            }

            // WE WILL HAVE TO MAKE A PROPER SOLUTION FOR THIS, THIS IS ONLY A WORKAROUND!!!
            var channelNullCheck = true
            val channel = viewModel.channelService.getChannelById(channelId)
            channelNullCheck = false
            try {
                viewModel.channelService.createChannelInvitation(
                    channelId,
                    authenticatedUser.user.id,
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
                Log.e(TAG, "${e.message} : Current State -> $viewModel.state")
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = e.message) {
                            if (channelNullCheck) {
                                viewModel.transition(
                                    MainViewSelectorState.SubscribedChannels(
                                        authenticatedUser = authenticatedUser,
                                        channels = viewModel.cacheManager.getChannels()
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
        viewModel.viewModelScope.launch {
            val authenticatedUser = viewModel.userInfoRepository.authenticatedUser.first()
            val currentUser = authenticatedUser?.user
            if (currentUser == null || !viewModel.userInfoRepository.checkTokenValidity()) {
                viewModel.transition(MainViewSelectorState.Unauthenticated)
                return@launch
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
                return@launch
            }

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
                Log.e(TAG, "${e.message} : Current State -> $viewModel.state")
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(e.message) {
                            viewModel.transition(
                                MainViewSelectorState.SubscribedChannels(
                                    authenticatedUser = authenticatedUser,
                                    channels = viewModel.cacheManager.getChannels()
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}