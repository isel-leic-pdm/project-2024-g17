package com.leic52dg17.chimp.core.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.ui.Modifier
import com.leic52dg17.chimp.core.ChimpApplication
import com.leic52dg17.chimp.ui.screens.main.MainViewSelector
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private fun onLogout() {
        val intent = Intent(this@MainActivity, LauncherActivity::class.java)
        this@MainActivity.startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val eventStreamIntent = Intent(this, (application as ChimpApplication).eventStreamService::class.java)
        startForegroundService(eventStreamIntent)

        val mainViewSelectorViewModel by viewModels<MainViewSelectorViewModel> {
            MainViewSelectorViewModelFactory(
                (application as ChimpApplication).channelService,
                (application as ChimpApplication).messageService,
                (application as ChimpApplication).userService,
                (application as ChimpApplication).channelInvitationService,
                (application as ChimpApplication).sseService,
                (application as ChimpApplication).userInfoRepository,
                { onLogout() },
                (application as ChimpApplication).channelCacheManager,
                (application as ChimpApplication).messageCacheManager,
                (application as ChimpApplication).channelRepository,
                (application as ChimpApplication).messageRepository
            )
        }
        (application as ChimpApplication).sseService.listen()
        enableEdgeToEdge()
        setContent {
            ChIMPTheme {
                Scaffold(
                    contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(
                        WindowInsetsSides.Start
                    )
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        // DANGEROUS. BE CAREFUL! THERE HAS TO BE A BETTER SOLUTION...
                        val authenticatedUser = runBlocking {
                            withContext(Dispatchers.IO) {
                                (application as ChimpApplication).userInfoRepository.authenticatedUser.first()
                            }
                        }
                        MainViewSelector(
                            mainViewSelectorViewModel,
                            authenticatedUser
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "MAIN_ACTIVITY"
    }
}