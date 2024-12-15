package com.leic52dg17.chimp.core

import io.github.cdimascio.dotenv.Dotenv

object Environment {
    private const val HOST_KEY = "HOST"
    private val dotenv: Dotenv = Dotenv.configure().ignoreIfMissing().load()

    /*fun getHostUrl(): String = dotenv[HOST_KEY]
        ?: System.getenv(HOST_KEY)
        ?: throw Exception("Missing HOST environment variable")*/

    fun getHostUrl() : String = "https://041e-2001-8a0-6709-ca00-4112-e975-6c3d-b438.ngrok-free.app"
}