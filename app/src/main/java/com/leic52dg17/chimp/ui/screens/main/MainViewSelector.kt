package com.leic52dg17.chimp.ui.screens.main

import com.leic52dg17.chimp.ui.views.registration_invitation.RegistrationInvitationView
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.ui.components.dialogs.ConfirmationDialog
import com.leic52dg17.chimp.ui.components.dialogs.SharedAlertDialog
import com.leic52dg17.chimp.ui.components.nav.BottomNavbar
import com.leic52dg17.chimp.ui.components.overlays.LoadingOverlay
import com.leic52dg17.chimp.ui.screens.main.nav.SelectedNavIcon
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.theme.custom.topBottomBorder
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import com.leic52dg17.chimp.ui.views.about.AboutView
import com.leic52dg17.chimp.ui.views.about.PrivacyPolicyView
import com.leic52dg17.chimp.ui.views.authentication.ChangePasswordView
import com.leic52dg17.chimp.ui.views.channel.ChannelInfoLoadingView
import com.leic52dg17.chimp.ui.views.channel.ChannelInfoView
import com.leic52dg17.chimp.ui.views.channel.ChannelMessageView
import com.leic52dg17.chimp.ui.views.channel_invitations.AcceptedInvitationView
import com.leic52dg17.chimp.ui.views.channel_invitations.AcceptingInvitationView
import com.leic52dg17.chimp.ui.views.channel.RemovedUserView
import com.leic52dg17.chimp.ui.views.channel.RemovingUserView
import com.leic52dg17.chimp.ui.views.channel_invitations.IncomingInvitationsView
import com.leic52dg17.chimp.ui.views.channel_invitations.InviteUsersToChannelView
import com.leic52dg17.chimp.ui.views.channel_invitations.InvitedUserView
import com.leic52dg17.chimp.ui.views.channel_invitations.InvitingUserView
import com.leic52dg17.chimp.ui.views.create_channel.CreateChannelView
import com.leic52dg17.chimp.ui.views.create_channel.CreatingChannelView
import com.leic52dg17.chimp.ui.views.error.ApplicationErrorView
import com.leic52dg17.chimp.ui.views.registration_invitation.RegistrationInvitationLoadingView
import com.leic52dg17.chimp.ui.views.subscribed.SubscribedChannelsLoadingView
import com.leic52dg17.chimp.ui.views.subscribed.SubscribedChannelsView
import com.leic52dg17.chimp.ui.views.user_info.UserInfoLoadingView
import com.leic52dg17.chimp.ui.views.user_info.UserInfoView

