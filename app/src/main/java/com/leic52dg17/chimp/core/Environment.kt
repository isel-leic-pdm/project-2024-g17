package com.leic52dg17.chimp.core

import io.github.cdimascio.dotenv.Dotenv

object Environment {
    private const val HOST_KEY = "HOST"
    private val dotenv: Dotenv = Dotenv.configure().ignoreIfMissing().load()

    /*fun getHostUrl(): String = dotenv[HOST_KEY]
        ?: System.getenv(HOST_KEY)
        ?: throw Exception("Missing HOST environment variable")*/

    fun getHostUrl() : String = "https://6ff8-2001-818-d83e-2100-63e5-e20c-8c89-5044.ngrok-free.app"
}