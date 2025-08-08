package com.joshayoung.notemark.note.network

import com.joshayoung.notemark.note.domain.models.Note

fun NoteDto.toNote(): Note {
    return Note(
        remoteId = id,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt
    )
}