package com.leic52dg17.chimp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.ui.components.BottomNavbar
import com.leic52dg17.chimp.ui.screens.main.MainScreen
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChIMPTheme {
                var selectedNavIcon by remember {
                    mutableStateOf("chats")
                }

                Scaffold(
                    bottomBar = { BottomNavbar(
                        selectedNavIcon,
                        Modifier
                            .padding(bottom = 32.dp),
                        { selectedNavIcon = "profile" },
                        { selectedNavIcon = "chats" },
                        { selectedNavIcon = "settings" }
                    ) }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        MainScreen(viewModel = MainViewModel())
                    }
                }
            }
        }
    }
}