package com.leic52dg17.chimp.core.repositories.user

import com.leic52dg17.chimp.domain.model.user.User

interface IUserRepository {
    suspend fun storeUsers(users: List<User>)
    suspend fun removeUser(user: User)
    suspend fun getStoredUsers(): List<User>
}