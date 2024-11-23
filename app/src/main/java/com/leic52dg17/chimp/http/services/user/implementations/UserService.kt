package com.leic52dg17.chimp.http.services.user.implementations

import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.http.services.common.ApiEndpoints
import com.leic52dg17.chimp.http.services.user.IUserService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.serialization.Serializable
import java.net.URL

class UserService(private val client: HttpClient): IUserService{
    override suspend fun getUserById(id: Int): User {
        val source = URL(ApiEndpoints.Users.GET_ALL)

        return client.get(source) { header("accept", "application/json") }
            .body<UserDto>()
            .toUser(source)
    }

    override suspend fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }

    @Serializable
    private data class UserDto(val id: Int, val username: String, val displayName: String) {
        fun toUser(source: URL) = User(userId = id, username = username, displayName = displayName)
    }
}