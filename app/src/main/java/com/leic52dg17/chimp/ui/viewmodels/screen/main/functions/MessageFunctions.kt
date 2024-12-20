package com.leic52dg17.chimp.ui.viewmodels.screen.main.functions

import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.core.cache.message.IMessageCacheManager
import com.leic52dg17.chimp.core.cache.message.MessageCacheManager
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.http.services.common.ServiceErrorTypes
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MessageFunctions(private val viewModel: MainViewSelectorViewModel, private val messageCacheManager: MessageCacheManager) {
    fun sendMessage(channelId: Int, messageText: String) {
        viewModel.viewModelScope.launch {
            val authenticatedUser = viewModel.userInfoRepository.authenticatedUser.first()
            try {
                val prevState = viewModel.stateFlow.value
                if (authenticatedUser?.user == null || !viewModel.userInfoRepository.checkTokenValidity()) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                    return@launch
                }
               val messageId = viewModel.messageService.createMessageInChannel(
                    messageText,
                    channelId,
                    authenticatedUser.user.id
                )
                val message = viewModel.messageService.getMessageById(messageId)
                messageCacheManager.forceUpdate(message)
            } catch (e: ServiceException) {
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = e.message) {
                            viewModel.transition(
                                MainViewSelectorState.SubscribedChannels(
                                    authenticatedUser = authenticatedUser,
                                    channels = viewModel.getSortedChannels()
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}