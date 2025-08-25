package com.joshayoung.notemark.note.domain.models

import com.joshayoung.notemark.note.network.NoteDto
import kotlinx.serialization.Serializable

@Serializable
data class NotesResponse(
    val notes: List<NoteDto>,
    val total: Int = 0,
)