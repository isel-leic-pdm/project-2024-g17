package com.leic52dg17.chimp.http.services.auth.requests

import kotlinx.serialization.Serializable

@Serializable
data class GetTokenRequest(val username: String, val password: String)