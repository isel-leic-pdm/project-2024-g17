package com.leic52dg17.chimp.core.repositories.user

import com.leic52dg17.chimp.core.repositories.user.database.UserDAO
import com.leic52dg17.chimp.core.repositories.user.model.UserEntity
import com.leic52dg17.chimp.domain.model.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val userDao: UserDAO
) : IUserRepository {
    override suspend fun storeUsers(users: List<User>) = withContext(Dispatchers.IO) {
        val userEntities = users.map {
            UserEntity(
                id = it.id,
                username = it.username,
                displayName = it.displayName
            )
        }

        userDao.insertAll(userEntities)
    }

    override suspend fun storeUser(user: User) = withContext(Dispatchers.IO) {
        val userEntity = UserEntity(
            id = user.id,
            username = user.username,
            displayName = user.displayName
        )

        userDao.insert(userEntity)
    }

    override suspend fun removeUser(user: User) = withContext(Dispatchers.IO) {
        val userEntity = UserEntity(
            id = user.id,
            username = user.username,
            displayName = user.displayName
        )

        userDao.delete(userEntity)
    }

    override suspend fun getStoredUsers(): List<User> = withContext(Dispatchers.IO) {
        val userEntities = userDao.getAll()

        userEntities.map { user ->
            User(
                id = user.id,
                username = user.username,
                displayName = user.displayName
            )
        }
    }

    override suspend fun getUserById(id: Int): User? = withContext(Dispatchers.IO) {
        val userEntity = userDao.getById(id)
            ?: return@withContext null

        User(
            id = userEntity.id,
            username = userEntity.username,
            displayName = userEntity.displayName
        )
    }
}