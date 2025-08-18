package com.joshayoung.notemark.note.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.auth.data.repository.AuthRepositoryImpl
import com.joshayoung.notemark.core.ConnectivityObserver
import com.joshayoung.notemark.core.domain.DataStorage
import com.joshayoung.notemark.core.navigation.Navigator
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import com.joshayoung.notemark.note.domain.database.LocalSyncDataSource
import com.joshayoung.notemark.note.domain.use_cases.PullRemoteNotesUseCase
import com.joshayoung.notemark.note.domain.use_cases.SyncNotesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    val authRepositoryImpl: AuthRepositoryImpl,
    val dataStorage: DataStorage,
    val connectivityObserver: ConnectivityObserver,
    val localDataSource: LocalDataSource,
    val localSyncDataSource: LocalSyncDataSource,
    val navigator: Navigator,
    val syncNotesUseCase: SyncNotesUseCase,
    val pullRemoteNotesUseCase: PullRemoteNotesUseCase
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
                interval = interval,
                hasUnsyncedChanges = true
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

                    // Add Use Case:
                    // Clear all the notes
                    localDataSource.removeAllNotes()

                    // Clear sync queue
                    localSyncDataSource.clearSyncQueue()
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
                    state = state.copy(isSyncing = true)
                    syncNotesUseCase.execute()
                    pullRemoteNotesUseCase.execute()
                    state = state.copy(isSyncing = false)
                }
            }
        }
    }
}