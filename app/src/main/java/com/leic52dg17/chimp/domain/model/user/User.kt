package com.leic52dg17.chimp.domain.model.user

import com.leic52dg17.chimp.domain.common.ErrorMessages
import kotlinx.serialization.Serializable

@Serializable
data class User (
    val id: Int,
    val username: String,
    val displayName: String
) {
    init {
        require(id > 0) {
            throw IllegalArgumentException(ErrorMessages.ID)
        }
        require(username.length <= 50) {
            throw IllegalArgumentException(ErrorMessages.USERNAME_TOO_LONG)
        }
        require(username.isNotEmpty()) {
            throw IllegalArgumentException(ErrorMessages.USERNAME_EMPTY)
        }
        require(username.isNotBlank()) {
            throw IllegalArgumentException(ErrorMessages.USERNAME_BLANK)
        }
        require(displayName.isNotEmpty()) {
            throw IllegalArgumentException(ErrorMessages.DISPLAY_EMPTY)
        }
        require(displayName.isNotBlank()) {
            throw IllegalArgumentException(ErrorMessages.DISPLAY_BLANK)
        }
        require(displayName.length <= 50) {
            throw IllegalArgumentException(ErrorMessages.DISPLAY_TOO_LONG)
        }
    }
}