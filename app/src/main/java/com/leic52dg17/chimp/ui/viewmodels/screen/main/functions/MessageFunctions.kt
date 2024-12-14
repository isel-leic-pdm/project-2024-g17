package com.leic52dg17.chimp.ui.viewmodels.screen.main.functions

import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.http.services.common.ServiceErrorTypes
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MessageFunctions(private val viewModel: MainViewSelectorViewModel) {
    fun sendMessage(channelId: Int, messageText: String) {
        viewModel.viewModelScope.launch {
            val authenticatedUser = viewModel.userInfoRepository.authenticatedUser.first()

            try {
                if (authenticatedUser?.user == null || !viewModel.userInfoRepository.checkTokenValidity()) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                }
                viewModel.messageService.createMessageInChannel(
                    messageText,
                    channelId,
                    authenticatedUser.user.id
                )
                val channelWithoutMessages = viewModel.channelService.getChannelById(channelId)
                val channelMessages = viewModel.messageService.getChannelMessages(channelId)
                val channelUsers = viewModel.userService.getChannelUsers(channelId)
                val updatedChannel =
                    channelWithoutMessages.copy(messages = channelMessages, users = channelUsers)
                viewModel.transition(
                    MainViewSelectorState.ChannelMessages(
                        channel = updatedChannel,
                        authenticatedUser = authenticatedUser
                    )
                )
            } catch (e: ServiceException) {
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = e.message) {
                            viewModel.transition(
                                MainViewSelectorState.SubscribedChannels(
                                    authenticatedUser = authenticatedUser
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}