package com.leic52dg17.chimp.http.services.user

import com.leic52dg17.chimp.domain.model.user.User

interface IUserService {
    suspend fun getUserById(id: Int): User?
    suspend fun getAllUsers(username: String? = null, page: Int? = null, limit: Int? = null): List<User>
    suspend fun getChannelUsers(channelId: Int): List<User>
}