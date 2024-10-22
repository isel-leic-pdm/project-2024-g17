package com.leic52dg17.chimp.ui.viewmodels.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.leic52dg17.chimp.ui.screens.authentication.AuthenticationScreenState

class AuthenticationScreenViewModel : ViewModel() {
    var state: AuthenticationScreenState by mutableStateOf(AuthenticationScreenState.Landing)
}