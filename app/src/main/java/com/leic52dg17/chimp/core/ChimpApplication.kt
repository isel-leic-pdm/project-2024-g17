package com.leic52dg17.chimp.core

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.room.RoomDatabase
import com.leic52dg17.chimp.core.cache.channel.ChannelCacheManager
import com.leic52dg17.chimp.core.cache.message.MessageCacheManager
import com.leic52dg17.chimp.core.interceptors.AuthTokenInterceptor
import com.leic52dg17.chimp.core.repositories.channel.ChannelRepository
import com.leic52dg17.chimp.core.repositories.channel.IChannelRepository
import com.leic52dg17.chimp.core.repositories.common.AppDatabase
import com.leic52dg17.chimp.core.repositories.common.AppDatabaseManager
import com.leic52dg17.chimp.core.repositories.messages.IMessageRepository
import com.leic52dg17.chimp.core.repositories.messages.MessageRepository
import com.leic52dg17.chimp.core.repositories.user.UserInfoPreferencesRepository
import com.leic52dg17.chimp.core.repositories.user.IUserInfoRepository
import com.leic52dg17.chimp.http.services.auth.IAuthenticationService
import com.leic52dg17.chimp.http.services.auth.implementations.AuthenticationService
import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.channel.implementations.ChannelService
import com.leic52dg17.chimp.http.services.channel_invitations.IChannelInvitationService
import com.leic52dg17.chimp.http.services.channel_invitations.implementations.ChannelInvitationService
import com.leic52dg17.chimp.http.services.foreground.EventStreamService
import com.leic52dg17.chimp.http.services.message.IMessageService
import com.leic52dg17.chimp.http.services.message.implementations.MessageService
import com.leic52dg17.chimp.http.services.registration_invitation.IRegistrationInvitationService
import com.leic52dg17.chimp.http.services.registration_invitation.Implementations.RegistrationInvitationService
import com.leic52dg17.chimp.http.services.sse.ISSEService
import com.leic52dg17.chimp.http.services.sse.implementations.SSEService
import com.leic52dg17.chimp.http.services.user.IUserService
import com.leic52dg17.chimp.http.services.user.implementations.UserService
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.sse.SSE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json

const val TAG = "CHIMP"

interface DependenciesContainer {
    val preferencesDataStore: DataStore<Preferences>
    val roomDatabase: RoomDatabase
    val userInfoRepository: IUserInfoRepository
    val channelRepository: IChannelRepository
    val messageRepository: IMessageRepository
    val registrationInvitationService: IRegistrationInvitationService
    val channelService: IChannelService
    val messageService: IMessageService
    val authenticationService: IAuthenticationService
    val userService: IUserService
    val channelInvitationService: IChannelInvitationService
    val sseService: ISSEService
    val channelCacheManager: ChannelCacheManager
    val messageCacheManager: MessageCacheManager
    val applicationDatabaseManager: AppDatabaseManager
    val eventStreamService: EventStreamService
}

class ChimpApplication : Application(), DependenciesContainer {
    private val client by lazy {
        HttpClient(OkHttp) {
            defaultRequest {
                val interceptor = AuthTokenInterceptor(userInfoRepository)
                attributes.put(AuthTokenInterceptor.Key, interceptor)
                interceptor.intercept(this)
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

    override val preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

    override val roomDatabase: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "chimp-local-db").fallbackToDestructiveMigration().build()
    }

    override val userInfoRepository: IUserInfoRepository by lazy {
        UserInfoPreferencesRepository(preferencesDataStore)
    }

    override val messageRepository by lazy {
        MessageRepository(roomDatabase.messageDao())
    }

    override val channelRepository: IChannelRepository by lazy {
        ChannelRepository(messageRepository, roomDatabase.channelDao())
    }

    private val sseScope = CoroutineScope(Dispatchers.IO)

    override val channelService: IChannelService by lazy {
        ChannelService(client)
    }

    override val messageService: IMessageService by lazy {
        MessageService(client)
    }

    override val registrationInvitationService : IRegistrationInvitationService by lazy {
        RegistrationInvitationService(client)
    }

    override val authenticationService: IAuthenticationService by lazy {
        AuthenticationService(client)
    }

    override val userService: IUserService by lazy {
        UserService(client)
    }

    override val channelInvitationService: IChannelInvitationService by lazy {
        ChannelInvitationService(client)
    }

    override val sseService: ISSEService by lazy {
        SSEService(client, sseScope)
    }

    override val channelCacheManager by lazy {
        ChannelCacheManager(channelRepository, userInfoRepository)
    }

    override val messageCacheManager by lazy {
        MessageCacheManager(userInfoRepository, messageRepository)
    }

    override val applicationDatabaseManager: AppDatabaseManager by lazy {
        AppDatabaseManager(roomDatabase.messageDao(), roomDatabase.channelDao())
    }

    override val eventStreamService: EventStreamService by lazy {
        EventStreamService()
    }
}