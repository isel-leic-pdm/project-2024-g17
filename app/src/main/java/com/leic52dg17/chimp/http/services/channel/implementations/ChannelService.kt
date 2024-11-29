package com.leic52dg17.chimp.http.services.channel.implementations

import android.util.Log
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import com.leic52dg17.chimp.http.services.channel.IChannelService
import com.leic52dg17.chimp.http.services.channel.requests.CreateChannelInvitationRequest
import com.leic52dg17.chimp.http.services.channel.requests.CreateChannelRequest
import com.leic52dg17.chimp.http.services.channel.requests.UpdateUserChannelRequest
import com.leic52dg17.chimp.http.services.channel.responses.CreateChannelInvitationResponse
import com.leic52dg17.chimp.http.services.channel.responses.CreateChannelResponse
import com.leic52dg17.chimp.http.services.channel.responses.GetChannelResponse
import com.leic52dg17.chimp.http.services.channel.responses.GetChannelsResponse
import com.leic52dg17.chimp.http.services.common.ApiEndpoints
import com.leic52dg17.chimp.http.services.common.ProblemDetails
import com.leic52dg17.chimp.http.services.common.ServiceErrorTypes
import com.leic52dg17.chimp.http.services.common.ServiceException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import java.net.URL

class ChannelService(private val client: HttpClient) : IChannelService {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun createChannel(
        ownerId: Int,
        name: String,
        isPrivate: Boolean,
        channelIconUrl: String,
    ): Int {
        val uri = URL(ApiEndpoints.Channel.CREATE)
        val request = CreateChannelRequest(ownerId, name, isPrivate)

        val response = client.post(uri) {
            header("Accept", "application/json")
            header("Content-Type", "application/json")
            setBody(request)
        }

        if (!response.status.isSuccess()) {
            if (response.contentType() == ContentType.Application.ProblemJson) {
                val details = json.decodeFromString<ProblemDetails>(response.body())
                Log.e(TAG, " ${details.title} -> ${details.errors}")
                throw ServiceException(details.title, ServiceErrorTypes.Common)
            } else if (response.status == HttpStatusCode.Unauthorized) {
                Log.e(TAG, "Unauthorized: ${response.status}")
                throw ServiceException(ErrorMessages.UNAUTHORIZED, ServiceErrorTypes.Unauthorized)
            } else {
                throw ServiceException(ErrorMessages.UNKNOWN, ServiceErrorTypes.Unknown)
            }
        }
        val responseBody = json.decodeFromString<CreateChannelResponse>(response.body())
        return responseBody.channelId
    }

    override suspend fun createChannelInvitation(
        channelId: Int,
        senderId: Int,
        receiverId: Int,
        permissionLevel: PermissionLevel
    ): Int {
        val uri = URL(ApiEndpoints.ChannelInvitation.CREATE)
        val request =
            CreateChannelInvitationRequest(senderId, receiverId, channelId, permissionLevel)

        val response = client.post(uri) {
            header("Accept", "application/json")
            header("Content-Type", "application/json")
            setBody(request)
        }

        if (!response.status.isSuccess()) {
            if (response.contentType() == ContentType.Application.ProblemJson) {
                val details = json.decodeFromString<ProblemDetails>(response.body())
                Log.e(TAG, " ${details.title} -> ${details.errors}")
                throw ServiceException(details.title, ServiceErrorTypes.Common)
            } else if (response.status == HttpStatusCode.Unauthorized) {
                Log.e(TAG, "Unauthorized: ${response.status}")
                throw ServiceException(ErrorMessages.UNAUTHORIZED, ServiceErrorTypes.Unauthorized)
            } else {
                throw ServiceException(ErrorMessages.UNKNOWN, ServiceErrorTypes.Unknown)
            }
        }
        val responseBody = json.decodeFromString<CreateChannelInvitationResponse>(response.body())
        return responseBody.channelInvitationId
    }

