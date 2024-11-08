package com.leic52dg17.chimp.ui.viewmodels.screen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.core.shared.SharedPreferencesHelper
import com.leic52dg17.chimp.http.services.auth.IAuthenticationService
import com.leic52dg17.chimp.model.common.Failure
import com.leic52dg17.chimp.model.common.Success
import com.leic52dg17.chimp.ui.screens.authentication.AuthenticationViewSelectorState
import kotlinx.coroutines.launch

class AuthenticationViewSelectorViewModel(
    private val authenticationService: IAuthenticationService,
    private val context: Context
) : ViewModel() {
    var state: AuthenticationViewSelectorState by mutableStateOf(AuthenticationViewSelectorState.Landing)

    fun transition(newState: AuthenticationViewSelectorState) {
        state = newState
    }

    fun loginUser(username: String, password: String, onAuthenticate: () -> Unit) {
        Log.i(TAG, "Logging $username in")
        if (state is AuthenticationViewSelectorState.Login) {
            transition(AuthenticationViewSelectorState.AuthenticationLoading)
            viewModelScope.launch {
                when (val result = authenticationService.loginUser(username, password)) {
                    is Failure -> {
                        transition(
                            AuthenticationViewSelectorState.Login(
                                true,
                                result.value.message
                            )
                        )
                    }

                    is Success -> {
                        Log.i(
                            TAG,
                            "Saving user with ID:${result.value.user?.userId} as authenticated user"
                        )
                        SharedPreferencesHelper.saveAuthenticatedUser(context, result.value)
                        val authenticatedUser =
                            SharedPreferencesHelper.getAuthenticatedUser(context)

                        if (authenticatedUser == null) Log.e(
                            TAG,
                            "!=== COULD NOT RETRIEVE AUTHENTICATED USER ==!"
                        )
                        else Log.i(TAG, "Authenticated user ID: ${authenticatedUser.user?.userId}")

                        onAuthenticate()
                        transition(AuthenticationViewSelectorState.Authenticated)
                    }
                }
            }
        }
    }

    fun signUpUser(username: String, password: String) {
        if (state is AuthenticationViewSelectorState.SignUp) {
            transition(AuthenticationViewSelectorState.AuthenticationLoading)
            viewModelScope.launch {
                when (val result = authenticationService.signUpUser(username, password)) {
                    is Failure -> {
                        transition(
                            AuthenticationViewSelectorState.SignUp(
                                true,
                                result.value.message
                            )
                        )
                    }

                    is Success -> {
                        SharedPreferencesHelper.saveAuthenticatedUser(context, result.value)
                        transition(AuthenticationViewSelectorState.Authenticated)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "AUTHENTICATION_VIEW_MODEL"
    }

    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        if (state is AuthenticationViewSelectorState.Authenticated) {
            transition(AuthenticationViewSelectorState.AuthenticationLoading)
            viewModelScope.launch {
                when (val result = authenticationService.changePassword(currentPassword, newPassword, confirmPassword)) {
                    is Failure -> {
                        transition(AuthenticationViewSelectorState.ChangePassword(true, result.value.message))
                    }
                    is Success -> {
                        SharedPreferencesHelper.saveAuthenticatedUser(context, result.value)
                        transition(AuthenticationViewSelectorState.Authenticated)
                    }
                }
            }
        }
        transition(AuthenticationViewSelectorState.Authenticated)
    }
}

@Suppress("UNCHECKED_CAST")
class AuthenticationViewSelectorViewModelFactory(
    private val authenticationService: IAuthenticationService,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthenticationViewSelectorViewModel(
            authenticationService,
            context
        ) as T
    }
}