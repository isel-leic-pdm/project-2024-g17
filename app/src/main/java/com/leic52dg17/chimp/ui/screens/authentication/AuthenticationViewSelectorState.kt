package com.leic52dg17.chimp.ui.screens.authentication

sealed interface AuthenticationViewSelectorState {
    data object Landing : AuthenticationViewSelectorState
    data class SignUp(val isDialogOpen: Boolean, val errorMessage: String? = null) : AuthenticationViewSelectorState
    data class Login(val isDialogOpen: Boolean, val errorMessage: String? = null) : AuthenticationViewSelectorState
    data class ChangePassword(val isDialogOpen: Boolean, val errorMessage: String? = null) : AuthenticationViewSelectorState
    data object AuthenticationLoading: AuthenticationViewSelectorState
    data object ForgotPassword : AuthenticationViewSelectorState
    data object Authenticated : AuthenticationViewSelectorState
}