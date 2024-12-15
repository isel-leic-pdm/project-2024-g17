package com.leic52dg17.chimp.core.interceptors

import com.leic52dg17.chimp.core.repositories.UserInfoRepository
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import io.ktor.util.AttributeKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AuthTokenInterceptor(private val userInfoRepository: UserInfoRepository) {
    companion object {
        val Key = AttributeKey<AuthTokenInterceptor>("AuthTokenInterceptor")
    }

    fun intercept(builder: DefaultRequest.DefaultRequestBuilder) {
        runBlocking {
            withContext(Dispatchers.IO) {
                val authenticatedUser = userInfoRepository.authenticatedUser.first()
                if (authenticatedUser?.authenticationToken != null) {
                    builder.header("Authorization", "Bearer ${authenticatedUser.authenticationToken}")
                }
            }
        }
    }
}