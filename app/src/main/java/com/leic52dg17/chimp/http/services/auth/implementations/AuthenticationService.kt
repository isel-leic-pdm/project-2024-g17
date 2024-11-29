package com.leic52dg17.chimp.http.services.auth.implementations

import android.util.Log
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.http.services.auth.IAuthenticationService
import com.leic52dg17.chimp.http.services.auth.requests.GetTokenRequest
import com.leic52dg17.chimp.http.services.auth.requests.UserChangePasswordRequest
import com.leic52dg17.chimp.http.services.auth.requests.UserSignUpRequest
import com.leic52dg17.chimp.http.services.auth.responses.GetTokenResponse
import com.leic52dg17.chimp.http.services.common.ApiEndpoints
import com.leic52dg17.chimp.http.services.common.ProblemDetails
import com.leic52dg17.chimp.http.services.common.ServiceErrorTypes
import com.leic52dg17.chimp.http.services.common.ServiceException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import java.net.URL

class AuthenticationService(private val client: HttpClient) : IAuthenticationService {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun loginUser(username: String, password: String): AuthenticatedUser {
        val source = URL(ApiEndpoints.Users.GET_TOKEN)
        val requestBody = GetTokenRequest(username, password)

        val response = client.post(source) {
            header("Accept", "application/json")
            header("Content-type", "application/json")
            setBody(requestBody)
        }

        if(!response.status.isSuccess()) {
            if(response.contentType() == ContentType.Application.ProblemJson) {
                val details = json.decodeFromString<ProblemDetails>(response.body())
                Log.e(TAG, " ${details.title} -> ${details.errors}")
                throw ServiceException(details.title, ServiceErrorTypes.Common)
            } else {
                Log.e(TAG, "Login: ${response.status}")
                throw ServiceException(ErrorMessages.UNKNOWN, ServiceErrorTypes.Unknown)
            }
        }

        val responseBody = response.body<GetTokenResponse>()
        val user = getUserByToken(responseBody.token)

        return AuthenticatedUser(responseBody.token, user)
    }

    override suspend fun signUpUser(username: String, displayName: String, password: String): AuthenticatedUser {
        val source = URL(ApiEndpoints.Users.CREATE_USER)
        Log.i(TAG, "Signup: $source")
        val requestBody = UserSignUpRequest(username, displayName, password)

        val response = client.post(source) {
            header("accept", "application/json")
            header("content-type", "application/json")
            setBody(requestBody)
        }

        if(!response.status.isSuccess()) {
            if(response.contentType() == ContentType.Application.ProblemJson) {
                val details = json.decodeFromString<ProblemDetails>(response.body())
                Log.e(TAG, " Signup: ${details.title} -> ${details.errors}")
                throw ServiceException(details.title, ServiceErrorTypes.Common)
            } else {
                Log.e(TAG, "${response.status}")
                throw ServiceException(ErrorMessages.UNKNOWN, ServiceErrorTypes.Unknown)
            }
        }

        return loginUser(username, password)
    }

    override suspend fun changePassword(
        username: String,
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): AuthenticatedUser {
        if (newPassword != confirmPassword) {
            throw ServiceException("Password confirmation does not match.", ServiceErrorTypes.Common)
        }

        val source = URL(ApiEndpoints.Users.CHANGE_PASSWORD)
        val requestBody = UserChangePasswordRequest(username, currentPassword, newPassword, confirmPassword)

        val response = client.post(source) {
            header("accept", "application/json")
            header("content-type", "application/json")
            setBody(requestBody)
        }

        if(!response.status.isSuccess()) {
            if(response.contentType() == ContentType.Application.ProblemJson) {
                val details = json.decodeFromString<ProblemDetails>(response.body())
                Log.e(TAG, "Change password: ${details.title} -> ${details.errors}")
                throw ServiceException(details.title, ServiceErrorTypes.Common)
            } else {
                throw ServiceException(ErrorMessages.UNKNOWN, ServiceErrorTypes.Unknown)
            }
        }

        return loginUser(username, newPassword)
    }

    override suspend fun forgotPassword(email: String): AuthenticatedUser {
        TODO("Not yet implemented")
    }

    private suspend fun getUserByToken(token: String): User {
        val replaced = ApiEndpoints.Users.GET_BY_TOKEN.replace("{token}", token)
        val source = URL(replaced)

        Log.i(TAG, "GET_BY_TOKEN: $replaced")

        val response = client.get(source) {
            header("accept", "application/json")
        }

        if(!response.status.isSuccess()) {
            if(response.contentType() == ContentType.Application.ProblemJson) {
                val details = json.decodeFromString<ProblemDetails>(response.body())
                Log.e(TAG, "GetUserByToken: ${details.title} -> ${details.errors}")
                throw ServiceException(details.title, ServiceErrorTypes.Common)
            } else {
                Log.e(TAG, "GetUserByToken: ${response.status}")
                throw ServiceException(ErrorMessages.UNKNOWN, ServiceErrorTypes.Unknown)
            }
        }

        return response.body<User>()
    }

    companion object {
        const val TAG = "AUTHENTICATION_SERVICE"
    }
}