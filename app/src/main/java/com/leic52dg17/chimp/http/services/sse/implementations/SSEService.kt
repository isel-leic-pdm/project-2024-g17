package com.leic52dg17.chimp.http.services.sse.implementations

import android.util.Log
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.channel.ChannelInvitation
import com.leic52dg17.chimp.domain.model.common.failure
import com.leic52dg17.chimp.domain.model.common.success
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.http.services.common.ApiEndpoints
import com.leic52dg17.chimp.http.services.sse.ISSEService
import com.leic52dg17.chimp.http.services.sse.events.EventContent
import com.leic52dg17.chimp.http.services.sse.events.EventResponse
import com.leic52dg17.chimp.http.services.sse.events.Events
import com.leic52dg17.chimp.http.services.sse.events.InvitationContent
import com.leic52dg17.chimp.http.services.sse.events.MessageContent
import com.leic52dg17.chimp.http.services.sse.events.MessageTypes
import com.leic52dg17.chimp.http.services.sse.results.SSEServiceError
import com.leic52dg17.chimp.http.services.sse.results.SSEServiceResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.sse
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.modules.polymorphic

class SSEService(
    private val httpClient: HttpClient,
    private val scope: CoroutineScope,
) : ISSEService {

    override val eventFlow = MutableSharedFlow<Events>()

    private var sseJob: Job? = null
    private val json = Json {
        ignoreUnknownKeys = true
        classDiscriminator = "type"
        serializersModule = kotlinx.serialization.modules.SerializersModule {
            polymorphic(EventContent::class) {
                subclass(MessageContent::class, MessageContent.serializer())
                subclass(InvitationContent::class, InvitationContent.serializer())
            }
        }
    }

    override fun listen(): SSEServiceResult {
        if (sseJob?.isActive == true) return failure(SSEServiceError(ErrorMessages.LISTEN_JOB_ALREADY_ACTIVE))

        sseJob = scope.launch {
            try {
                httpClient.sse(ApiEndpoints.Chat.LISTEN) {
                    while (true) {
                        incoming.collect { event ->
                            val eventData = event.data
                            if (eventData != null) {
                                handleEvent(eventData, eventFlow)
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                Log.i(SSE_SERVICE_SSE_SCOPE, e.message ?: "")
                return@launch
            }
        }

        sseJob?.invokeOnCompletion { throwable ->
            if (throwable is CancellationException) {
                Log.i(TAG, "SSE Coroutine was cancelled and channel was closed")
            }
        }

        return success(Unit)
    }

    private suspend fun handleEvent(jsonString: String, eventFlow: MutableSharedFlow<Events>) {
        try {
            Log.i(TAG, "Received event: $jsonString")
            val eventResponse = json.decodeFromString<EventResponse>(jsonString)

            when (eventResponse.data.type) {
                MessageTypes.ChannelMessage -> {
                    val messageContent =
                        json.decodeFromJsonElement<MessageContent>(eventResponse.data.content)

                    val message = Message(
                        id = messageContent.id,
                        channelId = messageContent.channelId,
                        userId = messageContent.userId,
                        text = messageContent.text,
                        createdAt = messageContent.createdAt
                    )

                    Log.i(TAG, "Received message id: ${message.id}")

                    eventFlow.emit(Events.ChannelMessage(message))
                }

                MessageTypes.Invitation -> {
                    val invitationContent =
                        json.decodeFromJsonElement<InvitationContent>(eventResponse.data.content)
                    val invitation = ChannelInvitation(
                        id = invitationContent.id,
                        senderId = invitationContent.senderId,
                        receiverId = invitationContent.receiverId,
                        channelId = invitationContent.channelId,
                        permissionLevel = invitationContent.permissionLevel
                    )

                    Log.i(TAG, "Received invitation id: ${invitation.id}")

                    eventFlow.emit(Events.Invitation(invitation))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to handle event: ${e.message}")
        }
    }


    override fun stopListening() {
        sseJob?.cancel()
    }

    companion object {
        const val TAG = "SSE_SERVICE"
        const val SSE_SERVICE_SSE_SCOPE = "SSE_SERVICE_SSE_SCOPE"
    }
}