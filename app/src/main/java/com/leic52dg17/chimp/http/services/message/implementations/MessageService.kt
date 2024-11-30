package com.leic52dg17.chimp.http.services.message.implementations

import android.util.Log
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.http.services.common.ApiEndpoints
import com.leic52dg17.chimp.http.services.common.ProblemDetails
import com.leic52dg17.chimp.http.services.common.ServiceErrorTypes
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.http.services.message.IMessageService
import com.leic52dg17.chimp.http.services.message.requests.CreateMessageRequest
import com.leic52dg17.chimp.http.services.message.responses.CreateMessageResponse
import com.leic52dg17.chimp.http.services.message.responses.GetMessagesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import java.net.URL

class MessageService(private val client: HttpClient) : IMessageService {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getChannelMessages(channelId: Int): List<Message> {
        val uri = URL(ApiEndpoints.Message.GET_BY_CHANNEL_ID + "?channelId=$channelId")

        Log.i(TAG, uri.toString())

        Log.d(TAG, "=== DEBUG ===\n GETTING MESSAGES FOR CHANNEL WITH ID: $channelId")

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
                Log.e(TAG, response.status.toString())
                throw ServiceException(ErrorMessages.UNKNOWN, ServiceErrorTypes.Unknown)
            }
        }
        Log.i(TAG, "=== DEBUG ===\n GOT MESSAGES FOR CHANNEL WITH ID: $channelId")

        return Json.decodeFromString<GetMessagesResponse>(response.body()).messages
    }

    override suspend fun createMessageInChannel(
        messageText: String,
        channelId: Int,
        senderId: Int
    ): Int {
        val uri = URL(ApiEndpoints.Message.CREATE_MESSAGE)
        val request = CreateMessageRequest(channelId, senderId, messageText)

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
                Log.e(TAG, response.status.toString())
                throw ServiceException(ErrorMessages.UNKNOWN, ServiceErrorTypes.Unknown)
            }
        }

        return Json.decodeFromString<CreateMessageResponse>(response.body()).messageId
    }

    companion object {
        const val TAG = "MESSAGE_SERVICE"
    }
}