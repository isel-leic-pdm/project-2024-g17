package com.leic52dg17.chimp.http.services.sse.events

import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class EventContent

@Serializable
@SerialName("ChannelMessage")
data class MessageContent(
    val id: Int,
    val channelId: Int,
    val userId: Int,
    val text: String,
    val createdAt: Long
) : EventContent()

@Serializable
@SerialName("Invitation")
data class InvitationContent(
    val id: Int,
    val senderId: Int,
    val receiverId: Int,
    val channelId: Int,
    val permissionLevel: PermissionLevel,
    val createdAt: Long
) : EventContent()
