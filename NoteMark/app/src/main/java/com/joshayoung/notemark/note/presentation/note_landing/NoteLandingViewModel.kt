package com.joshayoung.notemark.note.presentation.note_landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import com.joshayoung.notemark.core.domain.DataStorage
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import com.joshayoung.notemark.note.presentation.note_landing.mappers.toNoteUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteLandingViewModel(
    private val noteRepository: NoteRepository,
    private val dataStorage: DataStorage,
    private val localDataSource: LocalDataSource
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

    init {
        viewModelScope.launch {
            dataStorage.username.collect { user ->
                _state.update {
                    it.copy(
                        userAbbreviation = formatUser(user)
                    )
                }
            }
        }
    }

    private fun formatUser(user: String): String {
        if (user.length > 1)
        {
            val firstTwo = user.take(2)

            return firstTwo.uppercase()
        }

        return user.uppercase()
    }

    fun onAction(action: NoteLandingAction) {
        when(action) {
            NoteLandingAction.OnLogoutClick -> {
                viewModelScope.launch {
                    dataStorage.saveAuthData(null)
                }
            }
            is NoteLandingAction.OnDeleteClick -> {
                viewModelScope.launch {
                    noteRepository.deleteNote(
                        id = action.id
                    )
                    loadData()
                }
            }
        }
    }

    private suspend fun loadData() {
        localDataSource.getNotes().map { notes ->
            val noteUi = notes.map { it.toNoteUi() }
            _state.update {
                it.copy(
                    notes = noteUi,
                    hasItems = true
                )
            }
        }.launchIn(viewModelScope)
    }
}