package com.leic52dg17.chimp.model.message

import com.leic52dg17.chimp.model.common.ErrorMessages
import java.math.BigInteger

data class Message(
    val userId: Int,
    val channelId: Int,
    val text: String,
    val createdAt: BigInteger
) {
    init {
        require(userId > 0) {
            throw IllegalArgumentException(ErrorMessages.ID)
        }
        require(channelId > 0) {
            throw IllegalArgumentException(ErrorMessages.ID)
        }
        require(text.isNotEmpty()) {
            throw IllegalArgumentException(ErrorMessages.TEXT_EMPTY)
        }
    }
}