package com.joshayoung.notemark.note.presentation.add_note

sealed interface AddEditNoteAction {
    data object NavigateBack : AddEditNoteAction
}