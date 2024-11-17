package com.leic52dg17.chimp.domain.model.channel

import com.leic52dg17.chimp.domain.common.ErrorMessages
import java.math.BigInteger

data class UserChannel(
    val userId: Int,
    val channelId: Int,
    val joinedAt: BigInteger
) {
    init {
        require(userId > 0) {
            throw IllegalArgumentException(ErrorMessages.ID)
        }
        require(channelId > 0) {
            throw IllegalArgumentException(ErrorMessages.ID)
        }
        require(joinedAt > BigInteger.ZERO) {
            throw IllegalArgumentException(ErrorMessages.JOINED_AT)
        }
    }
}
