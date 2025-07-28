package com.joshayoung.notemark.presentation.note_landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.data.DataStorageImpl
import com.joshayoung.notemark.domain.repository.NoteMarkRepository
import com.joshayoung.notemark.domain.DataStorage
import com.joshayoung.notemark.domain.models.Notes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteLandingViewModel(
    private val noteMarkRepository: NoteMarkRepository,
    private val dataStorage: DataStorage
) : ViewModel() {

    private var _state = MutableStateFlow(NoteLandingState(notes = Notes(notes = emptyList())))
    val state = _state
        .onStart {
            loadData()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1000L),
            NoteLandingState(notes = Notes(notes = emptyList()))
        )

    val authData: Flow<String?> = dataStorage.getAuthData()
        .map { preferences -> preferences.refreshToken  }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Loading Token..."
        )

    fun onAction(action: NoteLandingAction) {
        when(action) {
            NoteLandingAction.OnLogoutClick -> {
                viewModelScope.launch {
                    dataStorage.saveAuthData(null)
                }
            }
        }
    }

    private suspend fun loadData() {
        val notes = noteMarkRepository.getNotes().notes
        if (notes != null) {
            _state.update {
                it.copy(
                    notes = notes,
                    hasItems = true
                )
            }
        }
    }
}