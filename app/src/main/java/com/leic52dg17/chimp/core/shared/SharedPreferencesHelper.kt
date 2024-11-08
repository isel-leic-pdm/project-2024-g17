package com.leic52dg17.chimp.core.shared

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.leic52dg17.chimp.core.activity.AuthenticationActivity
import com.leic52dg17.chimp.model.auth.AuthenticatedUser
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object SharedPreferencesHelper {
    private const val PREFS_NAME = "chimp_prefs"
    private const val KEY_AUTHENTICATED_USER = "authenticated_user"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveAuthenticatedUser(context: Context, user: AuthenticatedUser?) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        val userJson = user?.let { Json.encodeToString(it) }
        editor.putString(KEY_AUTHENTICATED_USER, userJson)
        editor.apply()
    }

    fun getAuthenticatedUser(context: Context): AuthenticatedUser? {
        val prefs = getPreferences(context)
        val userJson = prefs.getString(KEY_AUTHENTICATED_USER, null)
        return userJson?.let { Json.decodeFromString(it) }
    }

    fun logout(context: Context) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.remove(KEY_AUTHENTICATED_USER)
        editor.apply()
    }
}