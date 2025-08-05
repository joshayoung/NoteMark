package com.joshayoung.notemark.note.presentation.add_note

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.core.utils.getTimeStampForInsert
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import kotlinx.coroutines.launch
import java.util.UUID

class AddNoteViewModel (
    val noteRepository: NoteRepository,
    val savedStateHandle: SavedStateHandle,
    val localDataSource: LocalDataSource
) : ViewModel() {
    var state by mutableStateOf(AddNoteState())

    private var currentNote: Note? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { id ->
            viewModelScope.launch {
                // TODO: get this from local db:
                var currentNote = noteRepository.getNote(id)

                state = state.copy(
                    noteTitle = TextFieldState(
                        initialText = currentNote?.title ?: ""
                    ),
                    noteBody = TextFieldState(
                        initialText = currentNote?.content ?: ""
                    )
                )
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

                    val note = Note(
                        remoteId = UUID.randomUUID().toString(),
                        title = state.noteTitle.text.toString(),
                        content = state.noteBody.text.toString(),
                        createdAt = getTimeStampForInsert()
                    )

                    noteRepository.createNote(note)
                }
            }
        }

    }
}