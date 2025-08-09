package com.joshayoung.notemark.note.presentation.note_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NoteDetailViewModel : ViewModel() {
    var state by mutableStateOf(NoteDetailState())
        private set
}