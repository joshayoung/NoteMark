package com.joshayoung.notemark.note.presentation.note_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.joshayoung.notemark.core.navigation.Destination
import com.joshayoung.notemark.core.navigation.Navigator
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import com.joshayoung.notemark.note.presentation.note_list.mappers.toNoteUi
import kotlinx.coroutines.launch

class NoteDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository,
    private val navigator: Navigator
) : ViewModel() {
    var state by mutableStateOf(NoteDetailState())
        private set

    init {
        val noteId = savedStateHandle.toRoute<Destination.NoteDetail>().id

        viewModelScope.launch {
            noteRepository.getNote(noteId)?.let { note ->
                var noteUi = note.toNoteUi()
                state = state.copy(
                    dataCreated = noteUi.createdAt,
                    lastEdited = noteUi.lastEditedAt.toString(),
                    title = noteUi.title,
                    body = noteUi.content,
                    viewMode = NoteViewMode.DISPLAY
                )
            }
        }
    }

    fun onAction(action: NoteDetailAction) {
        when(action) {
            NoteDetailAction.GoBack -> {
                viewModelScope.launch {
                    navigator.navigateUp()
                }
            }
        }
    }
}