package com.leic52dg17.chimp.core.cache.message

import com.leic52dg17.chimp.domain.model.message.Message

interface IMessageCacheManager {
    fun forceUpdate(messages: List<Message>)
    fun forceUpdate(message: Message)
    suspend fun registerCallback(callback: (newMessages: List<Message>) -> Unit)
    suspend fun registerErrorCallback(callback: (errorMessage: String) -> Unit)
    suspend fun unregisterCallbacks()
    suspend fun runCallback()
    suspend fun runErrorCallback(errorMessage: String)
    fun getCachedMessage(): List<Message>
}