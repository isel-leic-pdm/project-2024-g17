package com.leic52dg17.chimp.http.services.foreground

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.core.ChimpApplication
import com.leic52dg17.chimp.domain.model.channel.ChannelInvitation
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.http.services.common.ApiEndpoints
import com.leic52dg17.chimp.http.services.sse.events.EventContent
import com.leic52dg17.chimp.http.services.sse.events.EventResponse
import com.leic52dg17.chimp.http.services.sse.events.InvitationContent
import com.leic52dg17.chimp.http.services.sse.events.MessageContent
import com.leic52dg17.chimp.http.services.sse.events.MessageTypes
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


class EventStreamService: Service() {
    private val notificationManager by lazy { getSystemService(NotificationManager::class.java) }
    private val channelId = "event_stream_channel"
    private var eventListenerJob: Job? = null

    private val client: HttpClient by lazy {
        HttpClient {
            install(SSE)
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            defaultRequest {
                runBlocking {
                    withContext(Dispatchers.IO) {
                        val authenticatedUser = (application as ChimpApplication).userInfoRepository.authenticatedUser.first()
                        if (authenticatedUser?.authenticationToken != null) {
                            header("Authorization", "Bearer ${authenticatedUser.authenticationToken}")
                        }
                    }
                }
            }
        }
    }

    private val json = Json {
        ignoreUnknownKeys = true
        classDiscriminator = "type"
        serializersModule = SerializersModule {
            polymorphic(EventContent::class) {
                subclass(MessageContent::class, MessageContent.serializer())
                subclass(InvitationContent::class, InvitationContent.serializer())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "Destroying foreground service")
        eventListenerJob?.cancel()
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Creating foreground service")
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "On start command called")
        val notification = createNotification()

        startForeground(notification)

        eventListenerJob = CoroutineScope(Dispatchers.IO).launch {
            listenForEvents()
        }

        return START_STICKY
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelId,
            "Event Stream Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("ChIMP")
            .setContentText("Staying up-to-date with the latest events...")
            .setSmallIcon(R.drawable.chimp_blue_final)
            .setPriority(NotificationManagerCompat.IMPORTANCE_NONE)
            .build()
    }

    private fun startForeground(notification: Notification) {
        ServiceCompat.startForeground(
            this,
            SERVICE_ID,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
        )
    }

    private suspend fun listenForEvents() {
        Log.i(TAG, "Connecting to SSE")
        client.sse(ApiEndpoints.Chat.LISTEN) {
            incoming.collect { event ->
                val eventData = event.data
                if (eventData != null) {
                    handleEvent(eventData)
                }
            }
        }
    }

    private fun handleEvent(jsonString: String) {
        val eventResponse = json.decodeFromString<EventResponse>(jsonString)

        when (eventResponse.data.type) {
            MessageTypes.ChannelMessage -> {
                Log.i(TAG, "Received message")
                val messageContent = json.decodeFromJsonElement<MessageContent>(eventResponse.data.content)

                val message = Message(
                    id = messageContent.id,
                    channelId = messageContent.channelId,
                    userId = messageContent.userId,
                    text = messageContent.text,
                    createdAt = messageContent.createdAt
                )

                val notification = NotificationCompat.Builder(this, channelId)
                    .setContentTitle("New message")
                    .setContentText(message.text)
                    .setSmallIcon(R.drawable.chimp_blue_final)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setChannelId(channelId)
                    .build()

                emitNotification(MESSAGE_NOTIFICATION_TAG, message.id, notification)
            }

            MessageTypes.Invitation -> {
                Log.i(TAG, "Received invitation")
                val invitationContent = json.decodeFromJsonElement<InvitationContent>(eventResponse.data.content)

                val invitation = ChannelInvitation(
                    id = invitationContent.id,
                    senderId = invitationContent.senderId,
                    receiverId = invitationContent.receiverId,
                    channelId = invitationContent.channelId,
                    permissionLevel = invitationContent.permissionLevel
                )

                val notification = NotificationCompat.Builder(this, channelId)
                    .setContentTitle("New invitation")
                    .setContentText("You were invited to a channel")
                    .setSmallIcon(R.drawable.chimp_blue_final)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setChannelId(channelId)
                    .build()

                emitNotification(INVITATION_NOTIFICATION_TAG, invitation.id, notification)
            }
        }
    }

    private fun emitNotification(tag: String, id: Int, notification: Notification) {
        Log.i(TAG, "Emitting notification with TAG: $tag and ID: $id")
        notificationManager.notify(tag, id, notification)
    }

    companion object {
        private const val MESSAGE_NOTIFICATION_TAG = "MESSAGE_NOTIFICATION"
        private const val INVITATION_NOTIFICATION_TAG = "INVITATION_NOTIFICATION"
        private const val SERVICE_ID = 100
        const val TAG = "EVENT_STREAM_SERVICE"
    }
}