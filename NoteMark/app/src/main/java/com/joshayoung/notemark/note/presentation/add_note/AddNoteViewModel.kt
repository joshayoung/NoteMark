package com.joshayoung.notemark.note.presentation.add_note

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.core.utils.getTimeStampForInsert
import com.joshayoung.notemark.core.utils.textAsFlow
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import java.util.UUID

class AddNoteViewModel (
    val noteRepository: NoteRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var state by mutableStateOf(AddNoteState())
        private set

    private var currentNote: Note? = null

    init {
        var title = state.noteTitle
        var content = state.noteBody
        savedStateHandle.get<Int>("noteId")?.let { id ->
            if (id != -1) {

                viewModelScope.launch {
                    currentNote = noteRepository.getNote(id)

                    state = state.copy(
                        noteTitle = TextFieldState(
                            initialText = currentNote?.title ?: ""
                        ),
                        noteBody = TextFieldState(
                            initialText = currentNote?.content ?: ""
                        ),
                        isEdit = true
                    )
                    title = state.noteTitle
                    content = state.noteBody
                }
            }
        }
        combine(title.textAsFlow(), content.textAsFlow()) { title, body ->
            if (title != "" || body != "") {
                Log.d("combining flow", "Collecting")
            }
        }.launchIn(viewModelScope)
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

            AddNoteAction.OnCloseClick -> {

            }
        }
    }
}