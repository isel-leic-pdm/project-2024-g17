package com.leic52dg17.chimp.model.auth

import com.leic52dg17.chimp.model.user.User
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticatedUser(
    val authenticationToken: String? = null,
    val user: User? = null
)
