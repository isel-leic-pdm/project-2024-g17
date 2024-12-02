package com.leic52dg17.chimp.core

import io.github.cdimascio.dotenv.Dotenv

object Environment {
    private const val HOST_KEY = "HOST"
    private val dotenv: Dotenv = Dotenv.configure().ignoreIfMissing().load()

    /*fun getHostUrl(): String = dotenv[HOST_KEY]
        ?: System.getenv(HOST_KEY)
        ?: throw Exception("Missing HOST environment variable")*/

    fun getHostUrl() : String = "https://bb6d-2001-8a0-6709-ca00-c535-4ed7-96ff-29f9.ngrok-free.app"
}