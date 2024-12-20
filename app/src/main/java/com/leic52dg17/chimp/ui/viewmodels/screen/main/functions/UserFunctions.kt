package com.leic52dg17.chimp.ui.viewmodels.screen.main.functions

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.domain.common.ErrorMessages
import com.leic52dg17.chimp.http.services.common.ServiceErrorTypes
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel.Companion.TAG
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserFunctions(private val viewModel: MainViewSelectorViewModel) {
    fun getUserProfile(id: Int) {
        Log.i(TAG, "Getting user profile for user with ID: $id")
        viewModel.transition(MainViewSelectorState.Loading)
        viewModel.viewModelScope.launch {
            val authenticatedUser = viewModel.userInfoRepository.authenticatedUser.first()
            if (authenticatedUser == null || !viewModel.userInfoRepository.checkTokenValidity()) {
                viewModel.transition(MainViewSelectorState.Unauthenticated)
                return@launch
            }
            try {
                val user = viewModel.userService.getUserById(id)
                if (user != null) {
                    Log.i(TAG, "Fetched user profile for user with ID: ${user.id}")
                    viewModel.transition(
                        MainViewSelectorState.UserInfo(
                            user,
                            authenticatedUser = authenticatedUser,
                            isCurrentUser = user.id == authenticatedUser.user?.id
                        )
                    )
                } else {
                    Log.e(TAG, "Error fetching user profile for user with ID: $id")
                    viewModel.transition(
                        MainViewSelectorState.Error(message = ErrorMessages.UNKNOWN) {
                            viewModel.transition(
                                MainViewSelectorState.SubscribedChannels(
                                    authenticatedUser = authenticatedUser,
                                    channels = viewModel.cacheManager.getChannels()
                                )
                            )
                        }
                    )
                }
            } catch (e: ServiceException) {
                if (e.type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.Error(message = e.message) {
                            viewModel.transition(
                                MainViewSelectorState.SubscribedChannels(
                                    authenticatedUser = authenticatedUser,
                                    channels = viewModel.cacheManager.getChannels()
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}