package com.leic52dg17.chimp.core.cache.user

import com.leic52dg17.chimp.domain.model.user.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

class UserCacheManager: IUserCacheManager {
    private var currentUser =  MutableStateFlow<User?>(null)

    override suspend fun cacheUser(user: User) {
        currentUser.emit(user)
    }

    override suspend fun getCachedUser(): User? {
        return currentUser.first()
    }
}