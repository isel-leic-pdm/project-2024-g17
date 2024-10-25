package com.leic52dg17.chimp.model.auth

import com.leic52dg17.chimp.model.user.User

data class AuthenticatedUser(
    val authenticationToken: String? = null,
    val user: User? = null
)
