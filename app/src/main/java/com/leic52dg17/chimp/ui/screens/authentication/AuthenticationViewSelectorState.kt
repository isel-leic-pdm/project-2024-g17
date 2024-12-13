package com.leic52dg17.chimp.ui.screens.authentication

sealed interface AuthenticationViewSelectorState {
    data class Error(val message: String, val onDismiss: () -> Unit ): AuthenticationViewSelectorState
    data object Landing : AuthenticationViewSelectorState
    data object SignUp : AuthenticationViewSelectorState
    data object Login : AuthenticationViewSelectorState
    data object ChangePassword: AuthenticationViewSelectorState
    data object ForgotPassword : AuthenticationViewSelectorState
    data object AuthenticationLoading: AuthenticationViewSelectorState
    data object Authenticated : AuthenticationViewSelectorState
}