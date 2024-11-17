package com.leic52dg17.chimp.model.user

import com.leic52dg17.chimp.model.common.ErrorMessages
import java.util.UUID

data class UserRequest(
    val requestId: Int,
    val userId: Int,
    val invitationCode: UUID
) {
    init {
        require(requestId > 0) {
            throw IllegalArgumentException(ErrorMessages.ID)
        }
        require(userId > 0) {
            throw IllegalArgumentException(ErrorMessages.ID)
        }
    }
}