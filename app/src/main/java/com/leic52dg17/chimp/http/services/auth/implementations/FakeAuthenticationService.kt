package com.leic52dg17.chimp.http.services.auth.implementations

import com.leic52dg17.chimp.http.services.auth.IAuthenticationService
import com.leic52dg17.chimp.http.services.auth.results.UserLoginResult
import com.leic52dg17.chimp.http.services.auth.results.UserSignUpResult
import com.leic52dg17.chimp.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.model.common.success
import com.leic52dg17.chimp.model.user.User
import kotlinx.coroutines.delay

class FakeAuthenticationService : IAuthenticationService {
    override suspend fun loginUser(username: String, password: String): UserLoginResult {
        return success(
            AuthenticatedUser(
                "example_token",
                User(
                    1,
                    "username1",
                    "User 1"
                )
            )
        )
    }

    override suspend fun signUpUser(username: String, password: String): UserSignUpResult {
        delay(2000)
        return success(
            AuthenticatedUser(
                "example_token",
                User(
                    1,
                    "username1",
                    "User 1"
                )
            )
        )
    }
}