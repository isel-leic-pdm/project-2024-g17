package com.leic52dg17.chimp.http.services.channel_invitations.implementations

import android.util.Log
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.channel.ChannelInvitation
import com.leic52dg17.chimp.domain.model.common.PermissionLevel
import com.leic52dg17.chimp.http.services.channel_invitations.responses.GetChannelInvitationResponse
import com.leic52dg17.chimp.http.services.channel_invitations.responses.GetChannelInvitationsResponse
import com.leic52dg17.chimp.http.services.channel_invitations.IChannelInvitationService
import com.leic52dg17.chimp.http.services.channel_invitations.requests.CreateChannelInvitationRequest
import com.leic52dg17.chimp.http.services.channel_invitations.responses.CreateChannelInvitationResponse
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

class ChannelInvitationService(private val client: HttpClient): IChannelInvitationService {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getChannelInvitationById(invitationId: Int): ChannelInvitation {
        val uri = URL(ApiEndpoints.ChannelInvitation.GET_BY_ID.replace("{id}", invitationId.toString()))

        val response = client.get(uri) {
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

        val responseBody = json.decodeFromString<GetChannelInvitationResponse>(response.body())

        return ChannelInvitation(
            id = responseBody.id,
            channelId = responseBody.channelId,
            senderId = responseBody.senderId,
            receiverId = responseBody.receiverId,
            permissionLevel = parsePermissionLevel(responseBody.permissionLevel),
        )
    }

    override suspend fun getChannelInvitationsByReceiverId(receiverId: Int): List<ChannelInvitation> {
        val uri = URL(ApiEndpoints.ChannelInvitation.GET_BY_USER_ID.replace("{id}", receiverId.toString()))

        val response = client.get(uri) {
            header("Content-Type", "application/json")
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

        val responseBody = json.decodeFromString<GetChannelInvitationsResponse>(response.body())

        val invitations = responseBody.channelInvitations.map { invitations ->
            invitations.toChannelInvitation()
        }

        return invitations
    }

    override suspend fun createChannelInvitation(
        senderId: Int,
        receiverId: Int,
        channelId: Int,
        permissionLevel: PermissionLevel
    ): Int {
        val uri = URL(ApiEndpoints.ChannelInvitation.CREATE)

        val request = CreateChannelInvitationRequest(senderId, receiverId, channelId, permissionLevel)

        val response = client.post(uri) {
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

    override suspend fun acceptChannelInvitation(invitationId: Int) {
        val uri = URL(ApiEndpoints.ChannelInvitation.ACCEPT.replace("{id}", invitationId.toString()))

        val response = client.put(uri) {
            header("Content-Type", "application/json")
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
    }

    override suspend fun rejectChannelInvitation(invitationId: Int) {
        val uri = URL(ApiEndpoints.ChannelInvitation.REJECT.replace("{id}", invitationId.toString()))

        val response = client.put(uri) {
            header("Content-Type", "application/json")
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
    }

    private fun parsePermissionLevel(permissionLevel: String): PermissionLevel {
        return when (permissionLevel) {
            "RR" -> PermissionLevel.RR
            "RW" -> PermissionLevel.RW
            else -> throw IllegalStateException("Unknown permission level")
        }
    }

    companion object {
        const val TAG = "CHANNEL_INVITATION_SERVICE"
    }
}