package com.leic52dg17.chimp.ui.viewmodels.screen.main.functions

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.core.cache.user.IUserCacheManager
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.domain.model.auth.AuthenticatedUser
import com.leic52dg17.chimp.domain.model.channel.Channel
import com.leic52dg17.chimp.domain.model.user.User
import com.leic52dg17.chimp.http.services.common.ServiceErrorTypes
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.receivers.IConnectivityObserver
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserFunctions(
    private val viewModel: MainViewSelectorViewModel,
    private val userCacheManager: IUserCacheManager,
    private val connectivityObserver: IConnectivityObserver
) {
    fun getUserProfile(id: Int, noWifiCallback: () -> Unit) {
        viewModel.viewModelScope.launch {
            Log.i(TAG, "Getting user profile for user with ID: $id")

            val authenticatedUser = viewModel.userInfoRepository.authenticatedUser.first()

            viewModel.transition(
                MainViewSelectorState.GettingUserInfo(
                    onBackClick = { viewModel.loadSubscribedChannels() },
                    isCurrentUser = id == authenticatedUser?.user?.id
                )
            )

            if (authenticatedUser == null || !viewModel.userInfoRepository.checkTokenValidity() || authenticatedUser.user == null) {
                viewModel.transition(MainViewSelectorState.Unauthenticated)
                return@launch
            }

            try {
                // If the user requested is the current user and it is in cache, return it
                if (id == authenticatedUser.user.id) {
                    val cachedUser = userCacheManager.getCachedUser()

                    if (cachedUser != null) {
                        Log.i(TAG, "Got user from cache")
                        transitionToUserInfo(cachedUser, authenticatedUser)
                        return@launch
                    }
                }

                // In case there is no internet connection
                if (!connectivityObserver.connectivityStatusFlow.value) {
                    noWifiCallback()
                    return@launch
                }

                getUserFromService(id, authenticatedUser)
            } catch (e: ServiceException) {
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    Log.i(TAG, e.message)
                    viewModel.transition(
                        MainViewSelectorState.Error(message = e.message) {
                            viewModel.loadSubscribedChannels()
                        }
                    )
                }
            }
        }
    }

    fun loadAvailableToInviteUsers(channel: Channel, username: String?, page: Int?, limit: Int?) {
        viewModel.viewModelScope.launch {
            Log.i(TAG, "Getting users...")
            val users = viewModel.userService.getAllUsers(username, page, limit)
            val authenticatedUser = viewModel.userInfoRepository.authenticatedUser.first()
            Log.i(TAG, "Got users -> $users")

            if (authenticatedUser == null) {
                viewModel.transition(
                    MainViewSelectorState.Unauthenticated
                )
            }
            Log.i(TAG, "Transitioning to inviting users with page $page and new users -> $users")
            viewModel.transition(
                MainViewSelectorState.InvitingUsers(
                    channel = channel,
                    authenticatedUser = authenticatedUser,
                    users = users,
                    page = page ?: 0
                )
            )
        }
    }

    private suspend fun getUserFromService(id: Int, authenticatedUser: AuthenticatedUser) {
        // Otherwise call the service and update the cache
        val user = viewModel.userService.getUserById(id)

        if (user != null) {
            Log.i(TAG, "Caching user")
            userCacheManager.cacheUser(user)
        }

        if (user != null) {
            Log.i(TAG, "Fetched user profile for user with ID: ${user.id}")
            transitionToUserInfo(user, authenticatedUser)
        } else {
            Log.e(TAG, "Error fetching user profile for user with ID: $id")
            viewModel.transition(
                MainViewSelectorState.Error(message = ErrorMessages.UNKNOWN) {
                    viewModel.loadSubscribedChannels()
                }
            )
        }
    }

    private fun transitionToUserInfo(user: User, authenticatedUser: AuthenticatedUser) {
        viewModel.transition(
            MainViewSelectorState.UserInfo(
                user,
                authenticatedUser = authenticatedUser,
                isCurrentUser = user.id == authenticatedUser.user?.id
            )
        )
    }

    companion object {
        const val TAG = "USER_FUNCTIONS"
    }
}