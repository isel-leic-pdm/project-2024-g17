package com.leic52dg17.chimp.http.services.sse.implementations

import android.util.Log
import com.leic52dg17.chimp.domain.common.ErrorMessages
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
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.modules.polymorphic
import java.math.BigInteger

class SSEService(
    private val httpClient: HttpClient,
    private val scope: CoroutineScope,
) : ISSEService {

    override val eventFlow = MutableSharedFlow<Events>()

    private var sseJob: Job? = null
    lateinit var channel: ByteReadChannel
    private val json = Json {
        ignoreUnknownKeys = true
        classDiscriminator = "type" // Tells the serializer to look for the "type" field
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
                            if(eventData != null) {
                                handleEvent(eventData, eventFlow)
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                Log.i("SSE_SERVICE_SSE_SCOPE", e.message ?: "")
                return@launch
            }
        }

        sseJob?.invokeOnCompletion { throwable ->
            if (throwable is CancellationException) {
                channel.cancel(null)
                Log.i("SSE_SERVICE", "SSE Coroutine was cancelled and channel was closed")
            }
        }

        return success(Unit)
    }

    private suspend fun handleEvent(jsonString: String, eventFlow: MutableSharedFlow<Events>) {
        try {
            val eventResponse = json.decodeFromString<EventResponse>(jsonString)

            when (eventResponse.data.type) {
                MessageTypes.ChannelMessage -> {
                    val messageContent = json.decodeFromJsonElement<MessageContent>(eventResponse.data.content)

                    val message = Message(
                        id = messageContent.id,
                        channelId = messageContent.channelId,
                        userId = messageContent.userId,
                        text = messageContent.text,
                        createdAt = messageContent.createdAt
                    )

                    Log.i("SSE_SERVICE", "Received message id: ${message.id}")

                    eventFlow.emit(Events.ChannelMessage(message))
                }

                MessageTypes.Invitation -> {

                }
            }
        } catch (e: Exception) {
            Log.e("SSE_SERVICE", "Failed to handle event: ${e.message}")
        }
    }


    override fun stopListening() {
        sseJob?.cancel()
    }
}