package com.joshayoung.notemark.presentation.add_note

sealed interface AddNoteAction {
    data object OnSaveClick: AddNoteAction
}