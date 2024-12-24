package com.leic52dg17.chimp.core.cache.user

import com.leic52dg17.chimp.domain.model.user.User

interface IUserCacheManager {
    suspend fun cacheUser(user: User)
    suspend fun getCachedUser(): User?
}