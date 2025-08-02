package com.joshayoung.notemark.note.presentation.note_landing.mappers

import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.presentation.note_landing.model.NoteUi

fun Note.toNoteUi() : NoteUi {
    return NoteUi(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt
    )
}