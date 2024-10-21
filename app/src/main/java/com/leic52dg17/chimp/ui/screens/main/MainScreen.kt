    package com.leic52dg17.chimp.ui.screens.main

    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Scaffold
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.saveable.rememberSaveable
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import com.leic52dg17.chimp.ui.components.misc.SharedAlertDialog
    import com.leic52dg17.chimp.ui.components.nav.BottomNavbar
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
                when (viewModel.state) {
                    is MainScreenState.SubscribedChannels -> {
                        isNavBarShown = true
                        SubscribedChannelsView(
                            innerPadding,
                            onCreateChannelClick = {
                                viewModel.transition(MainScreenState.CreateChannel)
                            }
                        )
                    }

                    is MainScreenState.CreateChannel -> {
                        isNavBarShown = false
                        CreateChannelView(
                            onBackClick = {
                                viewModel.transition(MainScreenState.SubscribedChannels)
                            },
                            onChannelNameInfoClick = { text ->
                                alertDialogText = text
                                handleDialogVisibilitySwitch()
                            }
                        )
                    }
                }
            }
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun MainScreenPreview() {
        MainScreen(viewModel = MainScreenViewModel())
    }
