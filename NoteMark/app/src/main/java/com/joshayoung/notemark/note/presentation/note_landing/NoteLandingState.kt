package com.joshayoung.notemark.note.presentation.note_landing

import com.joshayoung.notemark.note.presentation.note_landing.model.NoteUi

data class NoteLandingState(
    var notes: List<NoteUi>,
    var hasItems: Boolean = false,
    var userAbbreviation: String = ""
)
