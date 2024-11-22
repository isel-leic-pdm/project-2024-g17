package com.leic52dg17.chimp.http.services.common

object ApiEndpoints {
    private const val BASE_URL = "https://9e43-87-196-75-224.ngrok-free.app/api"

    object Users {
        const val GET_ALL = "$BASE_URL/users"
        const val GET_BY_ID = "$BASE_URL/users/{id}"
    }

    object Chat {
        const val LISTEN = "$BASE_URL/chat/listen"
    }
}