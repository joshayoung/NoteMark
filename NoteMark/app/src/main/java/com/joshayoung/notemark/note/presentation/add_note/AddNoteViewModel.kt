package com.joshayoung.notemark.note.presentation.add_note

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import kotlinx.coroutines.launch

class AddNoteViewModel (
    val noteRepository: NoteRepository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(AddNoteState())

    private var currentNote: Note? = null

    init {
        savedStateHandle.get<String>("noteId")?.let { id ->
            if (id != "") {
                viewModelScope.launch {
                    // TODO: get this from local db:
                    var notes = noteRepository.getNotes().notes?.let { notes ->
                        currentNote = notes.find { n ->
                            n.id == id
                        }

                        state = state.copy(
                            noteTitle = TextFieldState(
                                initialText = currentNote?.title ?: ""
                            ),
                            noteBody = TextFieldState(
                                initialText = currentNote?.content ?: ""
                            )
                        )

                        println("test")
                    }
                }
            }
        }
    }

    fun onAction(action: AddNoteAction) {
        when(action) {
            AddNoteAction.OnSaveClick -> {
                viewModelScope.launch {

                    if (currentNote != null) {

                        noteRepository.updateNote(currentNote, state.noteTitle.text.toString(), state.noteBody.text.toString())

                        return@launch
                    }
                    noteRepository.createNote(
                        state.noteTitle.text.toString(),
                        state.noteBody.text.toString()
                    )
                }
            }
        }

    }
}