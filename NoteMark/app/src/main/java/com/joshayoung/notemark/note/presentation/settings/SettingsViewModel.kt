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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
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
    var hasNoPendingSyncs: Boolean = false

    private val eventChannel = Channel<SettingsEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            localSyncDataSource.hasPendingSyncs().collect { value ->
                hasNoPendingSyncs = value
            }
        }

        viewModelScope.launch {
            connectivityObserver.isConnected.collect { value ->
                isNotConnected = !value
            }
        }
    }

    fun onAction(action : SettingsAction) {
        when(action) {
            SettingsAction.CheckForUnsyncedChanges -> {
                viewModelScope.launch {
                    if (isNotConnected) {
                        eventChannel.send(SettingsEvent.InternetOfflineCannotLogout)

                        return@launch
                    }

                    if (!hasNoPendingSyncs) {
                        localDataSource.removeAllNotes()
                        localSyncDataSource.clearSyncQueue()
                        authRepositoryImpl.logout()
                        return@launch
                    } else {
                        state = state.copy(displayLogoutPrompt = true)
                    }
                }
            }

            SettingsAction.LogOutWithoutSyncing -> {
                state = state.copy(displayLogoutPrompt = false)
                viewModelScope.launch {
                    // Add Use Case:
                    localDataSource.removeAllNotes()
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
                    state = state.copy(isSyncing = true, displayLogoutPrompt = false)
                    syncNotesUseCase.execute()
                    pullRemoteNotesUseCase.execute()
                    state = state.copy(isSyncing = false)
                }
            }
        }
    }
}