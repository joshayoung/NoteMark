package com.joshayoung.notemark.note.domain.repository

import com.joshayoung.notemark.core.data.networking.DataError
import com.joshayoung.notemark.core.data.networking.EmptyDataResult
import com.joshayoung.notemark.core.data.networking.EmptyResult
import com.joshayoung.notemark.core.data.networking.Result
import com.joshayoung.notemark.note.domain.models.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun createNote(
        title: String,
        body: String,
    ): Result<Note, DataError>

    suspend fun updateNote(
        note: Note?,
        title: String,
        body: String,
    ): Result<Note, DataError>

    fun getNotes(): Flow<List<Note>>

    suspend fun getNote(id: Long): Note?

    suspend fun deleteNote(note: Note): EmptyResult<DataError>

    suspend fun logout(): EmptyDataResult<DataError.Network>

    suspend fun removeAllNotes()
}