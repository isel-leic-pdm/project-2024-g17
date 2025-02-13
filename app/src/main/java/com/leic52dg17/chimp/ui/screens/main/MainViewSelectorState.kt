package com.leic52dg17.chimp.ui.screens.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.channel.ChannelInvitationDetails
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.ui.screens.main.nav.SelectedNavIcon

sealed interface MainViewSelectorState {
    data class Error(val message: String, val onDismiss: () -> Unit): MainViewSelectorState
    data class Initialized(val authenticatedUser: AuthenticatedUser?): MainViewSelectorState
    data class SubscribedChannels(val channels: List<Channel>, val authenticatedUser: AuthenticatedUser?): MainViewSelectorState
    data object GettingSubscribedChannels: MainViewSelectorState
    data class CreateChannel(val authenticatedUser: AuthenticatedUser?): MainViewSelectorState
    data class ChannelMessages(val channel: Channel, val authenticatedUser: AuthenticatedUser?, val hasWritePermissions: Boolean): MainViewSelectorState
    data class GettingChannelMessages(val channel: Channel, val authenticatedUser: AuthenticatedUser?, val oldMessages: List<Message>): MainViewSelectorState
    data class CreatingChannel(val authenticatedUser: AuthenticatedUser?, val channelName: String): MainViewSelectorState
    data class ChannelInfo(val channel: Channel, val authenticatedUser: AuthenticatedUser?): MainViewSelectorState
    data object GettingChannelInfo: MainViewSelectorState
    data object RemovingUser: MainViewSelectorState
    data class RemovedUser(val onBackClick: () -> Unit): MainViewSelectorState
    data object Loading: MainViewSelectorState
    data class UserInfo(val user: User, val authenticatedUser: AuthenticatedUser?, val isCurrentUser: Boolean): MainViewSelectorState
    data class GettingUserInfo(val isCurrentUser: Boolean, val onBackClick: () -> Unit): MainViewSelectorState
    data class InvitingUsers(val showAlertDialog: Boolean = false, val dialogText: String = "", val channel: Channel, val authenticatedUser: AuthenticatedUser?, val users: List<User>, val page: Int): MainViewSelectorState
    data class InvitingUser(val userDisplayName: String): MainViewSelectorState
    data class InvitedUser(val userDisplayName: String, val onBackClick: () -> Unit): MainViewSelectorState
    data object About: MainViewSelectorState
    data object PrivacyPolicy: MainViewSelectorState
    data class ChangePassword(val authenticatedUser: AuthenticatedUser?): MainViewSelectorState
    data class RegistrationInvitation(val authenticatedUser: AuthenticatedUser?, val token: String): MainViewSelectorState
    data class LoadingRegistrationInvitation(val authenticatedUser: AuthenticatedUser?): MainViewSelectorState
    data object Unauthenticated: MainViewSelectorState
    data class UserInvitations(val invitations: List<ChannelInvitationDetails>, val authenticatedUser: AuthenticatedUser?): MainViewSelectorState
    data object AcceptingInvitation: MainViewSelectorState
    data class AcceptedInvitation(val onBackClick: () -> Unit): MainViewSelectorState
    data class PublicChannels(val channels: List<Channel>, val page: Int, val currentSearchValue: String): MainViewSelectorState
    data class GettingPublicChannels(val textFieldValue: String, val onBackClick: () -> Unit): MainViewSelectorState
    data class JoiningPublicChannel(val channel: Channel): MainViewSelectorState
    data class JoinedPublicChannel(val channel: Channel): MainViewSelectorState
  
    companion object {
        val BooleanSaver: Saver<MutableState<Boolean>, *> = Saver(
            save = { it.value },
            restore = { mutableStateOf(it) }
        )
        val StringSaver: Saver<MutableState<String>, *> = Saver(
            save = { it.value },
            restore = { mutableStateOf(it) }
        )

        val SelectedNavIconSaver: Saver<MutableState<SelectedNavIcon>, *> = Saver(
            save = { it.value },
            restore = { mutableStateOf(it) }
        )
    }
}