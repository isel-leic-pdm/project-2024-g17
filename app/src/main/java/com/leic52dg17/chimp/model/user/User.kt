package com.leic52dg17.chimp.model.user

import com.leic52dg17.chimp.model.common.ErrorMessages
import kotlinx.serialization.Serializable

@Serializable
data class User (
    val userId: Int,
    val username: String,
    val displayName: String
) {
    init {
        require(userId > 0) {
            throw IllegalArgumentException(ErrorMessages.ID)
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
    }
}