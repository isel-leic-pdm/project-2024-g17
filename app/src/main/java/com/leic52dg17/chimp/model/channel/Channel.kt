package com.leic52dg17.chimp.model.channel

import com.leic52dg17.chimp.model.common.ErrorMessages
import com.leic52dg17.chimp.model.message.Message
import com.leic52dg17.chimp.model.user.User

data class Channel(
    val channelId: Int,
    val displayName: String,
    val ownerId: Int,
    val messages: List<Message>,
    val users: List<User>,
    val isPrivate: Boolean,
    val channelIconUrl: String
) {
    init {
        require(channelId > 1) {
            throw IllegalArgumentException(ErrorMessages.ID)
        }
        require(displayName.isNotBlank()) {
            throw IllegalArgumentException(ErrorMessages.DISPLAY_BLANK)
        }
        require(displayName.isNotEmpty()) {
            throw IllegalArgumentException(ErrorMessages.DISPLAY_EMPTY)
        }
        require(ownerId > 0) {
            throw IllegalArgumentException(ErrorMessages.ID)
        }
        require(channelIconUrl.isNotEmpty()) {
            throw IllegalArgumentException(ErrorMessages.CHANNEL_ICON_URL_EMPTY)
        }
        require(channelIconUrl.isNotBlank()) {
            throw IllegalArgumentException(ErrorMessages.CHANNEL_ICON_URL_BLANK)
        }
    }
}