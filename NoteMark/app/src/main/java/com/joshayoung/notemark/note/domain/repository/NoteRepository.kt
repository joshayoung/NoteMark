package com.joshayoung.notemark.note.domain.repository

import com.joshayoung.notemark.core.domain.util.DataError
import com.joshayoung.notemark.core.domain.util.EmptyDataResult
import com.joshayoung.notemark.core.domain.util.EmptyResult
import com.joshayoung.notemark.note.domain.models.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun createNote(title: String, body: String): EmptyDataResult<DataError>
    suspend fun updateNote(note: Note?, title: String, body: String): EmptyResult<DataError>
    suspend fun getNotes(): Flow<List<Note>>
    suspend fun deleteNote(id: Int)
    suspend fun getNote(id: Int) : Note?
}