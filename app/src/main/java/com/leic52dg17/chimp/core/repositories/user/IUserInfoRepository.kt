package com.leic52dg17.chimp.core.repositories.user

import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import kotlinx.coroutines.flow.Flow

interface IUserInfoRepository {
    val authenticatedUser: Flow<AuthenticatedUser?>
    suspend fun saveAuthenticatedUser(authenticatedUser: AuthenticatedUser)
    suspend fun clearAuthenticatedUser()
    suspend fun checkTokenValidity(): Boolean
}