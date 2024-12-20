package com.leic52dg17.chimp.http.services.message.implementations

import android.util.Log
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.http.services.common.ApiEndpoints
import com.leic52dg17.chimp.http.services.common.handleServiceResponse
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
import kotlinx.serialization.json.Json
import java.net.URL

class MessageService(private val client: HttpClient) : IMessageService {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getChannelMessages(channelId: Int): List<Message> {
        val uri = URL(ApiEndpoints.Message.GET_BY_CHANNEL_ID + "?channelId=$channelId")

        Log.i(TAG, uri.toString())

        Log.d(TAG, "GETTING MESSAGES FOR CHANNEL WITH ID: $channelId")

        val response = client.get(uri) {
            header("Accept", "application/json")
            header("Content-Type", "application/json")
        }

        handleServiceResponse(response, json, TAG)

        Log.i(TAG, "GOT MESSAGES FOR CHANNEL WITH ID: $channelId")

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

        handleServiceResponse(response, json, TAG)

        return Json.decodeFromString<CreateMessageResponse>(response.body()).messageId
    }

    companion object {
        const val TAG = "MESSAGE_SERVICE"
    }
}