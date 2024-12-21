package com.leic52dg17.chimp.ui.viewmodels.screen.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.core.repositories.user.IUserInfoRepository
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.http.services.auth.IAuthenticationService
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.receivers.ConnectivityObserver
import com.leic52dg17.chimp.ui.screens.authentication.AuthenticationViewSelectorState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthenticationViewSelectorViewModel(
    private val authenticationService: IAuthenticationService,
    private val userInfoRepository: IUserInfoRepository,
    private val connectivityObserver: ConnectivityObserver,
    initialState: AuthenticationViewSelectorState = AuthenticationViewSelectorState.Landing
) : ViewModel() {
    var state: AuthenticationViewSelectorState by mutableStateOf(initialState)
    val connectivityStatus = connectivityObserver.connectivityStatusFlow

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
                    viewModelScope.launch {
                        userInfoRepository.saveAuthenticatedUser(authenticatedUser)
                        if (userInfoRepository.authenticatedUser.first() == null) {
                            Log.e(TAG, "!=== COULD NOT RETRIEVE AUTHENTICATED USER ==!")
                            transition(
                                AuthenticationViewSelectorState.Error(message = ErrorMessages.AUTHENTICATED_USER_NULL) {
                                    transition(AuthenticationViewSelectorState.Login)
                                }
                            )
                        } else {
                            Log.i(TAG, "Authenticated user ID: ${authenticatedUser.user?.id}")
                            onAuthenticate()
                            transition(AuthenticationViewSelectorState.Authenticated)
                        }
                    }
                } catch (e: ServiceException) {
                    Log.e(TAG, "ServiceException: ${e.message}")
                    transition(
                        AuthenticationViewSelectorState.Error(e.message) {
                            AuthenticationViewSelectorState.Login
                        }
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Exception: ${e.message}")
                    transition(
                        AuthenticationViewSelectorState.Error(
                            message = e.message ?: ErrorMessages.UNKNOWN
                        ) {
                            transition(AuthenticationViewSelectorState.Login)
                        }
                    )
                }
            }
        }
    }

    fun signUpUser(username: String, displayName: String, password: String) {
        if (state is AuthenticationViewSelectorState.SignUp) {
            transition(AuthenticationViewSelectorState.AuthenticationLoading)
            viewModelScope.launch {
                try {
                    val authenticatedUser =
                        authenticationService.signUpUser(username, displayName, password)

                    viewModelScope.launch {
                        userInfoRepository.saveAuthenticatedUser(authenticatedUser)
                        transition(AuthenticationViewSelectorState.Authenticated)
                    }
                } catch (e: ServiceException) {
                    transition(
                        AuthenticationViewSelectorState.Error(e.message) {
                            transition(AuthenticationViewSelectorState.SignUp)
                        }
                    )
                }
            }
        }
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

                    viewModelScope.launch {
                        userInfoRepository.saveAuthenticatedUser(authenticatedUser)
                        transition(AuthenticationViewSelectorState.Authenticated)
                    }
                } catch (e: ServiceException) {
                    transition(
                        AuthenticationViewSelectorState.Error(message = e.message) {
                            transition(AuthenticationViewSelectorState.ChangePassword)
                        }
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
                    viewModelScope.launch {
                        userInfoRepository.saveAuthenticatedUser(authenticatedUser)
                        transition(AuthenticationViewSelectorState.Authenticated)
                    }
                } catch (e: ServiceException) {
                    transition(AuthenticationViewSelectorState.ForgotPassword)
                }
            }
        }
    }

    companion object {
        private const val TAG = "AUTHENTICATION_VIEW_MODEL"
    }
}

@Suppress("UNCHECKED_CAST")
class AuthenticationViewSelectorViewModelFactory(
    private val authenticationService: IAuthenticationService,
    private val userInfoRepository: IUserInfoRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthenticationViewSelectorViewModel(
            authenticationService,
            userInfoRepository,
            connectivityObserver
        ) as T
    }
}