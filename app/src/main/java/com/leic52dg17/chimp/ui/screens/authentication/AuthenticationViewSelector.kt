package com.leic52dg17.chimp.ui.screens.authentication

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.leic52dg17.chimp.core.activity.MainActivity
import com.leic52dg17.chimp.ui.components.overlays.LoadingOverlay
import com.leic52dg17.chimp.ui.theme.ChIMPTheme
import com.leic52dg17.chimp.ui.viewmodels.screen.AuthenticationViewSelectorViewModel
import com.leic52dg17.chimp.ui.views.LandingView
import com.leic52dg17.chimp.ui.views.authentication.LoginView
import com.leic52dg17.chimp.ui.views.authentication.SignUpView

@Composable
fun AuthenticationViewSelector(viewModel: AuthenticationViewSelectorViewModel) {
    ChIMPTheme {
        val currentState = viewModel.state
        val context = LocalContext.current

        var isLoading by rememberSaveable {
            mutableStateOf(false)
        }

        if (isLoading) {
            LoadingOverlay()
        }

        when (currentState) {
            is AuthenticationViewSelectorState.Landing -> {
                isLoading = false
                LandingView(
                    onLogInClick = { viewModel.transition(AuthenticationViewSelectorState.Login(false)) },
                    onSignUpClick = { viewModel.transition(AuthenticationViewSelectorState.SignUp(false)) },
                )
            }

            is AuthenticationViewSelectorState.Login -> {
                isLoading = false
                LoginView(
                    onLogInClick = { username, password -> viewModel.loginUser(username, password) },
                    onSignUpClick = { viewModel.transition(AuthenticationViewSelectorState.SignUp(false)) },
                    onBackClick = { viewModel.transition(AuthenticationViewSelectorState.Landing) },
                )
            }

            is AuthenticationViewSelectorState.SignUp -> {
                isLoading = false
                SignUpView(
                    onSignUpClick = { username, password -> viewModel.signUpUser(username, password) },
                    onLogInClick = { viewModel.transition(AuthenticationViewSelectorState.Login(false)) },
                    onBackClick = { viewModel.transition(AuthenticationViewSelectorState.Landing) },
                )
            }

            is AuthenticationViewSelectorState.ForgotPassword -> throw NotImplementedError()

            is AuthenticationViewSelectorState.Authenticated -> {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }

            is AuthenticationViewSelectorState.AuthenticationLoading -> {
                isLoading = true
            }
        }
    }
}
/*

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthenticationViewSelectorPreview() {
    AuthenticationViewSelector(viewModel = AuthenticationViewSelectorViewModel(
        FakeAuthenticationService(),
        Conte
    ))
}*/
