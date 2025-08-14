package com.joshayoung.notemark.note.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.auth.data.repository.AuthRepositoryImpl
import com.joshayoung.notemark.core.domain.DataStorage
import com.joshayoung.notemark.core.navigation.Navigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsViewModel(
    val authRepositoryImpl: AuthRepositoryImpl,
    val dataStorage: DataStorage,
    val navigator: Navigator
) : ViewModel() {
    var state by mutableStateOf(SettingsState())
        private set

    init {
        viewModelScope.launch {
            var interval = dataStorage.getSyncInterval()
            state = state.copy(
                interval = interval
            )
        }
    }

    fun onAction(action : SettingsAction) {
        when(action) {
            SettingsAction.OnLogoutClick -> {
                viewModelScope.launch {
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