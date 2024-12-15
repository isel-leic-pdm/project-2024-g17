package com.leic52dg17.chimp.domain.model.channel

import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import kotlinx.serialization.Serializable

@Serializable
data class ChannelInvitation(
    val id: Int,
    val channelId: Int,
    val senderId: Int,
    val receiverId: Int,
    val permissionLevel: PermissionLevel
) {
    init {
        require(channelId > 0) {
            throw IllegalArgumentException(ErrorMessages.ID)
        }
        require(senderId > 0) {
            throw IllegalArgumentException(ErrorMessages.ID)
        }
        require(receiverId > 0) {
            throw IllegalArgumentException(ErrorMessages.ID)
        }
    }
}
