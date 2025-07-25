package com.joshayoung.notemark.domain.repository

import com.joshayoung.notemark.domain.models.Note
import com.joshayoung.notemark.domain.models.Result

interface NoteMarkRepository {
    suspend fun register(username: String, email: String, password: String): Result
    suspend fun login(email: String, password: String): Result
    suspend fun createNote(title: String, body: String): Result
    suspend fun updateNote(note: Note): Result
    suspend fun getNotes(note: Note): Result
    suspend fun deleteNote(note: Note): Result
}