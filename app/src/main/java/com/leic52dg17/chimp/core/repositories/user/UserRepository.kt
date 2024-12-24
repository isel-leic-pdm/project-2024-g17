package com.leic52dg17.chimp.core.repositories.user

import com.leic52dg17.chimp.core.repositories.user.database.UserDAO
import com.leic52dg17.chimp.core.repositories.user.model.UserEntity
import com.leic52dg17.chimp.domain.model.user.User

class UserRepository(
    private val userDao: UserDAO
) : IUserRepository {
    override suspend fun storeUsers(users: List<User>) {
        val userEntities = users.map {
            UserEntity(
                id = it.id,
                username = it.username,
                displayName = it.displayName
            )
        }

        userDao.insertAll(userEntities)
    }

    override suspend fun removeUser(user: User) {
        val userEntity = UserEntity(
            id = user.id,
            username = user.username,
            displayName = user.displayName
        )

        userDao.delete(userEntity)
    }

    override suspend fun getStoredUsers(): List<User> {
        val userEntities = userDao.getAll()

        return userEntities.map { user ->
            User(
                id = user.id,
                username = user.username,
                displayName = user.displayName
            )
        }
    }
}