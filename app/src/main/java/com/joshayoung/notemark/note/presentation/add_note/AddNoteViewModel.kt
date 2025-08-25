package com.joshayoung.notemark.note.presentation.add_note

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.joshayoung.notemark.core.data.networking.Result
import com.joshayoung.notemark.core.domain.use_cases.NoteMarkUseCases
import com.joshayoung.notemark.core.navigation.Destination
import com.joshayoung.notemark.core.navigation.Navigator
import com.joshayoung.notemark.core.presentation.DebounceNoteSave
import com.joshayoung.notemark.core.utils.textAsFlow
import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class AddNoteViewModel(
    val noteRepository: NoteRepository,
    private val navigator: Navigator,
    savedStateHandle: SavedStateHandle,
    private val noteMarkUseCases: NoteMarkUseCases,
) : ViewModel() {
    var state by mutableStateOf(AddNoteState())
        private set

    private var currentNote: Note? = null
    private var initialTitle: String = ""
    private var initialBody: String = ""
    private var debounceNoteSave: DebounceNoteSave = DebounceNoteSave()

    init {
        viewModelScope.launch {
            val n = savedStateHandle.toRoute<Destination.AddNoteScreen>()
            n.id?.let { theId ->
                currentNote = noteRepository.getNote(theId)
                initialTitle = currentNote?.title ?: ""
                initialBody = currentNote?.content ?: ""
                state =
                    state.copy(
                        noteTitle =
                            TextFieldState(
                                initialText = currentNote?.title ?: "",
                            ),
                        noteBody =
                            TextFieldState(
                                initialText = currentNote?.content ?: "",
                            ),
                        inEditMode = true,
                    )
            } ?: run {
                var result = noteRepository.createNote(title = "", body = "")
                if (result is Result.Success) {
                    currentNote = result.data
                }
            }

            combine(state.noteTitle.textAsFlow(), state.noteBody.textAsFlow()) { title, body ->
                if (title != initialTitle || body != initialBody) {
                    state =
                        state.copy(
                            hasChangeInitialContent = true,
                        )
                    autoSaveNote(title.toString(), body.toString())
                }
            }.launchIn(viewModelScope)
        }
    }

    val autoSaveNote =
        debounceNoteSave.debounce(viewModelScope, 1000) { title: String, body: String ->
            viewModelScope.launch {
                val result = noteRepository.updateNote(currentNote, title, body)
                if (result is Result.Success) {
                    currentNote = result.data
                    noteMarkUseCases.updateSyncQueueUseCase(currentNote, state.inEditMode)
                }
            }
        }

    fun onAction(action: AddNoteAction) {
        when (action) {
            AddNoteAction.NavigateBack -> {
                viewModelScope.launch {
                    noteMarkUseCases.deleteEmptyNoteUseCase(currentNote)
                    navigator.navigateUp()
                }
            }
        }
    }
}