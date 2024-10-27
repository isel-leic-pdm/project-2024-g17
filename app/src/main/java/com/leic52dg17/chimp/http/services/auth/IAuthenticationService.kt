package com.leic52dg17.chimp.http.services.auth

import com.leic52dg17.chimp.http.services.auth.results.UserLoginResult
import com.leic52dg17.chimp.http.services.auth.results.UserSignUpResult

interface IAuthenticationService {
    suspend fun loginUser(username: String, password: String): UserLoginResult
    suspend fun signUpUser(username: String, password: String): UserSignUpResult
}