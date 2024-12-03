package com.leic52dg17.chimp.ui.viewmodels.screen.main.functions

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.core.shared.SharedPreferencesHelper
import com.leic52dg17.chimp.http.services.common.ServiceErrorTypes
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel.Companion.TAG
import kotlinx.coroutines.launch

class UserFunctions(private val viewModel: MainViewSelectorViewModel) {
    fun getUserProfile(id: Int) {
        Log.i(TAG, "Getting user profile for user with ID: $id")
        viewModel.transition(MainViewSelectorState.Loading)
        viewModel.viewModelScope.launch {
            val authenticatedUser = SharedPreferencesHelper.getAuthenticatedUser(viewModel.context)
            if (authenticatedUser == null || !SharedPreferencesHelper.checkTokenValidity(viewModel.context)) {
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
                            authenticatedUser = authenticatedUser
                        )
                    )
                } else {
                    Log.e(TAG, "Error fetching user profile for user with ID: $id")
                    viewModel.transition(
                        MainViewSelectorState.SubscribedChannels(
                            true,
                            "Unexpected error occurred when navigating to user info",
                            authenticatedUser = authenticatedUser
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "Exception thrown in getUserProfile with user id $id")
                if ((e as ServiceException).type === ServiceErrorTypes.Unauthorized) {
                    viewModel.transition(MainViewSelectorState.Unauthenticated)
                } else {
                    viewModel.transition(
                        MainViewSelectorState.SubscribedChannels(
                            true,
                            e.message,
                            authenticatedUser = authenticatedUser
                        )
                    )
                }
            }
        }
    }
}