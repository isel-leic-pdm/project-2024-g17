package com.leic52dg17.chimp.core.cache.message

import android.util.Log
import com.leic52dg17.chimp.core.cache.channel.ChannelCacheManager
import com.leic52dg17.chimp.core.repositories.messages.IMessageRepository
import com.leic52dg17.chimp.core.repositories.user.IUserInfoRepository
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.http.services.common.ServiceException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MessageCacheManager(
    private val userInfoRepository: IUserInfoRepository,
    private val messageRepository: IMessageRepository,
) : IMessageCacheManager {
    private var successCallback: ((newMessages: List<Message>) -> Unit)? = {}
    private var errorCallback: ((errorMessage: String) -> Unit)? = {}
    val _currentMessages = MutableStateFlow<List<Message>>(emptyList())

    override fun forceUpdate(message: Message) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val authenticatedUser: AuthenticatedUser? =
                    userInfoRepository.authenticatedUser.first()
                if (authenticatedUser?.user == null) {
                    Log.e(ChannelCacheManager.TAG, "Could not find authenticated user ID.")
                    return@launch
                }
                val storedMessages = messageRepository.getStoredMessages()
                val newMessage = listOf(message)
                val hasChanged = messageRepository.isUpdateDue(storedMessages, newMessage)
                val newMessages = storedMessages + newMessage
                Log.i(ChannelCacheManager.TAG, "HAS CHANGED FORCED - $hasChanged")
                if (hasChanged) {
                    messageRepository.storeMessages(newMessage)
                    _currentMessages.emit(newMessages)
                    runCallback()
                }
            } catch (e: ServiceException) {
                runErrorCallback(e.message)
            } catch (e: Exception) {
                runErrorCallback(ErrorMessages.UNKNOWN)
            }
        }
    }

    override suspend fun registerCallback(callback: (newMessages: List<Message>) -> Unit) {
        Log.i(TAG, "Registering callback...")
        successCallback = callback
        Log.i(TAG, "Registered callback!")
    }

    override suspend fun registerErrorCallback(callback: (errorMessage: String) -> Unit) {
        Log.i(TAG, "Registering error callback...")
        errorCallback = callback
        Log.i(TAG, "Registered error callback!")
    }

    override suspend fun unregisterCallbacks() {
        Log.i(TAG, "Unregistering callbacks...")
        successCallback = null
        errorCallback = null
        Log.i(TAG, "Unregistered callbacks!")
    }

    override suspend fun runCallback() {
        Log.i(TAG, "Running callback with message list size ${_currentMessages.value.size}...")
        if (successCallback == null) {
            Log.w(TAG, "There is no callback registered!!!")
            return
        } else successCallback?.invoke(_currentMessages.value)
        Log.i(TAG, "Ran callback!")
    }

    override suspend fun runErrorCallback(errorMessage: String) {
        Log.i(TAG, "Running error callback...")
        if (errorCallback == null) {
            Log.w(TAG, "There is no callback registered!!!")
            return
        } else errorCallback?.invoke(errorMessage)
        Log.i(TAG, "Ran error callback!")
    }

    override fun getCachedMessage(): List<Message> {
        return _currentMessages.value
    }

    companion object {
        const val TAG = "MESSAGE_CACHE_MANAGER"
    }
}