package com.leic52dg17.chimp.ui.screens.main

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
import com.leic52dg17.chimp.http.services.channel.implementations.FakeChannelService
import com.leic52dg17.chimp.ui.components.misc.SharedAlertDialog
import com.leic52dg17.chimp.ui.components.nav.BottomNavbar
import com.leic52dg17.chimp.ui.components.overlays.LoadingOverlay
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.viewmodels.screen.MainScreenViewModel
import com.leic52dg17.chimp.ui.views.create_channel.CreateChannelView
import com.leic52dg17.chimp.ui.views.subscribed.SubscribedChannelsView

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    ChIMPTheme {
        var isSharedAlertDialogShown by rememberSaveable(saver = MainScreenState.BooleanSaver) {
            mutableStateOf(false)
        }
        var isLoading by rememberSaveable(saver = MainScreenState.BooleanSaver) {
            mutableStateOf(false)
        }
        var alertDialogText by rememberSaveable(saver = MainScreenState.StringSaver) {
            mutableStateOf("")
        }
        var isNavBarShown by rememberSaveable(saver = MainScreenState.BooleanSaver) {
            mutableStateOf(true)
        }
        var selectedNavIcon by rememberSaveable(saver = MainScreenState.StringSaver) {
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
            if (isLoading) {
                LoadingOverlay()
            }
            when (viewModel.state) {
                is MainScreenState.GettingChannels -> {
                    isLoading = true
                }

                is MainScreenState.SubscribedChannels -> {
                    isLoading = false

                    val currentState = (viewModel.state as MainScreenState.SubscribedChannels)
                    if (currentState.showDialog) {
                        alertDialogText = currentState.dialogMessage
                            ?: stringResource(id = R.string.generic_error_en)
                        handleDialogVisibilitySwitch()
                    }

                    LaunchedEffect(Unit) {
                        val channels = viewModel.getCurrentUserSubscribedChannels()
                        if (channels != null) {
                            viewModel.transition(
                                MainScreenState.SubscribedChannels(
                                    false,
                                    channels = channels
                                )
                            )
                        } else {
                            viewModel.transition(
                                MainScreenState.CreateChannel(
                                    true,
                                    "Error getting channels"
                                )
                            )
                        }
                    }

                    isNavBarShown = true
                    SubscribedChannelsView(
                        currentState.channels,
                        innerPadding,
                        onCreateChannelClick = {
                            viewModel.transition(MainScreenState.CreateChannel(false))
                        }
                    )
                }

                is MainScreenState.CreateChannel -> {
                    isLoading = false

                    isNavBarShown = false

                    val currentState = (viewModel.state as MainScreenState.CreateChannel)
                    if (currentState.showDialog) {
                        alertDialogText = currentState.dialogMessage
                            ?: stringResource(id = R.string.generic_error_en)
                        handleDialogVisibilitySwitch()
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
            when (viewModel.state) {
                is MainScreenState.GettingChannels -> {

                }

                is MainScreenState.SubscribedChannels -> {
                    viewModel.getCurrentUserSubscribedChannels()
                    isNavBarShown = true
                    SubscribedChannelsView(
                        viewModel.currentUserSubscribedChannels,
                        innerPadding,
                        onCreateChannelClick = {
                            viewModel.transition(MainScreenState.CreateChannel(false))
                        }
                    )
                }

                is MainScreenState.CreateChannel -> {
                    val currentState = (viewModel.state as MainScreenState.CreateChannel)
                    if (viewModel.state is MainScreenState.CreateChannel && currentState.showDialog) {
                        isSharedAlertDialogShown = true
                        alertDialogText = currentState.dialogMessage
                            ?: stringResource(id = R.string.generic_error_en)
                    }
                    isNavBarShown = false
                    CreateChannelView(
                        onBackClick = {
                            viewModel.transition(MainScreenState.SubscribedChannels(false))
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
                        authenticatedUser = viewModel.authenticatedUser
                    )
                }

                is MainScreenState.CreatingChannel -> {
                    isNavBarShown = false
                    isLoading = false
                }
                is MainScreenState.CreatingChannel -> TODO()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen(viewModel = MainScreenViewModel(FakeChannelService()))
}
