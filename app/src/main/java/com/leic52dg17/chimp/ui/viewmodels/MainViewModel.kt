package com.leic52dg17.chimp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.leic52dg17.chimp.ui.screens.main.MainScreenState

class MainViewModel : ViewModel() {
    var state: MainScreenState by mutableStateOf(MainScreenState.CreateChannel)
}