package com.joshayoung.notemark.note.domain.repository

import com.joshayoung.notemark.core.domain.util.DataError
import com.joshayoung.notemark.core.domain.util.EmptyDataResult
import com.joshayoung.notemark.core.domain.util.EmptyResult
import com.joshayoung.notemark.core.domain.util.Result
import com.joshayoung.notemark.note.domain.models.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun createNote(title: String, body: String): Result<Note, DataError>
    suspend fun updateNote(note: Note?, title: String, body: String): Result<Note, DataError>
    suspend fun getNotes(): Flow<List<Note>>
    suspend fun getNote(id: Long) : Note?
    suspend fun deleteNote(note: Note) : EmptyResult<DataError>
}