package com.leic52dg17.chimp.domain.model.auth

import com.leic52dg17.chimp.domain.model.user.User
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticatedUser(
    val authenticationToken: String? = null,
    val user: User? = null
)
