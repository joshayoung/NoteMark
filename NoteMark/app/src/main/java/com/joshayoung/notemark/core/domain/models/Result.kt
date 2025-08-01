package com.joshayoung.notemark.core.domain.models

import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.domain.models.NotesData

data class Result(
    var success: Boolean,
    var error: Error? = null,
    val note: Note? = null,
    val notes: List<Note>? = null
)