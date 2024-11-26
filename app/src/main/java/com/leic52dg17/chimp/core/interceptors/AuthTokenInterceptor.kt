package com.leic52dg17.chimp.core.interceptors

import android.content.Context
import com.leic52dg17.chimp.core.shared.SharedPreferencesHelper
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import io.ktor.util.AttributeKey

class AuthTokenInterceptor(private val context: Context) {
    companion object {
        val Key = AttributeKey<AuthTokenInterceptor>("AuthTokenInterceptor")
    }

    fun intercept(builder: DefaultRequest.DefaultRequestBuilder) {
        val authToken = SharedPreferencesHelper.getAuthenticatedUser(context)?.authenticationToken
        builder.header("Authorization", "Bearer $authToken")
    }
}