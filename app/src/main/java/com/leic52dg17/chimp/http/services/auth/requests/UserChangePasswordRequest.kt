package com.leic52dg17.chimp.http.services.auth.requests

data class UserChangePasswordRequest(
    val username: String,
    val currentPassword: String,
    val newPassword: String,
    val confirmPassword: String
)