package com.joshayoung.notemark.note.data.mappers

import com.joshayoung.notemark.note.data.database.entity.NoteEntity
import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.network.NoteDto

fun Note.toNoteEntity() : NoteEntity {
    return NoteEntity(
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt
    )
}

fun NoteEntity.toNote() : Note {
    return Note(
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt
    )
}

fun Note.toNoteDto() : NoteDto {
    return NoteDto(
        id  = id,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt
    )
}