@Composable
fun MainViewSelector(
    viewModel: MainViewSelectorViewModel,
    authenticatedUser: AuthenticatedUser?
) {
    ChIMPTheme {
        var isSharedAlertDialogShown by rememberSaveable(saver = MainViewSelectorState.BooleanSaver) {
            mutableStateOf(false)
        }

        var isConfirmationDialogShown by rememberSaveable(saver = MainViewSelectorState.BooleanSaver) {
            mutableStateOf(false)
        }

        var isLoading by rememberSaveable(saver = MainViewSelectorState.BooleanSaver) {
            mutableStateOf(false)
        }

        var alertDialogText by rememberSaveable(saver = MainViewSelectorState.StringSaver) {
            mutableStateOf("")
        }

        var confirmationDialogTitle by rememberSaveable(saver = MainViewSelectorState.StringSaver) {
            mutableStateOf("")
        }
        var confirmationDialogText by rememberSaveable(saver = MainViewSelectorState.StringSaver) {
            mutableStateOf("")
        }
        var confirmationDialogConfirmText by rememberSaveable(saver = MainViewSelectorState.StringSaver) {
            mutableStateOf("")
        }
        var confirmationDialogCancelText by rememberSaveable(saver = MainViewSelectorState.StringSaver) {
            mutableStateOf("")
        }
        var confirmationDialogConfirmFunction by remember {
            mutableStateOf({})
        }

        var isNavBarShown by rememberSaveable(saver = MainViewSelectorState.BooleanSaver) {
            mutableStateOf(true)
        }

        var selectedNavIcon by rememberSaveable(saver = MainViewSelectorState.SelectedNavIconSaver) {
            mutableStateOf(SelectedNavIcon.Messages)
        }

        fun handleSharedAlertDialogVisibilitySwitch() {
            val currentVisibility = isSharedAlertDialogShown
            val newVisibility = !currentVisibility
            isSharedAlertDialogShown = newVisibility
        }

        fun handleConfirmationDialogVisibilitySwitch() {
            val currentVisibility = isConfirmationDialogShown
            val newVisibility = !currentVisibility
            isConfirmationDialogShown = newVisibility
        }

        if (isSharedAlertDialogShown) {
            SharedAlertDialog(
                onDismissRequest = { handleSharedAlertDialogVisibilitySwitch() },
                alertDialogText = alertDialogText
            )
        }

        if (isConfirmationDialogShown) {
            ConfirmationDialog(
                confirmationTitle = confirmationDialogTitle,
                confirmationText = confirmationDialogText,
                cancelButtonText = confirmationDialogCancelText,
                confirmButtonText = confirmationDialogConfirmText,
                onConfirm = confirmationDialogConfirmFunction,
                onCancel = {
                    handleConfirmationDialogVisibilitySwitch()
                }
            )
        }

        var showNoWifiDialog by rememberSaveable { mutableStateOf(false) }
        val connectivityStatus by viewModel.connectivityStatus.collectAsState()

        LaunchedEffect(connectivityStatus) {
            showNoWifiDialog = !connectivityStatus
        }

        if (showNoWifiDialog) {
            AlertDialog(
                onDismissRequest = { showNoWifiDialog = false },
                title = { Text(text = "No Wi-Fi Connection") },
                text = { Text(text = "Please check your internet connection and try again.") },
                confirmButton = {
                    Button(onClick = { showNoWifiDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }

        Scaffold(
            bottomBar = {
                if (isNavBarShown) {
                    BottomNavbar(
                        selectedIcon = selectedNavIcon,
                        onClickProfile = {
                            selectedNavIcon = SelectedNavIcon.Profile
                            val currentUser = authenticatedUser?.user
                            if (currentUser != null) {
                                viewModel.getUserProfile(currentUser.id)
                            }
                        },
                        onClickMessages = {
                            selectedNavIcon = SelectedNavIcon.Messages
                            val currentUser = authenticatedUser?.user
                            if (currentUser != null) {
                                viewModel.transition(
                                    MainViewSelectorState.SubscribedChannels(
                                        channels = viewModel.getSortedChannels(),
                                        authenticatedUser = authenticatedUser
                                    )
                                )
                            }
                        },
                        onClickAbout = {
                            selectedNavIcon = SelectedNavIcon.About
                            viewModel.transition(MainViewSelectorState.About)
                        },
                        rowModifier = Modifier
                            .topBottomBorder(1.dp, MaterialTheme.colorScheme.secondary)
                            .padding(bottom = 32.dp, top = 16.dp),
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                val state = viewModel.stateFlow.collectAsState().value

                if (isLoading) {
                    LoadingOverlay()
                }

                LaunchedEffect(state) {
                    isLoading = state is MainViewSelectorState.Loading
                }
                when (state) {
                    is MainViewSelectorState.Initialized -> {
                        LaunchedEffect(Unit) {
                            viewModel.getEventStream()
                            viewModel.transition(
                                MainViewSelectorState.SubscribedChannels(
                                    channels = viewModel.getSortedChannels(),
                                    authenticatedUser = authenticatedUser
                                )
                            )
                        }
                    }

                    is MainViewSelectorState.Error -> {
                        Log.i("MAIN_VIEW_SELECTOR", "Got into error: ${state.message}")

                        ApplicationErrorView(
                            message = state.message,
                            onDismiss = {
                                state.onDismiss()
                            }
                        )
                    }


                    is MainViewSelectorState.Loading -> {}

                    is MainViewSelectorState.SubscribedChannels -> {
                        Log.i(
                            "DEBUG",
                            "State channel messages -> ${
                                viewModel.getSortedChannels().map { it.messages.lastOrNull() }
                            }"
                        )
                        selectedNavIcon = SelectedNavIcon.Messages

                        isNavBarShown = true
                        SubscribedChannelsView(
                            channels = viewModel.getSortedChannels(),
                            onCreateChannelClick = {
                                viewModel.transition(
                                    MainViewSelectorState.CreateChannel(
                                        authenticatedUser = state.authenticatedUser
                                    )
                                )
                            },
                            onChannelClick = { channelId: Int ->
                                viewModel.loadChannelMessages(channelId)
                            }
                        )
                    }

                    MainViewSelectorState.GettingSubscribedChannels -> {
                        SubscribedChannelsLoadingView()
                    }

                    is MainViewSelectorState.ChangePassword -> {

                        isNavBarShown = false
                        val currentState =
                            viewModel.stateFlow.collectAsState().value as MainViewSelectorState.ChangePassword
                        ChangePasswordView(
                            onChangePassword = { _, _, _, _ ->
                                viewModel.transition(
                                    MainViewSelectorState.ChangePassword(
                                        authenticatedUser = state.authenticatedUser
                                    )
                                )
                            },
                            onBackClick = {
                                viewModel.transition(
                                    if (currentState.authenticatedUser?.user != null) {
                                        MainViewSelectorState.UserInfo(
                                            user = currentState.authenticatedUser.user,
                                            authenticatedUser = currentState.authenticatedUser,
                                            isCurrentUser = true
                                        )
                                    } else {
                                        MainViewSelectorState.Unauthenticated
                                    }
                                )
                            }
                        )
                    }

                    is MainViewSelectorState.RegistrationInvitation -> {
                        isNavBarShown = false
                        if (state.authenticatedUser?.user != null) {
                            RegistrationInvitationView(
                                onBackClick = {
                                    viewModel.transition(
                                        MainViewSelectorState.UserInfo(
                                            user = state.authenticatedUser.user,
                                            authenticatedUser = state.authenticatedUser,
                                            isCurrentUser = true
                                        )
                                    )
                                },
                                token = state.token,
                                getToken = {
                                    viewModel.createRegistrationInvitation(
                                        state.authenticatedUser.user.id
                                    )
                                }
                            )
                        }
                    }

                    is MainViewSelectorState.LoadingRegistrationInvitation -> {
                        if (state.authenticatedUser?.user != null) {
                            RegistrationInvitationLoadingView(
                                onBackClick = {
                                    viewModel.transition(
                                        MainViewSelectorState.UserInfo(
                                            user = state.authenticatedUser.user,
                                            authenticatedUser = state.authenticatedUser,
                                            isCurrentUser = true
                                        )
                                    )
                                })
                        }
                    }

                    is MainViewSelectorState.CreateChannel -> {
                        isNavBarShown = false

                        CreateChannelView(
                            onBackClick = {
                                viewModel.transition(
                                    MainViewSelectorState.SubscribedChannels(
                                        authenticatedUser = state.authenticatedUser,
                                        channels = viewModel.getSortedChannels()
                                    )
                                )
                            },
                            onChannelNameInfoClick = { text ->
                                alertDialogText = text
                                handleSharedAlertDialogVisibilitySwitch()
                            },
                            onCreateChannelRequest = { ownerId, name, isPrivate, channelIconUrl ->
                                if (!connectivityStatus) {
                                    showNoWifiDialog = true
                                } else {
                                    viewModel.createChannel(
                                        ownerId,
                                        name,
                                        isPrivate,
                                        channelIconUrl
                                    )
                                }
                            },
                            authenticatedUser = authenticatedUser
                        )
                    }

                    is MainViewSelectorState.CreatingChannel -> {
                        CreatingChannelView(channelName = state.channelName)
                        isNavBarShown = false
                    }

                    is MainViewSelectorState.ChannelMessages -> {
                        isNavBarShown = false
                        val currentChannel = state.channel

                        ChannelMessageView(
                            channel = currentChannel,
                            onBackClick = {
                                viewModel.transition(
                                    MainViewSelectorState.SubscribedChannels(
                                        authenticatedUser = state.authenticatedUser,
                                        channels = viewModel.getSortedChannels()
                                    )
                                )
                            },
                            onChannelNameClick = {
                                viewModel.loadChannelInfo(currentChannel.channelId)
                            },
                            onSendClick = { messageText ->
                                if (!connectivityStatus) {
                                    showNoWifiDialog = true
                                } else {
                                    viewModel.sendMessage(currentChannel.channelId, messageText)
                                }
                            },
                            hasWritePermissions = state.hasWritePermissions,
                            authenticatedUser = state.authenticatedUser
                        )
                    }

                    is MainViewSelectorState.GettingChannelMessages -> {
                        // Placeholder state
                    }

                    is MainViewSelectorState.ChannelInfo -> {
                        isNavBarShown = false

                        val channel = state.channel

                        ChannelInfoView(
                            channel = channel,
                            onBackClick = { viewModel.loadChannelMessages(channel.channelId) },
                            onAddToUserChannelClick = {
                                viewModel.transition(
                                    MainViewSelectorState.InvitingUsers(
                                        channel = channel,
                                        authenticatedUser = state.authenticatedUser,
                                        users = emptyList(),
                                        page = 0
                                    )
                                )
                            },
                            onRemoveUser = { userId, channelId ->
                                confirmationDialogConfirmFunction = {
                                    viewModel.removeUserFromChannel(userId, channelId)
                                    handleConfirmationDialogVisibilitySwitch()
                                }
                                confirmationDialogText =
                                    "Are you sure you wish to remove this user from the channel?"
                                confirmationDialogTitle = "Confirm user removal"
                                confirmationDialogCancelText = "No"
                                confirmationDialogConfirmText = "Yes"
                                handleConfirmationDialogVisibilitySwitch()
                            },
                            onUserClick = { userId ->
                                if (!connectivityStatus) {
                                    showNoWifiDialog = true
                                } else {
                                    viewModel.getUserProfile(userId)
                                }
                            },
                            onLeaveChannelClick = {
                                confirmationDialogConfirmFunction = {
                                    if (!connectivityStatus) {
                                        showNoWifiDialog = true
                                    } else {
                                        viewModel.leaveChannel(
                                            state.authenticatedUser?.user?.id,
                                            channel
                                        )
                                        handleConfirmationDialogVisibilitySwitch()
                                    }
                                }
                                confirmationDialogText =
                                    "Are you sure you want to leave the channel?\nThe oldest user will be assigned as the new owner.\nThis is not reversible."
                                confirmationDialogTitle = "Confirm your leave"
                                confirmationDialogCancelText = "No"
                                confirmationDialogConfirmText = "Yes"
                                handleConfirmationDialogVisibilitySwitch()
                            },
                            authenticatedUser = state.authenticatedUser
                        )
                    }

                    is MainViewSelectorState.RemovingUser -> {
                        RemovingUserView()
                    }
                    is MainViewSelectorState.RemovedUser -> {
                        RemovedUserView {
                            state.onBackClick()
                        }
                    }

                    is MainViewSelectorState.GettingChannelInfo -> {
                        isNavBarShown = false
                        ChannelInfoLoadingView()
                    }

                    is MainViewSelectorState.UserInfo -> {
                        val isCurrentUser = state.user.id == authenticatedUser?.user?.id
                        isNavBarShown = isCurrentUser
                        UserInfoView(
                            user = state.user,
                            isCurrentUser = isCurrentUser,
                            onBackClick = {
                                viewModel.transition(
                                    MainViewSelectorState.SubscribedChannels(
                                        authenticatedUser = state.authenticatedUser,
                                        channels = viewModel.getSortedChannels()
                                    )
                                )
                            },
                            onInvitationsClick = {
                                if (!connectivityStatus) {
                                    showNoWifiDialog = true
                                } else {
                                    viewModel.loadChannelInvitations(state.authenticatedUser)
                                }
                            },
                            onLogoutClick = { viewModel.logout() },
                            onChangePasswordClick = {
                                viewModel.transition(
                                    MainViewSelectorState.ChangePassword(
                                        authenticatedUser = state.authenticatedUser
                                    )
                                )
                            },
                            onRegistrationInvitationClick = {
                                if (state.authenticatedUser?.user != null) {
                                    if (!connectivityStatus) {
                                        showNoWifiDialog = true
                                    } else {
                                        viewModel.createRegistrationInvitation(state.authenticatedUser.user.id)
                                    }
                                }
                            },
                        )
                    }

                    is MainViewSelectorState.GettingUserInfo -> {
                        UserInfoLoadingView(
                            isCurrentUser = state.isCurrentUser,
                            onBackClick = state.onBackClick
                        )
                    }

                    is MainViewSelectorState.About -> {
                        isNavBarShown = true
                        AboutView(
                            onEmailClick = { viewModel.openEmail },
                            onPrivacyClick = {
                                viewModel.transition(
                                    MainViewSelectorState.PrivacyPolicy
                                )
                            }
                        )
                    }

                    MainViewSelectorState.PrivacyPolicy -> {
                        isNavBarShown = false
                        PrivacyPolicyView(
                            onBackClick = {
                                viewModel.transition(
                                    MainViewSelectorState.About
                                )
                            }
                        )
                    }

                    is MainViewSelectorState.InvitingUsers -> {
                        val currentChannel = state.channel
                        InviteUsersToChannelView(
                            channel = currentChannel,
                            onBackClick = {
                                viewModel.transition(
                                    MainViewSelectorState.ChannelInfo(
                                        currentChannel,
                                        authenticatedUser = state.authenticatedUser
                                    )
                                )
                            },
                            onInviteUserClick = { userId, channelId, permission, userDisplayName ->
                                if (!connectivityStatus) {
                                    showNoWifiDialog = true
                                } else {
                                    viewModel.inviteUserToChannel(
                                        userId,
                                        channelId,
                                        permission,
                                        userDisplayName
                                    )
                                }
                            },
                            users = state.users,
                            onSearch = { username ->
                                if (!connectivityStatus) {
                                  showNoWifiDialog = true
                                } else {
                                    viewModel.loadAvailableUsersToInvite(
                                        channel = currentChannel,
                                        limit = null, // Will default to 10 on the API
                                        page = state.page,
                                        username = username
                                    )
                                }
                            }
                        )
                    }

                    is MainViewSelectorState.InvitingUser -> {
                        InvitingUserView(userDisplayName = state.userDisplayName)
                    }

                    is MainViewSelectorState.InvitedUser -> {
                        InvitedUserView(userDisplayName = state.userDisplayName) {
                            state.onBackClick()
                        }
                    }

                    MainViewSelectorState.Unauthenticated -> {

                        ApplicationErrorView(
                            message = ErrorMessages.AUTHENTICATED_USER_NULL,
                            onDismiss = {
                                viewModel.logout()
                            }
                        )
                    }

                    is MainViewSelectorState.UserInvitations -> {
                        if (state.authenticatedUser?.user == null) {
                            viewModel.transition(MainViewSelectorState.Unauthenticated)
                        } else {
                            IncomingInvitationsView(
                                invitations = state.invitations,
                                onBackClick = { viewModel.getUserProfile(state.authenticatedUser.user.id) },
                                onAcceptClick = { invitationId ->
                                    if (!connectivityStatus) {
                                        showNoWifiDialog = true
                                    } else {
                                        viewModel.acceptChannelInvitation(
                                            invitationId,
                                            state.authenticatedUser
                                        )
                                    }
                                },
                                onDeclineClick = { invitationId ->
                                    if (!connectivityStatus) {
                                        showNoWifiDialog = true
                                    } else {
                                        viewModel.rejectChannelInvitation(
                                            invitationId,
                                            state.authenticatedUser
                                        )
                                    }
                                }
                            )
                        }
                    }

                    is MainViewSelectorState.AcceptingInvitation -> {
                        AcceptingInvitationView()
                    }

                    is MainViewSelectorState.AcceptedInvitation -> {
                        AcceptedInvitationView {
                            state.onBackClick()
                        }
                    }
                }
            }
        }
    }
}

