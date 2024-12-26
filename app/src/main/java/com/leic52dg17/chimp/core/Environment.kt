package com.leic52dg17.chimp.core

import io.github.cdimascio.dotenv.Dotenv

object Environment {
    private const val HOST_KEY = "HOST"
    private val dotenv: Dotenv = Dotenv.configure().ignoreIfMissing().load()

    /*fun getHostUrl(): String = dotenv[HOST_KEY]
        ?: System.getenv(HOST_KEY)
        ?: throw Exception("Missing HOST environment variable")*/
    fun getHostUrl() : String = "https://53cf-2001-818-d83e-2100-335a-9451-56a9-1280.ngrok-free.app"
}