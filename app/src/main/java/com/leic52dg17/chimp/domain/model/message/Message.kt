package com.leic52dg17.chimp.domain.model.message

import com.leic52dg17.chimp.domain.common.ErrorMessages
import kotlinx.serialization.Serializable
import java.math.BigInteger

@Serializable
data class Message(
    val id: Int,
    val userId: Int,
    val channelId: Int,
    val text: String,
    val createdAt: Long
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