    override suspend fun getUserSubscribedChannels(userId: Int): List<Channel> {
        val uri = URL(ApiEndpoints.Channel.GET_USER_SUBSCRIBED.replace("{id}", userId.toString()))

        val response = client.get(uri) {
            header("Accept", "application/json")
            header("Content-Type", "application/json")
        }

        if (!response.status.isSuccess()) {
            if (response.contentType() == ContentType.Application.ProblemJson) {
                val details = json.decodeFromString<ProblemDetails>(response.body())
                Log.e(TAG, " ${details.title} -> ${details.errors}")
                throw ServiceException(details.title, ServiceErrorTypes.Common)
            } else if (response.status == HttpStatusCode.Unauthorized) {
                Log.e(TAG, "Unauthorized: ${response.status}")
                throw ServiceException(ErrorMessages.UNAUTHORIZED, ServiceErrorTypes.Unauthorized)
            } else {
                Log.e(TAG, "ERROR: ${response.status}")
                throw ServiceException(ErrorMessages.UNKNOWN, ServiceErrorTypes.Unknown)
            }
        }
        val responseBody = json.decodeFromString<GetChannelsResponse>(response.body())

        val channelsWithEmptyUsersAndMessages = responseBody.channels.map { channel ->
            Channel(
                channelId = channel.id,
                displayName = channel.name,
                ownerId = channel.ownerId,
                isPrivate = channel.isPrivate,
                users = emptyList(),
                messages = emptyList(),
                channelIconUrl = "https://picsum.photos/300/300"
            )
        }

        return channelsWithEmptyUsersAndMessages
    }

    override suspend fun getChannelById(channelId: Int): Channel {
        val uri = URL(ApiEndpoints.Channel.GET_BY_ID.replace("{id}", channelId.toString()))

        val response = client.get(uri) {
            header("Accept", "application/json")
            header("Content-Type", "application/json")
        }

        if (!response.status.isSuccess()) {
            if (response.contentType() == ContentType.Application.ProblemJson) {
                val details = json.decodeFromString<ProblemDetails>(response.body())
                Log.e(TAG, " ${details.title} -> ${details.errors}")
                throw ServiceException(details.title, ServiceErrorTypes.Common)
            } else if (response.status == HttpStatusCode.Unauthorized) {
                Log.e(TAG, "Unauthorized: ${response.status}")
                throw ServiceException(ErrorMessages.UNAUTHORIZED, ServiceErrorTypes.Unauthorized)
            } else {
                throw ServiceException(ErrorMessages.UNKNOWN, ServiceErrorTypes.Unknown)
            }
        }
        val responseBody = json.decodeFromString<GetChannelResponse>(response.body())
        val channelWithoutMessagesOrUsers = Channel(
            displayName = responseBody.name,
            channelId = responseBody.id,
            ownerId = responseBody.ownerId,
            isPrivate = responseBody.isPrivate,
            users = emptyList(),
            messages = emptyList(),
            channelIconUrl = "https://picsum.photos/300/300"
        )
        return channelWithoutMessagesOrUsers
    }

    override suspend fun removeUserFromChannel(
        userId: Int,
        channelId: Int
    ): Int {
        val uri = URL(ApiEndpoints.Channel.REMOVE_USER_FROM_CHANNEL)
        val request = UpdateUserChannelRequest(userId, channelId)

        val response = client.put(uri) {
            header("Accept", "application/json")
            header("Content-Type", "application/json")
            setBody(request)
        }

        if (!response.status.isSuccess()) {
            if (response.contentType() == ContentType.Application.ProblemJson) {
                val details = json.decodeFromString<ProblemDetails>(response.body())
                Log.e(TAG, " ${details.title} -> ${details.errors}")
                throw ServiceException(details.title, ServiceErrorTypes.Common)
            } else if(response.status == HttpStatusCode.Unauthorized) {
                Log.e(TAG, "Unauthorized: ${response.status}")
                throw ServiceException(ErrorMessages.UNAUTHORIZED, ServiceErrorTypes.Unauthorized)
            } else {
                throw ServiceException(ErrorMessages.UNKNOWN, ServiceErrorTypes.Unknown)
            }
        }
        return userId
    }

    companion object {
        const val TAG = "CHANNEL_SERVICE"
    }
}