package com.leic52dg17.chimp.ui.viewmodels.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.core.cache.channel.IChannelCacheManager
import com.leic52dg17.chimp.core.repositories.channel.ChannelRepository
import com.leic52dg17.chimp.core.repositories.channel.IChannelRepository
import com.leic52dg17.chimp.core.repositories.user.IUserInfoRepository
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.channel.ChannelInvitationDetails
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.channel_invitations.IChannelInvitationService
import com.leic52dg17.chimp.http.services.message.IMessageService
import com.leic52dg17.chimp.http.services.sse.ISSEService
import com.leic52dg17.chimp.http.services.sse.events.Events
import com.leic52dg17.chimp.http.services.user.IUserService
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.functions.CacheCallbacks
import com.leic52dg17.chimp.ui.viewmodels.screen.main.functions.ChannelFunctions
import com.leic52dg17.chimp.ui.viewmodels.screen.main.functions.ChannelInvitationFunctions
import com.leic52dg17.chimp.ui.viewmodels.screen.main.functions.MessageFunctions
import com.leic52dg17.chimp.ui.viewmodels.screen.main.functions.UserFunctions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class MainViewSelectorViewModel(
    val channelService: IChannelService,
    val messageService: IMessageService,
    val userService: IUserService,
    val channelInvitationService: IChannelInvitationService,
    private val sseService: ISSEService,
    val userInfoRepository: IUserInfoRepository,
    private val onLogout: () -> Unit,
    private val channelCacheManager: IChannelCacheManager
) : ViewModel() {
    val stateFlow: MutableStateFlow<MainViewSelectorState> =
        MutableStateFlow(MainViewSelectorState.Loading)

    private val cacheCallbacks = CacheCallbacks(this)
    private val channelFunctions = ChannelFunctions(this, channelCacheManager)
    private val userFunctions = UserFunctions(this)
    private val messageFunctions = MessageFunctions(this)
    private val channelInvitationFunctions = ChannelInvitationFunctions(this, channelCacheManager)

    init {
        viewModelScope.launch {
            channelCacheManager.registerCallback { newChannels ->
                cacheCallbacks.channelSuccessCallback(newChannels)
            }
            channelCacheManager.registerErrorCallback { errorMessage ->
                cacheCallbacks.channelErrorCallback(errorMessage)
            }
            stateFlow.emit(
                MainViewSelectorState.Initialized(authenticatedUser = userInfoRepository.authenticatedUser.first())
            )
            channelCacheManager.startCollection()
        }
    }

    fun transition(newState: MainViewSelectorState) {
        viewModelScope.launch {
            stateFlow.emit(newState)
        }
    }

    private suspend fun handleIncomingEvent(event: Events) {
        when (event) {
            is Events.ChannelMessage -> {
                when (val state = stateFlow.last()) {
                    is MainViewSelectorState.SubscribedChannels -> {
                        val updatedChannels =
                            (state).channels?.map { channel ->
                                if (channel.channelId == event.message.channelId) {
                                    channel.copy(messages = channel.messages + event.message)
                                } else {
                                    channel
                                }
                            }
                        transition((state).copy(channels = updatedChannels))
                    }

                    is MainViewSelectorState.ChannelMessages -> {
                        val channel = (state).channel
                        viewModelScope.launch {
                            if (channel == null) {
                                val authenticatedUser = userInfoRepository.authenticatedUser.first()
                                transition(
                                    MainViewSelectorState.Error(message = ErrorMessages.CHANNEL_NOT_FOUND) {
                                        transition(
                                            MainViewSelectorState.SubscribedChannels(
                                                authenticatedUser = authenticatedUser
                                            )
                                        )
                                    }
                                )
                            } else {
                                val updatedChannel: Channel =
                                    if (event.message.channelId == channel.channelId) {
                                        channel.copy(messages = channel.messages + event.message)
                                    } else {
                                        channel
                                    }
                                transition((state).copy(channel = updatedChannel))
                            }
                        }
                    }

                    else -> {}
                }
            }

            is Events.Invitation -> {
                when (val state = stateFlow.last()) {
                    is MainViewSelectorState.UserInvitations -> {
                        val currState = (state)

                        viewModelScope.launch {
                            val channel =
                                channelService.getChannelById(event.channelInvitation.channelId)
                            val sender = userService.getUserById(event.channelInvitation.senderId)
                            val incomingInvitation = ChannelInvitationDetails(
                                id = event.channelInvitation.id,
                                senderId = event.channelInvitation.senderId,
                                receiverId = event.channelInvitation.receiverId,
                                channelId = event.channelInvitation.channelId,
                                permissionLevel = event.channelInvitation.permissionLevel,
                                senderUsername = if (sender?.username != null) sender.username else "Unknown user",
                                channelName = channel.displayName
                            )

                            transition(
                                currState.copy(
                                    invitations = currState.invitations + incomingInvitation,
                                    authenticatedUser = currState.authenticatedUser
                                )
                            )
                        }
                    }

                    else -> {}
                }
            }
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

    /**
     *  Channel functions
     */
    fun loadChannelMessages() = channelFunctions.loadChannelMessages()
    fun loadSubscribedChannels() = channelFunctions.loadSubscribedChannels()
    fun loadChannelInfo() = channelFunctions.loadChannelInfo()
    fun createChannel(ownerId: Int, name: String, isPrivate: Boolean, channelIconUrl: String) =
        channelFunctions.createChannel(ownerId, name, isPrivate, channelIconUrl)

    fun removeUserFromChannel(userId: Int, channelId: Int) =
        channelFunctions.removeUserFromChannel(userId, channelId)

    fun inviteUserToChannel(userId: Int, channelId: Int, permission: PermissionLevel) =
        channelFunctions.inviteUserToChannel(userId, channelId, permission)

    fun leaveChannel(userId: Int?, channel: Channel) =
        channelFunctions.leaveChannel(userId, channel)

    /**
     * De-authentication functions
     */
    fun logout() {
        transition(MainViewSelectorState.Loading)

        // SharedPreferencesHelper.logout(context)
        viewModelScope.launch {
            userInfoRepository.clearAuthenticatedUser()
            stopEventStream()
            onLogout()
        }
    }

    /**
     * User functions
     */
    fun getUserProfile(id: Int) = userFunctions.getUserProfile(id)

    /**
     * Message functions
     */
    fun sendMessage(channelId: Int, messageText: String) =
        messageFunctions.sendMessage(channelId, messageText)

    /**
     * Channel Invitation functions
     */
    fun loadChannelInvitations(authenticatedUser: AuthenticatedUser?) =
        channelInvitationFunctions.loadChannelInvitations(authenticatedUser)

    fun acceptChannelInvitation(invitationId: Int, authenticatedUser: AuthenticatedUser) =
        channelInvitationFunctions.acceptChannelInvitation(invitationId, authenticatedUser)

    fun rejectChannelInvitation(invitationId: Int, authenticatedUser: AuthenticatedUser) =
        channelInvitationFunctions.rejectChannelInvitation(invitationId, authenticatedUser)

    companion object {
        const val TAG = "MAIN_VIEW_SELECTOR_VIEW_MODEL"
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewSelectorViewModelFactory(
    private val channelService: IChannelService,
    private val messageService: IMessageService,
    private val userService: IUserService,
    private val channelInvitationService: IChannelInvitationService,
    private val sseService: ISSEService,
    private val userInfoRepository: IUserInfoRepository,
    private val onLogout: () -> Unit,
    private val channelCacheManager: IChannelCacheManager,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewSelectorViewModel(
            channelService,
            messageService,
            userService,
            channelInvitationService,
            sseService,
            userInfoRepository,
            onLogout,
            channelCacheManager,
        ) as T
    }
}