package com.leic52dg17.chimp.ui.viewmodels.screen.main.functions

import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.core.shared.SharedPreferencesHelper
import com.leic52dg17.chimp.http.services.common.ServiceErrorTypes
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import kotlinx.coroutines.launch

class MessageFunctions(private val viewModel: MainViewSelectorViewModel) {
    fun sendMessage(channelId: Int, messageText: String) {
        val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(viewModel.context)
        val currentUser = authenticatedUser?.user
        viewModel.viewModelScope.launch {
            try {
                if (currentUser == null || !SharedPreferencesHelper.checkTokenValidity(viewModel.context)) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                }
                viewModel.messageService.createMessageInChannel(
                    messageText,
                    channelId,
                    currentUser.id
                )
                val channelWithoutMessages = viewModel.channelService.getChannelById(channelId)
                val channelMessages = viewModel.messageService.getChannelMessages(channelId)
                val channelUsers = viewModel.userService.getChannelUsers(channelId)
                val updatedChannel =
                    channelWithoutMessages.copy(messages = channelMessages, users = channelUsers)
                viewModel.transition(
                    MainViewSelectorState.ChannelMessages(
                        false,
                        channel = updatedChannel,
                        authenticatedUser = authenticatedUser
                    )
                )
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
}