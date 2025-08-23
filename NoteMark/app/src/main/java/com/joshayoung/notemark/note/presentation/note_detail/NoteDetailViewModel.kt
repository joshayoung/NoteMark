package com.joshayoung.notemark.note.presentation.note_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.joshayoung.notemark.core.navigation.Destination
import com.joshayoung.notemark.core.navigation.Destination.AddNoteScreen
import com.joshayoung.notemark.core.navigation.Navigator
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import com.joshayoung.notemark.note.presentation.mappers.toNoteUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository,
    private val navigator: Navigator,
) : ViewModel() {
    private var _state = MutableStateFlow(NoteDetailState())
    val state =
        _state
            .onStart {
                loadData()
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(1000L),
                NoteDetailState(),
            )

    fun loadData() {
        val noteId = savedStateHandle.toRoute<Destination.NoteDetailScreen>().id
//        var t = savedStateHandle.get<String>("noteId")
//        var tt = savedStateHandle.get<String>("changedNote")
//        var ttt = savedStateHandle.get<String>("bla")
        viewModelScope.launch {
            noteRepository.getNote(noteId)?.let { note ->
                val noteUi = note.toNoteUi()
                _state.update {
                    it.copy(
                        noteId = noteUi.id,
                        dataCreated = noteUi.dateCreated,
                        lastEdited = noteUi.dateLastEdited,
                        title = noteUi.title,
                        body = noteUi.content ?: "",
                        viewMode = NoteViewMode.DISPLAY,
                    )
                }
            }
        }
    }

    fun onAction(action: NoteDetailAction) {
        when (action) {
            NoteDetailAction.GoBack -> {
                viewModelScope.launch {
                    navigator.navigateUp()
                }
            }

            is NoteDetailAction.GoToEdit -> {
                viewModelScope.launch {
                    navigator.navigate(
                        AddNoteScreen(id = action.id),
                    )
                }
            }
        }
    }
}