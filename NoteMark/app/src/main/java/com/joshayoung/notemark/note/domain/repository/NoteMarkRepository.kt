package com.joshayoung.notemark.note.domain.repository

import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.core.domain.Result

interface NoteMarkRepository {
    suspend fun register(username: String, email: String, password: String): Result
    suspend fun login(email: String, password: String): Result
    suspend fun createNote(title: String, body: String): Result
    suspend fun updateNote(note: Note?, title: String, body: String): Result
    suspend fun getNotes(): Result
    suspend fun deleteNote(id: String): Result
}