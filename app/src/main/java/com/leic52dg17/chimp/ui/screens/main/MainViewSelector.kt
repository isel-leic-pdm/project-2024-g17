package com.leic52dg17.chimp.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.ui.components.dialogs.ConfirmationDialog
import com.leic52dg17.chimp.ui.components.dialogs.SharedAlertDialog
import com.leic52dg17.chimp.ui.components.nav.BottomNavbar
import com.leic52dg17.chimp.ui.components.overlays.LoadingOverlay
import com.leic52dg17.chimp.ui.screens.main.nav.SelectedNavIcon
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.viewmodels.screen.MainViewSelectorViewModel
import com.leic52dg17.chimp.ui.views.UserInfoView
import com.leic52dg17.chimp.ui.views.about.AboutView
import com.leic52dg17.chimp.ui.views.channel.ChannelInfoView
import com.leic52dg17.chimp.ui.views.channel.ChannelMessageView
import com.leic52dg17.chimp.ui.views.create_channel.CreateChannelView
import com.leic52dg17.chimp.ui.views.subscribed.SubscribedChannelsView

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

        if(isConfirmationDialogShown) {
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

        Scaffold(
            bottomBar = {
                if (isNavBarShown) {
                    BottomNavbar(
                        selectedIcon = selectedNavIcon,
                        onClickProfile = {
                            selectedNavIcon = SelectedNavIcon.Profile
                            val currentUser = authenticatedUser?.user
                            if (currentUser != null) {
                                viewModel.getUserProfile(currentUser.userId)
                            }
                        },
                        onClickMessages = {
                            selectedNavIcon = SelectedNavIcon.Messages
                            val currentUser = authenticatedUser?.user
                            if(currentUser != null) {
                                viewModel.loadSubscribedChannels()
                            }
                        },
                        onClickAbout = {
                            selectedNavIcon = SelectedNavIcon.About
                            viewModel.transition(MainViewSelectorState.About)
                        },
                        rowModifier = Modifier
                            .padding(bottom = 32.dp),
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isLoading) {
                    LoadingOverlay()
                }
                when (viewModel.state) {
                    is MainViewSelectorState.Loading -> {
                        isLoading = true
                    }

                    is MainViewSelectorState.SubscribedChannels -> {
                        isLoading = false
                        selectedNavIcon = SelectedNavIcon.Messages

                        val currentState = (viewModel.state as MainViewSelectorState.SubscribedChannels)
                        if (currentState.showDialog) {
                            alertDialogText = currentState.dialogMessage
                                ?: stringResource(id = R.string.generic_error_en)
                            handleSharedAlertDialogVisibilitySwitch()
                        }

                        LaunchedEffect(Unit) {
                            viewModel.loadSubscribedChannels()
                        }

                        isNavBarShown = true
                        SubscribedChannelsView(
                            currentState.channels,
                            onCreateChannelClick = {
                                viewModel.transition(MainViewSelectorState.CreateChannel(false))
                            },
                            onChannelClick = {
                                viewModel.transition(
                                    MainViewSelectorState.ChannelMessages(
                                        false,
                                        channel = it
                                    )
                                )
                            }
                        )
                    }

                    is MainViewSelectorState.CreateChannel -> {
                        isLoading = false

                        isNavBarShown = false

                        val currentState = (viewModel.state as MainViewSelectorState.CreateChannel)
                        if (currentState.showDialog) {
                            alertDialogText = currentState.dialogMessage
                                ?: stringResource(id = R.string.generic_error_en)
                            handleSharedAlertDialogVisibilitySwitch()
                        }

                        CreateChannelView(
                            onBackClick = {
                                viewModel.transition(MainViewSelectorState.SubscribedChannels(false))
                            },
                            onChannelNameInfoClick = { text ->
                                alertDialogText = text
                                handleSharedAlertDialogVisibilitySwitch()
                            },
                            onCreateChannelRequest = { ownerId, name, isPrivate, channelIconUrl, channelIconContentDescription ->
                                viewModel.createChannel(
                                    ownerId,
                                    name,
                                    isPrivate,
                                    channelIconUrl,
                                    channelIconContentDescription
                                )
                            },
                            authenticatedUser = authenticatedUser
                        )
                    }

                    is MainViewSelectorState.CreatingChannel -> {
                        isNavBarShown = false
                        isLoading = true
                    }

                    is MainViewSelectorState.ChannelMessages -> {
                        isLoading = false
                        isNavBarShown = false
                        val currentState = (viewModel.state as MainViewSelectorState.ChannelMessages)
                        LaunchedEffect(Unit) {
                            viewModel.loadChannelMessages()
                        }
                        if (currentState.showDialog) {
                            alertDialogText = currentState.dialogMessage
                                ?: stringResource(id = R.string.generic_error_en)
                            handleSharedAlertDialogVisibilitySwitch()
                        }
                        val currentChannel = currentState.channel
                        if (currentChannel != null) {
                            ChannelMessageView(
                                channel = currentChannel,
                                onBackClick = {
                                    viewModel.transition(MainViewSelectorState.SubscribedChannels(false))
                                },
                                onChannelNameClick = {
                                    viewModel.transition(MainViewSelectorState.ChannelInfo(currentChannel))
                                }
                            )
                        }
                    }

                    is MainViewSelectorState.GettingChannelMessages -> {
                        isLoading = true
                    }

                    is MainViewSelectorState.ChannelInfo -> {
                        isLoading = false
                        isNavBarShown = false
                        val currentState = (viewModel.state as MainViewSelectorState.ChannelInfo)
                        LaunchedEffect(Unit) {
                            viewModel.loadChannelInfo()
                        }
                        currentState.channel?.let {
                            ChannelInfoView(
                                channel = it,
                                onBackClick = {
                                    viewModel.transition(MainViewSelectorState.ChannelMessages(channel = it))
                                },
                                onAddToUserChannelClick = { /*TODO()*/ },
                                onRemoveUser = { userId, channelId ->
                                    confirmationDialogConfirmFunction = {
                                        viewModel.removeUserFromChannel(userId, channelId)
                                        handleConfirmationDialogVisibilitySwitch()
                                    }
                                    confirmationDialogText = "Are you sure you wish to remove this user from the channel?"
                                    confirmationDialogTitle = "Confirm user removal"
                                    confirmationDialogCancelText = "No"
                                    confirmationDialogConfirmText = "Yes"
                                    handleConfirmationDialogVisibilitySwitch()
                                },
                                onUserClick = { userId -> viewModel.getUserProfile(userId) },
                                onLeaveChannelClick = {
                                    confirmationDialogConfirmFunction = {
                                        viewModel.leaveChannel(currentState.authenticatedUser?.user?.userId, it)
                                        handleConfirmationDialogVisibilitySwitch()
                                    }
                                    confirmationDialogText = "Are you sure you want to leave the channel?\n The oldest user will be assigned as the new owner.\n This is not reversible."
                                    confirmationDialogTitle = "Confirm your leave"
                                    confirmationDialogCancelText = "No"
                                    confirmationDialogConfirmText = "Yes"
                                    handleConfirmationDialogVisibilitySwitch()
                                },
                                authenticatedUser = currentState.authenticatedUser
                            )
                        }
                    }

                    MainViewSelectorState.GettingChannelInfo -> {
                        isLoading = true
                        isNavBarShown = false
                    }

                    is MainViewSelectorState.UserInfo -> {
                        val currentState = (viewModel.state as MainViewSelectorState.UserInfo)
                        UserInfoView(
                            user = currentState.user,
                            authenticatedUser = authenticatedUser,
                            onBackClick = {
                                viewModel.transition(MainViewSelectorState.SubscribedChannels(false))
                            },
                            onLogoutClick = { viewModel.logout() },
                        )
                    }

                    MainViewSelectorState.About -> {
                        isLoading = false
                        isNavBarShown = true
                        AboutView(
                            onBackClick = { viewModel.loadSubscribedChannels() }
                        )
                    }
                }
            }
        }
    }
}

