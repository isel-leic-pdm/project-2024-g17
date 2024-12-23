package com.leic52dg17.chimp.utils

import com.leic52dg17.chimp.core.cache.channel.ChannelCacheManager
import com.leic52dg17.chimp.core.cache.message.MessageCacheManager
import com.leic52dg17.chimp.core.repositories.channel.IChannelRepository
import com.leic52dg17.chimp.core.repositories.messages.IMessageRepository
import com.leic52dg17.chimp.core.repositories.user.IUserInfoRepository
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.channel.ChannelInvitation
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import com.leic52dg17.chimp.domain.model.common.success
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.http.services.auth.IAuthenticationService
import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.channel_invitations.IChannelInvitationService
import com.leic52dg17.chimp.http.services.fake.FakeData
import com.leic52dg17.chimp.http.services.message.IMessageService
import com.leic52dg17.chimp.http.services.registration_invitation.IRegistrationInvitationService
import com.leic52dg17.chimp.http.services.sse.ISSEService
import com.leic52dg17.chimp.http.services.sse.events.Events
import com.leic52dg17.chimp.http.services.sse.results.SSEServiceResult
import com.leic52dg17.chimp.http.services.user.IUserService
import com.leic52dg17.chimp.receivers.IConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.time.Instant

val testAuthenticatedUser = AuthenticatedUser(
    "test_token",
    User(1, "test_user", "testDisplay")
)

// Test dependencies
val fakeAuthenticationService by lazy {
    object : IAuthenticationService {
        override suspend fun logout() {}

        override suspend fun forgotPassword(email: String): AuthenticatedUser =
            testAuthenticatedUser

        override suspend fun loginUser(username: String, password: String): AuthenticatedUser =
            testAuthenticatedUser

        override suspend fun changePassword(
            username: String,
            currentPassword: String,
            newPassword: String,
            confirmPassword: String
        ): AuthenticatedUser = testAuthenticatedUser

        override suspend fun signUpUser(
            username: String,
            displayName: String,
            password: String
        ): AuthenticatedUser = testAuthenticatedUser
    }
}

val fakeChannelService by lazy {
    object : IChannelService {
        override suspend fun createChannel(
            ownerId: Int,
            name: String,
            isPrivate: Boolean,
            channelIconUrl: String
        ): Int = 1

        override suspend fun createChannelInvitation(
            channelId: Int,
            senderId: Int,
            receiverId: Int,
            permissionLevel: PermissionLevel
        ): Int = 1

        override suspend fun getUserSubscribedChannels(userId: Int): List<Channel> {
            return FakeData.channels.filter { it.users.any { user -> user.id == userId } }
        }

        override suspend fun getChannelById(channelId: Int): Channel {
            return FakeData.channels.first { channel -> channel.channelId == channelId }
        }

        override suspend fun removeUserFromChannel(userId: Int, channelId: Int): Int = 1

        override suspend fun getUserPermissionsByChannelId(
            userId: Int,
            channelId: Int
        ): PermissionLevel = PermissionLevel.RW

        override suspend fun getPublicChannels(
            channelName: String,
            page: Int?,
            limit: Int?
        ): List<Channel> {
            return FakeData.channels.filter { !it.isPrivate }
        }

        override suspend fun addUserToChannel(userId: Int, channelId: Int) {}
    }
}
val fakeUserService by lazy {
    object : IUserService {
        override suspend fun getUserById(id: Int): User? =
            FakeData.users.firstOrNull { it.id == id }

        override suspend fun getAllUsers(
            username: String?,
            page: Int?,
            limit: Int?
        ): List<User> = FakeData.users

        override suspend fun getChannelUsers(channelId: Int): List<User> =
            FakeData.channels.find { it.channelId == channelId }?.users ?: emptyList()
    }
}

val fakeMessageService by lazy {
    object : IMessageService {
        override suspend fun getChannelMessages(channelId: Int): List<Message> =
            FakeData.channels.find { it.channelId == channelId }?.messages ?: emptyList()

        override suspend fun createMessageInChannel(
            messageText: String,
            channelId: Int,
            senderId: Int
        ): Int = 1

        override suspend fun getMessageById(id: Int): Message =
            Message(1, 1, 1, "test_message", Instant.EPOCH.epochSecond)
    }
}

