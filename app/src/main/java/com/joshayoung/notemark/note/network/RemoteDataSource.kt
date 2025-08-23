package com.joshayoung.notemark.note.network

import com.joshayoung.notemark.core.domain.util.DataError
import com.joshayoung.notemark.core.domain.util.EmptyDataResult
import com.joshayoung.notemark.core.domain.util.Result
import com.joshayoung.notemark.note.domain.models.Note

interface RemoteDataSource {
    suspend fun createNote(note: Note): Result<Note, DataError.Network>

    suspend fun getNotes(): Result<List<Note>, DataError.Network>

    suspend fun updateNote(note: Note): EmptyDataResult<DataError.Network>

    suspend fun deleteNote(id: String?): EmptyDataResult<DataError.Network>
}