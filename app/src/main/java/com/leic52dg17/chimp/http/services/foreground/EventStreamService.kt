package com.leic52dg17.chimp.http.services.foreground

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.leic52dg17.chimp.R
import com.leic52dg17.chimp.domain.model.channel.ChannelInvitation
import com.leic52dg17.chimp.domain.model.message.Message
import com.leic52dg17.chimp.http.services.common.ApiEndpoints
import com.leic52dg17.chimp.http.services.sse.events.EventContent
import com.leic52dg17.chimp.http.services.sse.events.EventResponse
import com.leic52dg17.chimp.http.services.sse.events.InvitationContent
import com.leic52dg17.chimp.http.services.sse.events.MessageContent
import com.leic52dg17.chimp.http.services.sse.events.MessageTypes
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.plugins.sse.sse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


class EventStreamService: Service() {
    private lateinit var client: HttpClient
    private val channelId = "event_stream_channel"
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

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        client = HttpClient {
            install(SSE)
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelId,
            "Event Stream Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Event Stream")
            .setContentText("Listening for updates from the server...")
            .setSmallIcon(R.mipmap.chimp_logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun startForeground(notification: Notification) {
        ServiceCompat.startForeground(
            this,
            100,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
        )
    }

    private suspend fun listenForEvents() {
        client.sse(ApiEndpoints.Chat.LISTEN) {
            while (true) {
                incoming.collect { event ->
                    val eventData = event.data
                    if (eventData != null) {
                        handleEvent(eventData)
                    }
                }
            }
        }
    }

    private fun handleEvent(jsonString: String) {
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

                // TODO("NOTIFICATION LOGIC")
            }

            MessageTypes.Invitation -> {
                val invitationContent = json.decodeFromJsonElement<InvitationContent>(eventResponse.data.content)

                val invitation = ChannelInvitation(
                    id = invitationContent.id,
                    senderId = invitationContent.senderId,
                    receiverId = invitationContent.receiverId,
                    channelId = invitationContent.channelId,
                    permissionLevel = invitationContent.permissionLevel
                )

                // TODO("NOTIFICATION LOGIC")
            }
        }
    }

    private suspend fun emitNotification(id: Int, notification: Notification) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(id, notification)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}