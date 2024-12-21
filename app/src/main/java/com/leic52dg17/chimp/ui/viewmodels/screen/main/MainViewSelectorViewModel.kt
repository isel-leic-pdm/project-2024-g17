package com.leic52dg17.chimp.ui.viewmodels.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.core.cache.channel.ChannelCacheManager
import com.leic52dg17.chimp.core.cache.common.initializer.CacheInitializer
import com.leic52dg17.chimp.core.cache.common.manager.CommonCacheManager
import com.leic52dg17.chimp.core.cache.message.MessageCacheManager
import com.leic52dg17.chimp.core.repositories.channel.IChannelRepository
import com.leic52dg17.chimp.core.repositories.messages.IMessageRepository
import com.leic52dg17.chimp.core.repositories.user.IUserInfoRepository
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.channel.ChannelInvitationDetails
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.channel_invitations.IChannelInvitationService
import com.leic52dg17.chimp.http.services.message.IMessageService
import com.leic52dg17.chimp.http.services.registration_invitation.IRegistrationInvitationService
import com.leic52dg17.chimp.http.services.sse.ISSEService
import com.leic52dg17.chimp.http.services.sse.events.Events
import com.leic52dg17.chimp.http.services.user.IUserService
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.functions.CacheCallbacks
import com.leic52dg17.chimp.ui.viewmodels.screen.main.functions.ChannelFunctions
import com.leic52dg17.chimp.ui.viewmodels.screen.main.functions.ChannelInvitationFunctions
import com.leic52dg17.chimp.ui.viewmodels.screen.main.functions.MessageFunctions
import com.leic52dg17.chimp.ui.viewmodels.screen.main.functions.RegistrationInvitationFunctions
import com.leic52dg17.chimp.ui.viewmodels.screen.main.functions.UserFunctions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewSelectorViewModel(
    val channelService: IChannelService,
    val messageService: IMessageService,
    val registrationInvitationService: IRegistrationInvitationService,
    val userService: IUserService,
    val channelInvitationService: IChannelInvitationService,
    private val sseService: ISSEService,
    val userInfoRepository: IUserInfoRepository,
    private val onLogout: () -> Unit,
    private val channelCacheManager: ChannelCacheManager,
    private val messageCacheManager: MessageCacheManager,
    private val channelRepository: IChannelRepository,
    private val messageRepository: IMessageRepository,
    private val openEmailApp: () -> Unit
) : ViewModel() {
    val stateFlow: MutableStateFlow<MainViewSelectorState> =
        MutableStateFlow(MainViewSelectorState.Loading)

    private val cacheCallbacks = CacheCallbacks(this)
    private val cacheManager = CommonCacheManager(channelCacheManager, messageCacheManager)
    private val channelFunctions = ChannelFunctions(this, channelCacheManager, messageCacheManager)
    private val userFunctions = UserFunctions(this)
    private val registrationInvitationFunctions = RegistrationInvitationFunctions(this)
    private val messageFunctions = MessageFunctions(this, messageCacheManager)
    private val channelInvitationFunctions = ChannelInvitationFunctions(this, channelCacheManager, messageCacheManager)
    private val cacheInitializer = CacheInitializer(channelService, messageService, this, channelRepository, messageRepository, channelCacheManager, messageCacheManager)
    val openEmail = { openEmailApp() }

    init {
        viewModelScope.launch {
            val authenticatedUser = userInfoRepository.authenticatedUser.first()

            channelCacheManager.registerCallback { newChannels ->
                cacheCallbacks.channelSuccessCallback(newChannels)
            }
            channelCacheManager.registerErrorCallback { errorMessage ->
                cacheCallbacks.channelErrorCallback(errorMessage)
            }
            messageCacheManager.registerCallback { newMessages ->
                cacheCallbacks.messageSuccessCallback(newMessages)
            }
            messageCacheManager.registerErrorCallback { errorMessage ->
                cacheCallbacks.messageErrorCallback(errorMessage)
            }
            cacheInitializer.initializeCache(authenticatedUser)
            stateFlow.emit(
                MainViewSelectorState.Initialized(authenticatedUser = userInfoRepository.authenticatedUser.first())
            )
        }
    }

    fun transition(newState: MainViewSelectorState) {
        viewModelScope.launch {
            stateFlow.emit(newState)
        }
    }

    private suspend fun handleIncomingEvent(event: Events) {
        val state = stateFlow.value
        Log.i(TAG, "Handling event in current state -> ${state}.")
        when (event) {
            is Events.ChannelMessage -> {
                messageCacheManager.forceUpdate(event.message)
            }

            is Events.Invitation -> {
                when (state) {
                    is MainViewSelectorState.UserInvitations -> {
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
                                state.copy(
                                    invitations = state.invitations + incomingInvitation,
                                    authenticatedUser = state.authenticatedUser
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
    fun loadChannelMessages(channelId: Int) = channelFunctions.loadChannelMessages(channelId)
    fun loadSubscribedChannels() = channelFunctions.loadSubscribedChannels()
    fun loadChannelInfo(channelId: Int) = channelFunctions.loadChannelInfo(channelId)
    fun createChannel(ownerId: Int, name: String, isPrivate: Boolean, channelIconUrl: String) =
        channelFunctions.createChannel(ownerId, name, isPrivate, channelIconUrl)
    fun getSortedChannels(): List<Channel> {
        val cached = cacheManager.getChannels()
        return if (cached.isNotEmpty()) {
            cached.sortedByDescending { it.messages.lastOrNull()?.createdAt }
        } else {
            cached
        }
    }

    fun removeUserFromChannel(userId: Int, channelId: Int) =
        channelFunctions.removeUserFromChannel(userId, channelId)

    fun inviteUserToChannel(userId: Int, channelId: Int, permission: PermissionLevel, userDisplayName: String) =
        channelFunctions.inviteUserToChannel(userId, channelId, permission, userDisplayName)

    fun leaveChannel(userId: Int?, channel: Channel) =
        channelFunctions.leaveChannel(userId, channel)

    /**
     *  Registration functions
     */

    fun createRegistrationInvitation(creatorId: Int) = registrationInvitationFunctions.createRegistrationInvitation(creatorId)


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
    fun loadAvailableUsersToInvite(channel: Channel, username: String, page: Int?, limit: Int?) = userFunctions.loadAvailableToInviteUsers(channel, username, page, limit)

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
    private val registrationInvitationService: IRegistrationInvitationService,
    private val userService: IUserService,
    private val channelInvitationService: IChannelInvitationService,
    private val sseService: ISSEService,
    private val userInfoRepository: IUserInfoRepository,
    private val onLogout: () -> Unit,
    private val channelCacheManager: ChannelCacheManager,
    private val messageCacheManager: MessageCacheManager,
    private val channelRepository: IChannelRepository,
    private val messageRepository: IMessageRepository,
    private val openEmailApp: () -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewSelectorViewModel(
            channelService,
            messageService,
            registrationInvitationService,
            userService,
            channelInvitationService,
            sseService,
            userInfoRepository,
            onLogout,
            channelCacheManager,
            messageCacheManager,
            channelRepository,
            messageRepository,
            openEmailApp
        ) as T
    }
}