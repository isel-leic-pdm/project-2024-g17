package com.leic52dg17.chimp.http.services.auth.requests

import kotlinx.serialization.Serializable

@Serializable
data class UserSignUpRequest(val username: String, val displayName: String, val password: String)