/*
package com.leic52dg17.chimp.http.services.auth.implementations

import com.leic52dg17.chimp.http.services.auth.IAuthenticationService
import com.leic52dg17.chimp.http.services.auth.results.UserChangePasswordResult
import com.leic52dg17.chimp.http.services.auth.results.UserForgotPasswordResult
import com.leic52dg17.chimp.http.services.auth.results.UserLoginError
import com.leic52dg17.chimp.http.services.auth.results.UserLoginResult
import com.leic52dg17.chimp.http.services.auth.results.UserSignUpResult
import com.leic52dg17.chimp.http.services.fake.FakeData
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.common.Either
import com.leic52dg17.chimp.domain.model.common.success
import com.leic52dg17.chimp.domain.model.user.User

class FakeAuthenticationService : IAuthenticationService {
    override suspend fun loginUser(username: String, password: String): UserLoginResult {
        val user = FakeData.users.firstOrNull { it.username == username }
        if (user == null) return Either.Left(
            UserLoginError.AuthenticationError("User could not be found.")
        )

        return Either.Right(
            AuthenticatedUser(
                "oLgxYQkaGLyoUGVnunD6XLD5TXDVa1EDDEMrdkh7bTE=",
                user
            )
        )
    }

    override suspend fun signUpUser(username: String, password: String): UserSignUpResult {
        val user = User(
            FakeData.users.size + 1,
            username,
            username
        )
        FakeData.users.add(user)
        return success(
            AuthenticatedUser(
                "example_token",
                user
            )
        )
    }

    override suspend fun changePassword(
        username: String,
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): UserChangePasswordResult {
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

    override suspend fun forgotPassword(email: String): UserForgotPasswordResult {
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
}*/
