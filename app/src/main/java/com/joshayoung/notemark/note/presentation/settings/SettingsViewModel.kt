package com.joshayoung.notemark.note.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.core.domain.ConnectivityObserver
import com.joshayoung.notemark.core.domain.use_cases.NoteMarkUseCases
import com.joshayoung.notemark.core.navigation.Navigator
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import com.joshayoung.notemark.note.domain.repository.SyncRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    val connectivityObserver: ConnectivityObserver,
    val noteMarkUseCases: NoteMarkUseCases,
    val navigator: Navigator,
    val noteRepository: NoteRepository,
    val syncRepository: SyncRepository,
) : ViewModel() {
    var state by mutableStateOf(SettingsState())
        private set

    var isNotConnected: Boolean = false
    var hasNoPendingSyncs: Boolean = false

    private val eventChannel = Channel<SettingsEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            syncRepository.hasPendingSyncs().collect { value ->
                hasNoPendingSyncs = value
            }
        }

        viewModelScope.launch {
            connectivityObserver.isConnected.collect { value ->
                isNotConnected = !value
            }
        }
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.CheckForUnsyncedChanges -> {
                viewModelScope.launch {
                    if (isNotConnected) {
                        eventChannel.send(SettingsEvent.InternetOfflineCannotLogout)

                        return@launch
                    }

                    if (!hasNoPendingSyncs) {
                        viewModelScope.launch {
                            noteRepository.logout()
                        }
                        noteMarkUseCases.clearLocalDataUseCase()
                        return@launch
                    } else {
                        state = state.copy(displayLogoutPrompt = true)
                    }
                }
            }

            SettingsAction.LogOutWithoutSyncing -> {
                state = state.copy(displayLogoutPrompt = false)
                viewModelScope.launch {
                    noteRepository.logout()
                }
                noteMarkUseCases.clearLocalDataUseCase()
            }

            SettingsAction.NavigateBack -> {
                viewModelScope.launch {
                    navigator.navigateUp()
                }
            }

            is SettingsAction.SetSyncInterval -> {
                action
                viewModelScope.launch {
                    noteMarkUseCases.setSyncIntervalUseCase(action.interval)
                    state = state.copy(interval = action.interval)
                }
            }

            SettingsAction.Sync -> {
                viewModelScope.launch {
                    state = state.copy(isSyncing = true, displayLogoutPrompt = false)
                    noteMarkUseCases.syncNotesUseCase()
                    noteMarkUseCases.pullRemoteNotesUseCase()
                    state = state.copy(isSyncing = false)
                }
            }
        }
    }
}