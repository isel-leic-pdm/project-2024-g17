package com.leic52dg17.chimp.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.core.shared.SharedPreferencesHelper
import com.leic52dg17.chimp.http.services.channel.implementations.FakeChannelService
import com.leic52dg17.chimp.http.services.message.implementations.FakeMessageService
import com.leic52dg17.chimp.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.ui.components.misc.SharedAlertDialog
import com.leic52dg17.chimp.ui.components.nav.BottomNavbar
import com.leic52dg17.chimp.ui.components.overlays.LoadingOverlay
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.viewmodels.screen.MainViewSelectorViewModel
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

        var isLoading by rememberSaveable(saver = MainViewSelectorState.BooleanSaver) {
            mutableStateOf(false)
        }

        var alertDialogText by rememberSaveable(saver = MainViewSelectorState.StringSaver) {
            mutableStateOf("")
        }
        var isNavBarShown by rememberSaveable(saver = MainViewSelectorState.BooleanSaver) {
            mutableStateOf(true)
        }
        var selectedNavIcon by rememberSaveable(saver = MainViewSelectorState.StringSaver) {
            mutableStateOf("chats")
        }

        fun handleDialogVisibilitySwitch() {
            val currentVisibility = isSharedAlertDialogShown
            val newVisibility = !currentVisibility
            isSharedAlertDialogShown = newVisibility
        }

        if (isSharedAlertDialogShown) {
            SharedAlertDialog(
                onDismissRequest = { handleDialogVisibilitySwitch() },
                alertDialogText = alertDialogText
            )
        }
        Scaffold(
            bottomBar = {
                if (isNavBarShown) {
                    BottomNavbar(
                        selectedNavIcon,
                        Modifier
                            .padding(bottom = 32.dp),
                        { selectedNavIcon = "profile" },
                        { selectedNavIcon = "chats" },
                        { selectedNavIcon = "settings" }
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
                    is MainViewSelectorState.GettingChannels -> {
                        isLoading = true
                    }

                    is MainViewSelectorState.SubscribedChannels -> {
                        isLoading = false

                        val currentState = (viewModel.state as MainViewSelectorState.SubscribedChannels)
                        if (currentState.showDialog) {
                            alertDialogText = currentState.dialogMessage
                                ?: stringResource(id = R.string.generic_error_en)
                            handleDialogVisibilitySwitch()
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
                            handleDialogVisibilitySwitch()
                        }

                        CreateChannelView(
                            onBackClick = {
                                viewModel.transition(MainViewSelectorState.SubscribedChannels(false))
                            },
                            onChannelNameInfoClick = { text ->
                                alertDialogText = text
                                handleDialogVisibilitySwitch()
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
                            handleDialogVisibilitySwitch()
                        }
                        val currentChannel = currentState.channel
                        if (currentChannel != null) {
                            ChannelMessageView(
                                channel = currentChannel,
                                onBackClick = {
                                    viewModel.transition(MainViewSelectorState.SubscribedChannels(false))
                                }
                            )
                        }
                    }

                    is MainViewSelectorState.GettingChannelMessages -> {
                        isLoading = true
                    }

                    is MainViewSelectorState.ChannelInfo -> {
                        val currentState = (viewModel.state as MainViewSelectorState.ChannelInfo)
                        ChannelInfoView(
                            channel = currentState.channel,
                            onBackClick = { /*TODO()*/ },
                            onAddToUserChannelClick = { /*TODO()*/ },
                            onRemoveUser = { /*TODO()*/ },
                            onUserClick = { /*TODO()*/ },
                        )
                    }
                }
            }
        }
    }
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainViewSelectorPreview() {
    MainViewSelector(viewModel = MainViewSelectorViewModel(FakeChannelService(), FakeMessageService()))
}
*/
