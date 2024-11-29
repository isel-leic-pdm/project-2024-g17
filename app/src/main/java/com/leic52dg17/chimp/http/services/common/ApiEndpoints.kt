package com.leic52dg17.chimp.http.services.common

import com.leic52dg17.chimp.core.Environment

object ApiEndpoints {
    private val HOST = Environment.getHostUrl()
    private val BASE_URL = "$HOST/api"

    object Users {
        val GET_ALL = "$BASE_URL/users"
        val GET_BY_ID = "$BASE_URL/users/{id}"
        val GET_TOKEN = "$BASE_URL/users/token"
        val GET_BY_TOKEN = "$BASE_URL/users/token/{token}"
        val CREATE_USER = "$BASE_URL/users"
        val CHANGE_PASSWORD = "$BASE_URL/users/{user}"
        val GET_BY_CHANNEL = "$BASE_URL/users/channel/{id}"
    }

    object Channel {
        val CREATE = "$BASE_URL/channels"
        val GET_BY_ID = "$BASE_URL/channels/{id}"
        val GET_USER_SUBSCRIBED = "$BASE_URL/channels/user/{id}"
        val REMOVE_USER_FROM_CHANNEL = "$BASE_URL/channels/remove-user"
    }

    object Message {
        val GET_MESSAGE = "$BASE_URL/messages"
        val GET_BY_CHANNEL_ID = "$BASE_URL/messages"
        val CREATE_MESSAGE = "$BASE_URL/messages"
    }

    object ChannelInvitation {
        val CREATE = "$BASE_URL/channels/invitations"
        val GET_BY_ID = "$BASE_URL/channels/invitations/{id}"
        val USE = "$BASE_URL/channels/invitations/{id}"
        val GET_BY_CHANNEL_ID = "$BASE_URL/channels/invitations/channel/{id}"
    }

    object Chat {
        val LISTEN = "$BASE_URL/chat/listen"
    }
}