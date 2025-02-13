package com.leic52dg17.chimp.core.repositories.common

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leic52dg17.chimp.core.repositories.channel.database.ChannelDAO
import com.leic52dg17.chimp.core.repositories.channel.model.ChannelEntity
import com.leic52dg17.chimp.core.repositories.messages.database.MessageDAO
import com.leic52dg17.chimp.core.repositories.messages.model.MessageEntity
import com.leic52dg17.chimp.core.repositories.user.database.UserDAO
import com.leic52dg17.chimp.core.repositories.user.model.UserEntity

@Database(entities = [ChannelEntity::class, MessageEntity::class, UserEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun channelDao(): ChannelDAO
    abstract fun messageDao(): MessageDAO
    abstract fun userDao(): UserDAO
}