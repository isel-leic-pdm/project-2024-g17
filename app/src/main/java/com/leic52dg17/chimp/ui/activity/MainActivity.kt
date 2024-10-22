package com.leic52dg17.chimp.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.leic52dg17.chimp.ui.screens.main.MainScreen
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.viewmodels.screen.MainScreenViewModel
import com.leic52dg17.chimp.ui.viewmodels.screen.MainScreenViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainScreenViewModel by viewModels<MainScreenViewModel>(
            factoryProducer = {
                MainScreenViewModelFactory()
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
                        MainScreen(mainScreenViewModel)
                    }
                }
            }
        }
    }
}