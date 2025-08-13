package com.joshayoung.notemark.note.presentation.add_note

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.toRoute
import com.joshayoung.notemark.core.navigation.Destination
import com.joshayoung.notemark.core.navigation.Navigator
import com.joshayoung.notemark.core.utils.textAsFlow
import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class AddNoteViewModel (
    val noteRepository: NoteRepository,
    private val navigator: Navigator,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var state by mutableStateOf(AddNoteState())
        private set

    private var currentNote: Note? = null
    private var initialTitle : String = ""
    private var initialBody : String = ""

    init {
        viewModelScope.launch {
            val n = savedStateHandle.toRoute<Destination.AddNote>()
            val id = n.id
            if (id != null) {
                state = state.copy(
                    inEditMode = true
                )
                currentNote = noteRepository.getNote(id)
                initialTitle = currentNote?.title ?: ""
                initialBody = currentNote?.content ?: ""
            }

            state = state.copy(
                noteTitle = TextFieldState(
                    initialText = initialTitle
                ),
                noteBody = TextFieldState(
                    initialText = initialBody
                )
            )

            // TODO: Setup auto-saving
            combine(state.noteTitle.textAsFlow(), state.noteBody.textAsFlow()) { title, body ->
                if (title != initialTitle || body != initialBody) {
                    state = state.copy(
                        hasChangeInitialContent = true
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onAction(action: AddNoteAction) {
        when(action) {
            AddNoteAction.OnSaveClick -> {
                viewModelScope.launch {
                    if (currentNote != null) {

                        noteRepository.updateNote(currentNote, state.noteTitle.text.toString(), state.noteBody.text.toString())

                        //navigator.previousBackStackEntry("changedNote", "9")
                        navigator.navigateUp()
                        return@launch
                    }

                    noteRepository.createNote(
                        title = state.noteTitle.text.toString(),
                        body = state.noteBody.text.toString()
                    )

                    navigator.navigateUp()
                }
            }

            AddNoteAction.OnCloseClick -> {

            }

            AddNoteAction.NavigateBack -> {
                viewModelScope.launch {
                    navigator.navigateUp()
                }
            }
        }
    }
}