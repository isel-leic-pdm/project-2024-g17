package com.leic52dg17.chimp.core.cache.common.initializer

import android.util.Log
import com.leic52dg17.chimp.core.cache.channel.ChannelCacheManager
import com.leic52dg17.chimp.core.cache.message.MessageCacheManager
import com.leic52dg17.chimp.core.repositories.channel.IChannelRepository
import com.leic52dg17.chimp.core.repositories.messages.IMessageRepository
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.http.services.message.IMessageService
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CacheInitializer(
    private val channelService: IChannelService,
    private val messageService: IMessageService,
    private val viewModel: MainViewSelectorViewModel,
    private val channelRepository: IChannelRepository,
    private val messageRepository: IMessageRepository,
    private val channelCacheManager: ChannelCacheManager,
    private val messageCacheManager: MessageCacheManager
) : ICacheInitializer {
    override fun initializeCache(authenticatedUser: AuthenticatedUser?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (authenticatedUser?.user?.id == null) {
                    viewModel.transition(
                        MainViewSelectorState.Error(
                            message = ErrorMessages.AUTHENTICATED_USER_NULL
                        ) {
                            viewModel.logout()
                        }
                    )
                    return@launch
                }
                val userChannels = channelService.getUserSubscribedChannels(authenticatedUser.user.id)
                val storedChannels = channelRepository.getStoredChannels()
                // Emits current channel storage to the cache flow, until we don't have the new results (TODO: ADD VISUAL REPRESENTATION OF STILL LOADING STATE)
                channelCacheManager._currentChannels.emit(storedChannels)

                // Emits current message storage to the message flow
                val storedMessages = messageRepository.getStoredMessages()
                messageCacheManager._currentMessages.emit(storedMessages)

                Log.i("CACHE_INITIALIZER", "Stored messages -> $storedMessages \n Stored channels -> $storedChannels\n")
                val storedChannelsWithMessages = storedChannels.map { channel ->
                    val messagesForChannel = storedMessages.filter { it.channelId == channel.channelId }
                    channel.copy(messages = messagesForChannel)
                }

                Log.i("CACHE_INITIALIZER", "Transitioning to initialized with channels -> $storedChannelsWithMessages")

                val haveChannelsChanged =
                    channelRepository.isUpdateDue(storedChannels, userChannels)
                if (haveChannelsChanged) {
                    val differences = channelRepository.getDifferences(storedChannels, userChannels)
                    channelRepository.storeChannels(differences)
                }

                // Emits updated channels after diff check
                channelCacheManager._currentChannels.emit(userChannels)

                val newMessages = mutableListOf<Message>()
                viewModel.transition(MainViewSelectorState.GettingSubscribedChannels)
                for (channel in channelCacheManager._currentChannels.value) {
                    val messages = messageService.getChannelMessages(channel.channelId)
                    newMessages.addAll(messages)
                }

                val haveMessagesChanged = messageRepository.isUpdateDue(storedMessages, newMessages)

                if (haveMessagesChanged) {
                    val differences = messageRepository.getDifferences(storedMessages, newMessages)
                    messageRepository.storeMessages(differences)
                }
                // Emits updated messages after update
                messageCacheManager._currentMessages.emit(newMessages)

                channelCacheManager.runCallback()
                messageCacheManager.runCallback()
            } catch (e: ServiceException) {
                val prevState = viewModel.stateFlow.value
                viewModel.transition(
                    MainViewSelectorState.Error(message = e.message) {
                        viewModel.transition(
                            prevState
                        )
                    }
                )
            } catch (e: Exception) {
                val prevState = viewModel.stateFlow.value
                viewModel.transition(
                    MainViewSelectorState.Error(message = ErrorMessages.UNKNOWN) {
                        viewModel.transition(prevState)
                    }
                )
            }
        }
    }
}