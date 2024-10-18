package com.leic52dg17.chimp.ui.screens.authentication

sealed interface AuthenticationScreenState {
    data object Landing : AuthenticationScreenState
    data object SignUp : AuthenticationScreenState
    data object Login : AuthenticationScreenState
    data object ForgotPassword : AuthenticationScreenState
}