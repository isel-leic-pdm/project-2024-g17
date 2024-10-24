package com.leic52dg17.chimp.core.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.leic52dg17.chimp.ui.screens.authentication.AuthenticationScreen
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.viewmodels.screen.AuthenticationScreenViewModel

class AuthenticationActivity : ComponentActivity() {
    private val viewModel by viewModels<AuthenticationScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChIMPTheme {
                AuthenticationScreen(viewModel = viewModel)
            }
        }
    }
}