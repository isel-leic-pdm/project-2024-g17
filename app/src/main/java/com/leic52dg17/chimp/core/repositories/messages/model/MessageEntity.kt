package com.leic52dg17.chimp.core.repositories.messages.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leic52dg17.chimp.domain.model.message.Message

@Entity(tableName = "message")
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

fun MessageEntity.mapToMessage(): Message {
    return Message(
        this.id,
        this.userId,
        this.channelId,
        this.text,
        this.createdAt
    )
}