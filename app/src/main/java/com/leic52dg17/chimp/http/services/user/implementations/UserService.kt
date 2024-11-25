package com.leic52dg17.chimp.http.services.user.implementations

import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.http.services.common.ApiEndpoints
import com.leic52dg17.chimp.http.services.user.IUserService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import java.net.URL

class UserService(private val client: HttpClient): IUserService{
    override suspend fun getUserById(id: Int): User {
        val source = URL(ApiEndpoints.Users.GET_BY_ID.replace("{id}", id.toString()))

        val response = client.get(source) { header("accept", "application/json") }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to fetch user with id $id.")
        }

        return response.body<UserDto>().toUser()
    }

    override suspend fun getAllUsers(): List<User> {
        val source = URL(ApiEndpoints.Users.GET_ALL)

        val response = client.get(source) { header("accept", "application/json") }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to fetch users.")
        }

        val userResponse = response.body<UserResponse>()
        return userResponse.users.map { it.toUser() }
    }

    @Serializable
    private data class UserDto(val id: Int, val username: String, val displayName: String) {
        fun toUser() = User(id = id, username = username, displayName = displayName)
    }

    @Serializable
    private data class UserResponse(val users: List<UserDto>)
}