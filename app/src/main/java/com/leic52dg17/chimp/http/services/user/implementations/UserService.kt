package com.leic52dg17.chimp.http.services.user.implementations

import android.util.Log
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.http.services.common.ApiEndpoints
import com.leic52dg17.chimp.http.services.common.ProblemDetails
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.http.services.message.implementations.MessageService
import com.leic52dg17.chimp.http.services.message.implementations.MessageService.Companion
import com.leic52dg17.chimp.http.services.user.IUserService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URL

class UserService(private val client: HttpClient): IUserService{
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getUserById(id: Int): User {
        val source = URL(ApiEndpoints.Users.GET_BY_ID.replace("{id}", id.toString()))

        val response = client.get(source) {
            header("Accept", "application/json")
            header("Content-Type", "application/json")
        }

        if(!response.status.isSuccess()) {
            if(response.contentType() == ContentType.Application.ProblemJson) {
                val details = json.decodeFromString<ProblemDetails>(response.body())
                throw ServiceException(details.title)
            } else {
                Log.e(MessageService.TAG, response.status.toString())
                throw ServiceException(ErrorMessages.UNKNOWN)
            }
        }

        return response.body<UserDto>().toUser()
    }

    override suspend fun getAllUsers(): List<User> {
        val source = URL(ApiEndpoints.Users.GET_ALL)

        val response = client.get(source) {
            header("Accept", "application/json")
            header("Content-Type", "application/json")
        }

        if(!response.status.isSuccess()) {
            if(response.contentType() == ContentType.Application.ProblemJson) {
                val details = json.decodeFromString<ProblemDetails>(response.body())
                Log.e(TAG, " ${details.title} -> ${details.errors}")
                throw ServiceException(details.title)
            } else {
                Log.e(TAG, response.status.toString())
                throw ServiceException(ErrorMessages.UNKNOWN)
            }
        }

        val userResponse = response.body<UserResponse>()
        return userResponse.users.map { it.toUser() }
    }

    override suspend fun getChannelUsers(channelId: Int): List<User> {
        val source = URL(ApiEndpoints.Users.GET_BY_CHANNEL.replace("{id}", channelId.toString()))

        val response = client.get(source) {
            header("Accept", "application/json")
            header("Content-Type", "application/json")
        }

        if(!response.status.isSuccess()) {
            if(response.contentType() == ContentType.Application.ProblemJson) {
                val details = json.decodeFromString<ProblemDetails>(response.body())
                Log.e(TAG, " ${details.title} -> ${details.errors}")
                throw ServiceException(details.title)
            } else {
                Log.e(TAG, response.status.toString())
                throw ServiceException(ErrorMessages.UNKNOWN)
            }
        }

        Log.i(TAG, response.body<String>().toString())
        val userResponse = response.body<UserResponse>()
        return userResponse.users.map { it.toUser() }
    }

    @Serializable
    private data class UserDto(val id: Int, val username: String, val displayName: String) {
        fun toUser() = User(id = id, username = username, displayName = displayName)
    }

    @Serializable
    private data class UserResponse(val users: List<UserDto>)

    companion object {
        const val TAG = "USER_SERVICE"
    }
}