package com.leic52dg17.chimp.ui.screens.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.leic52dg17.chimp.http.services.auth.implementations.FakeAuthenticationService
import com.leic52dg17.chimp.http.services.channel.implementations.FakeChannelService
import com.leic52dg17.chimp.ui.components.overlays.LoadingOverlay
import com.leic52dg17.chimp.http.services.message.implementations.FakeMessageService
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.viewmodels.screen.AuthenticationScreenViewModel
import com.leic52dg17.chimp.ui.viewmodels.screen.MainScreenViewModel
import com.leic52dg17.chimp.ui.views.LandingView
import com.leic52dg17.chimp.ui.views.authentication.LoginView
import com.leic52dg17.chimp.ui.views.authentication.SignUpView

@Composable
fun AuthenticationScreen(viewModel: AuthenticationScreenViewModel) {
    ChIMPTheme {
        val currentState = viewModel.state

        var isLoading by rememberSaveable {
            mutableStateOf(false)
        }

        if (isLoading) {
            LoadingOverlay()
        }

        when (currentState) {
            is AuthenticationScreenState.Landing -> {
                isLoading = false
                LandingView(
                    onLogInClick = { viewModel.transition(AuthenticationScreenState.Login(false)) },
                    onSignUpClick = { viewModel.transition(AuthenticationScreenState.SignUp(false)) },
                )
            }

            is AuthenticationScreenState.Login -> {
                isLoading = false
                LoginView(
                    onLogInClick = { username, password -> viewModel.loginUser(username, password) },
                    onSignUpClick = { viewModel.transition(AuthenticationScreenState.SignUp(false)) },
                    onBackClick = { viewModel.transition(AuthenticationScreenState.Landing) },
                )
            }

            is AuthenticationScreenState.SignUp -> {
                isLoading = false
                SignUpView(
                    onSignUpClick = { username, password -> viewModel.signUpUser(username, password) },
                    onLogInClick = { viewModel.transition(AuthenticationScreenState.Login(false)) },
                    onBackClick = { viewModel.transition(AuthenticationScreenState.Landing) },
                )
            }

            is AuthenticationScreenState.ForgotPassword -> throw NotImplementedError()

            is AuthenticationScreenState.Authenticated -> TODO()

            is AuthenticationScreenState.AuthenticationLoading -> {
                isLoading = true
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthenticationScreenPreview() {
    AuthenticationScreen(viewModel = AuthenticationScreenViewModel(
        FakeAuthenticationService(),
        MainScreenViewModel(
            FakeChannelService(),
            FakeMessageService()
        )
    ))
}