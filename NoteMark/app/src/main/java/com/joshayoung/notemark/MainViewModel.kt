package com.joshayoung.notemark

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.domain.SessionStorage
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionStorage: SessionStorage
) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(isCheckingSession = true)
            state = state.copy(isAuthenticated = sessionStorage.get()?.accessToken != null)
            state = state.copy(isCheckingSession = false)
        }

    }
}