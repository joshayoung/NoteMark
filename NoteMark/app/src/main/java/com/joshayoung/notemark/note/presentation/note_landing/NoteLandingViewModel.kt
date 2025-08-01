package com.joshayoung.notemark.note.presentation.note_landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import com.joshayoung.notemark.core.domain.DataStorage
import com.joshayoung.notemark.note.domain.models.NotesData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteLandingViewModel(
    private val noteMarkRepository: NoteRepository,
    private val dataStorage: DataStorage
) : ViewModel() {

    private var _state = MutableStateFlow(NoteLandingState(notes = emptyList()))
    val state = _state
        .onStart {
            loadData()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1000L),
            NoteLandingState(notes = emptyList())
        )

    fun onAction(action: NoteLandingAction) {
        when(action) {
            NoteLandingAction.OnLogoutClick -> {
                viewModelScope.launch {
                    dataStorage.saveAuthData(null)
                }
            }
            is NoteLandingAction.OnDeleteClick -> {
                viewModelScope.launch {
                    noteMarkRepository.deleteNote(
                        id = action.id
                    )
                    loadData()
                }
            }
            NoteLandingAction.OnEditClick -> {
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