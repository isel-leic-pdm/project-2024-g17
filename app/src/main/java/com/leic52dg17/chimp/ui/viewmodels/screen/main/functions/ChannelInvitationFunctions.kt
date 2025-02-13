package com.leic52dg17.chimp.ui.viewmodels.screen.main.functions

import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.core.cache.channel.IChannelCacheManager
import com.leic52dg17.chimp.core.cache.message.IMessageCacheManager
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.channel.ChannelInvitationDetails
import com.leic52dg17.chimp.http.services.common.ServiceErrorTypes
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import kotlinx.coroutines.launch

class ChannelInvitationFunctions(
    private val viewModel: MainViewSelectorViewModel,
    private val channelCacheManager: IChannelCacheManager,
    private val messageCacheManager: IMessageCacheManager
) {
    fun loadChannelInvitations(authenticatedUser: AuthenticatedUser?) {
        try {
            viewModel.viewModelScope.launch {
                if (authenticatedUser?.user == null || !viewModel.userInfoRepository.checkTokenValidity()) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                }

                val invitations =
                    viewModel.channelInvitationService.getChannelInvitationsByReceiverId(
                        authenticatedUser.user.id
                    )

                val invitationDetails = invitations.map { invitation ->
                    val sender = viewModel.userService.getUserById(invitation.senderId)
                    val channel = viewModel.channelService.getChannelById(invitation.channelId)

                    ChannelInvitationDetails(
                        id = invitation.id,
                        channelId = invitation.channelId,
                        senderId = invitation.senderId,
                        receiverId = invitation.receiverId,
                        permissionLevel = invitation.permissionLevel,
                        channelName = channel.displayName,
                        senderUsername = if (sender?.displayName != null) sender.displayName else "Unknown user"
                    )
                }
                viewModel.transition(
                    MainViewSelectorState.UserInvitations(
                        invitationDetails,
                        authenticatedUser
                    )
                )
            }
        } catch (e: ServiceException) {
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

    fun acceptChannelInvitation(invitationId: Int, authenticatedUser: AuthenticatedUser) {
        viewModel.transition(
            MainViewSelectorState.AcceptingInvitation
        )
        try {
            viewModel.viewModelScope.launch {
                if (authenticatedUser.user == null || !viewModel.userInfoRepository.checkTokenValidity()) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.channelInvitationService.acceptChannelInvitation(
                        invitationId,
                    )
                    val invitation =
                        viewModel.channelInvitationService.getChannelInvitationById(invitationId)
                    val channel = viewModel.channelService.getChannelById(invitation.channelId)
                    val messages =
                        viewModel.messageService.getChannelMessages(channelId = channel.channelId)
                    viewModel.transition(MainViewSelectorState.AcceptedInvitation {
                        viewModel.loadChannelInvitations(
                            authenticatedUser
                        )
                    })
                    messageCacheManager.forceUpdate(messages)
                    channelCacheManager.forceUpdate(channel)
                }
            }
        } catch (e: ServiceException) {
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

    fun rejectChannelInvitation(invitationId: Int, authenticatedUser: AuthenticatedUser) {
        try {
            viewModel.viewModelScope.launch {
                if (authenticatedUser.user == null || !viewModel.userInfoRepository.checkTokenValidity()) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.channelInvitationService.rejectChannelInvitation(
                        invitationId,
                    )
                    loadChannelInvitations(authenticatedUser)
                }
            }
        } catch (e: ServiceException) {
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