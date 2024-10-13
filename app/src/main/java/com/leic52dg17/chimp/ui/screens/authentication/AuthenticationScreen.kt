package com.leic52dg17.chimp.ui.screens.authentication

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.viewmodels.AuthenticationViewModel
import com.leic52dg17.chimp.ui.views.LandingView
import com.leic52dg17.chimp.ui.views.authentication.LoginView
import com.leic52dg17.chimp.ui.views.authentication.SignUpView

@Composable
fun AuthenticationScreen(viewModel: AuthenticationViewModel) {
    ChIMPTheme {
        val currentState = viewModel.state

        when (currentState) {
            is AuthenticationScreenState.Landing -> LandingView(
                onLogInClick = { viewModel.state = AuthenticationScreenState.Login },
                onSignUpClick = { viewModel.state = AuthenticationScreenState.SignUp },
            )
            is AuthenticationScreenState.Login -> LoginView(
                onLogInClick = { /* TODO() */ },
                onSignUpClick = { viewModel.state = AuthenticationScreenState.SignUp },
                onBackClick = { viewModel.state = AuthenticationScreenState.Landing },
            )
            is AuthenticationScreenState.SignUp -> SignUpView(
                onSignUpClick = { /* TODO() */ },
                onLogInClick = { viewModel.state = AuthenticationScreenState.Login },
                onBackClick = { viewModel.state = AuthenticationScreenState.Landing },
            )
            is AuthenticationScreenState.ForgotPassword -> throw NotImplementedError()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthenticationScreenPreview() {
    AuthenticationScreen(viewModel = AuthenticationViewModel())
}