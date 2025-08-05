package com.joshayoung.notemark.note.domain.repository

import com.joshayoung.notemark.note.domain.models.Note
import kotlinx.coroutines.flow.Flow

typealias Result = com.joshayoung.notemark.core.domain.models.Result

interface NoteRepository {
    suspend fun createNote(note: Note): Result
    suspend fun updateNote(note: Note?, title: String, body: String): Result
    suspend fun getNotes(): Flow<List<Note>>
    suspend fun deleteNote(id: Int): Result
    suspend fun getNote(id: Int) : Note?
}