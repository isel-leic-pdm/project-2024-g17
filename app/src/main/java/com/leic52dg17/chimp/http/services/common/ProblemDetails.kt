package com.leic52dg17.chimp.http.services.common

import kotlinx.serialization.Serializable

@Serializable
data class ProblemDetails(
    val title: String,
    val errors: Map<String, String>? = null
)
