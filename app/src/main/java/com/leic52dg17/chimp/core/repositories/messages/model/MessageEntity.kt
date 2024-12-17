package com.leic52dg17.chimp.core.repositories.messages.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MessageEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "userId")
    val userId: Int,
    @ColumnInfo(name = "channelId")
    val channelId: Int,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "createdAt")
    val createdAt: Long
)
