package com.leic52dg17.chimp.http.services.auth

import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.http.services.auth.results.UserLoginResult
import com.leic52dg17.chimp.http.services.auth.results.UserSignUpResult
import com.leic52dg17.chimp.http.services.auth.results.UserChangePasswordResult
import com.leic52dg17.chimp.http.services.auth.results.UserForgotPasswordResult

interface IAuthenticationService {
    suspend fun loginUser(username: String, password: String): AuthenticatedUser
    suspend fun signUpUser(username: String, password: String): AuthenticatedUser
    suspend fun changePassword(username: String, currentPassword: String, newPassword: String, confirmPassword: String): AuthenticatedUser
    suspend fun forgotPassword(email: String): AuthenticatedUser
}