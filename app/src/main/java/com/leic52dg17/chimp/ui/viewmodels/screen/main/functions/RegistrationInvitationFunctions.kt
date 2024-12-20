package com.leic52dg17.chimp.ui.viewmodels.screen.main.functions

import androidx.lifecycle.viewModelScope
import com.leic52dg17.chimp.http.services.common.ServiceException
import com.leic52dg17.chimp.ui.screens.main.MainViewSelectorState
import com.leic52dg17.chimp.ui.viewmodels.screen.main.MainViewSelectorViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RegistrationInvitationFunctions(private val viewModel: MainViewSelectorViewModel) {
    fun createRegistrationInvitation(creatorId: Int) {
        viewModel.viewModelScope.launch {
            val authenticatedUser = viewModel.userInfoRepository.authenticatedUser.first()

            if (authenticatedUser?.user == null || !viewModel.userInfoRepository.checkTokenValidity()) {
                viewModel.transition(MainViewSelectorState.Unauthenticated)
                return@launch
            }

            try {
                val registrationInvitationId = viewModel.registrationInvitationService.createRegistrationInvitation(creatorId)
                val token = viewModel.registrationInvitationService.getRegistrationInvitation(registrationInvitationId)

                viewModel.transition(MainViewSelectorState.RegistrationInvitation(authenticatedUser = authenticatedUser, token = token))
            } catch (e: ServiceException) {
                viewModel.transition(
                    MainViewSelectorState.Error(message = e.message) {
                        viewModel.transition(MainViewSelectorState.SubscribedChannels(authenticatedUser = authenticatedUser))
                    }
                )
            }
        }
    }
}
