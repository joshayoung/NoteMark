package com.joshayoung.notemark.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.core.domain.AuthDataStorage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val authDataStorage: AuthDataStorage,
) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    private val eventChannel = Channel<String?>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
//            authDataStorage.values
//                .collect { value ->
//                    eventChannel.send(value.accessToken)
//                }
        }

        viewModelScope.launch {
            state = state.copy(isCheckingSession = true)
            state = state.copy(isAuthenticated = checkAccessToken())
            state = state.copy(isCheckingSession = false)
        }
    }

    private suspend fun checkAccessToken(): Boolean {
        return false
//        val token = authDataStorage.getAuthData().first().accessToken

//        return token != ""
    }
}