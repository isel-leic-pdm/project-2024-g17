package com.leic52dg17.chimp.ui.viewmodels.screen.main.functions

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.core.cache.channel.IChannelCacheManager
import com.leic52dg17.chimp.core.cache.message.IMessageCacheManager
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import com.leic52dg17.chimp.http.services.common.ServiceErrorTypes
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel.Companion.TAG
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ChannelFunctions(
    private val viewModel: MainViewSelectorViewModel,
    private val channelCacheManager: IChannelCacheManager,
    private val messageCacheManager: IMessageCacheManager
) {
    fun loadChannelMessages(channelId: Int) {
        viewModel.viewModelScope.launch {
            Log.i(TAG, "Loading channel messages for channel with ID: $channelId")

            val channel = viewModel.channelService.getChannelById(channelId)

            val authenticatedUser = viewModel.userInfoRepository.authenticatedUser.first()

            try {
                if (authenticatedUser?.user == null || !viewModel.userInfoRepository.checkTokenValidity()) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                }

                val channelMessages = messageCacheManager.getCachedMessage()
                    .filter { it.channelId == channel.channelId }
                val userPermissions = viewModel.channelService.getUserPermissionsByChannelId(
                    authenticatedUser.user.id,
                    channel.channelId
                )
                viewModel.transition(
                    MainViewSelectorState.ChannelMessages(
                        channel = channel.copy(messages = channelMessages),
                        authenticatedUser = authenticatedUser,
                        hasWritePermissions = userPermissions == PermissionLevel.RW
                    )
                )

                return@launch
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message} : Current State -> $viewModel.state")
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = e.message) {
                            viewModel.loadSubscribedChannels()
                        }
                    )
                }
            }
        }
    }

    fun loadChannelInfo(channelId: Int) {
        viewModel.viewModelScope.launch {
            Log.i(TAG, "Loading channel info for channel with id $channelId")
            val channel = viewModel.channelService.getChannelById(channelId)
            val currentUser = viewModel.userInfoRepository.authenticatedUser.first()

            if (viewModel.stateFlow.value !is MainViewSelectorState.GettingChannelInfo) {
                viewModel.transition(MainViewSelectorState.GettingChannelInfo)
                try {
                    if (currentUser == null || !viewModel.userInfoRepository.checkTokenValidity()) {
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
                    Log.e(TAG, "${e.message} : Current State -> ${viewModel.stateFlow.value}")
                    if (e.type === ServiceErrorTypes.Unauthorized) {
                        viewModel.transition(MainViewSelectorState.Unauthenticated)
                    } else {
                        loadChannelMessages(channel.channelId)
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
                } else if (e.type === ServiceErrorTypes.NotFoundForbidden) {
                    viewModel.transition(
                        MainViewSelectorState.Unauthenticated
                    )
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = e.message) {
                            loadSubscribedChannels()
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
                viewModel.transition(
                    MainViewSelectorState.CreatingChannel(
                        authenticatedUser = authenticatedUser,
                        channelName = name
                    )
                )

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
                viewModel.transition(
                    MainViewSelectorState.RemovingUser
                )
                viewModel.channelService.removeUserFromChannel(userId, channelId)
                viewModel.transition(
                    MainViewSelectorState.RemovedUser {
                        viewModel.loadChannelInfo(channelId)
                    }
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

    fun inviteUserToChannel(
        userId: Int,
        channelId: Int,
        permission: PermissionLevel,
        userDisplayName: String
    ) {
        Log.i(TAG, "Inviting user $userId to channel $channelId")
        viewModel.viewModelScope.launch {
            val authenticatedUser = viewModel.userInfoRepository.authenticatedUser.first()

            if (authenticatedUser?.user == null || !viewModel.userInfoRepository.checkTokenValidity()) {
                viewModel.transition(MainViewSelectorState.Unauthenticated)
                return@launch
            }
            viewModel.transition(
                MainViewSelectorState.InvitingUser(
                    userDisplayName = userDisplayName
                )
            )
            val channel = viewModel.channelService.getChannelById(channelId)

            try {
                viewModel.channelService.createChannelInvitation(
                    channelId,
                    authenticatedUser.user.id,
                    userId,
                    permission
                )
                viewModel.transition(
                    MainViewSelectorState.InvitedUser(
                        userDisplayName = userDisplayName,
                        onBackClick = {
                            viewModel.loadChannelInfo(channelId)
                        },
                    )
                )
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message} : Current State -> $viewModel.state")
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = e.message) {
                            viewModel.loadSubscribedChannels()
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
                channelCacheManager.removeFromCache(channel)
                viewModel.loadSubscribedChannels()
                return@launch
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message} : Current State -> $viewModel.state")
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(e.message) {
                            viewModel.loadSubscribedChannels()
                        }
                    )
                }
            }
        }
    }

    fun loadPublicChannels(channelName: String, page: Int) {
        viewModel.viewModelScope.launch {
            try {
                viewModel.transition(MainViewSelectorState.GettingPublicChannels(
                    channelName
                ) { viewModel.loadSubscribedChannels() })
                val channels = viewModel.channelService.getPublicChannels(channelName, page)
                    .filter { !it.isPrivate }
                val cachedChannels = channelCacheManager.getCachedChannels()
                val filtered =
                    channels.filterNot { channel -> cachedChannels.any { cached -> cached.channelId == channel.channelId } }
                viewModel.transition(
                    MainViewSelectorState.PublicChannels(
                        filtered,
                        page,
                        channelName
                    )
                )
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message} : Current State -> $viewModel.state")
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = e.message) {
                            viewModel.loadSubscribedChannels()
                        }
                    )
                }
            }
        }
    }

    fun joinPublicChannel(channel: Channel) {
        viewModel.viewModelScope.launch {
            try {
                val authenticatedUser = viewModel.userInfoRepository.authenticatedUser.first()
                if (authenticatedUser?.user == null) {
                    viewModel.transition(
                        MainViewSelectorState.Unauthenticated
                    )
                    return@launch
                }
                viewModel.transition(MainViewSelectorState.JoiningPublicChannel(channel))
                viewModel.channelService.addUserToChannel(
                    authenticatedUser.user.id,
                    channel.channelId
                )
                val messages = viewModel.messageService.getChannelMessages(channel.channelId)
                messageCacheManager.forceUpdate(messages)
                channelCacheManager.forceUpdate(channel)
                viewModel.transition(MainViewSelectorState.JoinedPublicChannel(channel))
            } catch (e: ServiceException) {
                Log.e(TAG, "${e.message} : Current State -> $viewModel.state")
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = e.message) {
                            viewModel.loadSubscribedChannels()
                        }
                    )
                }
            }
        }
    }
}