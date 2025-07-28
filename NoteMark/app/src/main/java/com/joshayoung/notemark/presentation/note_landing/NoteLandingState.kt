package com.joshayoung.notemark.presentation.note_landing

import com.joshayoung.notemark.domain.models.Note
import com.joshayoung.notemark.domain.models.Notes

data class NoteLandingState(
    var notes: Notes,
    var hasItems: Boolean = false
)
