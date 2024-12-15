package com.leic52dg17.chimp.core.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.leic52dg17.chimp.core.ChimpApplication
import com.leic52dg17.chimp.core.repositories.UserInfoRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LauncherActivity : ComponentActivity() {
    private val userInfoRepository: UserInfoRepository by lazy { (application as ChimpApplication).userInfoRepository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            userInfoRepository.authenticatedUser.collectLatest { authenticatedUser ->
                val intent = if (authenticatedUser == null) {
                    Intent(this@LauncherActivity, AuthenticationActivity::class.java)
                } else {
                    Intent(this@LauncherActivity, MainActivity::class.java)
                }
                startActivity(intent)
                finish()
            }
        }
    }
}