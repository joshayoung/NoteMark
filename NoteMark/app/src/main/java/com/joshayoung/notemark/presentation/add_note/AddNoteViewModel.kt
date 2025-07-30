package com.joshayoung.notemark.presentation.add_note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.domain.models.Note
import com.joshayoung.notemark.domain.repository.NoteMarkRepository
import kotlinx.coroutines.launch

class AddNoteViewModel (
    val noteMarkRepository: NoteMarkRepository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(AddNoteState())

    private var currentNoteId : Int? = null

    init {
        savedStateHandle.get<String>("noteId")?.let { id ->
            var t = id
        }
    }

    fun onAction(action: AddNoteAction) {
        when(action) {
            AddNoteAction.OnSaveClick -> {
                viewModelScope.launch {
                    noteMarkRepository.createNote(
                        state.noteTitle.text.toString(),
                        state.noteBody.text.toString()
                    )
                }
            }
        }

    }
}