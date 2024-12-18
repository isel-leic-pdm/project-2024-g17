package com.leic52dg17.chimp.core.repositories.messages.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.leic52dg17.chimp.core.repositories.messages.model.MessageEntity

@Dao
interface MessageDAO {
    @Query("SELECT * FROM message")
    fun getAll(): List<MessageEntity>

    @Insert
    fun insertAll(messages: List<MessageEntity>)

    @Delete
    fun delete(message: MessageEntity)

    @Query("DELETE FROM message")
    fun deleteAll()

    @Query("SELECT * FROM message WHERE id = :messageId")
    fun getById(messageId: Int): MessageEntity

    @Query("SELECT * FROM message WHERE channelId = :channelId")
    fun getByAllByChannelId(channelId: Int): List<MessageEntity>
}