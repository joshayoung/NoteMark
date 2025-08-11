package com.joshayoung.notemark.note.presentation.add_note

sealed interface AddNoteAction {
    data object OnSaveClick: AddNoteAction
    data object OnCloseClick: AddNoteAction
    data object NavigateBack: AddNoteAction
}