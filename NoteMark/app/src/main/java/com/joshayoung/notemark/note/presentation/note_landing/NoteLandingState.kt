package com.joshayoung.notemark.note.presentation.note_landing

import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.domain.models.NotesData

data class NoteLandingState(
    var notes: List<Note>,
    var hasItems: Boolean = false
)
