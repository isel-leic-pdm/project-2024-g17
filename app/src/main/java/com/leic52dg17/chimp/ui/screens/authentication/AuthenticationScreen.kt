package com.leic52dg17.chimp.ui.screens.authentication

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.leic52dg17.chimp.http.services.auth.implementations.FakeAuthenticationService
import com.leic52dg17.chimp.http.services.channel.implementations.FakeChannelService
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

        when (currentState) {
            is AuthenticationScreenState.Landing -> LandingView(
                onLogInClick = { viewModel.state = AuthenticationScreenState.Login(false) },
                onSignUpClick = { viewModel.state = AuthenticationScreenState.SignUp },
            )
            is AuthenticationScreenState.Login -> LoginView(
                onLogInClick = { /* TODO() */ },
                onSignUpClick = { viewModel.state = AuthenticationScreenState.SignUp },
                onBackClick = { viewModel.state = AuthenticationScreenState.Landing },
            )
            is AuthenticationScreenState.SignUp -> SignUpView(
                onSignUpClick = { /* TODO() */ },
                onLogInClick = { viewModel.state = AuthenticationScreenState.Login(false) },
                onBackClick = { viewModel.state = AuthenticationScreenState.Landing },
            )
            is AuthenticationScreenState.ForgotPassword -> throw NotImplementedError()
            AuthenticationScreenState.Authenticated -> TODO()
            AuthenticationScreenState.LoggingIn -> TODO()
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