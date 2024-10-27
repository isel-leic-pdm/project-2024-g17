package com.leic52dg17.chimp.ui.screens.authentication

sealed interface AuthenticationScreenState {
    data object Landing : AuthenticationScreenState
    data class SignUp(val isDialogOpen: Boolean, val errorMessage: String? = null) : AuthenticationScreenState
    data class Login(val isDialogOpen: Boolean, val errorMessage: String? = null) : AuthenticationScreenState
    data object LoggingIn: AuthenticationScreenState
    data object ForgotPassword : AuthenticationScreenState
    data object Authenticated : AuthenticationScreenState
}