val fakeChannelInvitationService by lazy {
    object : IChannelInvitationService {
        override suspend fun getChannelInvitationById(invitationId: Int): ChannelInvitation =
            FakeData.channelInvitations.find { it.id == invitationId } ?: ChannelInvitation(
                1,
                1,
                1,
                2,
                PermissionLevel.RW
            )

        override suspend fun getChannelInvitationsByReceiverId(receiverId: Int): List<ChannelInvitation> =
            FakeData.channelInvitations.filter { it.receiverId == receiverId }

        override suspend fun createChannelInvitation(
            senderId: Int,
            receiverId: Int,
            channelId: Int,
            permissionLevel: PermissionLevel
        ): Int = 1

        override suspend fun acceptChannelInvitation(invitationId: Int) {}

        override suspend fun rejectChannelInvitation(invitationId: Int) {}
    }
}

val fakeChannelCacheManager by lazy {
    ChannelCacheManager(
        fakeChannelRepository,
        fakeUserInfoRepository
    )
}

val fakeMessageCacheManager by lazy {
    MessageCacheManager(fakeUserInfoRepository, fakeMessageRepository)
}

val fakeChannelRepository by lazy {
    object : IChannelRepository {
        override suspend fun storeChannels(channels: List<Channel>) {}

        override suspend fun removeChannel(channel: Channel) {}

        override suspend fun getStoredChannels(): List<Channel> = FakeData.channels.filter { it.users.any { user -> user.id == testAuthenticatedUser.user?.id } }

        override suspend fun getDifferences(old: List<Channel>, new: List<Channel>): List<Channel> = emptyList()

        override suspend fun isUpdateDue(
            oldCache: List<Channel>,
            newCache: List<Channel>
        ): Boolean = false
    }
}

val fakeMessageRepository by lazy {
    object: IMessageRepository {
        override suspend fun storeMessages(messages: List<Message>) {}

        override suspend fun getStoredMessages(): List<Message> = FakeData.channels.map { it.messages }.flatten()

        override suspend fun getStoredMessagesForChannel(channelId: Int): List<Message> = fakeMessageService.getChannelMessages(channelId)

        override suspend fun getDifferences(old: List<Message>, new: List<Message>): List<Message> = emptyList()

        override suspend fun isUpdateDue(
            oldCache: List<Message>,
            newCache: List<Message>
        ): Boolean = false
    }
}

val fakeSseService by lazy {
    object : ISSEService {
        override val eventFlow: MutableSharedFlow<Events> = MutableSharedFlow()

        override fun listen(): SSEServiceResult = success(Unit)

        override fun stopListening() {}
    }
}

val fakeRegistrationInvitationService by lazy {
    object : IRegistrationInvitationService {
        override suspend fun createRegistrationInvitation(creatorId: Int): Int = 1

        override suspend fun getRegistrationInvitation(inviteId: Int): String = "test_token"
    }
}

val fakeUserInfoRepository by lazy {
    object : IUserInfoRepository {
        override val authenticatedUser: Flow<AuthenticatedUser?> = flow {
            emit(testAuthenticatedUser)
        }

        override suspend fun saveAuthenticatedUser(authenticatedUser: AuthenticatedUser) {}

        override suspend fun clearAuthenticatedUser() {}

        override suspend fun checkTokenValidity(): Boolean = true
    }
}

val fakeConnectivityObserver by lazy {
    object : IConnectivityObserver {
        override val connectivityStatusFlow: MutableStateFlow<Boolean>
            get() = MutableStateFlow(true)

        override fun startObserving(
            onLostCallback: () -> Unit,
            onAvailableCallback: () -> Unit,
            scope: CoroutineScope
        ) {
            scope.launch {
                delay(2000)
                connectivityStatusFlow.value = false
                onLostCallback()

                delay(2000)
                connectivityStatusFlow.value = true
                onAvailableCallback()
            }
        }

    }
}