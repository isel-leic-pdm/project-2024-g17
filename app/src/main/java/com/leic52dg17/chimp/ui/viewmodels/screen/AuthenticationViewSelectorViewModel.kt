package com.leic52dg17.chimp.ui.viewmodels.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.core.shared.SharedData
import com.leic52dg17.chimp.http.services.auth.IAuthenticationService
import com.leic52dg17.chimp.model.common.Failure
import com.leic52dg17.chimp.model.common.Success
import com.leic52dg17.chimp.ui.screens.authentication.AuthenticationViewSelectorState
import kotlinx.coroutines.launch

class AuthenticationViewSelectorViewModel(
    private val authenticationService: IAuthenticationService,
) : ViewModel() {
    var state: AuthenticationViewSelectorState by mutableStateOf(AuthenticationViewSelectorState.Landing)

    fun transition(newState: AuthenticationViewSelectorState) {
        state = newState
    }

    fun loginUser(username: String, password: String) {
        if(state is AuthenticationViewSelectorState.Login) {
            transition(AuthenticationViewSelectorState.LoggingIn)
            viewModelScope.launch {
                when(val result = authenticationService.loginUser(username, password)) {
                    is Failure -> {
                        transition(AuthenticationViewSelectorState.Login(true, result.value.message))
                    }
                    is Success -> {
                        SharedData.authenticatedUser = result.value
                        transition(AuthenticationViewSelectorState.Authenticated)
                    }
                }
            }
        }
    }

    fun signUpUser(username: String, password: String) {
        if (state is AuthenticationViewSelectorState.SignUp) {
            transition(AuthenticationViewSelectorState.LoggingIn)
            viewModelScope.launch {
                when (val result = authenticationService.signUpUser(username, password)) {
                    is Failure -> {
                        transition(AuthenticationViewSelectorState.SignUp(true, result.value.message))
                    }
                    is Success -> {
                        SharedData.authenticatedUser = result.value
                        transition(AuthenticationViewSelectorState.Authenticated)
                    }
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class AuthenticationViewSelectorViewModelFactory(
    private val authenticationService: IAuthenticationService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthenticationViewSelectorViewModel(
            authenticationService
        ) as T
    }
}