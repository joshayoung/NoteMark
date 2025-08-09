package com.joshayoung.notemark.note.presentation.note_list.mappers

import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.presentation.note_list.model.NoteUi

fun Note.toNoteUi() : NoteUi {
    return NoteUi(
        id = id,
        remoteId = remoteId,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt
    )
}