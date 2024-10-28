package com.leic52dg17.chimp.core.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.leic52dg17.chimp.core.ChimpApplication
import com.leic52dg17.chimp.ui.screens.authentication.AuthenticationViewSelector
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.viewmodels.screen.AuthenticationViewSelectorViewModel
import com.leic52dg17.chimp.ui.viewmodels.screen.AuthenticationViewSelectorViewModelFactory

class AuthenticationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewSelectorViewModel by viewModels<AuthenticationViewSelectorViewModel>(
            factoryProducer = {
                AuthenticationViewSelectorViewModelFactory(
                    (application as ChimpApplication).authenticationService,
                    applicationContext
                )
            }
        )
        enableEdgeToEdge()
        setContent {
            ChIMPTheme {
                AuthenticationViewSelector(viewModel = authViewSelectorViewModel)
            }
        }
    }
}