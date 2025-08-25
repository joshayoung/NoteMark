package com.joshayoung.notemark.note.presentation.add_note

import androidx.compose.foundation.text.input.TextFieldState

data class AddEditNoteState(
    val noteTitle: TextFieldState = TextFieldState(),
    val noteBody: TextFieldState = TextFieldState(),
    val inEditMode: Boolean = false,
    val hasChangeInitialContent: Boolean = false,
)