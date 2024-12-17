package com.leic52dg17.chimp.core.repositories.channel.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channel")
data class ChannelEntity(
    @PrimaryKey
    val channelId: Int,
    @ColumnInfo(name = "displayName")
    val displayName: String,
    @ColumnInfo(name = "ownerId")
    val ownerId: Int,
    @ColumnInfo(name = "isPrivate")
    val isPrivate: Boolean,
    @ColumnInfo(name = "channelIconUrl")
    val channelIconUrl: String,
)