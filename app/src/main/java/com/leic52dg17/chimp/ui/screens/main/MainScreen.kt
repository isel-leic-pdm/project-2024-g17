package com.leic52dg17.chimp.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.leic52dg17.chimp.ui.components.SharedAlertDialog
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.viewmodels.MainViewModel
import com.leic52dg17.chimp.ui.views.create_channel.CreateChannelView
import com.leic52dg17.chimp.ui.views.subscribed.SubscribedChannelsView

@Composable
fun MainScreen(viewModel: MainViewModel) {
    ChIMPTheme {
        var currentState = viewModel.state

        var isSharedAlertDialogShown by rememberSaveable(saver = MainScreenState.BooleanSaver) {
            mutableStateOf(false)
        }
        var alertDialogText by rememberSaveable(saver = MainScreenState.StringSaver) {
            mutableStateOf("")
        }

        fun handleDialogVisibilitySwitch() {
            val currentVisibility = isSharedAlertDialogShown
            val newVisibility = !currentVisibility
            isSharedAlertDialogShown = newVisibility
        }

        if(isSharedAlertDialogShown) {
            SharedAlertDialog(
                onDismissRequest = { handleDialogVisibilitySwitch() },
                alertDialogText = alertDialogText
            )
        }

        when(currentState) {
            is MainScreenState.SubscribedChannels -> SubscribedChannelsView()
            is MainScreenState.CreateChannel -> CreateChannelView(
                onBackClick = { currentState = MainScreenState.SubscribedChannels },
                onChannelNameInfoClick = { text ->
                    alertDialogText = text
                    handleDialogVisibilitySwitch()
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen(viewModel = MainViewModel())
}
