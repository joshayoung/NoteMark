package com.joshayoung.notemark.note.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.core.domain.ConnectivityObserver
import com.joshayoung.notemark.core.domain.DataStorage
import com.joshayoung.notemark.core.domain.use_cases.NoteMarkUseCases
import com.joshayoung.notemark.core.navigation.Destination.*
import com.joshayoung.notemark.core.navigation.Navigator
import com.joshayoung.notemark.note.data.mappers.toNote
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import com.joshayoung.notemark.note.presentation.mappers.toNoteUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteListViewModel(
    private val noteRepository: NoteRepository,
    private val dataStorage: DataStorage,
    private val connectivityObserver: ConnectivityObserver,
    private val localDataSource: LocalDataSource,
    private val noteMarkUseCases: NoteMarkUseCases,
    private val navigator: Navigator,
) : ViewModel() {
    private var _state = MutableStateFlow(NoteListState(notes = emptyList()))
    val state =
        _state
            .onStart {
                loadData()
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(1000L),
                NoteListState(notes = emptyList()),
            )

    val isConnected =
        connectivityObserver
            .isConnected
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                false,
            )

    init {
        viewModelScope.launch {
            dataStorage.username.collect { user ->
                _state.update {
                    it.copy(
                        userAbbreviation = formatUser(user),
                    )
                }
            }
        }
    }

    private fun formatUser(user: String): String {
        if (user.length > 1) {
            val firstTwo = user.take(2)

            return firstTwo.uppercase()
        }

        return user.uppercase()
    }

    fun onAction(action: NoteListAction) {
        when (action) {
            is NoteListAction.OnDeleteClick -> {
                viewModelScope.launch {
                    noteRepository.deleteNote(
                        note = action.noteUi.toNote(),
                    )
                }
            }

            is NoteListAction.AddNoteClick -> {
                viewModelScope.launch {
                    navigator.navigate(AddNoteScreen(null))
                }
            }

            is NoteListAction.GoToDetail -> {
                viewModelScope.launch {
                    noteMarkUseCases.syncNotesUseCase()
                    navigator.navigate(
                        NoteDetailScreen(id = action.id),
                    )
                }
            }

            NoteListAction.OnSettingsClick -> {
                viewModelScope.launch {
                    navigator.navigate(SettingsScreen)
                }
            }
        }
    }

    private fun loadData() {
        localDataSource
            .getNotes()
            .map { notes ->
                val noteUi = notes.map { it.toNoteUi() }
                _state.update {
                    it.copy(
                        notes = noteUi,
                        hasItems = true,
                    )
                }
            }.launchIn(viewModelScope)
    }
}