package com.joshayoung.notemark.note.data.mappers

import com.joshayoung.notemark.note.data.database.entity.NoteEntity
import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.network.NoteDto
import com.joshayoung.notemark.note.presentation.note_list.model.NoteUi

fun Note.toNoteEntity() : NoteEntity {
    return NoteEntity(
        id  = id,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt,
        remoteId = remoteId
    )
}

fun NoteEntity.toNote() : Note {
    return Note(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt,
        remoteId = remoteId
    )
}

fun Note.toNoteDto() : NoteDto {
    return NoteDto(
        id  = remoteId,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt
    )
}

fun NoteUi.toNote() : Note {
    return Note(
        id  = id,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt,
        remoteId = remoteId
    )
}
