package com.joshayoung.notemark.presentation.add_note

import androidx.compose.foundation.text.input.TextFieldState

data class AddNoteState(
    val noteTitle: TextFieldState = TextFieldState(),
    val noteBody: TextFieldState = TextFieldState()
)