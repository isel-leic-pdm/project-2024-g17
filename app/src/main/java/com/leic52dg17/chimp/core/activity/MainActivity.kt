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
import com.leic52dg17.chimp.core.shared.SharedPreferencesHelper
import com.leic52dg17.chimp.ui.screens.main.MainViewSelector
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.viewmodels.screen.MainViewSelectorViewModel
import com.leic52dg17.chimp.ui.viewmodels.screen.MainViewSelectorViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainViewSelectorViewModel by viewModels<MainViewSelectorViewModel>(
            factoryProducer = {
                MainViewSelectorViewModelFactory(
                    (application as ChimpApplication).channelService,
                    (application as ChimpApplication).messageService,
                    (application as ChimpApplication).userService,
                    (application as ChimpApplication).sseService,
                    applicationContext
                )
            }
        )
        mainViewSelectorViewModel.getEventStream()
        enableEdgeToEdge()
        setContent {
            ChIMPTheme {
                Scaffold(
                    contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(
                        WindowInsetsSides.Start)
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        MainViewSelector(
                            mainViewSelectorViewModel,
                            SharedPreferencesHelper.getAuthenticatedUser(applicationContext),
                            onLogout = {
                                val intent = Intent(this@MainActivity, LauncherActivity::class.java)
                                this@MainActivity.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}