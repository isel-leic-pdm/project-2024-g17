package com.leic52dg17.chimp.core.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant

class UserInfoPreferencesRepository(private val store: DataStore<Preferences>): UserInfoRepository {
    private val authenticatedUserKey = stringPreferencesKey("authenticated_user")

    override val authenticatedUser: Flow<AuthenticatedUser?> =
        store.data.map { preferences ->
            preferences[authenticatedUserKey]?.let { Json.decodeFromString(it) }
        }

    override suspend fun saveAuthenticatedUser(authenticatedUser: AuthenticatedUser) {
        val userJson = Json.encodeToString(authenticatedUser)
        store.edit { preferences ->
            preferences[authenticatedUserKey] = userJson
        }
    }

    override suspend fun clearAuthenticatedUser() {
        store.edit { preferences ->
            preferences.remove(authenticatedUserKey)
        }
    }

    override suspend fun checkTokenValidity(): Boolean {
        val preferences = store.data.first()
        val userJson = preferences[authenticatedUserKey]
        return userJson?.let {
            val authenticatedUser = Json.decodeFromString<AuthenticatedUser>(it)
            authenticatedUser.tokenExpirationDate?.let { expirationDate ->
                expirationDate >= Instant.EPOCH.epochSecond
            } ?: false
        } ?: false
    }


//    suspend fun getAuthenticatedUser(): AuthenticatedUser? {
//        val preferences = store.data.first()
//        val userJson = preferences[authenticatedUserKey]
//        return userJson?.let { Json.decodeFromString(it) }
//    }
}