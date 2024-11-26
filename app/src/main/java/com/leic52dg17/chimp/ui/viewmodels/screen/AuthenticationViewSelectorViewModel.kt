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
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.http.services.auth.IAuthenticationService
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.ui.screens.authentication.AuthenticationViewSelectorState
import kotlinx.coroutines.launch

class AuthenticationViewSelectorViewModel(
    private val authenticationService: IAuthenticationService,
    private val context: Context,
    initialState: AuthenticationViewSelectorState = AuthenticationViewSelectorState.Landing
) : ViewModel() {
    var state: AuthenticationViewSelectorState by mutableStateOf(initialState)

    fun transition(newState: AuthenticationViewSelectorState) {
        state = newState
    }

    fun loginUser(username: String, password: String, onAuthenticate: () -> Unit) {
        Log.i(TAG, "Logging $username in")
        if (state is AuthenticationViewSelectorState.Login) {
            transition(AuthenticationViewSelectorState.AuthenticationLoading)
            viewModelScope.launch {
                try {
                    val authenticatedUser = authenticationService.loginUser(username, password)
                    Log.i(
                        TAG,
                        "Saving user with ID:${authenticatedUser.user?.id} as authenticated user"
                    )
                    SharedPreferencesHelper.saveAuthenticatedUser(context, authenticatedUser)
                    val storedAuthenticatedUser =
                        SharedPreferencesHelper.getAuthenticatedUser(context)

                    if (storedAuthenticatedUser == null) {
                        Log.e(TAG, "!=== COULD NOT RETRIEVE AUTHENTICATED USER ==!")
                        transition(
                            AuthenticationViewSelectorState.Login(
                                isDialogOpen = true,
                                errorMessage = ErrorMessages.AUTHENTICATED_USER_NULL
                            )
                        )
                    } else {
                        Log.i(TAG, "Authenticated user ID: ${authenticatedUser.user?.id}")
                        onAuthenticate()
                        transition(AuthenticationViewSelectorState.Authenticated)
                    }
                } catch (e: ServiceException) {
                    Log.e(TAG, "ServiceException: ${e.message}")
                    transition(
                        AuthenticationViewSelectorState.Login(
                            isDialogOpen = true,
                            errorMessage = e.message
                        )
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Exception: ${e.message}")
                    transition(
                        AuthenticationViewSelectorState.Login(
                            isDialogOpen = true,
                            errorMessage = e.message
                        )
                    )
                }
            }
        }
    }

    fun signUpUser(username: String, password: String) {
        if (state is AuthenticationViewSelectorState.SignUp) {
            transition(AuthenticationViewSelectorState.AuthenticationLoading)
            viewModelScope.launch {
                try {
                    val authenticatedUser = authenticationService.signUpUser(username, password)
                    SharedPreferencesHelper.saveAuthenticatedUser(context, authenticatedUser)
                    transition(AuthenticationViewSelectorState.Authenticated)
                } catch (e: ServiceException) {
                    transition(
                        AuthenticationViewSelectorState.SignUp(
                            isDialogOpen = true,
                            errorMessage = e.message
                        )
                    )
                }
            }
        }
    }

    companion object {
        private const val TAG = "AUTHENTICATION_VIEW_MODEL"
    }

    fun changePassword(
        username: String,
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        if (state is AuthenticationViewSelectorState.Authenticated) {
            transition(AuthenticationViewSelectorState.AuthenticationLoading)
            viewModelScope.launch {
                try {
                    val authenticatedUser = authenticationService.changePassword(
                        username,
                        currentPassword,
                        newPassword,
                        confirmPassword
                    )
                    SharedPreferencesHelper.saveAuthenticatedUser(context, authenticatedUser)
                    transition(AuthenticationViewSelectorState.Authenticated)
                } catch (e: ServiceException) {
                    transition(
                        AuthenticationViewSelectorState.ChangePassword(
                            isDialogOpen = true,
                            errorMessage = e.message
                        )
                    )
                }
            }
        }
        transition(AuthenticationViewSelectorState.Authenticated)
    }

    fun forgotPassword(email: String) {
        if (state is AuthenticationViewSelectorState.ForgotPassword) {
            transition(AuthenticationViewSelectorState.AuthenticationLoading)
            viewModelScope.launch {
                try {
                    val authenticatedUser = authenticationService.forgotPassword(email)
                    SharedPreferencesHelper.saveAuthenticatedUser(context, authenticatedUser)
                    transition(AuthenticationViewSelectorState.Authenticated)
                } catch (e: ServiceException) {
                    transition(AuthenticationViewSelectorState.ForgotPassword)
                }
            }
        }
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