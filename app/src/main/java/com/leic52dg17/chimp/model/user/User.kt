package com.leic52dg17.chimp.model.user

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val userId: Int,
    val username: String,
    val displayName: String
)