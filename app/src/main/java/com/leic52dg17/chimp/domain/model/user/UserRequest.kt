package com.leic52dg17.chimp.domain.model.user

import com.leic52dg17.chimp.domain.common.ErrorMessages
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