package com.joshayoung.notemark.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.core.domain.DataStorage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataStorage: DataStorage,
) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    private val eventChannel = Channel<String?>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            dataStorage.values
                .collect { value ->
                    eventChannel.send(value)
                }
        }

        viewModelScope.launch {
            state = state.copy(isCheckingSession = true)
            state = state.copy(isAuthenticated = checkAccessToken())
            state = state.copy(isCheckingSession = false)
        }
    }

    private suspend fun checkAccessToken(): Boolean {
        val token = dataStorage.getAuthData().first().accessToken

        return token != ""
    }
}