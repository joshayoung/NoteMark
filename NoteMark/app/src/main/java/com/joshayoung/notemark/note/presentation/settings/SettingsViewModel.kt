package com.joshayoung.notemark.note.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.auth.data.repository.AuthRepositoryImpl
import com.joshayoung.notemark.auth.presentation.registration.RegistrationEvent
import com.joshayoung.notemark.core.ConnectivityObserver
import com.joshayoung.notemark.core.domain.DataStorage
import com.joshayoung.notemark.core.navigation.Navigator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    val authRepositoryImpl: AuthRepositoryImpl,
    val dataStorage: DataStorage,
    val connectivityObserver: ConnectivityObserver,
    val navigator: Navigator
) : ViewModel() {
    var state by mutableStateOf(SettingsState())
        private set

    var isNotConnected: Boolean = false

    private val eventChannel = Channel<SettingsEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            var interval = dataStorage.getSyncInterval()
            state = state.copy(
                interval = interval
            )
        }

        viewModelScope.launch {
            connectivityObserver.isConnected.collect { value ->
                isNotConnected = !value
            }
        }
    }

    fun onAction(action : SettingsAction) {
        when(action) {
            SettingsAction.OnLogoutClick -> {
                viewModelScope.launch {
                    if (isNotConnected) {
                        eventChannel.send(SettingsEvent.InternetOfflineCannotLogout)

                        return@launch
                    }

                    authRepositoryImpl.logout()
                }
            }

            SettingsAction.NavigateBack -> {
                viewModelScope.launch {
                    navigator.navigateUp()
                }
            }

            is SettingsAction.SetSyncInterval -> { action
                viewModelScope.launch {
                    dataStorage.saveSyncInterval(action.interval)
                    state = state.copy(
                        interval = action.interval
                    )
                }
            }

            SettingsAction.Sync -> {
                viewModelScope.launch {
                    state = state.copy(
                        isSyncing = true
                    )
                    delay(2000)
                    state = state.copy(
                        isSyncing = false
                    )
                }
            }
        }
    }
}