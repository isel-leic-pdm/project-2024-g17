package com.leic52dg17.chimp.core.repositories.common

import com.leic52dg17.chimp.core.repositories.channel.database.ChannelDAO
import com.leic52dg17.chimp.core.repositories.messages.database.MessageDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppDatabaseManager(private val messageDAO: MessageDAO, private val channelDAO: ChannelDAO) {
    fun clearDB() {
        CoroutineScope(Dispatchers.IO).launch {
            messageDAO.deleteAll()
            channelDAO.deleteAll()
        }
    }
}