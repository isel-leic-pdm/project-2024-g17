package com.leic52dg17.chimp.http.services.auth.implementations

import android.util.Log
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.common.Failure
import com.leic52dg17.chimp.domain.model.common.Success
import com.leic52dg17.chimp.domain.model.common.failure
import com.leic52dg17.chimp.domain.model.common.success
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.http.services.auth.IAuthenticationService
import com.leic52dg17.chimp.http.services.auth.requests.GetTokenRequest
import com.leic52dg17.chimp.http.services.auth.requests.UserChangePasswordRequest
import com.leic52dg17.chimp.http.services.auth.requests.UserSignUpRequest
import com.leic52dg17.chimp.http.services.auth.responses.GetTokenResponse
import com.leic52dg17.chimp.http.services.auth.results.UserChangePasswordResult
import com.leic52dg17.chimp.http.services.auth.results.UserChangePasswordError
import com.leic52dg17.chimp.http.services.auth.results.UserForgotPasswordResult
import com.leic52dg17.chimp.http.services.auth.results.UserLoginError
import com.leic52dg17.chimp.http.services.auth.results.UserLoginResult
import com.leic52dg17.chimp.http.services.auth.results.UserSignUpError
import com.leic52dg17.chimp.http.services.auth.results.UserSignUpResult
import com.leic52dg17.chimp.http.services.common.ApiEndpoints
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess
import java.net.URL

class AuthenticationService(private val client: HttpClient) : IAuthenticationService {
    override suspend fun loginUser(username: String, password: String): UserLoginResult {
        val source = URL(ApiEndpoints.Users.GET_TOKEN)
        val requestBody = GetTokenRequest(username, password)

        val response = client.post(source) {
            header("accept", "application/json")
            header("content-type", "application/json")
            setBody(requestBody)
        }

        if (!response.status.isSuccess()) {
            Log.e(TAG, "Error logging in user: ${response.status}")
            return failure(UserLoginError.AuthenticationError("Invalid credentials"))
        }

        val responseBody = response.body<GetTokenResponse>()
        val user = getUserByToken(responseBody.token)

        return success(AuthenticatedUser(responseBody.token, user))
    }

    override suspend fun signUpUser(username: String, password: String): UserSignUpResult {
        val source = URL(ApiEndpoints.Users.CREATE_USER)
        val requestBody = UserSignUpRequest(username, password)

        val response = client.post(source) {
            header("accept", "application/json")
            header("content-type", "application/json")
            setBody(requestBody)
        }

        if (!response.status.isSuccess()) {
            Log.e(TAG, "Error signing up user: ${response.status}")
            return failure(UserSignUpError.AuthenticationError("Error signing up"))
        }

        return when (val res = loginUser(username, password)) {
            is Failure -> {
                Log.e(TAG, "Error logging in user: ${res.value.message}")
                failure(UserSignUpError.AuthenticationError("Error logging in"))
            }

            is Success -> {
                Log.i(TAG, "User signed up successfully")
                success(res.value)
            }
        }
    }

    override suspend fun changePassword(
        username: String,
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): UserChangePasswordResult {
        if (newPassword != confirmPassword) {
            return failure(
                UserChangePasswordError.AuthenticationError("New password and confirm password fields do not match")
            )
        }

        val source = URL(ApiEndpoints.Users.CHANGE_PASSWORD)
        val requestBody = UserChangePasswordRequest(username, currentPassword, newPassword, confirmPassword)

        val response = client.post(source) {
            header("accept", "application/json")
            header("content-type", "application/json")
            setBody(requestBody)
        }

        if (!response.status.isSuccess()) {
            Log.e(TAG, "Error changing $username's password: ${response.status}")
            return failure(UserChangePasswordError.AuthenticationError("Error changing password"))
        }

        return when (val res = loginUser(username, newPassword)) {
            is Failure -> {
                Log.e(TAG, "Error logging in user: ${res.value.message}")
                failure(UserChangePasswordError.AuthenticationError("Error logging in"))
            }

            is Success -> {
                Log.i(TAG, "User signed up successfully")
                success(res.value)
            }
        }
    }

    override suspend fun forgotPassword(email: String): UserForgotPasswordResult {
        TODO("Not yet implemented")
    }

    private suspend fun getUserByToken(token: String): User {
        val source = URL(ApiEndpoints.Users.GET_BY_TOKEN.replace("{token}", token))
        val response = client.get(source) {
            header("accept", "application/json")
        }

        if (!response.status.isSuccess()) {
            Log.e(TAG, "Error getting user with token: ${token}, status: ${response.status}")
            throw Exception("Error fetching user by token")
        }

        return response.body<User>()
    }

    companion object {
        const val TAG = "AUTHENTICATION_SERVICE"
    }
}