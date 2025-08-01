package com.joshayoung.notemark.note.domain.models

import com.joshayoung.notemark.note.network.NoteDto
import kotlinx.serialization.Serializable

@Serializable
data class NotesData(
    val notes: List<NoteDto>,
    val total: Int = 0
)