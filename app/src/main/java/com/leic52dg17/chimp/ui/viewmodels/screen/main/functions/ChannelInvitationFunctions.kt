package com.leic52dg17.chimp.ui.viewmodels.screen.main.functions

import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.core.shared.SharedPreferencesHelper
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.channel.ChannelInvitationDetails
import com.leic52dg17.chimp.http.services.common.ServiceErrorTypes
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import kotlinx.coroutines.launch

class ChannelInvitationFunctions(private val viewModel: MainViewSelectorViewModel) {
    fun loadChannelInvitations(authenticatedUser: AuthenticatedUser?) {
        try {
            viewModel.viewModelScope.launch {
                if (authenticatedUser?.user == null || !SharedPreferencesHelper.checkTokenValidity(
                        viewModel.context
                    )
                ) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                }
                val invitations =
                    viewModel.channelService.getChannelInvitations(authenticatedUser.user.id)
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

    fun acceptChannelInvitation(invitationId: Int, authenticatedUser: AuthenticatedUser) {
        try {
            viewModel.viewModelScope.launch {
                if (authenticatedUser.user == null || !SharedPreferencesHelper.checkTokenValidity(
                        viewModel.context
                    )
                ) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.channelService.acceptChannelInvitation(
                        invitationId,
                        authenticatedUser.user.id
                    )
                }
            }
        } catch (e: ServiceException) {
            if (e.type === ServiceErrorTypes.Unauthorized) {
                viewModel.transition(MainViewSelectorState.Unauthenticated)
            } else {
                viewModel.transition(
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

    fun rejectChannelInvitation(invitationId: Int, authenticatedUser: AuthenticatedUser) {
        try {
            viewModel.viewModelScope.launch {
                if (authenticatedUser.user == null || !SharedPreferencesHelper.checkTokenValidity(
                        viewModel.context
                    )
                ) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.channelService.rejectChannelInvitation(
                        invitationId,
                        authenticatedUser.user.id
                    )
                }
            }
        } catch (e: ServiceException) {
            if (e.type === ServiceErrorTypes.Unauthorized) {
                viewModel.transition(MainViewSelectorState.Unauthenticated)
            } else {
                viewModel.transition(
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
}