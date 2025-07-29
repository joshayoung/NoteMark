package com.joshayoung.notemark

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.domain.DataStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataStorage: DataStorage,

) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    val authData: StateFlow<String> = dataStorage.values
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = "loading"
        )

    init {
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

        return token != null && token != "unset"
    }
}