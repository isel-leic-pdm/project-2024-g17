package com.leic52dg17.chimp.core.repositories.channel.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.leic52dg17.chimp.core.repositories.channel.model.ChannelEntity

@Dao
interface ChannelDAO {
    @Query("SELECT * FROM channel")
    fun getAll(): List<ChannelEntity>

    @Insert
    fun insertAll(channels: List<ChannelEntity>)

    @Query("DELETE FROM channel")
    fun delete(channel: ChannelEntity)

    @Delete
    fun deleteAll()

    @Query("SELECT * FROM channel WHERE channelId = :channelId")
    fun getById(channelId: Int)
}