package com.leic52dg17.chimp.http.services.registration_invitation.Implementations

import com.leic52dg17.chimp.http.services.channel.implementations.ChannelService.Companion.TAG
import com.leic52dg17.chimp.http.services.common.ApiEndpoints
import com.leic52dg17.chimp.http.services.common.handleServiceResponse
import com.leic52dg17.chimp.http.services.registration_invitation.IRegistrationInvitationService
import com.leic52dg17.chimp.http.services.registration_invitation.requests.CreateRegistrationInvitationRequest
import com.leic52dg17.chimp.http.services.registration_invitation.response.CreateRegistrationInvitationResponse
import com.leic52dg17.chimp.http.services.registration_invitation.response.GetRegistrationInvitationResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.json.Json
import java.net.URL

class RegistrationInvitationService(private val client: HttpClient) : IRegistrationInvitationService {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun createRegistrationInvitation(creatorId: Int): Int {
        val uri = URL(ApiEndpoints.RegistrationInvitation.CREATE)
        val request = CreateRegistrationInvitationRequest(creatorId)

        val response = client.post(uri) {
            header("Accept", "application/json")
            header("Content-Type", "application/json")
            setBody(request)
        }

        handleServiceResponse(response, json, TAG)

        val responseBody = json.decodeFromString<CreateRegistrationInvitationResponse>(response.body())
        return responseBody.inviteId

    }

    override suspend fun getRegistrationInvitation(inviteId: Int): String {
        val uri = URL(ApiEndpoints.RegistrationInvitation.GET_BY_ID.replace("{id}", inviteId.toString()))
        val response = client.get(uri) {
            header("Accept", "application/json")
            header("Content-Type", "application/json")
        }

        handleServiceResponse(response, json, TAG)

        val responseBody = json.decodeFromString<GetRegistrationInvitationResponse>(response.body())

        return responseBody.token

    }


}