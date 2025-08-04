package com.joshayoung.notemark.note.domain.repository

import com.joshayoung.notemark.note.domain.models.Note

typealias Result = com.joshayoung.notemark.core.domain.models.Result

interface NoteRepository {
    suspend fun createNote(note: Note): Result
    suspend fun updateNote(note: Note?, title: String, body: String): Result
    suspend fun getNotes(): Result
    suspend fun deleteNote(id: String): Result
}