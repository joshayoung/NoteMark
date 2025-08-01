package com.joshayoung.notemark.note.presentation.note_landing

import com.joshayoung.notemark.note.domain.models.Notes

data class NoteLandingState(
    var notes: Notes,
    var hasItems: Boolean = false
)
