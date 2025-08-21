package com.joshayoung.notemark

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.auth.domain.models.LoginResponse
import com.joshayoung.notemark.core.domain.DataStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataStorage: DataStorage,
    private val applicationScope: CoroutineScope
) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    private val eventChannel = Channel<String>()
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
            state = state.copy(
                isAuthenticated = checkAccessToken()
            )
            state = state.copy(isCheckingSession = false)
        }
    }

    private suspend fun checkAccessToken() : Boolean {
        val token = dataStorage.getAuthData().first().accessToken

        // TODO: Only check for null:
        return token != null && token != "unset" && token != ""
    }

    fun clearToken() {
        // NOTE: Clear the token to prevent the data flow from triggering with 'unset':
        viewModelScope.launch {
            if (state.isAuthenticated) {
                return@launch
            }

            dataStorage.saveAuthData(LoginResponse(
                accessToken = null,
                refreshToken = null,
                username = null
            ))
        }
    }
}