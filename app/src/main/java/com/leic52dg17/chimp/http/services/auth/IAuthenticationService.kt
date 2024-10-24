package com.leic52dg17.chimp.http.services.auth

import com.leic52dg17.chimp.http.services.auth.results.UserLoginResult

interface IAuthenticationService {
    suspend fun loginUser(
        username: String,
        password: String
    ): UserLoginResult
}