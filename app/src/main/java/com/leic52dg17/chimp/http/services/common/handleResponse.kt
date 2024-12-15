package com.leic52dg17.chimp.http.services.common

import android.util.Log
import com.leic52dg17.chimp.domain.common.ErrorMessages
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

suspend fun handleAuthServiceResponse(response: HttpResponse, json: Json, tag: String, log: String) {
    if (!response.status.isSuccess()) {
        if(response.contentType() == ContentType.Application.ProblemJson) {
            val details = json.decodeFromString<ProblemDetails>(response.body())
            Log.e(tag, "${details.title} -> ${details.errors}")
            throw ServiceException(details.title, ServiceErrorTypes.Common)
        } else {
            Log.e(tag, log)
            throw ServiceException(ErrorMessages.UNKNOWN, ServiceErrorTypes.Unknown)
        }
    }
}

suspend fun handleServiceResponse(response: HttpResponse, json: Json, tag: String) {
    if (!response.status.isSuccess()) {
        if (response.contentType() == ContentType.Application.ProblemJson) {
            val details = json.decodeFromString<ProblemDetails>(response.body())
            Log.e(tag, " ${details.title} -> ${details.errors}")
            throw ServiceException(details.title, ServiceErrorTypes.Common)
        } else if (response.status == HttpStatusCode.Unauthorized) {
            Log.e(tag, "Unauthorized: ${response.status}")
            throw ServiceException(ErrorMessages.UNAUTHORIZED, ServiceErrorTypes.Unauthorized)
        } else {
            throw ServiceException(ErrorMessages.UNKNOWN, ServiceErrorTypes.Unknown)
        }
    }
}