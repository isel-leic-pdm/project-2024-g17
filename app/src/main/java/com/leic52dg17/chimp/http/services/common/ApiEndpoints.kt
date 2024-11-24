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
    }

    object Chat {
        val LISTEN = "$BASE_URL/chat/listen"
    }
}