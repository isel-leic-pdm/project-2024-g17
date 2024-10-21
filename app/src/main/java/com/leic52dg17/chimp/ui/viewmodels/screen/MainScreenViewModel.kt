package com.leic52dg17.chimp.ui.viewmodels.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.leic52dg17.chimp.ui.screens.main.MainScreenState

class MainScreenViewModel : ViewModel() {
    var state: MainScreenState by mutableStateOf(MainScreenState.SubscribedChannels)

    fun transition(newState: MainScreenState) {
        state = newState
    }
}

@Suppress("UNCHECKED_CAST")
class MainScreenViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainScreenViewModel() as T
    }
}