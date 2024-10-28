package com.leic52dg17.chimp.core.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.leic52dg17.chimp.core.shared.SharedPreferencesHelper

class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = if (SharedPreferencesHelper.getAuthenticatedUser(this) == null) {
            Intent(this, AuthenticationActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
    }
}