package com.leic52dg17.chimp.http.services.common

object ApiEndpoints {
    private const val BASE_URL = "http://localhost:8080/api"

    object Users {
        const val GET_ALL = "$BASE_URL/users"
        const val GET_BY_ID = "$BASE_URL/users/{id}"
    }
}