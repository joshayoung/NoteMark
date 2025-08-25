package com.joshayoung.notemark.note.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.core.ConnectivityObserver
import com.joshayoung.notemark.core.domain.DataStorage
import com.joshayoung.notemark.core.domain.use_cases.NoteMarkUseCases
import com.joshayoung.notemark.core.navigation.Navigator
import com.joshayoung.notemark.note.data.SyncNoteWorkerScheduler
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import com.joshayoung.notemark.note.domain.database.LocalSyncDataSource
import com.joshayoung.notemark.note.domain.models.SyncInterval
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class SettingsViewModel(
    val dataStorage: DataStorage,
    val connectivityObserver: ConnectivityObserver,
    val noteMarkUseCases: NoteMarkUseCases,
    val localDataSource: LocalDataSource,
    val localSyncDataSource: LocalSyncDataSource,
    val navigator: Navigator,
    private val syncNoteWorkerScheduler: SyncNoteWorkerScheduler,
    val noteRepository: NoteRepository,
    val applicationScope: CoroutineScope,
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
                        clearLocalData()
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
                clearLocalData()
            }

            SettingsAction.NavigateBack -> {
                viewModelScope.launch {
                    navigator.navigateUp()
                }
            }

            is SettingsAction.SetSyncInterval -> {
                action
                viewModelScope.launch {
                    dataStorage.saveSyncInterval(action.interval)
                    state =
                        state.copy(
                            interval = action.interval,
                        )

                    // TODO: Temporary:
                    val interval =
                        when (state.interval) {
                            SyncInterval.FIFTEEN -> 15.minutes
                            SyncInterval.THIRTY -> 30.minutes
                            SyncInterval.HOUR -> 1.hours
                            else -> 30.minutes
                        }

                    viewModelScope.launch {
                        syncNoteWorkerScheduler.scheduleSync(
                            interval = interval,
                        )
                    }
                }
            }

            SettingsAction.Sync -> {
                viewModelScope.launch {
                    state = state.copy(isSyncing = true, displayLogoutPrompt = false)
                    noteMarkUseCases.syncNotesUseCase.execute()
                    noteMarkUseCases.pullRemoteNotesUseCase.execute()
                    state = state.copy(isSyncing = false)
                }
            }
        }
    }

    // Add Use Case:
    private fun clearLocalData() {
        applicationScope.launch {
            syncNoteWorkerScheduler.cancelSyncs()
            localSyncDataSource.clearSyncQueue()
            localDataSource.removeAllNotes()
        }
    }
}