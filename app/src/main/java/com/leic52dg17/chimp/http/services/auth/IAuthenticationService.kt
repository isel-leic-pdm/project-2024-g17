package com.leic52dg17.chimp.http.services.auth

import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser

interface IAuthenticationService {
    suspend fun loginUser(username: String, password: String): AuthenticatedUser
    suspend fun signUpUser(registrationInvitation: String, username: String, displayName: String, password: String): AuthenticatedUser
    suspend fun changePassword(username: String, currentPassword: String, newPassword: String, confirmPassword: String): AuthenticatedUser
    suspend fun forgotPassword(email: String): AuthenticatedUser
    suspend fun logout()
}