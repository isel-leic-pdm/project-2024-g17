package com.leic52dg17.chimp.core.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.leic52dg17.chimp.core.ChimpApplication
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
                    (application as ChimpApplication).messageService
                )
            }
        )
        enableEdgeToEdge()
        setContent {
            ChIMPTheme {
                Scaffold { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        MainViewSelector(mainViewSelectorViewModel)
                    }
                }
            }
        }
    }
}