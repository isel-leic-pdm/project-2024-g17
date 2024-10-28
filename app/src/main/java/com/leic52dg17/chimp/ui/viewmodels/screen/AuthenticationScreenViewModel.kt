package com.leic52dg17.chimp.ui.viewmodels.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.http.services.auth.IAuthenticationService
import com.leic52dg17.chimp.model.common.Failure
import com.leic52dg17.chimp.model.common.Success
import com.leic52dg17.chimp.ui.screens.authentication.AuthenticationScreenState
import kotlinx.coroutines.launch

class AuthenticationScreenViewModel(
    private val authenticationService: IAuthenticationService,
    private val mainScreenViewModel: MainScreenViewModel
) : ViewModel() {
    var state: AuthenticationScreenState by mutableStateOf(AuthenticationScreenState.Landing)

    fun transition(newState: AuthenticationScreenState) {
        state = newState
    }

    fun loginUser(username: String, password: String) {
        if(state is AuthenticationScreenState.Login) {
            transition(AuthenticationScreenState.LoggingIn)
            viewModelScope.launch {
                when(val result = authenticationService.loginUser(username, password)) {
                    is Failure -> {
                        transition(AuthenticationScreenState.Login(true, result.value.message))
                    }
                    is Success -> {
                        mainScreenViewModel.authenticatedUser = result.value
                        transition(AuthenticationScreenState.Authenticated)
                    }
                }
            }
        }
    }

    fun signUpUser(username: String, password: String) {
        if (state is AuthenticationScreenState.SignUp) {
            transition(AuthenticationScreenState.LoggingIn)
            viewModelScope.launch {
                when (val result = authenticationService.signUpUser(username, password)) {
                    is Failure -> {
                        transition(AuthenticationScreenState.SignUp(true, result.value.message))
                    }
                    is Success -> {
                        mainScreenViewModel.authenticatedUser = result.value
                        transition(AuthenticationScreenState.Authenticated)
                    }
                }
            }
        }
    }
}