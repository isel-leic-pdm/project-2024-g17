package com.leic52dg17.chimp.core

import android.app.Application
import com.leic52dg17.chimp.core.shared.SharedPreferencesHelper
import com.leic52dg17.chimp.http.services.auth.IAuthenticationService
import com.leic52dg17.chimp.http.services.auth.implementations.FakeAuthenticationService
import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.channel.implementations.FakeChannelService
import com.leic52dg17.chimp.http.services.message.IMessageService
import com.leic52dg17.chimp.http.services.message.implementations.FakeMessageService
import com.leic52dg17.chimp.http.services.sse.ISSEService
import com.leic52dg17.chimp.http.services.sse.implementations.SSEService
import com.leic52dg17.chimp.http.services.user.IUserService
import com.leic52dg17.chimp.http.services.user.implementations.UserService
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json

const val TAG = "CHIMP"

interface DependenciesContainer {
    val channelService: IChannelService
    val messageService: IMessageService
    val authenticationService: IAuthenticationService
    val userService: IUserService
    val sseService: ISSEService
}

class ChimpApplication : Application(), DependenciesContainer {
    private val client by lazy {
        HttpClient(OkHttp) {
            val authToken = SharedPreferencesHelper.getAuthenticatedUser(applicationContext)?.authenticationToken
            defaultRequest {
                header("Authorization", "Bearer $authToken")
            }
            install(SSE)
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }

    private val sseScope = CoroutineScope(Dispatchers.IO)

    override val channelService: IChannelService by lazy {
        FakeChannelService()
    }

    override val messageService: IMessageService by lazy {
        FakeMessageService()
    }

    override val authenticationService: IAuthenticationService by lazy {
        FakeAuthenticationService()
    }

    override val userService: IUserService by lazy {
        UserService(client)
    }

    override val sseService: ISSEService by lazy {
        SSEService(client, sseScope)
    }

}