package com.leic52dg17.chimp.http.services.auth.responses

import kotlinx.serialization.Serializable

@Serializable
data class GetTokenResponse(val token: String, val expirationDate: Long)
