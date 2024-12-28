package com.leic52dg17.chimp.ui.screens.authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.leic52dg17.chimp.ui.components.overlays.LoadingOverlay
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.viewmodels.screen.auth.AuthenticationViewSelectorViewModel
import com.leic52dg17.chimp.ui.views.authentication.LandingView
import com.leic52dg17.chimp.ui.views.authentication.ChangePasswordView
import com.leic52dg17.chimp.ui.views.authentication.ForgotPasswordView
import com.leic52dg17.chimp.ui.views.authentication.LoginView
import com.leic52dg17.chimp.ui.views.authentication.SignUpView
import com.leic52dg17.chimp.ui.views.error.ApplicationErrorView


@Composable
fun AuthenticationViewSelector(
    viewModel: AuthenticationViewSelectorViewModel,
    onAuthenticate: () -> Unit
) {
    ChIMPTheme {
        val currentState = viewModel.state
        var isLoading by rememberSaveable { mutableStateOf(false) }
        var showNoWifiDialog by rememberSaveable { mutableStateOf(false) }
        val connectivityStatus by viewModel.connectivityStatus.collectAsState()

        LaunchedEffect(connectivityStatus) {
            showNoWifiDialog = !connectivityStatus
        }

        if (showNoWifiDialog) {
            AlertDialog(
                onDismissRequest = { showNoWifiDialog = false },
                title = { Text(text = "No Wi-Fi Connection") },
                text = { Text(text = "Please check your internet connection and try again.") },
                confirmButton = {
                    Button(onClick = { showNoWifiDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }

        if (isLoading) {
            LoadingOverlay()
        }


        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentState) {
                    is AuthenticationViewSelectorState.Error -> {
                        ApplicationErrorView(
                            message = currentState.message
                        ) {
                            viewModel.transition(AuthenticationViewSelectorState.Landing)
                        }
                    }

                    is AuthenticationViewSelectorState.Landing -> {
                        isLoading = false
                        LandingView(
                            onLogInClick = {
                                viewModel.transition(
                                    AuthenticationViewSelectorState.Login
                                )
                            },
                            onSignUpClick = {
                                viewModel.transition(
                                    AuthenticationViewSelectorState.SignUp
                                )
                            },
                        )
                    }

                    is AuthenticationViewSelectorState.Login -> {
                        isLoading = false
                        LoginView(
                            onLogInClick = { username, password ->
                                if (!connectivityStatus) {
                                    showNoWifiDialog = true
                                } else {
                                    viewModel.loginUser(
                                        username,
                                        password,
                                        onAuthenticate
                                    )
                                }
                            },
                            onSignUpClick = {
                                viewModel.transition(
                                    AuthenticationViewSelectorState.SignUp
                                )
                            },
                            onBackClick = { viewModel.transition(AuthenticationViewSelectorState.Landing) },
                            onForgotPasswordClick = { viewModel.transition(AuthenticationViewSelectorState.ForgotPassword) },
                        )
                    }

                    is AuthenticationViewSelectorState.SignUp -> {
                        isLoading = false
                        SignUpView(
                            onSignUpClick = { registrationInvitation, username, displayName, password ->
                                if (!connectivityStatus) {
                                    showNoWifiDialog = true
                                } else {
                                    viewModel.signUpUser(
                                        registrationInvitation,
                                        username,
                                        displayName,
                                        password
                                    )
                                }
                            },
                            onLogInClick = {
                                viewModel.transition(
                                    AuthenticationViewSelectorState.Login
                                )
                            },
                            onBackClick = { viewModel.transition(AuthenticationViewSelectorState.Landing) },
                        )
                    }

                    is AuthenticationViewSelectorState.ForgotPassword -> {
                        isLoading = false
                        ForgotPasswordView(
                            onForgotPassword = { email -> viewModel.forgotPassword(email) },
                            onBackClick = { viewModel.transition(AuthenticationViewSelectorState.Landing) },
                        )
                    }

                    is AuthenticationViewSelectorState.ChangePassword -> {
                        isLoading = false
                        ChangePasswordView(
                            onChangePassword = { username, currentPassword, newPassword, confirmPassword ->
                                viewModel.changePassword(
                                    username,
                                    currentPassword,
                                    newPassword,
                                    confirmPassword
                                )
                            },
                            onBackClick = { viewModel.transition(AuthenticationViewSelectorState.Landing) },
                        )
                    }

                    is AuthenticationViewSelectorState.Authenticated -> { onAuthenticate() }

                    is AuthenticationViewSelectorState.AuthenticationLoading -> {
                        isLoading = true
                    }
                }
            }

        }

    }
}

